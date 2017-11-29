package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;

public class Server {
	private Vector<ServerThread> vector = new Vector<ServerThread>();
	private ServerManager sm = new ServerManager(vector);
	
	public Server() throws IOException{
		System.out.println("서버 시작");
		ServerSocket ss = new ServerSocket(9001);
		sm.start();
		try{
			while(true){
				ServerThread t = new ServerThread(ss.accept(), sm);
				sm.addVector(t);
				System.out.println("연결");
				t.start();
			}
		} finally{
			ss.close();
		}
	}
	
	public static void main(String[] args){
		try {
			Server server = new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

