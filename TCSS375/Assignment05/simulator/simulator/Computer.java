package simulator;

import java.util.Arrays;

/**
 * The simulator.Computer class is composed of registers, memory, PC, IR, and CC.
 * The simulator.Computer can execute a program based on the the instructions in memory.
 *  
 * @author mmuppa
 * @author acfowler
 * @author Roman Bureacov
 * @version 4.1
 */
public class Computer {

	private final static int MAX_MEMORY = 50;
	private final static int MAX_REGISTERS = 8;

	private BitString mRegisters[];
	private BitString mMemory[];
	private BitString mPC;
	private BitString mIR;
	private BitString mCC;

	/**
	 * Initialize all memory addresses to 0, registers to 0 to 7
	 * PC, IR to 16 bit 0s and CC to 000.
	 */
	public Computer() {
		mPC = new BitString();
		mPC.setUnsignedValue(0);
		mIR = new BitString();
		mIR.setUnsignedValue(0);
		mCC = new BitString();
		mCC.setBits(new char[] { '0', '0', '0' });
		
		mRegisters = new BitString[MAX_REGISTERS];
		for (int i = 0; i < MAX_REGISTERS; i++) {
			mRegisters[i] = new BitString();
			mRegisters[i].setUnsignedValue(i);
		}

		mMemory = new BitString[MAX_MEMORY];
		for (int i = 0; i < MAX_MEMORY; i++) {
			mMemory[i] = new BitString();
			mMemory[i].setUnsignedValue(0);
		}
	}
	
	// The public accessor methods shown below are useful for unit testing.
	// Do NOT add public mutator methods (setters)!
	
	/**
	 * @return the registers
	 */
	public BitString[] getRegisters() {
		return copyBitStringArray(mRegisters);
	}

	/**
	 * @return the memory
	 */
	public BitString[] getMemory() {
		return copyBitStringArray(mMemory);
	}

	/**
	 * @return the PC
	 */
	public BitString getPC() {
		return mPC.copy();
	}

	/**
	 * @return the IR
	 */
	public BitString getIR() {
		return mIR.copy();
	}

	/**
	 * @return the CC
	 */
	public BitString getCC() {
		return mCC.copy();
	}
	
	/**
	 * Safely copies a simulator.BitString array.
	 * @param theArray the array to copy.
	 * @return a copy of theArray.
	 */
	private BitString[] copyBitStringArray(final BitString[] theArray) {
		final BitString[] bitStrings = new BitString[theArray.length];
		Arrays.setAll(bitStrings, n -> bitStrings[n] = theArray[n].copy());
		return bitStrings;
	}

	/**
	 * Loads a 16 bit word into memory at the given address. 
	 * @param address memory address
	 * @param word data or instruction or address to be loaded into memory
	 */
	private void loadWord(int address, BitString word) {
		if (address < 0 || address >= MAX_MEMORY) {
			throw new IllegalArgumentException("Invalid address");
		}
		mMemory[address] = word;
	}
	
	/**
	 * Loads a machine code program, as Strings.
	 * @param theWords the Strings that contain the instructions or data.
	 */
	public void loadMachineCode(final String ... theWords) {
		if (theWords.length == 0 || theWords.length >= MAX_MEMORY) {
			throw new IllegalArgumentException("Invalid words");
		}
		for (int i = 0; i < theWords.length; i++) {
			final BitString instruction = new BitString();
			instruction.setBits(theWords[i].toCharArray());
			loadWord(i, instruction);
		}
	}

	// The next 6 methods are used to execute the required instructions:
	// BR, ADD, LD, ST, AND, NOT, TRAP
	
	/**
	 * op   nzp pc9offset
	 * 0000 000 000000000
	 * 
	 * The condition codes specified by bits [11:9] are tested.
	 * If bit [11] is 1, N is tested; if bit [11] is 0, N is not tested.
	 * If bit [10] is 1, Z is tested, etc.
	 * If any of the condition codes tested is 1, the program branches to the memory location specified by
	 * adding the sign-extended PCoffset9 field to the incremented PC.
	 */
	public void executeBranch() {
		// get nzp bits from BR instruction
		final int lIRBits = this.mIR.substring(4, 3).getUnsignedValue();
		final int lCurrentCCBits = this.mCC.getUnsignedValue();

		if ((lIRBits & lCurrentCCBits) != 0) { // if any bit matches
			// PC + offset
			final int lCurrentPC = this.mPC.getUnsignedValue();
			final int lOffset = this.mIR.substring(7, 9).get2sCompValue();
			this.mPC.setUnsignedValue(lCurrentPC + lOffset);
		}
	}
	
	/**
	 * op   dr  sr1      sr2
	 * 0001 000 000 0 00 000
	 * 
	 * OR
	 * 
	 * op   dr  sr1   imm5
	 * 0001 000 000 1 00000
	 * 
	 * If bit [5] is 0, the second source operand is obtained from SR2.
	 * If bit [5] is 1, the second source operand is obtained by sign-extending the imm5 field to 16 bits.
	 * In both cases, the second source operand is added to the contents of SR1 and the
	 * result stored in DR. The condition codes are set, based on whether the result is
	 * negative, zero, or positive.
	 */
	public void executeAdd() {
		final int lDestReg = this.getFirstRegOfIRBitString();
		final int lSrcReg1 = this.mIR.substring(7, 3).getUnsignedValue();
		final int lSrcReg1Val = this.mRegisters[lSrcReg1].get2sCompValue();
		final int lResult;
		if (this.mIR.getBits()[10] == '1') { // if immediate
			final int lImmediateVal = this.getImmediateBits2sComp(5);
			lResult = lSrcReg1Val + lImmediateVal;
		} else { // else with sr2
			if (this.mIR.substring(11, 2).getUnsignedValue() != 0)
				throw new IllegalArgumentException("expected two 0's at bits [11:12]");
			final int lSrcReg2 = this.mIR.substring(13, 3).getUnsignedValue();
			final int lSrcReg2Val = this.mRegisters[lSrcReg2].get2sCompValue();
			lResult = lSrcReg1Val + lSrcReg2Val;
		}
		this.mRegisters[lDestReg].set2sCompValue(lResult);

		this.setCC(lResult);
	}
	
	/**
	 * Performs the load operation by placing the data from PC
	 * + PC offset9 bits [8:0]
	 * into DR - bits [11:9]
	 * then sets CC.
	 */
	public void executeLoad() {
		final int lDestReg = this.getFirstRegOfIRBitString();
		final int lPCOffset = this.getImmediateBits2sComp(9);
		final int lCurrentPC = this.mPC.getUnsignedValue();
		final BitString lMemory = this.mMemory[lCurrentPC + lPCOffset].copy();
		this.mRegisters[lDestReg] = lMemory;
		this.setCC(lMemory);
	}
	
	/**
	 * Store the contents of the register specified by SR
	 * in the memory location whose address is computed by sign-extending bits [8:0] to 16 bits
	 * and adding this value to the incremented PC.
	 */
	public void executeStore() {
		final int lSrcReg = this.getFirstRegOfIRBitString();
		final int lCurrentPC = this.mPC.getUnsignedValue();
		final int lOffset = this.getImmediateBits2sComp(9);
		this.mMemory[lCurrentPC + lOffset] = this.mRegisters[lSrcReg].copy();
	}
	
	/**
	 * op   dr  sr1      sr2
	 * 0101 000 000 0 00 000
	 * 
	 * OR
	 * 
	 * op   dr  sr1   imm5
	 * 0101 000 000 1 00000
	 * 
	 * If bit [5] is 0, the second source operand is obtained from SR2.
	 * If bit [5] is 1, the second source operand is obtained by sign-extending the imm5 field to 16 bits.
	 * In either case, the second source operand and the contents of SR1 are bitwise ANDed
	 * and the result stored in DR.
	 * The condition codes are set, based on whether the binary value produced, taken as a 2’s complement integer,
	 * is negative, zero, or positive.
	 */
	public void executeAnd() {
		final int lDestReg = this.getFirstRegOfIRBitString();
		final int lSrcReg1 = this.mIR.substring(7, 3).getUnsignedValue();
		final int lSrcReg1Val = this.mRegisters[lSrcReg1].get2sCompValue();
		final int lResult;
		if (this.mIR.getBits()[10] == '1') { // use immediate
			final int lImmediate = this.mIR.substring(11, 5).get2sCompValue();
			lResult = lSrcReg1Val & lImmediate;
		} else { // use sr2
			if (this.mIR.substring(11, 2).getUnsignedValue() != 0)
				throw new IllegalArgumentException("expected two 0's at bits [4:3]");

			final int lSrcReg2 = this.mIR.substring(13, 3).getUnsignedValue();
			final int lSrcReg2Val = this.mRegisters[lSrcReg2].get2sCompValue();
			lResult = lSrcReg1Val & lSrcReg2Val;
		}
		this.mRegisters[lDestReg].set2sCompValue(lResult);
		this.setCC(lResult);
	}

	/**
	 * Performs not operation by using the data from the source register (bits[8:6]) 
	 * and inverting and storing in the destination register (bits[11:9]).
	 * Then sets CC.
	 */
	public void executeNot() {
		final int lDestReg = this.getFirstRegOfIRBitString();
		final int lSrcReg = this.mIR.substring(7, 3).getUnsignedValue();
		final int lResult = ~(this.mRegisters[lSrcReg].get2sCompValue()); // bitwise NOT
		this.mRegisters[lDestReg].set2sCompValue(lResult);
		this.setCC(lResult);
	}
	
	/**
	 * Executes the trap operation by checking the vector (bits [7:0]
	 * 
	 * vector x21 - OUT
	 * vector x25 - HALT
	 * 
	 * @return true if this Trap is a HALT command; false otherwise.
	 */
	public boolean executeTrap() {
		final int lHaltCode = 0x25;
		final int lOutCode = 0x21 ;
		final int lTrapVector = this.mIR.substring(9, 7).getUnsignedValue();
		if (lTrapVector == lOutCode) System.out.print((char) this.mRegisters[0].get2sCompValue());
		return lTrapVector == lHaltCode;
	}

	/**
	 * This method will execute all the instructions starting at address 0 
	 * until a HALT instruction is encountered. 
	 */
	public void execute() {
		BitString lOpCodeBits;
		int lOpCode;
		boolean lHalt = false;

		while (!lHalt) {
			// Fetch the next instruction
			this.mIR = this.mMemory[this.mPC.getUnsignedValue()];
			// increment the PC
			this.mPC.addOne();

			// Decode the instruction's first 4 bits 
			// to figure out the opcode
			lOpCodeBits = this.mIR.substring(0, 4);
			lOpCode = lOpCodeBits.getUnsignedValue();

			// What instruction is this?
			switch (lOpCode) {
				case 0 -> this.executeBranch();			// BR	0000
				case 1 -> this.executeAdd();			// ADD	0001
				case 2 -> this.executeLoad();			// LD	0010
				case 3 -> this.executeStore();			// ST	0011
				case 5 -> this.executeAnd();			// AND	0101
				case 9 -> this.executeNot();			// NOT	1001
				case 15 -> lHalt = this.executeTrap();	// TRAP	1111
				default -> throw new UnsupportedOperationException("Illegal opCode: " + lOpCode);
			}
		}
	}

	/**
	 * Displays the computer's state
	 */
	public void display() {
		System.out.println();
		System.out.print("PC ");
		mPC.display(true);
		System.out.print("   ");

		System.out.print("IR ");
		mIR.display(true);
		System.out.print("   ");

		System.out.print("CC ");
		mCC.display(true);
		System.out.println("   ");
		for (int i = 0; i < MAX_REGISTERS; i++) {
			System.out.printf("R%d ", i);
			mRegisters[i].display(true);
			if (i % 3 == 2) {
				System.out.println();
			} else {
				System.out.print("   ");
			}
		}
		System.out.println();
		for (int i = 0; i < MAX_MEMORY; i++) {
			System.out.printf("%3d ", i);
			mMemory[i].display(true);
			if (i % 3 == 2) {
				System.out.println();
			} else {
				System.out.print("   ");
			}
		}
		System.out.println();
		System.out.println();
	}

	private void setCC(final int pValue) {
		this.mCC.setBits(new char[] {'0', '0', '0'});
		// character array implementation unprotected
		if (pValue < 0) 		this.mCC.getBits()[0] = '1';
		else if (pValue > 0) 	this.mCC.getBits()[2] = '1';
		else 					this.mCC.getBits()[1] = '1';
	}

	private void setCC(final BitString pBits) {
		this.setCC(pBits.getUnsignedValue());
	}

	private int getFirstRegOfIRBitString() {
		return this.mIR.substring(4, 3).getUnsignedValue();
	}

	private int getImmediateBits2sComp(final int pBitCount) {
		final int lMaxBits = 16;
		return this.mIR.substring(lMaxBits - pBitCount, pBitCount).get2sCompValue();
	}
}
