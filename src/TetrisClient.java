import java.awt.EventQueue;
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
	
	public TetrisClient() throws UnknownHostException, IOException{
		
		socket = new Socket("localhost", 9001);
		
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		
		
	}
	
	public void run(){
		String response;
		
		try{
			response = input.readLine();
			
			if(response.startsWith("LOADING")){
				System.out.println("로딩중");
				LoadingUi loading = new LoadingUi();
				loading.setVisible(true);
			}
			
			while(true){
				response = input.readLine();
				
				if(response.startsWith("START")){
					TetrisGame game = new TetrisGame();
					game.setVisible(true);
				}
				
				if(response.startsWith("OTHER")){
					
				}
				
				try{
					Thread.sleep(200);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			
		} catch(IOException e){
			System.out.println("에러 " + e);
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
