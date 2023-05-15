package ALUOperations;

/**
 * Implements the increment operation, returns A + 1
 */
public class IncOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        int result = operandA + 1;
        if (operandA == -1) // will overflow if incrementing 0xFFFF
            return new OperationResult((short)result, true);
        return new OperationResult((short)result, false);
    }
}
