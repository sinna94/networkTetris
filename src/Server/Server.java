package Server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
		
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(9001);
		System.out.println("서버 시작");
		try{
			while(true){
				
				Player player1 = new Player(ss.accept());
				Player player2 = new Player(ss.accept());
				
				player1.setOther(player2);
				player2.setOther(player1);
				
				player1.start();
				player2.start();
				
				System.out.println("두명이 접속함");
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
	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	Player other;
	
	public Player(Socket socket){
		
		this.socket = socket;
		
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			output.println("LOADING");
			
		}catch(IOException e){
			System.out.println("연결이 끊어졌습니다.1");
		}
	}
	
	public void setOther(Player other){
		this.other = other;
	}
	
	public void run(){
		String command;
		try{
			output.println("모든 경기자가 연결되었습니다.");
			output.println("READY");
			int blockNum = (int) (Math.random() * 7);
			output.println("GO");						// 처음 시작 블록 전송
		//	output.println(""+blockNum);
		//	other.output.println(""+blockNum);
			
			while(true){
				command = input.readLine();
							
				if(command == null){
					continue;
				}

				if (command.startsWith("NEW")) {				//클라이언트에서 새로 생긴 블록 번호
					String Num = input.readLine();
					other.output.println("NEW,"+Num);
					//other.output.println(Num);
					
				}

				if(command.startsWith("MOVE")){					// 클라이언트에서 키보드를 누를 때 다른 클라이언트로 전송
					String KeyCode = input.readLine();					
					other.output.println("OTHER,"+KeyCode);
				//	other.output.println(KeyCode);
				}
				
				if(command.startsWith("DEL")){					// 클라이언트에서 라인을 부숨을 알림
					int num = (int)(Math.random()*10);
					output.println("OADD,"+num);
					other.output.println("ADD,"+num);
				}
				
				if(command.startsWith("QUIT")){
					return ;
				}
			}
		} catch (IOException e){
			System.out.println("연결이 끊어졌습니다.2");
		} finally{
			try{
				stop();
				socket.close();
			} catch (IOException e){
			}
		}
	}
}