package Client;

import java.io.IOException;

public class TetrisMain {
	public static void main(String[] args) throws IOException  {
		LoginUI login;
		ServerAccess sa= new ServerAccess();
		try {
			sa.start();
			login = new LoginUI(sa);
			login.setVisible(true);
		} catch (IOException e) {
			sa.closeSocket();
		}
	}
}
