package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Player extends Thread{
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	Player other;
	
	public Player(Socket socket){
		
		this.socket = socket;
		
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			output.println("LOADING");
			
		}catch(IOException e){
			System.out.println("������ ���������ϴ�.1");
		}
	}
	
	public void setOther(Player other){
		this.other = other;
	}
	
	public void run(){
		String command;
		try{
			output.println("��� ����ڰ� ����Ǿ����ϴ�.");
			output.println("READY");
			int blockNum = (int) (Math.random() * 7);
			output.println("GO");						// ó�� ���� ���� ����
		//	output.println(""+blockNum);
		//	other.output.println(""+blockNum);
			
			while(true){
				command = input.readLine();
							
				if(command == null){
					continue;
				}

				if (command.startsWith("NEW")) {				//Ŭ���̾�Ʈ���� ���� ���� ���� ��ȣ
					String Num = input.readLine();
					other.output.println("NEW,"+Num);
					//other.output.println(Num);
					
				}

				if(command.startsWith("MOVE")){					// Ŭ���̾�Ʈ���� Ű���带 ���� �� �ٸ� Ŭ���̾�Ʈ�� ����
					String KeyCode = input.readLine();					
					other.output.println("OTHER,"+KeyCode);
				//	other.output.println(KeyCode);
				}
				
				if(command.startsWith("DEL")){					// Ŭ���̾�Ʈ���� ������ �μ��� �˸�
					int num = (int)(Math.random()*10);
					output.println("OADD,"+num);
					other.output.println("ADD,"+num);
				}
				
				if(command.startsWith("QUIT")){
					return ;
				}
			}
		} catch (IOException e){
			System.out.println("������ ���������ϴ�.2");
		} finally{
			try{
				socket.close();
			} catch (IOException e){
			}
		}
	}
}