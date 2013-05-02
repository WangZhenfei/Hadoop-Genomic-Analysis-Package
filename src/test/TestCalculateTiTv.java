package test;

import main.vcf.CalculateTiTv;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: sibonli
 * Date: 4/19/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 *
 * @author Wai Lok Sibon Li
 */
public class TestCalculateTiTv {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testKnownCalculations() {
        // todo add same data from tested examples of ts/tv calculation
    }

    @Test
    public void testTransitionTable() {
        CalculateTiTv.SubstitutionType cs;

        cs = CalculateTiTv.SubstitutionType.substitutionLookup("A", "G");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSITION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("G", "A");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSITION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("C", "T");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSITION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("T", "C");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSITION);

        cs = CalculateTiTv.SubstitutionType.substitutionLookup("A", "C");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("A", "T");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("C", "A");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("T", "A");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("G", "C");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("G", "T");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("C", "G");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("T", "G");
        assertEquals(cs, CalculateTiTv.SubstitutionType.TRANSVERSION);

        cs = CalculateTiTv.SubstitutionType.substitutionLookup("A", "A");
        assertEquals(cs, CalculateTiTv.SubstitutionType.IDENTICAL);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("C", "C");
        assertEquals(cs, CalculateTiTv.SubstitutionType.IDENTICAL);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("G", "G");
        assertEquals(cs, CalculateTiTv.SubstitutionType.IDENTICAL);
        cs = CalculateTiTv.SubstitutionType.substitutionLookup("T", "T");
        assertEquals(cs, CalculateTiTv.SubstitutionType.IDENTICAL);
    }
}
