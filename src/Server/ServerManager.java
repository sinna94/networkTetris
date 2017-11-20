package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerManager extends Thread{
	private Vector<ServerThread> vector;
	private Vector<ServerThread> readyQ = new Vector<ServerThread>();
	
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
	
	public void socketClose(Socket s) throws IOException{
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				vector.get(i).setSocketOn(false);
				vector.get(i).getSocket().close();
				vector.remove(i);
				
				System.out.println("접속 종료, 접속자 수 : " + vector.size());
			}
		}
	}
	
	public ServerThread searchSocket(ServerThread s){				// 전체 벡터에서 특정 서버 쓰레드 찾기
		ServerThread t = null;
		for(int i=0;i<vector.size();i++){
			if(vector.get(i) == s){
				t = vector.get(i);
			}
		}
		return t;
	}
	
	public void sendReady(ServerThread serverThread){		// 클라이언트에게 시작 메시지 전송
		serverThread.sendMessage("start");
	}
	
	public void sendGo(ServerThread serverThread){		// 클라이언트에게 시작 메시지 전송
		serverThread.sendMessage("GO");
	}
	
	public boolean searchQueue(ServerThread s){				// 준비 큐에서 서버 쓰레드 찾기
		ServerThread so;
		for(int i = 0;i <readyQ.size();i++){
			so = readyQ.get(i);
			if(so == s){
				return true;
			}
		}
		return false;		
	}
	
	public void addReadyQueue(ServerThread s){				// 준비 큐에 서버 쓰레드 넣기
		if(!searchQueue(s)){
			readyQ.add(s);
		}
	}
	
	public void run(){
		while(true){			
			if(readyQ.size() >= 2){
				
				ServerThread s1 = readyQ.firstElement();
				readyQ.remove(0);
				ServerThread s2 = readyQ.firstElement();
				readyQ.remove(0);
				
				s1.setOther(s2);
				s2.setOther(s1);
				
				sendReady(searchSocket(s1));
				sendReady(searchSocket(s2));
				
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
