import java.awt.EventQueue;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TetrisClient extends Thread {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private TetrisGame game;
	private LoadingUi loading;
	private boolean start = true;
	
	public TetrisClient() throws UnknownHostException, IOException{
		
		socket = new Socket("localhost", 9001);
		
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		
	}
	
	public void run(){
		Object response;
		
		try{
			response = input.readLine();
			
			if(((String) response).startsWith("LOADING")){
				System.out.println("�ε���");
				loading = new LoadingUi();
				loading.setVisible(true);
			}
			
			while(start){
				response = input.readLine();
				
				if(((String) response).startsWith("START")){
					game = new TetrisGame();
					game.setVisible(true);
					loading.dispose();
					start = false;
					
					output.println("MOVE");
					output.println(game.getBoardImage()); 
					
					break;
				}
			}
			
			while(true){
				
				response = input.readLine();
				
				if(game.isMoveBlock() == true){						// ����� �������� ��� �� ���� �̹����� ������ ����
					System.out.println("����");
					output.println("MOVE");
					output.println(game.getBoardImage());
					game.setMoveBlock(false);
				}
				
				if(((String) response).startsWith("OTHER")){		// ���� ���� �̹����� �������� ���۵�
					System.out.println("����");
					response = input.readLine();
					game.setOtherPlay((Image) response);
				}
				System.out.println("����3");
				try{
					System.out.println("����4");
					Thread.sleep(200);
				} catch (InterruptedException e){
					System.out.println("����5");
					System.out.println("���� " + e);
					e.printStackTrace();
				}
			}
			
		} catch(IOException e){
			System.out.println("���� " + e);
			e.printStackTrace();
			
		} finally{
			try{
				socket.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TetrisClient client = new TetrisClient();
					client.start();
				} catch (Exception e) {	}
			}
		});
	}

}
