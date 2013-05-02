package main.qc;

import org.apache.hadoop.mapred.QueueAclsInfo;
import org.apache.hadoop.util.hash.Hash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * Date: 4/23/13
 * Time: 11:48 AM
 * @author Wai Lok Sibon Li
 */
public class SNPQualityController implements Iterable<QualityCriteria> {

    HashMap<String, QualityCriteria> criterion;

    /*
     * Returns whether the quality is good enough to pass based on the criteria
     */
    public boolean checkQuality(String[] data) {
        boolean qualityPass = true;
        for(QualityCriteria criteria : criterion.values()) {
            if(!criteria.meetsCriteria(data)) {
                qualityPass = false;
            }
        }
        return qualityPass;
    }

    /*
     * Adds criteria to the set of criterion that needs to be met.
     * Replaces previous criteria of the same type
     *
     * @params criteria     The criteria to add
     */
    public void addCriteria(QualityCriteria criteria) {
        criterion.put(criteria.getClass().toString(), criteria);
    }



    /*
     * Singleton design pattern
     * Can only have one controller across all process
     * */
    private static SNPQualityController instance;

    private SNPQualityController() {

    }

    public static SNPQualityController getInstance() {
        if(instance == null) {
            return new SNPQualityController();
        }
        return instance;
    }


    @Override
    public Iterator<QualityCriteria> iterator() {
        return criterion.values().iterator();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
