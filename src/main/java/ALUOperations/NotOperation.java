package ALUOperations;

/**
 * Implements the logical NOT (aka complement) operation, returns ~A
 */
public class NotOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        return new OperationResult((short) ~operandA, false);
    }
}
