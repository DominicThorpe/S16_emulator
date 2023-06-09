package ALUOperations;
/**
 * Interface which all ALU operations must implement.
 * @author Dominic Thorpe
 */
public interface ALUOperation {
    /**
     * Performs the mathematical/logical operation on the data provided by the ALU.
     * @param operandA The first operand
     * @param operandB The second operand
     * @param isWord True if the operands are 16 bits, otherwise false
     * @return The result of the operation
     */
    OperationResult performOperation(int operandA, int operandB, boolean isWord);
}
