package Tools;


public abstract class Tools{

//    protected String brand;
//    protected String code;
//    protected String type;
//    protected boolean weekdayCharge = true;
//    protected boolean weekendCharge = false;
//    protected boolean holidayCharge = false;
//    protected final float dailyCharge = 1.49f;

    public abstract String getBrand();

    public abstract String getCode();

    public abstract String getType();

    public abstract boolean getWeekdayCharge();
    public abstract boolean getWeekendCharge();
    public abstract boolean getHolidayCharge();
    public abstract float getDailyCharge();

}
