package main.vcf.tools;

import main.qc.FilterTextQualityCriteria;
import main.qc.SNPQualityController;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: sibonli
 * Date: 4/17/13
 * Time: 5:37 PM
 *
 * @author Wai Lok Sibon Li
 *
 * Computes the count of the number of changes in allele from the reference sequence
 */



public class CalculateSNPCount {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private static SNPQualityController qc = SNPQualityController.getInstance();
        private static String COUNT = "count";

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            qc.addCriteria(new FilterTextQualityCriteria());  // filters all except PASS

            String line = value.toString();
            if(!line.startsWith("#")) {
                String[] split = line.split("\t");
                if(split.length > 1 && qc.checkQuality(split)) {
//                    String positionID = split[0] + "_" + split[1];
//                    word.set(positionID);
                    word.set(COUNT);
                    context.write(word, one);
                }
            }
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

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

        Job job = new Job(conf, "CalculateSNPCount");

        job.setJarByClass(CalculateAlleleFrequency.class); // Added this line in after. Works better now

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }

}