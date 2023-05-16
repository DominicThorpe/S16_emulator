package ALUOperations;

public class SignExtOperation implements ALUOperation {
    @Override
    public OperationResult performOperation(int operandA, int operandB) {
        // if the sign bit of the lower byte is 0, set the upper byte to 0x0000, else set it to 0xFFFF
        if ((operandA & 0x0080) == 0) {
            System.out.println("Here");
            return new OperationResult((short) (operandA & 0x00FF), false);
        }
        return new OperationResult((short) (operandA | 0xFF00), false);
    }
}
