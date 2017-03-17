package cse.crypto.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.MinimalHTMLWriter;
import javax.xml.ws.handler.MessageContext;


public class Server implements Runnable {
	private ServerWindow serverWindow;
	
	private List<ListClient> clients = new ArrayList<ListClient>();
	private List<Integer> clientResponse = new ArrayList<Integer>(); 
	private List<ListDownload> listDownload = new ArrayList<ListDownload>();  
	
	private final int MAX_ATTEMPTS = 5;
	
	private DatagramSocket socket;
	private int port;
	private boolean running = false; 
	private Thread run, manage, send, receive;
	
	public Server(int port, ServerWindow serverWindow) {
		this.port = port;
		this.serverWindow = serverWindow;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}

	public void run() {
		running = true;
		serverWindow.updateInfor("Server started on port " + port);
		manageClients();
		receive();
	}
	
	// ---------------- kiem tra User online ------------------------
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					sendToAll("/i/server");
					sendStatus();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i = 0; i < clients.size(); i++) {
						ListClient c = clients.get(i);
						if (!clientResponse.contains(c.getID())) {
							if (c.attempt >= MAX_ATTEMPTS) {
								disconnect(c.getID(), false);
							}
							else {
								c.attempt++;
							}
						}
						else {
							clientResponse.remove(new Integer(c.getID()));
							c.attempt = 0;
						}
					}
				}
			}
		};
		manage.start();
	}
	
	//---------------- gui danh sach Online User cho peer-------------------
	private void sendStatus() {
		if (clients.size() <= 0) return;
		String users = "/u/";
		for (int i = 0; i < clients.size() - 1; i++) {
			users += clients.get(i).name + "/n/";
		}
		users += clients.get(clients.size() - 1).name + "/e/";
		sendToAll(users);
	}
	
	// --------------------- receive --------------------------------
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (SocketException e) {
					} catch (IOException e) {
						e.printStackTrace();
					}
					// kiem tra chuc nang 'packet nhan duoc' 
					processTest(packet);
					
				}
			}
		
		};
		receive.start();
	}
	
	//------------------- send all - string ---------------------------
	private void sendToAll(String message) {
		for (int i = 0; i < clients.size(); i++) {
			ListClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	//------------------ send one - string----------------------------
	private void send(String message, InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}
	
	//----------------- send one - byte  -----------------------------
	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	//------------------ gui List Download cho tat ca Client-------------------------------------
	private void sendListFind(String string, InetAddress address, int port, int ID) {
		send("/x/" + "clrTable", address, port);   // gui thong bao xoa Table Download de update
		
		try {
			Thread.sleep(4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < listDownload.size(); i++) {
			if (listDownload.get(i).getfileName().matches("(?i)(" + string + ").*") && listDownload.get(i).getID() != ID) {
				String txt = listDownload.get(i).getString() + "/e/" + listDownload.get(i).getIDFile();
				send(txt, address, port);
			}
		}
	}
	
	//--------------------------- kiem tra yeu cau tu client------------------------------------
	private void processTest(DatagramPacket packet) {
		String string = new String(packet.getData());
		if (string.startsWith("/c/")) {//---------------- client dang nhap -> luu username
			int id = RandomID.getIdentifier();  // random ID luu vao List clients
			String name = string.split("/c/|/e/")[1];
			
			serverWindow.updateInfor(name + " (ID: " + id + ") Connected!");
			clients.add(new ListClient(name ,packet.getAddress(), packet.getPort(), id));
			//sendListDownload();
			
			String ID = "/c/" + id;
			send(ID, packet.getAddress(), packet.getPort());
		}
		else if(string.startsWith("/s/")) {//-------------- client muon share file
			int IDFile = RandomID.getIdentifier();
			String ID = string.split("/s/|/e/")[2];			// ID username
			String fileName = string.split("/s/|/e/")[3];	// file name
			String txtIDFile = "/z/" + IDFile + "/e/"; 		// IDFile
			
			string = string.substring(0, string.lastIndexOf("/e/"));
			listDownload.add(new ListDownload(Integer.parseInt(ID), fileName ,packet.getAddress(), packet.getPort(), string, IDFile));
			
			send(txtIDFile, packet.getAddress(), packet.getPort());
		
		}
		else if (string.startsWith("/p/")) {//------------- client muon download file
			string = string.substring(0, string.lastIndexOf("/e/"));
			String[] selDown = string.split("/p/|/e/");
			/*
			 * selDown[1] == ID peer muon download
			 * selDown[2] == username peer muon download
			 */
			for (int i = 3; i < selDown.length; i++) {
				for (int j = 0; j < listDownload.size(); j++) {
					if (listDownload.get(j).getIDFile() == Integer.parseInt(selDown[i])) {
						String filePath = listDownload.get(j).getString().split("/s/|/e/")[6]; // lay Path file
						String sOpenMiniServer = "/o/" + filePath + "/e/" + selDown[1] + "/e/" + selDown[2] + "/e/" 
											           + packet.getAddress() + "/e/" + packet.getPort();
						send(sOpenMiniServer, listDownload.get(j).getAddress(), listDownload.get(j).getPort());
					}
				}
			}
		}	
		else if (string.startsWith("/o/")) {//--------------- client send infor P2P connected success 
			String[] respondMiniServer = string.split("/o/|/e/");
			try {
				InetAddress ip = InetAddress.getByName(respondMiniServer[4].substring(1, respondMiniServer[4].length()));
				int portPeer = Integer.parseInt(respondMiniServer[5]);
				// respondMiniServer[6] == port miniserver;
				// respondMiniServer[7] == file name;
				// respondMiniServer[8] == file size;
				string = "/r/" + packet.getAddress() + "/e/" + respondMiniServer[6] + "/e/" + respondMiniServer[7] + "/e/" + respondMiniServer[8];
				send(string, ip, portPeer);
			} 
			catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		else if (string.startsWith("/x/")) {//--------------- client muon xoa file shared
			String txtUpdateList = string;
			string = string.substring(0, string.lastIndexOf("/e/"));
			String[] selDel = string.split("/x/|/e/");
			
			for (int i = 1; i < selDel.length; i++) {
				for (int j = 0; j < listDownload.size(); ) {
					if (listDownload.get(j).getIDFile() == Integer.parseInt(selDel[i])) {
						listDownload.remove(j);
					}
					else {
						j++;
					}
				}
			}

			// gui listDownload update cho all client
			txtUpdateList = "/m/" + txtUpdateList.substring(3, txtUpdateList.lastIndexOf("/e/")) + "/e/";
			sendToAll(txtUpdateList);
			
		}
		else if (string.startsWith("/d/")) {//------------------ client muon disconnect
			String id = string.split("/d/|/e/")[1];
			disconnect(Integer.parseInt(id), true);
		}
		else if (string.startsWith("/i/")) {//------------------  client response
			clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
		}
		else if (string.startsWith("/f/")) { //--------------------client muon Search file
			String[] txtFind = string.split("/f/|/e/");
			
			sendListFind(txtFind[1], packet.getAddress(), packet.getPort(), Integer.parseInt(txtFind[2]));
		}
	}
	
	//------------------ server quit-------------------------------------------------
	public void quit() {
		for (int i = 0; i < clients.size(); i++) {
			disconnect(clients.get(i).getID(), true);
		}
		running = false;
		socket.close();
	}
	
	//---------------- xoa Peer va in thong bao ra man hinh server----------------------------------
	private void disconnect(int id, boolean status) {
		ListClient c = null;
		boolean existed = false;
		String txtUpdateListDownload = "/m/";
		
		for (int i = 0; i < listDownload.size(); ){
			if (listDownload.get(i).getID() == id) {
				txtUpdateListDownload += listDownload.get(i).getIDFile() + "/e/";
				listDownload.remove(i);
			}
			else {
				i++;
			}
		}
		sendToAll(txtUpdateListDownload);
		
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getID() == id) {
				c = clients.get(i);
				clients.remove(i);
				existed = true;
				break;
			}
		}
		if (!existed) return;
		String message = "";
		if (status) {
			message = "Peer '" + c.name + "' (ID: " + c.getID() + ") " + c.address.toString() + ":" + c.port + " Disconnected."; 
		}
		else {
			message = "Peer '" + c.name + "' (ID: " + c.getID() + ") " + c.address.toString() + ":" + c.port + " Time out."; 
		}
		serverWindow.updateInfor(message);
	}
	
}
