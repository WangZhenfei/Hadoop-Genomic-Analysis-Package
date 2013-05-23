package main.vcf.formats;


import fi.tkk.ics.hadoop.bam.SAMRecordWritable;
import fi.tkk.ics.hadoop.bam.util.DataInputWrapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.LineReader;
import org.broad.tribble.readers.AsciiLineReader;
import org.broadinstitute.variant.variantcontext.VariantContext;
import org.broadinstitute.variant.vcf.VCFCodec;
import org.broadinstitute.variant.vcf.VCFHeader;

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
//    private LineReader in;
    private Text key;
    private VCFRecordWritable value;
    private int maxLineLength;
    private long start;
    private long end;
    private long pos;
//    private VCFRecordWritable settings = new VCFRecordWritable();
//    VCFRecordWritable records;

    VCFCodec codec;
    VCFHeader header;
    AsciiLineReader reader;

//    RecordReader<LongWritable, VCFRecordWritable> recordReader;

//    public VCFRecordReader(RecordReader<LongWritable,VCFRecordWritable> rr) {
//        recordReader = rr;
//    }


    // TODO handle non-standard and machine specific options in the comments

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext context) throws IOException, InterruptedException {

        FileSplit fileSplit = (FileSplit) inputSplit;
        final Path file = fileSplit.getPath();
        FileSystem fs = file.getFileSystem(context.getConfiguration());
//        FSDataInputStream filein = fs.open(fileSplit.getPath());
        FSDataInputStream in = fs.open(file);
        reader = new AsciiLineReader(new DataInputWrapper(in));
//        if(codec==null) {
        codec = new VCFCodec();
        header = (VCFHeader) codec.readHeader(reader);

//            VCFHeader header  = (VCFHeader) codec.readHeader(reader);
//        }
//        Configuration conf = context.getConfiguration();
//        this.maxLineLength = conf.getInt("mapred.linerecordreader.maxlength",Integer.MAX_VALUE);
//        FileSystem fs = file.getFileSystem(conf);
        start = fileSplit.getStart();
        end= start + fileSplit.getLength();
        boolean skipFirstLine = false;

        in.seek(start);
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
        if (start == end) {
            return 0.0f;
        }
        else {
            return Math.min(1.0f, (pos - start) / (float)(end - start));
        }
    }


    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
//        if (!recordReader.nextKeyValue()) {
//            return false;
//        }
//        if(key==null) {
//            return false;
//        }
        String line = reader.readLine();
        if(line == null) {
            return false;
        }

        pos++;
        VariantContext record = codec.decode(line);
        key.set(record.getChr() + "_" + record.getStart() + "_" + record.getID());
//        getCurrentValue().setRecord(record);
        value.setRecord(record);
        return true;



//        if (key == null) {
//            key = new LongWritable();
//        }
//        key.set(pos);
//        if (value == null) {
//            value = new Text();
//        }
//        value.clear();
//        final Text endline = new Text("\n");
//        int newSize = 0;
//        for(int i=0;i<NLINESTOPROCESS;i++){
//            Text v = new Text();
//            while (pos < end) {
//                newSize = in.readLine(v, maxLineLength,Math.max((int)Math.min(Integer.MAX_VALUE, end-pos),maxLineLength));
//                value.append(v.getBytes(),0, v.getLength());
//                value.append(endline.getBytes(),0, endline.getLength());
//                if (newSize == 0) {
//                    break;
//                }
//                pos += newSize;
//                if (newSize < maxLineLength) {
//                    break;
//                }
//            }
//        }
//        if (newSize == 0) {
//            key = null;
//            value = null;
//            return false;
//        } else {
//            return true;
//        }

//        return false;
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
        reader.close();
//        recordReader.close();
//        if (in != null) {
//            in.close();
//        }
    }

}
