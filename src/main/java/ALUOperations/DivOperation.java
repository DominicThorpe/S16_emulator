package ALUOperations;

/**
 * Implements the signed division operation, returns A / B
 */
public class DivOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        return new OperationResult((short) (operandA / operandB), false);
    }
}
