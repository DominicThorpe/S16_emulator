package ALUOperations;


/**
 * Implements addition on the ALU, returns operand A + operand B
 */
public class AddOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        int result = operandA + operandB;
        System.out.println("Result: " + result);
        if (result < Short.MIN_VALUE || result > Short.MAX_VALUE)
            return new OperationResult((short) (operandA + operandB), true);
        return new OperationResult((short) (operandA + operandB), false);
    }
}
