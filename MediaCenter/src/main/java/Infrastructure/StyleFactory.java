package Infrastructure;

import Models.ColorInformation;
import javafx.scene.paint.Color;

public class StyleFactory {
    public static String GetTextStyle(ColorInformation colorInformation){
        return "-fx-text-fill:"+FormatRGBA(colorInformation.getTextColor())+";-fx-font-size: 50;";
    }
    public static String GetBorderStyle(ColorInformation colorInformation){
        return "-fx-border-style: solid outside; -fx-border-width: 6; -fx-border-radius: 5; -fx-border-color:"+FormatRGBA(colorInformation.getBorderColor())+";";
    }
    public static String GetPrimaryStyle(ColorInformation colorInformation){
        return "-fx-text-fill:"+FormatRGBA(colorInformation.getPrimaryColor())+";";
    }
    public static String GetSecondaryStyle(ColorInformation colorInformation){
        return "-fx-text-fill:"+FormatRGBA(colorInformation.getSecondaryColor())+";";
    }

    public static String FormatRGBA(Color color){
        return "rgba(red,green,blue,alpha);".replace("red,",color.getRed()*255+",").replace("green,",color.getGreen()*255+",").replace("blue,",color.getBlue()*255+",").replace("alpha)",color.getOpacity()*255+")");
    }
}
