package main.vcf.formats;

import fi.tkk.ics.hadoop.bam.util.DataInputWrapper;
import org.apache.hadoop.io.Writable;
import org.broad.tribble.readers.AsciiLineReader;
import org.broadinstitute.variant.variantcontext.VariantContext;
import org.broadinstitute.variant.vcf.VCFCodec;
import org.broadinstitute.variant.vcf.VCFHeader;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/17/13
 * Time: 1:32 PM
 *
 * Reading in a VCF file
 *
 * @author Wai Lok Sibon Li
 */
public class VCFRecordWritable implements Writable {

    VariantContext record;
    private VCFCodec codec;
    private VCFHeader header;
    private ArrayList<VariantContext> records;

    private boolean isHeaderWritten = false;

    public VariantContext getRecord() {
        return record;
    }

    public void setRecord(VariantContext vc) {
        record = vc;
    }

    @Override
    public void write(DataOutput out) throws IOException {
//        PrintWriter pw = new PrintWriter(new DataOutputWrapper(out));
        if(!isHeaderWritten) {
            out.writeChars(header.toString());
            isHeaderWritten = true;
        }
//        out.writeChars(record.toString());
        for(VariantContext r : records) {
            out.writeChars(r.toString());
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
//        File f = new File(fileName);
//        AsciiLineReader reader = new AsciiLineReader(IoUtil.openFileForReading(f));
//        DataInputStream dis = new DataInputStream(in);
        AsciiLineReader reader = new AsciiLineReader(new DataInputWrapper(in));
        if(codec==null) {
            codec = new VCFCodec();
            header = (VCFHeader) codec.readHeader(reader);
//            VCFHeader header  = (VCFHeader) codec.readHeader(reader);
        }

        if(records == null) {
            records = new ArrayList<VariantContext>();
        }
//        String line = reader.readLine();
        String line;
//        if(line !=null) {
        while((line = reader.readLine())!=null) {
//            record = codec.decode(line);
            records.add(codec.decode(line));
//
        }
    }
}