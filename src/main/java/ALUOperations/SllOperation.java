package ALUOperations;

/**
 * Implements the logical left shift bitwise operation, returns A {@literal <<} B
 */
public class SllOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA << operandB;
    }
}
