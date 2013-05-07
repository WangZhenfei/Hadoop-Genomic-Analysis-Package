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
import org.apache.hadoop.util.GenericOptionsParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/2/13
 * Time: 5:21 PM
 *
 * @author Wai Lok Sibon Li
 */





public class CalculateTargetRegionSNPCount {




    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private static SNPQualityController qc = SNPQualityController.getInstance();

        private static HashMap<String, ArrayList<Range>> bedMap = new HashMap<String, ArrayList<Range>>();

        //todo write bed file importer
        static {    // HARD CORE
            bedMap.put("8", new ArrayList<Range>());// Range(117291701, 117930819));
            bedMap.put("9", new ArrayList<Range>());
            bedMap.put("10", new ArrayList<Range>());
            bedMap.put("11", new ArrayList<Range>());
            bedMap.put("12", new ArrayList<Range>());
            bedMap.put("14", new ArrayList<Range>());
            bedMap.put("15", new ArrayList<Range>());
            bedMap.put("18", new ArrayList<Range>());
            bedMap.put("20", new ArrayList<Range>());

            bedMap.get("8").add(new Range(117291701, 117930819));
            bedMap.get("8").add(new Range(127830818, 128730818));
            bedMap.get("9").add(new Range(5891100, 6558270));
            bedMap.get("10").add(new Range(8376087, 8772195));
            bedMap.get("11").add(new Range(110644790, 110794790));
            bedMap.get("11").add(new Range(111047966, 111504790));
            bedMap.get("12").add(new Range(50497179, 51330290));
            bedMap.get("14").add(new Range(54370768, 54840250));
            bedMap.get("15").add(new Range(32958831, 33432615));
            bedMap.get("18").add(new Range(45936002, 46556002));
            bedMap.get("20").add(new Range(60840110, 60995164));
        }



        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            qc.addCriteria(new FilterTextQualityCriteria());  // filters all except PASS

            String line = value.toString();
            if(!line.startsWith("#")) {
                String[] split = line.split("\t");
                String chromosome = split[0];
                int position = Integer.parseInt(split[1]);
                ArrayList<Range> arrayList = bedMap.get(chromosome);
                Range matchRange = new Range(Integer.MIN_VALUE, Integer.MIN_VALUE);
                boolean matches = false;
                if(arrayList != null) {
                    for(Range r : arrayList) {
                        if(r.withinRange(position)) {
                            matches = true;
                            matchRange = r;
                        }
                    }
                }

                if(split.length > 1 && qc.checkQuality(split) && matches) {
//                    String positionID = split[0] + "_" + split[1];
                    word.set(chromosome+"_"+matchRange.getStart()+"_"+matchRange.getEnd());
//                    word.set(COUNT);
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

        Job job = new Job(conf, "CalculateTargetRegionSNPCount");

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

    public static class Range {
        private int start;
        private int end;
        public Range(int start, int end) {
            this.start = start;
            this.end = end;

        }

        public boolean withinRange(int i) {
            if(i>=start && i<=end) {
                return true;
            }
            return false;
        }
        public int getStart() {
            return start;
        }
        public int getEnd() {
            return end;
        }
    }

}


