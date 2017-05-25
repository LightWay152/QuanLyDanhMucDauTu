package dao.nbm.vn.ui;

import java.awt.BorderLayout;
import java.awt.Button;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.nbm.vn.model.ChungKhoan;
import dao.nbm.vn.model.TaiKhoan;

public class BaoCaoTaiSanUI extends JFrame {
	
	JList<TaiKhoan>listTaiKhoan;
	ArrayList<ChungKhoan>dsCK;

	JTextField txtSoTK,txtNgay,
	txtSoDuTienMat,txtTongTaiSan,txtTongGiaTriCK;

	DefaultTableModel dtmChungKhoan;
	JTable tblChungKhoan;

	JButton btnCapNhatGiaChiSoCK,btnThoat;

	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";

	public BaoCaoTaiSanUI(String title)
	{
		super(title);
		addControls();
		addEvents();

		ketNoiCoSoDuLieu();
		hienThiDanhSachSoTK();

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

	private void dongNhapLieu() {
		// TODO Auto-generated method stub
		txtSoTK.setEditable(false);
		txtNgay.setEditable(false);
		txtSoDuTienMat.setEditable(false);
		txtTongTaiSan.setEditable(false);
		txtTongGiaTriCK.setEditable(false);
		tblChungKhoan.setEnabled(false);
	}
	
	private void moNhapLieu() {
		// TODO Auto-generated method stub
		txtSoTK.setEditable(true);
		txtNgay.setEditable(true);
		txtSoDuTienMat.setEditable(true);
		txtTongTaiSan.setEditable(true);
		txtTongGiaTriCK.setEditable(true);
	}

	//Hàm này dùng khi bắt sự kiện click item của list trong hàm addEvents()
	private ArrayList<ChungKhoan> hienThiChiTietCkTheoSoTk(String soTK,int loai) {
		// TODO Auto-generated method stub
		ArrayList<ChungKhoan>dsCK=new ArrayList<ChungKhoan>();
		try {
			String sqlCK="select * from ChungKhoan where SoTK=? and LoaiCKHayCS=?";
			preStatement=conn.prepareStatement(sqlCK);
			preStatement.setString(1, soTK);
			preStatement.setInt(2,0);//LoaiCKHayCS=0 -> ck
			//preStatement.setString(3, soTK);
			
			//preStatement.setString(3,"Mua");//LoaiCKHayCS=0 -> ck

			result=preStatement.executeQuery();

			while(result.next())
			{
				ChungKhoan ckht=new ChungKhoan();
				ckht.setSoTK(result.getString(1));	
				ckht.setSttCK(result.getInt(2));		
				ckht.setNgay(result.getString(3));
				
				txtNgay.setText(result.getString(3));//ngày mới nhất
				
				ckht.setLoaiNgay(result.getInt(4));
				ckht.setMaVaChiSoCK(result.getString(5));
				ckht.setLoaiCKHayCS(result.getInt(6));
				ckht.setGia(result.getFloat(7));
				ckht.setSoLuong(result.getInt(8));				
				ckht.setThanhTien(result.getFloat(9));			
				ckht.setLenh(result.getString(10));
				dsCK.add(ckht);		
			}
			
			String sqlNgay="select TOP 1 Ngay from ChungKhoan where SoTK=? order by Ngay desc";
			preStatement=conn.prepareStatement(sqlNgay);
			preStatement.setString(1, soTK);

			result=preStatement.executeQuery();

			while(result.next())
			{
				/*ChungKhoan ckht=new ChungKhoan();
				ckht.setSoTK(result.getString(1));	
				ckht.setSttCK(result.getInt(2));		
				ckht.setNgay(result.getString(3));*/
				
				txtNgay.setText(result.getString(1));//ngày mới nhất
				
				/*ckht.setLoaiNgay(result.getInt(4));
				ckht.setMaVaChiSoCK(result.getString(5));
				ckht.setLoaiCKHayCS(result.getInt(6));
				ckht.setGia(result.getFloat(7));
				ckht.setSoLuong(result.getInt(8));				
				ckht.setThanhTien(result.getFloat(9));			
				ckht.setLenh(result.getString(10));
				dsCK.add(ckht);		*/
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dsCK;
	}

	//Hàm này dùng khi bắt sự kiện click item của list trong hàm addEvents()
	public ArrayList<TaiKhoan> hienThiChiTietTkTheoSoTk(String soTK) {
		// TODO Auto-generated method stub
		ArrayList<TaiKhoan>dsTK=new ArrayList<TaiKhoan>();
		try {
			String sqlChiTietTK="select * from TaiKhoan where SoTK=?";
			preStatement=conn.prepareStatement(sqlChiTietTK);
			preStatement.setString(1, soTK);

			result=preStatement.executeQuery();
			while(result.next())
			{
				TaiKhoan tkht=new TaiKhoan();
				tkht.setSoTK(result.getString(1));
				tkht.setTenTK(result.getString(2));	
				tkht.setNamSinh(result.getString(3));	
				tkht.setSoDT(result.getString(4));
				tkht.setSoCMND(result.getString(5));
				tkht.setEmail(result.getString(6));
				tkht.setDiaChi(result.getString(7));
				tkht.setSoDuTienMatBD(result.getInt(8));
				tkht.setTongTaiSanBD(result.getFloat(9));

				dsTK.add(tkht);		
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dsTK;
	}
		
	private void addEvents() {
		// TODO Auto-generated method stub
		btnCapNhatGiaChiSoCK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyCapNhatGiaChiSoCK();
			}
		});

		btnThoat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int ret=JOptionPane.showConfirmDialog(null, 
						"Bạn có chắc chắn muốn thoát Báo cáo tài sản?", 
						"Thông báo thoát", 
						JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.NO_OPTION)return;
				dispose();
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
				// TODO Auto-generated method stub
				if(listTaiKhoan.getSelectedValue()==null)return;

				dsCK=hienThiChiTietCkTheoSoTk(
						listTaiKhoan.getSelectedValue().getSoTK(),0);//LoaiCKHayCS=0->ck
				dtmChungKhoan.setRowCount(0);
				
				int stt=1;//làm số thứ tự 1,2,3,..
				float tongThanhTien=0;//tính tổng thành tiền ck
				for(ChungKhoan ckht:dsCK)
				{
					Vector<Object> vecCK=new Vector<Object>();

					vecCK.add(stt);//stt tự tăng của bảng
					stt++;
					
					//vecCK.add(ckht.getNgayMuaBanCK());
					//txtNgayMuaBanCK.setText(ckht.getNgayMuaBanCK());
					vecCK.add(ckht.getMaVaChiSoCK());
					//vecCKHT.add(ckht.getLoaiCKHayCS());
					vecCK.add(ckht.getGia());
					vecCK.add(ckht.getSoLuong());

					float gia=ckht.getGia();
					float soLuong=ckht.getSoLuong();
					float thanhTien=gia*soLuong;
					vecCK.add(thanhTien);//thành tiền

					tongThanhTien+=thanhTien;//cộng dồn để tính tổng giá trị CK

					dtmChungKhoan.addRow(vecCK);		
				}

				txtTongGiaTriCK.setText(String.valueOf(tongThanhTien));

				ArrayList<TaiKhoan>dsTK=
						hienThiChiTietTkTheoSoTk(
								listTaiKhoan.getSelectedValue().getSoTK());
				String selectedValueSoTK=listTaiKhoan.getSelectedValue().getSoTK();

				for(TaiKhoan tkht:dsTK)
				{		
					txtSoTK.setText(tkht.getSoTK());
					txtSoDuTienMat.setText(String.valueOf(tkht.getSoDuTienMatBD()));

					float soDuTienMat=tkht.getSoDuTienMatBD();
					float tongGiaTriCK=Float.parseFloat(txtTongGiaTriCK.getText());
					float tongTaiSan=soDuTienMat+tongGiaTriCK;

					//tổng tài sản sau khi tính toán
					txtTongTaiSan.setText(String.valueOf(tongTaiSan));

				}
			
			}
		});

	
	}

	public boolean kiemTraSoTkTrongBangCKTonTai(String sotk)
	{
		try
		{
			String sql="select * from ChungKhoanHienTai where SoTK=?";
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

	protected void xuLyCapNhatGiaChiSoCK() {
		// TODO Auto-generated method stub
		CapNhatGiaChiSoCKUI capNhatUI=new CapNhatGiaChiSoCKUI("Cập nhật Giá/Chỉ số chứng khoán");
		capNhatUI.showWindow();
	}

	private void addControls() {
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnLeft=new JPanel();
		pnLeft.setPreferredSize(new Dimension(200, 0));
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

		TitledBorder borderListTK=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Danh sách tài khoản");
		borderListTK.setTitleColor(Color.BLUE);
		borderListTK.setTitleJustification(TitledBorder.CENTER);
		listTaiKhoan.setBorder(borderListTK);

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
		JLabel lblSoTK=new JLabel("Số TK:");
		txtSoTK=new JTextField(12);
		txtSoTK.setEditable(false);
		JLabel lblNgay=new JLabel("Ngày:");
		txtNgay=new JTextField(12);
		txtNgay.setEditable(false);
		pnTopOfRightLine1.add(lblSoTK);
		pnTopOfRightLine1.add(txtSoTK);
		pnTopOfRightLine1.add(lblNgay);
		pnTopOfRightLine1.add(txtNgay);
		pnTopOfRight.add(pnTopOfRightLine1);

		JPanel pnTopOfRightLine4=new JPanel();
		pnTopOfRightLine4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblSoDuTienMat=new JLabel("Số dư tiền mặt:");
		txtSoDuTienMat=new JTextField(12);
		txtSoDuTienMat.setEditable(false);
		JLabel lblTongTaiSan=new JLabel("Tổng tài sản:");
		txtTongTaiSan=new JTextField(12);
		txtTongTaiSan.setEditable(false);
		pnTopOfRightLine4.add(lblSoDuTienMat);
		pnTopOfRightLine4.add(txtSoDuTienMat);
		pnTopOfRightLine4.add(lblTongTaiSan);
		pnTopOfRightLine4.add(txtTongTaiSan);	
		pnTopOfRight.add(pnTopOfRightLine4);

		JPanel pnTopOfRightLine5=new JPanel();
		pnTopOfRightLine5.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnCapNhatGiaChiSoCK=new JButton("Cập nhật Giá/Chỉ số chứng khoán");
		btnThoat=new JButton("Thoát");
		pnTopOfRightLine5.add(btnCapNhatGiaChiSoCK);
		pnTopOfRightLine5.add(btnThoat);
		pnTopOfRight.add(pnTopOfRightLine5);	

		lblSoTK.setPreferredSize(lblSoDuTienMat.getPreferredSize());
		lblNgay.setPreferredSize(lblTongTaiSan.getPreferredSize());
		
		JPanel pnBottomOfRight=new JPanel();
		pnBottomOfRight.setLayout(new BorderLayout());
		pnRight.add(pnBottomOfRight,BorderLayout.CENTER);
		pnBottomOfRight.setPreferredSize(new Dimension(0, 300));

		dtmChungKhoan=new DefaultTableModel();
		dtmChungKhoan.addColumn("Stt");
		dtmChungKhoan.addColumn("Mã CK");
		dtmChungKhoan.addColumn("Giá");
		dtmChungKhoan.addColumn("Số lượng");
		dtmChungKhoan.addColumn("Thành tiền");
		tblChungKhoan=new JTable(dtmChungKhoan);
		tblChungKhoan.setPreferredSize(new Dimension(0, 350));
		JScrollPane scTable=new JScrollPane(
				tblChungKhoan,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnBottomOfRight.add(scTable,BorderLayout.CENTER);

		JPanel pnTongGiaTriCK=new JPanel();
		pnTongGiaTriCK.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTongGiaTriCK=new JLabel("Tổng giá trị chứng khoán:");
		txtTongGiaTriCK=new JTextField(12);
		pnTongGiaTriCK.add(lblTongGiaTriCK);
		pnTongGiaTriCK.add(txtTongGiaTriCK);
		pnBottomOfRight.add(pnTongGiaTriCK,BorderLayout.SOUTH);

	}
	
	public void showWindow()
	{
		this.setSize(750,460);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
