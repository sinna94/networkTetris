package Server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(9001);
		System.out.println("서버 시작");
		
		try{
			while(true){
				Game Game1 = new Game();
				Game Game2 = new Game();
				
				Player player1 = new Player(Game1, Game2, ss.accept());
				Player player2 = new Player(Game2, Game1, ss.accept());
				
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

class Game{
	Color[][] board = new Color[20][10];
	
}

class Player extends Thread{
	Game game1;
	Game game2;
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	Player other;
	
	public Player(Game game1, Game game2, Socket socket){
		this.game1 = game1;
		this.game2 = game2;
		this.socket = socket;
		
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			output.println("LOADING");
			
		}catch(IOException e){
			System.out.println("연결이 끊어졌습니다.1");
		}
	}
	
	public void setOther(Player other){
		this.other = other;
	}
	
	public void run(){
		try{
			output.println("모든 경기자가 연결되었습니다.");
			output.println("START");
			while(true){
				String command = input.readLine();
				if(command == null){
					continue;
				}
				if(command.startsWith("Move")){
					
				}
				else if(command.startsWith("QUIT")){
					return ;
				}
			}
		} catch (IOException e){
			System.out.println("연결이 끊어졌습니다.2");
		} finally{
			try{
				socket.close();
			} catch (IOException e){
			}
		}
	}
}