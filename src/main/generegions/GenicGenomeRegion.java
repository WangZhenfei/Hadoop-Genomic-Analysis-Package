package main.generegions;

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
    private GenomeRegion[] exons;
    private GeneAnnotations geneAnnotatations;

    public GenicGenomeRegion(int start, int end, /*byte type, */char direction, String geneName, GenomeRegion[] exons) {
        super(start, end, direction, geneName);
        this.exons = exons;
    }
    public GenicGenomeRegion(int start, int end, /*byte type, */char direction, String geneName, GenomeRegion[] exons, GeneAnnotations annotations) {
        this(start, end, direction, geneName, exons);
        this.exons = exons;
        geneAnnotatations = annotations;
    }

    public GenomeRegion[] getExons() {return exons;}
    public GeneAnnotations getGeneAnnotations() {return geneAnnotatations;}


    protected void setGeneName(String geneName) {
        this.geneName=geneName;
        for(int i =0; i< exons.length; i++) {
            exons[i].setGeneName(geneName);
        }
    }
    protected void setExons(GenomeRegion[] r) {this.exons =r;}

    public void setGeneAnnotations(GeneAnnotations ga) {this.geneAnnotatations = ga;}


    /*
     * Parses a line from KnownGene.txt and creates a GenicGenomeRegion.
     */
    public static GenicGenomeRegion parseKGString(String line) {
        String[] split = line.split("\t");
        return new GenicGenomeRegion();
    }

    private final int GENE_NAME_INDEX = 0;
    private final int CHR_LOCATION_INDEX = 1;
    private final int GENE_ORIENTATION_INDEX = 2;
    private final int GENE_START_INDEX = 3;
    private final int GENE_END_INDEX = 4;

    private final int EXON_REGION_COUNT_INDEX = 7;
    private final int START_RANGES_INDEX = 8;
    private final int END_RANGES_INDEX = 9;
    private final int ALTERNATIVE_GENE_NAME_INDEX = 10;
}
