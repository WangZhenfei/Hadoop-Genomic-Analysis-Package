////package main.bam;
////
////import fi.tkk.ics.hadoop.bam.*;
////import fi.tkk.ics.hadoop.bam.cli.CLIPlugin;
////import fi.tkk.ics.hadoop.bam.cli.Utils;
////import fi.tkk.ics.hadoop.bam.custom.jargs.gnu.CmdLineParser;
////import fi.tkk.ics.hadoop.bam.util.*;
////import net.sf.picard.sam.SamFileHeaderMerger;
////import net.sf.samtools.SAMFileHeader;
////import net.sf.samtools.SAMFileReader;
////import net.sf.samtools.util.BlockCompressedInputStream;
////import net.sf.samtools.util.BlockCompressedOutputStream;
////import net.sf.samtools.util.BlockCompressedStreamConstants;
////import org.apache.hadoop.conf.Configuration;
////import org.apache.hadoop.fs.FSDataInputStream;
////import org.apache.hadoop.fs.FileStatus;
////import org.apache.hadoop.fs.FileSystem;
////import org.apache.hadoop.fs.Path;
////import org.apache.hadoop.io.*;
////import org.apache.hadoop.mapreduce.*;
////import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
////import org.apache.hadoop.mapreduce.lib.input.FileSplit;
////import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
////import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
////import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
////import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
////import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
////import org.apache.hadoop.util.LineReader;
//////import tutorial.hadoopbam.Sort;
////
////import java.io.*;
////import java.nio.charset.CharacterCodingException;
////import java.util.ArrayList;
////import java.util.List;
////import java.util.Locale;
////
/////**
//// * Created with IntelliJ IDEA.
//// * Date: 4/24/13
//// * Time: 2:44 PM
//// *
//// * @author Wai Lok Sibon Li
//// */
////public class SimpleBamReader2 extends CLIPlugin {
////
////    private static final String INPUT_PATHS_PROP = "hadoopbam.sort.input.paths";
////
////
////    private static SamFileHeaderMerger headerMerger = null;
////    public static SamFileHeaderMerger getHeaderMerger(Configuration conf)
////            throws IOException
////    {
////        // TODO: it would be preferable to cache this beforehand instead of
////        // having every task read the header block of every input file. But that
////        // would be trickier, given that SamFileHeaderMerger isn't trivially
////        // serializable.
////
////        // Save it in a static field, though, in case that helps anything.
////        if (headerMerger != null)
////            return headerMerger;
////
////        final List<SAMFileHeader> headers = new ArrayList<SAMFileHeader>();
////
////        for (final String in : conf.getStrings(INPUT_PATHS_PROP)) {
////            final Path p = new Path(in);
////
////            final SAMFileReader r =
////                    new SAMFileReader(p.getFileSystem(conf).open(p));
////            headers.add(r.getFileHeader());
////            r.close();
////        }
////
////        return headerMerger = new SamFileHeaderMerger(
////                SAMFileHeader.SortOrder.coordinate, headers, true);
////    }
////
//////    public SimpleBamReader2()  {
//////        super("hello");
//////
//////    }
//////
//////
//////    @Override
//////    protected int run(CmdLineParser cmdLineParser) {
//////        return 0;  //To change body of implemented methods use File | Settings | File Templates.
//////    }
//////
//////
//////
//////}
////////
//////class SimpleBamReader2 {
//////    public static void main(String[] args) throws Exception {
//////        Configuration conf = new Configuration();
//////
//////        Job job = new Job(conf, "simplebamreader");
//////
//////        job.setJarByClass(SimpleBamReader2.class); // Added this line in after. Works better now
//////
//////        job.setOutputKeyClass(Text.class);
//////        job.setOutputValueClass(IntWritable.class);
//////
//////        job.setMapperClass (Mapper.class);
//////        job.setReducerClass(SortReducer.class);
//////
//////        job.setInputFormatClass(TextInputFormat.class);
//////        job.setOutputFormatClass(TextOutputFormat.class);
//////
//////        FileInputFormat.addInputPath(job, new Path(args[0]));
//////        FileOutputFormat.setOutputPath(job, new Path(args[1]));
//////
//////        job.waitForCompletion(true);
//////    }
//////
//////    // start
////    @Override protected int run(CmdLineParser parser) {
////        final List<String> args = parser.getRemainingArgs();
////        if (args.isEmpty()) {
////            System.err.println("sort :: WORKDIR not given.");
////            return 3;
////        }
////        if (args.size() == 1) {
////            System.err.println("sort :: INPATH not given.");
////            return 3;
////        }
////
////        final String wrkDir = args.get(0),
////                out    = (String)parser.getOptionValue(outputFileOpt);
////
////        final List<String> strInputs = args.subList(1, args.size());
////
////        final List<Path> inputs = new ArrayList<Path>(strInputs.size());
////        for (final String in : strInputs)
////            inputs.add(new Path(in));
////
////        final boolean verbose = parser.getBoolean(verboseOpt);
////
////        final String intermediateOutName =
////                (out == null ? inputs.get(0) : new Path(out)).getName();
////
////        final Configuration conf = getConf();
////
////        SAMFormat format = null;
////        final String fmt = (String)parser.getOptionValue(formatOpt);
////        if (fmt != null) {
////            try { format = SAMFormat.valueOf(fmt.toUpperCase(Locale.ENGLISH)); }
////            catch (IllegalArgumentException e) {
////                System.err.printf("sort :: invalid format '%s'\n", fmt);
////                return 3;
////            }
////        }
////
////        if (format == null) {
////            if (out != null)
////                format = SAMFormat.inferFromFilePath(out);
////            if (format == null)
////                format = SAMFormat.BAM;
////        }
////
////        conf.set(AnySAMOutputFormat.OUTPUT_SAM_FORMAT_PROPERTY,
////                format.toString());
////
////        conf.setBoolean(AnySAMInputFormat.TRUST_EXTS_PROPERTY,
////                !parser.getBoolean(noTrustExtsOpt));
////
////        // Used by getHeaderMerger. SortRecordReader needs it to correct the
////        // reference indices when the output has a different index and
////        // SortOutputFormat needs it to have the correct header for the output
////        // records.
////        conf.setStrings(INPUT_PATHS_PROP, strInputs.toArray(new String[0]));
////
////        // Used by SortOutputFormat to name the output files.
////        conf.set(SortOutputFormat.OUTPUT_NAME_PROP, intermediateOutName);
////
////        // Let the output format know if we're going to merge the output, so that
////        // it doesn't write headers into the intermediate files.
////        conf.setBoolean(SortOutputFormat.WRITE_HEADER_PROP, out == null);
////
////        Path wrkDirPath = new Path(wrkDir);
////
////        final int reduceTasks = parser.getInt(reducersOpt, 1);
////
////        final Timer t = new Timer();
////        try {
////            // Required for path ".", for example.
////            wrkDirPath = wrkDirPath.getFileSystem(conf).makeQualified(wrkDirPath);
////
////            Utils.configureSampling(wrkDirPath, intermediateOutName, conf);
////
////            conf.setInt("mapred.reduce.tasks", reduceTasks);
////
////            final Job job = new Job(conf);
////
////            job.setJarByClass  (SimpleBamReader2.class);
////            job.setMapperClass (Mapper.class);
////            job.setReducerClass(SortReducer.class);
////
////            job.setMapOutputKeyClass(LongWritable.class);
////            job.setOutputKeyClass   (NullWritable.class);
////            job.setOutputValueClass (SAMRecordWritable.class);
////
////            job.setInputFormatClass (SortInputFormat.class);
////            job.setOutputFormatClass(SortOutputFormat.class);
////
////            for (final Path in : inputs)
////                FileInputFormat.addInputPath(job, in);
////
////            FileOutputFormat.setOutputPath(job, wrkDirPath);
////
////            job.setPartitionerClass(TotalOrderPartitioner.class);
////
////            System.out.println("sort :: Sampling...");
////            t.start();
////
////            InputSampler.<LongWritable,SAMRecordWritable>writePartitionFile(
////                    job,
////                    new InputSampler.RandomSampler<LongWritable, SAMRecordWritable>(
////                            0.01, 10000, Math.max(100, reduceTasks)));
////
////            System.out.printf("sort :: Sampling complete in %d.%03d s.\n",
////                    t.stopS(), t.fms());
////
////            job.submit();
////
////            System.out.println("sort :: Waiting for job completion...");
////            t.start();
////
////            if (!job.waitForCompletion(verbose)) {
////                System.err.println("sort :: Job failed.");
////                return 4;
////            }
////
////            System.out.printf("sort :: Job complete in %d.%03d s.\n",
////                    t.stopS(), t.fms());
////
////        } catch (IOException e) {
////            System.err.printf("sort :: Hadoop error: %s\n", e);
////            return 4;
////        } catch (ClassNotFoundException e) { throw new RuntimeException(e); }
////        catch   (InterruptedException e) { throw new RuntimeException(e); }
////
////        if (out != null) try {
////            System.out.println("sort :: Merging output...");
////            t.start();
////
////            final Path outPath = new Path(out);
////
////            final FileSystem srcFS = wrkDirPath.getFileSystem(conf);
////            FileSystem dstFS =    outPath.getFileSystem(conf);
////
////            // First, place the BAM header.
////
////            final SAMFileHeader header = getHeaderMerger(conf).getMergedHeader();
////            header.setSortOrder(SAMFileHeader.SortOrder.coordinate);
////
////            final OutputStream outs = dstFS.create(outPath);
////
////            // Don't use the returned stream, because we're concatenating directly
////            // and don't want to apply another layer of compression to BAM.
////            new SAMOutputPreparer().prepareForRecords(outs, format, header);
////
////            // Then, the actual SAM or BAM contents.
////
////            final FileStatus[] parts = srcFS.globStatus(new Path(
////                    wrkDir, conf.get(SortOutputFormat.OUTPUT_NAME_PROP) +
////                    "-[0-9][0-9][0-9][0-9][0-9][0-9]*"));
////
////            {int i = 0;
////                final Timer t2 = new Timer();
////                for (final FileStatus part : parts) {
////                    System.out.printf("sort :: Merging part %d (size %d)...",
////                            ++i, part.getLen());
////                    System.out.flush();
////
////                    t2.start();
////
////                    final InputStream ins = srcFS.open(part.getPath());
////                    IOUtils.copyBytes(ins, outs, conf, false);
////                    ins.close();
////
////                    System.out.printf(" done in %d.%03d s.\n", t2.stopS(), t2.fms());
////                }}
////            for (final FileStatus part : parts)
////                srcFS.delete(part.getPath(), false);
////
////            // And if BAM, the BGZF terminator.
////            if (format == SAMFormat.BAM)
////                outs.write(BlockCompressedStreamConstants.EMPTY_GZIP_BLOCK);
////
////            outs.close();
////
////            System.out.printf("sort :: Merging complete in %d.%03d s.\n",
////                    t.stopS(), t.fms());
////
////        } catch (IOException e) {
////            System.err.printf("sort :: Output merging failed: %s\n", e);
////            return 5;
////        }
////        return 0;
////    }
//////
//////
//////}
//////
//////// Start
////final class SortInputFormat
////        extends BGZFSplitFileInputFormat<LongWritable,Text>
////{
////    @Override public RecordReader<LongWritable,Text>
////    createRecordReader(InputSplit split, TaskAttemptContext ctx)
////            throws InterruptedException, IOException
////    {
////        final RecordReader<LongWritable,Text> rr = new SortRecordReader();
////        rr.initialize(split, ctx);
////        return rr;
////    }
////}
////final class SortRecordReader extends RecordReader<LongWritable,Text> {
////
////    private final LongWritable key = new LongWritable();
////
////    private final BlockCompressedLineRecordReader lineRR =
////            new BlockCompressedLineRecordReader();
////
////    @Override public void initialize(InputSplit spl, TaskAttemptContext ctx)
////            throws IOException
////    {
////        lineRR.initialize(spl, ctx);
////    }
////    @Override public void close() throws IOException { lineRR.close(); }
////
////    @Override public float getProgress() { return lineRR.getProgress(); }
////
////    @Override public LongWritable getCurrentKey  () { return key; }
////    @Override public Text         getCurrentValue() {
////        return lineRR.getCurrentValue();
////    }
////
////    @Override public boolean nextKeyValue()
////            throws IOException, CharacterCodingException
////    {
////        if (!lineRR.nextKeyValue())
////            return false;
////
////        Text line = getCurrentValue();
////        int tabOne = line.find("\t");
////
////        int rid = Integer.parseInt(Text.decode(line.getBytes(), 0, tabOne));
////
////        int tabTwo = line.find("\t", tabOne + 1);
////        int posBeg = tabOne + 1;
////        int posEnd = tabTwo - 1;
////
////        int pos = Integer.parseInt(
////                Text.decode(line.getBytes(), posBeg, posEnd - posBeg + 1));
////
////        key.set(BAMRecordReader.getKey0(rid, pos));
////        return true;
////    }
////}
////// LineRecordReader has only private fields so we have to copy the whole thing
////// over. Make the key a NullWritable while we're at it, we don't need it
////// anyway.
////final class BlockCompressedLineRecordReader
////        extends RecordReader<NullWritable,Text>
////{
////    private long start;
////    private long pos;
////    private long end;
////    private BlockCompressedInputStream bin;
////    private LineReader in;
////    private int maxLineLength;
////    private Text value = new Text();
////
////    public void initialize(InputSplit genericSplit,
////                           TaskAttemptContext context) throws IOException {
////        Configuration conf = context.getConfiguration();
////        this.maxLineLength = conf.getInt("mapred.linerecordreader.maxlength",
////                Integer.MAX_VALUE);
////
////        FileSplit split = (FileSplit) genericSplit;
////        start = (        split.getStart ()) << 16;
////        end   = (start + split.getLength()) << 16;
////
////        final Path file = split.getPath();
////        FileSystem fs = file.getFileSystem(conf);
////
////        bin =
////                new BlockCompressedInputStream(
////                        new WrapSeekable<FSDataInputStream>(
////                                fs.open(file), fs.getFileStatus(file).getLen(), file));
////
////        in = new LineReader(bin, conf);
////
////        if (start != 0) {
////            bin.seek(start);
////
////            // Skip first line
////            in.readLine(new Text());
////            start = bin.getFilePointer();
////        }
////        this.pos = start;
////    }
////
////    public boolean nextKeyValue() throws IOException {
////        while (pos <= end) {
////            int newSize = in.readLine(value, maxLineLength);
////            if (newSize == 0)
////                return false;
////
////            pos = bin.getFilePointer();
////            if (newSize < maxLineLength)
////                return true;
////        }
////        return false;
////    }
////
////    @Override public NullWritable getCurrentKey() { return NullWritable.get(); }
////    @Override public Text getCurrentValue() { return value; }
////
////    @Override public float getProgress() {
////        if (start == end) {
////            return 0.0f;
////        } else {
////            return Math.min(1.0f, (pos - start) / (float)(end - start));
////        }
////    }
////
////    @Override public void close() throws IOException { in.close(); }
////}
////
////final class SortOutputFormat extends TextOutputFormat<NullWritable,Text> {
////    public static final String
//////            OUTPUT_NAME_PROP  = "hadoopbam.sort.output.name",
////            WRITE_HEADER_PROP = "hadoopbam.sort.output.write-header";
////    public static final String OUTPUT_NAME_PROP =
////            "hadoopbam.summarysort.output.name";
////
////    @Override public RecordWriter<NullWritable,Text> getRecordWriter(
////            TaskAttemptContext ctx)
////            throws IOException
////    {
////        Path path = getDefaultWorkFile(ctx, "");
////        FileSystem fs = path.getFileSystem(ctx.getConfiguration());
////
////        final OutputStream file = fs.create(path);
////
////        return new TextOutputFormat.LineRecordWriter<NullWritable,Text>(
////                new DataOutputStream(
////                        new FilterOutputStream(new BlockCompressedOutputStream(file, null))
////                        {
////                            @Override public void close() throws IOException {
////                                // Don't close the BlockCompressedOutputStream, so we don't
////                                // get an end-of-file sentinel.
////                                this.out.flush();
////
////                                // Instead, close the file stream directly.
////                                file.close();
////                            }
////                        }));
////    }
////
////    @Override public Path getDefaultWorkFile(
////            TaskAttemptContext context, String ext)
////            throws IOException
////    {
////        String filename  = context.getConfiguration().get(OUTPUT_NAME_PROP);
////        String extension = ext.isEmpty() ? ext : "." + ext;
////        int    part      = context.getTaskAttemptID().getTaskID().getId();
////        return new Path(super.getDefaultWorkFile(context, ext).getParent(),
////                filename + "-" + String.format("%06d", part) + extension);
////    }
////
////    // Allow the output directory to exist.
////    @Override public void checkOutputSpecs(JobContext job) {}
////}
//////// End
//////
//////
//////
////final class SortReducer
////        extends Reducer<LongWritable,SAMRecordWritable,
////        NullWritable,SAMRecordWritable>
////{
////    @Override protected void reduce(
////            LongWritable ignored, Iterable<SAMRecordWritable> records,
////            Reducer<LongWritable,SAMRecordWritable,
////                    NullWritable,SAMRecordWritable>.Context
////                    ctx)
////            throws IOException, InterruptedException
////    {
////        for (SAMRecordWritable rec : records)
////            ctx.write(NullWritable.get(), rec);
////    }
////}
////
//////
//////}
////
////
////
////
////
//////public class SimpleBamReader2 extends CLIPlugin {
////    private static final List<Pair<CmdLineParser.Option, String>> optionDescriptions= new ArrayList<Pair<CmdLineParser.Option, String>>();;
////    private static final CmdLineParser.Option
////            reducersOpt    = new CmdLineParser.Option.IntegerOption('r', "reducers=N"),
////            verboseOpt     = new CmdLineParser.Option.BooleanOption('v', "verbose"),
////            outputFileOpt  = new CmdLineParser.Option.StringOption('o', "output-file=PATH"),
////            formatOpt      = new CmdLineParser.Option.StringOption('F', "format=FMT"),
////            noTrustExtsOpt = new CmdLineParser.Option.BooleanOption("no-trust-exts");
////
////    static {
////        optionDescriptions.add(new Pair<CmdLineParser.Option, String>(
////                reducersOpt, "use N reduce tasks (default: 1), i.e. produce N "+
////                "outputs in parallel"));
////        optionDescriptions.add(new Pair<CmdLineParser.Option, String>(
////                verboseOpt, "tell the Hadoop job to be more verbose"));
////        optionDescriptions.add(new Pair<CmdLineParser.Option, String>(
////                outputFileOpt, "output a complete SAM/BAM file to the file PATH, "+
////                "removing the parts from WORKDIR; SAM/BAM is chosen "+
////                "by file extension, if appropriate (but -F takes "+
////                "precedence)"));
////        optionDescriptions.add(new Pair<CmdLineParser.Option, String>(
////                noTrustExtsOpt, "detect SAM/BAM files only by contents, "+
////                "never by file extension"));
////        optionDescriptions.add(new Pair<CmdLineParser.Option, String>(
////                formatOpt, "select the output format based on FMT: SAM or BAM"));
////    }
////
////    public SimpleBamReader2()  {
////        super("command", "description", "version", "usageParams", optionDescriptions, "longDescription");
////
////    }
////
////
//////    @Override
//////    protected int run(CmdLineParser cmdLineParser) {
//////        return 0;  //To change body of implemented methods use File | Settings | File Templates.
//////    }
////
////
////
////}
//
//
//import fi.tkk.ics.hadoop.bam.AnySAMInputFormat;
//import fi.tkk.ics.hadoop.bam.SAMRecordWritable;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.mapreduce.InputSplit;
//import org.apache.hadoop.mapreduce.JobContext;
//import org.apache.hadoop.mapreduce.RecordReader;
//import org.apache.hadoop.mapreduce.TaskAttemptContext;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//
//import java.io.IOException;
//import java.util.List;
//
//
//
//
//final class SortInputFormat
//        extends FileInputFormat<LongWritable,SAMRecordWritable>
//{
//    private AnySAMInputFormat baseIF = null;
//
//    private void initBaseIF(final Configuration conf) {
//        if (baseIF == null)
//            baseIF = new AnySAMInputFormat(conf);
//    }
//
//    @Override public @Override public RecordReader<LongWritable,Range><LongWritable,SAMRecordWritable>
//                                                                      createRecordReader(InputSplit split, TaskAttemptContext ctx)
//        throws InterruptedException, IOException
//{
//    initBaseIF(ctx.getConfiguration());
//
//    final RecordReader<LongWritable,SAMRecordWritable> rr =
//            new SortRecordReader(baseIF.createRecordReader(split, ctx));
//    rr.initialize(split, ctx);
//    return rr;
//}
//
//    @Override protected boolean isSplitable(JobContext job, Path path) {
//        initBaseIF(job.getConfiguration());
//        return baseIF.isSplitable(job, path);
//    }
//    @Override public List<InputSplit> getSplits(JobContext job)
//            throws IOException
//    {
//        initBaseIF(job.getConfiguration());
//        return baseIF.getSplits(job);
//    }
//}
//
//
//
//
//
//
//
//// Because we want a total order and we may change the key when merging
//// headers, we can't use a mapper here: the InputSampler reads directly from
//// the InputFormat.
//final class SortInputFormat
//        extends FileInputFormat<LongWritable,SAMRecordWritable>
//{
//    private AnySAMInputFormat baseIF = null;
//
//    private void initBaseIF(final Configuration conf) {
//        if (baseIF == null)
//            baseIF = new AnySAMInputFormat(conf);
//    }
//
//    @Override public RecordReader<LongWritable,SAMRecordWritable>
//    createRecordReader(InputSplit split, TaskAttemptContext ctx)
//            throws InterruptedException, IOException
//    {
//        initBaseIF(ctx.getConfiguration());
//
//        final RecordReader<LongWritable,SAMRecordWritable> rr =
//                new SortRecordReader(baseIF.createRecordReader(split, ctx));
//        rr.initialize(split, ctx);
//        return rr;
//    }
//
//    @Override protected boolean isSplitable(JobContext job, Path path) {
//        initBaseIF(job.getConfiguration());
//        return baseIF.isSplitable(job, path);
//    }
//    @Override public List<InputSplit> getSplits(JobContext job)
//            throws IOException
//    {
//        initBaseIF(job.getConfiguration());
//        return baseIF.getSplits(job);
//    }
//}