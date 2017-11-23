package Communication;

import java.io.Serializable;
import java.util.ArrayList;

public class DataList implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Record> list = new ArrayList<>();
	
	public void setList(ArrayList<Record> list){
		this.list = list;
	}
	
	public ArrayList<Record> getList(){
		return list;
	}

}
