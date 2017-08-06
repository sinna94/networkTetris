import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class window extends JFrame implements KeyListener{

	private JPanel contentPane;
	public static Board board = new Board();

	public window() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 577);
		addKeyListener(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
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

		JPanel otherPanel = new JPanel();
		otherPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		otherPanel.setBounds(298, 276, 221, 252);
		contentPane.add(otherPanel);
		
		
		Thread t = new Thread(new DownThread());
	//	t.start();
		
		window.board.makeBlock();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		window.board.moveBlock(KeyCode);
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
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window frame = new window();
					frame.setVisible(true);
				} catch (Exception e) {	}
			}
		});
	}

	private class DownThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					System.out.println("쓰레드~~");
					Thread.sleep(2000);
					window.board.moveBlock(KeyEvent.VK_DOWN);
					repaint();
				}
			} catch (InterruptedException e) {		}

		}
	}
}





class gamePanel extends JPanel {
	public gamePanel() {
		
		this.setBackground(Color.GRAY);
		this.setBounds(12, 10, 274, 518);
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		
		if (Board.getTouchFloor()) {			// 블록이 내려갈 수 없을 때 새로운 블록을 만듬
			window.board.makeBlock();
			Board.setTouchFloor(false);
		}
		
		int boardX = getX();
		int boardY = getY();
		int boardW = getWidth();
		int boardH = getHeight();
		int blockW = (boardW - boardX) / 10;
		int blockH = (boardH - boardY) / 20;

		for (int i = 0; i < window.board.boardH; i++) {
			for (int j = 0; j < window.board.boardW; j++) {
				g.setColor(window.board.getBoard()[i][j]);
				g.fill3DRect(boardX + j * blockW, boardY + i * blockH, blockW, blockH, false);

			}
		}
		
		
	}
}
