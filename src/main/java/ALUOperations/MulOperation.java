package ALUOperations;

/**
 * Implements the signed multiplication operation, returns A * B
 */
public class MulOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        try {
            int result = Math.multiplyExact(operandA, operandB);

            if (
                (isWord && result > Short.MAX_VALUE) ||
                (isWord && result < Short.MIN_VALUE) || 
                (!isWord && result > Byte.MAX_VALUE) ||
                (!isWord && result < Byte.MIN_VALUE)
            ) return new OperationResult((short) result, true);

            return new OperationResult((short) result, false);
        } catch (ArithmeticException e) {
            return new OperationResult((short) (operandA * operandB), true);
        }
    }
}
