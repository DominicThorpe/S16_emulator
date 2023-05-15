package ALUOperations;

public class OperationResult {
    public final short result;
    public final boolean overflow;


    public OperationResult(short result, boolean overflow) {
        this.result = result;
        this.overflow = overflow;
    } 
}
