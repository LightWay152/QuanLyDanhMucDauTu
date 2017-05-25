package dao.nbm.vn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
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

public class LenhMuaBanUI extends JFrame {
	
	JTextField txtSoTK,txtNgayMuaBanCK,
	txtMaCK,txtSoLuong,txtGia,txtThanhTien,
	txtTongTienMua,txtTongTienBan,
	txtLenh,txtThue,txtPhi;

	JButton btnTatCaCK,btnTimCK,btnMua,btnBan,btnThoat;

	DefaultTableModel dtmLenhMuaCK,dtmLenhBanCK;
	JTable tblLenhMuaCK,tblLenhBanCK;
	
	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";
	
	public LenhMuaBanUI(String title)
	{
		super(title);
		addControls();
		addEvents();
		
		ketNoiCoSoDuLieu();
		hienThiDanhSachCK();
	}
	
	private void moNhapLieu()
	{
		txtSoTK.setEditable(true);
		txtNgayMuaBanCK.setEditable(true);
		txtMaCK.setEditable(true);
		txtSoLuong.setEditable(true);
		txtGia.setEditable(true);
		txtLenh.setEditable(true);
		txtPhi.setEditable(true);
		txtThue.setEditable(true);
		tblLenhMuaCK.setEnabled(true);
		tblLenhBanCK.setEnabled(true);
	}
	
	private void dongNhapLieu()
	{
		txtSoTK.setEditable(false);
		txtNgayMuaBanCK.setEditable(false);
		txtMaCK.setEditable(false);
		txtSoLuong.setEditable(false);
		txtGia.setEditable(false);
		txtLenh.setEditable(false);
		txtPhi.setEditable(false);
		txtThue.setEditable(false);
		txtThanhTien.setEditable(false);
		txtTongTienMua.setEditable(false);
		txtTongTienBan.setEditable(false);
	}

	private void hienThiDanhSachCK() {
		// TODO Auto-generated method stub	
		dongNhapLieu();
		try {	
			//đổ lên giao diện vào bảng những ck đã mua
			String sql="select SoTK,Ngay,MaVaChiSoCK,LoaiCKHayCS,Gia,SoLuong,Phi,Thue,ThanhTien,Lenh "
					+ "from ChungKhoan where LoaiCKHayCS=0 or LoaiCKHayCS=1";
			preStatement=conn.prepareStatement(sql);	
			result=preStatement.executeQuery();
			dtmLenhMuaCK.setRowCount(0);

			int stt=1;//stt tự tăng
			float tongTienMua=0;//tính tổng tiền mua ck
			while(result.next())
			{
				Vector<Object> vecLenhMuaCK=new Vector<Object>();

				vecLenhMuaCK.add(stt);
				stt++;
				vecLenhMuaCK.add(result.getString("SoTK"));
				vecLenhMuaCK.add(result.getString("Ngay"));
				vecLenhMuaCK.add(result.getString("MaVaChiSoCK"));
				vecLenhMuaCK.add(result.getFloat("Gia"));
				vecLenhMuaCK.add(result.getInt("SoLuong"));
				vecLenhMuaCK.add(result.getString("Lenh"));
				vecLenhMuaCK.add(result.getFloat("Phi"));
				vecLenhMuaCK.add(result.getFloat("Thue"));
				
				float gia=result.getFloat("Gia");
				float soLuong=result.getFloat("SoLuong");
				float phi=result.getFloat("Phi");
				float thue=result.getFloat("Thue");
				float thanhTien=(gia*soLuong)+(gia*(phi+thue));
				
				//lấy sau thành tiền 2 số
				DecimalFormat df = new DecimalFormat("#.00");
				vecLenhMuaCK.add(df.format(thanhTien));//ThanhTien
				
				tongTienMua+=thanhTien;//cộng dồn vào để tính tổng tiền mua ck
	
				dtmLenhMuaCK.addRow(vecLenhMuaCK);

			}
			
			txtTongTienMua.setText(String.valueOf(tongTienMua));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {	
			//đổ lên giao diện vào bảng những ck đã bán
			String sql="select SoTK,Ngay,MaVaChiSoCK,LoaiCKHayCS,Gia,SoLuong,Phi,Thue,ThanhTien,Lenh "
					+ "from ChungKhoan where LoaiCKHayCS=0 or LoaiCKHayCS=2";
			preStatement=conn.prepareStatement(sql);	
			result=preStatement.executeQuery();
			dtmLenhBanCK.setRowCount(0);

			int stt=1;//stt tự tăng
			float tongTienBan=0;//tính tổng tiền bán ck
			while(result.next())
			{
				Vector<Object> vecLenhBanCK=new Vector<Object>();

				vecLenhBanCK.add(stt);
				stt++;
				vecLenhBanCK.add(result.getString("SoTK"));
				vecLenhBanCK.add(result.getString("Ngay"));
				vecLenhBanCK.add(result.getString("MaVaChiSoCK"));
				vecLenhBanCK.add(result.getFloat("Gia"));
				vecLenhBanCK.add(result.getInt("SoLuong"));
				vecLenhBanCK.add(result.getString("Lenh"));
				vecLenhBanCK.add(result.getFloat("Phi"));
				vecLenhBanCK.add(result.getFloat("Thue"));
				
				float gia=result.getFloat("Gia");
				float soLuong=result.getFloat("SoLuong");
				float phi=result.getFloat("Phi");
				float thue=result.getFloat("Thue");
				float thanhTien=(gia*soLuong)+(gia*(phi+thue));
				
				//lấy sau thành tiền 2 số
				DecimalFormat df = new DecimalFormat("#.00");
				vecLenhBanCK.add(df.format(thanhTien));//ThanhTien
				
				tongTienBan+=thanhTien;//cộng dồn vào để tính tổng tiền bán ck
				
				dtmLenhBanCK.addRow(vecLenhBanCK);

			}
			
			txtTongTienBan.setText(String.valueOf(tongTienBan));
			
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
						"Bạn có chắc chắn muốn thoát Quản lý tài khoản", 
						"Thông báo thoát", 
						JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.NO_OPTION)return;
				dispose();
			}
		});
		
		btnTatCaCK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				hienThiDanhSachCK();
				txtSoTK.setEditable(true);
				txtMaCK.setEditable(true);
			}
		});
		
		btnTimCK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				txtSoTK.setEditable(true);
				txtMaCK.setEditable(true);
				if(kiemTraSoTkTrongBangCkTonTai(txtSoTK.getText()))
				{
					if(kiemTraMaCkrongBangCkTonTai(txtMaCK.getText()))
					{
						try
						{
							xuLyTimCK();
							txtSoTK.setEditable(true);
							txtMaCK.setEditable(true);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, 
								"Không tìm thấy Mã CK["+txtMaCK.getText()+"] tại tài khoản này!");
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(null, 
							"Không tìm thấy Số TK["+txtSoTK.getText()+"] này!");
				}
				
			}
		});
		
		btnMua.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				moNhapLieu();
				xuLyMuaCK();
			}
		});
		
		btnBan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				moNhapLieu();
				xuLyBanCK();
			}
		});
		
		tblLenhBanCK.addMouseListener(new MouseListener() {
			
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
				chonTKTrongBangLenhBanCK();
			}
		});
		
		tblLenhMuaCK.addMouseListener(new MouseListener() {
			
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
				chonTKTrongBangLenhMuaCK();
			}
		});
		
	}
	
	protected void xuLyTimCK() {
		// TODO Auto-generated method stub
		dongNhapLieu();
		try {	
			//đổ lên giao diện vào bảng những ck đã mua
			String sql="select SoTK,Ngay,MaVaChiSoCK,LoaiCKHayCS,Gia,SoLuong,Phi,Thue,ThanhTien,Lenh "
					+ "from ChungKhoan where SoTK=? and MaVaChiSoCK=? and (LoaiCKHayCS=0 or LoaiCKHayCS=1)";
			preStatement=conn.prepareStatement(sql);	
			preStatement.setString(1, txtSoTK.getText());
			preStatement.setString(2, txtMaCK.getText());
			result=preStatement.executeQuery();
			dtmLenhMuaCK.setRowCount(0);

			int stt=1;//stt tự tăng
			float tongTienMua=0;//tính tổng tiền mua ck
			while(result.next())
			{
				Vector<Object> vecLenhMuaCK=new Vector<Object>();

				vecLenhMuaCK.add(stt);
				stt++;
				vecLenhMuaCK.add(result.getString("SoTK"));
				vecLenhMuaCK.add(result.getString("Ngay"));
				vecLenhMuaCK.add(result.getString("MaVaChiSoCK"));
				vecLenhMuaCK.add(result.getFloat("Gia"));
				vecLenhMuaCK.add(result.getInt("SoLuong"));
				vecLenhMuaCK.add(result.getString("Lenh"));
				vecLenhMuaCK.add(result.getFloat("Phi"));
				vecLenhMuaCK.add(result.getFloat("Thue"));
				
				float gia=result.getFloat("Gia");
				float soLuong=result.getFloat("SoLuong");
				float phi=result.getFloat("Phi");
				float thue=result.getFloat("Thue");
				float thanhTien=(gia*soLuong)+(gia*(phi+thue));
				
				//lấy sau thành tiền 2 số
				DecimalFormat df = new DecimalFormat("#.00");
				vecLenhMuaCK.add(df.format(thanhTien));//ThanhTien
				
				tongTienMua+=thanhTien;//cộng dồn vào để tính tổng tiền mua ck
	
				dtmLenhMuaCK.addRow(vecLenhMuaCK);

			}
			
			txtTongTienMua.setText(String.valueOf(tongTienMua));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {	
			//đổ lên giao diện vào bảng những ck đã bán
			String sql="select SoTK,Ngay,MaVaChiSoCK,LoaiCKHayCS,Gia,SoLuong,Phi,Thue,ThanhTien,Lenh "
					+ "from ChungKhoan where SoTK=? and MaVaChiSoCK=? and (LoaiCKHayCS=0 or LoaiCKHayCS=2)";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtSoTK.getText());
			preStatement.setString(2, txtMaCK.getText());
			result=preStatement.executeQuery();
			dtmLenhBanCK.setRowCount(0);

			int stt=1;//stt tự tăng
			float tongTienBan=0;//tính tổng tiền bán ck
			while(result.next())
			{
				Vector<Object> vecLenhBanCK=new Vector<Object>();

				vecLenhBanCK.add(stt);
				stt++;
				vecLenhBanCK.add(result.getString("SoTK"));
				vecLenhBanCK.add(result.getString("Ngay"));
				vecLenhBanCK.add(result.getString("MaVaChiSoCK"));
				vecLenhBanCK.add(result.getFloat("Gia"));
				vecLenhBanCK.add(result.getInt("SoLuong"));
				vecLenhBanCK.add(result.getString("Lenh"));
				vecLenhBanCK.add(result.getFloat("Phi"));
				vecLenhBanCK.add(result.getFloat("Thue"));
				
				float gia=result.getFloat("Gia");
				float soLuong=result.getFloat("SoLuong");
				float phi=result.getFloat("Phi");
				float thue=result.getFloat("Thue");
				float thanhTien=(gia*soLuong)+(gia*(phi+thue));
				
				//lấy sau thành tiền 2 số
				DecimalFormat df = new DecimalFormat("#.00");
				vecLenhBanCK.add(df.format(thanhTien));//ThanhTien
				
				tongTienBan+=thanhTien;//cộng dồn vào để tính tổng tiền bán ck
				
				dtmLenhBanCK.addRow(vecLenhBanCK);

			}
			
			txtTongTienBan.setText(String.valueOf(tongTienBan));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public boolean kiemTraMaCkrongBangCkTonTai(String mack)
	{
		try
		{
			String sql="select * from ChungKhoan where MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, mack);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean kiemTraSoTkTrongBangCkTonTai(String sotk)
	{ 
		try
		{
			String sql="select * from ChungKhoan where SoTK=?";
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
	
	public boolean kiemTraLenhMuaCkHopLe(String lenh)
	{ 
		try
		{
			String sql="select * from ChungKhoan "
					+ "where (Lenh='Gui' or Lenh='Mua' or Lenh='Ban') "
					+ "and (LoaiCKHayCS=0 or LoaiCKHayCS=1 or LoaiCKHayCS=2)";
			preStatement=conn.prepareStatement(sql);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean kiemTraLenhBanCkHopLe(String lenh)
	{ 
		try
		{
			String sql="select * from ChungKhoan "
					+ "where (Lenh='Gui' or Lenh='Mua') "
					+ "and (LoaiCKHayCS=0 or LoaiCKHayCS=1)";
			preStatement=conn.prepareStatement(sql);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean kiemTraMaCkTheoSoTkBangCkTonTai(String sotk,String mackht)
	{
		try
		{
			String sql="select * from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, sotk);
			preStatement.setString(2, mackht);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean kiemTraChuoiChuaSo(String chuoikt)
	{	
		if(chuoikt.matches("[0-9]+"))
			return true;//chuỗi toàn số
		else
			return false;//chuỗi không chứa số		
	}
	
	private boolean kiemTraGia(String gia)
	{
		float ktGia= Float.parseFloat(gia);
		if (ktGia==(float)ktGia)
			return true;
		else
			return false;
	}

	private boolean kiemTraSoLuong(String soluong)
	{
		int ktSoLuong= Integer.parseInt(soluong);
		if (ktSoLuong==(int)ktSoLuong)
			return true;
		else
			return false;
	}
	
	protected void xuLyMuaCK() {
		//nếu số tk(đã tồn tại) thì cho kiểm tra tiếp việc thêm mới
		try {
			if(kiemTraSoTkTrongBangCkTonTai(txtSoTK.getText()))
			{
				try {
					if(kiemTraLenhMuaCkHopLe(txtLenh.getText()))
					{
						try {
							if( (!kiemTraChuoiChuaSo(txtMaCK.getText())) 
									&& (txtMaCK.getText().toString().length()==3)  
									&& (kiemTraGia(txtGia.getText()))   
									&& (kiemTraSoLuong(txtSoLuong.getText())) )
							{
								thucHienMuaCK();//mua ck
							}
							else
							{
								JOptionPane.showMessageDialog(null, 
										"Mã CK["+txtMaCK.getText()+"], "
												+ "Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
												+ "Giá["+txtGia.getText()+"], "
												+ "Số lượng["+txtSoLuong.getText()+"] "
												+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
												+ "- Ngày mua/bán CK phải có định dạng (dd/MM/yyyy);\n"
												+ "- Mã CK không được lớn hơn 3 ký tự;\n"
												+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
												+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
												+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
												+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
							}
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, 
									"Mã CK["+txtMaCK.getText()+"], "
											+ "Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
											+ "Giá["+txtGia.getText()+"], "
											+ "Số lượng["+txtSoLuong.getText()+"] "
											+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
											+ "- Ngày mua/bán CK phải có định dạng (dd/MM/yyyy);\n"
											+ "- Mã CK không được lớn hơn 3 ký tự;\n"
											+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
											+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
											+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
											+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
						} catch (HeadlessException e) {
							e.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, 
								"Lệnh ["+txtLenh.getText()+"] không hợp lệ. "
										+ "Mua chứng khoán thất bại!");
					}
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, 
							"Lệnh ["+txtLenh.getText()+"] nhập sai. "
									+ "Mua chứng khoán thất bại!");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
			else
			{
				JOptionPane.showMessageDialog(null, 
						"Số TK["+txtSoTK.getText()+"] nhập sai. "
								+ "Mua chứng khoán thất bại!");
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"] nhập sai. "
							+ "Mua chứng khoán thất bại!");
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected void xuLyBanCK() {
		//nếu số tk(đã tồn tại) thì cho kiểm tra tiếp việc thêm mới
				try {
					if(kiemTraSoTkTrongBangCkTonTai(txtSoTK.getText()))
					{
						try {
							if( (kiemTraLenhBanCkHopLe(txtLenh.getText())) )
							{
								try {
									if( (!kiemTraChuoiChuaSo(txtMaCK.getText())) 
											&& (txtMaCK.getText().toString().length()==3)  
											&& (kiemTraGia(txtGia.getText()))   
											&& (kiemTraSoLuong(txtSoLuong.getText())) )
									{
										thucHienBanCK();//mua ck
									}
									else
									{
										JOptionPane.showMessageDialog(null, 
												"Mã CK["+txtMaCK.getText()+"], "
														+ "Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
														+ "Giá["+txtGia.getText()+"], "
														+ "Số lượng["+txtSoLuong.getText()+"] "
														+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
														+ "- Ngày mua/bán CK phải có định dạng (dd/MM/yyyy);\n"
														+ "- Mã CK không được lớn hơn 3 ký tự;\n"
														+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
														+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
														+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
														+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
									}
								}catch(NumberFormatException e){
									JOptionPane.showMessageDialog(null, 
											"Mã CK["+txtMaCK.getText()+"], "
													+ "Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
													+ "Giá["+txtGia.getText()+"], "
													+ "Số lượng["+txtSoLuong.getText()+"] "
													+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
													+ "- Ngày mua/bán CK phải có định dạng (dd/MM/yyyy);\n"
													+ "- Mã CK không được lớn hơn 3 ký tự;\n"
													+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
													+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
													+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
													+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
								} catch (HeadlessException e) {
									e.printStackTrace();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, 
										"Lệnh ["+txtLenh.getText()+"] không hợp lệ. "
												+ "Mua chứng khoán thất bại!");
							}
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, 
									"Lệnh ["+txtLenh.getText()+"] nhập sai. "
											+ "Mua chứng khoán thất bại!");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}	
					else
					{
						JOptionPane.showMessageDialog(null, 
								"Số TK["+txtSoTK.getText()+"] nhập sai. "
										+ "Mua chứng khoán thất bại!");
					}
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, 
							"Số TK["+txtSoTK.getText()+"] nhập sai. "
									+ "Mua chứng khoán thất bại!");
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	protected void thucHienMuaCK() {
		// TODO Auto-generated method stub
		//nếu mã ckht/ chỉ số ck(chưa tồn tại) thì thông báo thêm mới chứng khoán
		float thanhTien=0;
		if(!kiemTraMaCkrongBangCkTonTai(txtMaCK.getText()))
		{
			//thêm mới vào bảng ChungKhoan
			int ret=JOptionPane.showConfirmDialog(null, 
					"Mã CK["+txtMaCK.getText()+"] đã tồn tại.\n"
							+ "Bạn có chắc chắn muốn mua thêm?", 
					"Lệnh mua/bán chứng khoán", 
					JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try {
				//thêm mới vào bảng ChungKhoan
				String sqlBangCK="insert into ChungKhoan values(?,?,?,?,?,?,?,?,?,?,?)";
				preStatement=conn.prepareStatement(sqlBangCK);

				preStatement.setString(1, txtSoTK.getText());
				preStatement.setString(2, txtNgayMuaBanCK.getText());//Ngay
				preStatement.setInt(3, -1);//LoaiNgay=-1 -> mua ck
				preStatement.setString(4, txtMaCK.getText());//MaVaChiSoCK
				preStatement.setInt(5, 1);//LoaiCKHayCS=1 -> mua ck
				preStatement.setFloat(6, Float.parseFloat(txtGia.getText()));//Gia	
				preStatement.setInt(7, Integer.parseInt(txtSoLuong.getText()));//SoLuong
				preStatement.setFloat(8, Float.parseFloat(txtPhi.getText()));//Phi
				preStatement.setFloat(9, Float.parseFloat(txtThue.getText()));//Thue	

				float gia=Float.parseFloat(txtGia.getText());//Gia
				float soLuong=Float.parseFloat(txtSoLuong.getText());//SoLuong
				float phi=Float.parseFloat(txtPhi.getText());//Phi
				thanhTien=(gia*soLuong)+(gia*phi);
				preStatement.setFloat(10, thanhTien);//ThanhTien
				
				preStatement.setString(11, "Mua");//Lenh='Mua'
				
				int x=preStatement.executeUpdate();
				if(x>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
				//cập nhật lại cột SuDuTienMatHT, TongTaiSanHT ở bảng TaiKhoanNapRut
				String sqlBangTK="update TienNapRut set SoDuTienMatHT=SoDuTienMatHT-? where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangTK);		
				preStatement.setFloat(1, thanhTien);//mua ck thì SoDuTienMatHT giảm đi
				preStatement.setString(2, txtSoTK.getText());

				int xBangTK=preStatement.executeUpdate();
				if(xBangTK>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
				//thêm mới vào bảng ChungKhoanMuaBan
				String sqlBangCKMB="insert into ChungKhoanMuaBan values(?,?,?,?,?,?,?,?,?,?,?)";
				preStatement=conn.prepareStatement(sqlBangCKMB);

				preStatement.setString(1, txtSoTK.getText());
				preStatement.setString(2, txtNgayMuaBanCK.getText());//Ngay
				preStatement.setInt(3, -1);//LoaiNgay=-1 -> mua ck
				preStatement.setString(4, txtMaCK.getText());//MaVaChiSoCK
				preStatement.setInt(5, 1);//LoaiCKHayCS=1 -> mua ck
				preStatement.setFloat(6, Float.parseFloat(txtGia.getText()));//Gia	
				preStatement.setInt(7, Integer.parseInt(txtSoLuong.getText()));//SoLuongHT -> này mới quan trọng
				preStatement.setFloat(8, Float.parseFloat(txtPhi.getText()));//Phi
				preStatement.setFloat(9, Float.parseFloat(txtThue.getText()));//Thue	

				float giaHT=Float.parseFloat(txtGia.getText());//GiaHT
				float soLuongHT=Float.parseFloat(txtSoLuong.getText());//SoLuongHT
				float phiHT=Float.parseFloat(txtPhi.getText());//PhiHT
				float thanhTienHT=(giaHT*soLuongHT)+(giaHT*phiHT);
				preStatement.setFloat(10, thanhTien);//ThanhTienHT
				
				preStatement.setString(11, "Mua");//Lenh='Mua'
				
				int xBangCKMB=preStatement.executeUpdate();
				if(xBangCKMB>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null,
						"Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
								+ "Giá["+txtGia.getText()+"], "
								+ "hoặc Số lượng["+txtSoLuong.getText()+"] "
								+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
								+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
								+ "- Mã CK không được vượt quá 3 ký tự số;\n"
								+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
								+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
								+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
								+ "- Phải tự nhập thông tin chính xác vào các mục cần thiết để tiến hành mua/bán chứng khoán.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else//nếu mã ckht(đã tồn tại) thì tiến hành cập nhật giá/số lượng ở bảng ChungKhoanMuaBan
			// còn ở bảng ChungKhoan thì thêm mới record để lưu lịch sử
		{
			//thêm mới vào bảng ChungKhoan
			int ret=JOptionPane.showConfirmDialog(null, 
					"Mã CK["+txtMaCK.getText()+"] đã tồn tại.\n"
							+ "Bạn có chắc chắn muốn mua thêm?", 
					"Lệnh mua/bán chứng khoán", 
					JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try {
				//thêm mới vào bảng ChungKhoan
				String sqlBangCK="insert into ChungKhoan values(?,?,?,?,?,?,?,?,?,?,?)";
				preStatement=conn.prepareStatement(sqlBangCK);

				preStatement.setString(1, txtSoTK.getText());
				preStatement.setString(2, txtNgayMuaBanCK.getText());//Ngay
				preStatement.setInt(3, -1);//LoaiNgay=-1 -> mua ck
				preStatement.setString(4, txtMaCK.getText());//MaVaChiSoCK
				preStatement.setInt(5, 1);//LoaiCKHayCS=1 -> mua ck
				preStatement.setFloat(6, Float.parseFloat(txtGia.getText()));//Gia	
				preStatement.setInt(7, Integer.parseInt(txtSoLuong.getText()));//SoLuong
				preStatement.setFloat(8, Float.parseFloat(txtPhi.getText()));//Phi
				preStatement.setFloat(9, Float.parseFloat(txtThue.getText()));//Thue	

				float gia=Float.parseFloat(txtGia.getText());//Gia
				float soLuong=Float.parseFloat(txtSoLuong.getText());//SoLuong
				float phi=Float.parseFloat(txtPhi.getText());//Phi
				thanhTien=(gia*soLuong)+(gia*phi);
				preStatement.setFloat(10, thanhTien);//ThanhTien
				
				preStatement.setString(11, "Mua");//Lenh='Mua'
				
				int x=preStatement.executeUpdate();
				if(x>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
				//cập nhật lại cột SuDuTienMatHT, TongTaiSanHT ở bảng TaiKhoanNapRut
				String sqlBangTK="update TaiKhoanNapRut set SoDuTienMatHT=SoDuTienMatHT-? where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangTK);		
				preStatement.setFloat(1, thanhTien);//mua ck thì SoDuTienMatHT giảm đi
				preStatement.setString(2, txtSoTK.getText());

				int xBangTK=preStatement.executeUpdate();
				if(xBangTK>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
				//cập nhật vào bảng ChungKhoanMuaBan
				String sqlBangCKMB="update ChungKhoanMuaBan set SoLuongHT=SoLuongHT+? where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangCKMB);
				preStatement.setFloat(1, Float.parseFloat(txtSoLuong.getText()));//mua ck thì SoLuongHT tăng lên
				preStatement.setString(2, txtSoTK.getText());
			
				int xBangCKMB=preStatement.executeUpdate();
				if(xBangCKMB>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null,
						"Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
								+ "Giá["+txtGia.getText()+"], "
								+ "hoặc Số lượng["+txtSoLuong.getText()+"] "
								+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
								+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
								+ "- Mã CK không được vượt quá 3 ký tự số;\n"
								+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
								+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
								+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
								+ "- Phải tự nhập thông tin chính xác vào các mục cần thiết để tiến hành mua/bán chứng khoán.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		dongNhapLieu();//mua/bán xong thì đóng nhập liệu lại
		
		
	}

	protected void thucHienBanCK() {
		// TODO Auto-generated method stub
		//nếu mã ckht/ chỉ số ck(đã tồn tại) thì thông báo bán chứng khoán
		float thanhTien=0;
		if(kiemTraMaCkrongBangCkTonTai(txtMaCK.getText()))
		{
			//thêm mới vào bảng ChungKhoan
			int ret=JOptionPane.showConfirmDialog(null, 
					"Mã CK["+txtMaCK.getText()+"] đã tồn tại.\n"
							+ "Bạn có chắc chắn muốn bán?", 
					"Lệnh mua/bán chứng khoán", 
					JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try {
				//thêm mới vào bảng ChungKhoan
				String sqlBangCK="insert into ChungKhoan values(?,?,?,?,?,?,?,?,?,?,?)";
				preStatement=conn.prepareStatement(sqlBangCK);

				preStatement.setString(1, txtSoTK.getText());
				preStatement.setString(2, txtNgayMuaBanCK.getText());//Ngay
				preStatement.setInt(3, -1);//LoaiNgay=-1 -> bán ck
				preStatement.setString(4, txtMaCK.getText());//MaVaChiSoCK
				preStatement.setInt(5, 2);//LoaiCKHayCS=2 -> mua ck
				preStatement.setFloat(6, Float.parseFloat(txtGia.getText()));//Gia	
				preStatement.setInt(7, Integer.parseInt(txtSoLuong.getText()));//SoLuong
				preStatement.setFloat(8, Float.parseFloat(txtPhi.getText()));//Phi
				preStatement.setFloat(9, Float.parseFloat(txtThue.getText()));//Thue	

				float gia=Float.parseFloat(txtGia.getText());//Gia
				float soLuong=Float.parseFloat(txtSoLuong.getText());//SoLuong
				float phi=Float.parseFloat(txtPhi.getText());//Phi
				float thue=Float.parseFloat(txtThue.getText());//Thue
				thanhTien=(gia*soLuong)+(gia*(phi+thue));
				preStatement.setFloat(10, thanhTien);//ThanhTien
				
				preStatement.setString(11, "Ban");//Lenh='Ban'
				
				int x=preStatement.executeUpdate();
				if(x>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
				//cập nhật lại cột SuDuTienMatHT, TongTaiSanHT ở bảng TaiKhoanNapRut
				String sqlBangTK="update TaiKhoanNapRut set SoDuTienMatHT=SoDuTienMatHT+? where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangTK);		
				preStatement.setFloat(1, thanhTien);//bán ck thì SoDuTienMatHT tăng đi
				preStatement.setString(2, txtSoTK.getText());

				int xBangTK=preStatement.executeUpdate();
				if(xBangTK>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
				//cập nhật vào bảng ChungKhoanMuaBan
				String sqlBangCKMB="update ChungKhoanMuaBan set SoLuongHT=SoLuongHT-? where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangCKMB);
				preStatement.setFloat(1, Float.parseFloat(txtSoLuong.getText()));//bán ck thì SoLuongHT giảm đi
				preStatement.setString(2, txtSoTK.getText());
			
				int xBangCKMB=preStatement.executeUpdate();
				if(xBangCKMB>0)
				{
					hienThiDanhSachCK();
					dongNhapLieu();
				}
				
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null,
						"Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
								+ "Giá["+txtGia.getText()+"], "
								+ "hoặc Số lượng["+txtSoLuong.getText()+"] "
								+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
								+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
								+ "- Mã CK không được vượt quá 3 ký tự số;\n"
								+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
								+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
								+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
								+ "- Phải tự nhập thông tin chính xác vào các mục cần thiết để tiến hành mua/bán chứng khoán.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else//nếu mã ckht(chưa tồn tại) thì thông báo bán thất bại
		{
			JOptionPane.showMessageDialog(null,
					"Ngày mua/bán CK["+txtNgayMuaBanCK.getText()+"], "
							+ "Giá["+txtGia.getText()+"], "
							+ "hoặc Số lượng["+txtSoLuong.getText()+"] "
							+ "nhập sai hoặc không tồn tại CK này. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
							+ "- Mã CK không được vượt quá 3 ký tự số;\n"
							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n");
		}	
		dongNhapLieu();//mua/bán xong thì đóng nhập liệu lại
		
		
	}


	
	protected void chonTKTrongBangLenhBanCK() {
		// TODO Auto-generated method stub
		int rowLenhBanCK=tblLenhBanCK.getSelectedRow();
		if(rowLenhBanCK==-1)return;
		
		String strSoTK=tblLenhBanCK.getValueAt(rowLenhBanCK, 1)+"";
		String strNgay=tblLenhBanCK.getValueAt(rowLenhBanCK, 2)+"";
		String strMaCK=tblLenhBanCK.getValueAt(rowLenhBanCK, 3)+"";
		String strGia=tblLenhBanCK.getValueAt(rowLenhBanCK, 4)+"";
		String strSoLuong=tblLenhBanCK.getValueAt(rowLenhBanCK, 5)+"";
		String strLenh=tblLenhBanCK.getValueAt(rowLenhBanCK,6)+"";
		String strPhi=tblLenhBanCK.getValueAt(rowLenhBanCK, 7)+"";
		String strThue=tblLenhBanCK.getValueAt(rowLenhBanCK, 8)+"";
		String strThanhTien=tblLenhBanCK.getValueAt(rowLenhBanCK, 9)+"";
		
		txtSoTK.setText(strSoTK);
		txtNgayMuaBanCK.setText(strNgay);
		txtMaCK.setText(strMaCK);
		txtGia.setText(strGia);
		txtSoLuong.setText(strSoLuong);
		txtLenh.setText(strLenh);
		txtPhi.setText(strPhi);
		txtThue.setText(strThue);
		txtThanhTien.setText(strThanhTien);
	}

	protected void chonTKTrongBangLenhMuaCK() {
		// TODO Auto-generated method stub
		int rowLenhMuaCK=tblLenhMuaCK.getSelectedRow();
		if(rowLenhMuaCK==-1)return;
		
		String strSoTK=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 1)+"";
		String strNgayMuaBanCK=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 2)+"";
		String strMaCK=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 3)+"";
		String strGia=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 4)+"";
		String strSoLuong=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 5)+"";
		String strLenh=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 6)+"";
		String strPhi=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 7)+"";
		String strThue=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 8)+"";
		String strThanhTien=tblLenhMuaCK.getValueAt(rowLenhMuaCK, 9)+"";
		
		txtSoTK.setText(strSoTK);
		txtNgayMuaBanCK.setText(strNgayMuaBanCK);
		txtMaCK.setText(strMaCK);
		txtGia.setText(strGia);
		txtSoLuong.setText(strSoLuong);
		txtLenh.setText(strLenh);
		txtPhi.setText(strPhi);
		txtThue.setText(strThue);
		txtThanhTien.setText(strThanhTien);
	}

	private void addControls() {
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnLenhMuaBan=new JPanel();
		pnLenhMuaBan.setLayout(new BorderLayout());
		con.add(pnLenhMuaBan,BorderLayout.CENTER);
		
		JPanel pnThongTinMuaBan=new JPanel();
		pnThongTinMuaBan.setLayout(new BoxLayout(
				pnThongTinMuaBan,BoxLayout.Y_AXIS));	
		pnLenhMuaBan.add(pnThongTinMuaBan,BorderLayout.CENTER);

		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Thông tin chi tiết lệnh mua bán chứng khoán");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		pnThongTinMuaBan.setBorder(borderTieuDe);
		
		JPanel pnLenhMuaBanLine1=new JPanel();
		pnLenhMuaBanLine1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoTK=new JLabel("Số TK:");
		txtSoTK=new JTextField(14);
		JLabel lblLenh=new JLabel("Lệnh:");
		txtLenh=new JTextField(14);
		JLabel lblNgayMuaBan=new JLabel("Ngày mua/bán:");
		txtNgayMuaBanCK=new JTextField(14);
		pnLenhMuaBanLine1.add(lblSoTK);
		pnLenhMuaBanLine1.add(txtSoTK);
		pnLenhMuaBanLine1.add(lblLenh);
		pnLenhMuaBanLine1.add(txtLenh);
		pnLenhMuaBanLine1.add(lblNgayMuaBan);
		pnLenhMuaBanLine1.add(txtNgayMuaBanCK);
		pnThongTinMuaBan.add(pnLenhMuaBanLine1);		
		
		JPanel pnLenhMuaBanLine2=new JPanel();
		pnLenhMuaBanLine2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMaCK=new JLabel("Mã CK:");
		txtMaCK=new JTextField(14);
		txtGia=new JTextField(14);
		JLabel lblGia=new JLabel("Giá:");
		txtSoLuong=new JTextField(14);
		JLabel lblSoLuong=new JLabel("Số lượng:");	
		pnLenhMuaBanLine2.add(lblMaCK);
		pnLenhMuaBanLine2.add(txtMaCK);
		pnLenhMuaBanLine2.add(lblGia);
		pnLenhMuaBanLine2.add(txtGia);
		pnLenhMuaBanLine2.add(lblSoLuong);
		pnLenhMuaBanLine2.add(txtSoLuong);
		pnThongTinMuaBan.add(pnLenhMuaBanLine2);
		
		JPanel pnLenhMuaBanLine3=new JPanel();
		pnLenhMuaBanLine3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPhi=new JLabel("Phí:");
		txtPhi=new JTextField(14);
		JLabel lblThue=new JLabel("Thuế:");
		txtThue=new JTextField(14);
		JLabel lblThanhTien=new JLabel("Thành tiền:");
		txtThanhTien=new JTextField(14);
		pnLenhMuaBanLine3.add(lblPhi);
		pnLenhMuaBanLine3.add(txtPhi);
		pnLenhMuaBanLine3.add(lblThue);
		pnLenhMuaBanLine3.add(txtThue);
		pnLenhMuaBanLine3.add(lblThanhTien);
		pnLenhMuaBanLine3.add(txtThanhTien);
		pnThongTinMuaBan.add(pnLenhMuaBanLine3);
		
		lblSoTK.setPreferredSize(lblMaCK.getPreferredSize());
		lblPhi.setPreferredSize(lblMaCK.getPreferredSize());
		lblLenh.setPreferredSize(lblThue.getPreferredSize());
		lblGia.setPreferredSize(lblThue.getPreferredSize());
		lblSoLuong.setPreferredSize(lblNgayMuaBan.getPreferredSize());
		lblThanhTien.setPreferredSize(lblNgayMuaBan.getPreferredSize());
		
		JPanel pnBtnLenhMuaBan=new JPanel();
		pnBtnLenhMuaBan.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnTatCaCK=new JButton("Tất cả CK");
		pnBtnLenhMuaBan.add(btnTatCaCK);
		btnTimCK=new JButton("Tìm CK");
		pnBtnLenhMuaBan.add(btnTimCK);
		btnMua=new JButton("Mua");
		pnBtnLenhMuaBan.add(btnMua);
		btnBan=new JButton("Bán");
		pnBtnLenhMuaBan.add(btnBan);
		btnThoat=new JButton("Thoát");
		pnBtnLenhMuaBan.add(btnThoat);		
		pnThongTinMuaBan.add(pnBtnLenhMuaBan);
		
		dtmLenhMuaCK=new DefaultTableModel();
		dtmLenhMuaCK.addColumn("Stt");
		dtmLenhMuaCK.addColumn("Số TK");
		dtmLenhMuaCK.addColumn("Ngày mua/bán");
		dtmLenhMuaCK.addColumn("Mã CK");
		dtmLenhMuaCK.addColumn("Giá");
		dtmLenhMuaCK.addColumn("Số lượng");
		dtmLenhMuaCK.addColumn("Lệnh");
		dtmLenhMuaCK.addColumn("Phí");
		dtmLenhMuaCK.addColumn("Thuế");
		dtmLenhMuaCK.addColumn("Thành tiền");
		tblLenhMuaCK=new JTable(dtmLenhMuaCK);
		tblLenhMuaCK.setPreferredSize(new Dimension(0,200));
		JScrollPane scTable=new JScrollPane(
				tblLenhMuaCK,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnThongTinMuaBan.add(scTable);
		
		JPanel pnTongTienMua=new JPanel();
		pnTongTienMua.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTongTienMua=new JLabel("Tổng tiền mua:");
		txtTongTienMua=new JTextField(14);
		pnTongTienMua.add(lblTongTienMua);
		pnTongTienMua.add(txtTongTienMua);
		pnThongTinMuaBan.add(pnTongTienMua);
		
		dtmLenhBanCK=new DefaultTableModel();
		dtmLenhBanCK.addColumn("Stt");
		dtmLenhBanCK.addColumn("Số TK");
		dtmLenhBanCK.addColumn("Ngày mua/bán");
		dtmLenhBanCK.addColumn("Mã CK");
		dtmLenhBanCK.addColumn("Giá");
		dtmLenhBanCK.addColumn("Số lượng");
		dtmLenhBanCK.addColumn("Lệnh");
		dtmLenhBanCK.addColumn("Phí");
		dtmLenhBanCK.addColumn("Thuế");
		dtmLenhBanCK.addColumn("Thành tiền");
		tblLenhBanCK=new JTable(dtmLenhBanCK);
		tblLenhBanCK.setPreferredSize(new Dimension(0,200));
		JScrollPane scTable2=new JScrollPane(
				tblLenhBanCK,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnThongTinMuaBan.add(scTable2);
		
		JPanel pnTongTienBan=new JPanel();
		pnTongTienBan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTongTienBan=new JLabel("Tổng tiền bán:");
		txtTongTienBan=new JTextField(14);
		pnTongTienBan.add(lblTongTienBan);
		pnTongTienBan.add(txtTongTienBan);
		pnThongTinMuaBan.add(pnTongTienBan);

	}
	
	public void showWindow()
	{
		this.setSize(680,550);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
}
