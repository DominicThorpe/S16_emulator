package ALUOperations;

public class XorOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB, boolean isWord) {
        return new OperationResult((short) (operandA ^ operandB), false);
    }
}
