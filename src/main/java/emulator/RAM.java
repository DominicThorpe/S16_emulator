package emulator;

/**
 * Represents the RAM of the system, which is 64 KiB in size (65,535 bytes).
 * @author Dominic Thorpe 
 */
public class RAM {
    /** Starting address of the data section */
    public final short data_section = 0x5800;
    /** Starting address of the code section */
    public final short code_section = (short)0x9000;

    private byte[] memory = new byte[0xFFFF];


    /**
     * Gets the value of at the specified RAM address
     * @param address The address to get
     * @return The byte at that value
     */
    public byte getValue(short address) {
        return this.memory[(int)address & 0xFFFF];
    }


    /**
     * Sets the value at the given address to the given value
     * @param address The address to set
     * @param value The new value
     */
    public void setValue(short address, byte value) {
        this.memory[(int)address & 0xFFFF] = value;
    }


    /**
     * Prints the addresses and values of all the non-zero memory locations in the data section of RAM
     */
    public void printDataSection() {
        for (int i = data_section; i < code_section; i++) {
            if (memory[i] != 0) {
                System.out.println(String.format("0x%04X: 0x%02X", i, memory[i]));
            }
        }
    }


    /**
     * Prints the addresses and values of all the non-zero memory locations in the code section of RAM
     */
    public void printCodeSection() {
        for (int i = code_section; i < 0xC000; i++) {
            if (memory[i] != 0) {
                System.out.println(String.format("0x%04X: 0x%02X", i, memory[i]));
            }
        }
    }
}
