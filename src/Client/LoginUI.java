package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginUI extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField idField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton cancelButton;
	private JButton joinButton;
	private ServerAccess sv;

	public LoginUI() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 175);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel inputPanel = new JPanel();
		inputPanel.setBounds(0, 0, 264, 103);
		contentPane.add(inputPanel);
		inputPanel.setLayout(null);

		JLabel idLabel = new JLabel("ID");
		idLabel.setBounds(30, 15, 55, 32);
		inputPanel.add(idLabel);

		idField = new JTextField();
		idField.setBounds(85, 16, 167, 32);
		inputPanel.add(idField);
		idField.setColumns(10);

		JLabel passLabel = new JLabel("Password");
		passLabel.setBounds(12, 57, 64, 32);
		inputPanel.add(passLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(85, 57, 167, 32);
		inputPanel.add(passwordField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 102, 264, 36);
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(new GridLayout(0, 3, 0, 0));

		loginButton = new JButton("로그인");
		loginButton.addActionListener(this);
		buttonPanel.add(loginButton);

		cancelButton = new JButton("취소");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		joinButton = new JButton("회원가입");
		joinButton.addActionListener(this);
		buttonPanel.add(joinButton);

		sv = new ServerAccess();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			login();

			

		}

		else if (e.getSource() == cancelButton) {
			dispose();
		} else if (e.getSource() == joinButton) {

		}
	}

	private void login() {
		Account account = new Account();
		account.setId(idField.getText());
		account.setPw(passwordField.getText());
		try {
			sv.oos.writeObject((Account) account);
			sv.oos.flush();
			String correct = sv.input.readLine();
			System.out.println(correct + " 송신완료");
			if (correct.equals("true")) {
				new LobbyUI();
				sv.socket.close();
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "틀림");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
