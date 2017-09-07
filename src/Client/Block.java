package Client;
import java.awt.Color;
import java.io.Serializable;

public abstract class Block implements Cloneable{
	/*
	 * private int O_block = 1; private int I_block = 2; private int S_block =
	 * 3; private int Z_block = 4; private int L_block = 5; private int J_block
	 * = 6; private int T_block = 7;
	 */

	private int[][] location;
	
	public int[][] getBlock() {
		return getLocation();
	}

	public abstract int[][] rotation();

	public abstract Color getColor();
	
	public int[][] getLocation() {
		return location;
	}

	public void setLocation(int[][] location) {
		this.location = location;		
	}
	
	public Object clone() {							// 블록 복사를 위해서
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}

class O_Block extends Block {

	public O_Block() {
		int[][] location = { { -1, 0 }, { -1, -1 }, { 0, 0 }, { 0, -1 } };
		setLocation(location);
	}
	@Override
	public int[][] rotation() {
		return getLocation();
	}

	@Override
	public Color getColor() {
		return Color.RED;
	}	
}

class I_Block extends Block {

	public I_Block() {
		int[][] location = { { -2, 0 }, { -1, 0 }, { 0, 0 }, { 1, 0 } };
		setLocation(location);
		}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < getLocation().length; i++) {
			rotateLocation[i][0] = getLocation()[i][1];
			rotateLocation[i][1] = getLocation()[i][0];
		}
		setLocation(rotateLocation);
		return getLocation();
	}
	
	@Override
	public Color getColor() {
		return Color.YELLOW;
	}
}

class S_Block extends Block {
	
	public S_Block(){
		int[][] location = { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 1, 0 } };
		setLocation(location);
	}
	
	private boolean ck = true;
	
	@Override
	public int[][] rotation() {								// 처음엔 x 두번째는 y 에 바꾸고 -1 곱해야 함
		int[][] rotateLocation = new int[4][2];

		if (ck) {
			for (int i = 0; i < getLocation().length; i++) {
				rotateLocation[i][0] = -getLocation()[i][1];
				rotateLocation[i][1] = getLocation()[i][0];
				ck = false;
			}
		} else {
			for (int i = 0; i < getLocation().length; i++) {
				rotateLocation[i][0] = getLocation()[i][1];
				rotateLocation[i][1] = -getLocation()[i][0];
				ck = true;
			}
		}
		setLocation(rotateLocation);
		return getLocation();
	}
	
	@Override
	public Color getColor() {
		return Color.GREEN;
	}
}

class Z_Block extends Block {
	public Z_Block(){
		int[][] location = { { -1, 0 }, { 0, -1 }, { 0, 0 }, { 1, -1 } };
		setLocation(location);
	}
	private boolean ck = true;
	
	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		if (ck) {
			for (int i = 0; i < getLocation().length; i++) {
				rotateLocation[i][0] = -getLocation()[i][1];
				rotateLocation[i][1] = getLocation()[i][0];
				ck = false;
			}
		} else {
			for (int i = 0; i < getLocation().length; i++) {
				rotateLocation[i][0] = getLocation()[i][1];
				rotateLocation[i][1] = -getLocation()[i][0];
				ck = true;
			}
		}
		setLocation(rotateLocation);
		return getLocation();
	}
	
	@Override
	public Color getColor() {
		return Color.BLUE;
	}
}

class L_Block extends Block {
	public L_Block(){
		int[][] location = { { -1, 0 }, { -1, -1 }, { 0, 0 }, { 1, 0 } };
		setLocation(location);
	}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < getLocation().length; i++) {
			rotateLocation[i][0] = -getLocation()[i][1];
			rotateLocation[i][1] = getLocation()[i][0];
		}
		setLocation(rotateLocation);
		return getLocation();
	}
	
	@Override
	public Color getColor() {
		return Color.ORANGE;
	}
}

class J_Block extends Block {
	public J_Block(){
		int[][] location = { { -1, 0 }, { 1, 0 }, { 0, 0 }, { 1, -1 } };
		setLocation(location);
	}
	
	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < getLocation().length; i++) {
			rotateLocation[i][0] = -getLocation()[i][1];
			rotateLocation[i][1] = getLocation()[i][0];
		}
		setLocation(rotateLocation);
		return getLocation();
	}
	
	@Override
	public Color getColor() {
		return Color.MAGENTA;
	}
}

class T_Block extends Block {
	public T_Block(){
		int[][] location = { { -1, 0 }, { 0, -1 }, { 0, 0 }, { 1, 0 } };
		setLocation(location);
	}
	
	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < getLocation().length; i++) {
			rotateLocation[i][0] = -getLocation()[i][1];
			rotateLocation[i][1] = getLocation()[i][0];
		}
		setLocation(rotateLocation);
		return getLocation();
	}
	
	@Override
	public Color getColor() {
		return Color.WHITE;
	}
}
