package Admin_Product;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Admin_starts.Vcontrol;
import all_DTO.RProductDTO;


public class ProductFrame extends JFrame implements ActionListener{
	
	
	private JTable jTable;
	private JScrollPane jScrPane;
	private JButton addBtn, deleteBtn;
	private DefaultTableModel model;
	
	String columnNames[] = {
			"상품분류", "상품이름", "가격"	
		};
	
	Object rowData[][];
	
	
	public ProductFrame() {
		setTitle("메뉴판");
		Vcontrol vc = Vcontrol.getInstance();
		setBounds(100, 100, 640, 480);
		
		vc.productDao.list = vc.productDao.loadAll();
		
		setLayout(null);
		
		JLabel title = new JLabel("메뉴판");
		Font f = new Font("HY울릉도M", Font.BOLD, 30);
		title.setFont(f);
		title.setBounds(280, 10, 180, 60);
		add(title);
		
		
		if(vc.productDao.list.size() != 0){
			rowData = new Object[vc.productDao.list.size()][3];
			
			for(int i=0; i<rowData.length; ++ i){
				RProductDTO d = vc.productDao.list.get(i);
				rowData[i][0] = d.getPro_type();
				rowData[i][1] = d.getPro_name();
				rowData[i][2] = d.getPrice();
				//rowData[i][3] = d.getQuantity();
			}
		}else{
			rowData = new Object[1][3];
		}
		model = new DefaultTableModel(rowData,  columnNames);
		jTable = new JTable(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		jTable.getColumnModel().getColumn(0).setMaxWidth(300);
		jTable.getColumnModel().getColumn(1).setMaxWidth(300);
		jTable.getColumnModel().getColumn(2).setMaxWidth(300);
		//jTable.getColumnModel().getColumn(3).setMaxWidth(180);
		
		jTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		jScrPane = new JScrollPane(jTable);
		jScrPane.setBounds(10, 100, 600, 280);
		add(jScrPane);
		
		Font f1 = new Font("HY울릉도M", Font.PLAIN, 20);
		
		addBtn = new JButton("추가");
		addBtn.setFont(f1);
		addBtn.setBounds(200, 380, 100, 50);
		add(addBtn);
		addBtn.addActionListener(this);
		
		deleteBtn = new JButton("삭제");
		deleteBtn.setFont(f1);
		deleteBtn.setBounds(350, 380, 100, 50);
		add(deleteBtn);
		deleteBtn.addActionListener(this);
		
		jTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int rowNum = jTable.getSelectedRow();
				vc.productDao.selPro = vc.productDao.list.get(rowNum);
			}
		});
		
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		
		if(ob == addBtn){
			new ProductAddFrame(this);
			
		}else if(ob == deleteBtn){
			Vcontrol vc = Vcontrol.getInstance();
			if(vc.productDao.selPro == null){
				JOptionPane.showMessageDialog(null, "삭제하고싶은 과자행을 선택해주세요!");
				return;
			}
			vc.productDao.deleteBbs();
			renew();
			
		}
		
	}
	
	public void renew() {
		removeAllRow();
		Vcontrol vc = Vcontrol.getInstance();
		vc.productDao.list = vc.productDao.loadAll();
		rowData = new Object[vc.productDao.list.size()][3];
		if(vc.productDao.list.size() != 0){
			
			for(int i=0; i<vc.productDao.list.size(); ++ i){
				RProductDTO d = vc.productDao.list.get(i);
				Object[] selected = {d.getPro_type(),
				rowData[i][1] = d.getPro_name(),
				rowData[i][2] = d.getPrice()};
				//rowData[i][3] = d.getQuantity();
				
				model.addRow(selected);
			}
		}else{
			rowData = new Object[1][3];
		}
	        
	}
	
	public void removeAllRow(){   //선택 상품 테이블의 row를 전부 지움
	      if(model.getRowCount() > 0){
	         for(int i = model.getRowCount() - 1; i >= 0; i--){
	        	 model.removeRow(i);
	         }
	      }
	   }
	
	
	
}











