package Server;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(9001);
		System.out.println("���� ����");
		
		try{
			while(true){
				
				Player player1 = new Player(ss.accept());
				Player player2 = new Player(ss.accept());
				
				player1.setOther(player2);
				player2.setOther(player1);
				
				player1.start();
				player2.start();
				
				System.out.println("�θ��� ������");
			}
		} finally{
			ss.close();
		}
	}
}

class Player extends Thread{
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	Player other;
	
	public Player(Socket socket){
		
		this.socket = socket;
		
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			output.println("LOADING");
			
		}catch(IOException e){
			System.out.println("������ ���������ϴ�.1");
		}
	}
	
	public void setOther(Player other){
		this.other = other;
	}
	
	public void run(){
		try{
			output.println("��� ����ڰ� ����Ǿ����ϴ�.");
			output.println("START");
			while(true){
				Object command = input.readLine();
				if(command == null){
					continue;
				}
				
				if(((String) command).startsWith("MOVE")){					// Ŭ���̾�Ʈ���� Ű���带 ���� �� �ٸ� Ŭ���̾�Ʈ�� ����
					System.out.println("����");
					command = input.readLine();
					other.output.println("OTHER");
					other.output.println(command);
				}
				
				else if(((String) command).startsWith("QUIT")){
					return ;
				}
			}
		} catch (IOException e){
			System.out.println("������ ���������ϴ�.2");
		} finally{
			try{
				socket.close();
			} catch (IOException e){
			}
		}
	}
}