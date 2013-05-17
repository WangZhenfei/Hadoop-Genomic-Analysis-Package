package main.vcf;

import com.sun.corba.se.spi.ior.Writeable;
import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/17/13
 * Time: 1:32 PM
 *
 * Object to capture
 *
 * @author Wai Lok Sibon Li
 */
public class VCFRecord implements Writeable {

    public VCFSettings vcfSettings;

    @Override
    public void write(OutputStream outputStream) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
