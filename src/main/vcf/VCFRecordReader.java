package main.vcf;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/17/13
 * Time: 1:48 PM
 *
 * @author Wai Lok Sibon Li
 */
class VCFRecordReader extends RecordReader<LongWritable, VCFRecord> {
    private LineReader in;
    private LongWritable key;
    private VCFRecord value = new VCFRecord();
    private VCFSettings settings = new VCFSettings();


    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public VCFRecord getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }

}
