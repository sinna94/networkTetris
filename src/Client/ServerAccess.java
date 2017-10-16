package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerAccess {
	public Socket socket;
	public BufferedReader input;
	public static PrintWriter output;
	public ObjectOutputStream oos;
	
	
	public ServerAccess() throws IOException{
		socket = new Socket("localhost", 9001);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
}
