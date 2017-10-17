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

public class Login implements Runnable{

	private Socket socket;
	private ConnectDB db;
	public Login(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {		
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			db = new ConnectDB();
			boolean c = true;
			
			while (c) {
				Account account = (Client.Account) ois.readObject();		// account  객체 클라이언트에서 전송 받음
				
				if(account == null){
					continue;
				}
				
				String id = account.getId();						// 디비에 있는 계정 정보와 맞는지 확인
				String pw = account.getPw();
				System.out.println(id + " " + pw);
				ResultSet rs = db.getQueryResult("SELECT id, pw FROM tetris.account WHERE id = '" + id + "' and pw = '" + pw + "';");

				if(rs.next()){										// 맞으면 결과 값이 있으니까 true
					output.println("true");
					c = false;
				}
				else{
					output.println("false");
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
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
