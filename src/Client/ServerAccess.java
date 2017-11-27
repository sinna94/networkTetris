package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import Communication.Account;
import Communication.DataList;
import Communication.LoginList;

public class ServerAccess extends Thread{
	public Socket socket;
	public BufferedReader input;
	public static PrintWriter output;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public boolean loginOK = false;
	public boolean isRecieve = false;
	private boolean isRun = true; 
	
	private LobbyUI lobby;
	private LoginUI login;
	private RecordUI record;
	private JoinUI join;
	
	private String userId;
	
	public ServerAccess() throws IOException{
		socket = new Socket("localhost", 9001);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public void closeSocket() throws IOException{
		socket.close();
		isRun = false;
	}
	
	public void setLogin(LoginUI login){
		this.login = login;
	}
	
	public void setLobby(LobbyUI lobby){
		this.lobby = lobby;
	}
	
	public void setRecord(RecordUI record){
		this.record = record;
	}
		
	public void setJoin(JoinUI join){
		this.join = join;
	}
	
	public void run(){
		while(isRun){
			String response = null;
			
				try {
					response = input.readLine();
				} catch (IOException e1) {
					try {
						closeSocket();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			System.out.println(response);
			
			if(response == null){
				continue;
			}
			
			if(response.startsWith("login")){					// 로그인 정보 받기
				String correct = response.split(",")[1];
				if (correct.equals("true") == true) {
					login.loginOK();				
					output.println("list");
					try {
						LoginList l = (LoginList) ois.readObject();
						lobby.setList(l);
						lobby.addList();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
				else{
					login.loginFail();
				}
			}
			
			if(response.startsWith("list")){
				
			}
			
			if(response.startsWith("chat")){					// 채팅 받기
				String chat = response.split(",")[1];
				lobby.addString("\n"+chat);
			}
			
			if(response.startsWith("record")){
				try {
					DataList list = (DataList) ois.readObject();
					record.setRecord(list);
					record.addData();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(response.startsWith("in")){
				String p = response.split(",")[1];
				lobby.addList(p);
			}
			
			if(response.startsWith("out")){
				String p = response.split(",")[1];
				lobby.delList(p);
			}
			
			if(response.startsWith("start")){					// 게임 시작
				try {
					System.out.println("게임을 시작합니다.");
					lobby.runGame();
					TetrisGame game = new TetrisGame();
					TetrisThread tt = new TetrisThread(lobby, this, game);
					tt.go();
										
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void login(String id, String pw) {
		Account account = new Account();
		account.setId(id);
		account.setPw(pw);
		try {
			oos.writeObject((Account) account);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startGame(){
		output.println("start");
	}
	
	public void sendMessage(String text) {
		output.println("chat," + text);
		
	}
	
	public void setIsRun(boolean run){
		isRun = run;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
