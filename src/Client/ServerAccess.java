package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ServerAccess extends Thread{
	public Socket socket;
	public BufferedReader input;
	public static PrintWriter output;
	public ObjectOutputStream oos;
	public BufferedWriter bos;
	public boolean loginOK = false;
	public boolean isRecieve = false;
	private JTextArea chatArea;
	
	public ServerAccess() throws IOException{
		socket = new Socket("localhost", 9001);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		oos = new ObjectOutputStream(socket.getOutputStream());
		bos = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public void setTextArea(JTextArea chatArea){
		this.chatArea = chatArea;
	}
	
	public void run(){
		while(true){
			String response = null;
			try {
				response = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(response == null){
				continue;
			}
			System.out.println(response);
			if(response.startsWith("login")){					// 로그인 정보 받기
				String correct = response.split(",")[1];
				if (correct.equals("true") == true) {
					loginOK = true;
					System.out.println("로그인 성공");
				} 
				isRecieve = true;
			}
			if(response.startsWith("chat")){					// 채팅 받기
				String chat = response.split(",")[1];
				addString("\n"+chat);
			}
		}
	}
	
	public void addString(String str){
		chatArea.append(str);
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

	public void sendMessage(String text) {
		output.println("chat," + text);
		
	}
}
