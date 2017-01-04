package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {
	public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;
	private Job currentJob;
	Job[] jobs = new Job[2];
	Code code = new Code();
	
	public MachineModel() {
		this(() -> System.exit(0));
	}
	
	public MachineModel(HaltCallback cb) {
		callback = cb;
		//INSTRUCTION_MAP entry for "ADDI"
        INSTRUCTIONS.put(0xA, arg -> {
            cpu.setAccumulator(cpu.getAccumulator() + arg);
            cpu.incrementIP();
        });

        //INSTRUCTION_MAP entry for "ADD"
        INSTRUCTIONS.put(0xB, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            cpu.setAccumulator(cpu.getAccumulator() + arg1);
            cpu.incrementIP();
        });

        //INSTRUCTION_MAP entry for "ADDN"
        INSTRUCTIONS.put(0xC, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(cpu.getAccumulator() + arg2);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "SUBI"
        INSTRUCTIONS.put(0xD, arg -> {
        	cpu.setAccumulator(cpu.getAccumulator() - arg);
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "SUB"
        INSTRUCTIONS.put(0xE, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase() + arg);
            cpu.setAccumulator(cpu.getAccumulator() - arg1);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "SUBN"
        INSTRUCTIONS.put(0xF, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(cpu.getAccumulator() - arg2);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "MULI"
        INSTRUCTIONS.put(0x10, arg -> {
        	cpu.setAccumulator(cpu.getAccumulator() * arg);
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "MUL"
        INSTRUCTIONS.put(0x11, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase() + arg);
            cpu.setAccumulator(cpu.getAccumulator() * arg1);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "MULN"
        INSTRUCTIONS.put(0x12, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(cpu.getAccumulator() * arg2);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "DIVI"
        INSTRUCTIONS.put(0x13, arg -> {
        	if (arg == 0) {
            	throw new DivideByZeroException("Cannot divide by zero");
            }
        	cpu.setAccumulator(cpu.getAccumulator() / arg);
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "DIV"
        INSTRUCTIONS.put(0x14, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase() + arg);
            if (arg1 == 0) {
            	throw new DivideByZeroException("Cannot divide by zero");
            }
            cpu.setAccumulator(cpu.getAccumulator() / arg1);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "DIVN"
        INSTRUCTIONS.put(0x15, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            if (arg2 == 0) {
            	throw new DivideByZeroException("Cannot divide by zero");
            }
            cpu.setAccumulator(cpu.getAccumulator() / arg2);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "NOP"
        INSTRUCTIONS.put(0x0, arg -> {
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "LODI"
        INSTRUCTIONS.put(0x1, arg -> {
        	cpu.setAccumulator(arg);
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "LOD"
        INSTRUCTIONS.put(0x2, arg -> {
        	int arg1 = memory.getData(cpu.getMemoryBase()+arg);
        	cpu.setAccumulator(arg1);
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "LODN"
        INSTRUCTIONS.put(0x3, arg -> {
        	int arg1 = memory.getData(cpu.getMemoryBase()+arg);
        	int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
        	cpu.setAccumulator(arg2);
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "STO"
        INSTRUCTIONS.put(0x4, arg -> {
        	memory.setData(arg+cpu.getMemoryBase(), cpu.getAccumulator());
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "STON"
        INSTRUCTIONS.put(0x5, arg -> {
        	int arg1 = memory.getData(arg+cpu.getMemoryBase());
        	memory.setData(arg1+cpu.getMemoryBase(), cpu.getAccumulator());
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "JMPI"
        INSTRUCTIONS.put(0x6, arg -> {
        	cpu.setInstructionPointer(cpu.getInstructionPointer()+arg);
        });
        
        //INSTRUCTION_MAP entry for "JUMP"
        INSTRUCTIONS.put(0x7, arg -> {
        	int arg1 = memory.getData(arg+cpu.getMemoryBase());
        	cpu.setInstructionPointer(cpu.getInstructionPointer()+arg1);
        });
        
        //INSTRUCTION_MAP entry for "JMZI"
        INSTRUCTIONS.put(0x8, arg -> {
        	if (cpu.getAccumulator() == 0) {
        		cpu.setInstructionPointer(cpu.getInstructionPointer()+arg);
        	}
        	else {
        		cpu.incrementIP();
        	}
        });
        
        //INSTRUCTION_MAP entry for "JMPZ"
        INSTRUCTIONS.put(0x9, arg -> {
        	if (cpu.getAccumulator() == 0) {
        		int arg1 = memory.getData(arg+cpu.getMemoryBase());
            	cpu.setInstructionPointer(cpu.getInstructionPointer()+arg1);
        	}
        	else {
        		cpu.incrementIP();
        	}
        });
        
        //INSTRUCTION_MAP entry for "ANDI"
        //This relates to the && operation in C: non-zero values 
        //can be treated as true and 0 as false. a && b is only true 
        //if both a and b are true.
        INSTRUCTIONS.put(0x16, arg -> {
        	if (cpu.getAccumulator() != 0 && arg != 0) {
        		cpu.setAccumulator(1);
        	}
        	else {
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "AND"
        INSTRUCTIONS.put(0x17, arg -> {
        	int arg1 = memory.getData(arg + cpu.getMemoryBase());
        	if (cpu.getAccumulator() != 0 && arg1 != 0) {
        		cpu.setAccumulator(1);
        	}
        	else {
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "NOT"
        //this corresponds to exchanging true and false. 
        //Increment instructionPointer.
        INSTRUCTIONS.put(0x18, arg -> {
        	if (cpu.getAccumulator() != 0) {
        		cpu.setAccumulator(0);
        	}
        	else {
        		cpu.setAccumulator(1);
        	}
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "CMPL"
        // If the indicated memory value is negative this signals 
        //true in the accumulator, otherwise false.
        INSTRUCTIONS.put(0x19, arg -> {
        	int arg1 = memory.getData(arg + cpu.getMemoryBase());
        	if (arg1 < 0) {
        		cpu.setAccumulator(1);
        	}
        	else {
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "CMPZ"
        //If the indicated memory value is zero this signals true in 
        //the accumulator, otherwise false.
        INSTRUCTIONS.put(0x1A, arg -> {
        	int arg1 = memory.getData(arg + cpu.getMemoryBase());
        	if (arg1 == 0) {
        		cpu.setAccumulator(1);
        	}
        	else {
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "HALT"
        INSTRUCTIONS.put(0x1F, arg -> {
        	callback.halt();
        });
        
        INSTRUCTIONS.put(0x1B, arg -> {
        	int target = memory.getData(cpu.getMemoryBase() + arg);
        	cpu.setInstructionPointer(currentJob.getStartcodeIndex()+target);
        });
        
        for (int i = 0; i < jobs.length; i++) {
        	jobs[i] = new Job();
        }
        currentJob = jobs[0];
        jobs[0].setStartcodeIndex(0);
        jobs[0].setStartmemoryIndex(0);
        jobs[1].setStartcodeIndex(Code.CODE_MAX/4);
        jobs[1].setStartmemoryIndex(Memory.DATA_SIZE/2);
	}
	
	int getAccumulator() {
		return cpu.getAccumulator();
	}

	void setAccumulator(int accumulator) {
		cpu.setAccumulator(accumulator);
	}

	int getInstructionPointer() {
		return cpu.getInstructionPointer();
	}

	void setInstructionPointer(int instructionPointer) {
		cpu.setInstructionPointer(instructionPointer);
	}

	int getMemoryBase() {
		return cpu.getMemoryBase();
	}

	void setMemoryBase(int memoryBase) {
		cpu.setMemoryBase(memoryBase);
	}
	int[] getData() {
		return memory.getData();
	}
	int getData(int index) {
		return memory.getData(index);
	}

	void setData(int index, int value) {
		memory.setData(index, value);
	}
	int getChangedIndex() {
		return memory.getChangedIndex();
	}

	Instruction get(int instructionNum) {
		return (INSTRUCTIONS.get(instructionNum));
	}

	void setCode(int i, int op, int arg) {
		code.setCode(i, op, arg);
	}

	Code getCode() {
		return code;
	}
	
	Job getCurrentJob() {
		return currentJob;
	}
	void setJob(int i) {
		if (i != 0 && i != 1) {
			throw new IllegalArgumentException ("Exception thrown!");
		}
		currentJob.setCurrentAcc(cpu.getAccumulator());
		currentJob.setCurrentIP(cpu.getInstructionPointer());
		currentJob = jobs[i];
		cpu.setAccumulator(currentJob.getCurrentIP());
		cpu.setInstructionPointer(currentJob.getCurrentAcc());
		cpu.setMemoryBase(currentJob.getStartmemoryIndex());
		
	}

	States getCurrentState() {
		return currentJob.getCurrentState();
	}

	void setCurrentState(States currentState) {
		currentJob.setCurrentState(currentState);
	}
	
	void clearJob() {
		memory.clear(currentJob.getStartmemoryIndex(), 
				currentJob.getStartmemoryIndex() + Memory.DATA_SIZE/2);
		code.clear(currentJob.getStartcodeIndex(), 
				currentJob.getStartcodeIndex() + currentJob.getCodeSize());
		setAccumulator(0);
		setInstructionPointer(currentJob.getStartcodeIndex());
		currentJob.reset();
	}
	
	void step() {
		try {
			int ip = cpu.getInstructionPointer();
			if (ip < currentJob.getStartcodeIndex() || 
					ip >= currentJob.getStartcodeIndex()+currentJob.getCodeSize()) {
				throw new CodeAccessException ("problem with instruction pointer!");
			}
			int opcode = code.getOp(ip); 
			int arg = code.getArg(ip);
			get(opcode).execute(arg);
		}
		catch(Exception e){
			callback.halt();
			throw e;
		}
	}
	
}
