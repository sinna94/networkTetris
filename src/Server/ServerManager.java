package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import Communication.LoginList;

public class ServerManager extends Thread{
	private Vector<ServerThread> vector;
	private Vector<ServerThread> readyQ = new Vector<ServerThread>();
	private LoginList l = new LoginList();
		
	public ServerManager(Vector<ServerThread> vector) {
		this.vector = vector;
	}

	public LoginList getLoginList(){
		return l;
	}
	
	public void setLoginList(String id){
		l.setList(id);
		for(int i =0;i<l.getList().size();i++){
			System.out.println(l.getList().get(i));
		}
	}
	
	public void sendAll(String str){
		for(int i=0;i<vector.size();i++){
			vector.get(i).sendMessage(str);
		}
	}
	
	public void sendAllNM(ServerThread st, String str){
		for(int i=0;i<vector.size();i++){
			if(vector.get(i) != st){
				vector.get(i).sendMessage(str);
			}
		}
	}
	
	public void addVector(ServerThread t){
		vector.add(t);
		System.out.println("���� ����, ������ �� : " + vector.size());
	}
	
	public void socketClose(Socket s) throws IOException{
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				vector.get(i).setSocketOn(false);
				vector.get(i).getSocket().close();
				vector.remove(i);
				
				l.remove(i);
				
				System.out.println("���� ����, ������ �� : " + vector.size());
			}
		}
	}
	
	public ServerThread searchSocket(ServerThread s){				// ��ü ���Ϳ��� Ư�� ���� ������ ã��
		ServerThread t = null;
		for(int i=0;i<vector.size();i++){
			if(vector.get(i) == s){
				t = vector.get(i);
			}
		}
		return t;
	}
	
	public void sendReady(ServerThread serverThread){		// Ŭ���̾�Ʈ���� ���� �޽��� ����
		serverThread.sendMessage("start");
	}
	
	public void sendGo(ServerThread serverThread){		// Ŭ���̾�Ʈ���� ���� �޽��� ����
		serverThread.sendMessage("GO");
	}
	
	public boolean searchQueue(ServerThread s){				// �غ� ť���� ���� ������ ã��
		ServerThread so;
		for(int i = 0;i <readyQ.size();i++){
			so = readyQ.get(i);
			if(so == s){
				return true;
			}
		}
		return false;		
	}
	
	public void addReadyQueue(ServerThread s){				// �غ� ť�� ���� ������ �ֱ�
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
