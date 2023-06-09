package ALUOperations;

/**
 * Implements the negation operation operation, returns -A
 */
public class NegOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        return new OperationResult((short) (-operandA), false);
    }
}
