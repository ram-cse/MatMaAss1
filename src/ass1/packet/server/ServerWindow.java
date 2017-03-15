package ass1.packet.server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ServerWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtPort;
	private JTextArea txtInfor;
	
	private Server server;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow frame = new ServerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerWindow() {
		setTitle("SERVER");
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPort = new JLabel("PORT");
		lblPort.setBounds(10, 11, 46, 14);
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setText("4444");
		txtPort.setBounds(53, 8, 102, 20);
		txtPort.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JButton btnStart = new JButton("START SERVER");
		JButton btnStop = new JButton("STOP SERVER");
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int port = Integer.parseInt(txtPort.getText());
				server = new Server(port, ServerWindow.this);
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		btnStart.setBounds(177, 6, 110, 23);
		contentPane.add(btnStart);
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int click = JOptionPane.showConfirmDialog(null, "Are you sure ?", "STOP SERVER ", JOptionPane.YES_NO_OPTION);
				if (click == JOptionPane.YES_OPTION) {
					server.quit();
					btnStop.setEnabled(false);
					btnStart.setEnabled(true);
				}
			}
		});
		btnStop.setEnabled(false);
		btnStop.setBounds(313, 7, 109, 23);
		contentPane.add(btnStop);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 424, 244);
		contentPane.add(scrollPane);
		
		txtInfor = new JTextArea();
		txtInfor.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtInfor.setEditable(false);
		scrollPane.setViewportView(txtInfor);
	}
	
	public void updateInfor(String string) {
		txtInfor.append(string + "\n");
	}
}
