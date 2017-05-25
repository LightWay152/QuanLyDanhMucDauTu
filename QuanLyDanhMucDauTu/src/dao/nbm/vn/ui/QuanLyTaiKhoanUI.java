package dao.nbm.vn.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.nbm.vn.model.ChungKhoan;
import dao.nbm.vn.model.TaiKhoan;
import dao.nbm.vn.service.kiemTraDinhDangEmail;

public class QuanLyTaiKhoanUI extends JFrame {

	JList<TaiKhoan> listTaiKhoan;//hiện ra ds soTK
	ArrayList<ChungKhoan>dsCK;//hiện ra bảng CK

	JTextField txtTenTK,txtNamSinh,txtSoDT,
	txtSoCMND,txtSoTK,txtEmail,txtDiaChi,
	txtNgay,txtMaCK,txtGia,txtSoLuong,
	txtSoDuTienMat,txtTongTaiSan;
	
	JTextField txtTongGiaTriCK;//dùng tính tổng tài sản

	DefaultTableModel dtmChungKhoan;
	JTable tblChungKhoan;

	JButton btnThemTK,btnXoaTK,btnSuaTK,btnCapNhatTK,
	btnThemCK,btnXoaCK,btnSuaCK,btnCapNhatCK,btnThoat;

	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";

	public QuanLyTaiKhoanUI(String title)
	{
		super(title);
		addControls();
		addEvents();

		ketNoiCoSoDuLieu();
		hienThiDanhSachSoTK();

	}	
/*------------------- Bắt đầu các xử lý dữ liệu từ database ------------------*/
	
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

	public boolean kiemTraSoTkTrongBangTkTonTai(String sotk)
	{
		try
		{
			String sql="select * from TaiKhoan where SoTK=?";
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

	public boolean kiemTraMaCkTheoSoTkTrongBangCkTonTai(String sotk,String mack)
	{
		try
		{
			String sql="select * from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, sotk);
			preStatement.setString(2, mack);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean kiemTraSoCMNDTrongBangTkTonTai(String socmnd)
	{
		try
		{
			String sql="select * from TaiKhoan where SoCMND=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, socmnd);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean kiemTraSoDTTrongBangTkTonTai(String sodt)
	{
		try
		{
			String sql="select * from TaiKhoan where SoDT=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, sodt);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean kiemTraEmailTrongBangTkTonTai(String email)
	{
		try
		{
			String sql="select * from TaiKhoan where Email=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, email);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean kiemTraMaCkTheoSoTkBangCkTonTai(String sotk,String mack)
	{
		try
		{
			String sql="select * from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, sotk);
			preStatement.setString(2, mack);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean kiemTraNgayThangNam(String ngayThangNam)
	{
		if (ngayThangNam.matches("([0-9]{2})/([0-9]{2})/([1-9]{4})"))
			return true;
		else
			return false;
	}

	public boolean kiemTraChuoiChuaSo(String chuoikt)
	{	
		if(chuoikt.matches("[0-9]+"))
			return true;//chuỗi toàn số
		else
			return false;//chuỗi không chứa số		
	}

	public boolean kiemTraChuoiItNhat1So(String chuoikt1s)
	{	
		if(chuoikt1s.matches(".*\\d+.*"))
			return true;//chuỗi chứa ít nhất 1 số
		else
			return false;//chuỗi không chứa số		
	}

	public String dinhDangTienTe(String chuoitien)
	{	
		DecimalFormat df = new DecimalFormat("#,###.00");
		return chuoitien=df.format(new BigDecimal(chuoitien));	
	}

	public boolean kiemTraEmail(String email)
	{	
		kiemTraDinhDangEmail ktEmail=new kiemTraDinhDangEmail(); 
		if(ktEmail.kiemTraEmailHopLe(email))
			return true;//email hợp lệ
		else
			return false;//email không hợp lệ	
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

		btnThemCK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyThemCK();
			}
		});

		btnCapNhatCK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyCapNhatCK();
				capNhatTaiKhoan();
			}
		});

		btnXoaCK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyXoaCK();
				capNhatTaiKhoan();
			}
		});

		btnSuaCK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLySuaCK();
				capNhatTaiKhoan();
			}
		});

		btnThemTK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyThemTK();
			}
		});
		
		btnCapNhatTK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
					xuLyCapNhatTK();
					capNhatTaiKhoan();
			}
		});

		btnXoaTK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyXoaTK();
			}
		});

		btnSuaTK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLySuaTK();
			}
		});

		tblChungKhoan.addMouseListener(new MouseListener() {

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

				int rowBangCK=tblChungKhoan.getSelectedRow();
				if(rowBangCK==-1)return;

				String ngayMuaBanCK=tblChungKhoan.getValueAt(rowBangCK, 1)+"";
				String maCK=tblChungKhoan.getValueAt(rowBangCK, 2)+"";
				String gia=tblChungKhoan.getValueAt(rowBangCK, 3)+"";
				String soLuong=tblChungKhoan.getValueAt(rowBangCK, 4)+"";

				txtNgay.setText(ngayMuaBanCK);
				txtMaCK.setText(maCK);
				txtGia.setText(gia);
				txtSoLuong.setText(soLuong);

			}
		});

		listTaiKhoan.addMouseListener(new MouseListener() {

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
				
				if(listTaiKhoan.getSelectedValue()==null)return;

				dsCK=hienThiChiTietCKTheoSoTK(
						listTaiKhoan.getSelectedValue().getSoTK(),1);//LoaiCKHayCS=1 ->ck
				dtmChungKhoan.setRowCount(0);
				int stt=1;//làm số thứ tự 1,2,3,..
				float tongThanhTien=0;//tính tổng thành tiền
				for(ChungKhoan ck:dsCK)
				{
					Vector<Object> vec=new Vector<Object>();

					vec.add(stt);//stt tự tăng của bảng
					stt++;
					
					vec.add(ck.getNgay());
					txtNgay.setText(ck.getNgay());
					vec.add(ck.getMaVaChiSoCK());
					txtMaCK.setText(ck.getMaVaChiSoCK());
					vec.add(ck.getGia());
					txtGia.setText(String.valueOf(ck.getGia()));
					vec.add(ck.getSoLuong());
					txtSoLuong.setText(String.valueOf(ck.getSoLuong()));

					float gia=ck.getGia();
					float soLuong=ck.getSoLuong();
					float thanhTien=gia*soLuong;
					vec.add(thanhTien);//thành tiền

					tongThanhTien+=thanhTien;//cộng dồn vào để tính tổng giá trị CK
					dtmChungKhoan.addRow(vec);		
				}

				txtTongGiaTriCK.setText(String.valueOf(tongThanhTien));

				ArrayList<TaiKhoan>dsTK=
						hienThiChiTietTKTheoSoTK(
								listTaiKhoan.getSelectedValue().getSoTK());
				String selectedValueSoTK=listTaiKhoan.getSelectedValue().getSoTK();

				for(TaiKhoan tk:dsTK)
				{	
					txtSoTK.setText(tk.getSoTK());
					txtTenTK.setText(tk.getTenTK());
					txtNamSinh.setText(tk.getNamSinh());
					txtSoDT.setText(tk.getSoDT());
					txtSoCMND.setText(tk.getSoCMND());
					txtEmail.setText(tk.getEmail());
					txtDiaChi.setText(tk.getDiaChi());
					txtSoDuTienMat.setText(String.valueOf(tk.getSoDuTienMatBD()));
					txtTongTaiSan.setText(String.valueOf(tk.getTongTaiSanBD()));

				}
				//gọi hàm này để cập nhật lại TongTienNapRut,SoDuTienMat,TongTaiSan 
				//sau khi thao tác giao diện
				capNhatTaiKhoan();
			}
		});

	}		

	protected void capNhatTaiKhoan() {
		try {
			/*cập nhật lại 3 cột SoDuTienMat, TongTaiSan
			**ở bảng TaiKhoan sau khi thêm CK đầu tiên cho TK mới tạo
			*/
			String sqlCapNhatTK="update TaiKhoan set SoDuTienMatBD=?,TongTaiSanBD=? where SoTK=?";
			preStatement=conn.prepareStatement(sqlCapNhatTK);
			
			float soDuTienMat=Float.parseFloat(txtSoDuTienMat.getText());
			preStatement.setFloat(1, soDuTienMat);
			
			float tongGiaTriCK=Float.parseFloat(txtTongGiaTriCK.getText());
			float tongTaiSan=soDuTienMat+tongGiaTriCK;
			
			//tổng tài sản
			//tongTaiSan=tongGiaTriCK+soDuTienMat;
			preStatement.setFloat(2, tongTaiSan);
			
			preStatement.setString(3, txtSoTK.getText());
			preStatement.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();		
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
					+ "Cập nhật tài khoản thất bại!");		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	protected void xuLySuaTK() {
		// TODO Auto-generated method stub
		moNhapLieu();
		txtMaCK.setEditable(false);
		txtGia.setEditable(false);
		txtSoLuong.setEditable(false);

		try {
			//if 1: nếu tên tk không chứa ký tự số thì cho kiểm tra tiếp số tk
			if(!kiemTraChuoiItNhat1So(txtTenTK.getText()))
			{
				//if 2: kiểm tra số tk tổn tài chưa, mục sửa tk này user nhập đúng chưa
				if(kiemTraSoTkTrongBangTkTonTai(txtSoTK.getText())
						&& kiemTraChuoiChuaSo(txtSoTK.getText())
						&& (txtSoTK.getText().toString().length()==12))
				{		
					//if 3: kiểm tra định dạng dd/MM/yyyy của NamSinh
					if(kiemTraNgayThangNam(txtNamSinh.getText()))
					{	
						//if 4: nếu số cmnd(đã tồn tại) hoặc mục nhập chỉnh sửa 
						// đúng định dạng thì kiểm tra tiếp số đt
						if(kiemTraSoCMNDTrongBangTkTonTai(txtSoCMND.getText())
								&& kiemTraChuoiChuaSo(txtSoCMND.getText())
								&& (txtSoCMND.getText().toString().length()>8)
								&& (txtSoCMND.getText().toString().length()<13))
						{		
							//if 5: nếu số đt(đã tồn tại) và mục nhập để chỉnh sửa 
							// đúng định dạng thì kiểm tra tiếp email
							if(kiemTraSoDTTrongBangTkTonTai(txtSoDT.getText())
									&& kiemTraChuoiChuaSo(txtSoDT.getText())
									&& (txtSoDT.getText().toString().length()==10))
							{		
								//if 6: nếu email(đã tồn tại) và mục nhập để chỉnh sửa 
								// đúng định dạng thì kiểm tra tới số dư tm, tổng tài sản
								if(kiemTraEmailTrongBangTkTonTai(txtEmail.getText())
										&& kiemTraEmail(txtEmail.getText()))
								{
									//if 7: kiểm tra điều kiện cho số dư tiền mặt: 
									// - không chứa số âm, không quá 2 tỷ tỷ, 
									// - không chứa ký tự đặc biệt, 
									// - phải tự nhập tay toàn bộ 2 mục này
									if((Integer.parseInt(txtSoDuTienMat.getText())>=0) 
											&& (Integer.parseInt(txtSoDuTienMat.getText())<2000000000)
											&&(kiemTraChuoiChuaSo(txtSoDuTienMat.getText())) )
									{
										thucHienSuaTK();//sửa tk
									}
									else
									{
										JOptionPane.showMessageDialog(null, 
												"Số dư tiền mặt["+txtSoDuTienMat.getText()+"] "
														+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
														+ "- Không được bỏ trống 2 mục này;\n"
														+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
														+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
														+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
									}				
								}
								else//else 6: nếu email(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
								{
									JOptionPane.showMessageDialog(null, 
											"Email["+txtEmail.getText()+"] chưa tồn tại "
													+ "hoặc chưa đúng định dạng email (vd: abc123@gmail.com). "
													+ "Sửa tài khoản thất bại!");
								}	
							}
							else//else 5: nếu số đt(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
							{
								JOptionPane.showMessageDialog(null, 
										"Số DT["+txtSoDT.getText()+"] chưa tồn tại "
												+ "hoặc có chứa ký tự chữ "
												+ "hoặc chưa đủ/vượt quá 10 số. "
												+ "Sửa tài khoản thất bại!");
							}
						}
						else//else 4: nếu số cmnd(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
						{
							JOptionPane.showMessageDialog(null, 
									"Số CMND["+txtSoCMND.getText()+"] chưa tồn tại "
											+ "hoặc có chứa ký tự chữ "
											+ "hoặc chưa đủ/vượt quá khoảng [9->12] số. "
											+ "Sửa tài khoản thất bại!");	
						}	

					}	
					else//else 3: nếu năm sinh nhâp sai định dạng dd/MM/yyyy thì thông báo cập nhật tk thất bại
					{
						JOptionPane.showMessageDialog(null, 
								"Năm sinh["+txtNamSinh.getText()+"] sai định dạng dd/MM/yyyy. "
										+ "Sửa tài khoản thất bại!");
					}		
				}
				else//else 2: nếu số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
				{
					JOptionPane.showMessageDialog(null, 
							"Số TK["+txtSoTK.getText()+"] chưa tồn tại "
									+ "hoặc có chứa ký tự chữ "
									+ "hoặc chưa đủ/vượt quá 12 ký tự. "
									+ "Sửa tài khoản thất bại!");
				}
			}
			else//else 1: nếu tên tk có chứa ký tự số thì thông báo cập nhật tk thất bại
			{
				JOptionPane.showMessageDialog(null, 
						"Tên TK["+txtTenTK.getText()+"] chứa ký tự số là không hợp lệ "
								+ "hoặc không tự nhập tay lại mục cần sửa. "
								+ "Sửa tài khoản thất bại!");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Số dư tiền mặt["+txtSoDuTienMat.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Không được bỏ trống 2 mục này;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}


	}

	private void thucHienSuaTK() {
		// TODO Auto-generated method stub
		int ret=JOptionPane.showConfirmDialog(null, 
				"Số TK["+txtSoTK.getText()+"] đã tồn tại và các thông tin chỉnh sửa đã chính xác.\n"
						+ "Bạn có chắc chắn muốn sửa tài khoản?", 
						"Sửa tài khoản", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try
		{
			//sửa thông tin từ bảng TaiKhoan
			String sqlBangTKBD="update TaiKhoan set TenTK=?,NamSinh=?,SoCMND=?,DiaChi=?,SoDT=?,Email=?,SoDuTienMatBD=?,TongTaiSanBD=? where SoTK=?";
			preStatement=conn.prepareStatement(sqlBangTKBD);

			preStatement.setString(1, txtTenTK.getText());
			preStatement.setString(2, txtNamSinh.getText());
			preStatement.setString(3, txtSoCMND.getText());
			preStatement.setString(4, txtDiaChi.getText());
			preStatement.setString(5, txtSoDT.getText());
			preStatement.setString(6, txtEmail.getText());
			preStatement.setInt(7, Integer.parseInt(txtSoDuTienMat.getText()));
			
			float tongGiaTriCK=Float.parseFloat(txtTongGiaTriCK.getText());
			float soDuTienMat=Integer.parseInt(txtSoDuTienMat.getText());
			float tongTaiSan=tongGiaTriCK+soDuTienMat;
			preStatement.setFloat(8, tongTaiSan);

			preStatement.setString(9, txtSoTK.getText());

			int xBangTKBD=preStatement.executeUpdate();
			if(xBangTKBD>0)
			{
				hienThiCkSauKhiCapNhatCK();
				dongNhapLieu();
			}		
			
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, 
					"Số dư tiền mặt ["+txtSoDuTienMat.getText()+"]"
							+ "hoặc Tổng tài sản ["+txtTongTaiSan.getText()+"] "
							+ "chứa ký tự số là không hợp lệ. "
							+ "Cập nhật tài khoản thất bại!");
		}			
		catch(Exception e)
		{
			e.printStackTrace();
		}			
	}

	protected void xuLyXoaTK() {
		// TODO Auto-generated method stub
		moNhapLieu();
		//nếu số tk(đã tồn tại) thì cho tiến hành xóa
		if(kiemTraSoTkTrongBangTkTonTai(txtSoTK.getText()))
		{
			int ret=JOptionPane.showConfirmDialog(null, 
					"Số TK["+txtSoTK.getText()+"] đã tồn tại.\n"
							+ "Bạn có chắc chắn muốn xóa tài khoản?", 
							"Xóa tài khoản", 
							JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try
			{
				//xóa tài khoản và các mã ck trong bảng CK
				
				String sqlBangCKBD="delete from ChungKhoan where SoTK=(select SoTK from TaiKhoan where SoTK=?)";
				preStatement=conn.prepareStatement(sqlBangCKBD);
				preStatement.setString(1, txtSoTK.getText());
				int xBangCKBD=preStatement.executeUpdate();
				if(xBangCKBD>0)
				{
					txtNgay.setText("");
					txtMaCK.setText("");
					txtGia.setText("");
					txtSoLuong.setText("");
					txtTongGiaTriCK.setText("");
					hienThiCkSauKhiCapNhatCK();
					dongNhapLieu();
				}	

				//xóa tài khoản và các mã ck trong bảng TKBD
				String sqlBangTKBD="delete from TaiKhoan where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangTKBD);
				preStatement.setString(1, txtSoTK.getText());

				int xBangTKBD=preStatement.executeUpdate();
				if(xBangTKBD>0)
				{	
					txtMaCK.setText("");
					txtGia.setText("");
					txtSoLuong.setText("");
					txtTongGiaTriCK.setText("");
					hienThiCkSauKhiCapNhatCK();
					dongNhapLieu();
				}	
				hienThiDanhSachSoTK();
				
					
				//xóa số tài khoản trong bảng TienNapRut
				String sqlBangTNR="delete from TienNapRut where SoTK=(select SoTK from TaiKhoan where SoTK=?)";
				preStatement=conn.prepareStatement(sqlBangTNR);
				preStatement.setString(1, txtSoTK.getText());
				int xBangTNR=preStatement.executeUpdate();
				if(xBangTNR>0)
				{
					txtNgay.setText("");
					txtMaCK.setText("");
					txtGia.setText("");
					txtSoLuong.setText("");
					txtTongGiaTriCK.setText("");
					hienThiCkSauKhiCapNhatCK();
					dongNhapLieu();
				}	

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
		}
		else//nếu số tk(chưa tồn tại) thì thông báo xóa tk thất bại
		{
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"] không tồn tại. "
							+ "Xóa tài khoản thất bại!");
		}
		
	}
	
	protected void xuLyCapNhatTK()
	{
		try {
			//if 1: nếu tên tk không chứa ký tự số và không bỏ trống thì cho kiểm tra tiếp số số tk
			if( !kiemTraChuoiItNhat1So(txtTenTK.getText()) )
			{
				try {
					//if 2: nếu số tk(chưa tồn tại) và không bỏ trống thì cho kiểm tra tiếp số cmnd
					if( !kiemTraSoTkTrongBangTkTonTai(txtSoTK.getText())
							&& kiemTraChuoiChuaSo(txtSoTK.getText())
							&& (txtSoTK.getText().toString().length()==12) )
					{		
						try {
							//if 3: kiểm tra định dạng dd/MM/yyyy của NamSinh và không bỏ trống
							if( kiemTraNgayThangNam(txtNamSinh.getText()) )
							{	
								try {
									//if 4: nếu số cmnd(chưa tồn tại) và không bỏ trống thì kiểm tra tiếp số đt
									if( !kiemTraSoCMNDTrongBangTkTonTai(txtSoCMND.getText())
											&& kiemTraChuoiChuaSo(txtSoCMND.getText())
											&& (txtSoCMND.getText().toString().length()>8)
											&& (txtSoCMND.getText().toString().length()<13) )
									{		
										try {
											//if 5: nếu số đt(chưa tồn tại) và không bỏ trống thì kiểm tra tiếp email
											if( !kiemTraSoDTTrongBangTkTonTai(txtSoDT.getText())
													&& kiemTraChuoiChuaSo(txtSoDT.getText())
													&& (txtSoDT.getText().toString().length()==10) )
											{		
												try {
													//if 6: nếu email(chưa tồn tại) và không bỏ trống thì thực hiện việc cập nhật tk mới
													if( !kiemTraEmailTrongBangTkTonTai(txtEmail.getText())
															&& kiemTraEmail(txtEmail.getText()) )
													{
														try {
															//if 7: kiểm tra điều kiện cho tổng tiền nạp/rút ban đầu: 
															// - không chứa số âm, không quá 2 tỷ tỷ, 
															// - không chứa ký tự đặc biệt, 
															// - phải tự nhập tay mục này
															if( (Integer.parseInt(txtSoDuTienMat.getText())>=0) 
																	&& (Integer.parseInt(txtSoDuTienMat.getText())<2000000000)
																	&& (kiemTraChuoiChuaSo(txtSoDuTienMat.getText())) )
															{
																thucHienCapNhatTK();//cập nhật tk
															}
															else
															{
																JOptionPane.showMessageDialog(null, 
																		"Số dư tiền mặt["+txtSoDuTienMat.getText()+"] "
																				+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
																				+ "- Không được bỏ trống, nên tự nhập tay;\n"
																				+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
																				+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n");
															}
														}catch(NumberFormatException e){
															JOptionPane.showMessageDialog(null, 
																	"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
																	+ "Cập nhật tài khoản thất bại!");
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}				
													}
													else//else 6: nếu email(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
													{
														JOptionPane.showMessageDialog(null, 
																"Email["+txtEmail.getText()+"] đã tồn tại "
																		+ "hoặc bị bỏ trống, "
																		+ "hoặc chưa đúng định dạng email (vd: abc123@gmail.com). "
																		+ "Cập nhật tài khoản thất bại!");
													}
												}catch(NumberFormatException e){
													JOptionPane.showMessageDialog(null, 
															"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
															+ "Cập nhật tài khoản thất bại!");
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}	
											}
											else//else 5: nếu số đt(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
											{
												JOptionPane.showMessageDialog(null, 
														"Số DT["+txtSoDT.getText()+"] đã tồn tại "
																+ "hoặc bị bỏ trống, "
																+ "hoặc có chứa ký tự chữ, "
																+ "hoặc chưa đủ/vượt quá 10 số. "
																+ "Cập nhật tài khoản thất bại!");
											}
										}catch(NumberFormatException e){
											JOptionPane.showMessageDialog(null, 
													"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
													+ "Cập nhật tài khoản thất bại!");
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									else//else 4: nếu số cmnd(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
									{
										JOptionPane.showMessageDialog(null, 
												"Số CMND["+txtSoCMND.getText()+"] đã tồn tại "
														+ "hoặc bị bỏ trống, "
														+ "hoặc có chứa ký tự chữ, "
														+ "hoặc chưa đủ/vượt quá khoảng [9->12] số. "
														+ "Cập nhật tài khoản thất bại!");	
									}
								}catch(NumberFormatException e){
									JOptionPane.showMessageDialog(null, 
											"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
											+ "Cập nhật tài khoản thất bại!");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}	

							}	
							else//else 3: nếu năm sinh nhâp sai định dạng dd/MM/yyyy thì thông báo cập nhật tk thất bại
							{
								JOptionPane.showMessageDialog(null, 
										"Năm sinh["+txtNamSinh.getText()+"] sai định dạng dd/MM/yyyy "
												+ "hoặc bị bỏ trống. "
												+ "Cập nhật tài khoản thất bại!");
							}
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, 
									"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
									+ "Cập nhật tài khoản thất bại!");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}
					else//else 2: nếu số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
					{
						JOptionPane.showMessageDialog(null, 
								"Số TK["+txtSoTK.getText()+"] đã tồn tại "
										+ "hoặc bị bỏ trống, "
										+ "hoặc có chứa ký tự chữ, "
										+ "hoặc chưa đủ/vượt quá 12 ký tự. "
										+ "Cập nhật tài khoản thất bại!");
					}
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, 
							"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
							+ "Cập nhật tài khoản thất bại!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			else//else 1: nếu tên tk có chứa ký tự số thì thông báo cập nhật tk thất bại
			{
				JOptionPane.showMessageDialog(null, 
						"Tên TK["+txtTenTK.getText()+"] chứa ký tự số là không hợp lệ "
								+ "hoặc bị bỏ trống, "
								+ "Cập nhật tài khoản thất bại!");
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
					+ "Cập nhật tài khoản thất bại!");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
		
		

	}

	private void thucHienCapNhatTK() {
		
		try {
			float tongGiaTriCK=Float.parseFloat(txtTongGiaTriCK.getText());//khởi tạo tongGiaTriCK để tính tongTaiSan
			float soDuTienMat=Float.parseFloat(txtSoDuTienMat.getText());//khởi tạo số dư tiền mặt
			float tongTaiSan=soDuTienMat+tongGiaTriCK;//khởi tạo tongTaiSan
			
			int ret=JOptionPane.showConfirmDialog(null, 
					"Số TK["+txtSoTK.getText()+"] chưa tồn tại.\n"
							+ "Bạn có chắc chắn muốn cập nhật tài khoản?", 
							"Cập nhật tài khoản", 
							JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try
			{
				//cập nhật tài khoản mới vào bảng TaiKhoan
				String sqlBangTK="insert into TaiKhoan values(?,?,?,?,?,?,?,?,?)";
				preStatement=conn.prepareStatement(sqlBangTK);
				preStatement.setString(1, txtSoTK.getText());
				preStatement.setString(2, txtTenTK.getText());
				preStatement.setString(3, txtNamSinh.getText());
				preStatement.setString(4, txtSoDT.getText());
				preStatement.setString(5, txtSoCMND.getText());
				preStatement.setString(6, txtEmail.getText());
				preStatement.setString(7, txtDiaChi.getText());
				preStatement.setFloat(8, soDuTienMat);//ghi số dư tiền mặt
				
				tongGiaTriCK=0;//TongGiaTriCKBD=0 -> chưa mua ck nào cả
				tongTaiSan=soDuTienMat+tongGiaTriCK;
				preStatement.setFloat(9, tongTaiSan);//TongTaiSan


				int xBangTK=preStatement.executeUpdate();
				if(xBangTK>0)
				{
					hienThiDanhSachSoTK();
					dongNhapLieu();
				}

			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, 
						"Số dư tiền mặt ["+txtSoDuTienMat.getText()+"], "
								+ "Ngày bắt đầu ["+txtNgay.getText()+"], "
								+ "Giá["+txtGia.getText()+"], "
								+ "Số lượng["+txtSoLuong.getText()+"] "
								+ "không hợp lệ. "
								+ "Cập nhật tài khoản thất bại!");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
					+ "Cập nhật tài khoản thất bại!");
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	protected void xuLyThemTK() {
		// TODO Auto-generated method stub
		moNhapLieu();
		txtTenTK.setText("");
		txtNamSinh.setText("");
		txtSoCMND.setText("");
		txtDiaChi.setText("");
		txtSoDT.setText("");
		txtEmail.setText("");
		txtNgay.setText("");
		txtSoTK.setText("");
		txtSoDuTienMat.setText("0");
		txtTongTaiSan.setText("");
		txtMaCK.setText("");
		txtMaCK.setEditable(false);
		txtGia.setText("");
		txtGia.setEditable(false);
		txtSoLuong.setText("");
		txtSoLuong.setEditable(false);
		txtTongGiaTriCK.setText("");
	}

	protected void xuLySuaCK() {
		moNhapLieu();
		//nếu số tk(đã tồn tại) thì cho kiểm tra tiếp việc sửa
		if(kiemTraSoTkTrongBangCkTonTai(txtSoTK.getText()))
		{
			try {
				//if 2: kiểm tra tới số mã ck, giá, số lượng
				if((!kiemTraChuoiChuaSo(txtMaCK.getText())) 
						&& (txtMaCK.getText().toString().length()==3)  
						&& (kiemTraGia(txtGia.getText()))   
						&& (kiemTraSoLuong(txtSoLuong.getText()))
						&& (!kiemTraNgayThangNam(txtNgay.getText())) )
				{
					thucHienSuaCK();//sửa ck
				}
				else
				{
					JOptionPane.showMessageDialog(null, 
							"Ngày bắt đầu["+txtNgay.getText()+"], "
									+ "Mã CK["+txtMaCK.getText()+"], "
									+ "Giá["+txtGia.getText()+"], "
									+ "Số lượng["+txtSoLuong.getText()+"] "
									+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
									+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
									+ "- Mã CK không được vượt quá 3 ký tự số;\n"
									+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
									+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
									+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
									+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null,
						"Ngày bắt đầu["+txtNgay.getText()+"], "
								+ "Giá["+txtGia.getText()+"], "
								+ "hoặc Số lượng["+txtSoLuong.getText()+"] "
								+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
								+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
								+ "- Mã CK không được vượt quá 3 ký tự số;\n"
								+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
								+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
								+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
								+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		else//nếu số tk(chưa tồn tại) thì không cho kiểm tra tiếp việc sửa
		{
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"] không tồn tại. "
							+ "Sửa chứng khoán thất bại!");
		}	

	}

	private void thucHienSuaCK() {
		// TODO Auto-generated method stub
		int ret=JOptionPane.showConfirmDialog(null, 
				"Mã CK["+txtMaCK.getText()+"] "
						+ "trong Số TK["+txtSoTK.getText()+"] đã tồn tại.\n"
						+ "Bạn có chắc chắn muốn sửa chứng khoán?", 
						"Sửa chứng khoán", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try
		{
			//sửa trong bảng ChungKhoan
			String sql="update ChungKhoan set Ngay=?,Gia=?,SoLuong=?,ThanhTien=? where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);

			preStatement.setString(1, txtNgay.getText());
			preStatement.setFloat(2, Float.parseFloat(txtGia.getText()));
			preStatement.setInt(3, Integer.parseInt(txtSoLuong.getText()));

			float gia=Float.parseFloat(txtGia.getText());
			int soLuong=Integer.parseInt(txtSoLuong.getText());
			float thanhTien=gia*soLuong;
			preStatement.setFloat(4, thanhTien);

			preStatement.setString(5, txtSoTK.getText());
			preStatement.setString(6, txtMaCK.getText());

			int x=preStatement.executeUpdate();
			if(x>0)
			{
				hienThiCkSauKhiCapNhatCK();
				dongNhapLieu();
			}		
			
		}
		catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Mã CK["+txtMaCK.getText()+"], "
							+ "Ngày bắt đầu["+txtNgay.getText()+"], "
							+ "Giá["+txtGia.getText()+"], "
							+ "Số lượng["+txtSoLuong.getText()+"] "
							+ "không tồn tại hoặc nhập sai dữ liệu. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày mua/bán CK phải có định dạng (dd/MM/yyyy);\n"
							+ "- Mã CK không được lớn hơn 3 ký tự;\n"
							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	protected void xuLyXoaCK() {
		moNhapLieu();
		try {
			//nếu số tk(đã tồn tại) thì cho kiểm tra tiếp việc xóa
			if(kiemTraSoTkTrongBangCkTonTai(txtSoTK.getText()))
			{
				if(!kiemTraMaCkTheoSoTkTrongBangCkTonTai(txtSoTK.getText(),txtMaCK.getText()))
				{
					//nếu mã ck(chưa tồn tại) trong số tk(đã tồn tại) thì không cho xóa ck
					JOptionPane.showMessageDialog(null, 
							"Mã CK["+txtMaCK.getText()+"] "
									+ "trong Số TK["+txtSoTK.getText()+"] không tồn tại. "
									+ "Xóa chứng khoán thất bại!");
				}
				else//nếu mã ck(đã tồn tại) trong số tk(đã tồn tại) thì cho phép xóa ck
				{
					int ret=JOptionPane.showConfirmDialog(null, 
							"Mã CK["+txtMaCK.getText()+"] "
									+ "trong Số TK["+txtSoTK.getText()+"] đã tồn tại.\n"
									+ "Bạn có chắc chắn muốn xóa chứng khoán?", 
									"Xóa chứng khoán", 
									JOptionPane.YES_NO_OPTION);
					if(ret==JOptionPane.NO_OPTION)return;
					try
					{
						//xóa trong bảng ChungKhoan
						String sql="delete from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
						preStatement=conn.prepareStatement(sql);
						preStatement.setString(1, txtSoTK.getText());
						preStatement.setString(2, txtMaCK.getText());

						int x=preStatement.executeUpdate();
						if(x>0)
						{
							txtNgay.setText("");
							txtMaCK.setText("");
							txtGia.setText("");
							txtSoLuong.setText("");
							txtTongGiaTriCK.setText("");
							hienThiCkSauKhiCapNhatCK();
							dongNhapLieu();
						}	
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}	
				}		
			}
			else//nếu số tk(chưa tồn tại) thì không cho kiểm tra tiếp việc xóa
			{
				JOptionPane.showMessageDialog(null, 
						"Số TK["+txtSoTK.getText()+"] không tồn tại. "
								+ "Xóa chứng khoán thất bại!");
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Mã CK["+txtMaCK.getText()+"], "
							+ "Ngày bắt đầu["+txtNgay.getText()+"], "
							+ "Giá["+txtGia.getText()+"], "
							+ "Số lượng["+txtSoLuong.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
							+ "- Mã CK không được lớn hơn 3 ký tự;\n"
							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void xuLyCapNhatCK() {
		//nếu số tk(đã tồn tại) thì cho kiểm tra tiếp việc thêm mới
		if(kiemTraSoTkTrongBangCkTonTai(txtSoTK.getText()))
		{
			if(kiemTraMaCkTheoSoTkBangCkTonTai(txtSoTK.getText(), txtMaCK.getText()))
			{
				//nếu mã ck(đã tồn tại) trong số tk(đã tồn tại) 
				// hoặc vi phạm các điều kiện trong if thì không cho thêm mới
				JOptionPane.showMessageDialog(null, 
						"Mã CK["+txtMaCK.getText()+"] "
								+ "trong Số TK["+txtSoTK.getText()+"] đã tồn tại. "
								+ "Cập nhật chứng khoán thất bại!");
			}
			else//nếu mã ck(chưa tồn tại) trong số tk(đã tồn tại) thì cho thêm mới ck
			{
				try {
					if( (!kiemTraChuoiChuaSo(txtMaCK.getText())) 
							&& (txtMaCK.getText().toString().length()==3)  
							&& (kiemTraGia(txtGia.getText()))   
							&& (kiemTraSoLuong(txtSoLuong.getText())) )
					{
						thucHienCapNhatCK();//cập nhật ck
					}
					else
					{
						JOptionPane.showMessageDialog(null, 
								"Mã CK["+txtMaCK.getText()+"], "
										+ "Ngày bắt đầu["+txtNgay.getText()+"], "
										+ "Giá["+txtGia.getText()+"], "
										+ "Số lượng["+txtSoLuong.getText()+"] "
										+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
										+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
										+ "- Mã CK không được lớn hơn 3 ký tự;\n"
										+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
										+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
										+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
										+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
					}
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, 
							"Mã CK["+txtMaCK.getText()+"], "
									+ "Ngày bắt đầu["+txtNgay.getText()+"], "
									+ "Giá["+txtGia.getText()+"], "
									+ "Số lượng["+txtSoLuong.getText()+"] "
									+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
									+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
									+ "- Mã CK không được lớn hơn 3 ký tự;\n"
									+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
									+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
									+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
									+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
				} catch (HeadlessException e) {
					e.printStackTrace();
				}

			}		
		}
		else//nếu số tk(chưa tồn tại) thì không cho kiểm tra tiếp việc thêm mới
		{
			thucHienCapNhatCK();//cập nhật ck
		}

	}

	private void thucHienCapNhatCK() {
		// TODO Auto-generated method stub
		float tongGiaTriCK=0;
		int ret=JOptionPane.showConfirmDialog(null, 
				"Mã CK["+txtMaCK.getText()+"] "
						+ "trong Số TK["+txtSoTK.getText()+"] chưa tồn tại.\n"
						+ "Bạn có chắc chắn muốn cập nhật chứng khoán?", 
						"Cập nhật chứng khoán", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try
		{
			//cập nhật ck mới vào bảng ChungKhoan
			String sqlBangCK="insert into ChungKhoan(SoTK,Ngay,LoaiNgay,MaVaChiSoCK,LoaiCKHayCS,Gia,SoLuong,ThanhTien,Lenh) "
					+ "values((select SoTK from TaiKhoan where SoTK=?),?,?,?,?,?,?,?,?)";
			preStatement=conn.prepareStatement(sqlBangCK);

			preStatement.setString(1, txtSoTK.getText());
			preStatement.setString(2, txtNgay.getText());//ngày bắt đầu
			preStatement.setInt(3, 0);//LoaiNgay=0 -> ngày bắt đầu
			preStatement.setString(4, txtMaCK.getText());//MaVaChiSoCK
			preStatement.setInt(5, 0);//LoaiCKHayCS=0 -> ck
			preStatement.setFloat(6, Float.parseFloat(txtGia.getText()));	
			preStatement.setInt(7, Integer.parseInt(txtSoLuong.getText()));

			float gia=Float.parseFloat(txtGia.getText());//giá
			float soLuong=Float.parseFloat(txtSoLuong.getText());//số lượng
			float thanhTien=gia*soLuong;
			preStatement.setFloat(8, thanhTien);//thành tiền
			tongGiaTriCK+=thanhTien;//tính tổng giá trị CK để tính tổng tài sản
			
			preStatement.setString(9, "BD");//Lenh="BD"->thêm ck lúc bắt đầu
			
			int xBangCK=preStatement.executeUpdate();
			if(xBangCK>0)
			{
				hienThiCkSauKhiCapNhatCK();
				txtTongGiaTriCK.setText(String.valueOf(tongGiaTriCK));
				dongNhapLieu();
			}
			
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Mã CK["+txtMaCK.getText()+"], "
							+ "Ngày bắt đầu["+txtNgay.getText()+"], "
							+ "Giá["+txtGia.getText()+"], "
							+ "Số lượng["+txtSoLuong.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
							+ "- Mã CK không được lớn hơn 3 ký tự;\n"
							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void xuLyThemCK() {
		txtMaCK.setEditable(true);
		txtGia.setEditable(true);
		txtSoLuong.setEditable(true);
		txtNgay.setEditable(true);
		txtMaCK.setText("");
		txtGia.setText("");
		txtSoLuong.setText("");
		txtTongGiaTriCK.setText("");
	}

	private void dongNhapLieu() {	
		txtTenTK.setEditable(false);
		txtNamSinh.setEditable(false);
		txtSoDT.setEditable(false);
		txtSoCMND.setEditable(false);
		txtSoTK.setEditable(false);
		txtEmail.setEditable(false);
		txtDiaChi.setEditable(false);
		txtNgay.setEditable(false);
		
		txtMaCK.setEditable(false);
		txtGia.setEditable(false);
		txtSoLuong.setEditable(false);
				
		txtSoDuTienMat.setEditable(false);
		txtTongTaiSan.setEditable(false);
		
		tblChungKhoan.setEnabled(true);//không đóng trường này
		txtTongGiaTriCK.setEditable(false);
		
	}

	private void moNhapLieu() {
		txtTenTK.setEditable(true);
		txtNamSinh.setEditable(true);
		txtSoDT.setEditable(true);
		txtSoCMND.setEditable(true);
		txtSoTK.setEditable(true);
		txtEmail.setEditable(true);
		txtDiaChi.setEditable(true);
		txtNgay.setEditable(true);
		
		txtMaCK.setEditable(true);
		txtGia.setEditable(true);
		txtSoLuong.setEditable(true);
				
		txtSoDuTienMat.setEditable(true);
		txtTongTaiSan.setEditable(false);//không cho tự nhập trường này
		
		tblChungKhoan.setEnabled(true);
		txtTongGiaTriCK.setEditable(false);//không cho tự nhập trường này
	}

/*------------------- Kết thúc các xử lý dữ liệu từ database ------------------*/
	
	
/*------------------- Bắt đầu các xử lý về giao diện ------------------*/
	
	//Hàm này dùng khi bắt sự kiện click item của list trong hàm addEvents()
	public ArrayList<ChungKhoan> hienThiChiTietCKTheoSoTK(String soTK, int loai) {
		ArrayList<ChungKhoan>dsCK=new ArrayList<ChungKhoan>();
		try {
			String sql="select * from ChungKhoan where SoTK=? and LoaiCKHayCS=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, soTK);
			preStatement.setInt(2, 0);//loại là ck
	
			result=preStatement.executeQuery();
			
			int stt=1;//số thứ tự tự tăng
			while(result.next())
			{
				ChungKhoan ck=new ChungKhoan();
				ck.setSoTK(result.getString(1));
				ck.setSttCK(result.getInt(2));
				ck.setNgay(result.getString(3));
				ck.setLoaiNgay(result.getInt(4));
				ck.setMaVaChiSoCK(result.getString(5));
				ck.setLoaiCKHayCS(result.getInt(6));
				ck.setGia(result.getFloat(7));
				ck.setSoLuong(result.getInt(8));				
				ck.setThanhTien(result.getFloat(9));			
				ck.setLenh(result.getString(10));
				dsCK.add(ck);		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dsCK;
	}

	//Hàm này dùng khi bắt sự kiện click item của list trong hàm addEvents()
	public ArrayList<TaiKhoan> hienThiChiTietTKTheoSoTK(String soTK) {
		// TODO Auto-generated method stub
		ArrayList<TaiKhoan>dsTK=new ArrayList<TaiKhoan>();
		try {
			String sqlChiTietTK="select * from TaiKhoan where SoTK=?";
			preStatement=conn.prepareStatement(sqlChiTietTK);
			preStatement.setString(1, soTK);
	
			result=preStatement.executeQuery();
			while(result.next())
			{
				TaiKhoan tk=new TaiKhoan();
				tk.setSoTK(result.getString(1));
				tk.setTenTK(result.getString(2));	
				tk.setNamSinh(result.getString(3));	
				tk.setSoDT(result.getString(4));
				tk.setSoCMND(result.getString(5));
				tk.setEmail(result.getString(6));
				tk.setDiaChi(result.getString(7));
				tk.setSoDuTienMatBD(result.getInt(8));
				tk.setTongTaiSanBD(result.getFloat(9));
	
				dsTK.add(tk);		
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dsTK;
	}
	
	public void hienThiToanBoCK() 
	{
	// TODO Auto-generated method stub	
	float tongGiaTriCK = 0;
	for (int i = 0; i < tblChungKhoan.getRowCount(); i++){
		float giaTriCK = Float.parseFloat((String) tblChungKhoan.getValueAt(i, 5));
		tongGiaTriCK += giaTriCK;
	}
	txtTongGiaTriCK.setText(String.valueOf(tongGiaTriCK));

	float tongGiaTriCKLucThem=0;
		
	try {		
		String sqlChungKhoan="select * from ChungKhoan";
		preStatement=conn.prepareStatement(sqlChungKhoan);	
		result=preStatement.executeQuery();
		dtmChungKhoan.setRowCount(0);

		int i=1;//số thứ tự tự tăng
		while(result.next())
		{
			Vector<Object> vecBangCK=new Vector<Object>();
			
			vecBangCK.add(i);//stt
			i++;
			vecBangCK.add(result.getString("Ngay"));
			vecBangCK.add(result.getString("MaVaChiSoCK"));
			vecBangCK.add(result.getFloat("Gia"));
			vecBangCK.add(result.getInt("SoLuong"));

			float gia=result.getFloat("Gia");//giá
			float soLuong=result.getInt("SoLuong");//số lượng
			float thanhTien=gia*soLuong;//thành tiền
			
			DecimalFormat df = new DecimalFormat("#.00");//lấy sau thành tiền 2 số
			vecBangCK.add(df.format(thanhTien));//thành tiền
			
			tongGiaTriCKLucThem+=thanhTien;//+ thêm giá trị ck mới thêm
			
			dtmChungKhoan.addRow(vecBangCK);

		}
		float soDuTienMat=Float.parseFloat(txtSoDuTienMat.getText());
		float tongGiaTriCKSauKhiThem=tongGiaTriCK+tongGiaTriCKLucThem;
		float tongTaiSanSauKhiThem=soDuTienMat+tongGiaTriCKSauKhiThem;
		txtTongGiaTriCK.setText(String.valueOf(tongGiaTriCKSauKhiThem));
		txtTongTaiSan.setText(String.valueOf(tongTaiSanSauKhiThem));

	}catch(NullPointerException e){
		System.out.println(""+e);
	} catch (SQLException e) {
		System.out.println(""+e);
	}catch(Exception e){
		e.printStackTrace();
	}
}	

	public void hienThiCkSauKhiCapNhatCK() {
		// TODO Auto-generated method stub
		try {		
			String sql="select * from ChungKhoan where SoTK=? and LoaiCKHayCS=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtSoTK.getText());
			preStatement.setInt(2, 0);//LoaiCKHayCS=0 -> ck
			result=preStatement.executeQuery();
			dtmChungKhoan.setRowCount(0);

			//lấy sau thành tiền 2 số
			DecimalFormat df = new DecimalFormat("#.00");

			float tongGiaTriCK = 0;
			for (int i = 0; i < tblChungKhoan.getRowCount(); i++){
				float giaTriCK = Float.parseFloat((String) tblChungKhoan.getValueAt(i, 5));
				tongGiaTriCK += giaTriCK;
			}
			txtTongGiaTriCK.setText(String.valueOf(tongGiaTriCK));

			float tongGiaTriCKLucThem=0;

			int stt=1;//stt tự tăng theo code, không dính tới csdl
			while(result.next())
			{
				Vector<Object> vecBangCK=new Vector<Object>();
				vecBangCK.add(stt);//cột stt
				stt++;//tăng stt lên 1
				vecBangCK.add(result.getString("Ngay"));
				vecBangCK.add(result.getString("MaVaChiSoCK"));
				vecBangCK.add(df.format(result.getFloat("Gia")));
				vecBangCK.add(result.getInt("SoLuong"));

				float gia=result.getFloat("Gia");//giá
				float soLuong=result.getInt("SoLuong");//số lượng
				float thanhTien=gia*soLuong;//thành tiền

				tongGiaTriCKLucThem+=thanhTien;//+ thêm giá trị ck mới thêm

				vecBangCK.add(df.format(thanhTien));//thành tiền
				dtmChungKhoan.addRow(vecBangCK);
			}
			float soDuTienMat=Float.parseFloat(txtSoDuTienMat.getText());
			float tongGiaTriCKSauKhiThem=tongGiaTriCK+tongGiaTriCKLucThem;
			float tongTaiSanSauKhiThem=soDuTienMat+tongGiaTriCKSauKhiThem;
			txtTongGiaTriCK.setText(String.valueOf(tongGiaTriCKSauKhiThem));
			txtTongTaiSan.setText(String.valueOf(tongTaiSanSauKhiThem));

		}catch(Exception e){
			e.printStackTrace();
		}	
	}	

	public void hienThiDanhSachSoTK() {		
		dongNhapLieu();//chỉ view
		try {		
			Vector<TaiKhoan>vecTaiKhoan=new Vector<TaiKhoan>();
			try {
				String sql="select * from TaiKhoan";
				statement = conn.createStatement();
				result = statement.executeQuery(sql);
				while(result.next())
				{
					TaiKhoan tk=new TaiKhoan();
					tk.setSoTK(result.getString(1));
					tk.setTenTK(result.getString(2));
					tk.setNamSinh(result.getString(3));
					tk.setSoDT(result.getString(4));
					tk.setSoCMND(result.getString(5));
					tk.setEmail(result.getString(6));
					tk.setDiaChi(result.getString(7));					
					tk.setSoDuTienMatBD(result.getInt(8));
					tk.setTongTaiSanBD(result.getFloat(9));
					vecTaiKhoan.add(tk);
					listTaiKhoan.setListData(vecTaiKhoan);		
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ketNoiCoSoDuLieu(){
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl="jdbc:sqlserver://"+strServer+":1433;databaseName="+strDatabase+";integratedSecurity=true;";
			conn=DriverManager.getConnection(connectionUrl);
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	
	private void addControls() {
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnLeft=new JPanel();
		pnLeft.setPreferredSize(new Dimension(250, 0));
		JPanel pnRight=new JPanel();

		JSplitPane sp=new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,pnLeft,pnRight);
		sp.setOneTouchExpandable(true);
		con.add(sp,BorderLayout.CENTER);

		pnLeft.setLayout(new BorderLayout());
		listTaiKhoan=new JList<TaiKhoan>();
		JScrollPane scListTaiKhoan=new JScrollPane(
				listTaiKhoan,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnLeft.add(scListTaiKhoan,BorderLayout.CENTER);

		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Danh sách tài khoản");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		listTaiKhoan.setBorder(borderTieuDe);

		JPanel pnBtnTK=new JPanel();
		btnThemTK=new JButton("Thêm TK");
		btnXoaTK=new JButton("Xóa TK");
		btnSuaTK=new JButton("Sửa TK");
		btnCapNhatTK=new JButton("Cập nhật TK");
		pnBtnTK.add(btnThemTK);
		pnBtnTK.add(btnXoaTK);
		pnBtnTK.add(btnSuaTK);
		pnBtnTK.add(btnCapNhatTK);
		pnLeft.add(pnBtnTK,BorderLayout.SOUTH);

		pnRight.setLayout(new BorderLayout());
		JPanel pnTopOfRight=new JPanel();
		pnTopOfRight.setLayout(new BoxLayout(
				pnTopOfRight,BoxLayout.Y_AXIS));	
		pnRight.add(pnTopOfRight,BorderLayout.NORTH);	

		JPanel pnTitle=new JPanel();
		pnTitle.setLayout(new FlowLayout());
		JLabel lblTieuDe=new JLabel("Thông tin chi tiết tài khoản");
		lblTieuDe.setForeground(Color.BLUE);
		pnTitle.add(lblTieuDe);
		pnTopOfRight.add(pnTitle);

		JPanel pnTopOfRightLine1=new JPanel();
		pnTopOfRightLine1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTenTK=new JLabel("1. Tên TK:");
		txtTenTK=new JTextField(12);
		JLabel lblNamSinh=new JLabel("2. Năm sinh:");
		txtNamSinh=new JTextField(12);
		JLabel lblSoDT=new JLabel("3. Số ĐT:");
		txtSoDT=new JTextField(12);
		pnTopOfRightLine1.add(lblTenTK);
		pnTopOfRightLine1.add(txtTenTK);
		pnTopOfRightLine1.add(lblNamSinh);
		pnTopOfRightLine1.add(txtNamSinh);	
		pnTopOfRightLine1.add(lblSoDT);
		pnTopOfRightLine1.add(txtSoDT);
		pnTopOfRight.add(pnTopOfRightLine1);

		JPanel pnTopOfRightLine2=new JPanel();
		pnTopOfRightLine2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoCMND=new JLabel("4. Số CMND:");
		txtSoCMND=new JTextField(12);
		JLabel lblEmail=new JLabel("5. Email:");
		txtEmail=new JTextField(12);
		JLabel lblNgay=new JLabel("6. Ngày bắt đầu:");
		txtNgay=new JTextField(12);
		pnTopOfRightLine2.add(lblSoCMND);
		pnTopOfRightLine2.add(txtSoCMND);
		pnTopOfRightLine2.add(lblEmail);
		pnTopOfRightLine2.add(txtEmail);
		pnTopOfRightLine2.add(lblNgay);
		pnTopOfRightLine2.add(txtNgay);
		pnTopOfRight.add(pnTopOfRightLine2);
		
		JPanel pnTopOfRightLine3=new JPanel();
		pnTopOfRightLine3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblDiaChi=new JLabel("6. Địa chỉ:");
		txtDiaChi=new JTextField(56);
		pnTopOfRightLine3.add(lblDiaChi);
		pnTopOfRightLine3.add(txtDiaChi);
		pnTopOfRight.add(pnTopOfRightLine3);
			
		JPanel pnTopOfRightLine4=new JPanel();
		pnTopOfRightLine4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoTK=new JLabel("7. Số TK:");
		txtSoTK=new JTextField(12);
		JLabel lblSoDuTienMat=new JLabel("8. Số dư tiền mặt:");
		txtSoDuTienMat=new JTextField(12);
		JLabel lblTongTaiSan=new JLabel("9. Tổng tài sản:");
		txtTongTaiSan=new JTextField(12);
		pnTopOfRightLine4.add(lblSoTK);
		pnTopOfRightLine4.add(txtSoTK);
		pnTopOfRightLine4.add(lblSoDuTienMat);
		pnTopOfRightLine4.add(txtSoDuTienMat);
		pnTopOfRightLine4.add(lblTongTaiSan);
		pnTopOfRightLine4.add(txtTongTaiSan);
		pnTopOfRight.add(pnTopOfRightLine4);		

		JPanel pnTopOfRightLine5=new JPanel();
		pnTopOfRightLine5.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMaCK=new JLabel("11. Mã CK:");
		txtMaCK=new JTextField(12);
		JLabel lblGia=new JLabel("12. Giá:");
		txtGia=new JTextField(12);
		JLabel lblSoLuong=new JLabel("13. Số lượng:");
		txtSoLuong=new JTextField(12);
		pnTopOfRightLine5.add(lblMaCK);
		pnTopOfRightLine5.add(txtMaCK);
		pnTopOfRightLine5.add(lblGia);
		pnTopOfRightLine5.add(txtGia);
		pnTopOfRightLine5.add(lblSoLuong);
		pnTopOfRightLine5.add(txtSoLuong);	
		pnTopOfRight.add(pnTopOfRightLine5);
		
		lblTenTK.setPreferredSize(lblSoCMND.getPreferredSize());
		lblDiaChi.setPreferredSize(lblSoCMND.getPreferredSize());
		lblSoTK.setPreferredSize(lblSoCMND.getPreferredSize());
		lblMaCK.setPreferredSize(lblSoCMND.getPreferredSize());
		lblNamSinh.setPreferredSize(lblSoDuTienMat.getPreferredSize());		
		lblEmail.setPreferredSize(lblSoDuTienMat.getPreferredSize());
		lblGia.setPreferredSize(lblSoDuTienMat.getPreferredSize());
		lblSoDT.setPreferredSize(lblNgay.getPreferredSize());		
		lblTongTaiSan.setPreferredSize(lblNgay.getPreferredSize());
		lblSoLuong.setPreferredSize(lblNgay.getPreferredSize());

		JPanel pnBtnCK=new JPanel();
		pnBtnCK.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnThemCK=new JButton("Thêm CK");
		btnXoaCK=new JButton("Xóa CK");
		btnSuaCK=new JButton("Sửa CK");
		btnCapNhatCK=new JButton("Cập nhật CK");
		pnBtnCK.add(btnThemCK);
		pnBtnCK.add(btnXoaCK);
		pnBtnCK.add(btnSuaCK);
		pnBtnCK.add(btnCapNhatCK);	
		pnTopOfRight.add(pnBtnCK);	

		JPanel pnBottomOfRight=new JPanel();
		pnBottomOfRight.setLayout(new BorderLayout());
		pnRight.add(pnBottomOfRight,BorderLayout.SOUTH);
		pnBottomOfRight.setPreferredSize(new Dimension(0, 400));

		dtmChungKhoan=new DefaultTableModel();

		dtmChungKhoan.addColumn("Stt");//stt này tự tăng theo biến i, không liên quan csdl
		dtmChungKhoan.addColumn("Ngày cập nhật CK");
		dtmChungKhoan.addColumn("Mã CK");
		dtmChungKhoan.addColumn("Giá");
		dtmChungKhoan.addColumn("Số lượng");
		dtmChungKhoan.addColumn("Thành tiền");
		tblChungKhoan=new JTable(dtmChungKhoan);
		tblChungKhoan.setPreferredSize(new Dimension(0, 350));
		JScrollPane scTableChungKhoan=new JScrollPane(
				tblChungKhoan,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnBottomOfRight.add(scTableChungKhoan,BorderLayout.CENTER);		

		JPanel pnButtonCK=new JPanel();
		pnButtonCK.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTongGiaTriCK=new JLabel("Tổng giá trị chứng khoán:");
		txtTongGiaTriCK=new JTextField(12);
		btnThoat=new JButton("Thoát");	
		pnButtonCK.add(lblTongGiaTriCK);
		pnButtonCK.add(txtTongGiaTriCK);
		pnButtonCK.add(btnThoat);
		pnBottomOfRight.add(pnButtonCK,BorderLayout.SOUTH);

	}

	public void showWindow()
	{
		this.setSize(1090,650);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

/*------------------- Kết thúc các xử lý về giao diện -------------------*/
	
}
