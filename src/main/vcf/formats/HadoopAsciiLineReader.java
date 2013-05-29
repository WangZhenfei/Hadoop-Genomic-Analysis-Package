package main.vcf.formats;

import fi.tkk.ics.hadoop.bam.util.DataInputWrapper;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.Text;
import org.broad.tribble.readers.AsciiLineReader;
import org.broad.tribble.readers.LineReader;
import org.broad.tribble.readers.PositionalBufferedStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/27/13
 * Time: 11:27 PM
 *
 * @author Wai Lok Sibon Li
 */
public class HadoopAsciiLineReader implements LineReader {

    private static final int BUFFER_OVERFLOW_INCREASE_FACTOR = 2;
    private static final byte LINEFEED = (byte) ('\n' & 0xff);
    private static final byte CARRIAGE_RETURN = (byte) ('\r' & 0xff);

    FSDataInputStream is;
    char[] lineBuffer;
    Text line;

    org.apache.hadoop.util.LineReader lr;

//    public HadoopAsciiLineReader() {
//        this(null);
//    }

    public HadoopAsciiLineReader(org.apache.hadoop.util.LineReader lr) {
        this.lr = lr;
    }

    public HadoopAsciiLineReader(FSDataInputStream is){
        this.is = is;
        lr = new org.apache.hadoop.util.LineReader(is);
//        lr = new org.apache.hadoop.util.LineReader(new DataInputWrapper(is));
//        this.is = new PositionalBufferedStream(is);
        line = new Text();
//        this(new PositionalBufferedStream(is));
        lineBuffer = new char[10000];
        System.out.println("sphere");
    }

//    public HadoopAsciiLineReader(PositionalBufferedStream is) {
//        this.is = is;
//        // Allocate this only once, even though it is essentially a local variable of
//        // readLine.  This makes a huge difference in performance
//        lineBuffer = new char[10000];
//    }


    //    @Override
//    public HadoopAsciiLineReader(InputStream is){
////        super(new PositionalBufferedStream(is));
//    }

//    public final String readLine(FSDataInputStream stream) throws IOException {
//
//
//        org.apache.hadoop.util.LineReader lr = new org.apache.hadoop.util.LineReader(new DataInputWrapper(is));
//
//        int num = lr.readLine(line);
//        if(num < 0) {
//            return null;
//        }
//        return line.toString();
////        while((num  > 0) {
////            System.out.println(line.toString());
////        }
////
////
////
////
////        int linePosition = 0;
////
////        while (true) {
////            final int b = stream.read();
////
////            if (b == -1) {
////                // eof reached.  Return the last line, or null if this is a new line
////                if (linePosition > 0) {
////                    return new String(lineBuffer, 0, linePosition);
////                } else {
////                    return null;
////                }
////            }
////
////            final char c = (char) (b & 0xFF);
////            if (c == LINEFEED || c == CARRIAGE_RETURN) {
////                if (c == CARRIAGE_RETURN && stream.peek() == LINEFEED) {
////                    stream.read(); // <= skip the trailing \n in case of \r\n termination
////                }
////
////                return new String(lineBuffer, 0, linePosition);
////            } else {
////                // Expand line buffer size if neccessary.  Reserve at least 2 characters
////                // for potential line-terminators in return string
////
////                if (linePosition > (lineBuffer.length - 3)) {
////                    char[] temp = new char[BUFFER_OVERFLOW_INCREASE_FACTOR * lineBuffer.length];
////                    System.arraycopy(lineBuffer, 0, temp, 0, lineBuffer.length);
////                    lineBuffer = temp;
////                }
////
////                lineBuffer[linePosition++] = c;
////            }
////        }
//    }


//    public long getPosition(){
//        if(is == null){
//            throw new RuntimeException("getPosition() called but no default stream was provided to the class on creation");
//        }
//        return is.getPosition();
//    }

    @Override
    public String readLine() throws IOException {

        if ( is == null && lr == null){
            throw new RuntimeException("readLine() called without an explicit stream argument but no default stream was provided to the class on creation");
        }

        Text text = new Text();
//        line = new Text();
        int num = lr.readLine(text);
        if(num < 0) {
            return null;
        }
        return text.toString();
//        return readLine(is);
    }

    @Override
    public void close() {

        try {
            if ( is != null ) {
                is.close();
            }
            lr.close();
        } catch (IOException ex) {
            new RuntimeException("Failed to close HadoopLineReader", ex);
        }
        lineBuffer = null;
    }

    public void printFully() throws IOException {
        Text text = new Text();
        int num;
        while((num = lr.readLine(text)) > 0) {
            System.out.println(text.toString());
        }
    }
}
