package vn.ducquoc.jutil;

import org.junit.Assert;

/**
 * Tests for <code>vn.ducquoc.jutil.HeathcareUtil</code>.
 */
public class HealthcareUtilTest {

    @org.junit.Test
    public void testValidNpi() {
        Assert.assertTrue(HealthcareUtil.isValidNpi("1234567893"));
        Assert.assertTrue(HealthcareUtil.isValidNpi("1987654328"));

        Assert.assertFalse(HealthcareUtil.isValidNpi("1234567890"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("1987654320"));
    }

    @org.junit.Test
    public void testValidNpi_WithPrefix() {
        Assert.assertTrue(HealthcareUtil.isValidNpi("808401234567893"));
        Assert.assertTrue(HealthcareUtil.isValidNpi("808401987654328"));

        Assert.assertFalse(HealthcareUtil.isValidNpi("808401234567890"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("808401987654320"));
    }

    @org.junit.Test
    public void testValidNpi_CornerCases() {
        Assert.assertTrue(HealthcareUtil.isValidNpi("0000000006"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("0000000000"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("00000000006"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("000000006"));

        Assert.assertTrue(HealthcareUtil.isValidNpi("0000000014"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("00000000144"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("00000000014"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("000000014"));

        Assert.assertFalse(HealthcareUtil.isValidNpi("0123456789"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("06"));
        Assert.assertFalse(HealthcareUtil.isValidNpi("0"));
    }

    @org.junit.Test
    public void testValidDea_WrongPrefix() {
        Assert.assertFalse(HealthcareUtil.isValidDea("1234563"));
        Assert.assertFalse(HealthcareUtil.isValidDea("1987657"));

        Assert.assertFalse(HealthcareUtil.isValidDea("1234560"));
        Assert.assertFalse(HealthcareUtil.isValidDea("1987650"));

        Assert.assertFalse(HealthcareUtil.isValidDea("CM1234563"));
        Assert.assertFalse(HealthcareUtil.isValidDea("DM1234563"));
        Assert.assertFalse(HealthcareUtil.isValidDea("EM1234563"));

        Assert.assertFalse(HealthcareUtil.isValidDea("GP1987657"));
        Assert.assertFalse(HealthcareUtil.isValidDea("HP1987657"));
        Assert.assertFalse(HealthcareUtil.isValidDea("IP1987657"));
    }

    @org.junit.Test
    public void testValidDea_WithPrefix() { // starts with "A,B,F,M,P,R"
        Assert.assertTrue(HealthcareUtil.isValidDea("AM1234563"));
        Assert.assertTrue(HealthcareUtil.isValidDea("FP1987657"));

        Assert.assertFalse(HealthcareUtil.isValidDea("AM1234560"));
        Assert.assertFalse(HealthcareUtil.isValidDea("FP1987650"));

        Assert.assertTrue(HealthcareUtil.isValidDea("BM1234563"));
        Assert.assertTrue(HealthcareUtil.isValidDea("FM1234563"));
        Assert.assertTrue(HealthcareUtil.isValidDea("PM1234563"));
        Assert.assertTrue(HealthcareUtil.isValidDea("RM1234563"));
        Assert.assertTrue(HealthcareUtil.isValidDea("MM1234563"));
    }

    @org.junit.Test
    public void testValidDea_CornerCases() {
        Assert.assertTrue(HealthcareUtil.isValidDea("AZ0000000"));
        Assert.assertTrue(HealthcareUtil.isValidDea("AA0000000"));
        Assert.assertTrue(HealthcareUtil.isValidDea("AA0000012"));

        Assert.assertFalse(HealthcareUtil.isValidDea("AA0000001"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA00000000"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA00000001"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA00000010"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA00000012"));

        Assert.assertFalse(HealthcareUtil.isValidDea("AA000000"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA000001"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA000010"));
        Assert.assertFalse(HealthcareUtil.isValidDea("AA000012"));
    }

}
