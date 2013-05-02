package main.bam;

import fi.tkk.ics.hadoop.bam.*;
import net.sf.samtools.SAMRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 4/26/13
 * Time: 2:48 PM
 *
 * Reads in a BAM file and writes the same java metadata to file and prints the SAM formatted records to screen
 *
 * @author Wai Lok Sibon Li
 */
public class SimpleBamOperation {
    public static void main(String[] args) throws Exception {

        final GenericOptionsParser parser;    // Handles parsing options such as -libjar
        try {
            parser = new GenericOptionsParser(args);

        } catch (Exception e) {
            System.err.printf("Error in Hadoop arguments: %s\n", e.getMessage());
            System.exit(1);

            // Hooray for javac
            return;
        }

        args = parser.getRemainingArgs();

        Configuration conf = new Configuration();

        Job job = new Job(conf, "simplebamoperation");

        job.setJarByClass(SimpleBamOperation.class); // Added this line in after. Works better now

//        job.setOutputKeyClass(Text.class);    // Artifact from WordCount example
//        job.setOutputValueClass(IntWritable.class);   // Artifact from WordCount example
        job.setMapOutputKeyClass(LongWritable.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(SAMRecordWritable.class);


        job.setMapperClass (Mapper.class);
        job.setReducerClass(SimpleBamOperationReducer.class);

//        job.setInputFormatClass(TextInputFormat.class);   // Artifact from WordCount example
//        job.setInputFormatClass(SortInputFormat.class);   // Artifact from WordCount example
        job.setInputFormatClass(BAMInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }

    public static class SimpleBamOperationReducer extends Reducer<LongWritable,SAMRecordWritable,
            NullWritable,SAMRecordWritable> {
        @Override protected void reduce(
                LongWritable ignored, Iterable<SAMRecordWritable> records, Reducer<LongWritable,SAMRecordWritable,
                        NullWritable,SAMRecordWritable>.Context ctx) throws IOException, InterruptedException {
            for (SAMRecordWritable rec : records) {

                SAMRecord samRecord = rec.get();
                // For operations on SAM records, see http://picard.sourceforge.net/javadoc/net/sf/samtools/SAMRecord.html
                String temp = samRecord.getSAMString();
                System.out.println(temp);
                ctx.write(NullWritable.get(), rec);
            }
        }
    }

}
