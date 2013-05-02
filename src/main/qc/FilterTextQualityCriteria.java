package main.qc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User: sibonli
 * Date: 4/23/13
 * Time: 12:22 PM
 * @author Wai Lok Sibon Li
 */
public class FilterTextQualityCriteria extends QualityCriteria {

    private final static String PASS = "PASS";
    private int index = 6; // Index of the quality measure in the VCF file
    private ArrayList<String> filterCriteria;


    /* Bunch of defaults used in the 1000 genomes project */
    private final static String LOW_SNPQUAL = "low_snpqual";
    private final static String LOW_VARIANTREADS = "low_VariantReads";
    private final static String LOW_VARIANTRATIO = "low_VariantRatio";
    private final static String SINGLE_STRAND = "single_strand";
    private final static String LOW_COVERAGE = "low_coverage";
    private final static String NO_DATA = "No_data";
    private final static String NO_VAR = "No_var";

    /*
     * Default constructor that filters everything that doesn't say pass
     */
    public FilterTextQualityCriteria() {
        filterCriteria = null;
    }

    public FilterTextQualityCriteria(ArrayList<String> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public FilterTextQualityCriteria(ArrayList<String> filterCriteria, int index) {
        this.index = index;
        this.filterCriteria = filterCriteria;
    }

    @Override
    public boolean meetsCriteria(String[] data) {
        String filterText = data[index].toUpperCase().trim();
        if(filterText.equals(PASS)) {
            return true;
        }
        if(filterCriteria==null) {  // In the case that no criteria was specified, filter all
            return false;
        }

        String[] split = filterText.split(",");
        HashSet<String> filterTerms = new HashSet<String>(split.length);
        for(int i=0; i<split.length; i++) {
            filterTerms.add(split[i].trim());
        }
        for(String criteria : filterCriteria) {
            if(filterTerms.contains(criteria)) {
                return false;
            }
        }
        return true;
    }
}
