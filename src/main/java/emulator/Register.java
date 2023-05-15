package emulator;

/**
 * Represents a single 16-bit register in the S16 processor. 
 * 
 * @author Dominic Thorpe
 */
public class Register {
    /** Current value of the register */
    protected short value = 0; 


    /**
     * Returns the current value of the register with protection to ensure only 16 bits are returned.
     * @return The current 16-bit register contents
     */
    public short getValue() {
        return value;
    }


    /**
     * Sets the value of the register to the specified value.
     * @param newValue The new value of the register (only the bottom 16 bits are used)
     */
    public void setValue(short newValue) {
        this.value = newValue;
    }
}
