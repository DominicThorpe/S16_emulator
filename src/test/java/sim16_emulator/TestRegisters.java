package sim16_emulator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import emulator.RegisterFile;

/**
 * Unit tests for the registers and register file
 */
public class TestRegisters {
    @Test
    public void testRegFileInit() {
        RegisterFile registerFile = new RegisterFile();
        assertEquals(registerFile.getRegister(0, true, true), 0);
        assertEquals(registerFile.getRegister(1, true, true), 0);
        assertEquals(registerFile.getRegister(2, true, true), 0);
        assertEquals(registerFile.getRegister(3, true, true), 0);

        assertEquals(registerFile.getRegister(4, true, true), 0);
        assertEquals(registerFile.getRegister(5, true, true), (short) 0xFFFF);
        assertEquals(registerFile.getRegister(6, true, true), (short) 0xFFFF);
        assertEquals(registerFile.getRegister(7, true, true), (short) 0xFFFF);
    }

    @Test
    public void testSetWholeValues() {
        RegisterFile registerFile = new RegisterFile();

        registerFile.setRegister(0, 20, true, true);
        assertEquals(registerFile.getRegister(0, true, true), 20);

        registerFile.setRegister(0, -20, true, true);
        assertEquals(registerFile.getRegister(0, true, true), -20);

        registerFile.setRegister(0, 32767, true, true);
        assertEquals(registerFile.getRegister(0, true, true), 32767);

        registerFile.setRegister(0, 32768, true, true);
        assertEquals(registerFile.getRegister(0, true, true), -32768);

        registerFile.setRegister(0, -32768, true, true);
        assertEquals(registerFile.getRegister(0, true, true), -32768);

        registerFile.setRegister(0, -32769, true, true);
        assertEquals(registerFile.getRegister(0, true, true), 32767);
    }

    @Test
    public void testSetSplitRegisters() {
        RegisterFile registerFile = new RegisterFile();

        registerFile.setRegister(0, 20, false, true);
        assertEquals(registerFile.getRegister(0, false, true), 20);

        registerFile.setRegister(0, -20, false, true);
        assertEquals((byte)registerFile.getRegister(0, false, true), -20);

        registerFile.setRegister(0, 127, false, true);
        assertEquals(registerFile.getRegister(0, false, true), 127);

        registerFile.setRegister(0, 128, false, true);
        assertEquals((byte)registerFile.getRegister(0, false, true), -128);

        registerFile.setRegister(0, -128, false, true);
        assertEquals((byte)registerFile.getRegister(0, false, true), -128);

        registerFile.setRegister(0, -129, false, true);
        assertEquals(registerFile.getRegister(0, false, true), 127);
    }
}
