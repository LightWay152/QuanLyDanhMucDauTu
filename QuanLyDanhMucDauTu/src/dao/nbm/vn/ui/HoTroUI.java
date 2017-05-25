package dao.nbm.vn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class HoTroUI extends JDialog {
	
	JLabel lblHoTro;
	JButton btnDong;
	
	public HoTroUI(String title)
	{
		this.setTitle(title);
		addControls();
		addEvents();
	}

	private void addEvents() {
		// TODO Auto-generated method stub
		btnDong.addActionListener(new ActionListener() {
			
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
		
		JPanel pnHoTro=new JPanel();
		con.add(pnHoTro,BorderLayout.WEST);
		
		TitledBorder borderTieuDe=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Hỗ trợ");
		borderTieuDe.setTitleColor(Color.BLUE);
		borderTieuDe.setTitleJustification(TitledBorder.CENTER);
		pnHoTro.setBorder(borderTieuDe);
		
		JPanel pnChiTietHoTro=new JPanel();
		pnChiTietHoTro.setLayout(new BoxLayout(
				pnChiTietHoTro,
				BoxLayout.Y_AXIS));
		
		JPanel pnLapTrinhVien=new JPanel();
		pnLapTrinhVien.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblHinhLTV=new JLabel();
		ImageIcon imgHinhLTV=new ImageIcon("images/user1.png");
		lblHinhLTV.setIcon(imgHinhLTV);
		lblHinhLTV.setPreferredSize(new Dimension(32, 32));	
		JLabel txtLapTrinhVien=new JLabel("Lập trình viên:");
		pnLapTrinhVien.add(lblHinhLTV);
		pnLapTrinhVien.add(txtLapTrinhVien);	
		
		JPanel pnLapTrinhVien1=new JPanel();
		pnLapTrinhVien1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel txtLapTrinhVien1=new JLabel("1/ Nguyễn Bá Minh Đạo");
		pnLapTrinhVien1.add(txtLapTrinhVien1);
		
		JPanel pnLapTrinhVien2=new JPanel();
		pnLapTrinhVien2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel txtLapTrinhVien2=new JLabel("2/ Nguyễn Hải Triều");
		pnLapTrinhVien2.add(txtLapTrinhVien2);

		JPanel pnEmail=new JPanel();
		pnEmail.setLayout(new FlowLayout(FlowLayout.LEFT));	
		JLabel lblHinhEmail=new JLabel();
		ImageIcon imgHinhEmail=new ImageIcon("images/gmail.png");
		lblHinhEmail.setIcon(imgHinhEmail);
		lblHinhEmail.setPreferredSize(new Dimension(32, 32));	
		JLabel txtEmail=new JLabel("Email: dacna213@gmail.com");
		pnEmail.add(lblHinhEmail);
		pnEmail.add(txtEmail);
		
		JPanel pnHotLine=new JPanel();
		pnHotLine.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblHinhHotline=new JLabel();
		ImageIcon imgHinhHotline=new ImageIcon("images/hotline.png");
		lblHinhHotline.setIcon(imgHinhHotline);
		lblHinhHotline.setPreferredSize(new Dimension(32, 32));
		JLabel txtHotLine=new JLabel("HotLine: 0909123456");
		pnHotLine.add(lblHinhHotline);
		pnHotLine.add(txtHotLine);
		
		JPanel pnDong=new JPanel();
		pnDong.setLayout(new FlowLayout(FlowLayout.CENTER));;
		btnDong =new JButton("Đóng");
		pnDong.add(btnDong);
		
		pnChiTietHoTro.add(pnLapTrinhVien);
		pnChiTietHoTro.add(pnLapTrinhVien1);
		pnChiTietHoTro.add(pnLapTrinhVien2);
		pnChiTietHoTro.add(pnEmail);
		pnChiTietHoTro.add(pnHotLine);
		pnChiTietHoTro.add(pnDong);
		
		pnHoTro.add(pnChiTietHoTro);
	
	}
	public void showWindow()
	{
		this.setSize(237,270);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
