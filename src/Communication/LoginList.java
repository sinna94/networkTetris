package Communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class LoginList implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<String> list;
	
	public LoginList(){
		list = new ArrayList<>();
	}
	
	public ArrayList<String> getList(){
		return list;
	}
	
	public void setList(String id){
		list.add(id);
	}

	public void remove(int i){
		list.remove(i);
	}
	
}
