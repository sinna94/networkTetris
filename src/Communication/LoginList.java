package Communication;

import java.io.Serializable;
import java.util.Vector;

public class LoginList implements Serializable{

	private Vector<String> vector;
	
	public LoginList(){
		vector = new Vector<>();
	}
	
	public Vector<String> getList(){
		return vector;
	}
	
	public void setList(String id){
		vector.add(id);
	}

	public void remove(int i){
		vector.remove(i);
	}
	
}
