package vn.ducquoc.jutil;

/**
 * Tests for <code>vn.ducquoc.jutil.HeathcareUtil</code>.
 */
public class HealthcareUtilTest {

    @org.junit.Test
    public void testValidNpi() {
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("1234567893"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("1987654328"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("1234567890"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("1987654320"));
    }

    @org.junit.Test
    public void testValidNpi_WithPrefix() {
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("808401234567893"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("808401987654328"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("808401234567890"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("808401987654320"));
    }

    @org.junit.Test
    public void testValidNpi_CornerCases() {
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("0000000006"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("0000000000"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("00000000006"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("000000006"));

        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("0000000014"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("00000000144"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("00000000014"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("000000014"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("0123456789"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("0"));
    }

    @org.junit.Test
    public void testValidDea() {
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("1234563"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("1987657"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("1234560"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("1987650"));
    }

    @org.junit.Test
    public void testValidDea_WithPrefix() {
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("AM1234563"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("FP1987657"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AM1234560"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("FP1987650"));
    }

    @org.junit.Test
    public void testValidDea_CornerCases() {
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("0000000"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("AA0000000"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidDea("AA0000012"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA0000001"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA00000000"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA00000001"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA00000010"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA00000012"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA000000"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA000001"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA000010"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidDea("AA000012"));
    }

}
