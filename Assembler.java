package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import project1.InstructionMap;

public class Assembler {
	
	public static String assemble(File input, File output) {
		ArrayList<String> code = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		boolean incode = true;
		String ret = "success";
		try {
			Scanner inputFile = new Scanner(input);
			int num = 1;
			while (inputFile.hasNextLine()) {
				String line = inputFile.nextLine();
				if (line.trim().length() == 0) {
					if (inputFile.hasNextLine()) {
						if (inputFile.nextLine().trim().length() > 0) {
							ret = "ERROR: Line " + num + " is blank!";
							break;
						}
					}
				}
				else if (line.charAt(0) == ' '){
					ret = "ERROR: Line " + num + " starts with white space (space)";
					break;
				} 
				else if (line.charAt(0) == '\t'){
					ret = "ERROR: Line " + num + " starts with white space (tab)";
					break;
				} 
				else if (line.trim().toUpperCase().equals("DATA") && !line.trim().equals("DATA")){
					ret = "ERROR: Line " + num + " has a data problem (DATA not in upper case)";
					break;
				} 
				else {
					if (incode && line.trim().equals("DATA")) {
						incode = false;
					}
					else if (incode) {
						code.add(line.trim());
					}
					else {
						data.add(line.trim());
					}
				}
				num++;
			}
			inputFile.close();
			
		} catch (FileNotFoundException ex){
			ex.printStackTrace();
		} catch (StringIndexOutOfBoundsException ex){
			ex.printStackTrace();
		}
		
		ArrayList<String> outText = new ArrayList<>();
		int num = 1;
		for (String a : code) {
			String[] parts = a.trim().split("\\s+");
			if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) && !InstructionMap.sourceCodes.contains(parts[0])){
				ret = "ERROR: Line " + num + " does not have the " + 
						"instruction mnemonic in upper case";
				return ret;
			} 
			else if (!InstructionMap.sourceCodes.contains(parts[0])){
				ret = "ERROR: Line " + num + " has a bad mnemonic";
				return ret;
			} 
			else if (InstructionMap.noArgument.contains(parts[0]) && parts.length != 1){
				ret = "ERROR: Line " + num + " has an illegal argument";
				return ret;
			} 
			else if (!InstructionMap.noArgument.contains(parts[0]) && parts.length == 1){
				ret = "ERROR: Line " + num + " is missing an argument";
				return ret;
			} 
			else if (!InstructionMap.noArgument.contains(parts[0]) && parts.length >= 3) {
				ret = "ERROR: Line " + num + " has more than one argument";
				return ret;
			}
			else {
				if (parts.length == 2) {
					if (parts[1].startsWith("#")) {
						if (!InstructionMap.immediateOK.contains(parts[0])){
							ret = "ERROR: Line " + num + " has an illegal immediate argument";
							return ret;
						}
						else {
							if (parts[0].equals("JUMP")) {
								parts[0] = "JMP";
							}
							if (parts[0].equals("JMPZ")) {
								parts[0] = "JMZ";
							}
							parts[1] = parts[1].substring(1);
							parts[0] = parts[0] + "I";
						}
					}
					else if (parts[1].startsWith("&")) {
						if (!InstructionMap.indirectOK.contains(parts[0])){
							ret = "ERROR: Line " + num + " has an illegal indirect argument";
							return ret;
						} 
						else {
							if (parts[0].equals("JUMP")) {
								parts[0] = "JMP";
							}
							parts[1] = parts[1].substring(1);
							parts[0] = parts[0] + "N";
						}
						
					}
					int opcode = InstructionMap.opcode.get(parts[0]);
					try {
						Integer.parseInt(parts[1],16);
					} catch (NumberFormatException ex) {
						ret = "ERROR: Line " + num 
								+ " does not have a numeric argument";
						return ret;
					}
					outText.add(Integer.toHexString(opcode).toUpperCase() + " " + parts[1]);
				}
				else if (parts.length == 1) {
					int opcode = InstructionMap.opcode.get(parts[0]);
					outText.add(Integer.toHexString(opcode).toUpperCase() + " 0");
				}
			}
			num++;
			//System.out.println(parts[0]);
			
		}
		outText.add("-1");
		int num2 = num + 1;
		for (String line : data) {
			String[] parts = line.trim().split("\\s+");
			if (parts.length != 2){
				ret = "ERROR: Line " + num2 
						+ " has a bad memory format";
				return ret;
			} 
			else {
				try {
					Integer.parseInt(parts[0],16);
				} catch (NumberFormatException ex) {
					ret = "ERROR: Line " + num2 
							+ " has an illegal number format (data memory address)";
					return ret;
				}
				try {
					Integer.parseInt(parts[1],16);
				} catch (NumberFormatException ex) {
					ret = "ERROR: Line " + num2 
							+ " has an illegal number format (data memory value)";
					return ret;
				}
			}
			num2++;
		}
		
		outText.addAll(data);
		
		if (ret.equals("success")) {
			try {
				PrintWriter out = new PrintWriter(output);
				for (String a : outText) {
					out.println(a);
					//System.out.println(a);
				}
				out.close();
			} catch (FileNotFoundException ex) {
				ret = "ERROR: unable to open " + output;
				return ret;
			}
		}
		return ret;
}
	
	public static void main(String[] args) {
//		System.out.println("Enter the name of the file without extension: ");
//		try (Scanner keyboard = new Scanner(System.in)) {
//			String filename = keyboard.nextLine();
//			String i = assemble(new File(filename + ".pasm"), 
//					new File(filename + ".pexe"));
//			System.out.println(i );
//		}
		for (int i = 3; i <= 26; i++) {
			String s = "";
			if (i < 10) {
				s += "0";
			}
			s += i + "e";
			System.out.println(s + "    " + assemble(new File(s + ".pasm"), new File(s + ".pexe")));
		}
	}
}