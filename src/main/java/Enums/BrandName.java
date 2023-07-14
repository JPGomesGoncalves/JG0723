package Enums;
public enum BrandName {
    STIHL("Stihl"),
    WERNER("Werner"),
    DEWALT("Dewalt"),
    RIDGID("Ridgid");

    public final String label;

    private BrandName(String label) {
        this.label = label;
    }
}
