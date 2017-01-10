package Admin_AccountFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteButtonEventHandler implements ActionListener {

	private SetDBconnection DBconnection;
	private Connection conn;
	private Statement stmt;		
	private int cnt = 0;
	
	private MembershipManagementJFrame membership_ManagementJPanel;
	
	public DeleteButtonEventHandler(MembershipManagementJFrame membershipManagementJPanel) {
		
		this.membership_ManagementJPanel = membershipManagementJPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
		
		cnt = delete();
		
		if(cnt == 1) {			
			
			for(int i = 0; i < membership_ManagementJPanel.rowData_length; i++)
			{
				membership_ManagementJPanel.rowData[i][0] = "";
				membership_ManagementJPanel.rowData[i][1] = "";
				membership_ManagementJPanel.rowData[i][2] = "";
				membership_ManagementJPanel.rowData[i][3] = "";
				membership_ManagementJPanel.rowData[i][4] = "";
			}
			
			new ShowMembershipList().make_list(membership_ManagementJPanel);
			membership_ManagementJPanel.renew();
		}
		else
			System.out.println("삭제 실패");
	}
	
	public int delete() {
		
		String id = (String)membership_ManagementJPanel.
				rowData[membership_ManagementJPanel.selected_row][1];
		
		
		String sql = "delete \n";
		sql += " from customer \n";
		sql += " where member_id = '" + id + "'";
		
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
					
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		return cnt;
	}

}
