import java.awt.Color;

public abstract class Block {
	/*
	 * private int O_block = 1; private int I_block = 2; private int S_block =
	 * 3; private int Z_block = 4; private int L_block = 5; private int J_block
	 * = 6; private int T_block = 7;
	 */

	public abstract int[][] getBlock();

	public abstract int[][] rotation();

	public abstract Color getColor();
	
	public abstract int[][] getLocation();
}

class O_Block extends Block {

	private int[][] location = { { -1, 0 }, { -1, -1 }, { 0, 0 }, { 0, -1 } };
	
	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {
		return location;
	}

	@Override
	public Color getColor() {
		return Color.RED;
	}

	@Override
	public int[][] getLocation() {
		return location;
	}
}

class I_Block extends Block {

	private int[][] location = { { -2, 0 }, { -1, 0 }, { 0, 0 }, { 1, 0 } };

	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < location.length; i++) {
			rotateLocation[i][0] = location[i][1];
			rotateLocation[i][1] = location[i][0];
		}
		location = rotateLocation;
		return location;
	}
	
	@Override
	public Color getColor() {
		return Color.YELLOW;
	}
	
	@Override
	public int[][] getLocation() {
		return location;
	}
}

class S_Block extends Block {
	private int[][] location = { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 1, 0 } };
	private boolean ck = true;
	
	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {								// 처음엔 x 두번째는 y 에 바꾸고 -1 곱해야 함
		int[][] rotateLocation = new int[4][2];

		if (ck) {
			for (int i = 0; i < location.length; i++) {
				rotateLocation[i][0] = -location[i][1];
				rotateLocation[i][1] = location[i][0];
				ck = false;
			}
		} else {
			for (int i = 0; i < location.length; i++) {
				rotateLocation[i][0] = location[i][1];
				rotateLocation[i][1] = -location[i][0];
				ck = true;
			}
		}
		location = rotateLocation;
		return location;
	}
	
	@Override
	public Color getColor() {
		return Color.GREEN;
	}
	
	@Override
	public int[][] getLocation() {
		return location;
	}
}

class Z_Block extends Block {
	private int[][] location = { { -1, 0 }, { 0, -1 }, { 0, 0 }, { 1, -1 } };
	private boolean ck = true;
	
	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		if (ck) {
			for (int i = 0; i < location.length; i++) {
				rotateLocation[i][0] = -location[i][1];
				rotateLocation[i][1] = location[i][0];
				ck = false;
			}
		} else {
			for (int i = 0; i < location.length; i++) {
				rotateLocation[i][0] = location[i][1];
				rotateLocation[i][1] = -location[i][0];
				ck = true;
			}
		}
		location = rotateLocation;
		return location;
	}
	
	@Override
	public Color getColor() {
		return Color.BLUE;
	}
	
	@Override
	public int[][] getLocation() {
		return location;
	}
}

class L_Block extends Block {
	private int[][] location = { { -1, 0 }, { -1, -1 }, { 0, 0 }, { 1, 0 } };

	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < location.length; i++) {
			rotateLocation[i][0] = -location[i][1];
			rotateLocation[i][1] = location[i][0];
		}
		location = rotateLocation;
		return location;
	}
	
	@Override
	public Color getColor() {
		return Color.ORANGE;
	}
	
	@Override
	public int[][] getLocation() {
		return location;
	}
}

class J_Block extends Block {
	private int[][] location = { { -1, 0 }, { 1, 0 }, { 0, 0 }, { 1, -1 } };

	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < location.length; i++) {
			rotateLocation[i][0] = -location[i][1];
			rotateLocation[i][1] = location[i][0];
		}
		location = rotateLocation;
		return location;
	}
	
	@Override
	public Color getColor() {
		return Color.MAGENTA;
	}
	
	@Override
	public int[][] getLocation() {
		return location;
	}
}

class T_Block extends Block {
	private int[][] location = { { -1, 0 }, { 0, -1 }, { 0, 0 }, { 1, 0 } };

	@Override
	public int[][] getBlock() {
		return location;
	}

	@Override
	public int[][] rotation() {
		int[][] rotateLocation = new int[4][2];

		for (int i = 0; i < location.length; i++) {
			rotateLocation[i][0] = -location[i][1];
			rotateLocation[i][1] = location[i][0];
		}
		location = rotateLocation;
		return location;
	}
	
	@Override
	public Color getColor() {
		return Color.WHITE;
	}
	
	@Override
	public int[][] getLocation() {
		return location;
	}
}
