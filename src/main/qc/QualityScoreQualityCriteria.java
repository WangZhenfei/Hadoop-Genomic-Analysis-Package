package main.qc;

/**
 * Created with IntelliJ IDEA.
 * Date: 4/23/13
 * Time: 1:43 PM
 * @author Wai Lok Sibon Li
 */
public class QualityScoreQualityCriteria extends QualityCriteria {

    private int threshold;

    private int index = 5; // Index of the quality measure in the VCF file

    public QualityScoreQualityCriteria(int threshold) {
        this.threshold = threshold;
    }

    public QualityScoreQualityCriteria(int threshold, int index) {
        this.threshold = threshold;
        this.index = index;
    }

    @Override
    public boolean meetsCriteria(String[] data) {
        int score = Integer.parseInt(data[index]);
        if(score > threshold) {
            return true;
        }
        return false;
    }

    public int getThreshold() {
        return threshold;
    }
}
