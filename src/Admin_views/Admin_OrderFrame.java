package Admin_views;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Admin_DAO.OrderDAO;
import Admin_starts.Vcontrol;
import all_DTO.ProductDTO;


public class Admin_OrderFrame extends JFrame {
	private JScrollPane listScrollPane;
	private JTable listTable;
	private DefaultTableModel defaultTableModel;
	
	private String[] column = {
		"좌석", "id", "상품분류", "상품이름", "수량", "주문 확인"
	};
	
	Object[][] rowData;
	
	OrderDAO productManage = new OrderDAO();
	
	public Admin_OrderFrame(OrderDAO productDao) {
		super("주문내역");
		setLayout(null);
		setBounds(150, 150, 640, 480);
		
		this.productManage = productDao;
		
		defaultTableModel = new DefaultTableModel(column, 0) {
			// 셀편집을 못하게 하는 필드
			public boolean isCellEditable(int row, int column) {
				if(column == 5)
					return true;
				return false;
			}
		};
		if(productDao.orderList.size() > 0){
			Object[] orderData = {
					productManage.orderList.get(0).getSeat(),
					productManage.orderList.get(0).getId(),
					productManage.orderList.get(0).getProductType(),
					productManage.orderList.get(0).getProductName(),
					productManage.orderList.get(0).getQuantity(),
					"주문확인"
			};
			
			defaultTableModel.addRow(orderData);
			
			Object[] data = {
					null,
					"총 주문량",
					productManage.orderList.size()
			};
			
			defaultTableModel.addRow(data);
		}
		listTable = new JTable(defaultTableModel);
		
		DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
		defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		listTable.getColumnModel().getColumn(0).setMaxWidth(40);
		listTable.getColumnModel().getColumn(1).setMaxWidth(120);
		listTable.getColumnModel().getColumn(2).setMaxWidth(120);
		listTable.getColumnModel().getColumn(3).setMaxWidth(150);
		listTable.getColumnModel().getColumn(4).setMaxWidth(50);
		listTable.getColumnModel().getColumn(5).setMaxWidth(100);
		
		listTable.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
		listTable.getColumnModel().getColumn(1).setCellRenderer(defaultTableCellRenderer);
		listTable.getColumnModel().getColumn(2).setCellRenderer(defaultTableCellRenderer);
		listTable.getColumnModel().getColumn(3).setCellRenderer(defaultTableCellRenderer);
		listTable.getColumnModel().getColumn(4).setCellRenderer(defaultTableCellRenderer);
		listTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
		listTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
	
		listScrollPane = new JScrollPane(listTable);
		
		listScrollPane.setBounds(60, 80, 500, 300);
		
		add(listScrollPane);
		
		setVisible(true);
	}
	
	public void tablePrint(OrderDAO productDao){
		this.productManage = productDao;
		
		removeAllRow();
		
		rowData = new Object[productManage.orderList.size()][6];
		
		if(productManage.orderList.size() > 0){
			for(int i = 0; i < productManage.orderList.size(); i++){
				Object[] orderData = {
						productManage.orderList.get(i).getSeat(),
						productManage.orderList.get(i).getId(),
						productManage.orderList.get(i).getProductType(),
						productManage.orderList.get(i).getProductName(),
						productManage.orderList.get(i).getQuantity(),
						"주문확인"
				};
				
				defaultTableModel.addRow(orderData);
			}
			
			Object[] data = {
					null,
					"총 주문량",
					productManage.orderList.size()
			};
			
			defaultTableModel.addRow(data);
		}
	}
	
	public void removeAllRow(){	//선택 상품 테이블의 row를 전부 지움
		if(defaultTableModel.getRowCount() > 0){
			for(int i = defaultTableModel.getRowCount() - 1; i >= 0; i--){
				defaultTableModel.removeRow(i);
			}
		}
	}
	
	//주문확인용 버튼 renderer
		class ButtonRenderer extends JButton implements TableCellRenderer{

			public ButtonRenderer(){
				setOpaque(true);
			}
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if(isSelected){
					setForeground(table.getSelectionForeground());
					setBackground(table.getSelectionBackground());
				}else{
					setForeground(table.getForeground());
					setBackground(UIManager.getColor("Button.background"));
				}
				setText((value == null)?"x":value.toString());
				return this;
			}
		}
		
		class ButtonEditor extends DefaultCellEditor{
			protected JButton button;
			private String label;
			private boolean isPushed;
			private int selectRow;
			
			public ButtonEditor(JCheckBox checkBox) {
				super(checkBox);
				button = new JButton();
				button.setOpaque(true);
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							//주문확인 날려주기
							Vcontrol vcontrol = Vcontrol.getInstance();
							Socket socket = vcontrol.clients.get(vcontrol.pcseat[productManage.orderList.get(selectRow).getSeat()-1]);
							DataOutputStream out = new DataOutputStream(socket.getOutputStream());
							ProductDTO confirmDto = productManage.orderList.get(selectRow);
							
							vcontrol.orderDao.saleStock(confirmDto);
							
							out.writeUTF("주문");
							out.writeUTF(confirmDto.getProductName());
							out.writeInt(confirmDto.getQuantity());
							
							/*vcontrol.pcseat[confirmDto.getSeat()-1].setMoney(
									(confirmDto.getPrice()*confirmDto.getQuantity())
									+ vcontrol.pcseat[confirmDto.getSeat()-1].getMoney());*/
							
							productManage.orderList.remove(confirmDto);
							tablePrint(productManage);
							setVisible(true);
						}catch(Exception e1){
							e1.printStackTrace();
						}
						
					}
				});
			}

			//이게 버튼처럼 눌리게 하는건가
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				if(isSelected){
					button.setForeground(table.getSelectionForeground());
					button.setBackground(table.getSelectionBackground());
				}else{
					button.setForeground(table.getForeground());
					button.setBackground(table.getBackground());
				}
				
				selectRow = row;
				label = (value== null)?"x":value.toString();
				button.setText(label);
				isPushed = true;
				return button;
			}

			//이 메소드 안씀
			@Override
			public Object getCellEditorValue() {
				if(isPushed){
				}
				
				isPushed = false;
				return label;
			}
		}
}
