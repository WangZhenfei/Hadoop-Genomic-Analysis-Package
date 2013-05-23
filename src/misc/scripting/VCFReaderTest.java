package misc.scripting;

import net.sf.picard.io.IoUtil;
import org.broad.tribble.readers.AsciiLineReader;
import org.broadinstitute.variant.variantcontext.VariantContext;
import org.broadinstitute.variant.vcf.VCFCodec;
import org.broadinstitute.variant.vcf.VCFHeader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/20/13
 * Time: 12:27 PM
 *
 * @author Wai Lok Sibon Li
 */
public class VCFReaderTest {
    public static void main(String[] args) {
        File f = new File(args[0]);
        VCFCodec codec = new VCFCodec();   // If you want to do it this way.... Why not make this static?!
        AsciiLineReader reader = new AsciiLineReader(IoUtil.openFileForReading(f));

        VCFHeader header = (VCFHeader) codec.readHeader(reader);
        try {
            String line;
            System.out.println("scurred " + header.toString());
            while((line = reader.readLine())!=null) {
                System.out.println(line);
                VariantContext vc = codec.decode(line);
                HashMap<String, Object> map = new HashMap<String, Object>(vc.getAttributes());
                for(String s : map.keySet()) {
                    System.out.println(s);
                }
                System.out.println("isSNP " + vc.isSNP());
                System.out.println("getPhredScaledQual " + vc.getPhredScaledQual());
                ArrayList<String> filters = new ArrayList<String>(vc.getFilters());
                for(String s : filters) {
                    System.out.println(s);
                }
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        reader.close();

//        VariantContextIterator variantIterator = new VariantContextIterator(file);
//        final VCFHeader header = variantIterator.getHeader();
    }
}
