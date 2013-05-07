package misc.scripting;//package misc;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/3/13
 * Time: 10:32 AM
 *
 * Assuming that all the results are in separate folders under one directory, concatenates the files into out.merged.txt
 *
 * @author Wai Lok Sibon Li
 */
public class MergeHadoopResults {

    public static final String hadoopOutFileName = "part-r-00000";

    public static void main(String[] args) {


        if(args.length != 1) {
            throw new RuntimeException("Input is one argument <input folder>");
        }
        String filename = args[0];
        String outFileName = "out.merged.txt";
        try {
            PrintWriter pw = new PrintWriter(new File(outFileName));


            File f = new File(filename);
            File[] files = f.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    try {
                        File textFile = new File(files[i].getAbsolutePath()+System.getProperty("file.separator")+hadoopOutFileName);
                        BufferedReader br = new BufferedReader(new FileReader(textFile));
//                        int lineCount = 0;
                        String line;
                        // Assuming that it is only one line
                        while((line = br.readLine())!=null) {
                            pw.println(files[i].getName() + "\t" + line);
//                            lineCount ++;
                        }
//                        if(lineCount>1) {
//                            System.err.println("File in directory "+ files[i].getName() + " has more than one line. You may want to consider separating the files");
//                        }
                    }
                    catch (NullPointerException e) {
                        System.err.println("Directory " + files[i].getName() + " is empty. ");
                    }
                }
            }
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
