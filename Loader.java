package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Loader {

	public static String load(MachineModel model, File file, 
			int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if (model == null || file == null) {
			return null;
		}
		try {
			Scanner input = new Scanner(file);
			boolean incode = true;
			while (input.hasNextLine()) {
				Scanner parser = new Scanner(input.nextLine());
				int parseNum = parser.nextInt(16);
				//System.out.println(parseNum);
				if (incode && parseNum == -1) {
					incode = false;
				}
				else if (incode && parseNum != -1) {
					int arg = parser.nextInt(16);
					model.setCode(codeOffset + codeSize, parseNum, arg);
					codeSize++;
				}
				else {
					int val = parser.nextInt(16);
					model.setData(parseNum + memoryOffset, val);
				}
				parser.close();
			}
			input.close();
			return ("" + codeSize);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return ("Array Index" + e.getMessage());
		}
		catch (NoSuchElementException e) {
			return ("From Scanner: NoSuchElementException");
		}
		catch (FileNotFoundException e) {
			return ("File " + file.getName() + " Not Found");
		}
	}
	
	public static void main(String[] args) {
		MachineModel model = new MachineModel();
		String s = Loader.load(model, new File("factorial.pexe"),100,200);
		for(int i = 100; i < 100+Integer.parseInt(s); i++) {
			System.out.println(model.getCode().getText(i));			
		}
		System.out.println(200 + " " + model.getData(200));
	}
}
