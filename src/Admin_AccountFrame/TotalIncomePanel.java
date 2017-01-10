package Admin_AccountFrame;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Admin_dbfiles.DBConnectionMgr;
import Admin_starts.Vcontrol;

public class TotalIncomePanel extends JPanel implements ActionListener {
	private JTable jTable;
	private JScrollPane jScrPane;
	private DefaultTableModel model;
	
	private ChargeTabbedFrame chargeTabbedFrame;
	
	String columnNames[] = {
			"상품판매수익", "pc판매수익", "총 수익"	
		};
	
	Object rowData[][];
	
	public TotalIncomePanel(ChargeTabbedFrame chargeTabbedFrame) {
		this.chargeTabbedFrame = chargeTabbedFrame;
		setLayout(null);
		setBounds(100, 100, 640, 480);
		
		
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		rowData = new Object[1][3];
		
		rowData[0][0] = ProTotalIncome();
		rowData[0][1] = PcFeeTotalIncome(); 
		rowData[0][2] = ProTotalIncome() + PcFeeTotalIncome();
			
		model = new DefaultTableModel(rowData,  columnNames);
		jTable = new JTable(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		jTable.getColumnModel().getColumn(0).setMaxWidth(300);
		jTable.getColumnModel().getColumn(1).setMaxWidth(300);
		jTable.getColumnModel().getColumn(2).setMaxWidth(300);
		
		jTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

		jScrPane = new JScrollPane(jTable);
		jScrPane.setBounds(0, 0, 640, 350);
		add(jScrPane);
		
		JButton SendBtn = new JButton("일일정산");
		SendBtn.setBounds(250, 355, 90, 50);
		SendBtn.addActionListener(this);
		SendBtn.setVisible(true);
		
		add(SendBtn);
		
		Font f1 = new Font("HY울릉도M", Font.PLAIN, 20);
		
		setVisible(true);
	}
	
	private int ProTotalIncome(){
		Vcontrol vc = Vcontrol.getInstance();
		int sum = 0;
		vc.salesHisDao.list = vc.salesHisDao.loadAll();
		
		for(int i = 0; i< vc.salesHisDao.list.size(); ++i){
			sum += vc.salesHisDao.list.get(i).getTotal_income();
		}
		
		return sum;
	}
	
	private int PcFeeTotalIncome(){
		DBConnectionMgr pool = DBConnectionMgr.getInstance();
		int sum = 0;
		Vcontrol vc = Vcontrol.getInstance();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		String sql = "select * \n";
		sql += " from fee_history \n";
		
		try {
			conn = pool.getConnection();
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
					
			
			while(rs.next())
			{
				sum += rs.getInt(3);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(conn, psmt, rs);
		}
		return sum;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		SetDBconnection DBconnection;
		Connection conn = null;
		PreparedStatement stmt;
		int count = 0;
		
		DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
		
		/* insert
		sysdate
		rowData[0][0]	상품판매수익
		rowData[0][1] 	pc판매수익
		*/
		
		String sql = "insert into calculate(calculate_date, hours_income, product_income) "
				+ "values(sysdate, ?, ?)"; 
		
		String mi = " select minutes from fee_history ";
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, (int)rowData[0][1]); 
			stmt.setInt(2, (int)rowData[0][0]);
			
			count = stmt.executeUpdate();
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		if(count == 1)
		{
			// 테이블 초기화
			sql = "truncate table fee_history ";
			
			try {
				stmt = conn.prepareStatement(sql);
				sql = "truncate table sales_history ";
				stmt.executeUpdate();
				
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(null, "정산되었습니다.");
			
			chargeTabbedFrame.dispose();
			
		}
	}
	
}
