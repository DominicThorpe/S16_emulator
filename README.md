# The S16 Emulator

The S16 emulator is an emulator written in Java for a custom microprocessor architecture. It is a callback to the old 8086 processor architecture, more information can be found (https://en.wikipedia.org/wiki/Intel_8086)[here].

## Features
 - 4 general purpose 16-bit registers which can be used as 2 8-bit registers each
 - 4 special purpose 16-bit registers (cannot be split)
 - 64 KiB of RAM
 - CISC instruction set
 - Interrupts
 - Screen and stdio output
 - Keyboard input

## Instruction Set

Information on the instruction set and format of instructions can be found in this repositiory (https://github.com/DominicThorpe/S16_assembler)[here].

## Memory Layout

## Remaining Features to Implement

The microprocessor still needs the following instructions to be implemented:
 - Push and pop stack
 - Input and output
 - Load and store to/from RAM
 - Call and return subroutine
 - Call and return from interrupts
