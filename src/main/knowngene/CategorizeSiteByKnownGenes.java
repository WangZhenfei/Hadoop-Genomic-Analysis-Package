package main.knowngene;

import main.qc.SNPQualityController;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/29/13
 * Time: 12:01 PM
 *
 * Takes in a UCSC known genes format file and partitions genome sites
 * based on whether they are in an exon, intron or intragenic region
 *
 * For an example input format, see:
 * http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/knownGene.txt.gz
 *
 * @author Wai Lok Sibon Li
 */
public class CategorizeSiteByKnownGenes {

    public static void partitionGenome(Configuration conf, String dataOutputString, String dataInputString) throws Exception {
        partitionGenome(conf, new Path(dataOutputString), new Path(dataInputString));
    }

    public static void partitionGenome(Configuration conf, Path dataOutput, Path dataInput) throws Exception {
        Job job = new Job(conf, "categorizesitebyknowngenes");

        job.setJarByClass(CategorizeSiteByKnownGenes.class); // Added this line in after. Works better now

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(GenomeSiteTypeMapper.class);
        job.setReducerClass(Reducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, dataInput);
        FileOutputFormat.setOutputPath(job, dataOutput);

        job.waitForCompletion(true);
    }



    public static class GenomeSiteTypeMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private static SNPQualityController qc = SNPQualityController.getInstance();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            KnownGeneAnnotation knownGeneAnnotation = new KnownGeneAnnotation(value.toString());  // filters all except PASS


            String line = value.toString();
            if(!line.startsWith("#")) {
                String[] split = line.split("\t");
                if(split.length > 1 && qc.checkQuality(split)) {
                    String positionID = split[0] + "_" + split[1];
                    word.set(positionID);
                    context.write(one, word);
                }
            }
        }
    }

    // Unless you want to use it to sort?
//    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
//
//        public void reduce(Text key, Iterable<IntWritable> values, Context context)
//                throws IOException, InterruptedException {
//            int sum = 0;
//            for (IntWritable val : values) {
//                sum += val.get();
//            }
//            context.write(key, new IntWritable(sum));
//        }
//    }
}
