package ALUOperations;

/**
 * Implements the AND bitwise operation, returns A {@literal &} B
 */
public class AndOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        return new OperationResult((short) (operandA & operandB), false);
    }
}
