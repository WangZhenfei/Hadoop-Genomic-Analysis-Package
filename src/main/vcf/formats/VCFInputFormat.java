package main.vcf.formats;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/16/13
 * Time: 6:34 PM
 *
 * Handling the Variant Call Format (VCF) files in Hadoop as a FileInputFormat
 * For more info, see: http://vcftools.sourceforge.net/specs.html
 * and http://www.1000genomes.org/wiki/Analysis/Variant%20Call%20Format/vcf-variant-call-format-version-41
 *
 * @author Wai Lok Sibon Li
 */
public class VCFInputFormat extends FileInputFormat<Text,VCFRecordWritable> {


    @Override
    public RecordReader<Text, VCFRecordWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
//        return new RecordReader<LongWritable, SAMRecordWritable>() {
        return new VCFRecordReader();
    }

    /*
     * Set to not splittable because the VCF content is highly dependent on the info in the header.
     * Plus it's usually under 64MB anyways.
     */
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }


}

