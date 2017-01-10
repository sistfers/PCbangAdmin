package Admin_starts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Admin_dbfiles.DBConnectionMgr;

public class DB_query {
	public DB_query() {
	}
	
	// 로그인 메소드
	public static boolean loginMember(String id, String pass) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String hashPass = "hash_pass 초기값";
		boolean flag = false;
		DBConnectionMgr pool = null;

		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			con = pool.getConnection();
			sql = "select password from customer where member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			hashPass = rs.getString("password");

			if (pass.equals(hashPass) ){
				flag = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
}//DB_query클래스 종료
