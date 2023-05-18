package sim16_emulator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import emulator.CPU;

public class TestCtrlUnit {
    @Test
    public void testUnconditionalJump() {
        CPU cpu = new CPU("src/test/sse/test_jump.sse");
        assertEquals(cpu.regFile.getRegister(1, true, true), 100);
        assertEquals(cpu.regFile.getRegister(2, true, true), 0);
        assertEquals(cpu.regFile.getRegister(3, true, true), 400);
    }
}
