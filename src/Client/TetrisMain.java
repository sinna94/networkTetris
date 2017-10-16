package Client;

import java.io.IOException;

public class TetrisMain {
	public static void main(String[] args)  {
		LoginUI login;
		try {
			login = new LoginUI();
			login.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
