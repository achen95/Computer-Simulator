package project;

public class Memory {
	public static final int DATA_SIZE = 2048;
	private int[] data = new int[DATA_SIZE];
	private int changedIndex = -1;
	
	int[] getData() {
		return data;
	}
	
	int getData(int index) {
		return data[index];
	}

	public int getChangedIndex() {
		return changedIndex;
	}

	void setData(int index, int value) {
		data[index] = value;
		changedIndex = index;
	}
	
	void clear(int start, int end) {
		for (int i = 0; i < end; i++) {
			if (start <= i) {
				data[i] = 0;
			}
		}
		changedIndex = -1;
	}
	
}
