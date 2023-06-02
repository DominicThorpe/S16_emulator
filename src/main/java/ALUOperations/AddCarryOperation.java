package ALUOperations;

import emulator.ALU;

/**
 * Implements carry addition on the ALU, returns operand A + 1 if the carry flag is set, else A + 0
 */
public class AddCarryOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        int result = ALU.statusRegister.getCarry() ? operandA + 1 : operandA;
        if (
            (isWord && (result < Short.MIN_VALUE || result > Short.MAX_VALUE)) ||
            (!isWord && (result < Byte.MIN_VALUE || result > Byte.MAX_VALUE))
        ) return new OperationResult((short) result, true);

        return new OperationResult((short) result, false);
    }
}
