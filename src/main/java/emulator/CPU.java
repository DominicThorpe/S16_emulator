package emulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Represents an S16 CPU with all the components it will need, so far only including the register file
 * but will include the ALU, IO ports, RAM, other registers and more.
 * 
 * It does not go as deep as simulating individual control lines in this version of the class, but
 * provides a more high-level emulation.
 * 
 * @author Dominic Thorpe
 */
public class CPU {
    /** The control unit */
    public final ControlUnit controlUnit = new ControlUnit();
    /** The CPU registers which may or may not be SplitRegisters */
    public final RegisterFile regFile = new RegisterFile();
    /** The system RAM */
    public final RAM ram = new RAM();
    /** The Arithmetic Logic Unit */
    public final ALU alu = new ALU();
    
    private Register instrRegister = new Register();
    private short programCounter = ram.code_section;


    /**
     * Constructor for the CPU class, creates a CPU with initialised values and load the program
     * specified into RAM, then execute.
     * @param sourceFile The filename of the source file to execute
     */
    public CPU(String sourceFile) {
        System.out.println("Initialising CPU...");

        readFileToRAM(sourceFile);

        while (programCounter < 0xC000) {
            instrRegister.setValue((short) (ram.getValue(programCounter) << 8 | ram.getValue((short) (programCounter + 1)) & 0x00FF));
            if ((instrRegister.getValue() & 0xFC00) == 0xFC00) { // break loop if HALT detected
                break;
            }

            controlUnit.executeInstruction(this, instrRegister.getValue());
            
            programCounter += 2;
        }

        regFile.printAll();
    }


    /**
     * Finds if the bytes array passed has the start of an assembly directive at the index.
     * 
     * @param bytes The array of bytes to check
     * @param index The index to check for a directive at
     * @return The main part of the directive (e.g. "code" or "data"), or null if no directive
     */
    private String get_directive(byte[] bytes, int index) {
        if (bytes[index] == 0x2E) { // 0x2E is ASCII for '.'
            String substr = new String(Arrays.copyOfRange(bytes, index, index + 16), StandardCharsets.UTF_8);
            if (substr.startsWith(".data:")) {
                return "data";
            } else if (substr.startsWith(".code:")) {
                return "code";
            }
        }

        return null;
    }


    /**
     * Takes a filename for a file to read into RAM and reads it into RAM ready for execution
     * @param filename The filename of the executable to reac
     */
    private void readFileToRAM(String filename) {
        Path path = Paths.get(filename);
        short positionInRAM = ram.data_section; // start at the data section

        try {
            byte[] bytes = Files.readAllBytes(path);

            for (int i = 0; i < bytes.length; i++) {
                // handle assembler directives
                String directive = get_directive(bytes, i);
                if (directive == "data") { // go to start of data section and skip 6 bytes
                    positionInRAM = ram.data_section;
                    i += 5;
                    continue;
                } else if (directive == "code") { // go to start of code section and skip 6 bytess
                    positionInRAM = ram.code_section;
                    i += 5;
                    continue;
                }

                // insert the value into RAM and increment the address to write to
                this.ram.setValue(positionInRAM, bytes[i]);
                positionInRAM++;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    /**
     * Returns the value of the program counter
     * @return Value of the PC
     */
    public int getPC() {
        return programCounter;
    }


    /**
     * Sets the value of the program counter
     * @param value New value of the PC
     */
    public void setPC(short value) {
        programCounter = value;
    }
}
