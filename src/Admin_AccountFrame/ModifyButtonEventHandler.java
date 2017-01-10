package Admin_AccountFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ModifyButtonEventHandler implements ActionListener {
	
	private MembershipManagementJFrame membership_ManagementJPanel;
	
	public ModifyButtonEventHandler(MembershipManagementJFrame membership_ManagementJPanel) {
		
		this.membership_ManagementJPanel = membership_ManagementJPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(membership_ManagementJPanel.selected_row != 0)
			new MiniJframe((String)membership_ManagementJPanel.
					rowData[membership_ManagementJPanel.selected_row][1]);
		
		membership_ManagementJPanel.renew(); 
	}

}

class MiniJframe extends JFrame implements ActionListener {
	
	private Dimension dim;
	
	private SetDBconnection DBconnection;
	private Connection conn;
	private Statement stmt;		
	
	private JLabel pass;
	private JTextField password;
	private JButton pbutton;
	
	private String id;
	
	MiniJframe(String id) {
		setTitle("회원 정보 수정");
		this.id = id;
	
		setLayout(null);				
		setBounds(0, 0, 400, 300);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width/2)-(this.getWidth()/2)+400, (dim.height/2)-(this.getHeight()/2)-300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		pass = new JLabel("변경할 패스워드");
		pass.setBounds(150, 50, 100, 30);
		password = new JTextField();
		password.setBounds(150, 100, 100, 30);
		pbutton = new JButton("확인");
		pbutton.setBounds(150, 150, 100, 30);
		
		pbutton.addActionListener(this);
		
		add(pass);
		add(password);
		add(pbutton);
	
		setResizable(false);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		update(id, password.getText());
		
		dispose();
	}
	
	public void update(String seleted_id, String change_pass) {
		DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
		
		int cnt = 0;
				
		String sql = "update customer set password = '" + change_pass + "' \n";
		sql += " where member_id = '" + seleted_id + "'";
		
		try {
			stmt = conn.createStatement();
			cnt = stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
					
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}		
	}
	
}
