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
	private ServerManager sm;
	private BufferedReader input;
	private PrintWriter output;
	private ObjectInputStream ois;
	private String id;
	
	public ServerThread(Socket socket, ServerManager sm) throws IOException {
		this.socket = socket;
		this.sm = sm;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		ois = new ObjectInputStream(socket.getInputStream());
		output = new PrintWriter(socket.getOutputStream(), true);
		db = new ConnectDB();
	}

	public Socket getSocket(){
		return socket;
	}
	
	public String getID(){
		return id;
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
	
	@Override
	public void run() {		
		try {
			receiveAccount();
			System.out.println("�α��� �Ϸ�");
			while(true){
				String response = input.readLine();
				
				if(response == null){
					continue;
				}
				
				System.out.println(response);
				
				if(response.startsWith("chat")){
					String chat = response.split(",")[1];
					sm.sendAll("chat," + id + " : " + chat);
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
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}