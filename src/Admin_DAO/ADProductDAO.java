package Admin_DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Admin_dbfiles.DBConnectionMgr;
import all_DTO.RProductDTO;


public class ADProductDAO {
	
	public List<RProductDTO> list = null;
	public RProductDTO selPro = null;
	
	public ADProductDAO() {
		
		list = new ArrayList<RProductDTO>();
	}
	
	
	public List<RProductDTO> loadAll(){
		String sql = "select * \n"
				+ "from PRODUCT \n";
		
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
				RProductDTO d = new RProductDTO();
				d.setPro_type(rs.getString("pro_type"));
				d.setPro_name(rs.getString("pro_name"));
				d.setPrice(rs.getInt("price"));
				d.setQuantity(rs.getInt("quantity"));
				list.add(d);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			pool.freeConnection(conn, stmt, rs);
		}
		return list;
	}
	
	public boolean deleteBbs() {
		
		String sql = "delete PRODUCT \n"
				+ "where pro_name = ?";
		int count = 0;
		boolean suc = false;
		
		Connection conn = null;
		PreparedStatement pstm = null;
		
		DBConnectionMgr pool = null;
		
		try{
			pool = DBConnectionMgr.getInstance();
			conn = pool.getConnection();
			pstm = conn.prepareStatement(sql);
			
			pstm.setString(1, selPro.getPro_name());
			
			count = pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, pstm);
		}
		if(count >0){
			suc = true;
			selPro = null;
		}else{
			suc = false;
		}
		return suc;
	}
	
	public boolean add(RProductDTO dto) {
		list = loadAll();
		boolean suc;
		int count = 0;
		
		
		
		String sql = "insert into PRODUCT \n"
				+ "values(?, ?, ?, 100)\n";
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		DBConnectionMgr pool = null;
		
		try{
			pool = DBConnectionMgr.getInstance();
			conn = pool.getConnection();
			pstm = conn.prepareStatement(sql);
			
			pstm.setString(1, dto.getPro_type());
			pstm.setString(2, dto.getPro_name());
			pstm.setInt(3, dto.getPrice());
			
			count = pstm.executeUpdate();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pool.freeConnection(conn, pstm);
		}
		
		if(count > 0){
			suc = true;
		}else{
			suc = false;
		}
		return suc;
	}
}





















