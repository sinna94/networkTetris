package Client;

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

public class LoginUI extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField idField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton cancelButton;
	private JButton joinButton;
	private ServerAccess sv;
	
	public LoginUI(ServerAccess sv) throws IOException {
		this.sv = sv;
		
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			sv.login(idField.getText(), passwordField.getText());
			while (true) {
				System.out.println(sv.isRecieve + " " + sv.loginOK);
				if (sv.isRecieve){
					if (sv.loginOK) {
						new LobbyUI(sv);
						this.dispose();
					} else {
						JOptionPane.showMessageDialog(null, "틀림");
					}
					break;
				}
				else{
					continue;
				}
			}
		}

		else if (e.getSource() == cancelButton) {
			dispose();
		} else if (e.getSource() == joinButton) {

		}
	}

	
	
}
