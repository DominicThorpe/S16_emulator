package ALUOperations;

/**
 * Implements the decrement operation, returns A - 1
 */
public class DecOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA - 1;
    }
}
