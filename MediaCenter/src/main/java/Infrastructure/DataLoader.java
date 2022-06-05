package Infrastructure;

import Models.App;
import Models.BasicInformation;
import Models.ColorInformation;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private final static String currentUserName = DataSaver.GetCurrentUserName();
    public static BasicInformation LoadBasicData(){
        BasicInformation basicInformation = new BasicInformation();

        DataSaver.LoadObject(basicInformation, currentUserName);
        return basicInformation;
    }

    public static List<App> LoadApps(){
        List<App> apps = new ArrayList<>();

        for(Object obj:DataSaver.LoadManyObjects(new App(), currentUserName)){
            apps.add((App)obj);
        }

        return apps;
    }

    public static ColorInformation LoadColorInformation(){
        ColorInformation colorInformation = new ColorInformation();

        DataSaver.LoadObject(colorInformation, currentUserName);
        return colorInformation;
    }
}
