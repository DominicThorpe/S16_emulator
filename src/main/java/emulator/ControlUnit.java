package emulator;

/**
 * Represents the control unit of the CPU which is responsible for coordinating the processor and 
 * handling the different opcodes.
 * 
 * @author Dominic Thorpe 
 */
public class ControlUnit {
    /**
     * Takes an instruction and a reference to the CPU object, then executes the instruction, this may
     * mutate the status register, program counter, registers, and RAM.
     * @param cpu Reference to the CPU object
     * @param instruction The instruction to execute
     */
    public void executeInstruction(CPU cpu, int instruction) {
        int opcode = (instruction & 0xFC00) >> 10;
        byte meta = (byte)((instruction & 0x03C0) >> 6);

        int operandA = (instruction & 0x0038) >>> 3;
        int operandB = instruction & 0x0007;

        Register registerA = cpu.regFile.getRegisterObject(operandA);
        Register registerB = cpu.regFile.getRegisterObject(operandB);

        switch (opcode) {
            case 0x0000: // No Operation
                break;
            
            case 0x0001:
            case 0x0002:
                cpu.alu.executeInstruction(new ALUOperations.AddOperation(), registerA, registerB, meta);
                break;
                
            case 0x0004:
                cpu.alu.executeInstruction(new ALUOperations.IncOperation(), registerA, registerB, meta);
                break;
            
            case 0x0005:
            case 0x0006:
                cpu.alu.executeInstruction(new ALUOperations.SubOperation(), registerA, registerB, meta);
                break;
            
            case 0x0008:
                cpu.alu.executeInstruction(new ALUOperations.DecOperation(), registerA, registerB, meta);
                break;
            
            case 0x000A:
                cpu.alu.executeInstruction(new ALUOperations.NegOperation(), registerA, registerB, meta);
                break;
            
            case 0x0016: // Move immediate
                int immediate = cpu.ram.getValue((short) (cpu.getPC() + 2)) << 8 
                                 | cpu.ram.getValue((short) (cpu.getPC() + 3));
                cpu.regFile.setRegister(operandA, immediate, true, true);

                cpu.setPC((short) (cpu.getPC() + 2)); // increment the PC by 2 more than usual
                break;
            
            case 0x0017:
            case 0x0018:
                cpu.alu.executeInstruction(new ALUOperations.MulOperation(), registerA, registerB, meta);
                break;

            case 0x0019:
            case 0x001A:
                cpu.alu.executeInstruction(new ALUOperations.DivOperation(), registerA, registerB, meta);
                break;
            
            case 0x003F:
                break;

            default:
                System.out.println(String.format("Opcode: 0x%04X not yet implemented!", opcode));
                break;
        }
    }
}
