package Communication;

import java.io.Serializable;

public class Record implements Serializable{
	private static final long serialVersionUID = 1L;
	private String winner;
	private String loser;
	private String date;
	
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getLoser() {
		return loser;
	}
	public void setLoser(String loser) {
		this.loser = loser;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
