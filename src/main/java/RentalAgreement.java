import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import Enums.EnumToolCode;
import Tools.Tools;

public class RentalAgreement {
    String toolCode = "";
    String toolType = "";
    String toolBrand = "";
    int rentalDays = -1;
    LocalDate checkedOutDate;
    LocalDate dueDate;
    float dailyRentalCharge = -1;
    int chargeDays = -1;
    float preDiscountCharge;
    int discountPercent = -1;
    float discountAmount = -1;
    float finalCharge = -1;

    //this is for Jackhammer, removes charges on weekends and holidays
    private int countWeekdays(final LocalDate startDate,final LocalDate endDate,final List<LocalDate> holidays)
    {
        // Validate method arguments
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to countWeekdays (" + startDate + "," + endDate + "," + holidays + ")");
        }

        // Predicate 1: Is given date is a holiday
        Predicate<LocalDate> isHoliday = date -> holidays.contains(date);

        // Predicate 2: Is given date a weekend
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Get all days between two dates
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // Iterate over stream of all dates and check each day against any weekday or holiday
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(isHoliday.or(isWeekend).negate())
                .toList().size();
    }
    //this is for ladder, removes holidays charges
    private int countWeekdaysAndWeekends(final LocalDate startDate,final LocalDate endDate,final List<LocalDate> holidays)
    {
        // Validate method arguments
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to countWeekdaysAndWeekends (" + startDate + "," + endDate + "," + holidays + ")");
        }

        // Predicate 1: Is given date is a holiday
        Predicate<LocalDate> isHoliday = date -> holidays.contains(date);

        // Get all days between two dates
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // Iterate over stream of all dates and check each day against any weekday or holiday
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(isHoliday.negate())
                .toList().size();
    }
    //this is for chainsaw, method removes charges on weekends
    private int countWeekDaysAndHolidays(final LocalDate startDate,final LocalDate endDate,final List<LocalDate> holidays)
    {
        // Validate method arguments
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to countWeekDaysAndHolidays (" + startDate + "," + endDate + "," + holidays + ")");
        }

        // Predicate 2: Is the given date a weekday
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Get all days between two dates
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // Iterate over stream of all dates and check each day against any weekday and holiday
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(isWeekend.negate())
                .toList().size();
    }
    public RentalAgreement createRentalAgreement(String toolCode, int rentalDays, int discount, LocalDate checkoutDate) throws Exception {
        try{
            if(discount > 100 || discount < 0){
                throw new Exception("discount did not have a value from 0 to 100");
            } else if(rentalDays < 1){
                throw new Exception("rentalDays cannot be less then 1");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

        RentalAgreement outcome = new RentalAgreement();
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int rentalChargeDays = 0;
        Tools checkOutTools =  ToolFactory.getTools(EnumToolCode.valueOf(toolCode.toUpperCase()));

        LocalDate IndependenceDay = (LocalDate.of(checkoutDate.getYear(),7,4));
        LocalDate laborDay = LocalDate.of(checkoutDate.getYear(),9,1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        if (IndependenceDay.getDayOfWeek() == DayOfWeek.SATURDAY){
            IndependenceDay = IndependenceDay.minusDays(1);
        } else if (IndependenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            IndependenceDay = IndependenceDay.plusDays(1);
        }

        //gives a list of holidays
        List<LocalDate> holiday = new ArrayList<>(
                Arrays.asList(
                        IndependenceDay,
                        laborDay)
                );

        //check and logic for charges of weekdays/weekends/Holidays
        if (checkOutTools.getWeekdayCharge() && !checkOutTools.getWeekendCharge() && !checkOutTools.getHolidayCharge()){
            rentalChargeDays = countWeekdays(checkoutDate.plusDays(1),dueDate.plusDays(1), holiday);
        } else if(checkOutTools.getWeekdayCharge() && !checkOutTools.getWeekendCharge() && checkOutTools.getHolidayCharge()){
            rentalChargeDays = countWeekDaysAndHolidays(checkoutDate.plusDays(1),dueDate.plusDays(1), holiday);
        } else if(checkOutTools.getWeekdayCharge() && checkOutTools.getWeekendCharge() && !checkOutTools.getHolidayCharge()) {
            rentalChargeDays = countWeekdaysAndWeekends(checkoutDate.plusDays(1),dueDate.plusDays(1), holiday);
        }

        //final charge calculations with rounding to
        float dailyRentalCharge = new BigDecimal(checkOutTools.getDailyCharge()).setScale(2, RoundingMode.HALF_UP).floatValue();
        float preDiscountCharge = new BigDecimal(checkOutTools.getDailyCharge() * rentalChargeDays).setScale(2, RoundingMode.HALF_UP).floatValue();
        float discountAmount = new BigDecimal(((float)discount/100) * preDiscountCharge).setScale(2, RoundingMode.HALF_UP).floatValue();
        float finalCharge = new BigDecimal(preDiscountCharge - discountAmount).setScale(2, RoundingMode.HALF_UP).floatValue();

        outcome.toolCode = checkOutTools.getCode();
        outcome.toolType = checkOutTools.getType();
        outcome.toolBrand = checkOutTools.getBrand();
        outcome.rentalDays = rentalDays;
        outcome.checkedOutDate = checkoutDate;
        outcome.dueDate = dueDate;
        outcome.dailyRentalCharge = dailyRentalCharge;
        outcome.chargeDays = rentalChargeDays;
        outcome.preDiscountCharge = preDiscountCharge;
        outcome.discountPercent = discount;
        outcome.discountAmount = discountAmount;
        outcome.finalCharge = finalCharge;

        return outcome;
    }

    public void printAgrement(){

        System.out.println("\b") ;
        System.out.println("\b") ;

        String results = "Tool Code: " + getToolCode() +
                "\n Tool Type: " + getToolType() +
                "\n Tool Brand: " + getToolBrand() +
                "\n Rental Days: " + getRentalDays() +
                "\n Checked Out Date: " + App.dateFormatter.format(getCheckedOutDate()) +
                "\n Due Date: " + App.dateFormatter.format(getDueDate()) +
                "\n Daily Rental Charge: " + App.currencyFormatter.format(getDailyRentalCharge())+
                "\n Charge Days: " + getChargeDays() +
                "\n PreDiscount Charge: " + App.currencyFormatter.format(getPreDiscountCharge()) +
                "\n Discount Percent: " + getDiscountPercent() + "%" +
                "\n Discount Amount: " + App.currencyFormatter.format(getDiscountAmount()) +
                "\n Final Charge: " + App.currencyFormatter.format(getFinalCharge());
        System.out.println(results);
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public LocalDate getCheckedOutDate() {
        return checkedOutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public float getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public float getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public float getFinalCharge() {
        return finalCharge;
    }
}
