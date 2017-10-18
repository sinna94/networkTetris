package Client;

import java.io.IOException;

public class TetrisMain {
	public static void main(String[] args) throws IOException  {
		LoginUI login;
		ServerAccess sv = new ServerAccess();
		try {
			sv.start();
			login = new LoginUI(sv);
			login.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
