package simulator;
/*
 * Unit tests for the simulator.Computer class.
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Alan Fowler
 * @author Roman Bureacov
 * @version 1.3
 */
class ComputerTest {

	private static final String HALT_CODE = "1111000000100101";

	// An instance of the simulator.Computer class to use in the tests.
	private Computer myComputer;

	@BeforeEach
	void setUp() {
		myComputer = new Computer();
	}
	
	/*
	 * NOTE:
	 * Programs in unit tests should ideally have one instruction per line
	 * with a comment for each line.
	 */

	/**
	 * Test method for {@link simulator.Computer#executeBranch()}.
	 */
	@Test
	void testExecuteBranch() {
		final String[] lBranchTest = {
				"0001000000100010", // ADD	R0 <- 0 + 2
				"0101111111100000", // AND 	R7 <- 0
				"0001111111100001",	// ADD	R7 <- + 1 (set some arbitrary condition code)
				"0000111000000001",	// BR	nzp PC + 1
				"0101000000100000", // AND	R0 <- 0
				"0101111111100000", // AND 	R7 <- 0
				"0001111111111111", // ADD	R7 <- + #-1
				"0000111000000001",	// BR	nzp PC + 1
				"0101000000100000", // AND	R0 <- 0
				"0101111111100000", // AND 	R7 <- 0
				"0000111000000001",	// BR	nzp PC + 1
				"0101000000100000", // AND	R0 <- 0
				HALT_CODE
		};

		this.myComputer.loadMachineCode(lBranchTest);
		this.myComputer.execute();

		assertEquals(
				2,
				this.myComputer.getRegisters()[0].get2sCompValue(),
				"Branch instruction did not execute properly"
		);
	}

	@Test
	void testExecuteBranchP() {
		final String[] lBranchTest = {
				"0001000000101111",	// ADD	R0 <- + #15
				"0000001000000010",	// BR	p PC + 2
				"0101000000100000",	// AND	R0 <- 0
				"0101000000100000",	// AND	R0 <- 0
				HALT_CODE
		};
		this.myComputer.loadMachineCode(lBranchTest);
		this.myComputer.execute();

		assertEquals(
				15,
				this.myComputer.getRegisters()[0].get2sCompValue(),
				"Branch did not jump properly on code P"
		);
	}

	@Test
	void testExecuteBranchZ() {
		final String[] lBranchTest = {
				"0101000000100000",	// AND	R0 <- 0
				"0000010000000010",	// BR	z PC + 2
				"0001000000101111",	// ADD	R0 <- + #15
				"0001000000101111",	// ADD	R0 <- + #15
				HALT_CODE
		};
		this.myComputer.loadMachineCode(lBranchTest);
		this.myComputer.execute();

		assertEquals(
				0,
				this.myComputer.getRegisters()[0].get2sCompValue(),
				"Branch did not jump properly on code Z"
		);
	}

	@Test
	void testExecuteBranchN() {
		final String[] lBranchTest = {
				"0001000000111111",	// ADD	R0 <- + -1
				"0000100000000010",	// BR	n PC + 2
				"0101000000100000",	// AND	R0 <- 0
				"0101000000100000",	// AND	R0 <- 0
				HALT_CODE
		};
		this.myComputer.loadMachineCode(lBranchTest);
		this.myComputer.execute();

		assertEquals(
				-1,
				this.myComputer.getRegisters()[0].get2sCompValue(),
				"Branch did not jump properly on code N"
		);
	}

	@Test
	void testExecuteBranch000() {
		final String[] lBranchTest = {
				"0001000000111111",	// ADD	R0 <- + -1
				"0000000000000010",	// BR	000 PC + 2
				"0001000000101111",	// ADD	R0 <- + #15
				"0001000000101111",	// ADD	R0 <- + #15
				HALT_CODE
		};
		this.myComputer.loadMachineCode(lBranchTest);
		this.myComputer.execute();

		assertEquals(
				29,
				this.myComputer.getRegisters()[0].get2sCompValue(),
				"Branch did not jump properly on code N"
		);
	}

	@Test
	void testExecuteBack() {
		final String[] lBranchTest = {
				// looping program, 3*5
				"0101000000100000", // AND	R0 <- 0 (init)
				"0101001001100000", // AND	R1 <- 0
				"0101010010100000", // AND	R2 <- 0
				"0001000000100011", // ADD	R0 <- 3 (set up)
				"0001001001100101", // ADD	R1 <- 5
				"0001010010000001",	// ADD	R2 <- + R1
				"0001000000111111", // ADD	R0 <- + #-1
				"0000001111111101", // BR	p PC - 3
				HALT_CODE
		};

		this.run(lBranchTest);

		assertEquals(
				15,
				this.myComputer.getRegisters()[2].get2sCompValue(),
				"branch did not work in multiplication algorithm"
		);
	}

	/**
	 * Test method for {@link simulator.Computer#executeLoad()}.
	 */
	@Test
	void testExecuteLoad() {
		final String[] lLoadTest = {
				"0000000000000010",	// 2 in memory
				"0010000000000010",	// LD	R0 <- PC + 2
				"0010001111111101",	// LD	R1 <- PC - 3
				HALT_CODE,
				"1111111111111111", // -1 in memory
		};

		this.myComputer.loadMachineCode(lLoadTest);
		this.myComputer.execute();

		assertEquals(
				-1,
				this.myComputer.getRegisters()[0].get2sCompValue(),
				"Register 0 did not load PC + 2"
		);
		assertEquals(
				2,
				this.myComputer.getRegisters()[1].get2sCompValue(),
				"Register 1 did not load PC - 3"
		);
	}
	
	/**
	 * Test method for {@link simulator.Computer#executeStore()}.
	 */
	@Test
	void testExecuteStore() {
		final String[] lStoreTest = {
				"0001000000101111",	//	ADD	R0 <- 0 + #15
				"0001001001111110", //	ADD R1 <- 1 + #-2
				"0011001111111111",	//	ST	R1 PC - 1
				"0011000000000001", //	ST	R0 PC + 1
				HALT_CODE
		};

		this.myComputer.loadMachineCode(lStoreTest);
		this.myComputer.execute();

		assertEquals(
				15,
				this.myComputer.getMemory()[5].get2sCompValue(),
				"store did not store information at PC + 1"
		);
		assertEquals(
				-1,
				this.myComputer.getMemory()[2].get2sCompValue(),
				"store did not store information at PC - 1"
		);
	}

	/**
	 * Test method for {@link simulator.Computer#executeAnd()}.
	 */
	@Test
	void testExecuteAnd() {
		final String[] lAndTest = {
				"0001000000101111",	// ADD	R0 <- 0 + #15
				"0101001001100000",	// AND	R1 <- 0
				"0001001001101110",	// ADD	R1 <- + #14
				"0101010000000001", // AND	R2 <- R0 AND R1
				HALT_CODE
		};

		this.run(lAndTest);

		assertEquals(
				0b01110,
				this.myComputer.getRegisters()[2].get2sCompValue(),
				"R2 did not receive the anded product of R0 and R1"
		);
	}

	/**
	 * Test method for {@link simulator.Computer#executeNot()}.
	 */
	@Test
	void testExecuteNot5() {
	
		//myComputer.display();
		
		// NOTE: R5 contains #5 initially when the simulator.Computer is instantiated
		// So, iF we execute R4 <- NOT R5, then R4 should contain 1111 1111 1111 1010    (-6)
		// AND CC should be 100
		
		String program[] = {
			"1001100101111111",    // R4 <- NOT R5
			"1111000000100101"     // TRAP - vector x25 - HALT
		};
		
		myComputer.loadMachineCode(program);
		myComputer.execute();
		
		assertEquals(-6, myComputer.getRegisters()[4].get2sCompValue());
		
		// Check that CC was set correctly
		BitString expectedCC = new BitString();
		expectedCC.setBits("100".toCharArray());
		assertEquals(expectedCC.get2sCompValue(), myComputer.getCC().get2sCompValue());
		
		//myComputer.display();
	}

	/**
	 * Test method for {@link simulator.Computer#executeAdd()}. <br>
	 * Computes 2 + 2. R0 <- R2 + R2
	 */
	@Test
	void testExecuteAddR2PlusR2() {
		
		String[] program =
			{"0001000010000010",  // R0 <- R2 + R2 (#4)
		     "1111000000100101"}; // HALT
		
		myComputer.loadMachineCode(program);
		myComputer.execute();

		assertEquals(4, myComputer.getRegisters()[0].get2sCompValue());
		
		// Check that CC was set correctly
		BitString expectedCC = new BitString();
		expectedCC.setBits("001".toCharArray());
		assertEquals(expectedCC.get2sCompValue(), myComputer.getCC().get2sCompValue());
	}
	
	/**
	 * Test method for {@link simulator.Computer#executeAdd()}. <br>
	 * Computes 2 + 3. R0 <- R2 + #3
	 */
	@Test
	void testExecuteAddR2PlusImm3() {
		
		String[] program =
			{"0001000010100011",  // R0 <- R2 + #3
		     "1111000000100101"}; // HALT
		
		myComputer.loadMachineCode(program);
		myComputer.execute();

		assertEquals(5, myComputer.getRegisters()[0].get2sCompValue());
		
		// Check that CC was set correctly
		BitString expectedCC = new BitString();
		expectedCC.setBits("001".toCharArray());
		assertEquals(expectedCC.get2sCompValue(), myComputer.getCC().get2sCompValue());
	}
	
	/**
	 * Test method for {@link simulator.Computer#executeAdd()}. <br>
	 * Computes 2 - 3. R0 <- R2 + #-3
	 */
	@Test
	void testExecuteAddR2PlusImmNeg3() {
		
		String[] program =
			{"0001000010111101",  // R0 <- R2 + #-3
		     "1111000000100101"}; // HALT
		
		myComputer.loadMachineCode(program);
		myComputer.execute();

		assertEquals(-1, myComputer.getRegisters()[0].get2sCompValue());
		
		// Check that CC was set correctly
		BitString expectedCC = new BitString();
		expectedCC.setBits("100".toCharArray());
		assertEquals(expectedCC.get2sCompValue(), myComputer.getCC().get2sCompValue());
	}

	private void run(final String... pMachineCode) {
		this.myComputer.loadMachineCode(pMachineCode);
		this.myComputer.execute();
	}
}
