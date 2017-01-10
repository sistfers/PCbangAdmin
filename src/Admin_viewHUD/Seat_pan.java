package Admin_viewHUD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import Admin_dbfiles.DBConnectionMgr;
import Admin_starts.Vcontrol;
import Admin_views.Seat_panAb;

public class Seat_pan extends Seat_panAb implements ActionListener,
		MouseListener {
	private static final long serialVersionUID = 1L;
	Vcontrol vcm = Vcontrol.getInstance("시트팬");
	ImageIcon img = null;
	JLayeredPane lpane;
	JPanel panel3;

	public Seat_pan(int i) {
		num = i;
		isChecked = false;
		img("gameOff");
		setLayout(null);

		// 제이레이어드 패널
		lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1600, 900);
		lpane.setLayout(null);
		lpane.setOpaque(false);
		// 이미지 패널
		JPanel panel = new InnerPanel();
		panel.setBounds(0, 0, 112, 133);
		panel.setOpaque(false);
		// 안에 들어갈 내용물들
		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setBounds(0, 0, 112, 133);

		int y = 15;
		for (int a = 0; a < 4; a++) {
			if (a == 0)
				label[a] = new JLabel((i + 1) + ". 빈자리");
			else
				label[a] = new JLabel("");

			label[a].setBounds(20, y, 80, 15);
			y += 16;
			label[a].setForeground(Color.BLACK);
			label[a].setFont(new Font("HY울릉도L", 1, 12));
			panel2.add(label[a]);
		}
		panel2.setOpaque(false);

		// 체크패널
		panel3 = new CheckPanel();
		panel3.setLayout(null);
		panel3.setBounds(0, 0, 99, 99);
		panel3.setOpaque(false);
		// 마지막 붙이기
		lpane.add(panel, new Integer(0), 0);
		lpane.add(panel2, new Integer(1), 0);

		add(lpane);
		setVisible(true);

		setOpaque(false);
		setFocusable(true);
		addMouseListener(this);
		/** 여기서부터는 오른쪽 버튼 구현~ */
		pMenu = new JPopupMenu();
		miEnd = new JMenuItem("정산");
		miEnd.addActionListener(this);
		miChat = new JMenuItem("메세지보내기");
		miChat.addActionListener(this);
		pMenu.add(miEnd);
		pMenu.add(miChat);
		// 패널에 마우스 리스너를 붙인다. JPopupMenu는 이런식으로 구현을 해야 한다..
		addMouseListener(new MousePopupListener());
	}


	public void img(String s) {
		// 이미지 받아오기 - turnOn, turnOff, gameOn, gameOff
		img = new ImageIcon(getClass().getClassLoader().getResource(s + ".png"));
	     repaint();
	}

	/** 이부분이 상태 체크 */
	public void turnOn() {
		img("gameOn");
		isTurned = true;
	}

	public void turnOff() {
		img("gameOff");
		isTurned = false;
	}

	public void checkOn() {
		lpane.add(panel3, new Integer(2), 0);
		this.isChecked = true;
		repaint();
	}

	@Override
	public void checkOff() {
		lpane.remove(panel3);
		this.isChecked = false;
		repaint();
	}

	// 이미지불러오는패널
	class InnerPanel extends JPanel {
		private static final long serialVersionUID = 1547128190348749556L;

		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(img.getImage(), 0, 0, null);
		}
	}

	// 체크패널
	@SuppressWarnings("serial")
	class CheckPanel extends JPanel {
		ImageIcon img_c;

		CheckPanel() {
			img_c = new ImageIcon(getClass().getClassLoader().getResource("check.png"));
		}

		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(img_c.getImage(), 0, 0, null);
		}
	}

	/** 여기서부터 액션처리 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		if (this.isChecked == false) {
			checkOn();

		} else if (this.isChecked == true) {
			checkOff();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@SuppressWarnings("static-access")
	@Override
	public void mousePressed(MouseEvent me) {
		if (me.getModifiers() == me.BUTTON3_MASK)
			pMenu.show(this, me.getX(), me.getY());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	// 팝업 : 로그아웃 정산메소드와 메세지를 손님께 메소드
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miEnd) {
			addPcFee();
			vcm.logout(num);
			
			
			JOptionPane.showMessageDialog(null, "계산을 끝냈습니다", "계산끝",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if (e.getSource() == miChat) {
			vcm.messageFromPC(num, "채팅을 시작합니다\n");
		} 
	}
	
	public void addPcFee(){
		DBConnectionMgr pool = DBConnectionMgr.getInstance();
		
		Vcontrol vc = Vcontrol.getInstance();
		Connection conn = null;
		PreparedStatement psmt = null;
		int count = 0;
		
		String sql = "insert into fee_history(fee_date, minutes, payment, id, seat) "
				+ "values(sysdate, ?, ?, ?, ?)"; //날짜, 시간, 요금, id, 좌석
		
		try {
			conn = pool.getConnection();
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, vc.mf.pan[num].gametimes);
			psmt.setInt(2, vc.pcseat[num].getMoney());
			psmt.setString(3, vc.mf.pan[num].nickname);
			psmt.setInt(4, vc.pcseat[num].getNum_seat());
			
			count = psmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, psmt);
		}
	}

	class MousePopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseClicked(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			checkPopup(e);
		}

		private void checkPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				pMenu.show(Seat_pan.this, e.getX(), e.getY());
			}
		}
	}

}
