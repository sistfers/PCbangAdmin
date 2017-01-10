package Admin_AccountFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import all_DTO.ChargeDTO;

public class ChargeHistoryJFrame extends JPanel{

	public Object rowData[][];
	private String columnNames[] = {
			"id", "좌석", "사용시간", "충전금액"
	};
	
	private JTable HistoryTable;
	private JScrollPane jScrPane; 	
	
	private DefaultTableModel model;
	
	private SetDBconnection DBconnection;
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	private ArrayList<ChargeDTO> alist = new ArrayList<ChargeDTO>(50);
	
	public ChargeHistoryJFrame() {
		
		
		setLayout(null);
		setBounds(0, 0, 640, 480);	
		
		show_history();
		
		rowData = new Object[alist.size()][4];		
		clearTotable();
		printTotable();
		
//		HistoryTable = new JTable(rowData, columnNames);
		model = new DefaultTableModel(rowData, columnNames);
		HistoryTable = new JTable(model);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		HistoryTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		HistoryTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		HistoryTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		HistoryTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);		
				
		jScrPane = new JScrollPane(HistoryTable); 
		jScrPane.setBounds(0, 0, 640, 480);		
				
		add(jScrPane);
		
		//setResizable(false);
		setVisible(true);
	}
	
	public void clearTotable() {
		
		for(int i = 0; i < alist.size(); i++)
		{
			rowData[i][0] = "";
			rowData[i][1] = "";
			rowData[i][2] = "";
			rowData[i][3] = "";
			
//			renew();
		}
	}
	
	public void printTotable() {
		for(int i = 0; i < alist.size(); i++)
		{
			rowData[i][0] = alist.get(i).getId();
			rowData[i][1] = alist.get(i).getSeat();
			rowData[i][2] = alist.get(i).getMinutes();
			rowData[i][3] = alist.get(i).getPayment();
		}
	}
	
	public void renew() {
		
		HistoryTable.invalidate();
		HistoryTable.validate();
		HistoryTable.repaint();		
	}
	
	public void show_history() {
		
		DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
	
		String sql = "select * \n";
		sql += " from fee_history \n";
		
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				/*
				 * private String today;
					private int minutes;
					private int payment;
					private String id;
					private int seat;
				 */
				
				ChargeDTO dto = new ChargeDTO();
				
				DateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
				String tempDate = sdFormat.format(rs.getDate(1));
				dto.setToday(tempDate);
				dto.setMinutes(rs.getString(2));
				dto.setPayment(rs.getInt(3));
				dto.setId(rs.getString(4));
				dto.setSeat(rs.getInt(5));
				
				alist.add(dto);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
