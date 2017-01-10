package Admin_AccountFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import Admin_AccountFrame.SetDBconnection;

public class MonthlyGraphic extends JPanel
{
    private int histogramHeight = 270;
    private int barWidth = 50;
    private int barGap = 10;

    private JPanel barPanel;
    private JPanel labelPanel;

    private List<Bar> bars = new ArrayList<Bar>();

    public MonthlyGraphic()
    {
        setBorder( new EmptyBorder(10, 10, 10, 10) );
        setLayout( new BorderLayout() );

        barPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder( compound );

        labelPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        labelPanel.setBorder( new EmptyBorder(5, 10, 0, 10) );

        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
    }

    public void addHistogramColumn(String label, int value, String month, Color color)
    {
        Bar bar = new Bar(label, value, month, color);
        bars.add( bar );
    }
//	panel.addHistogramColumn("12월", dec, december,  Color.CYAN);
    
    public void layoutHistogram()
    {
        barPanel.removeAll();
        labelPanel.removeAll();

        int maxValue = 0;

        for (Bar bar: bars)
            maxValue = Math.max(maxValue, bar.getValue());

        for (Bar bar: bars)
        {
            JLabel label = new JLabel(bar.getMonth());
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.TOP);
            label.setVerticalAlignment(JLabel.BOTTOM);
            int barHeight = (bar.getValue() * histogramHeight) / maxValue;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon( icon );
            barPanel.add( label );

            JLabel barLabel = new JLabel( bar.getLabel() );
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            labelPanel.add( barLabel );
        }
    }
//	panel.addHistogramColumn("12월", dec, december,  Color.CYAN);
    private class Bar
    {
        private String label;
        private int value;
        private String month;
        private Color color;

        public Bar(String label, int value, String month, Color color)
        {
            this.label = label;
            this.value = value;
            this.month = month;
            this.color = color;
        }

        public String getLabel()
        {
            return label;
        }

        public int getValue()
        {
            return value;
        }
        
        public String getMonth()
        {
        	return month;
        }

        public Color getColor()
        {
            return color;
        }
    }

    private class ColorIcon implements Icon
    {
        private int shadow = 3;

        private Color color;
        private int width;
        private int height;

        public ColorIcon(Color color, int width, int height)
        {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth()
        {
            return width;
        }

        public int getIconHeight()
        {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shadow, y + shadow, shadow, height - shadow);
        }
    }

    public JPanel createAndShowGUI()
    {    	
    	SetDBconnection DBconnection;
    	Connection conn;
    	PreparedStatement stmt;
    	ResultSet rs;
    	
    	DBconnection = new SetDBconnection();
		conn = DBconnection.makeConnection();
		
		String july = "";
		int jul_y = 0;
		
		String sql7 = "select sum(hours_income+product_income), ";
		sql7 += "to_char(sum(hours_income+product_income), '$999,999,999')";
		sql7 += "from calculate ";
		sql7 += "where calculate_date >= '16/07/01'";
		sql7 += "and calculate_date <= '16/07/31'";
    	
    	try {
			stmt = conn.prepareStatement(sql7);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				jul_y = rs.getInt(1);
				july = rs.getString(2);
			}
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}	    			
		
		String august = "";
		int aug = 0;
		
		String sql8 = "select sum(hours_income+product_income), ";
		sql8 += "to_char(sum(hours_income+product_income), '$999,999,999')";
		sql8 += "from calculate ";
		sql8 += "where calculate_date >= '16/08/01'";
		sql8 += "and calculate_date <= '16/08/31'";
    	
    	try {
			stmt = conn.prepareStatement(sql8);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				aug = rs.getInt(1);
				august = rs.getString(2);
			}
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}	    			
		
		String September = "";
		int sep = 0;
		
		String sql9 = "select sum(hours_income+product_income), ";
		sql9 += "to_char(sum(hours_income+product_income), '$999,999,999')";
		sql9 += "from calculate ";
		sql9 += "where calculate_date >= '16/09/01'";
		sql9 += "and calculate_date <= '16/09/30'";
    	
    	try {
			stmt = conn.prepareStatement(sql9);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				sep = rs.getInt(1);
				September = rs.getString(2);
			}
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}	    	
		
		String October = "";
		int oct = 0;
		
		String sql10 = "select sum(hours_income+product_income), ";
		sql10 += "to_char(sum(hours_income+product_income), '$999,999,999')";
		sql10 += "from calculate ";
		sql10 += "where calculate_date >= '16/10/01'";
		sql10 += "and calculate_date <= '16/10/31'";
    	
    	try {
			stmt = conn.prepareStatement(sql10);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				oct = rs.getInt(1);
				October = rs.getString(2);
			}
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}	    	
		
		String November = "";
		int nov = 0;
		
		String sql11 = "select sum(hours_income+product_income), ";
		sql11 += "to_char(sum(hours_income+product_income), '$999,999,999')";
		sql11 += "from calculate ";
		sql11 += "where calculate_date >= '16/11/01'";
		sql11 += "and calculate_date <= '16/11/30'";
    	
    	try {
			stmt = conn.prepareStatement(sql11);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				nov = rs.getInt(1);
				November = rs.getString(2);
			}
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}	    	
		
    	String december = "";
    	int dec = 0;
    	
    	String sql12 = "select sum(hours_income+product_income), ";
    	sql12 += "to_char(sum(hours_income+product_income), '$999,999,999')";
    	sql12 += "from calculate ";
    	sql12 += "where calculate_date >= '16/12/01'";
    	sql12 += "and calculate_date <= '16/12/31'";
    	
    	try {
			stmt = conn.prepareStatement(sql12);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				dec = rs.getInt(1);
				december = rs.getString(2);
			}
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}	    	
    	
    	MonthlyGraphic panel = new MonthlyGraphic();
//        panel.addHistogramColumn("1월", 0, "", Color.RED);
//        panel.addHistogramColumn("2월", 0, "", Color.YELLOW);
//        panel.addHistogramColumn("3월", 0, "", Color.BLUE);
//        panel.addHistogramColumn("4월", 0, "", Color.ORANGE);
//        panel.addHistogramColumn("5월", 0, "", Color.MAGENTA);
//        panel.addHistogramColumn("6월", 0, "", Color.CYAN);
        panel.addHistogramColumn("7월", jul_y, july, Color.RED);
        panel.addHistogramColumn("8월", aug, august, Color.YELLOW);
        panel.addHistogramColumn("9월", sep, September, Color.BLUE);
        panel.addHistogramColumn("10월", oct, October, Color.ORANGE);
        panel.addHistogramColumn("11월", nov, November, Color.MAGENTA);
        panel.addHistogramColumn("12월", dec, december,  Color.CYAN);
        panel.layoutHistogram();

//        JPanel frame = new JPanel("Histogram Panel");
        JPanel frame = new JPanel();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( panel );
//        frame.setLocationByPlatform( true );
//        frame.pack();
//        frame.setVisible( true );        
        
        return frame;
    }
    
}