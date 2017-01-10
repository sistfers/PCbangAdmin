package Admin_Product;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;


import Admin_starts.Vcontrol;
import all_DTO.RProductDTO;


public class ProductAddFrame extends JFrame implements ActionListener{
	
	private JButton cancelBtn, okBtn;
	
	private JTextField inputType;
	private JTextField inputName;
	private JTextField inputPrice;
	private ProductFrame mc;
	public boolean isDispose;
	
	public ProductAddFrame(ProductFrame mc) {
		setTitle("메뉴 추가");
		this.mc = mc;
		isDispose = false;
		Font f = new Font("굴림", Font.BOLD, 12);
		JLabel typeLabel = new JLabel("상품분류");
		typeLabel.setFont(f);
		typeLabel.setBounds(20, 10, 60, 20);
		add(typeLabel);
		
		inputType = new JTextField("");
		inputType.setBounds(90, 10, 100, 20);
		add(inputType);
		
		JLabel nameLabel = new JLabel("상품이름");
		nameLabel.setFont(f);
		nameLabel.setBounds(20, 50, 60, 20);
		add(nameLabel);
		
		inputName = new JTextField("");
		inputName.setBounds(90, 50, 100, 20);
		add(inputName);
		
		JLabel priceLabel = new JLabel("가격");
		priceLabel.setFont(f);
		priceLabel.setBounds(20, 90, 60, 20);
		add(priceLabel);
		
		inputPrice = new JTextField("");
		inputPrice.setBounds(90, 90, 100, 20);
		add(inputPrice);
		
		cancelBtn = new JButton("취소");
		cancelBtn.setBounds(40, 130, 60, 30);
		add(cancelBtn);
		cancelBtn.addActionListener(this);
		
		okBtn = new JButton("완료");
		okBtn.setBounds(110, 130, 60, 30);
		add(okBtn);
		okBtn.addActionListener(this);
		
		setLayout(null);
		getContentPane().setBackground(new Color(102, 120, 120));
		setBounds(200, 100, 230, 220);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		
		if(ob == okBtn){
			Vcontrol vc = Vcontrol.getInstance();
			if(inputType.getText().equals("") ||
					inputName.getText().equals("") ||
					inputPrice.getText().equals("")){
				JOptionPane.showMessageDialog(null, "작성자, 제목, 내용을 모두 기입하여주십시오");
				return;
			}
			String pro_type = inputType.getText();
			String pro_name = inputName.getText();
			int price = Integer.parseInt(inputPrice.getText());
			RProductDTO dto = new RProductDTO(pro_type, pro_name, price, 100);
			vc.productDao.add(dto);
			mc.renew();
			dispose();
		}else if(ob == cancelBtn){
			mc.renew();
			dispose();
		}
		
	}
	
}











