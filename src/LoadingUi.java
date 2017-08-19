import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class LoadingUi extends JFrame{

	private JPanel contentPane;
	private JLabel lbwait;
	public LoadingUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 267, 163);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		lbwait = new JLabel("Waiting for others.");
		lbwait.setFont(new Font("±¼¸²", Font.BOLD | Font.ITALIC, 16));
		lbwait.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbwait, BorderLayout.CENTER);
	}
	
	
}
