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
			Account account = (Client.Account) ois.readObject();		// account  객체 클라이언트에서 전송 받음
			
			if(account == null){
				continue;
			}
			String id = account.getId();						// 디비에 있는 계정 정보와 맞는지 확인
			String pw = account.getPw();
			ResultSet rs = db.getQueryResult("SELECT id, pw FROM tetris.account WHERE id = '" + id + "' and pw = '" + pw + "';");

			if(rs.next()){										// 맞으면 결과 값이 있으니까 true
				System.out.println("로그인 성공");
				this.id = id;
				output.println("login,true");
				break;
			}
			else{
				System.out.println("로그인 실패");
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
			receiveAccount();										// 로그인 
			System.out.println("로그인 완료");
			while(socketOn){
				String response = input.readLine();
				if(response == null){
					continue;
				}
								
				if(response.startsWith("chat")){
					String chat = response.split(",")[1];
					sm.sendAll("chat," + id + " : " + chat);
				}
				
				if(response.startsWith("start")){					// 클라이언트가 게임 시작을 누름
					sm.addReadyQueue(this);
				}
				
				if (response.startsWith("NEW")) {				//클라이언트에서 새로 생긴 블록 번호
					other.output.println(response);
				}

				if(response.startsWith("MOVE")){					// 클라이언트에서 키보드를 누를 때 다른 클라이언트로 전송				
					other.output.println("OTHER,"+ response.split(",")[1]);
				}
				
				if(response.startsWith("DEL")){					// 클라이언트에서 라인을 부숨을 알림
					int num = (int)(Math.random()*10);
					output.println("OADD,"+num);
					other.output.println("ADD,"+num);
				}
				
				if(response.startsWith("QUIT")){
					output.println("QUIT,lose");
					other.output.println("QUIT,win");
					saveWin(getID(), other.getID());
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
				System.out.println("소켓 종료T");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}