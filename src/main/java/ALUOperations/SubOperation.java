package ALUOperations;

/**
 * Implements the subtraction operation, returns A - B
 */
public class SubOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA - operandB;
    }
}
