package Client;
import java.io.IOException;

public class TetrisThread{
	private TetrisGame game;
	private LobbyUI lobby;
	private ServerAccess sa;
	
	public TetrisThread(LobbyUI lobby, ServerAccess sa, TetrisGame game) throws IOException {
		this.sa = sa;
		this.lobby = lobby;
		this.game = game;
	}
	
	public void go(){
		String response;
		
		try{
			//game.makeNewBlock();

			while (true) {
				response = sa.input.readLine();
				if (response.startsWith("OTHER")) { // 다른 클라이언트의 키 움직임
					int KeyCode = Integer.valueOf(response.split(",")[1]);
					game.otherBlockMove(KeyCode);
				}

				if (response.startsWith("NEW")) { // 다른 클라이언트 블록이 새로 생김
					int blockNum = Integer.valueOf(response.split(",")[1]);
					game.other.makeBlock(blockNum);
				}

				if (response.startsWith("OADD")) {
					int num = Integer.valueOf(response.split(",")[1]);
					game.other.makeLine(num);
				}

				if (response.startsWith("ADD")) { // 다른 클라이언트에서 라인을 부숨
					int num = Integer.valueOf(response.split(",")[1]);
					game.board.makeLine(num);
				}
				if (response.startsWith("QUIT")) {
					String win = response.split(",")[1];
					game.exit(win);
					lobby.endGame();
					break;
				}
				if (response.startsWith("ITEM")){
					int num = Integer.valueOf(response.split(",")[1]);
					switch(num){
					case 0:
						game.other.itemLineDel();
						break;
					case 1:
						for(int i =0;i<2;i++){
							game.other.itemLineDel();
						}
						break;
					case 2:
						break;
					case 3:
						break;
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("에러 " + e);
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			System.out.println("에러 " + e);
			e.printStackTrace();

		}
	}
	
	public static class Key{
		public static void moveBlock(int keyCode) { // 서버로 키 코드 전송
			ServerAccess.output.println("MOVE,"+ keyCode);
		}
		public static void newBlock(int blockNum) { // 서버로 블록 번호 전송
			ServerAccess.output.println("NEW," + blockNum);
		}
		public static void delLine(){
			ServerAccess.output.println("DEL");
		}
		public static void quitGame(){
			ServerAccess.output.println("QUIT");
		}
		public static void useItem(int num){
			ServerAccess.output.println("ITEM,"+num);
		}
	}
}
