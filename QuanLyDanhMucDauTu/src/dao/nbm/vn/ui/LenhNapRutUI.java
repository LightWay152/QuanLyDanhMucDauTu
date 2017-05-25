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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.nbm.vn.model.TaiKhoan;

public class LenhNapRutUI extends JFrame {

	JTextField txtSoTK,txtNgayNapRut,txtLenhNapRut,
	txtSoTienNapRut,txtSoDuTienMat,txtTongTaiSan;
	
	JTextField txtTongTienNapRut;

	JButton btnTatCaTK,btnTimTK,btnNap,btnRut,btnThoat;

	DefaultTableModel dtmTaiKhoan;
	JTable tblTaiKhoan;

	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";

	public LenhNapRutUI(String title)
	{
		super(title);
		addControls();
		addEvents();

		ketNoiCoSoDuLieu();
		hienThiThongTinLenhNapRut();
	}

	private void moNhapLieu()
	{
		txtSoTK.setEditable(true);
		txtNgayNapRut.setEditable(true);
		txtLenhNapRut.setEditable(true);
		txtSoTienNapRut.setEditable(true);
		tblTaiKhoan.setEnabled(true);
	}

	private void dongNhapLieu()
	{
		txtSoTK.setEditable(false);
		txtNgayNapRut.setEditable(false);
		txtLenhNapRut.setEditable(false);
		txtSoTienNapRut.setEditable(false);
		txtSoDuTienMat.setEditable(false);
		txtTongTaiSan.setEditable(false);
		txtTongTienNapRut.setEditable(false);
	}

	private void hienThiThongTinLenhNapRut() {
		// TODO Auto-generated method stub	
		dongNhapLieu();
		float soDuTienMatSauKhiNapRut=0;//số dư tiền mặt hiện có của tài khoản sau khi nạp/rút
		try {		
			String sql="select * from TongTaiSan";
			preStatement=conn.prepareStatement(sql);
			result=preStatement.executeQuery();
			dtmTaiKhoan.setRowCount(0);

			int stt=1;//stt tự tăng
			//lấy sau thành tiền 2 số
			DecimalFormat df = new DecimalFormat("#.00");
			
			float tongTienNapRut=0;//tính tổng tiền nạp rút		
			for (int i = 0; i < tblTaiKhoan.getRowCount(); i++){
				float soTienNapRut = Float.parseFloat((String) tblTaiKhoan.getValueAt(i, 4));
				tongTienNapRut += soTienNapRut;
			}
			txtTongTienNapRut.setText(df.format(tongTienNapRut));
			
			while(result.next())
			{
				Vector<Object> vecLenhNapRut=new Vector<Object>();

				vecLenhNapRut.add(stt);
				stt++;
				//theo dữ liệu trong db ngày này là ngày nạp/rút tiền
				vecLenhNapRut.add(result.getString("SoTK"));
				vecLenhNapRut.add(result.getString("Ngay"));
				vecLenhNapRut.add(result.getString("Lenh"));
				vecLenhNapRut.add(result.getFloat("SoTienNapRut"));
				vecLenhNapRut.add(result.getFloat("SoDuTienMat"));
				vecLenhNapRut.add(result.getFloat("TongTaiSan"));
				
				float soDuTienMat=result.getFloat("SoDuTienMat");
				float tongTaiSan=result.getFloat("TongTaiSan");			
				float soTienNapRut=result.getFloat("SoTienNapRut");
						
				txtSoTienNapRut.setText(df.format(soTienNapRut));
				txtSoDuTienMat.setText(df.format(soDuTienMat));
				txtTongTaiSan.setText(df.format(tongTaiSan));
				
				tongTienNapRut+=soTienNapRut;//cộng dồn vào để tính tổng tiền nạp rút
			
				dtmTaiKhoan.addRow(vecLenhNapRut);
			}
			
			txtTongTienNapRut.setText(df.format(tongTienNapRut));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void capNhatSoDuTienMatHTSauKhiRut() {
		//sql ở đây
		int ret=JOptionPane.showConfirmDialog(null, 
				"Số TK["+txtSoTK.getText()+"] đã tồn tại.\n"
						+ "Bạn có chắc chắn muốn cập nhật số dư tiền mặt cho tài khoản này?", 
						"Cập nhật tài khoản", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try {
			//cập nhật record mới cho bảng TongTaiSan
			String sqlBangCKHT="insert into TongTaiSan values(?,?,?,?,?,?)";
			preStatement=conn.prepareStatement(sqlBangCKHT);

			preStatement.setString(1, txtSoTK.getText());
			preStatement.setString(2, txtNgayNapRut.getText());//Ngay	
			preStatement.setString(3, "Rut");//Lenh='Rut'
			preStatement.setFloat(4, Float.parseFloat(txtSoTienNapRut.getText()));//SoTienNapRut	
			preStatement.setFloat(5, Float.parseFloat(txtSoDuTienMat.getText()));
			
			float soTienNapRut=Float.parseFloat(txtSoTienNapRut.getText());
			float soDuTienMat=Float.parseFloat(txtSoDuTienMat.getText());
			float tongTaiSan=soDuTienMat-soTienNapRut;//rút tiền
			
			preStatement.setFloat(6, tongTaiSan);//thêm record mới về lúc rút tiền
				
			int xBangCK=preStatement.executeUpdate();
			if(xBangCK>0)
			{
				hienThiThongTinLenhNapRut();
				dongNhapLieu();
			}
			
			
			//sửa cột SoDuTienMatHT bảng TaiKhoanNapRut lúc rút tiền
			String sqlBangTKNR1="update TaiKhoanNapRut set SoDuTienMatHT=SoDuTienMatHT-? where SoTK=?";
			preStatement=conn.prepareStatement(sqlBangTKNR1);

			float soDuTienMatHT=Float.parseFloat(txtSoDuTienMat.getText());
			float soTienNapRutHT=Float.parseFloat(txtSoTienNapRut.getText());
			float tongTaiSanHT=soDuTienMat-soTienNapRut;
				
			preStatement.setFloat(1, soDuTienMatHT);
			preStatement.setString(2, txtSoTK.getText());

			int xBangTKNR1=preStatement.executeUpdate();
			if(xBangTKNR1>0)
			{
				hienThiThongTinLenhNapRut();
				dongNhapLieu();
			}
			
			//sửa TongTaiSanHT từ bảng TaiKhoanNapRut lúc rút tiền
			String sqlBangTKNR2="update TaiKhoanNapRut set TongTaiSanHT=TongTaiSanHT-? where SoTK=?";
			preStatement=conn.prepareStatement(sqlBangTKNR2);

			soDuTienMatHT=Float.parseFloat(txtSoDuTienMat.getText());
			soTienNapRutHT=Float.parseFloat(txtSoTienNapRut.getText());
			tongTaiSanHT=soDuTienMat-soTienNapRut;
				
			preStatement.setFloat(1, tongTaiSanHT);
			preStatement.setString(2, txtSoTK.getText());

			int xBangTKNR2=preStatement.executeUpdate();
			if(xBangTKNR2>0)
			{
				hienThiThongTinLenhNapRut();
				dongNhapLieu();
			}


		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"], "
							+ "Ngày nạp/rút["+txtNgayNapRut.getText()+"], "
							+ "Lệnh["+txtLenhNapRut.getText()+"], "
							+ "Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
							+ "nhập không hợp lệ. "
							+ "Cập nhật tài khoản thất bại!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void capNhatSoDuTienMatHTSauKhiNap() {
		//sql ở đây
		int ret=JOptionPane.showConfirmDialog(null, 
				"Số TK["+txtSoTK.getText()+"] đã tồn tại.\n"
						+ "Bạn có chắc chắn muốn cập nhật số dư tiền mặt cho tài khoản này?", 
						"Cập nhật tài khoản", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try {
			//cập nhật record mới cho bảng TongTaiSan
			String sqlBangCKHT="insert into TongTaiSan values(?,?,?,?,?,?)";
			preStatement=conn.prepareStatement(sqlBangCKHT);

			preStatement.setString(1, txtSoTK.getText());
			preStatement.setString(2, txtNgayNapRut.getText());//Ngay	
			preStatement.setString(3, "Nap");//Lenh='Nap'
			preStatement.setFloat(4, Float.parseFloat(txtSoTienNapRut.getText()));//SoTienNapRut	
			preStatement.setFloat(5, Float.parseFloat(txtSoDuTienMat.getText()));
			
			float soTienNapRut=Float.parseFloat(txtSoTienNapRut.getText());
			float soDuTienMat=Float.parseFloat(txtSoDuTienMat.getText());
			float tongTaiSanSauNap=soTienNapRut+soDuTienMat;
			
			preStatement.setFloat(6, tongTaiSanSauNap);//thêm record mới về lúc nạp tiền
			
			int xBangCKHT=preStatement.executeUpdate();
			if(xBangCKHT>0)
			{
				hienThiThongTinLenhNapRut();
				dongNhapLieu();
			}
			
			
			//cập nhật cột SoDuTienMatHT bảng TaiKhoanNapRut
			String sqlBangTKNR1="update TaiKhoanNapRut set SoDuTienMatHT=SoDuTienMatHT+? where SoTK=?";
			preStatement=conn.prepareStatement(sqlBangTKNR1);

			float soTienNapRutHT=Float.parseFloat(txtSoTienNapRut.getText());
			float soDuTienMatHT=Float.parseFloat(txtSoDuTienMat.getText());
			float tongTaiSanHT=soDuTienMatHT-soTienNapRutHT;
				
			preStatement.setFloat(1, soDuTienMatHT);
			preStatement.setString(2, txtSoTK.getText());

			int xBangTKNR1=preStatement.executeUpdate();
			if(xBangTKNR1>0)
			{
				hienThiThongTinLenhNapRut();
				dongNhapLieu();
			}
			
			//cập nhật cột TongTaiSanHT bảng TaiKhoanNapRut
			String sqlBangTKNR2="update TaiKhoanNapRut set TongTaiSanHT=TongTaiSanHT+? where SoTK=?";
			preStatement=conn.prepareStatement(sqlBangTKNR2);

			soTienNapRutHT=Float.parseFloat(txtSoTienNapRut.getText());
			soDuTienMatHT=Float.parseFloat(txtSoDuTienMat.getText());
			tongTaiSanHT=soDuTienMatHT-soTienNapRutHT;
				
			preStatement.setFloat(1, tongTaiSanHT);
			preStatement.setString(2, txtSoTK.getText());

			int xBangTKNR2=preStatement.executeUpdate();
			if(xBangTKNR2>0)
			{
				hienThiThongTinLenhNapRut();
				dongNhapLieu();
			}

		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"], "
							+ "Ngày nạp/rút["+txtNgayNapRut.getText()+"], "
							+ "Lệnh["+txtLenhNapRut.getText()+"], "
							+ "Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
							+ "nhập không hợp lệ. "
							+ "Cập nhật tài khoản thất bại!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
						"Bạn có chắc chắn muốn thoát Lệnh nạp/rút tiền?", 
						"Thông báo thoát", 
						JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.NO_OPTION)return;
				dispose();
			}
		});
		
		btnTatCaTK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub			
				hienThiThongTinLenhNapRut();
				txtSoTK.setEditable(true);
			}
		});
		
		btnTimTK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				txtSoTK.setEditable(true);
				if(kiemTraSoTkTrongBangTKNRTonTai(txtSoTK.getText()))
				{
					xuLyTimTK();
				}
				else
				{
					JOptionPane.showMessageDialog(null, 
							"Số TK["+txtSoTK.getText()+"] không tồn tại. "
									+ "Vui lòng kiểm tra lại!");
				}
				
			}
		});

		btnNap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				moNhapLieu();
				xuLyNap();
			}
		});
		
		btnRut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				moNhapLieu();
				xuLyRut();
			}
		});

		tblTaiKhoan.addMouseListener(new MouseListener() {

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
				int rowTaiKhoan=tblTaiKhoan.getSelectedRow();
				if(rowTaiKhoan==-1)return;

				String soTK=tblTaiKhoan.getValueAt(rowTaiKhoan, 1)+"";
				String ngayNapRut=tblTaiKhoan.getValueAt(rowTaiKhoan, 2)+"";
				String lenhNapRut=tblTaiKhoan.getValueAt(rowTaiKhoan, 3)+"";
				String soTienNapRut=tblTaiKhoan.getValueAt(rowTaiKhoan, 4)+"";

				txtSoTK.setText(soTK);
				txtNgayNapRut.setText(ngayNapRut);
				txtLenhNapRut.setText(lenhNapRut);
				txtSoTienNapRut.setText(soTienNapRut);

			}
		});

	}

	protected void xuLyTimTK() {
		// TODO Auto-generated method stub
		txtSoTK.setEditable(true);
		float soDuTienMatSauKhiNapRut=0;//số dư tiền mặt hiện có của tài khoản sau khi nạp/rút
		try {		
			String sql="select * from TongTaiSan where SoTK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtSoTK.getText());
			result=preStatement.executeQuery();
			dtmTaiKhoan.setRowCount(0);

			int stt=1;//stt tự tăng
			//lấy sau thành tiền 2 số
			DecimalFormat df = new DecimalFormat("#.00");
			
			float tongTienNapRut=0;//tính tổng tiền nạp rút		
			for (int i = 0; i < tblTaiKhoan.getRowCount(); i++){
				float soTienNapRut = Float.parseFloat((String) tblTaiKhoan.getValueAt(i, 4));
				tongTienNapRut += soTienNapRut;
			}
			txtTongTienNapRut.setText(df.format(tongTienNapRut));
			
			while(result.next())
			{
				Vector<Object> vecLenhNapRut=new Vector<Object>();

				vecLenhNapRut.add(stt);
				stt++;
				//theo dữ liệu trong db ngày này là ngày nạp/rút tiền
				vecLenhNapRut.add(result.getString("SoTK"));
				vecLenhNapRut.add(result.getString("Ngay"));
				vecLenhNapRut.add(result.getString("Lenh"));
				vecLenhNapRut.add(result.getFloat("SoTienNapRut"));
				vecLenhNapRut.add(result.getFloat("SoDuTienMat"));
				vecLenhNapRut.add(result.getFloat("TongTaiSan"));
				
				float soDuTienMat=result.getFloat("SoDuTienMat");
				float tongTaiSan=result.getFloat("TongTaiSan");			
				float soTienNapRut=result.getFloat("SoTienNapRut");
						
				txtSoTienNapRut.setText(df.format(soTienNapRut));
				txtSoDuTienMat.setText(df.format(soDuTienMat));
				txtTongTaiSan.setText(df.format(tongTaiSan));

				tongTienNapRut+=soTienNapRut;//cộng dồn vào để tính tổng tiền nạp rút
				
				dtmTaiKhoan.addRow(vecLenhNapRut);
			}
			
			txtTongTienNapRut.setText(df.format(tongTienNapRut));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void xuLyRut() {
		try {
			//if 1: nếu số tk(đã tồn tại) thì cho kiểm tra tiếp ngày nạp/rút
			if(kiemTraSoTkTrongBangTKNRTonTai(txtSoTK.getText())
					&& kiemTraChuoiChuaSo(txtSoTK.getText())
					&& (txtSoTK.getText().toString().length()==12))
			{		
				//if 2: kiểm tra định dạng dd/MM/yyyy của txtNgayNapRut
				if(!kiemTraNgayThangNam(txtNgayNapRut.getText()))
				{	
					//if 3: kiểm tra điều kiện cho số dư tiền nạp/rút: 
					// - không chứa số âm, không quá 2 tỷ tỷ, 
					// - không chứa ký tự đặc biệt, 
					// - phải tự nhập tay mục này
					if((Float.parseFloat(txtSoTienNapRut.getText())>0 
							&& Float.parseFloat(txtSoTienNapRut.getText())<2000000000)
							&&(kiemTraChuoiChuaSo(txtSoTienNapRut.getText())))
					{
						try {
							//if 4: kiểm tra LenhNapRut đúng định dạng hay không
							if( (!kiemTraChuoiChuaSo(txtLenhNapRut.getText())) 
									&& (txtLenhNapRut.getText().toString().length()==3)
									&& (txtLenhNapRut.getText().equals("Rut")) )
							{
								//cập nhật 1 record mới cho cột SoDuTienMatHT 
								//ở bảng TaiKhoanHienTai -> này để lệnh rút tiền
								capNhatSoDuTienMatHTSauKhiRut();
							}
							else//else 4: txtLenhNapRut không đúng định dạng thì thông báo nạp tiền thất bại
							{
								JOptionPane.showMessageDialog(null, 
										"Lệnh["+txtLenhNapRut.getText()+"], "
												+ "nhập không hợp lệ. "
												+ "Rút tiền thất bại!");
							}
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, 
									"Số TK["+txtSoTK.getText()+"], "
											+ "Ngày nạp/rút["+txtNgayNapRut.getText()+"], "
											+ "Lệnh["+txtLenhNapRut.getText()+"], "
											+ "Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
											+ "nhập không hợp lệ. "
											+ "Rút tiền thất bại!");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}
					else//else 3: nếu số tiền nạp/rút nhâp sai các điều kiện dưới đây thì thông báo nạp tiền thất bại
					{
						JOptionPane.showMessageDialog(null, 
								"Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
										+ "nhập sai, nạp tiền thất bại. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
										+ "- Không được để trống, số tiền nạp/rút có thể là số thực;\n"
										+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
										+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
										+ "- Không tự nhập tay mục số tiền nạp/rút này.");
					}				

				}	
				else//else 2: nếu ngày nạp/rút nhâp sai định dạng dd/MM/yyyy thì thông báo nạp tiền thất bại
				{
					JOptionPane.showMessageDialog(null, 
							"Ngày nạp/rút["+txtNgayNapRut.getText()+"] sai định dạng dd/MM/yyyy. "
									+ "Rút tiền thất bại!");
				}		
			}
			else//else 1: nếu số tk(chưa tồn tại) thì thông báo nạp tiền thất bại
			{
				JOptionPane.showMessageDialog(null, 
						"Số TK["+txtSoTK.getText()+"] chưa tồn tại "
								+ "hoặc có chứa ký tự chữ "
								+ "hoặc chưa đủ/vượt quá 12 ký tự. "
								+ "Rút tiền thất bại!");
			}				
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"], "
							+ "Ngày nạp/rút["+txtNgayNapRut.getText()+"], "
							+ "Lệnh["+txtLenhNapRut.getText()+"], "
							+ "Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày nạp/rút["+txtNgayNapRut.getText()+"] phải đúng định dạng (dd/MM/yyyy);\n"
							+ "- Lệnh["+txtLenhNapRut.getText()+"] không được vượt quá 3 ký tự chữ;\n"
							+ "- Không được để trống, số tiền nạp/rút có thể là số thực;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay mục số tiền nạp/rút này.");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

	public boolean kiemTraSoTkTrongBangTKNRTonTai(String sotk)
	{
		try
		{
			String sql="select * from TaiKhoanNapRut where SoTK=?";
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

	public boolean kiemTraChuoiChuaSo(String chuoikt)
	{	
		if(chuoikt.matches("[0-9]+"))
			return true;//chuỗi toàn số
		else
			return false;//chuỗi không chứa số		
	}

	public boolean kiemTraNgayThangNam(String ngayThangNam)
	{
		if (ngayThangNam.matches("([0-9]{2})/([0-9]{2})/([1-9]{4})"))
			return true;
		else
			return false;
	}

	protected void xuLyNap() {
		try {
			//if 1: nếu số tk(đã tồn tại) thì cho kiểm tra tiếp ngày nạp/rút
			if(kiemTraSoTkTrongBangTKNRTonTai(txtSoTK.getText())
					&& kiemTraChuoiChuaSo(txtSoTK.getText())
					&& (txtSoTK.getText().toString().length()==12))
			{		
				//if 2: kiểm tra định dạng dd/MM/yyyy của txtNgayNapRut
				if(!kiemTraNgayThangNam(txtNgayNapRut.getText()))
				{	
					//if 3: kiểm tra điều kiện cho số dư tiền nạp/rút: 
					// - không chứa số âm, không quá 2 tỷ tỷ, 
					// - không chứa ký tự đặc biệt, 
					// - phải tự nhập tay mục này
					if((Float.parseFloat(txtSoTienNapRut.getText())>0 
							&& Float.parseFloat(txtSoTienNapRut.getText())<2000000000)
							&&(kiemTraChuoiChuaSo(txtSoTienNapRut.getText())))
					{
						try {
							//if 4: kiểm tra LenhNapRut đúng định dạng hay không
							if( (!kiemTraChuoiChuaSo(txtLenhNapRut.getText())) 
									&& (txtLenhNapRut.getText().toString().length()==3)
									&& (txtLenhNapRut.getText().equals("Nap")) )
							{
								//cập nhật 1 record mới cho cột SoDuTienMatHT 
								//ở bảng TaiKhoanHienTai -> này để lệnh nạp tiền
								capNhatSoDuTienMatHTSauKhiNap();
							}
							else//else 4: txtLenhNapRut không đúng định dạng thì thông báo nạp tiền thất bại
							{
								JOptionPane.showMessageDialog(null, 
										"Lệnh["+txtLenhNapRut.getText()+"], "
												+ "nhập không hợp lệ. "
												+ "Nạp tiền thất bại!");
							}
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, 
									"Số TK["+txtSoTK.getText()+"], "
											+ "Ngày nạp/rút["+txtNgayNapRut.getText()+"], "
											+ "Lệnh["+txtLenhNapRut.getText()+"], "
											+ "Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
											+ "nhập không hợp lệ. "
											+ "Nạp tiền thất bại!");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}
					else//else 3: nếu số tiền nạp/rút nhâp sai các điều kiện dưới đây thì thông báo nạp tiền thất bại
					{
						JOptionPane.showMessageDialog(null, 
								"Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
										+ "nhập sai, nạp tiền thất bại. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
										+ "- Không được để trống, số tiền nạp/rút có thể là số thực;\n"
										+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
										+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
										+ "- Không tự nhập tay mục số tiền nạp/rút này.");
					}				

				}	
				else//else 2: nếu ngày nạp/rút nhâp sai định dạng dd/MM/yyyy thì thông báo nạp tiền thất bại
				{
					JOptionPane.showMessageDialog(null, 
							"Ngày nạp/rút["+txtNgayNapRut.getText()+"] sai định dạng dd/MM/yyyy. "
									+ "Nạp tiền thất bại!");
				}		
			}
			else//else 1: nếu số tk(chưa tồn tại) thì thông báo nạp tiền thất bại
			{
				JOptionPane.showMessageDialog(null, 
						"Số TK["+txtSoTK.getText()+"] chưa tồn tại "
								+ "hoặc có chứa ký tự chữ "
								+ "hoặc chưa đủ/vượt quá 12 ký tự. "
								+ "Nạp tiền thất bại!");
			}				
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtSoTK.getText()+"], "
							+ "Ngày nạp/rút["+txtNgayNapRut.getText()+"], "
							+ "Lệnh["+txtLenhNapRut.getText()+"], "
							+ "Số tiền nạp/rút["+txtSoTienNapRut.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày nạp/rút phải đúng định dạng (dd/MM/yyyy);\n"
							+ "- Lệnh không được vượt quá 3 ký tự chữ;\n"
							+ "- Không được để trống, số tiền nạp/rút có thể là số thực;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay mục số tiền nạp/rút này.");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

	private void addControls() {
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnLenhNapRut=new JPanel();
		pnLenhNapRut.setLayout(new BorderLayout());
		con.add(pnLenhNapRut,BorderLayout.CENTER);

		JPanel pnThongTinNapRut=new JPanel();
		pnThongTinNapRut.setLayout(new BoxLayout(
				pnThongTinNapRut,BoxLayout.Y_AXIS));	
		pnLenhNapRut.add(pnThongTinNapRut,BorderLayout.CENTER);

		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Thông tin chi tiết lệnh nạp rút tiền");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		pnThongTinNapRut.setBorder(borderTieuDe);

		JPanel pnLenhNapRutLine1=new JPanel();
		pnLenhNapRutLine1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoTK=new JLabel("Số TK:");
		JLabel lblNgayNapRut=new JLabel("Ngày nạp/rút:");	
		JLabel lblLenhNapRut=new JLabel("Lệnh:");
		txtSoTK=new JTextField(12);
		txtNgayNapRut=new JTextField(12);
		txtLenhNapRut=new JTextField(12);
		pnLenhNapRutLine1.add(lblSoTK);
		pnLenhNapRutLine1.add(txtSoTK);
		pnLenhNapRutLine1.add(lblNgayNapRut);
		pnLenhNapRutLine1.add(txtNgayNapRut);
		pnLenhNapRutLine1.add(lblLenhNapRut);
		pnLenhNapRutLine1.add(txtLenhNapRut);
		pnThongTinNapRut.add(pnLenhNapRutLine1);		

		JPanel pnLenhNapRutLine2=new JPanel();
		pnLenhNapRutLine2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoTienNapRut=new JLabel("Số tiền nạp/rút:");
		txtSoTienNapRut=new JTextField(12);
		JLabel lblSoDuTienMat=new JLabel("Số dư tiền mặt:");
		txtSoDuTienMat=new JTextField(12);
		JLabel lblTongTaiSan=new JLabel("Tổng tài sản:");
		txtTongTaiSan=new JTextField(12);
		pnLenhNapRutLine2.add(lblSoTienNapRut);
		pnLenhNapRutLine2.add(txtSoTienNapRut);
		pnLenhNapRutLine2.add(lblSoDuTienMat);
		pnLenhNapRutLine2.add(txtSoDuTienMat);
		pnLenhNapRutLine2.add(lblTongTaiSan);
		pnLenhNapRutLine2.add(txtTongTaiSan);
		pnThongTinNapRut.add(pnLenhNapRutLine2);

		lblSoTK.setPreferredSize(lblSoTienNapRut.getPreferredSize());
		lblNgayNapRut.setPreferredSize(lblSoDuTienMat.getPreferredSize());
		lblLenhNapRut.setPreferredSize(lblTongTaiSan.getPreferredSize());

		JPanel pnBtnLenhNapRut=new JPanel();
		pnBtnLenhNapRut.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnTatCaTK=new JButton("Tất cả TK");
		pnBtnLenhNapRut.add(btnTatCaTK);
		btnTimTK=new JButton("Tìm TK");
		pnBtnLenhNapRut.add(btnTimTK);
		btnNap=new JButton("Nạp");
		pnBtnLenhNapRut.add(btnNap);
		btnRut=new JButton("Rút");
		pnBtnLenhNapRut.add(btnRut);
		btnThoat=new JButton("Thoát");
		pnBtnLenhNapRut.add(btnThoat);		
		pnThongTinNapRut.add(pnBtnLenhNapRut);

		dtmTaiKhoan=new DefaultTableModel();
		dtmTaiKhoan.addColumn("Stt");
		dtmTaiKhoan.addColumn("Số TK");
		dtmTaiKhoan.addColumn("Ngày nạp/rút");
		dtmTaiKhoan.addColumn("Lệnh");
		dtmTaiKhoan.addColumn("Số tiền nạp/rút");
		tblTaiKhoan=new JTable(dtmTaiKhoan);
		//tblTaiKhoanHienTai.setPreferredSize(new Dimension(0,350));
		JScrollPane scTable=new JScrollPane(
				tblTaiKhoan,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnThongTinNapRut.add(scTable);

		JPanel pnSoDuTienMatHTVaTongTaiSanHT=new JPanel();
		pnSoDuTienMatHTVaTongTaiSanHT.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTongTienNapRut=new JLabel("Tổng tiền nạp/rút:");
		txtTongTienNapRut=new JTextField(12);
		pnSoDuTienMatHTVaTongTaiSanHT.add(lblTongTienNapRut);
		pnSoDuTienMatHTVaTongTaiSanHT.add(txtTongTienNapRut);
		pnThongTinNapRut.add(pnSoDuTienMatHTVaTongTaiSanHT);


	}
	public void showWindow()
	{
		this.setSize(700,450);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
