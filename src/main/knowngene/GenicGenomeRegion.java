package main.knowngene;

/**
 * A gene region object that stores the positions, gene names etc. of a genome.
 * 06-09-2006
 *
 * @author Wai Lok Sibon Li
 */
public class GenicGenomeRegion extends GenomeRegion{
//    private int start;
//    private int end;
//    //private byte type;
//    private char direction;
//    private String geneName;
//    private int length;
    private GenomeRegion[] genomeRegions;

    public GenicGenomeRegion(int start, int end, /*byte type, */char direction, String geneName, GenomeRegion[] genomeRegions) {
        super(start, end, direction, geneName);
//        this.start = start;
//        this.end = end;
//        //this.type = type;
//        this.direction = direction;
//        this.geneName = geneName;
        this.genomeRegions = genomeRegions;
//        length = end-start;
    }

//    public int getStart() {return start;}
//    public int getEnd() {return end;}
//    //public byte getType() {return type;}
//    public char getDirection() {return direction;}
//    public String getGeneName() {return geneName;}
//    public int getLength() {return length;}
    public GenomeRegion[] getGenomeRegions() {return genomeRegions;}


//    protected void setStart(int start) {this.start=start;length = end-start;}
//    protected void setEnd(int end) {this.end=end;length = end-start;}
//    //protected void setType(byte type) {this.type=type;}
//    protected void setDirection(char direction) {this.direction=direction;}
    protected void setGeneName(String geneName) {
        this.geneName=geneName;
        for(int i =0; i< genomeRegions.length; i++) {
            genomeRegions[i].setGeneName(geneName);
            //System.out.println(geneName);
        }
    }
    protected void setGenomeRegions(GenomeRegion[] r) {this.genomeRegions =r;}
}
