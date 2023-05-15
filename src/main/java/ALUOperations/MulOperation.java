package ALUOperations;

/**
 * Implements the signed multiplication operation, returns A * B
 */
public class MulOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        try {
            int result = Math.multiplyExact(operandA, operandB);
            return new OperationResult((short) result, false);
        } catch (ArithmeticException e) {
            return new OperationResult((short) (operandA * operandB), true);
        }
    }
}
