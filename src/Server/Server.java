package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;

public class Server {
	private Vector<ServerThread> vector = new Vector<ServerThread>();
	private ServerManager sm = new ServerManager(vector);
	
	public Server() throws IOException{
		System.out.println("���� ����");
		ServerSocket ss = new ServerSocket(9001);
		try{
			while(true){
				ServerThread t = new ServerThread(ss.accept(), sm);
				vector.add(t);
				System.out.println("����");
				t.start();
			}
		} finally{
			ss.close();
		}
		/*
		
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
		*/
	}
	
	public static void main(String[] args){
		try {
			Server server = new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

