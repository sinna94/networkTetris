package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login implements Runnable{

	private Socket socket;
	private ConnectDB db;
	public Login(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			db = new ConnectDB();
			
			while(true){
				System.out.println("수신 대기중");
				String response = input.readLine();
				System.out.println(response + " 수신 완료");
				if(response == null){
					continue;
				}
				
				if(response.startsWith("Login")){
					String id = response.split(",")[1];
					String pw = response.split(",")[2];
					ResultSet rs = db.getQueryResult("SELECT id, pw FROM tetris.account WHERE id = '" + id + "' and pw = '" + pw + "';");
					
					while (rs.next()) {
						if(id.equals(rs.getString("id"))  && pw.equals(rs.getString("pw"))){
							output.println("true");
						}
						else{
							output.println("false");
						}
						System.out.println("발신 완료");
						socket.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		/*
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Account account = (Account) ois.readObject();
			System.out.println(account.getId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
}
