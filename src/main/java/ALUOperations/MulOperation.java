package ALUOperations;

/**
 * Implements the signed multiplication operation, returns A * B
 */
public class MulOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA * operandB;
    }
}
