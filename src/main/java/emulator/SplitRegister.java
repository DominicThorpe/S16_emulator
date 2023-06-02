package emulator;

/**
 * Represents a register in the S16 that can be accessed either as a single 16-bit register, or as two
 * 8-bit registers.
 * 
 * @author Dominic Thorpe 
 */
public class SplitRegister extends Register {
    /**
     * Gets the upper 8 bits of the value in the register
     * @return 8 bit value, does not shift result down
     */
    public short getHighValue() {
        return (short) (value & 0xFF00);
    }


    /**
     * Sets the upper 8 bits of the value of the register to the new value
     * @param newValue The new value of the upper 8 bits, remaining bits in the new value are discarded
     */
    public void setHighValue(short newValue) {
        this.value = (short) ((newValue << 8) | (this.value & 0xFF));
    }


    /**
     * Gets the lower 8 bits of the value in the register
     * @return 8 bit value
     */
    public short getLowValue() {
        return (short) (value & 0x00FF);
    }

    /**
     * Sets the lower 8 bits of the value of the register to the new value
     * @param newValue The new value of the lower 8 bits, remaining bits in the new value are discarded
     */
    public void setLowValue(short newValue) {
        this.value = (short) ((newValue & 0x00FF) | (this.value & 0xFF00));
    }
}
