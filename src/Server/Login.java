package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Login implements Runnable{

	private Socket socket;
	
	public Login(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Account account = (Account) ois.readObject();
			System.out.println(account.getId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
