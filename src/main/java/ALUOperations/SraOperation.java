package ALUOperations;

/**
 * Implements the arithmetic right shift bitwise operation, returns A {@literal >>} B
 */
public class SraOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        return new OperationResult((short) (operandA >> operandB), false);
    }
}
