package Tools;

public class Chainsaw extends Tools{
    
    protected String brand;
    protected String code;
    protected String type;
    private final boolean weekdayCharge = true;
    private final boolean weekendCharge = false;
    private final boolean holidayCharge = true;
    private final float dailyCharge = 1.49f;

    public Chainsaw(String brand, String code){
        this.brand = brand;
        this.code = code;
        this.type = "Chainsaw";
    }

    public boolean getWeekdayCharge(){
        return weekdayCharge;
    }
    public boolean getWeekendCharge(){
        return weekendCharge;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getType() {
        return type;
    }

    public boolean getHolidayCharge(){
        return holidayCharge;
    }
    public float getDailyCharge(){
        return dailyCharge;
    }
}
