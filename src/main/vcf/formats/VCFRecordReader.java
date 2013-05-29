package main.vcf.formats;


import fi.tkk.ics.hadoop.bam.SAMRecordWritable;
import fi.tkk.ics.hadoop.bam.util.DataInputWrapper;
import net.sf.picard.io.IoUtil;
import net.sf.samtools.util.IOUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.FileSplit;
//import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.LineReader;
import org.broad.tribble.readers.AsciiLineReader;
import org.broadinstitute.variant.variantcontext.VariantContext;
import org.broadinstitute.variant.vcf.VCFCodec;
import org.broadinstitute.variant.vcf.VCFHeader;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/17/13
 * Time: 1:48 PM
 *
 * @author Wai Lok Sibon Li
 *
 * org.apache.hadoop.mapreduce.RecordReader class for VCF format
 *
 */
class VCFRecordReader extends RecordReader<Text, VCFRecordWritable> {

//    private FileSplit fileSplit;
//    private Configuration conf;
//    private boolean processed = false;
//
//    @Override
//    public boolean next(Text key, VCFRecordWritable value) throws IOException {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Text createKey() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public VCFRecordWritable createValue() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public long getPos() throws IOException {
//        return 0;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void close() throws IOException {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public float getProgress() throws IOException {
//        return 0;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//}









//    private LineReader in;
    private Text key;
    private VCFRecordWritable value;
//    private int maxLineLength;
    private long start;
    private long end;
    private long pos;
//    private VCFRecordWritable settings = new VCFRecordWritable();
//    VCFRecordWritable records;

    VCFCodec codec;
    VCFHeader header;
    HadoopAsciiLineReader reader;

    FileSplit fileSplit;


//    RecordReader<LongWritable, VCFRecordWritable> recordReader;

//    public VCFRecordReader(RecordReader<LongWritable,VCFRecordWritable> rr) {
//        recordReader = rr;
//    }


    // TODO handle non-standard and machine specific options in the comments

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext context) throws IOException, InterruptedException {
        key = new Text();
        value = new VCFRecordWritable();
        fileSplit = (FileSplit) inputSplit;
        final Path file = fileSplit.getPath();
        FileSystem fs = file.getFileSystem(context.getConfiguration());
//        FSDataInputStream filein = fs.open(fileSplit.getPath());
        FSDataInputStream filein = fs.open(file);


        filein.seek(start);
        LineReader lr = new LineReader(filein);

//        Text line = new Text();
//        int num;
//        while((num = lr.readLine(line)) > 0) {
//            System.out.println(line.toString());
//        }

//        reader = new AsciiLineReader(filein);

//        String line;
//        char c;
        reader = new HadoopAsciiLineReader(lr);
//        reader.printFully();
        System.out.println("where");
        codec = new VCFCodec();

//        String line;
//        while((line=reader.readLine()) != null) {
//            System.out.println(line);
//        }

        header = (VCFHeader) codec.readHeader(reader);
//        int num;
        System.out.println("here");
        String l;
        while((l = reader.readLine()) != null) {
            System.out.println(l);
        }
        System.out.println("present");


//            VCFHeader header  = (VCFHeader) codec.readHeader(reader);
//        }
//        Configuration conf = context.getConfiguration();
//        this.maxLineLength = conf.getInt("mapred.linerecordreader.maxlength",Integer.MAX_VALUE);
//        FileSystem fs = file.getFileSystem(conf);
        start = fileSplit.getStart();
        end= start + fileSplit.getLength();
        boolean skipFirstLine = false;



        if (start != 0){
//            skipFirstLine = true;
//            --start;
            throw new RuntimeException("Start should always be 0 in the case of no file splits right? (I'm not sure)");
        }
//        in = new LineReader(filein,conf);
//        if(skipFirstLine){
//            start += in.readLine(new Text(),0,(int)Math.min((long)Integer.MAX_VALUE, end - start));
//        }
        this.pos = start;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
//        return recordReader.getProgress();
//        return 0.0f;
        if (start == end) {
            return 0.0f;
        }
        else {
            return Math.min(1.0f, (pos - start) / (float)(end - start));
        }
    }


    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        String line = reader.readLine();
        System.out.println("1z " + line);
        while(line!=null && line.startsWith(VCFHeader.HEADER_INDICATOR)) {
            System.out.println("2z " + line);
            line = reader.readLine();
            pos++;
        }
        if(line == null) {
            return false;
        }

        VariantContext record = codec.decode(line);

        System.out.println("3z " + line);
        key.set(record.getChr() + "_" + record.getStart());
        value.setRecord(record);
        return true;
    }






    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
//        return recordReader.getCurrentKey();
        return key;
    }

    @Override
    public VCFRecordWritable getCurrentValue() throws IOException, InterruptedException {
//        return recordReader.getCurrentValue();
        return value;
    }


    @Override
    public void close() throws IOException {
//        filein.close();
        reader.close();
//        recordReader.close();
//        if (in != null) {
//            in.close();
//        }
    }

}
