import java.awt.Color;
import java.awt.event.KeyEvent;

public class Board{

	final int boardW = 10;
	final int boardH = 20;

	private Color[][] board = new Color[boardH][boardW];
	private static int[][] currentBlockLocation = new int[4][2];
	private static Color currentBlockColor = null;
	private Color initBoard = Color.GRAY;
	private static boolean touchFloor = false;
	private static Block block = null;
	
	public Board() {
		for (int i = 0; i < boardH; i++) {					// 보드 초기화
			for (int j = 0; j < boardW; j++) {
				board[i][j] = initBoard;
			}
		}
	}

	public Color[][] getBoard() {
		return board;
	}

	public void makeBlock() { 								// 블록 생성
		int blockNum = (int) (Math.random() * 7);
		int[][] location = new int[4][2];

		switch (blockNum) {
		case 0:
			block = new O_Block();
			break;
		case 1:
			block = new I_Block();
			break;
		case 2:
			block = new S_Block();
			break;
		case 3:
			block = new Z_Block();
			break;
		case 4:
			block = new L_Block();
			break;
		case 5:
			block = new J_Block();
			break;
		case 6:
			block = new T_Block();
			break;
		default:
			break;
		}

		location = block.getBlock();
		setCurrentBlockColor(block.getColor());
		addBlock(location, getCurrentBlockColor());
	}

	public void addBlock(int[][] location, Color color) { 	// 보드에 블록 추가
		int[][] XY = new int[4][2];
		for (int i = 0; i < location.length; i++) {
			XY[i][0] = -1*location[i][1];					// y축
			XY[i][1] = ((location[i][0]) + (boardW / 2));	// x축
		}
		for (int i = 0; i < XY.length; i++) {
			board[XY[i][0]][XY[i][1]] = color;
			currentBlockLocation[i][0] = XY[i][0];
			currentBlockLocation[i][1] = XY[i][1];
		}
	}
	
	public void addBlock(int[][] location){
		int[][] XY = new int[4][2];
		int x = currentBlockLocation[2][1];						//(0, 0) 자리임 
		int y = currentBlockLocation[2][0];
		
		for (int i = 0; i < XY.length; i++) {

			System.out.println(location[i][0] + ", " + location[i][1] );
			board[location[i][0] + y][location[i][1] + x] = getCurrentBlockColor();
			currentBlockLocation[i][0] = location[i][0] + y;
			currentBlockLocation[i][1] = location[i][1] + x;
		}
	}

	public void moveBlock(int KeyCode) {		
		boolean[] moveOk = new boolean[4];
		setCurrentBlockColor(this.board[currentBlockLocation[0][0]][currentBlockLocation[0][1]]);
		
		try {
			switch (KeyCode) {
			case KeyEvent.VK_DOWN:												// 아래 방향키
				currentBlockInit();
				moveOk = checkBlockDown();
				BlockDown(moveOk);
				break;
				
			case KeyEvent.VK_UP:
				currentBlockInit();
				if(checkRotation(block.rotation())){
					System.out.println("회전");
				}
				else{
					returnBlock();
				}
				break;
				
			case KeyEvent.VK_LEFT:
				currentBlockInit();
				moveOk = checkBlockLeft();
				BlockLeft(moveOk);
				break;
				
			case KeyEvent.VK_RIGHT:
				currentBlockInit();
				moveOk = checkBlockRight();
				BlockRight(moveOk);
				break;
				
			case KeyEvent.VK_SPACE:

				break;
				
			default:
				break;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			for (int i = 0; i < currentBlockLocation.length; i++) {
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		}
	}

	public void currentBlockInit(){
		for (int i = 0; i < currentBlockLocation.length; i++) {													// 현재 블록 위치 초기화
			this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = initBoard;
		}
	}
	
	public void returnBlock(){
		for (int i = 0; i < currentBlockLocation.length; i++) {													// 현재 블록 위치 복구
			this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
		}
	}
	
	public boolean checkRotation(int[][] location){
		int x = currentBlockLocation[2][1];						//(0, 0) 자리임 
		int y = currentBlockLocation[2][0];
		
		try {
			for (int i = 0; i < location.length; i++) {
				System.out.println(location[i][0] + ", " + location[i][1] );
				if (board[location[i][0] + y][location[i][1] + x] != initBoard) {
					System.out.println("실패");
					return false;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("초과");
			return false;
		}

		addBlock(location);
		return true;
	}
	
	public boolean[] checkBlockDown(){
		boolean[] moveOk = new boolean[4];
		for (int i = 0; i < currentBlockLocation.length; i++) {													// 아래 칸으로 이동 가능한지 확인
				
			if (this.board[currentBlockLocation[i][0] + 1][currentBlockLocation[i][1]] == initBoard) {
				moveOk[i] = true;
			} else {
				moveOk[i] = false;
				setTouchFloor(true);
			}
		}
		return moveOk;
	}
	
	/* 
	public void checkTouch(int RL){
		for (int i = 0; i < currentBlockLocation.length; i++) {													
			System.out.println(this.board[currentBlockLocation[i][0] + 1][currentBlockLocation[i][1] + RL]);
			if (this.board[currentBlockLocation[i][0] + 1][currentBlockLocation[i][1]] != initBoard) {
				setTouchFloor(true);
			}
		}
	}
	*/
	public void BlockDown(boolean[] moveOk){
		if (isFalse(moveOk)) {																					// 블록 이동 가능시 이동, 불가능시 복원
			for (int i = 0; i < currentBlockLocation.length; i++) {
				currentBlockLocation[i][0]++;
				
				if (currentBlockLocation[i][0] == boardH - 1) {													// 블록이 바닥에 닿았는지 확인
					setTouchFloor(true);
					//setCurrentBlockColor(Color.WHITE);
				}
				
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		} else

		{
			for (int i = 0; i < currentBlockLocation.length; i++) {
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		}
	}
	
	public boolean isFalse(boolean[] ck) {
		for (int i = 0; i < ck.length; i++) {
			if (ck[i] == false) {
				return false;
			}
		}
		return true;
	}

	public boolean[] checkBlockLeft(){
		boolean[] moveOk = new boolean[4];
		for (int i = 0; i < currentBlockLocation.length; i++) {													// 왼쪽 칸으로 이동 가능한지 확인
			if (this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1] - 1] != initBoard) {
				moveOk[i] = false;
			} else {
				moveOk[i] = true;
			}
		}
		return moveOk;
	}
	
	public void BlockLeft(boolean[] moveOk){
		if (isFalse(moveOk)) {																					// 블록 이동 가능시 이동, 불가능시 복원
			for (int i = 0; i < currentBlockLocation.length; i++) {
				currentBlockLocation[i][1]--;
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();

			}
		} else {
			for (int i = 0; i < currentBlockLocation.length; i++) {
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		}
	}
	
	public boolean[] checkBlockRight(){
		boolean[] moveOk = new boolean[4];
		for (int i = 0; i < currentBlockLocation.length; i++) {													// 오른쪽 칸으로 이동 가능한지 확인
			if (this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1] + 1] != initBoard) {
				moveOk[i] = false;
			} else {
				moveOk[i] = true;
			}
		}
		return moveOk;
	}
	
	public void BlockRight(boolean[] moveOk){
		if (isFalse(moveOk)) {																					// 블록 이동 가능시 이동, 불가능시 복원
			for (int i = 0; i < currentBlockLocation.length; i++) {
				currentBlockLocation[i][1]++;
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();

			}
		} else {
			for (int i = 0; i < currentBlockLocation.length; i++) {
				this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		}
	}
	
	public static Color getCurrentBlockColor() {
		return currentBlockColor;
	}

	public static void setCurrentBlockColor(Color currentBlockColor) {
		Board.currentBlockColor = currentBlockColor;
	}

	public static boolean getTouchFloor() {
		return touchFloor;
	}

	public static void setTouchFloor(boolean touchFloor) {
		Board.touchFloor = touchFloor;
	}
}



