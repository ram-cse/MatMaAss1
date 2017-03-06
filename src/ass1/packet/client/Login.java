package ass1.packet.client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtAddress;
	private JTextField txtPort;

	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		txtUsername.setBounds(63, 61, 167, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblUsername.setBounds(112, 24, 69, 26);
		contentPane.add(lblUsername);
		
		txtAddress = new JTextField();
		txtAddress.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		txtAddress.setColumns(10);
		txtAddress.setBounds(63, 143, 167, 20);
		txtAddress.setText("localhost");
		contentPane.add(txtAddress);
		
		txtPort = new JTextField();
		txtPort.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		txtPort.setColumns(10);
		txtPort.setBounds(63, 223, 167, 20);
		txtPort.setText("4444");
		contentPane.add(txtPort);
		
		JLabel lblIpAdrress = new JLabel("IP Address :");
		lblIpAdrress.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblIpAdrress.setBounds(106, 106, 81, 26);
		contentPane.add(lblIpAdrress);
		
		JLabel lblPort = new JLabel("Port :");
		lblPort.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPort.setBounds(123, 187, 48, 26);
		contentPane.add(lblPort);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
	
		});
		btnLogin.setBounds(102, 300, 89, 23);
		contentPane.add(btnLogin);
	}
	
// Nhay den cua so chinh client
	private void login() {
		if(txtUsername.getText().equals("") || txtAddress.getText().equals("") || txtPort.getText().equals("")) return;
		
		String name = txtUsername.getText();
		String address = txtAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		
		dispose();
		new ClientWindow(name, address, port);
	}
	

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
