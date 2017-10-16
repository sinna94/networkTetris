package Client;
import java.io.IOException;

public class TetrisClient extends Thread {
	private TetrisGame game;
	private LoadingUi loading;
	private boolean start = true;
	private ServerAccess sv;
	
	public TetrisClient() throws IOException {
		sv = new ServerAccess();
	}
	
	public void run(){
		String response;
		
		try{
			response = sv.input.readLine();
			
			if(response.startsWith("LOADING")){
				System.out.println("�ε���");
				loading = new LoadingUi();
				loading.setVisible(true);
			}
			
			while(start){
				response = sv.input.readLine();
				
				if(response.startsWith("READY")){			// ���� �غ�
					game = new TetrisGame();
					game.setVisible(true);
					loading.dispose();
				}
				
				if(response.startsWith("GO")){				// ���� ����
					game.makeNewBlock();
					
					Thread t = new Thread(game);					// �ð��� ������ ���� �� ĭ�� ����� �������� ������
					t.start();
					
					start = false;	
					break;
				}
			}
			
			while(true){
				response = sv.input.readLine();
				
				if(response.startsWith("OTHER")){				// �ٸ� Ŭ���̾�Ʈ�� Ű ������
					int KeyCode = Integer.valueOf(response.split(",")[1]);
					game.otherBlockMove(KeyCode);
				}
									
				if(response.startsWith("NEW")){					// �ٸ� Ŭ���̾�Ʈ ����� ���� ����
					int blockNum = Integer.valueOf(response.split(",")[1]);
					game.other.makeBlock(blockNum);
				}
				
				if(response.startsWith("OADD")){
					int num = Integer.valueOf(response.split(",")[1]);
					game.other.makeLine(num);
				}
				
				if(response.startsWith("ADD")){					// �ٸ� Ŭ���̾�Ʈ���� ������ �μ�
					int num = Integer.valueOf(response.split(",")[1]);
					game.board.makeLine(num);
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
				sv.socket.close();
			} catch (IOException e){
				System.out.println("2���� " + e);
				e.printStackTrace();
			}
		}
	}
	
	public static class Key{
		public static void moveBlock(int keyCode) { // ������ Ű �ڵ� ����
			ServerAccess.output.println("MOVE");
			ServerAccess.output.println(keyCode);
		}

		public static void newBlock(int blockNum) { // ������ ��� ��ȣ ����
			ServerAccess.output.println("NEW");
			ServerAccess.output.println(blockNum);
		}
		public static void delLine(){
			ServerAccess.output.println("DEL");
		}
	}
}
