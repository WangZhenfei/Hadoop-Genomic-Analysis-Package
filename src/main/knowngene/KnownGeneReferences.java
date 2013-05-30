package main.knowngene;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/29/13
 * Time: 4:04 PM
 *
 * @author Wai Lok Sibon Li
 */
/**
 * Identifies attributes of a gene from from kgXref.txt file from UCSC genome database.
 * Author: Wai Lok, Sibon Li
 * 06-09-2006
 */

import java.util.*;
import java.io.*;

@Deprecated
public class KnownGeneReferences implements Comparable<KnownGeneReferences> {
    private String ucscID;
    private String mRNAID;
    private String swissProtAcc;
    private String swissProtID;
    private String geneSymbol;
    private String refSeqID;
    private String ncbiAcc;
    private String description;

    public KnownGeneReferences (String ucscID, String mRNAID, String swissProtAcc, String swissProtID, String geneSymbol, String refSeqID, String ncbiAcc, String description) {
        this.ucscID = ucscID;
        this.mRNAID = mRNAID;
        this.swissProtAcc = swissProtAcc;
        this.swissProtID = swissProtID;
        this.geneSymbol = geneSymbol;
        this.refSeqID = refSeqID;
        this.ncbiAcc = ncbiAcc;
        this.description = description;
    }

    /* Constructor that takes in a line from the kgXref.txt file and splits it up */
    public KnownGeneReferences (String line) {

        String[] split = line.split("\\t");
        //System.out.println(split[7]);
        ucscID = split[0];
        mRNAID = split[1];
        swissProtAcc = split[2];
        swissProtID = split[3];
        geneSymbol = split[4];
        refSeqID = split[5];
        ncbiAcc = split[6];
        description = split[7];
    }


    public int compareTo(KnownGeneReferences kgr) {
        return getRefSeqID().compareTo(kgr.getRefSeqID());
    }


    public String getUcscID() {return ucscID;}
    public String getMRNAID() {return mRNAID;}
    public String getSwissProtAcc() {return swissProtAcc;}
    public String getSwissProtID() {return swissProtID;}
    public String getGeneSymbol() {return geneSymbol;}
    public String getRefSeqID() {return refSeqID;}
    public String getNcbiAcc() {return ncbiAcc;}
    public String getDescription() {return description;}


    protected void setUcscID(String ucscID) {
        this.ucscID=ucscID;
    }
    protected void setMRNAID(String mRNAID) {
        this.mRNAID=mRNAID;
    }
    protected void setSwissProtAcc(String swissProtAcc) {
        this.swissProtAcc=swissProtAcc;
    }
    protected void setSwissProtID(String swissProtID) {
        this.swissProtID=swissProtID;
    }
    protected void setGeneSymbol(String geneSymbol) {
        this.geneSymbol=geneSymbol;
    }
    protected void setRefSeqID(String refSeqID) {
        this.refSeqID=refSeqID;
    }
    protected void setNcbiAcc(String ncbiAcc) {
        this.ncbiAcc=ncbiAcc;
    }
    protected void setDescription(String description) {
        this.description=description;
    }
}
