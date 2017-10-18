package Server;

import java.net.Socket;
import java.util.Vector;

public class ServerManager {
	private Vector<ServerThread> vector;
	
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
		System.out.println("辑滚 立加, 立加磊 荐 : " + vector.size());
	}
	
	public void socketClose(Socket s){
		for(int i=0;i<vector.size();i++){
			if(vector.get(i).getSocket() == s){
				vector.remove(i);
				System.out.println("立加 辆丰, 立加磊 荐 : " + vector.size());
			}
		}
	}
}
