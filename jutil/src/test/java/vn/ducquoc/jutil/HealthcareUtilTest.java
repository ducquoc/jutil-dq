package vn.ducquoc.jutil;

/**
 * Tests for <code>vn.ducquoc.jutil.HeathcareUtil</code>.
 */
public class HealthcareUtilTest {

    @org.junit.Test
    public void testValidNpi() {
        org.junit.Assert.assertTrue(HealthcareUtil.luhnValidate("1234567893"));
        org.junit.Assert.assertTrue(HealthcareUtil.luhnValidate("1987654328"));
        org.junit.Assert.assertTrue(HealthcareUtil.luhnValidate("0000000014"));

        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("1234567893"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("1987654328"));

        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("808401234567893"));
        org.junit.Assert.assertTrue(HealthcareUtil.isValidNpi("808401987654328"));

        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("1234567890"));
        org.junit.Assert.assertFalse(HealthcareUtil.isValidNpi("1987654320"));

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

}
