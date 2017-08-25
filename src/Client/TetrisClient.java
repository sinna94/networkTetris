package Client;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TetrisClient extends Thread {

	private Socket socket;
	private BufferedReader input;
	private static PrintWriter output;
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private TetrisGame game;
	private LoadingUi loading;
	private boolean start = true;
	
	public TetrisClient() throws UnknownHostException, IOException{
		
		socket = new Socket("122.40.66.211", 9001);
		
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
	
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		
	}
	
	public void run(){
		String response;
		
		try{
			response = input.readLine();
			
			if(response.startsWith("LOADING")){
				System.out.println("로딩중");
				loading = new LoadingUi();
				loading.setVisible(true);
			}
			
			while(start){
				response = input.readLine();
				
				if(response.startsWith("READY")){			// 게임 준비
					game = new TetrisGame();
					game.setVisible(true);
					loading.dispose();
				}
				
				if(response.startsWith("GO")){				// 게임 시작
					game.makeNewBlock();
					//int blockNum = (int) (Math.random() * 7);
					//int blockNum = Integer.valueOf(response.split(",")[1]);
					//game.board.makeBlock(blockNum);
					//game.other.makeBlock(blockNum);
					//game.board.makeBlock(Integer.valueOf(input.readLine()));
					//game.other.makeBlock(Integer.valueOf(input.readLine()));
					
					Thread t = new Thread(game);					// 시간이 지날때 마다 한 칸씩 블록이 내려오는 쓰레드
					t.start();
					
					start = false;	
					break;
				}
			}
			
			while(true){
				response = input.readLine();
				
				if(response.startsWith("OTHER")){				// 다른 클라이언트의 키 움직임
					//int KeyCode = Integer.valueOf(input.readLine());
					int KeyCode = Integer.valueOf(response.split(",")[1]);
					game.otherBlockMove(KeyCode);
				}
									
				if(response.startsWith("NEW")){
					//int blockNum = Integer.valueOf(input.readLine());
					int blockNum = Integer.valueOf(response.split(",")[1]);
					game.other.makeBlock(blockNum);
				}
				
				try{
					Thread.sleep(100);
				} catch (InterruptedException e){
					System.out.println("에러 " + e);
					e.printStackTrace();
				}
			}
			
		} catch(IOException e){
			System.out.println("1에러 " + e);
			e.printStackTrace();
			
		} finally{
			try{
				socket.close();
			} catch (IOException e){
				System.out.println("2에러 " + e);
				e.printStackTrace();
			}
		}
	}
	
	public static class Key{
		public static void moveBlock(int keyCode) { // 서버로 키 코드 전송
			output.println("MOVE");
			output.println(keyCode);
		}

		public static void newBlock(int blockNum) { // 서버로 블록 번호 전송
			output.println("NEW");
			output.println(blockNum);
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TetrisClient client = new TetrisClient();
					client.start();
				} catch (Exception e) {	
					System.out.println("C에러 " + e);
				}
			}
		});
	}

}
