package ALUOperations;

/**
 * Implements the logical NOT (aka complement) operation, returns ~A
 */
public class NotOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return ~operandA;
    }
}
