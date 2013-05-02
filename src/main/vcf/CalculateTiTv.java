package main.vcf;

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

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sibonli
 * Date: 4/17/13
 * Time: 5:37 PM
 *
 * @author Wai Lok Sibon Li
 *
 * Calculate the transition/transversion ratio between the genomes and the reference sequences
 */

public class CalculateTiTv {



    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private static SNPQualityController qc = SNPQualityController.getInstance(); // temp

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            qc.addCriteria(new FilterTextQualityCriteria());

            SubstitutionType.initializeBaseID();
            String line = value.toString();
            if(!line.startsWith("#")) {
                String[] split = line.split("\t");
                if(split.length > 1 && qc.checkQuality(split)) {
                    System.out.print(line+"\t" + split.length);
                    word.set(SubstitutionType.substitutionLookup(split[3], split[4]).name());
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

    public enum SubstitutionType {
        TRANSITION, TRANSVERSION, IDENTICAL;

        protected static HashMap<String, Integer> baseID;
        public static SubstitutionType substitutionLookup(String from, String to) {
            try {
                if(from.equals(to)) {
                    return IDENTICAL;
                }
                return (transitionTable[baseID.get(from)][baseID.get(to)])?TRANSITION:TRANSVERSION;
            }
            catch(NullPointerException e) {
                if(baseID == null) {
                    initializeBaseID();
                    return substitutionLookup(from, to);
                }
                else {
                    e.printStackTrace();
                }
            }
            return null;
        }
        private static void initializeBaseID() {
            baseID = new HashMap<String, Integer>(4);
            baseID.put("A", 0);
            baseID.put("C", 1);
            baseID.put("G", 2);
            baseID.put("T", 3);
        }

        private final static boolean[][] transitionTable = {
                {false, false, true, false},    // A
                {false, false, false, true},    // C
                {true, false, false, false},    // G
                {false, true, false, false}     // T
        };
    }


    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        Job job = new Job(conf, "calculatetransitiontransversion");

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