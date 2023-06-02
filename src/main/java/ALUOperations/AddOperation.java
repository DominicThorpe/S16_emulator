package ALUOperations;


/**
 * Implements addition on the ALU, returns operand A + operand B
 */
public class AddOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        int result = operandA + operandB;
        if (isWord && (result < Short.MIN_VALUE || result > Short.MAX_VALUE))
            return new OperationResult((short) (operandA + operandB), true);
        else if (!isWord && (result < Byte.MIN_VALUE || result > Byte.MAX_VALUE))
            return new OperationResult((short) (byte) (operandA + operandB), true);

        return new OperationResult((short) (operandA + operandB), false);
    }
}
