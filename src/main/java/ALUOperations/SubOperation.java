package ALUOperations;

/**
 * Implements the subtraction operation, returns A - B
 */
public class SubOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        int result = operandA - operandB;
        if (result < Short.MIN_VALUE || result > Short.MAX_VALUE)
            return new OperationResult((short)result, true);
        return new OperationResult((short)result, false);
    }
}
