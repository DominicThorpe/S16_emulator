package ALUOperations;

/**
 * Implements the negation operation operation, returns -A
 */
public class NegOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return -operandA;
    }
}
