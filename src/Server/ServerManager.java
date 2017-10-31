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
		System.out.println("���� ����, ������ �� : " + vector.size());
	}
	
	public void socketClose(Socket s){
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				vector.remove(i);
				System.out.println("���� ����, ������ �� : " + vector.size());
			}
		}
	}
	
	public ServerThread searchSocket(Socket s){				// ��ü ���Ϳ��� Ư�� ���� ã��
		ServerThread t = null;
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				t = vector.get(i);
			}
		}
		return t;
	}
	
	public void sendready(ServerThread serverThread){		// Ŭ���̾�Ʈ���� ���� �޽��� ����
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
	
	public void addReadyQueue(Socket s){				// �غ� ť�� ������ �ֱ�
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

					System.out.println("�θ��� ������");

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
