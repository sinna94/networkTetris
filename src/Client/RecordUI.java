package Client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Communication.DataList;
import Communication.Record;

public class RecordUI extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable tableRecord;
	private JLabel textRecord;
	private JButton btnExit;
	private ServerAccess sa;
	private DataList list = new DataList();
	private DefaultTableModel model;
	
	public RecordUI(ServerAccess sa) {
		this.sa = sa;
		
		sa.setRecord(this);
		
		callData();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 587, 322);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		textRecord = new JLabel();
		textRecord.setFont(new Font("±¼¸²", Font.PLAIN, 15));
		panel.add(textRecord);
		
		String header[] = {"°ÔÀÓ¹øÈ£", "½ÂÀÚ", "ÆÐÀÚ", "³¯Â¥"};
		model = new DefaultTableModel(null, header);
		tableRecord = new JTable(model);
		tableRecord.setRowSelectionAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(tableRecord);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		btnExit = new JButton("´Ý±â");
		btnExit.addActionListener(this);
		panel_1.add(btnExit);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnExit){
			dispose();
		}
	}

	public void callData(){
		ServerAccess.output.println("RECORD");
	}
	
	public void addData(){
		ArrayList<Record> record = list.getList();
		
		String winner;
		String loser;
		String date;
		
		int win = 0;
		int lose = 0;
		
		for(int i = 0;i<record.size();i++){
			Record r = record.get(i);
			winner = r.getWinner();
			loser = r.getLoser();
			
			if(winner.equals(sa.getUserId()) == true){
				win++;
			}
			if(loser.equals(sa.getUserId()) == true){
				lose++;
			}
			
			date = r.getDate();
			System.out.println(winner);
			String [] data = {String.valueOf(i), winner, loser, date};
			model.addRow(data);
		}
		textRecord.setText("ÀüÀû [ " + (win+lose) + "Àü " + win + "½Â " + lose + "ÆÐ ]");
	}
	

	public void setRecord(DataList list) {
		this.list = list;
	}
}
