package ALUOperations;

/**
 * Implements the signed division operation, returns A / B
 */
public class DivOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA / operandB;
    }
}
