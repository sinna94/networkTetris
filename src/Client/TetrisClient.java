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
				System.out.println("로딩중");
				loading = new LoadingUi();
				loading.setVisible(true);
			}
			
			while(start){
				response = sv.input.readLine();
				
				if(response.startsWith("READY")){			// 게임 준비
					game = new TetrisGame();
					game.setVisible(true);
					loading.dispose();
				}
				
				if(response.startsWith("GO")){				// 게임 시작
					game.makeNewBlock();
					
					Thread t = new Thread(game);					// 시간이 지날때 마다 한 칸씩 블록이 내려오는 쓰레드
					t.start();
					
					start = false;	
					break;
				}
			}
			
			while(true){
				response = sv.input.readLine();
				
				if(response.startsWith("OTHER")){				// 다른 클라이언트의 키 움직임
					int KeyCode = Integer.valueOf(response.split(",")[1]);
					game.otherBlockMove(KeyCode);
				}
									
				if(response.startsWith("NEW")){					// 다른 클라이언트 블록이 새로 생김
					int blockNum = Integer.valueOf(response.split(",")[1]);
					game.other.makeBlock(blockNum);
				}
				
				if(response.startsWith("OADD")){
					int num = Integer.valueOf(response.split(",")[1]);
					game.other.makeLine(num);
				}
				
				if(response.startsWith("ADD")){					// 다른 클라이언트에서 라인을 부숨
					int num = Integer.valueOf(response.split(",")[1]);
					game.board.makeLine(num);
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
				sv.socket.close();
			} catch (IOException e){
				System.out.println("2에러 " + e);
				e.printStackTrace();
			}
		}
	}
	
	public static class Key{
		public static void moveBlock(int keyCode) { // 서버로 키 코드 전송
			ServerAccess.output.println("MOVE");
			ServerAccess.output.println(keyCode);
		}

		public static void newBlock(int blockNum) { // 서버로 블록 번호 전송
			ServerAccess.output.println("NEW");
			ServerAccess.output.println(blockNum);
		}
		public static void delLine(){
			ServerAccess.output.println("DEL");
		}
	}
}
