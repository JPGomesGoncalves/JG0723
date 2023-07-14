import Enums.BrandName;
import Enums.EnumToolCode;
import Tools.*;

public class ToolFactory {

    public static Tools getTools(EnumToolCode toolCode){
        if (toolCode == null)
            return null;

        final String toolString = toolCode.toString();

        switch (toolCode){
            case CHNS:
                return new Chainsaw(BrandName.STIHL.label, toolString);
            case JAKD:
                return new Jackhammer(BrandName.DEWALT.label,toolString);
            case LADW:
                return new Ladder(BrandName.WERNER.label,toolString);
            case JAKR:
                return new Jackhammer(BrandName.RIDGID.label,toolString);
        }
        return null;
    }
}
