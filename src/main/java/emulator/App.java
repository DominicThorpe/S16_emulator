package emulator;

/**
 * Driver class to run the S16 emulator
 * @author Dominic Thorpe
 */
public class App {
    /** 
     * Main entry point of the S16 emulator 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new CPU("test.sse");
    }
}
