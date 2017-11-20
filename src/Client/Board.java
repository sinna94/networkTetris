package Client;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Board{

	final int boardW = 10;
	final int boardH = 20;

	public boolean other;
	
	private Color[][] board = new Color[boardH][boardW];
	private int[][] currentBlockLocation = new int[4][2];
	private Color currentBlockColor = null;
	private Block currentBlock;
	private Color initBoard = Color.GRAY;
	private boolean touchFloor = false;
	private  Block block = null;
	private int topLine;
	private int fullLine;
	public boolean gameover = false;
	
	public Board(boolean other) {
		for (int i = 0; i < boardH; i++) {					// 보드 초기화
			for (int j = 0; j < boardW; j++) {
				board[i][j] = initBoard;
			}
		}
		this.other = other;
	}

	public Color[][] getBoard() {
		return board;
	}

	// 블록 생성, 보드에 추가
	
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
		block.rotation();								// 처음 회전 씹히는 것 때문에 넣음
	}
	
	public void makeBlock(int blockNum) { 								// 블록 생성
	//	int blockNum = (int) (Math.random() * 7);
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
		block.rotation();								// 처음 회전 씹히는 것 때문에 넣음
	}
	
	public void addBlock(int[][] location, Color color) { 	// 보드에 블록 추가
		int[][] XY = new int[4][2];
		
		fullLine();
		
		for (int i = 0; i < location.length; i++) {
			XY[i][0] = -1*location[i][1];					// y축
			XY[i][1] = ((location[i][0]) + (boardW / 2));	// x축
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
		int x = currentBlockLocation[2][1];						//(0, 0) 자리임 
		int y = currentBlockLocation[2][0];
		
		for (int i = 0; i < XY.length; i++) {
			board[location[i][0] + y][location[i][1] + x] = getCurrentBlockColor();
			currentBlockLocation[i][0] = location[i][0] + y;
			currentBlockLocation[i][1] = location[i][1] + x;
		}
	}

	// 블록 이동, 회전 관련
	
	public void moveBlock(int KeyCode) {		
		boolean[] moveOk = new boolean[4];
		setCurrentBlockColor(this.board[currentBlockLocation[0][0]][currentBlockLocation[0][1]]);
		try {
			switch (KeyCode) {
			case KeyEvent.VK_DOWN:												// 아래 방향키
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
		for (int i = 0; i < currentBlockLocation.length; i++) {													// 아래 칸으로 이동 가능한지 확인
				
			if (board[currentBlockLocation[i][0] + 1][currentBlockLocation[i][1]] == initBoard) {
				moveOk[i] = true;
			} else {																				// 다른 색 블록이 있을 경우
				moveOk[i] = false;
				setTouchFloor(true);
			}
		}
		return moveOk;
	}
	
	public void BlockDown(Color[][] board, boolean[] moveOk){
		if (isFalse(moveOk)) {																					// 블록 이동 가능시 이동, 불가능시 복원
			for (int i = 0; i < currentBlockLocation.length; i++) {
				currentBlockLocation[i][0]++;
				
				if (currentBlockLocation[i][0] == boardH - 1) {													// 블록이 바닥에 닿았는지 확인
					setTouchFloor(true);
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
	
	// 게임 시스템 관련 메소드
	
	public Block getCurrentBlock() {
		return currentBlock;
	}
	
	public Color getCurrentBlockColor() {
		return currentBlockColor;
	}

	public void setCurrentBlockColor(Color currentBlockColor) {
		this.currentBlockColor = currentBlockColor;
	}

	public boolean getTouchFloor() {
		return isTouchFloor();
	}

	public void setTouchFloor(boolean touchFloor) {
		this.touchFloor = touchFloor;
	}
	
	public boolean isFalse(boolean[] ck) {						// 배열 안에 false 가 있으면 false 리턴하는 메소드
		for (int i = 0; i < ck.length; i++) {
			if (ck[i] == false) {
				return false;
			}
		}
		return true;
	}

	public void top(){											// 쌓인 블록의 가장 위를 찾는다
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
	
	public boolean isBlock(Color[] line){						// 한 줄에 블록이 있는지 검사한다.
		for (int i = 0; i < line.length; i++) {
			if (line[i] != initBoard) {
				return true;
			}
		}
		return false;
	}

	public boolean isFull(Color[]line){							// 한 줄이 블록으로 가득 차있는지 검사한다.
		for (int i = 0; i < line.length; i++) {
			if (line[i] == initBoard) {
				return false;
			}
		}
		return true;
	}
	
	public void delLine(int num){								// 한줄을 삭제한다.
		for(int i = 0; i < boardW ; i++){
			board[num][i] = initBoard;
		}
		if(!other)
			TetrisThread.Key.delLine();
	}
	
	public void downLine(int num){								// topLine 밑으로 한줄 씩 내린다.
		for(int i = num; i >= boardH - topLine ; i--){
			board[i] = board[i-1].clone(); 
		}
	}

	public void fullLine(){										// 꽉 찬 라인을 대상으로 작동한다.
		for (int i = 0; i <= boardH - 1; i++) {		
			if(isFull(board[i]) == true){
				fullLine = i;
				delLine(fullLine);
				downLine(fullLine);
				topLine++;
			}
		}
	}

	public boolean isTouchFloor() {
		return touchFloor;
	}
	
	public void upLine(){
		currentBlockInit();
		for(int i = boardH - topLine; i < boardH - 1  ; i++){
			board[i] = board[i+1].clone(); 
		}
		
		for(int i = 0;i <boardW;i++){
			board[boardH-1][i] = initBoard;
		}
		addBlock(block.getLocation());
		topLine++;
	}
	
	public void makeLine(int num){
		upLine();
				
		for(int i = 0;i <boardW;i++){
			if(i != num){
				board[boardH-1][i] = Color.DARK_GRAY;
			}
		}
	}
}
