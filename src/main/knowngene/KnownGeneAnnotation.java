package main.knowngene;

/**
 * Created with IntelliJ IDEA.
 * Date: 5/29/13
 * Time: 12:15 PM
 *
 * @author Wai Lok Sibon Li
 */
public class KnownGeneAnnotation {

    private final int GENE_NAME_INDEX = 0;
    private final int CHR_LOCATION_INDEX = 1;
    private final int GENE_ORIENTATION_INDEX = 2;
    private final int GENE_START_INDEX = 3;
    private final int GENE_END_INDEX = 4;

    private final int EXON_REGION_COUNT_INDEX = 7;
    private final int START_RANGES_INDEX = 8;
    private final int END_RANGES_INDEX = 9;
    private final int ALTERNATIVE_GENE_NAME_INDEX = 10;

    public KnownGeneAnnotation (String line) {
        String[] split = line.split("\t");
    }
}

class ExonRange {
    long start, end;
    ExonRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
