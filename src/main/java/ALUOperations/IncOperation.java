package ALUOperations;

/**
 * Implements the increment operation, returns A + 1
 */
public class IncOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA + 1;
    }
}
