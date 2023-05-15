package ALUOperations;

/**
 * Implements the logical left shift bitwise operation, returns A {@literal <<} B
 */
public class SllOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        return new OperationResult((short) (operandA << operandB), false);
    }
}
