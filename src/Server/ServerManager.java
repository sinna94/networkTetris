package Server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class ServerManager extends Thread{
	private Vector<ServerThread> vector;
	private Vector<Socket> readyQ = new Vector<Socket>();
	
	public ServerManager(Vector<ServerThread> vector) {
		this.vector = vector;
	}
	
	public void sendAll(String str){
		for(int i=0;i<vector.size();i++){
			vector.get(i).sendMessage(str);
		}
	}
	
	public void addVector(ServerThread t){
		vector.add(t);
		System.out.println("서버 접속, 접속자 수 : " + vector.size());
	}
	
	public void socketClose(Socket s){
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				vector.remove(i);
				System.out.println("접속 종료, 접속자 수 : " + vector.size());
			}
		}
	}
	
	public ServerThread searchSocket(Socket s){				// 전체 벡터에서 특정 소켓 찾기
		ServerThread t = null;
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				t = vector.get(i);
			}
		}
		return t;
	}
	
	public void sendready(ServerThread serverThread){		// 클라이언트에게 시작 메시지 전송
		serverThread.sendMessage("start");
	}
	
	public boolean searchQueue(Socket s){
		Socket so;
		for(int i = 0;i <readyQ.size();i++){
			so = readyQ.get(i);
			if(so == s){
				return true;
			}
		}
		return false;		
	}
	
	public void addReadyQueue(Socket s){				// 준비 큐에 소켓을 넣기
		if(!searchQueue(s)){
			readyQ.add(s);
		}
	}
	
	public void run(){
		while(true){			
			if(readyQ.size() >= 2){
				
				Socket s1 = readyQ.firstElement();
				readyQ.remove(0);
				Socket s2 = readyQ.firstElement();
				readyQ.remove(0);
				
				sendready(searchSocket(s1));
				sendready(searchSocket(s2));

				try {
					Player player1 = new Player(s1);
					Player player2 = new Player(s2);

					player1.setOther(player2);
					player2.setOther(player1);

					player1.start();
					player2.start();

					System.out.println("두명이 접속함");

				} finally {
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
