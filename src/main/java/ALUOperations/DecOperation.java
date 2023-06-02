package ALUOperations;

/**
 * Implements the decrement operation, returns A - 1
 */
public class DecOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        int result = operandA - 1;
        if (operandA == 0) // will overflow if decrementing 0
            return new OperationResult((short)result, true);
        return new OperationResult((short)result, false);
    }
}
