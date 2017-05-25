package dao.nbm.vn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.nbm.vn.model.TaiKhoan;

public class ThongKeReturnUI extends JFrame {
	
	JTextField txtSoTK,txtChiSoCK,txtNam;

	JButton btnTatCa,btnTimKiem,btnThoat;

	DefaultTableModel dtmReturnThangNam;
	
	JTable tblReturnThangNam;
	
	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";
	
	public ThongKeReturnUI(String title)
	{
		super(title);
		addControls();
		addEvents();
		
		ketNoiCoSoDuLieu();
		hienThiThongKeReturn();
		
	}
	
	private void ketNoiCoSoDuLieu(){
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl="jdbc:sqlserver://"+strServer+":1433;databaseName="+strDatabase+";integratedSecurity=true;";
			conn=DriverManager.getConnection(connectionUrl);

		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
	
	private void hienThiThongKeReturn() {
		// TODO Auto-generated method stub	
		try {		
			String sqlTaiKhoan="select * from ThongKeReturn";
			preStatement=conn.prepareStatement(sqlTaiKhoan);	
			result=preStatement.executeQuery();
			dtmReturnThangNam.setRowCount(0);

			while(result.next())
			{
				Vector<Object> vecTKReturn=new Vector<Object>();
				vecTKReturn.add(result.getInt("Nam"));
				vecTKReturn.add(result.getString("SoTKVaChiSoCK"));		
				vecTKReturn.add(result.getFloat("Thang1"));
				vecTKReturn.add(result.getFloat("Thang2"));
				vecTKReturn.add(result.getFloat("Thang3"));
				vecTKReturn.add(result.getFloat("Thang4"));
				vecTKReturn.add(result.getFloat("Thang5"));
				vecTKReturn.add(result.getFloat("Thang6"));
				vecTKReturn.add(result.getFloat("Thang7"));
				vecTKReturn.add(result.getFloat("Thang8"));
				vecTKReturn.add(result.getFloat("Thang9"));
				vecTKReturn.add(result.getFloat("Thang10"));
				vecTKReturn.add(result.getFloat("Thang11"));
				vecTKReturn.add(result.getFloat("Thang12"));
				vecTKReturn.add(result.getFloat("ReturnNam"));
				
				dtmReturnThangNam.addRow(vecTKReturn);

			}

		}catch(NullPointerException e){
			System.out.println(""+e);
		} catch (SQLException e) {
			System.out.println(""+e);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void addEvents() {
		// TODO Auto-generated method stub
		btnThoat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int ret=JOptionPane.showConfirmDialog(null, 
						"Bạn có chắc chắn muốn thoát Thống kê Return", 
						"Thông báo thoát", 
						JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.NO_OPTION)return;
				dispose();
			}
		});
		btnTatCa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				hienThiThongKeReturn();
			}
		});
		btnTimKiem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					xuLyTimThongTinReturn();
				}catch(NumberFormatException e){
						JOptionPane.showMessageDialog(null, 
								"Nhập thông tin cần tìm bị lỗi. Vui lòng kiểm tra lại!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
	public boolean kiemTraSoTkVaChiSoCKTonTai(String sotk)
	{
		try
		{
			String sql="select * from ThongKeReturn where SoTKVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, sotk);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean kiemTraNamTonTai(int nam)
	{
		try
		{
			String sql="select * from ThongKeReturn where Nam=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setInt(1, nam);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	protected void xuLyTimThongTinReturn() {
		// TODO Auto-generated method stub
		if(kiemTraSoTkVaChiSoCKTonTai(txtSoTK.getText()))//tìm theo SoTK
		{
			hienThiThongKeReturnTheoSoTK();
		}
		else//nếu không tìm thấy theo SoTK
		{
			if(kiemTraSoTkVaChiSoCKTonTai(txtChiSoCK.getText()))//tìm theo ChiSoCK
			{
				hienThiThongKeReturnTheoChiSoCK();
			}
			else//nếu không tìm thấy theo ChiSoCK
			{
				if(kiemTraNamTonTai(Integer.parseInt(txtNam.getText())))//tìm theo Nam
				{
					hienThiThongKeReturnTheoNam();
				}
				else
				{
					JOptionPane.showMessageDialog(null, 
							"Số TK["+txtSoTK.getText()+"] "
									+ "hoặc chỉ số CK["+txtChiSoCK.getText()+"] "
									+ "hoặc năm["+txtNam.getText()+"] không tồn tại.\n"
									+ "Không tìm thấy!");
				}
			}
		}
		
	}

	private void hienThiThongKeReturnTheoNam() {
		// TODO Auto-generated method stub
		try {		
			String sql="select * from ThongKeReturn where Nam=?";
			preStatement=conn.prepareStatement(sql);
			int nam=Integer.parseInt(txtNam.getText());
			preStatement.setInt(1, nam);
			result=preStatement.executeQuery();
			dtmReturnThangNam.setRowCount(0);

			while(result.next())
			{
				Vector<Object> vecTKReturn=new Vector<Object>();
				vecTKReturn.add(result.getInt("Nam"));
				vecTKReturn.add(result.getString("SoTKVaChiSoCK"));		
				vecTKReturn.add(result.getFloat("Thang1"));
				vecTKReturn.add(result.getFloat("Thang2"));
				vecTKReturn.add(result.getFloat("Thang3"));
				vecTKReturn.add(result.getFloat("Thang4"));
				vecTKReturn.add(result.getFloat("Thang5"));
				vecTKReturn.add(result.getFloat("Thang6"));
				vecTKReturn.add(result.getFloat("Thang7"));
				vecTKReturn.add(result.getFloat("Thang8"));
				vecTKReturn.add(result.getFloat("Thang9"));
				vecTKReturn.add(result.getFloat("Thang10"));
				vecTKReturn.add(result.getFloat("Thang11"));
				vecTKReturn.add(result.getFloat("Thang12"));
				vecTKReturn.add(result.getFloat("ReturnNam"));
				
				dtmReturnThangNam.addRow(vecTKReturn);

			}

		}catch(NullPointerException e){
			System.out.println(""+e);
		} catch (SQLException e) {
			System.out.println(""+e);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void hienThiThongKeReturnTheoSoTK() {
		// TODO Auto-generated method stub	
		try {		
			String sql="select * from ThongKeReturn where SoTKVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtSoTK.getText());
			result=preStatement.executeQuery();
			dtmReturnThangNam.setRowCount(0);

			while(result.next())
			{
				Vector<Object> vecTKReturn=new Vector<Object>();
				vecTKReturn.add(result.getInt("Nam"));
				vecTKReturn.add(result.getString("SoTKVaChiSoCK"));		
				vecTKReturn.add(result.getFloat("Thang1"));
				vecTKReturn.add(result.getFloat("Thang2"));
				vecTKReturn.add(result.getFloat("Thang3"));
				vecTKReturn.add(result.getFloat("Thang4"));
				vecTKReturn.add(result.getFloat("Thang5"));
				vecTKReturn.add(result.getFloat("Thang6"));
				vecTKReturn.add(result.getFloat("Thang7"));
				vecTKReturn.add(result.getFloat("Thang8"));
				vecTKReturn.add(result.getFloat("Thang9"));
				vecTKReturn.add(result.getFloat("Thang10"));
				vecTKReturn.add(result.getFloat("Thang11"));
				vecTKReturn.add(result.getFloat("Thang12"));
				vecTKReturn.add(result.getFloat("ReturnNam"));
				
				dtmReturnThangNam.addRow(vecTKReturn);

			}

		}catch(NullPointerException e){
			System.out.println(""+e);
		} catch (SQLException e) {
			System.out.println(""+e);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void hienThiThongKeReturnTheoChiSoCK() {
		// TODO Auto-generated method stub	
		try {		
			String sql="select * from ThongKeReturn where SoTKVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtChiSoCK.getText());
			result=preStatement.executeQuery();
			dtmReturnThangNam.setRowCount(0);

			while(result.next())
			{
				Vector<Object> vecTKReturn=new Vector<Object>();
				vecTKReturn.add(result.getInt("Nam"));
				vecTKReturn.add(result.getString("SoTKVaChiSoCK"));		
				vecTKReturn.add(result.getFloat("Thang1"));
				vecTKReturn.add(result.getFloat("Thang2"));
				vecTKReturn.add(result.getFloat("Thang3"));
				vecTKReturn.add(result.getFloat("Thang4"));
				vecTKReturn.add(result.getFloat("Thang5"));
				vecTKReturn.add(result.getFloat("Thang6"));
				vecTKReturn.add(result.getFloat("Thang7"));
				vecTKReturn.add(result.getFloat("Thang8"));
				vecTKReturn.add(result.getFloat("Thang9"));
				vecTKReturn.add(result.getFloat("Thang10"));
				vecTKReturn.add(result.getFloat("Thang11"));
				vecTKReturn.add(result.getFloat("Thang12"));
				vecTKReturn.add(result.getFloat("ReturnNam"));
				
				dtmReturnThangNam.addRow(vecTKReturn);

			}

		}catch(NullPointerException e){
			System.out.println(""+e);
		} catch (SQLException e) {
			System.out.println(""+e);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void addControls() {
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnThongKeReturn=new JPanel();
		pnThongKeReturn.setLayout(new BorderLayout());
		con.add(pnThongKeReturn,BorderLayout.CENTER);
		
		JPanel pnThongTinReturn=new JPanel();
		pnThongTinReturn.setLayout(new BoxLayout(
				pnThongTinReturn,BoxLayout.Y_AXIS));	
		pnThongKeReturn.add(pnThongTinReturn,BorderLayout.CENTER);

		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Thông tin thống kê Return tháng & năm");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		pnThongTinReturn.setBorder(borderTieuDe);
		
		JPanel pnSoTK=new JPanel();
		pnSoTK.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoTK=new JLabel("Số TK:");
		txtSoTK=new JTextField(15);
		pnSoTK.add(lblSoTK);
		pnSoTK.add(txtSoTK);
		pnThongTinReturn.add(pnSoTK);
		
		JPanel pnChiSoCK=new JPanel();
		pnChiSoCK.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblChiSoCK=new JLabel("Chỉ số CK:");
		txtChiSoCK=new JTextField(15);
		pnChiSoCK.add(lblChiSoCK);
		pnChiSoCK.add(txtChiSoCK);
		pnThongTinReturn.add(pnChiSoCK);
		
		JPanel pnNam=new JPanel();
		pnNam.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNam=new JLabel("Năm:");
		txtNam=new JTextField(15);
		pnNam.add(lblNam);
		pnNam.add(txtNam);
		pnThongTinReturn.add(pnNam);
		
		lblSoTK.setPreferredSize(lblChiSoCK.getPreferredSize());
		lblNam.setPreferredSize(lblChiSoCK.getPreferredSize());
		
		JPanel pnBtnReturnThang=new JPanel();
		pnBtnReturnThang.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnTatCa=new JButton("Tất cả");
		btnTimKiem=new JButton("Tìm kiếm");
		btnThoat=new JButton("Thoát");
		pnBtnReturnThang.add(btnTatCa);
		pnBtnReturnThang.add(btnTimKiem);
		pnBtnReturnThang.add(btnThoat);		
		pnThongTinReturn.add(pnBtnReturnThang);
		
		dtmReturnThangNam=new DefaultTableModel();
		dtmReturnThangNam.addColumn("Năm");
		dtmReturnThangNam.addColumn("Số TK & Chỉ số CK");
		dtmReturnThangNam.addColumn("Tháng 1");
		dtmReturnThangNam.addColumn("Tháng 2");
		dtmReturnThangNam.addColumn("Tháng 3");
		dtmReturnThangNam.addColumn("Tháng 4");
		dtmReturnThangNam.addColumn("Tháng 5");
		dtmReturnThangNam.addColumn("Tháng 6");
		dtmReturnThangNam.addColumn("Tháng 7");
		dtmReturnThangNam.addColumn("Tháng 8");
		dtmReturnThangNam.addColumn("Tháng 9");
		dtmReturnThangNam.addColumn("Tháng 10");
		dtmReturnThangNam.addColumn("Tháng 11");
		dtmReturnThangNam.addColumn("Tháng 12");
		dtmReturnThangNam.addColumn("Return năm");
		
		JTable tblReturnThangNam=new JTable(dtmReturnThangNam);
		tblReturnThangNam.setPreferredSize(new Dimension(0,250));
		JScrollPane scTableReturnThangNam=new JScrollPane(
				tblReturnThangNam,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnThongTinReturn.add(scTableReturnThangNam);


	}
	
	public void showWindow()
	{
		this.setSize(1200,450);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}


}
