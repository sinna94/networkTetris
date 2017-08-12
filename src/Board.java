import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Board{

	final int boardW = 10;
	final int boardH = 20;

	private Color[][] board = new Color[boardH][boardW];
	private static int[][] currentBlockLocation = new int[4][2];
	private static Color currentBlockColor = null;
	private static Block currentBlock;
	private Color initBoard = Color.GRAY;
	private static boolean touchFloor = false;
	private static Block block = null;
	private int topLine;
	private int fullLine;
	public boolean gameover = false;
	
	public Board() {
		for (int i = 0; i < boardH; i++) {					// ���� �ʱ�ȭ
			for (int j = 0; j < boardW; j++) {
				board[i][j] = initBoard;
			}
		}
	}

	public Color[][] getBoard() {
		return board;
	}

	// ��� ����, ���忡 �߰�
	
	public void makeBlock() { 								// ��� ����
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
		block.rotation();								// ó�� ȸ�� ������ �� ������ ����
	}

	public void addBlock(int[][] location, Color color) { 	// ���忡 ��� �߰�	, ���߿� Ʈ���� ĳġ �Ἥ ��� ���� �ڸ� ������ Ȯ��
		int[][] XY = new int[4][2];
		
		fullLine();
		
		for (int i = 0; i < location.length; i++) {
			XY[i][0] = -1*location[i][1];					// y��
			XY[i][1] = ((location[i][0]) + (boardW / 2));	// x��
		}
		
		for (int i = 0; i < XY.length; i++) {
			if(board[XY[i][0]][XY[i][1]] == initBoard){
			board[XY[i][0]][XY[i][1]] = color;
			currentBlockLocation[i][0] = XY[i][0];
			currentBlockLocation[i][1] = XY[i][1];
			}
			else{
				gameover = true;
				break;
			}
		}
	}
	
	public void addBlock(int[][] location){
		int[][] XY = new int[4][2];
		int x = currentBlockLocation[2][1];						//(0, 0) �ڸ��� 
		int y = currentBlockLocation[2][0];
		
		for (int i = 0; i < XY.length; i++) {
			board[location[i][0] + y][location[i][1] + x] = getCurrentBlockColor();
			currentBlockLocation[i][0] = location[i][0] + y;
			currentBlockLocation[i][1] = location[i][1] + x;
		}
	}

	// ��� �̵�, ȸ�� ����
	
	public void moveBlock(int KeyCode) {		
		boolean[] moveOk = new boolean[4];
		setCurrentBlockColor(this.board[currentBlockLocation[0][0]][currentBlockLocation[0][1]]);
		
		try {
			switch (KeyCode) {
			case KeyEvent.VK_DOWN:												// �Ʒ� ����Ű
				currentBlockInit();
				moveOk = checkBlockDown(board);
				BlockDown(board, moveOk);
				top();
				break;
				
			case KeyEvent.VK_UP:
				currentBlockInit();
				currentBlock = (Block) block.clone();
				if(checkRotation(currentBlock.rotation())){
					addBlock(block.rotation());
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
				while(getTouchFloor() == false){
					currentBlockInit();
					moveOk = checkBlockDown(board);
					BlockDown(board, moveOk);
				}
				top();
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
		for (int i = 0; i < currentBlockLocation.length; i++) {													// ���� ��� ��ġ �ʱ�ȭ
			this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = initBoard;
		}
	}
	
	public void returnBlock(){
		for (int i = 0; i < currentBlockLocation.length; i++) {													// ���� ��� ��ġ ����
			this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
		}
	}
	
	public boolean checkRotation(int[][] location){
		int x = currentBlockLocation[2][1];						//(0, 0) �ڸ��� 
		int y = currentBlockLocation[2][0];
		
		try {
			for (int i = 0; i < location.length; i++) {
				if (board[location[i][0] + y][location[i][1] + x] != initBoard) {
					return false;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}

		
		return true;
	}
	
	public boolean[] checkBlockDown(Color[][] board){
		boolean[] moveOk = new boolean[4];
		for (int i = 0; i < currentBlockLocation.length; i++) {													// �Ʒ� ĭ���� �̵� �������� Ȯ��
				
			if (board[currentBlockLocation[i][0] + 1][currentBlockLocation[i][1]] == initBoard) {
				moveOk[i] = true;
			} else {																				// �ٸ� �� ����� ���� ���
				moveOk[i] = false;
				setTouchFloor(true);
			}
		}
		return moveOk;
	}
	
	public void BlockDown(Color[][] board, boolean[] moveOk){
		if (isFalse(moveOk)) {																					// ��� �̵� ���ɽ� �̵�, �Ұ��ɽ� ����
			for (int i = 0; i < currentBlockLocation.length; i++) {
				currentBlockLocation[i][0]++;
				
				if (currentBlockLocation[i][0] == boardH - 1) {													// ����� �ٴڿ� ��Ҵ��� Ȯ��
					setTouchFloor(true);
					//setCurrentBlockColor(Color.WHITE);
				}
				
				board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		} else

		{
			for (int i = 0; i < currentBlockLocation.length; i++) {
				board[currentBlockLocation[i][0]][currentBlockLocation[i][1]] = getCurrentBlockColor();
			}
		}
	}
	
	public boolean[] checkBlockLeft(){
		boolean[] moveOk = new boolean[4];
		for (int i = 0; i < currentBlockLocation.length; i++) {													// ���� ĭ���� �̵� �������� Ȯ��
			if (this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1] - 1] != initBoard) {
				moveOk[i] = false;
			} else {
				moveOk[i] = true;
			}
		}
		return moveOk;
	}
	
	public void BlockLeft(boolean[] moveOk){
		if (isFalse(moveOk)) {																					// ��� �̵� ���ɽ� �̵�, �Ұ��ɽ� ����
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
		for (int i = 0; i < currentBlockLocation.length; i++) {													// ������ ĭ���� �̵� �������� Ȯ��
			if (this.board[currentBlockLocation[i][0]][currentBlockLocation[i][1] + 1] != initBoard) {
				moveOk[i] = false;
			} else {
				moveOk[i] = true;
			}
		}
		return moveOk;
	}
	
	public void BlockRight(boolean[] moveOk){
		if (isFalse(moveOk)) {																					// ��� �̵� ���ɽ� �̵�, �Ұ��ɽ� ����
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
	
	// ���� �ý��� ���� �޼ҵ�
	
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
	
	public boolean isFalse(boolean[] ck) {						// �迭 �ȿ� false �� ������ false �����ϴ� �޼ҵ�
		for (int i = 0; i < ck.length; i++) {
			if (ck[i] == false) {
				return false;
			}
		}
		return true;
	}

	public void top(){											// ���� ����� ���� ���� ã�´�
		for (int i = boardH - 1; i > 0; i--) {
			if(isBlock(board[i])){
				topLine = boardH - i;
			}
			
			if(isFull(board[i])){
				fullLine = i;
				topLine--;
			}
		}
	}
	
	public boolean isBlock(Color[] line){						// �� �ٿ� ����� �ִ��� �˻��Ѵ�.
		for (int i = 0; i < line.length; i++) {
			if (line[i] != initBoard) {
				return true;
			}
		}
		return false;
	}

	public boolean isFull(Color[]line){							// �� ���� ������� ���� ���ִ��� �˻��Ѵ�.
		for (int i = 0; i < line.length; i++) {
			if (line[i] == initBoard) {
				return false;
			}
		}
		return true;
	}
	
	public void delLine(int num){								// ������ �����Ѵ�.
		for(int i = 0; i < boardW ; i++){
			board[num][i] = initBoard;
		}
	}
	
	public void downLine(int num){								// topLine ������ ���� �� ������.
		for(int i = num; i >= boardH - topLine ; i--){
			board[i] = board[i-1].clone(); 
		}
	}

	public void fullLine(){										// �� �� ������ ������� �۵��Ѵ�.
		for (int i = boardH - 1; i >= 0; i--) {			
			if(isFull(board[i])){
				fullLine = i;
				delLine(fullLine);
				downLine(fullLine);
				topLine++;
			}
		}
	}
	
	
}
