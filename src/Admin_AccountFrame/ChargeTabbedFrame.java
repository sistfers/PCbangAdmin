package Admin_AccountFrame;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import Admin_Product.ProductSellFrame;


public class ChargeTabbedFrame extends JFrame {
	public ProductSellFrame panel1 = new ProductSellFrame();
	public ChargeHistoryJFrame panel2 = new ChargeHistoryJFrame();
	public TotalIncomePanel panel3;
	
	public ChargeTabbedFrame() {
		JTabbedPane tab = new JTabbedPane();
		panel3 = new TotalIncomePanel(this);
		tab.addTab("상품판매내역", panel1);
		tab.addTab("PC요금내역", panel2);
		tab.addTab("총판매내역", panel3);
		
		setBounds(100, 100, 640, 480);

		add(tab);
		
		setVisible(true);
		
		
	}
}
