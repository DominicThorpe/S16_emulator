package sim16_emulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import ALUOperations.ALUOperation;
import emulator.ALU;
import emulator.RegisterFile;

/**
 * Unit tests for the Arithmetic Logic Unit.
 */
public class TestALU 
{
    public final ALU alu = new ALU();
    public final RegisterFile registers = new RegisterFile();


    private void setAndExecute(ALUOperation operation, int valueA, int valueB, boolean signed) {
        registers.setRegister(0, valueA, true, true);
        registers.setRegister(1, valueB, true, true);

        alu.executeInstruction(
            operation, 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte)(signed ? 0b1111 : 0b1110)
        );
    }

    /**
     * Test that the ALU operations return the correct values.
     */
    @Test
    public void testAluAddition() {
        // testing signed addition positive no overflow
        setAndExecute(new ALUOperations.AddOperation(), 30, 40, true);
        assertEquals(registers.getRegister(0, true, true), 70);
        assertEquals(registers.getRegister(1, true, true), 40);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing signed addition zero no overflow
        setAndExecute(new ALUOperations.AddOperation(), 0, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertFalse(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing signed addition negative no overflow
        setAndExecute(new ALUOperations.AddOperation(), -30, 10, true);
        assertEquals(registers.getRegister(0, true, true), -20);
        assertEquals(registers.getRegister(1, true, true), 10);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertTrue(alu.statusRegister.getNegative());

        // testing signed addition negative no overflow = -1
        setAndExecute(new ALUOperations.AddOperation(), -1, 0, true);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertTrue(alu.statusRegister.getNegative());

        setAndExecute(new ALUOperations.AddOperation(), -2, 1, true);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertTrue(alu.statusRegister.getNegative());

        setAndExecute(new ALUOperations.AddOperation(), 1, -2, true);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertTrue(alu.statusRegister.getNegative());

        // testing unsigned addition no carry 
        setAndExecute(new ALUOperations.AddOperation(), 0xB300, 0x0F33, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xC233);
        assertEquals(registers.getRegister(1, true, true), (short) 0x0F33);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing unsigned addition with carry 
        setAndExecute(new ALUOperations.AddOperation(), 0xB333, 0x8555, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0x3888);
        assertEquals(registers.getRegister(1, true, true), (short) 0x8555);
        assertTrue(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing unsigned addition zero 
        setAndExecute(new ALUOperations.AddOperation(), 0, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertFalse(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testAluIncrement() {
        // increment positive no overflow
        setAndExecute(new ALUOperations.IncOperation(), 0, 0, false);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertEquals(registers.getRegister(0, true, true), 1);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // increment with carry
        setAndExecute(new ALUOperations.IncOperation(), 0xFFFF, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testAluSubtraction() {
        // testing signed subtraction positive no overflow
        setAndExecute(new ALUOperations.SubOperation(), 40, 30, true);
        assertEquals(registers.getRegister(0, true, true), 10);
        assertEquals(registers.getRegister(1, true, true), 30);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing signed subtraction zero no overflow
        setAndExecute(new ALUOperations.SubOperation(), 40, 40, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 40);
        assertFalse(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing signed subtraction with overflow
        setAndExecute(new ALUOperations.SubOperation(), -30000, 20000, true);
        assertEquals(registers.getRegister(0, true, true), 15536);
        assertEquals(registers.getRegister(1, true, true), 20000);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertTrue(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing signed subtraction negative no overflow
        setAndExecute(new ALUOperations.SubOperation(), 30, 40, true);
        assertEquals(registers.getRegister(0, true, true), -10);
        assertEquals(registers.getRegister(1, true, true), 40);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertTrue(alu.statusRegister.getNegative());

        // testing unsigned subtraction no carry 
        setAndExecute(new ALUOperations.SubOperation(), 0xB300, 0x0F33, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xA3CD);
        assertEquals(registers.getRegister(1, true, true), (short) 0x0F33);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing unsigned subtraction with carry 
        setAndExecute(new ALUOperations.SubOperation(), -30000, 20000, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0x3CB0);
        assertEquals(registers.getRegister(1, true, true), 20000);
        assertTrue(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // testing unsigned subtraction zero 
        setAndExecute(new ALUOperations.SubOperation(), 0, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertFalse(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testAluDecrement() {
        // decrement positive no overflow
        setAndExecute(new ALUOperations.DecOperation(), 50, 0, false);
        assertEquals(registers.getRegister(0, true, true), 49);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
        
        // decrement with carry
        setAndExecute(new ALUOperations.DecOperation(), 0, 0, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFFFF);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertTrue(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testAluNegation() {
        // test negation of positive number
        setAndExecute(new ALUOperations.NegOperation(), 10, 0, true);
        assertEquals(registers.getRegister(0, true, true), -10);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertTrue(alu.statusRegister.getNegative());

        // test negation of negative number
        setAndExecute(new ALUOperations.NegOperation(), -10, 0, true);
        assertEquals(registers.getRegister(0, true, true), 10);
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getNegative());

        // test negation of zero
        setAndExecute(new ALUOperations.NegOperation(), 0, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testAluComplement() {
        // test complement non-zero
        setAndExecute(new ALUOperations.NotOperation(), 0x5555, 0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xAAAA);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertTrue(alu.statusRegister.getNegative());

        // test complement result zero
        setAndExecute(new ALUOperations.NotOperation(), -1, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(alu.statusRegister.getZero());
    }

    @Test
    public void testAluAnd() {
        // result positive
        setAndExecute(new ALUOperations.AndOperation(), 0x5555, 0xF0F0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x5050);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // result negative
        setAndExecute(new ALUOperations.AndOperation(), 0xAAAA, 0xF0F0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xA0A0);
        assertTrue(alu.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.AndOperation(), 0xAAAA, 0x0000, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(alu.statusRegister.getZero());
    }

    @Test
    public void testAluOr() {
        // result positive
        setAndExecute(new ALUOperations.OrOperation(), 0x5555, 0x0A0A, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x5F5F);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // result negative
        setAndExecute(new ALUOperations.OrOperation(), 0x5555, 0xFF00, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFF55);
        assertTrue(alu.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.OrOperation(), 0x0000, 0x0000, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(alu.statusRegister.getZero());
    }

    @Test
    public void testAluXor() {
        // result positive
        setAndExecute(new ALUOperations.XorOperation(), 0xAAAA, 0xFFFF, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x5555);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // result negative
        setAndExecute(new ALUOperations.XorOperation(), 0x5555, 0xFF00, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xAA55);
        assertTrue(alu.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.XorOperation(), 0xFFFF, 0xFFFF, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(alu.statusRegister.getZero());
    }

    @Test
    public void testShiftLeftLogical() {
        // result non-zero
        setAndExecute(new ALUOperations.SllOperation(), 100, 2, false);
        assertEquals(registers.getRegister(0, true, true), 400);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.SllOperation(), 100, 15, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertFalse(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testShiftRightLogical() {
        // result non-zero
        setAndExecute(new ALUOperations.SrlOperation(), 100, 2, false);
        assertEquals(registers.getRegister(0, true, true), 25);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.SrlOperation(), 100, 8, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertFalse(alu.statusRegister.getCarry());
        assertTrue(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }

    @Test
    public void testShiftRightArithmetic() {
        // result non-zero
        setAndExecute(new ALUOperations.SraOperation(), -100, 2, false);
        assertEquals(registers.getRegister(0, true, true), -25);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.SraOperation(), -100, 8, false);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertFalse(alu.statusRegister.getCarry());
        assertFalse(alu.statusRegister.getZero());
        assertFalse(alu.statusRegister.getOverflow());
        assertFalse(alu.statusRegister.getNegative());
    }
}
