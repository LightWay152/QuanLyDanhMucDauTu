package dao.nbm.vn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;


public class GioiThieuVaHuongDanUI extends JFrame {
	
	JLabel lblGioiThieuVaHuongDan;
	JButton btnThoat;
	
	public GioiThieuVaHuongDanUI(String title)
	{
		super(title);
		addControls();
		addEvents();
	}

	private void addEvents() {
		// TODO Auto-generated method stub
		btnThoat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
	}

	private void addControls() {
		// TODO Auto-generated method stub
		Container con=getContentPane();
		con.setLayout(new BorderLayout());
		
		JPanel pnGioiThieuVaHuongDan=new JPanel();
		con.add(pnGioiThieuVaHuongDan,BorderLayout.CENTER);
		
		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Giới thiệu & Hướng dẫn");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		pnGioiThieuVaHuongDan.setBorder(borderTieuDe);
		
		JPanel pnChiTiet=new JPanel();
		pnChiTiet.setLayout(new BoxLayout(pnChiTiet,BoxLayout.Y_AXIS));
		
		JLabel txtGioiThieuVaHuongDan1=new JLabel("Chào mừng các bạn đến với chương trình Quản lý danh mục đầu tư.");
		JLabel txtGioiThieuVaHuongDan2=new JLabel("Đây là 1 chương trình cho phép người dùng quản lý số lượng lớn");
		JLabel txtGioiThieuVaHuongDan3=new JLabel("các danh mục tài khoản với nhiều giao dịch chứng khoán khác nhau.");
		JLabel txtGioiThieuVaHuongDan21=new JLabel("   ");
		JLabel txtGioiThieuVaHuongDan4=new JLabel("1/ Chức năng của chương trình:");
		JLabel txtGioiThieuVaHuongDan5=new JLabel("- Giới thiệu & Hướng dẫn");
		JLabel txtGioiThieuVaHuongDan6=new JLabel("- Quản lý tài khoản");
		JLabel txtGioiThieuVaHuongDan7=new JLabel("- Thống kê Return tháng & năm");
		JLabel txtGioiThieuVaHuongDan8=new JLabel("- Báo cáo tài sản");
		JLabel txtGioiThieuVaHuongDan9=new JLabel("- Lệnh mua/bán chứng khoán");
		JLabel txtGioiThieuVaHuongDan10=new JLabel("- Lệnh nạp/rút tiền");
		JLabel txtGioiThieuVaHuongDan11=new JLabel("- Hỗ trợ");
		JLabel txtGioiThieuVaHuongDan22=new JLabel("   ");
		JLabel txtGioiThieuVaHuongDan12=new JLabel("2/ Hướng dẫn:");
		JLabel txtGioiThieuVaHuongDan13=new JLabel("- Giới thiệu & Hướng dẫn: Giới thiệu & Hướng dẫn sử dụng");
		JLabel txtGioiThieuVaHuongDan14=new JLabel("- Quản lý tài khoản: Tạo/Cập nhật/Xóa tài khoản & chứng khoán");
		JLabel txtGioiThieuVaHuongDan15=new JLabel("- Thống kê Return tháng & năm: xem Return tháng & năm của tài khoản");
		JLabel txtGioiThieuVaHuongDan16=new JLabel("- Báo cáo tài sản: xem Số dư/Tổng tiền hiện tại và Cập nhật giá/chỉ số chứng khoán");
		JLabel txtGioiThieuVaHuongDan17=new JLabel("- Lệnh mua/bán chứng khoán: Mua/Bán chứng khoán cho tài khoản");
		JLabel txtGioiThieuVaHuongDan18=new JLabel("- Lệnh nạp/rút tiền: Nạp/Rút tiền cho tài khoản");
		JLabel txtGioiThieuVaHuongDan19=new JLabel("- Hỗ trợ: liên lạc với tư vấn viên nếu gặp trục trặc khi sử dụng");
		JLabel txtGioiThieuVaHuongDan23=new JLabel("   ");
		JLabel txtGioiThieuVaHuongDan20=new JLabel("Mong chương trình giúp ích được cho quý khách!");
		JLabel txtGioiThieuVaHuongDan24=new JLabel("   ");
		
		pnChiTiet.add(txtGioiThieuVaHuongDan1);
		pnChiTiet.add(txtGioiThieuVaHuongDan2);
		pnChiTiet.add(txtGioiThieuVaHuongDan3);
		pnChiTiet.add(txtGioiThieuVaHuongDan21);
		pnChiTiet.add(txtGioiThieuVaHuongDan4);
		pnChiTiet.add(txtGioiThieuVaHuongDan5);
		pnChiTiet.add(txtGioiThieuVaHuongDan6);
		pnChiTiet.add(txtGioiThieuVaHuongDan7);
		pnChiTiet.add(txtGioiThieuVaHuongDan8);
		pnChiTiet.add(txtGioiThieuVaHuongDan9);
		pnChiTiet.add(txtGioiThieuVaHuongDan10);
		pnChiTiet.add(txtGioiThieuVaHuongDan11);
		pnChiTiet.add(txtGioiThieuVaHuongDan22);
		pnChiTiet.add(txtGioiThieuVaHuongDan12);
		pnChiTiet.add(txtGioiThieuVaHuongDan13);
		pnChiTiet.add(txtGioiThieuVaHuongDan14);
		pnChiTiet.add(txtGioiThieuVaHuongDan15);
		pnChiTiet.add(txtGioiThieuVaHuongDan16);
		pnChiTiet.add(txtGioiThieuVaHuongDan17);
		pnChiTiet.add(txtGioiThieuVaHuongDan18);
		pnChiTiet.add(txtGioiThieuVaHuongDan19);
		pnChiTiet.add(txtGioiThieuVaHuongDan23);
		pnChiTiet.add(txtGioiThieuVaHuongDan20);
		pnChiTiet.add(txtGioiThieuVaHuongDan24);
		

		btnThoat=new JButton("Thoát");
		pnChiTiet.add(btnThoat);
		
		pnGioiThieuVaHuongDan.add(pnChiTiet);
		
		
	}
	public void showWindow()
	{
		this.setSize(500,470);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
