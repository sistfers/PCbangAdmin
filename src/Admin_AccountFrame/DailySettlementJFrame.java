package Admin_AccountFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import all_DTO.DailySettlementDTO;

public class DailySettlementJFrame extends JFrame implements ActionListener {
	
	private JLabel date;
	private JLabel year;
	private JLabel month;
	
	public JTextField input_year;
	public JTextField input_month;
	private JButton date_search;
	private JButton graph;
	private JPanel jf;
	
	public Object rowData[][];	
	private String columnNames[] = {
			"일자", "수익"
	};
	
	public JTable calculatehistoryTable;
	public DefaultTableModel model;
	public JScrollPane jScrPane;
	
	private Dimension dim;
	private ArrayList<DailySettlementDTO> alist = new ArrayList<DailySettlementDTO>(31);

	private SetDBconnection DBconnection;
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public DailySettlementJFrame() {
		setTitle("월 매출 현황");
		setLayout(null);				
		setBounds(0, 0, 720, 576);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		date = new JLabel("일자");
		year = new JLabel("년");
		month = new JLabel("월");
		
		input_year = new JTextField();
		input_month = new JTextField();
		
		date.setBounds(100, 80, 100, 30);
		year.setBounds(270, 80, 100, 30);
		month.setBounds(400, 80, 100, 30);
		
		input_year.setBounds(170, 80, 100, 30);
		input_month.setBounds(300, 80, 100, 30);
		input_year.setHorizontalAlignment(JTextField.CENTER);
		input_month.setHorizontalAlignment(JTextField.CENTER);
		
		date_search = new JButton("검색");		
		date_search.setBounds(450, 80, 60, 30);
		
		graph = new JButton("back");
		graph.setBounds(540, 80, 70, 30);
		graph.addActionListener(this);
		
		date_search.addActionListener(this);
		
		MonthlyGraphic mg = new MonthlyGraphic();
		jf = mg.createAndShowGUI();
		
		jf.setBounds(50, 150, 600, 400);
		add(jf);		
		
		rowData = new Object[31][5];							
		
		add(date);
		add(year);
		add(month);
		add(input_year);
		add(input_month);
		add(date_search);
		add(graph);
		
		setResizable(false);
		setVisible(true);
	}	
	
	public void renew() {
				
		calculatehistoryTable.invalidate();
		calculatehistoryTable.validate();
		calculatehistoryTable.repaint();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		if(e.getSource().equals(date_search))
		{
			DBconnection = new SetDBconnection();
			conn = DBconnection.makeConnection();
			
			int year;
			int month;
			
			year = Integer.parseInt(input_year.getText());
			month = Integer.parseInt(input_month.getText());
			
			alist.clear();
			clearTotable();
			search(year, month);
			printTotable();
		}
		else if(e.getSource().equals(graph))
		{
			jScrPane.setVisible(false);
//			calculatehistoryTable.setVisible(false);
		}
		
	}	
	
	public void clearTotable() {
		
		for(int i = 0; i < 31; i++)
		{
			rowData[i][0] = "";
			rowData[i][1] = "";
			
//			frame.renew();
		}
	}
	
	public void printTotable() {
		rowData = new Object[alist.size()][2];
		
		for(int i = 0; i < alist.size(); i++)
		{
			rowData[i][0] = alist.get(i).getDate();
			rowData[i][1] = alist.get(i).getHipi();
		}

		model = new DefaultTableModel(rowData, columnNames);
//		calculatehistoryTable = new JTable(rowData, columnNames);	
		
		calculatehistoryTable = new JTable(model);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		calculatehistoryTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		calculatehistoryTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);	
		
		jScrPane = new JScrollPane(calculatehistoryTable); 
		jScrPane.setBounds(50, 150, 600, 350);		
		
		add(jScrPane);
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
		
		String sql = "select calculate_date, to_char(hours_income+PRODUCT_INCOME, '$9,999,999') \n";
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
//				dto.setHours_income(rs.getInt(2));
//				dto.setProduct_income(rs.getInt(3));
				dto.setHipi(rs.getString(2));
				
				alist.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}
