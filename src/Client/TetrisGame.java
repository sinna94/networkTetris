package Client;
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

public class TetrisGame extends JFrame implements KeyListener, Runnable{

	private JPanel contentPane;
	public Board board = new Board(false);
	public Board other = new Board(true);
	private JPanel otherPanel;
	private JPanel gamePanel;
	
	
	public TetrisGame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 577);
		addKeyListener(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

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
		blockPanel.setBounds(298, 76, 221, 94);
		contentPane.add(blockPanel);
		
		otherPanel = new JPanel(){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				int boardW = this.getWidth();
				int boardH = this.getHeight();
				int blockW = boardW / 10;
				int blockH = boardH / 20;
			
				if (other.getTouchFloor()) { // 블록이 내려갈 수 없을 때 새로운 블록을 만듬
					other.setTouchFloor(false);
				}
				
				for (int i = 0; i < other.boardH; i++) {
					for (int j = 0; j < other.boardW; j++) {
						g.setColor(other.getBoard()[i][j]);
						g.fill3DRect(j * blockW, i * blockH, blockW, blockH, false); // 상대보드
					}
				}

			}
		};
		
		otherPanel.setBackground(Color.LIGHT_GRAY);
		
		otherPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		otherPanel.setBounds(300, 210, 230, 320);
		contentPane.add(otherPanel);
		
		gamePanel = new JPanel(){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				int boardW = this.getWidth();
				int boardH = this.getHeight();
				int blockW = boardW / 10;
				int blockH = boardH / 20;
							
				if (board.getTouchFloor()) { // 블록이 내려갈 수 없을 때 새로운 블록을 만듬
					makeNewBlock();
					board.setTouchFloor(false);
				}
				
				if (board.gameover) {
					JOptionPane.showMessageDialog(null, "GameOver");
					exit();
				}

				else {
					for (int i = 0; i < board.boardH; i++) {
						for (int j = 0; j < board.boardW; j++) {
							g.setColor(board.getBoard()[i][j]);
							g.fill3DRect(j * blockW, i * blockH, blockW, blockH, false);			// 내 보드
						}
					}
				}
				
			}
		};
		gamePanel.setBackground(Color.GRAY);
		gamePanel.setBounds(12, 10, 270, 520);
		contentPane.add(gamePanel);
		
	//	board.makeBlock();
	}
	
	public void makeNewBlock(){
		int blockNum = (int) (Math.random() * 7);
		TetrisClient.Key.newBlock(blockNum);
		board.makeBlock(blockNum);
	}
	
	public void exit(){
		this.dispose();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {	
		int KeyCode = e.getKeyCode();
		blockMove(KeyCode);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void blockMove(int KeyCode){
		board.moveBlock(KeyCode);
		TetrisClient.Key.moveBlock(KeyCode);
		repaint();
	}
	
	public void otherBlockMove(int KeyCode){
		other.moveBlock(KeyCode);
		repaint(); 
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				blockMove(KeyEvent.VK_DOWN);
			}
		} catch (InterruptedException e) {
		}

	}

}
