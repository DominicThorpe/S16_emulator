package ALUOperations;

/**
 * Implements the logical right shift operation, returns A >>> B
 */
public class SrlOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA >>> operandB;
    }
}
