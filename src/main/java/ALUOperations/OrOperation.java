package ALUOperations;

/**
 * Implements the OR bitwise operation, returns A | B
 */
public class OrOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        return new OperationResult((short) (operandA | operandB), false);
    }
}
