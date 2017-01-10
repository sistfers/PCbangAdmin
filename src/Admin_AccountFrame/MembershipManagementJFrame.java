package Admin_AccountFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MembershipManagementJFrame extends JFrame implements MouseListener {
	
	private JButton modify_memberPassword;
	private JButton delete_member;
	
	public Object rowData[][];	
	private String columnNames[] = {
			"회원번호", "아이디", "이름", "전화번호", "남은 시간"
	};
	
	private JTable MemberTable;
	private JScrollPane jScrPane;
	public int selected_row;
	public int rowData_length;
	
	private Dimension dim;
	
	public MembershipManagementJFrame() {
		setTitle("회원 관리");
		setLayout(null);				
		setBounds(0, 0, 720, 576);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		modify_memberPassword = new JButton("수정");
		delete_member = new JButton("삭제");		
		
		modify_memberPassword.setBounds(250, 500, 100, 30);
		delete_member.setBounds(350, 500, 100, 30);
		
		modify_memberPassword.addActionListener(new ModifyButtonEventHandler(this));
		delete_member.addActionListener(new DeleteButtonEventHandler(this));
		
		rowData = new Object[50][5];		
		MemberTable = new JTable(rowData, columnNames);
		MemberTable.addMouseListener(this);
				
		jScrPane = new JScrollPane(MemberTable); 
		jScrPane.setBounds(0, 0, 715, 476);		
				
		add(jScrPane);
		add(modify_memberPassword);
		add(delete_member);	
		
		ShowMembershipList membership_list = new ShowMembershipList();
		membership_list.make_list(this);
		
		setResizable(false);
		setVisible(true);
	}	
	
	public void renew() {
				
		MemberTable.invalidate();
		MemberTable.validate();
		MemberTable.repaint();		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		selected_row = MemberTable.getSelectedRow();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}	

}
