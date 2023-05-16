package emulator;

/**
 * The complete register file of the S16 CPU including split and regular registers. 
 * 
 * @author Dominic Thorpe
 */
public class RegisterFile {
    private Register[] registers = new Register[8];


    /**
     * Instantiates and initialises the registers, the generat purposes registers and the return value
     * are set to 0, and the frame, base, and stack pointers are set to 0xFFFF initially, and may be 
     * mutated to any 16 bit value later.
     */
    public RegisterFile() {
        registers[0] = new SplitRegister(); // ax
        registers[1] = new SplitRegister(); // bx
        registers[2] = new SplitRegister(); // cx
        registers[3] = new SplitRegister(); // dx
        registers[4] = new Register(); // rp
        registers[5] = new Register(); // fp
        registers[6] = new Register(); // bp
        registers[7] = new Register(); // sp

        // set the frame, base, and stack pointers to the top of the stack at 0xFFFF
        registers[5].setValue((short)0xFFFF);
        registers[6].setValue((short)0xFFFF);
        registers[7].setValue((short)0xFFFF);
    }


    /**
     * Gets the value in the specified register and returns it. If the register is a split register
     * then only the upper or lower 8 bits can be fetched.
     * 
     * @param registerIndex The index of the register to get
     * @param high True if the upper 8 bits are desired
     * @param low True if the lower 8 bits are desired
     * @return The value of the upper and/or lower 8 bits of the register
     */
    public int getRegister(int registerIndex, boolean high, boolean low) {
        Register register = registers[registerIndex];

        // if the high and low registers are set, or the register is not a split register
        // load the whole value
        if ((high && low) || !(register instanceof SplitRegister)) {
            return register.getValue();
        }

        SplitRegister splitRegister = (SplitRegister)register;
        if (high) {
            return splitRegister.getHighValue();
        } else if (low) {
            return splitRegister.getLowValue();
        }

        return 0;
    }


    /**
     * Sets the value of the specified register to the new value. If the register is a split register
     * then just the lower or just the upper 8 bits can be set.
     * 
     * @param registerIndex The index of the register to set
     * @param newValue The new value to set the register to
     * @param high True if setting the upper 8 bits is desired
     * @param low True if setting the lower 8 bits is desired
     */
    public void setRegister(int registerIndex, short newValue, boolean high, boolean low) {
        Register register = registers[registerIndex];

        // if the high and low registers are set, or the register is not a split register
        // load the whole value
        if ((high && low) || !(register instanceof SplitRegister)) {
            register.setValue((short)newValue);
            return;
        }

        SplitRegister splitRegister = (SplitRegister)register;
        if (high) {
            splitRegister.setHighValue(newValue);
        } else if (low) {
            splitRegister.setLowValue(newValue);
        }
    }


    /**
     * Returns an object pointer to the Register at the given index in the file.
     * @param index Index of the register to get
     * @return Objetc pointer to the register
     */
    public Register getRegisterObject(int index) {
        return this.registers[index];
    }


    /**
     * Prints the formatted content of the registers.
     */
    public void printAll() {
        System.out.println(String.format("Ax: 0x%04X", this.getRegister(0, true, true)));
        System.out.println(String.format("Bx: 0x%04X", this.getRegister(1, true, true)));
        System.out.println(String.format("Cx: 0x%04X", this.getRegister(2, true, true)));
        System.out.println(String.format("Dx: 0x%04X", this.getRegister(3, true, true)));
        
        System.out.println(String.format("Rp: 0x%04X", this.getRegister(4, true, true)));
        System.out.println(String.format("Fp: 0x%04X", this.getRegister(5, true, true)));
        System.out.println(String.format("Bp: 0x%04X", this.getRegister(6, true, true)));
        System.out.println(String.format("Sp: 0x%04X", this.getRegister(7, true, true)));
    }
}
