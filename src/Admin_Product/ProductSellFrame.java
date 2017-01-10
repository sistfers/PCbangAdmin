package Admin_Product;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import Admin_starts.Vcontrol;
import all_DTO.SalesHisDTO;


public class ProductSellFrame extends JPanel {
	private JTable jTable;
	private JScrollPane jScrPane;
	private DefaultTableModel model;
	
	String columnNames[] = {
			"상품분류", "상품이름", "판매수량", "판매단가", "판매금액"	
		};
	
	Object rowData[][];
	
	
	public ProductSellFrame() {
		Vcontrol vc = Vcontrol.getInstance();
		setBounds(100, 100, 640, 480);
		
		vc.salesHisDao.list = vc.salesHisDao.loadAll();
		
		setLayout(null);
		
		if(vc.salesHisDao.list.size() != 0){
			rowData = new Object[vc.salesHisDao.list.size()][5];
			
			for(int i=0; i<rowData.length; ++ i){
				SalesHisDTO d = vc.salesHisDao.list.get(i);
				rowData[i][0] = d.getPro_type();
				rowData[i][1] = d.getPro_name();
				rowData[i][2] = d.getSale_quantity();
				rowData[i][3] = d.getPrice();
				rowData[i][4] = d.getTotal_income();
			}
		}else{
			rowData = new Object[1][5];
		}
		model = new DefaultTableModel(rowData,  columnNames);
		jTable = new JTable(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		jTable.getColumnModel().getColumn(0).setMaxWidth(200);
		jTable.getColumnModel().getColumn(1).setMaxWidth(200);
		jTable.getColumnModel().getColumn(2).setMaxWidth(200);
		jTable.getColumnModel().getColumn(3).setMaxWidth(200);
		jTable.getColumnModel().getColumn(4).setMaxWidth(200);
		
		jTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		jScrPane = new JScrollPane(jTable);
		jScrPane.setBounds(0, 0, 640, 480);
		add(jScrPane);
		
		Font f1 = new Font("HY울릉도M", Font.PLAIN, 20);
		
		setVisible(true);
	}
}
