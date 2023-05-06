package emulator;

/**
 * Represents the status register held within the ALU, but also has the interrupt flag
 */
public class StatusRegister {
    private boolean carry = false; // unsigned operation overflow
    private boolean overflow = false; // signed operation overflow 
    private boolean zero = false; // result was zero
    private boolean negative = false; // result was negative
    private boolean interrupt = false; // currently running an interrupt service routine


    /**
     * Sets all flags to false
     */
    private void resetFlags() {
        carry = false;
        overflow = false;
        zero = false;
        negative = false;
        interrupt = false;
    }


    /**
     * Takes a value and sets the appropriate status register flags for it. It compares it to an
     * expected value to check if the carry/overflow flags should be set. Carry is set of unsigned
     * operations, overflow for signed operations.
     * 
     * @param value The value to set flags for
     * @param expected The value if there is no overflow
     * @param signed True if the operation was signed, otherwise false
     */
    public void setFlagsForValue(int value, int expected, boolean signed) {
        resetFlags();
        if (value == 0) {
            zero = true;
            return;
        }

        if (value < 0) {
            negative = true;
        }

        if (value == expected) {
            return;
        }

        if (signed) {
            overflow = true;
        } else {
            carry = true;
        }
    }


    /**
     * Formatted prints the current status of the flags in the status register.
     */
    public void printStatus() {
        System.out.println("Overflow:  " + overflow);
        System.out.println("Carry:     " + carry);
        System.out.println("Zero:      " + zero);
        System.out.println("Negative:  " + negative);
        System.out.println("Interrupt: " + interrupt);
    }


    /**
     * Gets the current value of the carry flag
     * @return The current value of the carry flag
     */
    public boolean getCarry() { 
        return carry; 
    }


    /**
     * Sets the value of the carry flag to the new value
     * @param value The new value of the carry flag
     */
    public void setCarry(boolean value) {
        carry = value;
    }


    /**
     * Gets the current value of the overflow flag
     * @return The current value of the overflow flag
     */
    public boolean getOverflow() { 
        return overflow; 
    }


    /**
     * Sets the value of the overflow flag to the new value
     * @param value The new value of the overflow flag
     */
    public void setOverflow(boolean value) {
        overflow = value;
    }


    /**
     * Gets the current value of the zero flag
     * @return The current value of the zero flag
     */
    public boolean getZero() { 
        return zero; 
    }


    /**
     * Sets the value of the zero flag to the new value
     * @param value The new value of the zero flag
     */
    public void setZero(boolean value) {
        zero = value;
    }


    /**
     * Gets the current value of the negative flag
     * @return The current value of the negative flag
     */
    public boolean getNegative() { 
        return negative; 
    }


    /**
     * Sets the value of the negative flag to the new value
     * @param value The new value of the negative flag
     */
    public void setNegative(boolean value) {
        negative = value;
    }


    /**
     * Gets the current value of the interrupt flag
     * @return The current value of the interrupt flag
     */
    public boolean getInterrupt() { 
        return interrupt; 
    }


    /**
     * Sets the value of the interrupt flag to the new value
     * @param value The new value of the interrupt flag
     */
    public void setInterrupt(boolean value) {
        interrupt = value;
    }
}
