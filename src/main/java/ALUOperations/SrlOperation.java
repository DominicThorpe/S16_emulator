package ALUOperations;

/**
 * Implements the logical right shift operation, returns A >>> B
 */
public class SrlOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        return new OperationResult((short) (operandA >>> operandB), false);
    }
}
