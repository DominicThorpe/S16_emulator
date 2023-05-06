package ALUOperations;

/**
 * Implements the OR bitwise operation, returns A | B
 */
public class OrOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA | operandB;
    }
}
