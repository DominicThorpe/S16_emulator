package sim16_emulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
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
        registers.setRegister(0, (short) valueA, true, true);
        registers.setRegister(1, (short) valueB, true, true);

        alu.executeInstruction(
            operation, 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte)(signed ? 0b1111 : 0b1110)
        );
    }

    private void setAndExecuteSplit(ALUOperation operation, int valueA, int valueB, byte meta) {
        boolean high = (meta & 0b1000) == 0b1000;
        boolean low = (meta & 0b0100) == 0b0100;
        registers.setRegister(0, (short) 0, true, true);
        registers.setRegister(1, (short) 0, true, true);
        registers.setRegister(0, (short) valueA, high, low);
        registers.setRegister(1, (short) valueB, high, low);
        
        alu.executeInstruction(
            operation, 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            meta
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
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing signed addition zero no overflow
        setAndExecute(new ALUOperations.AddOperation(), 0, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertFalse(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing signed addition negative no overflow
        setAndExecute(new ALUOperations.AddOperation(), -30, 10, true);
        assertEquals(registers.getRegister(0, true, true), -20);
        assertEquals(registers.getRegister(1, true, true), 10);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        // testing signed addition negative no overflow = -1
        setAndExecute(new ALUOperations.AddOperation(), -1, 0, true);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertTrue(ALU.statusRegister.getNegative());

        setAndExecute(new ALUOperations.AddOperation(), -2, 1, true);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertTrue(ALU.statusRegister.getNegative());

        setAndExecute(new ALUOperations.AddOperation(), 1, -2, true);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertTrue(ALU.statusRegister.getNegative());

        // testing unsigned addition no carry 
        setAndExecute(new ALUOperations.AddOperation(), 0xB300, 0x0F33, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xC233);
        assertEquals(registers.getRegister(1, true, true), (short) 0x0F33);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing unsigned addition with carry 
        setAndExecute(new ALUOperations.AddOperation(), 0xB333, 0x8555, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0x3888);
        assertEquals(registers.getRegister(1, true, true), (short) 0x8555);
        assertTrue(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing unsigned addition zero 
        setAndExecute(new ALUOperations.AddOperation(), 0, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertFalse(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testAdditionSplitReg() {
        setAndExecuteSplit(new ALUOperations.AddOperation(), 50, 60, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), 110);
        assertEquals(registers.getRegister(1, false, true), 60);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        setAndExecuteSplit(new ALUOperations.AddOperation(), 40, 50, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), 0x5A00);
        assertEquals(registers.getRegister(1, true, false), 0x3200);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        setAndExecuteSplit(new ALUOperations.AddOperation(), 187, 165, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), 96);
        assertEquals(registers.getRegister(1, false, true), 165);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertTrue(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testAluIncrement() {
        // increment positive no overflow
        setAndExecute(new ALUOperations.IncOperation(), 0, 0, false);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertEquals(registers.getRegister(0, true, true), 1);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // increment with carry
        setAndExecute(new ALUOperations.IncOperation(), 0xFFFF, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testIncrementSplitReg() {
        setAndExecuteSplit(new ALUOperations.IncOperation(), 50, 0, (byte) 0b0110);
        assertEquals(registers.getRegister(0, false, true), 51);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        setAndExecuteSplit(new ALUOperations.IncOperation(), 0xFF, 0, (byte) 0b0110);
        assertEquals(registers.getRegister(0, false, true), 0);
        assertTrue(ALU.statusRegister.getCarry());

        setAndExecuteSplit(new ALUOperations.IncOperation(), 50, 0, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, false), 0x3300);
        assertFalse(ALU.statusRegister.getCarry());

        setAndExecuteSplit(new ALUOperations.IncOperation(), 0xFF, 0, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, false), 0);
        assertTrue(ALU.statusRegister.getCarry());
    }

    @Test
    public void testAluSubtraction() {
        // testing signed subtraction positive no overflow
        setAndExecute(new ALUOperations.SubOperation(), 40, 30, true);
        assertEquals(registers.getRegister(0, true, true), 10);
        assertEquals(registers.getRegister(1, true, true), 30);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing signed subtraction zero no overflow
        setAndExecute(new ALUOperations.SubOperation(), 40, 40, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 40);
        assertFalse(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing signed subtraction with overflow
        setAndExecute(new ALUOperations.SubOperation(), -30000, 20000, true);
        assertEquals(registers.getRegister(0, true, true), 15536);
        assertEquals(registers.getRegister(1, true, true), 20000);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertTrue(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing signed subtraction negative no overflow
        setAndExecute(new ALUOperations.SubOperation(), 30, 40, true);
        assertEquals(registers.getRegister(0, true, true), -10);
        assertEquals(registers.getRegister(1, true, true), 40);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        // testing unsigned subtraction no carry 
        setAndExecute(new ALUOperations.SubOperation(), 0xB300, 0x0F33, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xA3CD);
        assertEquals(registers.getRegister(1, true, true), (short) 0x0F33);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing unsigned subtraction with carry 
        setAndExecute(new ALUOperations.SubOperation(), -30000, 20000, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0x3CB0);
        assertEquals(registers.getRegister(1, true, true), 20000);
        assertTrue(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // testing unsigned subtraction zero 
        setAndExecute(new ALUOperations.SubOperation(), 0, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertFalse(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testSubtractionSplitReg() {
        setAndExecuteSplit(new ALUOperations.SubOperation(), 50, 60, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), 0x00F6);
        assertEquals(registers.getRegister(1, false, true), 60);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        setAndExecuteSplit(new ALUOperations.SubOperation(), 50, 40, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), 0x0A00);
        assertEquals(registers.getRegister(1, true, false), 0x2800);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        setAndExecuteSplit(new ALUOperations.SubOperation(), -120, 20, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), 0x0074);
        assertEquals(registers.getRegister(1, false, true), 20);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertTrue(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testAluDecrement() {
        // decrement positive no overflow
        setAndExecute(new ALUOperations.DecOperation(), 50, 0, false);
        assertEquals(registers.getRegister(0, true, true), 49);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
        
        // decrement with carry
        setAndExecute(new ALUOperations.DecOperation(), 0, 0, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFFFF);
        assertEquals(registers.getRegister(1, true, true), 0);
        assertTrue(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testDecrementSplitReg() {
        setAndExecuteSplit(new ALUOperations.DecOperation(), 50, 0, (byte) 0b0110);
        assertEquals(registers.getRegister(0, false, true), 49);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        setAndExecuteSplit(new ALUOperations.DecOperation(), 0, 0, (byte) 0b0110);
        assertEquals(registers.getRegister(0, false, true), 0x00FF);
        assertTrue(ALU.statusRegister.getCarry());

        setAndExecuteSplit(new ALUOperations.DecOperation(), 50, 0, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, false), 0x3100);
        assertFalse(ALU.statusRegister.getCarry());

        setAndExecuteSplit(new ALUOperations.DecOperation(), 0, 0, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, false), (short) 0xFF00);
        assertTrue(ALU.statusRegister.getCarry());
    }

    @Test
    public void testAluNegation() {
        // test negation of positive number
        setAndExecute(new ALUOperations.NegOperation(), 10, 0, true);
        assertEquals(registers.getRegister(0, true, true), -10);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        // test negation of negative number
        setAndExecute(new ALUOperations.NegOperation(), -10, 0, true);
        assertEquals(registers.getRegister(0, true, true), 10);
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getNegative());

        // test negation of zero
        setAndExecute(new ALUOperations.NegOperation(), 0, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testAluComplement() {
        // test complement non-zero
        setAndExecute(new ALUOperations.NotOperation(), 0x5555, 0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xAAAA);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        // test complement result zero
        setAndExecute(new ALUOperations.NotOperation(), -1, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());
    }

    @Test
    public void testUnaryBitwiseSplitReg() {
        // test negation of positive lower number
        setAndExecuteSplit(new ALUOperations.NegOperation(), 10, 0, (byte) 0b0111);
        assertEquals((byte) registers.getRegister(0, false, true), -10);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        // test negation of negative upper number
        setAndExecuteSplit(new ALUOperations.NegOperation(), -10, 0, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), (short) 0x0A00);
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getNegative());

        // test negation of zero
        setAndExecuteSplit(new ALUOperations.NegOperation(), 0, 0, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), 0);
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getNegative());

        // test complement lower non-zero
        setAndExecuteSplit(new ALUOperations.NotOperation(), 0x5555, 0, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), (short) 0x00AA);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertTrue(ALU.statusRegister.getNegative());

        // test complement upper non-zero
        setAndExecuteSplit(new ALUOperations.NotOperation(), -1, 0, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());
    }

    @Test
    public void testAluAnd() {
        // result positive
        setAndExecute(new ALUOperations.AndOperation(), 0x5555, 0xF0F0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x5050);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // result negative
        setAndExecute(new ALUOperations.AndOperation(), 0xAAAA, 0xF0F0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xA0A0);
        assertTrue(ALU.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.AndOperation(), 0xAAAA, 0x0000, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());
    }

    @Test
    public void testAluOr() {
        // result positive
        setAndExecute(new ALUOperations.OrOperation(), 0x5555, 0x0A0A, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x5F5F);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // result negative
        setAndExecute(new ALUOperations.OrOperation(), 0x5555, 0xFF00, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFF55);
        assertTrue(ALU.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.OrOperation(), 0x0000, 0x0000, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());
    }

    @Test
    public void testAluXor() {
        // result positive
        setAndExecute(new ALUOperations.XorOperation(), 0xAAAA, 0xFFFF, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x5555);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // result negative
        setAndExecute(new ALUOperations.XorOperation(), 0x5555, 0xFF00, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xAA55);
        assertTrue(ALU.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.XorOperation(), 0xFFFF, 0xFFFF, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());
    }

    @Test
    public void testLogicalBinarySplitReg() {
        // AND result positive
        setAndExecuteSplit(new ALUOperations.AndOperation(), 0x0055, 0x0F0, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), (short) 0x0050);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // AND result negative
        setAndExecuteSplit(new ALUOperations.AndOperation(), 0x00AA, 0x00F0, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), (short) 0xA000);
        assertTrue(ALU.statusRegister.getNegative());

        // AND result zero
        setAndExecuteSplit(new ALUOperations.AndOperation(), 0xAAAA, 0x0000, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), 0);
        assertTrue(ALU.statusRegister.getZero());

        // OR lower byte
        setAndExecuteSplit(new ALUOperations.OrOperation(), 0x00AA, 0x00FA, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), (short) 0x00FA);

        // OR upper byte
        setAndExecuteSplit(new ALUOperations.OrOperation(), 0x00AA, 0x00F0, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), (short) 0xFA00);

        // XOR lower byte
        setAndExecuteSplit(new ALUOperations.XorOperation(), 0x00AA, 0x000F, (byte) 0b0111);
        assertEquals(registers.getRegister(0, false, true), (short) 0x00A5);

        // XOR upper byte
        setAndExecuteSplit(new ALUOperations.XorOperation(), 0x00AA, 0x00F0, (byte) 0b1011);
        assertEquals(registers.getRegister(0, true, false), (short) 0x5A00);
    }

    @Test
    public void testShiftLeftLogical() {
        // result non-zero
        setAndExecute(new ALUOperations.SllOperation(), 100, 2, false);
        assertEquals(registers.getRegister(0, true, true), 400);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.SllOperation(), 100, 15, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertFalse(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testShiftRightLogical() {
        // result non-zero
        setAndExecute(new ALUOperations.SrlOperation(), 100, 2, false);
        assertEquals(registers.getRegister(0, true, true), 25);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.SrlOperation(), 100, 8, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertFalse(ALU.statusRegister.getCarry());
        assertTrue(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testShiftRightArithmetic() {
        // result non-zero
        setAndExecute(new ALUOperations.SraOperation(), -100, 2, false);
        assertEquals(registers.getRegister(0, true, true), -25);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // result zero
        setAndExecute(new ALUOperations.SraOperation(), -100, 8, false);
        assertEquals(registers.getRegister(0, true, true), -1);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testShiftsSplitReg() {
        // shift left lower result non-zero
        setAndExecuteSplit(new ALUOperations.SllOperation(), 10, 2, (byte) 0b0110);
        assertEquals(registers.getRegister(0, false, true), 40);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // shift left upper
        setAndExecuteSplit(new ALUOperations.SllOperation(), 10, 2, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, false), (short) 0x2800);

        // shift left upper with overflow
        setAndExecuteSplit(new ALUOperations.SllOperation(), 96, 2, (byte) 0b0110);
        assertEquals(registers.getRegister(0, true, true), (short) 0x0080);

        // shift right logical lower result non-zero
        setAndExecuteSplit(new ALUOperations.SrlOperation(), 16, 2, (byte) 0b0110);
        assertEquals(registers.getRegister(0, false, true), 4);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // shift right logical upper
        setAndExecuteSplit(new ALUOperations.SrlOperation(), 16, 2, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, false), (short) 0x0400);

        // shift right logical upper with overflow
        setAndExecuteSplit(new ALUOperations.SrlOperation(), 0x0007, 2, (byte) 0b1010);
        assertEquals(registers.getRegister(0, true, true), (short) 0x0100);
    }

    @Test
    public void testAluMultiplication() {
        // test signed multiplication positive no overflow
        setAndExecute(new ALUOperations.MulOperation(), 100, 8, true);
        assertEquals(registers.getRegister(0, true, true), 800);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // test signed multiplication zero
        setAndExecute(new ALUOperations.MulOperation(), 100, 0, true);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());

        // test signed multiplication negative no overflow
        setAndExecute(new ALUOperations.MulOperation(), -100, 10, true);
        assertEquals(registers.getRegister(0, true, true), -1000);
        assertTrue(ALU.statusRegister.getNegative());

        // test unsigned multiplication positive
        setAndExecute(new ALUOperations.MulOperation(), 100, 8, false);
        assertEquals(registers.getRegister(0, true, true), 800);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // test unsigned multiplication zero
        setAndExecute(new ALUOperations.MulOperation(), 100, 0, false);
        assertEquals(registers.getRegister(0, true, true), 0);
        assertTrue(ALU.statusRegister.getZero());

        // test unsigned multiplication carry
        setAndExecute(new ALUOperations.MulOperation(), -100, 10, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFC18);
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testAluDivision() {
        // test signed division no overflow
        setAndExecute(new ALUOperations.DivOperation(), 100, 5, true);
        assertEquals(registers.getRegister(0, true, true), 20);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // test signed division negative dividend
        setAndExecute(new ALUOperations.DivOperation(), -100, 5, true);
        assertEquals(registers.getRegister(0, true, true), -20);
        assertTrue(ALU.statusRegister.getNegative());

        // test signed division negative divisor
        setAndExecute(new ALUOperations.DivOperation(), 100, -5, true);
        assertEquals(registers.getRegister(0, true, true), -20);
        assertTrue(ALU.statusRegister.getNegative());

        // test unsigned division no overflow
        setAndExecute(new ALUOperations.DivOperation(), 100, 5, false);
        assertEquals(registers.getRegister(0, true, true), 20);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // test unsigned division negative dividend
        setAndExecute(new ALUOperations.DivOperation(), -100, 5, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFFEC);
        assertFalse(ALU.statusRegister.getNegative());

        // test unsigned division negative divisor
        setAndExecute(new ALUOperations.DivOperation(), 100, -5, false);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFFEC);
        assertFalse(ALU.statusRegister.getNegative());
    }

    @Test
    public void testSignExtension() {
        // test sign extend positive
        setAndExecute(new ALUOperations.SignExtOperation(), 0xF755, 0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0x0055);
        assertFalse(ALU.statusRegister.getCarry());
        assertFalse(ALU.statusRegister.getZero());
        assertFalse(ALU.statusRegister.getOverflow());
        assertFalse(ALU.statusRegister.getNegative());

        // test sign extend negative
        setAndExecute(new ALUOperations.SignExtOperation(), 0x55D5, 0, true);
        assertEquals(registers.getRegister(0, true, true), (short) 0xFFD5);
        assertTrue(ALU.statusRegister.getNegative());
    }

    @Test
    public void testAddCarry() {
        // test without carry
        setAndExecute(new ALUOperations.AddOperation(), 200, 100, false);
        alu.executeInstruction(
            new ALUOperations.AddCarryOperation(), 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte) 0b1110
        );
        assertEquals(registers.getRegister(0, true, true), 300);

        // test with carry
        setAndExecute(new ALUOperations.AddOperation(), Short.MAX_VALUE, 1, false);
        alu.executeInstruction(
            new ALUOperations.AddCarryOperation(), 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte) 0b1110
        );
        assertEquals(registers.getRegister(0, true, true), Short.MIN_VALUE + 1);

        // test with overflow
        setAndExecute(new ALUOperations.AddOperation(), Short.MAX_VALUE, 1, true);
        alu.executeInstruction(
            new ALUOperations.AddCarryOperation(), 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte) 0b1110
        );
        assertEquals(registers.getRegister(0, true, true), Short.MIN_VALUE);
    }

    @Test
    public void testSubCarry() {
        // test without carry
        setAndExecute(new ALUOperations.SubOperation(), 300, 100, false);
        alu.executeInstruction(
            new ALUOperations.SubCarryOperation(), 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte) 0b1110
        );
        assertEquals(registers.getRegister(0, true, true), 200);

        // test with carry
        setAndExecute(new ALUOperations.SubOperation(), Short.MIN_VALUE, 1, false);
        alu.executeInstruction(
            new ALUOperations.SubCarryOperation(), 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte) 0b1110
        );
        assertEquals(registers.getRegister(0, true, true), Short.MAX_VALUE - 1);

        // test with overflow
        setAndExecute(new ALUOperations.SubOperation(), Short.MIN_VALUE, 1, true);
        alu.executeInstruction(
            new ALUOperations.SubCarryOperation(), 
            registers.getRegisterObject(0), 
            registers.getRegisterObject(1), 
            (byte) 0b1110
        );
        assertEquals(registers.getRegister(0, true, true), Short.MAX_VALUE);
    }
}
