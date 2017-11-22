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
	private Thread t;
	private boolean run = true;
	private JLabel itemName;
	
	public TetrisGame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 577);
		addKeyListener(this);
		setVisible(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel itemPanel = new JPanel();
		itemPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		itemPanel.setBounds(298, 10, 232, 59);
		contentPane.add(itemPanel);
		itemPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblItem = new JLabel("ITEM");
		lblItem.setHorizontalAlignment(SwingConstants.CENTER);
		itemPanel.add(lblItem, BorderLayout.NORTH);

		itemName = new JLabel("");
		itemName.setHorizontalAlignment(SwingConstants.CENTER);
		itemPanel.add(itemName, BorderLayout.CENTER);
		
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
		otherPanel.setBounds(300, 79, 230, 451);
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
					
					int num = (int) (Math.random() * 20);
					if(num < 4 && itemName.getText().equals("") == true){
						board.setItem(num);
						switch(num){
						case 0:
							itemName.setText("1줄 삭제");
							break;
						case 1:
							itemName.setText("2줄 삭제");
							break;
						case 2:
							itemName.setText("1줄 추가");
							break;
						case 3:
							itemName.setText("커트");
							break;
						}
						itemName.setText("");
					}
				}
				
				if (board.gameover) {
					TetrisThread.Key.quitGame();
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
		
		t = new Thread(this);
		t.start();
		blockMove(KeyEvent.VK_SPACE);
	} 
	
	public void makeNewBlock(){
		int blockNum = (int) (Math.random() * 7);
		TetrisThread.Key.newBlock(blockNum);
		board.makeBlock(blockNum);
	}
	
	public void exit(String win){
		run = false;
		if(win.equals("win") == true)
			JOptionPane.showMessageDialog(null, "승리했습니다.");
		else
			JOptionPane.showMessageDialog(null, "패배했습니다.");
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
		TetrisThread.Key.moveBlock(KeyCode);
		repaint();
	}
	
	public void otherBlockMove(int KeyCode){
		other.moveBlock(KeyCode);
		repaint(); 
	}

	public void run() {
		try {
			while (run) {
				Thread.sleep(1000);
				blockMove(KeyEvent.VK_DOWN);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
