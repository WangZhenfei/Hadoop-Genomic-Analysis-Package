package misc.scripting;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/13/13
 * Time: 2:57 PM
 *
 * @author Wai Lok Sibon Li
 */
public class StreamGobbler  extends Thread
{
    InputStream is;
    String type;
    OutputStream os;

    StreamGobbler(InputStream is, String type)
    {
        this(is, type, null);
    }
    StreamGobbler(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    public void run()
    {
        try
        {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            {

                if(type.equals("ERR")) {
                    System.out.println(type + ">" + line);
                }
                else if (pw != null) {
                    pw.println(line);
                }
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
