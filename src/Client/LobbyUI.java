package Client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import Communication.LoginList;

public class LobbyUI extends JFrame implements ActionListener, KeyListener {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea chatArea;
	private JButton btnGameStart;
	private JButton btnExit;
	private JScrollPane scrollPane;
	private ServerAccess sa;
	private LoadingUi loading;
	private JButton btnRecord;
	private JScrollPane listPane;
	private JList<String> playerList;
	private DefaultListModel<String> lm;
	private Vector<String> listv;
	private LoginList l = new LoginList();	
	
	public LobbyUI(ServerAccess sa) {
		
		this.sa = sa;
		sa.setLobby(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 643, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel chatPanel = new JPanel();
		chatPanel.setBounds(12, 10, 472, 314);
		contentPane.add(chatPanel);
		chatPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(0, 293, 472, 21);
		chatPanel.add(textField);
		textField.setColumns(10);
		textField.addKeyListener(this);
		
		chatArea = new JTextArea();
		chatArea.setText("로그인 되었습니다.");
		chatArea.setEditable(false);
		chatArea.setForeground(Color.BLACK);
		//chatArea.setBounds(0, 0, 603, 289);
		
		scrollPane = new JScrollPane(chatArea);
		scrollPane.setLocation(0, 0);
		scrollPane.setSize(472, 283);
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
		
		btnRecord = new JButton("Record");
		btnRecord.addActionListener(this);
		buttonPanel.add(btnRecord);
		buttonPanel.add(btnExit);
		
		listPane = new JScrollPane();
		listPane.setBounds(496, 10, 119, 314);
		contentPane.add(listPane);
		
		lm = new DefaultListModel<>();
		listv = new Vector<>();
		
		playerList = new JList();
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPane.setViewportView(playerList);
		
		setVisible(true);
	}
	
	public void addString(String str){
		chatArea.append(str);
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}
	
	public void runGame() throws IOException{				// 테트리스 게임 창을 위해 로딩창과 로비창을 종료
		loading.dispose();
		this.setVisible(false);
	}
	
	public void endGame(){
		this.setVisible(true);
	}
	
	public void addList(String p){
		lm.addElement(p);
		playerList.setModel(lm);
	}
	
	public void setList(LoginList l){
		this.l = l;
	}
	
	public void addList(){
		listv = l.getList();
		for(int i=0;i<listv.size();i++){
			lm.add(i, listv.get(i));
		}
		playerList.setModel(lm);
		
	}
	
	public void delList(String p){
		for(int i=0;i<lm.size();i++){
			if(lm.get(i).equals(p)==true){
				lm.remove(i);
			}
		}
		
		playerList.setModel(lm);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnGameStart){
			loading = new LoadingUi();
			sa.startGame();
		}
		else if(e.getSource() == btnExit){
			System.exit(1);
		}
		else if(e.getSource() == btnRecord){
			new RecordUI(sa);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			sa.sendMessage(textField.getText());	
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
