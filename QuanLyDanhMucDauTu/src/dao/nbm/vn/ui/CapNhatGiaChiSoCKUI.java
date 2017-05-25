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
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.nbm.vn.model.ChungKhoan;
import dao.nbm.vn.model.TaiKhoan;

public class CapNhatGiaChiSoCKUI extends JFrame {
	
	DefaultTableModel dtmCapNhatGiaVaChiSoCK;
	JTable tblCapNhatGiaVaChiSoCK;
	
	JTextField txtSoTK,txtNgay,txtMaVaChiSoCK,txtGia;

	JButton btnCapNhat,btnThoat;
	
	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";
	
	public CapNhatGiaChiSoCKUI(String title)
	{
		super(title);
		addControls();
		addEvents();
		
		ketNoiCoSoDuLieu();
		hienThiDanhSachCKHT();
	}
	
	private void moNhapLieu()
	{
		txtNgay.setEditable(true);
		txtMaVaChiSoCK.setEditable(true);
		txtGia.setEditable(true);
		tblCapNhatGiaVaChiSoCK.setEnabled(true);
	}
	
	private void dongNhapLieu()
	{
		txtNgay.setEditable(false);
		txtMaVaChiSoCK.setEditable(false);
		txtGia.setEditable(false);
	}

	private void hienThiDanhSachCKHT() {
		// TODO Auto-generated method stub	
		dongNhapLieu();
		try {		
			String sql="select * from ChungKhoanMuaBan where LoaiCKHayCS=0 or LoaiCKHayCS=1";
			preStatement=conn.prepareStatement(sql);	
			result=preStatement.executeQuery();
			dtmCapNhatGiaVaChiSoCK.setRowCount(0);

			int stt=1;//stt tự tăng
			while(result.next())
			{
				Vector<Object> vecBangCKHT=new Vector<Object>();

				vecBangCKHT.add(stt);
				stt++;
				vecBangCKHT.add(result.getString("Ngay"));
				vecBangCKHT.add(result.getString("MaVaChiSoCK"));
				vecBangCKHT.add(result.getFloat("Gia"));

				dtmCapNhatGiaVaChiSoCK.addRow(vecBangCKHT);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

	private void addEvents() {
		// TODO Auto-generated method stub
		btnThoat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int ret=JOptionPane.showConfirmDialog(null, 
						"Bạn có chắc chắn muốn thoát Cập nhật Giá/Chỉ số CK?", 
						"Thông báo thoát", 
						JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.NO_OPTION)return;
				dispose();
			}
		});
		btnCapNhat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				moNhapLieu();
				xuLyCapNhat();

			}
		});
		tblCapNhatGiaVaChiSoCK.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int rowBangCK=tblCapNhatGiaVaChiSoCK.getSelectedRow();
				if(rowBangCK==-1)return;
				
				String ngay=tblCapNhatGiaVaChiSoCK.getValueAt(rowBangCK, 1)+"";
				String maVaChiSoCK=tblCapNhatGiaVaChiSoCK.getValueAt(rowBangCK, 2)+"";
				String gia=tblCapNhatGiaVaChiSoCK.getValueAt(rowBangCK, 3)+"";
				
				txtNgay.setText(ngay);
				txtMaVaChiSoCK.setText(maVaChiSoCK);
				txtGia.setText(gia);
			}
		});
	}

	public boolean kiemTraMaVaChiSoCkTrongBangCKMBTonTai(String mavachisock)
	{
		try
		{
			String sql="select * from ChungKhoanMuaBan where MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, mavachisock);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	protected void xuLyCapNhat() {
		//nếu mã/chỉ số ck(chưa tồn tại) thì thông báo cập nhật giá thất bại
		if(!kiemTraMaVaChiSoCkTrongBangCKMBTonTai(txtMaVaChiSoCK.getText()))
		{
			JOptionPane.showMessageDialog(null, 
					"Mã/Chỉ số CK["+txtMaVaChiSoCK.getText()+"] chưa tồn tại. "
							+ "Cập nhật giá thất bại!");
		}
		else//nếu mã/chỉ số ck(đã tồn tại) thì tiến hành cập nhật giá
		{
			int ret=JOptionPane.showConfirmDialog(null, 
					"Mã/Chỉ số CK["+txtMaVaChiSoCK.getText()+"] đã tồn tại.\n"
							+ "Bạn có chắc chắn muốn cập nhật giá?", 
					"Cập nhật giá", 
					JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try
			{
				String sql="update ChungKhoanMuaBan set Ngay=?,Gia=? where MaVaChiSoCK=?";
				preStatement=conn.prepareStatement(sql);

				preStatement.setString(1, txtNgay.getText());
				preStatement.setFloat(2, Float.parseFloat(txtGia.getText()));

				preStatement.setString(3, txtMaVaChiSoCK.getText());

				int x=preStatement.executeUpdate();
				if(x>0)
				{
					hienThiDanhSachCKHT();
					dongNhapLieu();
				}	

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}		
	}

	private void addControls() {
		// TODO Auto-generated method stub
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnCapNhatGiaChiSoCK=new JPanel();
		pnCapNhatGiaChiSoCK.setLayout(new BorderLayout());
		con.add(pnCapNhatGiaChiSoCK,BorderLayout.CENTER);
		
		JPanel pnThongTinCapNhat=new JPanel();
		pnThongTinCapNhat.setLayout(new BoxLayout(
				pnThongTinCapNhat,BoxLayout.Y_AXIS));	
		pnCapNhatGiaChiSoCK.add(pnThongTinCapNhat,BorderLayout.CENTER);

		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Cập nhật Giá/Chỉ số chứng khoán");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		pnThongTinCapNhat.setBorder(borderTieuDe);
		
		JPanel pnNgayMuaBanCK=new JPanel();
		pnNgayMuaBanCK.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNgay=new JLabel("Ngày cập nhật:");
		txtNgay=new JTextField(24);
		pnNgayMuaBanCK.add(lblNgay);
		pnNgayMuaBanCK.add(txtNgay);
		pnThongTinCapNhat.add(pnNgayMuaBanCK);
		
		JPanel pnMaVaChiSoCK=new JPanel();
		pnMaVaChiSoCK.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMaVaChiSoCK=new JLabel("Mã/Chỉ số CK:");
		txtMaVaChiSoCK=new JTextField(24);;
		pnMaVaChiSoCK.add(lblMaVaChiSoCK);
		pnMaVaChiSoCK.add(txtMaVaChiSoCK);
		pnThongTinCapNhat.add(pnMaVaChiSoCK);
		
		JPanel pnGia=new JPanel();
		pnGia.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblGia=new JLabel("Giá:");
		txtGia=new JTextField(24);
		pnGia.add(lblGia);
		pnGia.add(txtGia);
		pnThongTinCapNhat.add(pnGia);
		
		lblMaVaChiSoCK.setPreferredSize(lblNgay.getPreferredSize());
		lblGia.setPreferredSize(lblNgay.getPreferredSize());
		
		
		JPanel pnButtonCN=new JPanel();
		pnButtonCN.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnCapNhat=new JButton("Cập nhật");
		pnButtonCN.add(btnCapNhat);
		btnThoat=new JButton("Thoát");
		pnButtonCN.add(btnThoat);		
		pnThongTinCapNhat.add(pnButtonCN);
		
		dtmCapNhatGiaVaChiSoCK=new DefaultTableModel();
		dtmCapNhatGiaVaChiSoCK.addColumn("Stt");
		dtmCapNhatGiaVaChiSoCK.addColumn("Ngày mua/bán CK");
		dtmCapNhatGiaVaChiSoCK.addColumn("Mã/Chỉ số CK");
		dtmCapNhatGiaVaChiSoCK.addColumn("Giá");
		tblCapNhatGiaVaChiSoCK=new JTable(dtmCapNhatGiaVaChiSoCK);
		tblCapNhatGiaVaChiSoCK.setPreferredSize(new Dimension(0,220));
		JScrollPane scTable=new JScrollPane(
				tblCapNhatGiaVaChiSoCK,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnThongTinCapNhat.add(scTable);
		
	}
	public void showWindow()
	{
		this.setSize(450,420);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
