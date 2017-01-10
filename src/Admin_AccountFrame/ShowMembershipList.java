package Admin_AccountFrame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import all_DTO.MembershipDTO;

public class ShowMembershipList {

	private SetDBconnection DBconnection;
	private Connection conn;
	private Statement stmt;		
	private ResultSet rs;	
	
	private ArrayList<MembershipDTO> alist;
	
	public ShowMembershipList() {
		
	}

	public void make_list(MembershipManagementJFrame jpanel) {
				
		DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
		
		jpanel.rowData_length = search();
		printTotable(jpanel);
		
		DBconnection.disConnection();
	}
	
	private void printTotable(MembershipManagementJFrame jpanel) {
		
		for(int i = 0; i < alist.size(); i++)
		{
			jpanel.rowData[i][0] = alist.get(i).getMember_number();
			jpanel.rowData[i][1] = alist.get(i).getMember_id();
			jpanel.rowData[i][2] = alist.get(i).getName();
			jpanel.rowData[i][3] = alist.get(i).getPhone_number();
			jpanel.rowData[i][4] = alist.get(i).getMinutes();
		}
	}
	
	public int search() {
		
		//100
		alist = new ArrayList<MembershipDTO>(100);
		alist.clear();
		
		String sql = "select * \n";
		sql += "from customer \n";
		
		try {
			stmt = conn.createStatement();			
			rs = stmt.executeQuery(sql);

			/*
				private int member_number;
				private String member_id;
				private String password;
				private String name;
				private int age;
				private String phone_number;
				private int auth;
				private int seat;
				private int minutes;
			 */			
			
			while(rs.next())
			{
				MembershipDTO dto = new MembershipDTO();
				dto.setMember_number(rs.getInt(1));
				dto.setMember_id(rs.getString(2));
				dto.setPassword(rs.getString(3));
				dto.setName(rs.getString(4));
				dto.setAge(rs.getInt(5));
				dto.setPhone_number(rs.getString(6));
				dto.setAuth(rs.getInt(7));
				dto.setSeat(rs.getInt(8));
				dto.setMinutes(rs.getInt(9));
				
				alist.add(dto);
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
					
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return alist.size();
	}
	
}
