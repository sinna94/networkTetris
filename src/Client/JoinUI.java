package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Communication.Account;

public class JoinUI extends JFrame implements ActionListener{
	private JPanel contentPane;
	private JButton joinButton;
	private JButton cancelButton;
	private JTextField textField;
	private JPasswordField passwordField;
	
	private ServerAccess sa;
	
	public JoinUI(ServerAccess sa) {
		this.sa = sa;
		
		sa.setJoin(this);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 312, 182);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setVisible(true);
		
		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(null);
		
		JLabel idLabel = new JLabel("ID");
		idLabel.setBounds(31, 29, 57, 15);
		inputPanel.add(idLabel);
		
		JLabel pwLabel = new JLabel("비밀번호");
		pwLabel.setBounds(31, 67, 57, 15);
		inputPanel.add(pwLabel);
		
		textField = new JTextField();
		textField.setBounds(100, 23, 178, 28);
		inputPanel.add(textField);
		textField.setColumns(20);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		passwordField.setBounds(100, 61, 178, 28);
		inputPanel.add(passwordField);
		
		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		
		joinButton = new JButton("가입");
		cancelButton = new JButton("취소");
		joinButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		buttonPanel.add(joinButton);
		buttonPanel.add(cancelButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == joinButton){
			if(textField.getText().equals("")==false && passwordField.getText().equals("")==false){
				Account ac = new Account();
				ac.setId(textField.getText());
				ac.setPw(passwordField.getText());
				ServerAccess.output.println("JOIN");
				try {
					sa.oos.writeObject(ac);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "모두 입력해주세요.");
			}
		}
		else if (e.getSource() == cancelButton){
			dispose();
		}
		
	}
}
