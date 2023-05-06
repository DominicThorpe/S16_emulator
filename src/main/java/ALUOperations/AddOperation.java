package ALUOperations;


/**
 * Implements addition on the ALU, returns operand A + operand B
 */
public class AddOperation implements ALUOperation {
    @Override
    public int performOperation(int operandA, int operandB) {
        return operandA + operandB;
    }
}
