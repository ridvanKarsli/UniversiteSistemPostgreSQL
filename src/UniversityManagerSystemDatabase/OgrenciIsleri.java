package UniversityManagerSystemDatabase;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Model.Cinsiyet;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OgrenciIsleri {
	
	private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5433/UniversityManagerSystem_db?user=postgres&password=3519";
	private Connection conn;

	private JFrame frame;
	private JTextField txtAd;
	private JTextField txtSoyad;
	private JTextField txtOkulNo;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTable table;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OgrenciIsleri window = new OgrenciIsleri();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OgrenciIsleri() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Ad:");
		lblNewLabel.setBounds(52, 34, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Soyad:");
		lblNewLabel_1.setBounds(52, 62, 61, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Okul no:");
		lblNewLabel_1_1.setBounds(52, 90, 61, 16);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		txtAd = new JTextField();
		txtAd.setBounds(134, 29, 130, 26);
		frame.getContentPane().add(txtAd);
		txtAd.setColumns(10);
		
		txtSoyad = new JTextField();
		txtSoyad.setColumns(10);
		txtSoyad.setBounds(134, 57, 130, 26);
		frame.getContentPane().add(txtSoyad);
		
		txtOkulNo = new JTextField();
		txtOkulNo.setColumns(10);
		txtOkulNo.setBounds(134, 85, 130, 26);
		frame.getContentPane().add(txtOkulNo);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Cinsiyet");
		lblNewLabel_1_1_1.setBounds(52, 118, 61, 16);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		JRadioButton rdbErkek = new JRadioButton("Erkek");
		rdbErkek.setSelected(true);
		buttonGroup.add(rdbErkek); 
		rdbErkek.setBounds(134, 114, 80, 23);
		frame.getContentPane().add(rdbErkek);
		
		JRadioButton rdbKiz = new JRadioButton("Kız");
		buttonGroup.add(rdbKiz);
		rdbKiz.setBounds(226, 114, 80, 23);
		frame.getContentPane().add(rdbKiz);
		
		JButton btnKaydet = new JButton("Kaydet");
		btnKaydet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String ad = txtAd.getText();
				String soyad = txtSoyad.getText(); 
				String okulNo = txtOkulNo.getText();
				Cinsiyet cinsiyet = rdbKiz.isSelected() ? Cinsiyet.KIZ : Cinsiyet.ERKEK;
				
				
				try {
					getConnection();
					//Statement insert = conn.createStatement();
					//sql injection atağına sebep olacak yazım şekli
//					insert.executeUpdate("INSERT INTO ogrenciler(ad, soyad, okulNo, cinsiyet) " +
//		                     "VALUES ('" + ad + "', '" + soyad + "', " + okulNo + ", '" + cinsiyet + "')");
					PreparedStatement insert = conn.prepareStatement("INSERT INTO ogrenciler(ad, soyad, okulNo, cinsiyet) VALUES (?,?,?,?)");
					
					insert.setString(1, ad);
					insert.setString(2, soyad);
					insert.setInt(3, Integer.valueOf(okulNo));
					insert.setString(4, cinsiyet.toString());
					
					insert.executeUpdate();

					
					insert.close();
					conn.close();
					
					JOptionPane.showMessageDialog(btnKaydet, "Yeni kullanıcı veritabanına eklendi");
					
					tabloDoldur(); //yeni öprenci eklenince tablo güncellensin
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			
		});
		btnKaydet.setBounds(303, 29, 117, 29);
		frame.getContentPane().add(btnKaydet);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 146, 427, 120);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedIndex = table.getSelectedRow();
				
				int selectedId = Integer.valueOf(table.getModel().getValueAt(selectedIndex, 0).toString());
				String selectedAd = table.getValueAt(selectedIndex, 1).toString();
				String selectedSoyad = table.getValueAt(selectedIndex, 2).toString();
				String selectedOkulno = table.getValueAt(selectedIndex, 3).toString();
				String selectedCinsiyet = table.getValueAt(selectedIndex, 4).toString();
				
				txtAd.setText(selectedAd);
				txtSoyad.setText(selectedSoyad);
				txtOkulNo.setText(selectedOkulno);
				
				rdbKiz.setSelected(selectedCinsiyet.equals("KIZ"));
				rdbErkek.setSelected(!selectedCinsiyet.equals("KIZ"));
				
			}
		});
		scrollPane.setViewportView(table);
		
		JButton btnSil = new JButton("Sil");
		btnSil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedIndex = table.getSelectedRow();
				if (selectedIndex == -1) {
					JOptionPane.showMessageDialog(btnSil, "öncelikler silinecek kayıdı tablodan seçiniz");
					return;
				}
				int selectedId = Integer.valueOf(table.getModel().getValueAt(selectedIndex, 0).toString());//id al
				
				
				getConnection();
				
				try {
					PreparedStatement delete = conn.prepareStatement("DELETE FROM ogrenciler WHERE id=?");
					
					delete.setInt(1, selectedId);

					delete.executeUpdate();

					
					delete.close();
					conn.close();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tabloDoldur();
		
			}
		});
		btnSil.setBounds(303, 57, 117, 29);
		frame.getContentPane().add(btnSil);
		
		JButton btnGuncelle = new JButton("Güncelle");
		btnGuncelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedIndex = table.getSelectedRow();
				
				if(selectedIndex == -1) {
					JOptionPane.showMessageDialog(btnGuncelle, "öncelikle güncellencek kayıdı tablodan seçiniz");
					return;
				}
				
				String id = table.getModel().getValueAt(selectedIndex, 0).toString();
				String ad = txtAd.getText();
				String soyad = txtSoyad.getText();
				String okulno = txtOkulNo.getText();
				String cinsiyet = rdbKiz.isSelected() ? Cinsiyet.KIZ.toString() : Cinsiyet.ERKEK.toString();
				
				getConnection();
				
				try {
					PreparedStatement update = conn.prepareStatement("UPDATE ogrenciler SET ad=?, soyad=?, okulno=?, cinsiyet=? WHERE id=?;");
					
					update.setString(1, ad);
					update.setString(2, soyad);
					update.setInt(3, Integer.valueOf(okulno));
					update.setString(4, cinsiyet);
					update.setInt(5, Integer.valueOf(id));
					
					update.executeUpdate();
					
					update.close();
					conn.close();
					
					tabloDoldur();
					
					
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				
						
			}
		});
		btnGuncelle.setBounds(303, 85, 117, 29);
		frame.getContentPane().add(btnGuncelle);
		
		JButton btnTemizle = new JButton("Temizle");
		btnTemizle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component[] components = frame.getContentPane().getComponents();
				
				for(Component component : components) {
					if(component instanceof JTextField) {
						((JTextField)component).setText(""); 
					}
				}
			}
		});
		btnTemizle.setBounds(303, 113, 117, 29);
		frame.getContentPane().add(btnTemizle);
		
		tabloDoldur();
	}
	
	private void getConnection() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void tabloDoldur() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Id");
		model.addColumn("ad");
		model.addColumn("soyad");
		model.addColumn("okulNo");
		model.addColumn("cinsiyet");
		
		Object[] satir = new Object[model.getColumnCount()];
		
		int satirSayisi = 10;
		
		try {
			getConnection();
			Statement selectAll =  conn.createStatement();
			ResultSet rs = selectAll.executeQuery("SELECT * FROM ogrenciler");
			
			while(rs.next()) {
				satir[0] = rs.getInt(1);
				satir[1] = rs.getString(2);
				satir[2] = rs.getString(3);
				satir[3] = rs.getInt(4);
				satir[4] = rs.getString(5);
				
				model.addRow(satir);
			}
			
			selectAll.close();
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		table.setModel(model);
		
 	}
}





