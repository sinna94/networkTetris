package Client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class LobbyUI extends JFrame implements ActionListener, KeyListener {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea chatArea;
	private JButton btnGameStart;
	private JButton btnExit;
	private ServerAccess sv;
	public LobbyUI(ServerAccess sv) {
		
		this.sv = sv;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 643, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel chatPanel = new JPanel();
		chatPanel.setBounds(12, 10, 603, 314);
		contentPane.add(chatPanel);
		chatPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(0, 293, 603, 21);
		chatPanel.add(textField);
		textField.setColumns(10);
		textField.addKeyListener(this);
		
		chatArea = new JTextArea();
		chatArea.setText("로그인 되었습니다.");
		chatArea.setEditable(false);
		chatArea.setForeground(Color.BLACK);
		//chatArea.setBounds(0, 0, 603, 289);
		sv.setTextArea(chatArea);
		
		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setLocation(0, 0);
		scrollPane.setSize(603, 283);
		chatPanel.add(scrollPane);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(12, 334, 603, 41);
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		btnGameStart = new JButton("Game Start");
		btnGameStart.addActionListener(this);
		buttonPanel.add(btnGameStart);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(this);
		buttonPanel.add(btnExit);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnGameStart){
		}
		else if(e.getSource() == btnExit){
			dispose();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			sv.sendMessage(textField.getText());	
			textField.setText("");
		}
			
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
