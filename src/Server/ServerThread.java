package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import Client.Account;

public class ServerThread extends Thread{

	private Socket socket;
	private ConnectDB db;
	ServerManager sm;
	public BufferedReader input;
	public PrintWriter output;
	private ObjectInputStream ois;
	private String id;
	private boolean socketOn = true;
	
	ServerThread other;
	
	public ServerThread(Socket socket, ServerManager sm) throws IOException {
		this.socket = socket;
		this.sm = sm;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		ois = new ObjectInputStream(socket.getInputStream());
		output = new PrintWriter(socket.getOutputStream(), true);
		db = new ConnectDB();
	}

	public void setOther(ServerThread other){
		this.other = other;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public String getID(){
		return id;
	}
	
	public void setSocketOn(boolean socketOn){
		this.socketOn = socketOn;
	}
	
	public void receiveAccount() throws ClassNotFoundException, IOException, SQLException{
		while (true) {
			Account account = (Client.Account) ois.readObject();		// account  ��ü Ŭ���̾�Ʈ���� ���� ����
			
			if(account == null){
				continue;
			}
			String id = account.getId();						// ��� �ִ� ���� ������ �´��� Ȯ��
			String pw = account.getPw();
			ResultSet rs = db.getQueryResult("SELECT id, pw FROM tetris.account WHERE id = '" + id + "' and pw = '" + pw + "';");

			if(rs.next()){										// ������ ��� ���� �����ϱ� true
				System.out.println("�α��� ����");
				this.id = id;
				output.println("login,true");
				break;
			}
			else{
				System.out.println("�α��� ����");
				output.println("login,false");
			}
		}
	}
	
	public void sendMessage(String str){
		output.println(str);
	}
	
	public void saveWin(String winner, String loser) throws SQLException{
		db.noExcuteQuery("INSERT INTO game (winner, loser) VALUES ('" + winner + "', '" + loser + "');");
	}
		
	
	@Override
	public void run() {		
		try {
			receiveAccount();										// �α��� 
			System.out.println("�α��� �Ϸ�");
			while(socketOn){
				String response = input.readLine();
				if(response == null){
					continue;
				}
								
				if(response.startsWith("chat")){
					String chat = response.split(",")[1];
					sm.sendAll("chat," + id + " : " + chat);
				}
				
				if(response.startsWith("start")){					// Ŭ���̾�Ʈ�� ���� ������ ����
					sm.addReadyQueue(this);
				}
				
				if (response.startsWith("NEW")) {				//Ŭ���̾�Ʈ���� ���� ���� ��� ��ȣ
					other.output.println(response);
				}

				if(response.startsWith("MOVE")){					// Ŭ���̾�Ʈ���� Ű���带 ���� �� �ٸ� Ŭ���̾�Ʈ�� ����				
					other.output.println("OTHER,"+ response.split(",")[1]);
				}
				
				if(response.startsWith("DEL")){					// Ŭ���̾�Ʈ���� ������ �μ��� �˸�
					int num = (int)(Math.random()*10);
					output.println("OADD,"+num);
					other.output.println("ADD,"+num);
				}
				
				if(response.startsWith("QUIT")){
					output.println("QUIT,lose");
					other.output.println("QUIT,win");
					saveWin(getID(), other.getID());
				}
				
				if(response.startsWith("ITEM")){
					other.output.println(response);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				sm.socketClose(socket);
				System.out.println("���� ����T");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}