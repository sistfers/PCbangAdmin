package Admin_viewHUD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

import Admin_AccountFrame.ChargeTabbedFrame;
import Admin_AccountFrame.DailySettlementJFrame;
import Admin_AccountFrame.MembershipManagementJFrame;
import Admin_Product.ProductFrame;
import Admin_starts.HostPcServer;
import Admin_starts.Vcontrol;


import Admin_views.Seat_panAb;

import Admin_views.Admin_OrderFrame;
import Admin_views.adb_Manage;
import _client_Boards.Board;

public class Manage_Fr_Hud extends adb_Manage implements ActionListener {
	private boolean stopped  = false;
	
	private static final long serialVersionUID = 1L;
	Vcontrol vcm;

	JPanel panel, pan_navi, pan_clock;
	public JButton bt[] = new JButton[8]; // 네비게이션 버튼 4개(화면, 회원, 재고, 매상)
	public JPanel seat50; // 50개 패널을 담기 위한 그릇
	int pX, pY;
	int x = 0, y = 0; // 좌표 계속 움직이게 해주는 x, y
	int sx = 77, sy = 0;

	public JPanel jpanel01 = null;
    public JPanel jpanel02 = null;
    public JPanel jpanel03 = null;
    
    HostPcServer hp;
    
    private JLabel labelsit;
    private JLabel labelid;
    private JLabel makeBy;
    private Font f1 = new Font("HY울릉도M", Font.BOLD, 20);
	
	JPopupMenu popup;
	JMenuItem allOnSeat, allOffSeat, turnOnSeat, turnOffSeat, calculSeat;
	JPanel pan_imgClock;
	ImageIcon image, image2, image3;
	ImageIcon img;

	
	 EtchedBorder eborder;
	 BevelBorder border;
	JLabel label[] = new JLabel[10];
	
	
	public Manage_Fr_Hud() {
		vcm =  Vcontrol.getInstance();
		// 프레임 초기 설정
		setSize(1600, 900);
		setTitle("씨스트마Pc방 관리프로그램 ver.1.01");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setBackground(Color.BLACK);
		// 프레임 초기 설정
        
		// 프레임 화면 중앙 배열
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		// 프레임 화면 중앙 배열
		
		// 가장 큰 JLayer패널= 레이어를 순서대로 올려줌
		JLayeredPane lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1600, 900);
		lpane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		lpane.setLayout(null);
		// 가장 큰 JLayer패널= 레이어를 순서대로 올려줌
		
		// 배경패널
		panel = new MyPanel("mainHud_back.png");
		panel.setLayout(null);
		panel.setBounds(0, -30, 1600, 900);
		// 배경패널
		
		// 시계패널
		pan_imgClock = new MyPanel2();
		pan_imgClock.setLayout(null);
		pan_imgClock.setBounds(15, 20, 179, 149);
		pan_imgClock.setOpaque(false);
		// 시계패널
		
		// 시계글씨패널
		pan_clock = new MyClock();
		pan_clock.setBounds(80, 53, 100, 100);
		pan_clock.setOpaque(false); // 이걸 해줘야 뒤에가 투명해진다.
		// 시계글씨패널
		
		// 왼쪽위 가동률정보패널@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBounds(110,60,275,340);
		panel1.setBorder(new EtchedBorder(EtchedBorder.RAISED));
	    panel1.setOpaque(false);
	      
	    progress.setUI(new ProgressCircleUI());
	    progress.setStringPainted(true);
	    progress.setFont(f1);
	    progress.setOpaque(false);
	    progress.setBorder(new EtchedBorder(EtchedBorder.RAISED));
	    progress.setValue(0);
	    progress.setBounds(5, 5, 265, 265);
	    
	    makeBy = new JLabel("<html>MADE BY<br>전창건<br>김태영, 박성우, 배한주, 조영숙</html>",JLabel.CENTER);
	    makeBy.setFont(new Font("HY울릉도M", Font.BOLD, 15));
	    
	    makeBy.setBounds(5, 275, 270, 60);
	    makeBy.setForeground(Color.white);
	       
	    panel1.add(progress);
	    panel1.add(makeBy);
		// 왼쪽위 가동률정보패널@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		
		// 왼쪽아래 좌석정보패널@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		JPanel panel11 = new JPanel();
		panel11.setBounds(110,440,275,340);
		eborder=new EtchedBorder(EtchedBorder.RAISED);
		border=new BevelBorder(BevelBorder.RAISED);
		jpanel02 = panel11;
		
		panel11.setLayout(null);
		panel11.setOpaque(false);
		
		ImageIcon image1 = new ImageIcon(getClass().getClassLoader().getResource("labelImg_80.png"));  //이미지 경로
		
		Font f = new Font("HY울릉도M", Font.BOLD, 20);  
		
		labelsit = new JLabel("SEAT",image1,JLabel.CENTER);
		labelsit.setFont(f);
		labelsit.setVerticalTextPosition(JLabel.CENTER);
		labelsit.setHorizontalTextPosition(JLabel.CENTER);
		labelsit.setForeground(Color.white); 
		labelsit.setBounds(0, 0, 135, 80);
		labelsit.setBackground(Color.GRAY);
		labelsit.setHorizontalAlignment(JLabel.CENTER);
		labelsit.setOpaque(true);
		

		labelid = new JLabel("",image1,JLabel.CENTER);
		labelid.setFont(f);
		labelid.setVerticalTextPosition(JLabel.CENTER);
		labelid.setHorizontalTextPosition(JLabel.CENTER);
		labelid.setForeground(Color.white); 
		labelid.setBounds(140, 0, 135, 80);
		labelid.setBackground(Color.GRAY);
		labelid.setHorizontalAlignment(JLabel.CENTER);
		labelid.setOpaque(true);
		
		panel11.setLayout(null);
		panel11.setBackground(Color.white);
		panel11.setOpaque(false);
		
		ImageIcon image2 = new ImageIcon(getClass().getClassLoader().getResource("labelImg_50.png"));  //이미지 경로
		ImageIcon nameImage = new ImageIcon(getClass().getClassLoader().getResource("name.png"));  //이미지 경로
		ImageIcon ageImage = new ImageIcon(getClass().getClassLoader().getResource("age.png"));  //이미지 경로
		ImageIcon startImage = new ImageIcon(getClass().getClassLoader().getResource("startingtime.png"));  //이미지 경로
		ImageIcon usingImage = new ImageIcon(getClass().getClassLoader().getResource("usingtime.png"));  //이미지 경로
		ImageIcon totalImage = new ImageIcon(getClass().getClassLoader().getResource("total.png"));  //이미지 경로
		Font f1 = new Font("HY울릉도M", Font.BOLD, 20);  
		
		int lx1 = 0, ly1 = 90, lx2 = 135,ly2 = 50;
		
		
			
		label[0] = new JLabel("",nameImage, JLabel.CENTER);
		label[0].setFont(f1);
		label[0].setVerticalTextPosition(JLabel.CENTER);
		label[0].setHorizontalTextPosition(JLabel.CENTER);
		label[0].setForeground(Color.white); 
		label[0].setHorizontalAlignment(JLabel.CENTER);
		label[0].setBounds(lx1, ly1, lx2, ly2);
		
		label[1] = new JLabel("",image2,JLabel.CENTER);
		label[1].setFont(f1);
		label[1].setVerticalTextPosition(JLabel.CENTER);
		label[1].setHorizontalTextPosition(JLabel.CENTER);
		label[1].setForeground(Color.white); 
		label[1].setBounds(lx1 + 140, ly1, lx2, ly2);
		
		
		label[2] = new JLabel("",ageImage,JLabel.CENTER);
		label[2].setFont(f1);
		label[2].setVerticalTextPosition(JLabel.CENTER);
		label[2].setHorizontalTextPosition(JLabel.CENTER);
		label[2].setForeground(Color.white); 
		label[2].setBounds(lx1, ly1+ 50, lx2, ly2);
		
		label[3] = new JLabel("",image2,JLabel.CENTER);
		label[3].setFont(f1);
		label[3].setVerticalTextPosition(JLabel.CENTER);
		label[3].setHorizontalTextPosition(JLabel.CENTER);
		label[3].setForeground(Color.white); 
		label[3].setBounds(lx1 + 140, ly1+ 50, lx2, ly2);
		
		
		label[4] = new JLabel("",startImage,JLabel.CENTER);
		label[4].setFont(f1);
		label[4].setVerticalTextPosition(JLabel.CENTER);
		label[4].setHorizontalTextPosition(JLabel.CENTER);
		label[4].setForeground(Color.white); 
		label[4].setBounds(lx1, ly1+ 100, lx2, ly2);
		
		
		label[5] = new JLabel("",image2,JLabel.CENTER);
		label[5].setFont(f1);
		label[5].setVerticalTextPosition(JLabel.CENTER);
		label[5].setHorizontalTextPosition(JLabel.CENTER);
		label[5].setForeground(Color.white); 
		label[5].setBounds(lx1 + 140, ly1+ 100, lx2, ly2);
		
		label[6] = new JLabel("",usingImage,JLabel.CENTER);
		label[6].setFont(f1);
		label[6].setVerticalTextPosition(JLabel.CENTER);
		label[6].setHorizontalTextPosition(JLabel.CENTER);
		label[6].setForeground(Color.white); 
		label[6].setBounds(lx1, ly1+ 150, lx2, ly2);
		
		label[7] = new JLabel("",image2,JLabel.CENTER);
		label[7].setFont(f1);
		label[7].setVerticalTextPosition(JLabel.CENTER);
		label[7].setHorizontalTextPosition(JLabel.CENTER);
		label[7].setForeground(Color.white); 
		label[7].setBounds(lx1 + 140, ly1+ 150, lx2, ly2);
		
		label[8] = new JLabel("",totalImage,JLabel.CENTER);
		label[8].setFont(f1);
		label[8].setVerticalTextPosition(JLabel.CENTER);
		label[8].setHorizontalTextPosition(JLabel.CENTER);
		label[8].setForeground(Color.white);
		label[8].setBounds(lx1, ly1+ 200, lx2, ly2);
		
		label[9] = new JLabel("",image2,JLabel.CENTER);
		label[9].setFont(f1);
		label[9].setVerticalTextPosition(JLabel.CENTER);
		label[9].setHorizontalTextPosition(JLabel.CENTER);
		label[9].setForeground(Color.white);
		label[9].setBounds(lx1 + 140, ly1 + 200 , lx2, ly2);
		
		
		for (int i = 0 ,j = 1; i < 10; i=i+2,j = j+2) {
		//label[j].setBorder(eborder);
		//label[j].setBackground(Color.GRAY);
		label[j].setOpaque(false);
		
		//label[i].setBorder(border);
		//label[i].setBackground(Color.GRAY);
		label[i].setOpaque(false);
		
		panel11.add(label[j]);
		panel11.add(label[i]);
		}
		panel11.add(labelid);
		panel11.add(labelsit);
		// 왼쪽아래 좌석정보패널 끝@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		


		// 버튼패널생성@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		pan_navi = new JPanel();
		pan_navi.setLayout(null);
		pan_navi.setBounds(390, 50, 1136, 50);
		pan_navi.setOpaque(false);
		// 버튼패널생성 끝@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		
		
		// 버튼생성@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		x = 0;
		
		
		
		for (int i = 0; i < 8; i++) {
			bt[i] = new JButton(new ImageIcon(getClass().getClassLoader().getResource("bt"+ i +".png")));
			bt[i].setBorderPainted(false);
			bt[i].setFocusPainted(false);
			bt[i].setBounds(x, 10, 142, 40);
			bt[i].setContentAreaFilled(false);
			bt[i].addActionListener(this);
			x += 142;
			pan_navi.add(bt[i]);
		}
		
		
		
		
		bt[0].setText("컴퓨터on/off");
		bt[1].setText("주문내역");
		bt[2].setText("회원관리");
		bt[3].setText("메뉴판");
		bt[4].setText("당일매출");
		bt[5].setText("정산");
		bt[6].setText("게시판");
		bt[7].setText("끝내기");
		// 버튼생성 끝@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		//좌석패
		// 좌석 패널 시작 시작점 390 280  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		seat50 = new JPanel();
		seat50.setLayout(null);
		
		seat50.setOpaque(false);
		seat50.setBounds(405, 115, 1120, 665);
		x = 0;
		y = 0;
		
		for (int i = 0; i < 50; i++) {
			pan[i] = new Seat_pan(i);
			if (i % 10 == 0 && i != 0) {
				x = 0;
				y += 133;
			}
			
			pan[i].setBounds(x, y, 112, 133);
			pan[i].x = x + 390;
			pan[i].y = y + 280 + 30;
			x += 112;
		}
		// 좌석 패널  끝  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		
		// 셀렉트패널영역@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		SelectPanel sPanel = new SelectPanel();
		sPanel.setBounds(0, -30, 1600, 900);
		sPanel.setForeground(new Color(36, 205, 198));
		sPanel.setOpaque(false);

		MyThread11  th1 = new MyThread11(pan);
		
		th1.start( );
        
		// 마지막 붙이기
		lpane.add(panel, new Integer(0), 0);
		lpane.add(pan_imgClock, new Integer(4), 0);
		lpane.add(pan_clock, new Integer(5), 0); // 시계패널은 최상단
		lpane.add(panel11, new Integer(6), 0);
		lpane.add(seat50, new Integer(7), 0);
		lpane.add(sPanel, new Integer(0), 0);
		lpane.add(panel1, new Integer(0), 0);
		lpane.add(pan_navi, new Integer(8), 0);
		
		getContentPane().add(lpane);
		setVisible(true);

		// 좌석 액션 후후.
		Thread th = new MyThread(1);
		th.start( );

		// 화면 버튼 구현하기
		/** 여기서부터는 오른쪽 버튼 구현~ */
		popup = new JPopupMenu();
		allOnSeat = new JMenuItem("전체켜기");
		allOffSeat = new JMenuItem("전체끄기");
		allOnSeat.addActionListener(this);
		allOffSeat.addActionListener(this);
		
		popup.add(allOnSeat);
		popup.add(allOffSeat);
	

	}

	// 이미지 그리기 위한 마이패널
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
		ImageIcon image;
		
		MyPanel(String img) {
			image = new ImageIcon(getClass().getClassLoader().getResource(img));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image != null) {
				g.drawImage(image.getImage(), 0, 0, this);
			}
		}

		public void update(Graphics g) {
			paintComponent(g);
		}

	}// 마이패널 종료

	public void reimg() {
		repaint();
	}
	
	//클릭시왼쪽아래 회원정보 뜨는스레드@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	class MyThread11 extends Thread {
	      public Seat_panAb pan[]=new Seat_panAb[50]; 
	      int ch = 0;
	      int chAll = 0;
	      int pick = 0;
	      MyThread11(Seat_panAb[] pan) {
	         this.pan = pan;
	      }

	      public void run( ) {
	         while(!stopped){
	            chAll = 0;
	            for(int i= 0; i < pan.length; i++){
	               if(pan[i].isChecked){
	                  chAll++;
	                  if(ch == 0){
	                     ch++;
	                     pick = i;
	                  }else if(ch == 1){
	                     if(!(pick == i)){
	                        pan[pick].checkOff();
	                     }
	                     pick = i;
	                  }else{
	                     ch--;
	                     pan[pick].checkOff();
	                     pick = i;
	                  }
	               }
	            }
	            if(chAll == 0){
	            	if(ch==1){
	            	   labelsit.setText("SEAT");
	            	   labelid.setText("");
	            	   label[9].setText("");
	            	   label[7].setText("");
	            	   label[5].setText("");
	            	   label[1].setText("");
	            	   label[3].setText("");
	            	}
	            }else {
	            	labelid.setText((pick+1)+"");
	            	//labelid.setText(pan[pick].nickname);
	            	label[9].setText(Integer.toString(pan[pick].totalmoney));
	            	label[7].setText(pan[pick].gametimes);
	            	label[5].setText(pan[pick].starttimes);
	            	label[1].setText(pan[pick].realname);
	            	label[3].setText(String.valueOf(pan[pick].age));
	            }
	         }
	      }
	   }
	//클릭시왼쪽아래 회원정보 뜨는스레드 끝@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//시계이미지쓰레드 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	@SuppressWarnings("serial")
	class MyPanel2 extends JPanel {

		int i=2;
		
		MyPanel2() {
			
			
			image = new ImageIcon(getClass().getClassLoader().getResource("cl1.png"));
			image2 = new ImageIcon(getClass().getClassLoader().getResource("cl2.png"));
			image3 = new ImageIcon(getClass().getClassLoader().getResource("cl3.png"));
			img = image;
			
			Thread thread = new ClockRoThread();
			thread.start( );

		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (img != null) {
				g.drawImage(img.getImage(), 0, 0, this);
			}
		}

		class ClockRoThread extends Thread {
			public void run( ) {

				try {
					while (!stopped) {
						Thread.sleep(1000);
						switch (i) {
						case 1:
							img = image;
							i = 2;
							pan_imgClock.repaint();
							break;
						case 2:
							img = image2;
							i = 3;
							pan_imgClock.repaint();
							break;
						case 3:
							img = image3;
							i = 1;
							pan_imgClock.repaint();
							break;
						}

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}
	//시계이미지쓰레드 끝 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//드래그@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	// 선택영역 그리기 위한 패널
	@SuppressWarnings("serial")
	class SelectPanel extends JPanel implements MouseMotionListener,MouseListener {
		int x, y, pX, pY, iX, iY;

		
		
		SelectPanel() {
			addMouseListener(this);
			addMouseMotionListener(this);
			setFocusable(true);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.draw3DRect(x, y, pX - x, pY - y, false);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			pX = e.getX();
			pY = e.getY();
			repaint();
		}

		public void mousePressed(MouseEvent e) {
			x = e.getX();
			iX = e.getX();
			y = e.getY();
			iY = e.getY();
			pX = e.getX();
			pY = e.getY();
		}

		public void mouseReleased(MouseEvent e) {
			for (int i = 0; i < 50; i++) {
				if (x < pan[i].x && pan[i].x < pX && y < pan[i].y
						&& pan[i].y < pY)
					pan[i].checkOn();
			}

			x = 0;
			y = 0;
			pX = 0;
			pY = 0;
			repaint();
		}
		
		

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
	
	//progress bar circle UI
    class ProgressCircleUI extends BasicProgressBarUI {
       @Override public Dimension getPreferredSize(JComponent c) {
           Dimension d = super.getPreferredSize(c);
           int v = Math.max(d.width, d.height);
           d.setSize(v+80, v+80);
           return d;
         }
         @Override
         public void paint(Graphics g, JComponent c) {
            Insets b = progressBar.getInsets(); // area for border
            int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
            int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
            if (barRectWidth <= 0 || barRectHeight <= 0) {
               return;
            }
          
            // draw the cells
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new Color(255 , 255 , 255));
            double degree = 360 * progressBar.getPercentComplete();
            double sz = Math.min(barRectWidth, barRectHeight);
            double cx = b.left + barRectWidth  * .5;
            double cy = b.top  + barRectHeight * .5;
            double or = sz * .5;
            double ir = or * .5; //or - 20;
            Shape inner = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
            Shape outer = new Arc2D.Double(
              cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);
            Area area = new Area(outer);
            area.subtract(new Area(inner));
            g2.fill(area);
            g2.dispose();
          
            // Deal with possible text painting
            if (progressBar.isStringPainted()) {
               paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
            }
         }
       @Override
       protected Color getSelectionForeground() {
          return Color.WHITE;
       }
       @Override
       protected Color getSelectionBackground() {
          return Color.WHITE;
       }
         
         
    }

	// 좌석 쓰레드! 1일 경우 좌석, 2일경우 꺼짐켜짐으로 사용된다.
	//좌석@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	// 값이 1이면 좌석생성 2이면 좌석 전부켜기 3이면 좌석 전부 끄기
	class MyThread extends Thread {
		int i;

		MyThread(int i) {
			this.i = i;
		}

		public void run( ) {
			Set<Integer> hs = null;
			if (i == 1) {
				hs = new LinkedHashSet<Integer>();
				for (; hs.size() < 50;) {
					int x = (int) ((Math.random() * 50));
					hs.add(x);
				}
			} else {
				hs = new HashSet<Integer>();
				for (int a = 0; a < 50; a++)
					hs.add(a);
			}
			try {
				int tmp=0;
				for (Integer s : hs) {

					if (i == 1)
						Thread.sleep(1);
					else {
						Thread.sleep(1);
					}

					switch (i) {
					case 1:
						tmp++;
						if(tmp>30)
							//Thread.sleep(s*10 -(s*5));
						if(tmp==50)
						{
						}
							
						seat50.add(pan[s]);
						
						break;
					case 2:
						vcm.turnOn(s);
						break;
					case 3:
						vcm.turnOff(s);
						break;
					}
					repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//좌석 끝@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//시계갱신스레드@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	@SuppressWarnings("serial")
	class MyClock extends JPanel {
		Calendar ctoday = Calendar.getInstance();
		int i = ctoday.get(Calendar.AM_PM);
		String[] ampm = { "AM", "PM" };
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String time = sdf.format(today);
		JLabel timeLabel;
		JLabel ampmLabel;

		public MyClock() {
			this.setLayout(null);
			
			timeLabel = new JLabel(time);
			timeLabel.setBounds(0, 0, 100, 20);
			timeLabel.setForeground(new Color(36, 205, 198));
			timeLabel.setFont(new Font("배달의민족 한나", Font.BOLD, 12));
			ampmLabel = new JLabel(ampm[i]);
			ampmLabel.setBounds(15, 20, 100, 30);
			ampmLabel.setForeground(new Color(36, 205, 198));
			ampmLabel.setFont(new Font("배달의민족 한나", Font.BOLD, 12));

			add(timeLabel, BorderLayout.NORTH);
			add(ampmLabel, BorderLayout.CENTER);
			Thread thread = new MyClockThread();
			thread.start( );
		}

		class MyClockThread extends Thread {
			public void run( ) {
				while (!stopped) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					today = new Date();
					time = sdf.format(today);
					timeLabel.setText(time);
				}

			}
		}
	}
//시계갱신스레드 끝 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	//버튼 액션@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	@Override
	// 액션퍼폼
	public void actionPerformed(ActionEvent e) {
		// 팝업메뉴나오게
		if (e.getSource() == bt[0]) {
			popup.show(Manage_Fr_Hud.this, 420, 100);
			// 올온!
		} else if (e.getSource() == allOnSeat) {
			Thread seatThread = new MyThread(2);
			seatThread.start( );
			// 올오프!
		} else if (e.getSource() == allOffSeat) {
			Thread seatThread = new MyThread(3);
			seatThread.start( );
			
		}  else if (e.getSource() == bt[1]) {// 주문내역
			if(vcm.takeFrame == null){
	            vcm.takeFrame = new Admin_OrderFrame(vcm.orderDao);
	         }else{
	            vcm.takeFrame.tablePrint(vcm.orderDao);
	            vcm.takeFrame.setVisible(true);
	         }

			
		} else if (e.getSource() == bt[2]) {// 회원관리
			new MembershipManagementJFrame();

			
		} else if (e.getSource() == bt[3]) {// 메뉴판
			new ProductFrame();
			
			
		} else if (e.getSource() == bt[4]) { // 당일매출
			new ChargeTabbedFrame();
			
			
		} else if (e.getSource() == bt[5]) {// 정산
			new DailySettlementJFrame();
			
		}else if (e.getSource() == bt[6]) {//게시판
			new Board();
			
			
			
		}else if (e.getSource() == bt[7]){//끝내기
			System.exit(0);
			
		}
		//버튼 액션 끝 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			

	}
	public void stop() {
        stopped = true;
  }
}// 클래스 종료
