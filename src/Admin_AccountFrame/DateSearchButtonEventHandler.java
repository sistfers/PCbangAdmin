package Admin_AccountFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import all_DTO.DailySettlementDTO;

public class DateSearchButtonEventHandler implements ActionListener {
	
	private SetDBconnection DBconnection;
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	private ArrayList<DailySettlementDTO> alist = new ArrayList<DailySettlementDTO>(100);;
	
	private DailySettlementJFrame frame;

	public DateSearchButtonEventHandler(DailySettlementJFrame dailySettlement) {
		
		this.frame = dailySettlement;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
		
		int year;
		int month;
		
		year = Integer.parseInt(frame.input_year.getText());
		month = Integer.parseInt(frame.input_month.getText());
		
		alist.clear();
		clearTotable(frame);
		search(year, month);
		printTotable(frame);
	}
	
	public void clearTotable(DailySettlementJFrame frame) {
		
		for(int i = 0; i < 20; i++)
		{
			frame.rowData[i][0] = "";
			frame.rowData[i][1] = "";
			
			frame.renew();
		}
	}
	
	public void printTotable(DailySettlementJFrame frame) {
		for(int i = 0; i < alist.size(); i++)
		{
			frame.rowData[i][0] = alist.get(i).getDate();
			frame.rowData[i][1] = alist.get(i).getHours_income() + 
					alist.get(i).getProduct_income();
			frame.renew();
		}
	}

	public void search(int year, int month) {
		
		int last_date;
		
		if(month == 1 || month == 3 || month == 5 || month == 7 ||
				month == 8 || month == 10 || month == 12)
		{
			last_date = 31;
		} else if(month == 2)
		{
			last_date = 28;
		}
		else
		{
			last_date = 30;
		}
		
		String sql = "select * \n";
		sql += " from calculate \n";
		sql += " where calculate_date >= '" + year + "/" + month + "/" + "01'";
		sql += " and calculate_date <= '" + year + "/" + month + "/" + last_date + "'";
		
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				/*
				 * private String date;
					private int hours;
					private int hours_income;
					private int product_income;
				 */
				
				DailySettlementDTO dto = new DailySettlementDTO();
				
				DateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
				String tempDate = sdFormat.format(rs.getDate(1));
				dto.setDate(tempDate);
//				dto.setHours(rs.getInt(2));
				dto.setHours_income(rs.getInt(2));
				dto.setProduct_income(rs.getInt(3));
				
				alist.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}
