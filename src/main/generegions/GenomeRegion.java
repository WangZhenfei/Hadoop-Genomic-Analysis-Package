package main.generegions;

/**
 * A region object that stores the positions, gene names etc. of a genome.
 * 05-09-2006
 *
 * @author Wai Lok Sibon Li
 */
public class GenomeRegion implements Comparable<GenomeRegion> {
    protected int start;
    protected int end;
    protected GenomeRegionType type;
    protected char direction;
    protected String geneName;
    protected int length;

    public GenomeRegion(int start, int end, GenomeRegionType type) {
        this.start = start;
        this.end = end;
        this.type = type;
        length = end-start;
    }

    public GenomeRegion(int start, int end) {
        this(start, end, GenomeRegionType.UNSPECIFIED);
    }

    public GenomeRegion(int start, int end,  char direction, String geneName) {
        this(start, end, GenomeRegionType.UNSPECIFIED, direction, geneName);
    }
    public GenomeRegion(int start, int end, GenomeRegionType type, char direction, String geneName) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.direction = direction;
        this.geneName = geneName;
        length = end-start;
    }

    public int compareTo(GenomeRegion r) {
        return start-r.getStart();
    }

    public int getStart() {return start;}
    public int getEnd() {return end;}
    public GenomeRegionType getType() {return type;}
    public char getDirection() {return direction;}
    public String getGeneName() {return geneName;}
    public int getLength() {return length;}


    protected void setStart(int start) {this.start=start;length = end-start;}
    protected void setEnd(int end) {this.end=end;length = end-start;}
    protected void setType(GenomeRegionType type) {this.type=type;}
    protected void setDirection(char direction) {this.direction=direction;}
    protected void setGeneName(String geneName) {this.geneName=geneName;}

    public enum GenomeRegionType {
        EXON, INTRON, INTERGENIC, UNSPECIFIED;
    }
}
