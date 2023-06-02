package ALUOperations;

/**
 * Implements the increment operation, returns A + 1
 */
public class IncOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        int result = operandA + 1;

        // will overflow if incrementing 0xFFFF and is word length or 0xFF and is byte length
        if ((isWord && operandA >= Short.MAX_VALUE) || (!isWord && operandA >= Byte.MAX_VALUE) || operandA == -1)
            return new OperationResult((short) result, true);
        return new OperationResult((short)result, false);
    }
}
