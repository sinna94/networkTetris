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
	private PrintWriter output;
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private TetrisGame game;
	private LoadingUi loading;
	private boolean start = true;
	
	public TetrisClient() throws UnknownHostException, IOException{
		
		socket = new Socket("localhost", 9001);
		
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
				
				if(response.startsWith("START")){			// ���� ����
					game = new TetrisGame();
					game.setVisible(true);
					loading.dispose();
					start = false;				
					
					output.println("BLOCK");				// ���� ��� ����
					oos.writeObject(TetrisGame.board.getCurrentBlock());
					
					break;
				}
			}
			
			while(true){
				
				response = input.readLine();
				
				if(response.startsWith("BLOCK")){					// ���� ��ϻ� ����
					Block otherBlock = (Block) ois.readObject();
					TetrisGame.other.addBlock(otherBlock.getLocation(), otherBlock.getColor());
				}
				
				if(game.isMoveBlock() == true){						// ����� �������� ��� �� ���� �̹����� ������ ����
					System.out.println("����");
					output.println("MOVE");
					
					game.setMoveBlock(false);
				}
				
				if(response.startsWith("OTHER")){		// ���� ���� �̹����� �������� ���۵�
					System.out.println("����");
					
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
			System.out.println("1���� " + e);
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			System.out.println("���� " + e);
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
