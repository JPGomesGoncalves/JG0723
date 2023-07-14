import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.*;
import Enums.EnumToolCode;
import Tools.*;


public class App {
    //formattors for date and currenct
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Locale defaultFormattingLocale = Locale.getDefault(Locale.Category.FORMAT);
    private static final String defaultDateFormat = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, null, IsoChronology.INSTANCE, defaultFormattingLocale);
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(defaultDateFormat, defaultFormattingLocale);
    static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);


    //input validations
    public static String validateToolCodeInput(Scanner myScanner) throws Exception {
        System.out.println("Enter toolcode, current toolcodes are: " + Arrays.asList(EnumToolCode.values()));
        String toolCode = myScanner.nextLine();
        if(Arrays.stream(EnumToolCode.values()).anyMatch(index -> index.toString().equalsIgnoreCase(toolCode)) == false)
            throw new Exception();
        else {
            return toolCode;
        }
    }

    public static int validateRentalDays(Scanner myScanner) throws Exception {
        System.out.println("Enter rental day count");
        int RentalDays = Integer.parseInt(myScanner.nextLine());
        if (RentalDays <= 0)
            throw new Exception();
        return RentalDays;
    }

    public static int validateDiscount(Scanner myScanner) throws Exception {
        System.out.println("Enter discount");
        int discount = Integer.parseInt(myScanner.nextLine());
        if(discount < 0 || discount > 100)
            throw new Exception();
        return discount;
    }

    public static LocalDate validateCheckiytDate(Scanner myScanner) {
        LocalDate sampleDate = Year.now().atMonth(Month.NOVEMBER).atDay(26);
        System.out.println("Enter date in M/d/yy format, for example " + dateFormatter.format(sampleDate));
        String dateString = myScanner.nextLine();

        LocalDate checkoutDate = LocalDate.parse(dateString, dateFormatter);
        System.out.println("Date entered was " + dateFormatter.format(checkoutDate));

        return checkoutDate;
    }



    public static void main(String[] args) throws Exception {
        int rentalDays = 0;
        int discount = 0;
        LocalDate checkoutDate;
        String toolCode = "";

        boolean toolCodeinputPassed = false;
        boolean toolRentalCountPassed = false;
        boolean toolDiscountPassed = false;

        Scanner myScanner = new Scanner(System.in);  // Create a Scanner object
        RentalAgreement outcome = new RentalAgreement();

        //continue user input until correct values are given
        while(true){

            //validation for toolCode
            if (toolCodeinputPassed == false){
                try{
                    toolCode = validateToolCodeInput(myScanner);
                } catch (Exception e){
                    System.out.println( ANSI_RED +"Error: not a proper code, please try again" + ANSI_RESET);
                    continue;
                }finally {
                    System.out.println("\b") ;
                }
                toolCodeinputPassed = true;
            }


            //validation for rental count
            if (toolRentalCountPassed == false){
                try {
                    rentalDays = validateRentalDays(myScanner);
                } catch (Exception e){
                    System.out.println(ANSI_RED + "Error: please use number greater than 0, please try again" + ANSI_RESET);
                    continue;
                } finally {
                    System.out.println("\b") ;
                }
                toolRentalCountPassed = true;
            }

            //validation for discount
            if (toolDiscountPassed == false){
                try {
                    discount = validateDiscount(myScanner);
                }catch (Exception e){
                    System.out.println(ANSI_RED + "Error: please use a valued number from 0 to 100, please try again" + ANSI_RESET);
                    continue;
                }finally {
                    System.out.println("\b") ;
                }
                toolDiscountPassed = true;
            }

            //validation for checkout date
            try {
                checkoutDate = validateCheckiytDate(myScanner);
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Error: Invalid date, please try again" + ANSI_RESET);
                continue;
            }finally {
                System.out.println("\b") ;
            }
            break;
        }
            //set all rental agreements fields
            outcome = outcome.createRentalAgreement(toolCode,rentalDays,discount,checkoutDate);

            outcome.printAgrement();

    }
}
