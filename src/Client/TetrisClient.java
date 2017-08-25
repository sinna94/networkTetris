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
				System.out.println("�ε���");
				loading = new LoadingUi();
				loading.setVisible(true);
			}
			
			while(start){
				response = input.readLine();
				
				if(response.startsWith("READY")){			// ���� �غ�
					game = new TetrisGame();
					game.setVisible(true);
					loading.dispose();
				}
				
				if(response.startsWith("GO")){				// ���� ����
					game.makeNewBlock();
					//int blockNum = (int) (Math.random() * 7);
					//int blockNum = Integer.valueOf(response.split(",")[1]);
					//game.board.makeBlock(blockNum);
					//game.other.makeBlock(blockNum);
					//game.board.makeBlock(Integer.valueOf(input.readLine()));
					//game.other.makeBlock(Integer.valueOf(input.readLine()));
					
					Thread t = new Thread(game);					// �ð��� ������ ���� �� ĭ�� ����� �������� ������
					t.start();
					
					start = false;	
					break;
				}
			}
			
			while(true){
				response = input.readLine();
				
				if(response.startsWith("OTHER")){				// �ٸ� Ŭ���̾�Ʈ�� Ű ������
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
					System.out.println("���� " + e);
					e.printStackTrace();
				}
			}
			
		} catch(IOException e){
			System.out.println("1���� " + e);
			e.printStackTrace();
			
		} finally{
			try{
				socket.close();
			} catch (IOException e){
				System.out.println("2���� " + e);
				e.printStackTrace();
			}
		}
	}
	
	public static class Key{
		public static void moveBlock(int keyCode) { // ������ Ű �ڵ� ����
			output.println("MOVE");
			output.println(keyCode);
		}

		public static void newBlock(int blockNum) { // ������ ��� ��ȣ ����
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
					System.out.println("C���� " + e);
				}
			}
		});
	}

}
