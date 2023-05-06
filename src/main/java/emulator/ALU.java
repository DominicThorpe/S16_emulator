package emulator;

import ALUOperations.ALUOperation;

/**
 * Responsible for performing all arithmetic and logical operations within the CPU. Its operations must
 * implement the ALUOperation interface.
 * 
 * @author Dominic Thorpe
 * @see ALUOperation
 */
public class ALU {
    /** The CPU status register contained within the ALU */
    public final StatusRegister statusRegister = new StatusRegister();


    /**
     * Takes an ALUOperation and executes it on the given registers, placing the result in destination.
     * 
     * The register code dictates which parts of split registers should be used. If the register is not
     * a split register, then 0b1111 should be used for instructions that have 2 operands, 0b1100, if
     * it has 1 operand, and 0b0000 if it has none.
     * 
     * If the register is a split register, then set the upper bit to use the upper byte, and the lower
     * bit to use the lower byte. The first 2 bits represent the destination register and the second 2
     * represent the target register.
     * 
     * @param operation The operation to perform
     * @param destination The first operand and the destination of the result
     * @param target The second operand
     * @param meta Flags in the order: high, low, set_flags, signed, which denote if the high and/or
     * low byte of a split register should be used, if the operation should set the flags of the status
     * register, and if the operation is signed.
     */
    public void executeInstruction(ALUOperation operation, Register destination, Register target, byte meta) {
        boolean high = (meta & 0b1000) != 0;
        boolean low = (meta & 0b0100) != 0;
        
        int argA = getOperand(destination, high, low); 
        int argB = getOperand(target, high, low);

        int result = operation.performOperation(argA, argB);
        if (high && low)
            destination.setValue(result & 0xFFFF);
        else if (destination instanceof SplitRegister) {
            SplitRegister splitDest = (SplitRegister)destination;

            if (high)
                splitDest.setHighValue(result);
            else if (low)
                splitDest.setLowValue(result);
        }
    }


    /**
     * Gets the first operand for the operation from the registers, if the register is a SplitRegister
     * and the regCode == 0b10XX or 0b01XX, then only the top/bottom byte will be retreived from the 
     * register respectively.
     * 
     * @param register The destination register
     * @param high True if the high byte should be red
     * @param low True if the low byte should be read
     * @return The value in the register
     */
    public int getOperand(Register register, boolean high, boolean low) {
        // if destination is a split register get high and/or low byte depending on the flags
        if (register instanceof SplitRegister) { 
            SplitRegister splitDest = (SplitRegister)register;
            return (high ? splitDest.getHighValue() : 0) | (low ? splitDest.getLowValue() : 0);
        } 
        
        else if (high && low) // otherwise just get the whole contents
            return register.getValue();

        return 0;
    }
}
