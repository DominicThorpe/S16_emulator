package ALUOperations;

/**
 * Implements the arithmetic right shift bitwise operation, returns A {@literal >>} B
 */
public class SraOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA >> operandB;
    }
}
