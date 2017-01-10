package Admin_DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Admin_dbfiles.DBConnectionMgr;
import all_DTO.SalesHisDTO;

public class SalesHisDAO {
	public List<SalesHisDTO> list = null;
	
	public SalesHisDAO() {
		list = new ArrayList<SalesHisDTO>();
	}
	
	public List<SalesHisDTO> loadAll(){
		String sql = "select * \n"
				+ "from SALES_HISTORY \n";
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		DBConnectionMgr pool = null;
		
		try {
			pool = DBConnectionMgr.getInstance();
			conn = pool.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			list.removeAll(list);
			while(rs.next()){
				SalesHisDTO d = new SalesHisDTO();
				d.setPro_type(rs.getString("pro_type"));
				d.setPro_name(rs.getString("pro_name"));
				d.setPrice(rs.getInt("price"));
				d.setSale_quantity(rs.getInt("Sale_quantity"));
				d.setTotal_income(rs.getInt("total_income"));
				list.add(d);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			pool.freeConnection(conn, stmt, rs);
		}
		return list;
	}
}
