package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerAccess {
	public Socket socket;
	public BufferedReader input;
	public static PrintWriter output;
	public ObjectOutputStream oos;
	public BufferedWriter bos;
	
	public ServerAccess() throws IOException{
		socket = new Socket("localhost", 9001);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		oos = new ObjectOutputStream(socket.getOutputStream());
		bos = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	public Socket getSocket(){
		return socket;
	}
}
