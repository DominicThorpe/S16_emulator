package ALUOperations;

/**
 * Implements the AND bitwise operation, returns A {@literal &} B
 */
public class AndOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA & operandB;
    }
}
