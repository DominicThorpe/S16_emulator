package sim16_emulator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import emulator.CPU;

public class TestCtrlUnit {
    @Test
    public void testConditionalBranch() {
        // test branch equal, not equal, greater than, less than, greater equal, less equal
        CPU cpu = new CPU("src/test/sse/test_branching.sse");
        assertEquals(cpu.regFile.getRegister(2, true, true), 100);
    }

    @Test
    public void testUnconditionalJump() {
        // test the JUMP instruction
        CPU cpu = new CPU("src/test/sse/test_jump.sse");
        assertEquals(cpu.regFile.getRegister(1, true, true), 100);
        assertEquals(cpu.regFile.getRegister(2, true, true), 0);
        assertEquals(cpu.regFile.getRegister(3, true, true), 400);
    }
}
