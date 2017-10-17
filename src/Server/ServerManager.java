package Server;

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
}
