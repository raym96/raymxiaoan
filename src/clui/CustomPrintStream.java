package clui;

import java.io.*;

/**
 * The Class CustomPrintStream, allows to preserve the console output, 
 * and write to a file while also having it displayed on the console.
 * @author He Xiaoan
 * @author Ji Raymond
 * 
 */
public class CustomPrintStream extends PrintStream {
    
    /** The second. */
    private final PrintStream second;

    /**
     * Instantiates a new personal stream.
     *
     * @param main the main
     * @param second the second
     */
    public CustomPrintStream(OutputStream main, PrintStream second) {
        super(main);
        this.second = second;
    }
    
    /**
     * Sets the output stream.
     *
     * @param fileName the new output stream
     */
    public static void setOutPutFile(String fileName){
    	try {
			FileOutputStream out = new FileOutputStream(fileName);
		    CustomPrintStream output = new CustomPrintStream(out, System.out);
		    System.setOut(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

    /**
     * Closes the main stream. 
     * The second stream is just flushed but <b>not</b> closed.
     * @see java.io.PrintStream#close()
     */
    @Override
    public void close() {
        // just for documentation
        super.close();
    }

    /**
     * Flush.
     */
    @Override
    public void flush() {
        super.flush();
        second.flush();
    }

    /**
     * Write.
     *
     * @param buf the buf
     * @param off the off
     * @param len the len
     */
    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        second.write(buf, off, len);
    }

    /**
     * Write.
     *
     * @param b the b
     */
    @Override
    public void write(int b) {
        super.write(b);
        second.write(b);
    }

    /**
     * Write.
     *
     * @param b the b
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        second.write(b);
    }
}
