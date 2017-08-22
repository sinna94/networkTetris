import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class TetrisGame extends JFrame implements KeyListener{

	private JPanel contentPane;
	public static Board board = new Board();
	public static Board other = new Board();
	private boolean moveBlock = false;
	private Graphics boardImage;
	private Image otherPlay = null;
	private JPanel otherPanel;
	
	public TetrisGame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 577);
		addKeyListener(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		otherPanel = new JPanel();
		otherPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		otherPanel.setBounds(298, 276, 221, 252);
		contentPane.add(otherPanel);
		
		gamePanel gamePanel = new gamePanel();
		contentPane.add(gamePanel);

		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		scorePanel.setBounds(298, 10, 221, 59);
		contentPane.add(scorePanel);
		scorePanel.setLayout(new BorderLayout(0, 0));

		JLabel lblScore = new JLabel("SCORE");
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		scorePanel.add(lblScore, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scorePanel.add(lblNewLabel, BorderLayout.CENTER);

		JPanel blockPanel = new JPanel();
		blockPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		blockPanel.setBounds(298, 76, 221, 190);
		contentPane.add(blockPanel);

		
		
		
		Thread t = new Thread(new DownThread());					// 시간이 지날때 마다 한 칸씩 블록이 내려오는 쓰레드
		t.start();
		
		TetrisGame.board.makeBlock();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {		
		int KeyCode = e.getKeyCode();
		TetrisGame.board.moveBlock(KeyCode);
		setMoveBlock(true);
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean isMoveBlock() {
		return moveBlock;
	}

	public void setMoveBlock(boolean moveBlock) {
		this.moveBlock = moveBlock;
	}

	
	public Image getOtherPlay() {
		return otherPlay;
	}

	public void setOtherPlay(Image otherPlay) {
		this.otherPlay = otherPlay;
	}
	
	public Graphics getBoardImage() {
		return boardImage;
	}

	public void setBoardImage(Graphics boardImage) {
		this.boardImage = boardImage;
	}

	private int[] otherPanelSize(){
		int[] size = {298, 276, 221, 252};
		
		return size;
	}
	
		
	private class DownThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(1000);
					TetrisGame.board.moveBlock(KeyEvent.VK_DOWN);
					setMoveBlock(true);
					repaint();
				}
			} catch (InterruptedException e) {		}

		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		int boardX = getX();
		int boardY = getY();
		int boardW = getWidth();
		int boardH = getHeight();
		int blockW = (boardW - boardX) / 10;
		int blockH = (boardH - boardY) / 20;
		
		int OboardX = otherPanel.getX();
		int OboardY = otherPanel.getY();
		int OboardW = otherPanel.getWidth();
		int OboardH = otherPanel.getHeight();
		int OblockW = (OboardW - OboardX) / 10;
		int OblockH = (OboardH - OboardY) / 20;
		
		if (TetrisGame.board.getTouchFloor()) { // 블록이 내려갈 수 없을 때 새로운 블록을 만듬
			board.makeBlock();
			TetrisGame.board.setTouchFloor(false);

		}

		if (TetrisGame.board.gameover) {
			for (int i = 0; i < TetrisGame.board.boardH; i++) {
				for (int j = 0; j < TetrisGame.board.boardW; j++) {
					g.setColor(Color.BLACK);
					g.fill3DRect(boardX + j * blockW, boardY + i * blockH, blockW, blockH, false);
				}
			}
			JOptionPane.showMessageDialog(null, "GameOver");
		}

		else {
			for (int i = 0; i < TetrisGame.board.boardH; i++) {
				for (int j = 0; j < TetrisGame.board.boardW; j++) {
					System.out.println("그리는 중");
					g.setColor(TetrisGame.board.getBoard()[i][j]);
					g.fill3DRect(boardX + j * blockW, boardY + i * blockH, blockW, blockH, false);			// 내 보드
					g.setColor(TetrisGame.other.getBoard()[i][j]);
					g.fill3DRect(OboardX + j * OblockW, OboardY + i * OblockH, OblockW, OblockH, false);	// 상대 보드
				}
			}
		}
		
	}
	
	class gamePanel extends JPanel {
		
		
		public gamePanel() {

			this.setBounds(12, 10, 274, 518);
		}

	}
}
