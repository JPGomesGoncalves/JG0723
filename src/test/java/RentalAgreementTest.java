import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RentalAgreementTest {

    @Test//test 1
    void createRentalAgreementInvalidDiscountTest() {
        RentalAgreement testRentalAgreement = new RentalAgreement();
        String toolCode = "JAKR";
        int rentalDays = 5;
        int discount = 101;
        LocalDate checkOutDate = LocalDate.of(2015,9,3);

        Exception exception = assertThrows(Exception.class, () -> testRentalAgreement.createRentalAgreement(toolCode, rentalDays,discount,checkOutDate));
        assertEquals("discount did not have a value from 0 to 100", exception.getMessage());
    }

    @Test//test 2
    void createRentalAgreementLadderNoHolidayChargeTest() throws Exception {
        RentalAgreement testRentalAgreement = new RentalAgreement();
        String toolCode = "LADW";
        int rentalDays = 3;
        int discount = 10;
        LocalDate checkOutDate = LocalDate.of(2020,7,2);

        testRentalAgreement = testRentalAgreement.createRentalAgreement(toolCode, rentalDays,discount,checkOutDate);

        assertEquals(2,testRentalAgreement.chargeDays);
        assertEquals(3.58f,testRentalAgreement.finalCharge, 0.01);
        assertEquals("Ladder",testRentalAgreement.toolType);
        assertEquals("Werner",testRentalAgreement.toolBrand);
        assertEquals(LocalDate.of(2020,7,5),testRentalAgreement.dueDate);
    }

    @Test//test 3
    void createRentalAgreementChainsawNoHolidayChargeTest() throws Exception {
        RentalAgreement testRentalAgreement = new RentalAgreement();
        String toolCode = "CHNS";
        int rentalDays = 5;
        int discount = 25;
        LocalDate checkOutDate = LocalDate.of(2015,7,2);

        testRentalAgreement = testRentalAgreement.createRentalAgreement(toolCode, rentalDays,discount,checkOutDate);

        assertEquals(3,testRentalAgreement.chargeDays);
        assertEquals(3.35f,testRentalAgreement.finalCharge, 0.01);
        assertEquals("Chainsaw",testRentalAgreement.toolType);
        assertEquals("Stihl",testRentalAgreement.toolBrand);
        assertEquals(LocalDate.of(2015,7,7),testRentalAgreement.dueDate);
    }

    @Test//test 4
    void createRentalAgreementJackHammerDHolidayTest() throws Exception {
        RentalAgreement testRentalAgreement = new RentalAgreement();
        String toolCode = "JAKD";
        int rentalDays = 6;
        int discount = 0;
        LocalDate checkOutDate = LocalDate.of(2015,9,3);

        testRentalAgreement = testRentalAgreement.createRentalAgreement(toolCode, rentalDays,discount,checkOutDate);

        assertEquals(3,testRentalAgreement.chargeDays);
        assertEquals(5.97f,testRentalAgreement.finalCharge, 0.01);
        assertEquals("Jackhammer",testRentalAgreement.toolType);
        assertEquals("Dewalt",testRentalAgreement.toolBrand);
        assertEquals(LocalDate.of(2015,9,9),testRentalAgreement.dueDate);
    }

    @Test//test 5
    void createRentalAgreementJackHammerRHolidayTest() throws Exception {
        RentalAgreement testRentalAgreement = new RentalAgreement();
        String toolCode = "JAKR";
        int rentalDays = 9;
        int discount = 0;
        LocalDate checkOutDate = LocalDate.of(2015,7,2);

        testRentalAgreement = testRentalAgreement.createRentalAgreement(toolCode, rentalDays,discount,checkOutDate);

        assertEquals(5,testRentalAgreement.chargeDays);
        assertEquals(9.95f,testRentalAgreement.finalCharge, 0.01);
        assertEquals("Jackhammer",testRentalAgreement.toolType);
        assertEquals("Ridgid",testRentalAgreement.toolBrand);
        assertEquals(LocalDate.of(2015,7,11),testRentalAgreement.dueDate);
    }

    @Test//test 6
    void createRentalAgreementJackHammerRHolidayDiscountTest() throws Exception {
        RentalAgreement testRentalAgreement = new RentalAgreement();
        String toolCode = "JAKR";
        int rentalDays = 4;
        int discount = 50;
        LocalDate checkOutDate = LocalDate.of(2020,7,20);

        testRentalAgreement = testRentalAgreement.createRentalAgreement(toolCode, rentalDays,discount,checkOutDate);

        assertEquals(4,testRentalAgreement.chargeDays);
        assertEquals(3.99f,testRentalAgreement.finalCharge, 0.01);
        assertEquals("Jackhammer",testRentalAgreement.toolType);
        assertEquals("Ridgid",testRentalAgreement.toolBrand);
        assertEquals(LocalDate.of(2020,7,24),testRentalAgreement.dueDate);
    }
}