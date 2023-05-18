package emulator;

import ALUOperations.OperationResult;

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
            
            case 0x0001: // Add
            case 0x0002: // Add unsigned
                cpu.alu.executeInstruction(new ALUOperations.AddOperation(), registerA, registerB, meta);
                break;
            
            case 0x0003: // Add carry
                cpu.alu.executeInstruction(new ALUOperations.AddCarryOperation(), registerA, registerB, meta);
                break;
                
            case 0x0004: // Increment
                cpu.alu.executeInstruction(new ALUOperations.IncOperation(), registerA, registerB, meta);
                break;
            
            case 0x0005: // subtract
            case 0x0006: // subtract unsigned
                cpu.alu.executeInstruction(new ALUOperations.SubOperation(), registerA, registerB, meta);
                break;

            case 0x0007: // Sub carry
                cpu.alu.executeInstruction(new ALUOperations.SubCarryOperation(), registerA, registerB, meta);
                break;
            
            case 0x0008: // decrement
                cpu.alu.executeInstruction(new ALUOperations.DecOperation(), registerA, registerB, meta);
                break;
            
            case 0x0009: // compare and set flags, do not mutate registers
                OperationResult result = new ALUOperations.SubOperation().performOperation(registerA.getValue(), registerB.getValue());
                ALU.statusRegister.setFlagsForValue(result, true);
                break;
            
            case 0x000A: // negate
                cpu.alu.executeInstruction(new ALUOperations.NegOperation(), registerA, registerB, meta);
                break;
            
            case 0x000B: // move
                registerA.setValue(registerB.getValue());
                break;
            
            case 0x0012: // swap
                short old = registerB.getValue();
                registerB.setValue(registerA.getValue());
                registerA.setValue(old);
                break;
            
            case 0x0016: // Move immediate
                short immediate = (short) (
                    cpu.ram.getValue((short) (cpu.getPC() + 2)) << 8
                     | (cpu.ram.getValue((short) (cpu.getPC() + 3)) & 0x00FF)
                );
                cpu.regFile.setRegister(operandA, immediate, true, true);

                cpu.setPC((short) (cpu.getPC() + 2)); // increment the PC by 2 more than usual
                break;
            
            case 0x0017: // multiply
            case 0x0018: // multiply unsigned
                cpu.alu.executeInstruction(new ALUOperations.MulOperation(), registerA, registerB, meta);
                break;

            case 0x0019: // divide
            case 0x001A: // divide unsigned
                cpu.alu.executeInstruction(new ALUOperations.DivOperation(), registerA, registerB, meta);
                break;
            
            case 0x001B: // Sign extend lower byte
                cpu.alu.executeInstruction(new ALUOperations.SignExtOperation(), registerA, registerB, meta);
                break;
            
            case 0x001C: // complement
                cpu.alu.executeInstruction(new ALUOperations.NotOperation(), registerA, registerB, meta);
                break;

            case 0x001D: // logical AND
                cpu.alu.executeInstruction(new ALUOperations.AndOperation(), registerA, registerB, meta);
                break;

            case 0x001E: // logical OR
                cpu.alu.executeInstruction(new ALUOperations.OrOperation(), registerA, registerB, meta);
                break;
            
            case 0x001F: // logical XOR
                cpu.alu.executeInstruction(new ALUOperations.XorOperation(), registerA, registerB, meta);
                break;
            
            case 0x0020: // Shift right arithmetic
                cpu.alu.executeInstruction(new ALUOperations.SraOperation(), registerA, registerB, meta);
                break;

            case 0x0021: // Shift left logical
                cpu.alu.executeInstruction(new ALUOperations.SrlOperation(), registerA, registerB, meta);
                break;

            case 0x0022: // Shift right logical
                cpu.alu.executeInstruction(new ALUOperations.SllOperation(), registerA, registerB, meta);
                break;

            case 0x0023: // Clear register
                registerA.setValue((short) 0);
                break;
            
            case 0x0026: // Jump
                cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x0027: // Branch equal
                if (ALU.statusRegister.getZero())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x0028: // Branch not equal
                if (!ALU.statusRegister.getZero())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x0029: // Branch greater than
                if (!ALU.statusRegister.getNegative() && !ALU.statusRegister.getZero())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x002A: // Branch less than
                if (ALU.statusRegister.getNegative() && !ALU.statusRegister.getZero())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x002B: // Branch greater than or equal
                if (!ALU.statusRegister.getNegative() || ALU.statusRegister.getZero())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x002C: // Branch less than or equal
                if (ALU.statusRegister.getNegative() || ALU.statusRegister.getZero())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;

            case 0x002D: // Branch if register zero
                if (registerA.getValue() == 0)
                    cpu.setPC((short) (registerB.getValue() - 2));
                break;

            case 0x002E: // Branch if register not zero
                if (registerA.getValue() != 0)
                    cpu.setPC((short) (registerB.getValue() - 2));
                break;
            
            case 0x002F: // Branch if overflow flag set
                ALU.statusRegister.printStatus();
                if (ALU.statusRegister.getOverflow()) {
                    cpu.setPC((short) (registerA.getValue() - 2));
                }
                break;

            case 0x0030: // Branch if carry flag set
                if (ALU.statusRegister.getCarry())
                    cpu.setPC((short) (registerA.getValue() - 2));
                break;
            
            case 0x0031: // Set carry flag
                ALU.statusRegister.setCarry(true);
                break;

            case 0x0032: // Clear carry flag
                ALU.statusRegister.setCarry(false);
                break;
            
            case 0x0033: // Set interrupts enabled flag
                ALU.statusRegister.enableInterrupts();
                break;

            case 0x0034: // Clear interrupts enabled flag
                ALU.statusRegister.disableInterrupts();
                break;
            
            case 0x003F: // halt and yield to operating system
                break;

            default:
                System.out.println(String.format("Opcode: 0x%04X not yet implemented!", opcode));
                break;
        }
    }
}
