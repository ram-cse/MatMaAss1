package cse.crypto.client;

import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import cse.crypto.helper.App;
import cse.crypto.helper.App.AlgType;

public class ClientWindow extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTable tableSearch;
	private JTable tableShare;
	private JTextField txtSearch;
	private JTextField txtFilePath;
	private JFileChooser fileChooserBrowse = new JFileChooser();
	private JFileChooser fileChooserSave = new JFileChooser();
	private JList listUser = new JList();
	
	private Client client;
	private Thread run, listen, miniServer;
	public boolean running = false;
	private ServerSocket serverSocket;
	
	String fileName, fileType, fileSize, fileLocation ;
	
	private int countOpen = 0;
	private JTable tableDownload;
	private JTextField txtFileSave;
	private int portMiniServer;
	
	public ClientWindow(String name, String address, int port) {
		setResizable(false);
		setTitle("Share File Client : " + name);
		client = new Client(name, address, port);
		
		boolean connect = client.openConnection(address);
		if (!connect) {
			System.err.println("Connection failed!");
		}
		
		createWindow();
		
		String connection = "/c/" + name + "/e/";
		client.sendToServer(connection.getBytes());
		
		running = true;
		run = new Thread(this, "Running");
		run.start();
		
	}
//----------------------------------------- CREATE WINDOW ----------------------------------------------------
	private void createWindow() {
		JButton btnDelete = new JButton("Delete");
		JButton btnDownload = new JButton("Download");
		
		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
					disconnect();
					System.exit(0);
				}
			}
		});
		mnFile.add(mntmExit);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPaneSearch = new JScrollPane();
		scrollPaneSearch.setBounds(192, 30, 500, 216);
		
		JScrollPane scrollPaneShare = new JScrollPane();
		scrollPaneShare.setBounds(192, 314, 500, 216);
		
		DefaultTableModel model0 = new DefaultTableModel();
		tableShare = new JTable(model0);
		tableShare.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
					btnDelete.setEnabled(true);
				}
			}
		});
		
		tableShare.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"File name", "Type", "Size", "Location" , "ID File"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPaneShare.setViewportView(tableShare);
		
		DefaultTableModel model1 = new DefaultTableModel();
		tableSearch = new JTable(model1);
		tableSearch.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
					btnDownload.setEnabled(true);
				}
			}
		});
		tableSearch.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"File name", "Type", "Size", "Peer" , "ID File"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false , false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPaneSearch.setViewportView(tableSearch);
		contentPane.setLayout(null);
		contentPane.add(scrollPaneShare);
		contentPane.add(scrollPaneSearch);
		
		DefaultTableModel model2 = new DefaultTableModel();
		
		btnDownload.setEnabled(false);
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectDownload(tableSearch);
				tableSearch.clearSelection();
				btnDownload.setEnabled(false);
			}
		});
		btnDownload.setBounds(702, 33, 89, 23);
		contentPane.add(btnDownload);
		
		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					searchFile(txtSearch.getText());
				}
			}
		});
		txtSearch.setBounds(192, 4, 277, 20);
		contentPane.add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchFile(txtSearch.getText());
			}
		});
		btnSearch.setBounds(479, 3, 89, 23);
		contentPane.add(btnSearch);
		
		JButton btnShare = new JButton("Share");
		btnShare.setEnabled(false);
		btnShare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				File file = fileChooserBrowse.getSelectedFile();
				fileName = file.getName();
				fileType = typeOfFile(file);
				fileSize = convertByte(file.length(), false); 
				fileLocation = file.getPath();
				boolean exist = false;
				// kiem tra file da share
				for (int i = 0; i < tableShare.getRowCount(); i++) {
					if (tableShare.getValueAt(i, 3).equals(fileLocation)) {
						exist = true;   // file da ton tai
						JOptionPane.showMessageDialog(null, "File has existed !", "NOTIFY !", JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
				
				if (!exist) {
					// gui thong tin fileShare toi server
					String message = "/e/" + client.getID() + "/e/" + fileName + "/e/" + fileType + "/e/" + fileSize + "/e/" + fileLocation;
					sendToServer(message, true);
					// Mo serverSocket 1 lan
					if (countOpen == 0) {
						try {
							portMiniServer = client.getID() + 1100;
							serverSocket = new ServerSocket(portMiniServer);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						countOpen++;
					}
				}
				
				// xoa Path va khong cho nhan share
				txtFilePath.setText("");
				btnShare.setEnabled(false);
			}
		});
		btnShare.setBounds(702, 349, 89, 23);
		contentPane.add(btnShare);
		
		txtFilePath = new JTextField();
		txtFilePath.setEditable(false);
		txtFilePath.setBounds(261, 283, 431, 20);
		contentPane.add(txtFilePath);
		txtFilePath.setColumns(10);
		
		JLabel lblSelectFile = new JLabel("Select File :");
		lblSelectFile.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSelectFile.setBounds(192, 286, 65, 14);
		contentPane.add(lblSelectFile);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int retBrowse = fileChooserBrowse.showDialog(rootPane, "Browse File");
				if(retBrowse == JFileChooser.APPROVE_OPTION){
					txtFilePath.setText(fileChooserBrowse.getSelectedFile().toString());
					btnShare.setEnabled(true);
				}
			}
		});
		btnBrowse.setBounds(695, 282, 89, 23);
		contentPane.add(btnBrowse);
		
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeSelectedRows(tableShare);
				tableShare.clearSelection();
				btnDelete.setEnabled(false);
			}
		});
		btnDelete.setBounds(702, 493, 89, 23);
		contentPane.add(btnDelete);
		listUser.setFont(new Font("Times New Roman", 0, 24));
		
		
		listUser.setBounds(10, 30, 172, 221);
		contentPane.add(listUser);
		
		Label labelOnline = new Label("Online Users");
		labelOnline.setFont(new Font("Times New Roman", Font.BOLD, 14));
		labelOnline.setBounds(48, 4, 95, 22);
		contentPane.add(labelOnline);
		
		JScrollPane scrollPaneDownload = new JScrollPane();
		scrollPaneDownload.setBounds(10, 314, 172, 216);
		contentPane.add(scrollPaneDownload);
		
		tableDownload = new JTable();
		tableDownload.setFillsViewportHeight(true);
		tableDownload.setShowVerticalLines(true);
		tableDownload.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"File name", "Status"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPaneDownload.setViewportView(tableDownload);
		
		JLabel lblDownloading = new JLabel("DOWNLOADING...");
		lblDownloading.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblDownloading.setBounds(36, 285, 127, 14);
		contentPane.add(lblDownloading);
		
		txtFileSave = new JTextField();
		txtFileSave.setEditable(false);
		txtFileSave.setColumns(10);
		txtFileSave.setBounds(261, 252, 431, 20);
		txtFileSave.setText("E:\\");
		contentPane.add(txtFileSave);
		
		JLabel labelSave = new JLabel("Save As :");
		labelSave.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		labelSave.setBounds(192, 255, 65, 14);
		contentPane.add(labelSave);
		
		JButton button = new JButton("Change...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooserSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retSave = fileChooserSave.showDialog(rootPane, "Save As");
				if(retSave == JFileChooser.APPROVE_OPTION){
					txtFileSave.setText(fileChooserSave.getSelectedFile().toString());
				}
			}
		});
		button.setBounds(695, 251, 89, 23);
		contentPane.add(button);
		
		JComboBox<String> combSelectAlg = new JComboBox<String>();
		combSelectAlg.addItem("RSA");
		combSelectAlg.addItem("DES");
		combSelectAlg.addItem("AES");
		combSelectAlg.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent item) {
				if(item.getItem().toString().equals("RSA")){
					App.setSendingAlgName(AlgType.RSA);
				//	Debug.d("SELECTED", "RSA");
				}else if(item.getItem().toString().equals("DES")){
					App.setSendingAlgName(AlgType.DES);
				//	Debug.d("SELECTED", "DES");
				}else if(item.getItem().toString().equals("AES")){
					App.setSendingAlgName(AlgType.AES);
				//	Debug.d("SELECTED", "AES");
				}	
			}
		});
		combSelectAlg.setBounds(721, 318, 63, 20);
		contentPane.add(combSelectAlg);
		combSelectAlg.setSelectedIndex(0);
		App.setSendingAlgName(AlgType.RSA);
		
		JLabel lblAl = new JLabel("AL:");
		lblAl.setBounds(702, 321, 46, 14);
		contentPane.add(lblAl);
		
		JButton btnCheckMd = new JButton("Check SUM");
		btnCheckMd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckSumDialog dialog = new CheckSumDialog();
				dialog.setResizable(false);
                dialog.setLocationRelativeTo(getRootPane());
                dialog.setModal(true);
                dialog.setVisible(true);
                dialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
			}
		});
		btnCheckMd.setBounds(702, 67, 89, 23);
		contentPane.add(btnCheckMd);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});
		
		setVisible(true);
		
		
	}
	

	public void run() {
		listen();
	}
	
// ---------------------------- disconnect ---------------------------------------
	public void disconnect() {
		String disconnect = "/d/" + client.getID() + "/e/";
		sendToServer(disconnect, false);
		running = false;
		client.close();
	}
	
//----------------------------- send message to server-----------------------------
	private void sendToServer(String message, boolean bShare) { 
		if (message.equals("")) return;
		if (bShare) {
			message = "/s/" + client.getName() + message + "/e/";
		}
		client.sendToServer(message.getBytes());
	}
	
// --------------------------------- receive --------------------------------------
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while (running) {
					String message = client.receive();
					if(message.startsWith("/c/")) {	//------------------------------- successful connect, luu ID					
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1])); 
						//System.out.println(client.getID());
					}
					else if (message.startsWith("/s/")) {//---------------------------- nhan list download
						String[] text = message.split("/s/|/e/");
						setTableSearch(text);
					}
					else if (message.startsWith("/z/")) {//------------------- nhan ID File tu Server
						setTableShare(Integer.parseInt(message.split("/z/|/e/")[1]));
					}
					else if (message.startsWith("/x/")) {//------------------- xoa list table Search
						clearTable(tableSearch);
					}
					else if (message.startsWith("/i/")) {//------------------- phan hoi server
						String text = "/i/" + client.getID() + "/e/";
						sendToServer(text, false);
					}
					else if (message.startsWith("/u/")) {//------------------- nhan list user online
						String[] u = message.split("/u/|/n/|/e/");
						updateUser(Arrays.copyOfRange(u, 1, u.length - 1));
					}
					else if (message.startsWith("/o/")) {//------------------- mo MiniServer port send file
						String pathFile = message.substring(3, message.indexOf("/e/")); // direction File
						
						Files fileShare = new Files(pathFile);
						long size = fileShare.getSize();		  // size file share
						String fileNametoPeer = fileShare.getName();
						
						// open mini server
						SendingPeer sendingFile = new SendingPeer(serverSocket, pathFile);
						sendingFile.start();
						
						message = message.substring(0, message.lastIndexOf("/e/"));
						message = message + "/e/" + portMiniServer + "/e/" + fileNametoPeer + "/e/" + size + "/e/";
						sendToServer(message, false); // gui thong bao miniServer ready!
					}
					else if (message.startsWith("/r/")) {//------------------- respond tu miniserver cho down file
						String[] text = message.split("/r//|/e/");
						String address = text[1];
						int port = Integer.parseInt(text[2]);
						String tenFile = text[3];
						long sizeRecv = Long.parseLong(text[4]);
						
						// Add vao Table Downloading....
						int row_index = setTableDownload(tenFile);
						
						// start download
						ReceivingPeer receivingFile = new ReceivingPeer(address, port, sizeRecv, txtFileSave.getText(), tenFile, row_index, ClientWindow.this);
						receivingFile.start();
					}
					else if (message.startsWith("/m/")) {
						String[] updateList = message.split("/m/|/e/");

						// update table Search
						DefaultTableModel model = (DefaultTableModel) tableSearch.getModel();
						if (model.getRowCount() > 0) {
							for (int i = 1; i < updateList.length - 1; i++) {
								for (int j = 0; j < model.getRowCount(); ) {
									if (tableSearch.getValueAt(j, 4).equals(updateList[i])) {
										model.removeRow(j);
									}
									else {
										j++;
									}
								}
							}
						}
					}
				}
			}
		};
		listen.start();
	}
	
//---------------------------- ADD Table Downloading ....---------------------
	private int setTableDownload(String fileName) {
		int numColTableDownload = tableDownload.getModel().getColumnCount();
		Object[] fill = new Object[numColTableDownload];
		// file name
		fill[0] = fileName;
		
		((DefaultTableModel) tableDownload.getModel()).addRow(fill);
		
		return tableDownload.getModel().getRowCount() - 1;    // tra ve vi tri add vao Table
	}
	
// ----------------------------- search file download ---------------------------
	private void searchFile(String textSearch) {
		textSearch = "/f/" + textSearch + "/e/" + client.getID() + "/e/";
		sendToServer(textSearch, false);
	}
// ----------------------------- add Table Search -----------------------------
	private void setTableSearch(String[] text) {
		String username, fileName, fileType, fileSize, fileLocation, IDFile;
		
		username = text[1];
		fileName = text[3];
		fileType = text[4];
		fileSize = text[5];
		fileLocation = text[6];
		IDFile = text[7];
		
		// them vao tableDownload
		int numColTableSearch = tableSearch.getModel().getColumnCount();
		Object[] fill = new Object[numColTableSearch];
		// file name
		fill[0] = fileName;
		// type 
		fill[1] = fileType;
		// Size
		fill[2] = fileSize;
		// Peer
		fill[3] = username;
		// ID
		fill[4] = IDFile;
		((DefaultTableModel) tableSearch.getModel()).addRow(fill);
	}
// ----------------------------- add table Share ------------------------------
	private void setTableShare(int IDFile) {
		// them vao tableShare
		int numColTableShare = tableShare.getModel().getColumnCount();
		Object[] fill = new Object[numColTableShare];
		// file name
		fill[0] = fileName;
		// type 
		fill[1] = fileType;
		// Size
		fill[2] = fileSize;
		// Location
		fill[3] = fileLocation;
		// IDFile
		fill[4] = IDFile;
		
		((DefaultTableModel) tableShare.getModel()).addRow(fill);
	}
	
//----------------- update user--------------------------
	public void updateUser(String[] users) {
		listUser.setListData(users);
	}
	

//--------------- Kieu mo rong cua file------------------
	private String typeOfFile(File file) {
		int dot = file.getName().lastIndexOf('.');
		return file.getName().substring(dot + 1);
	}
	
//---------- chuyen byte -> kB, MB, GB, TB....--------------
	public static String convertByte(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + "";
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
//------------------ xoa hang tableShare---------------------
	private void removeSelectedRows(JTable table){
		   String selDel = "/x/";	
		
		   DefaultTableModel model = (DefaultTableModel) table.getModel();
		   int[] rows = table.getSelectedRows();
		   for(int i = 0; i < rows.length; i++){
			 selDel += model.getValueAt(rows[i] - i, 4) + "/e/";
		     model.removeRow(rows[i] - i);
		   }
		   sendToServer(selDel, false);
	}
	
//----------------- chon hang Download------------------------
		private void selectDownload(JTable table){
			   String selDown = "/p/" + client.getID() + "/e/" + client.getName() + "/e/";	
			
			   DefaultTableModel model = (DefaultTableModel) table.getModel();
			   int[] rows = table.getSelectedRows();
			   for(int i = 0; i < rows.length; i++){
				 selDown += model.getValueAt(rows[i], 4) + "/e/";
			   }
			   sendToServer(selDown, false);
		}
	
//------------------ clean all table-------------------------
	private void clearTable(JTable table) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}
	
//-------------------- update Status ---------------------------
	public synchronized void updateStatus(int rows, long percent, String str){
		tableDownload.setValueAt(percent+"% "+ str, rows, 1);
	}
}
