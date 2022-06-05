package Models;

import java.util.List;

public class App {
    private String name;
    private String path;
    private List<String> arguments;
    private String imagePath;
    private Process process;
    private int appType;

    private boolean isShownOnHomeScreen;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public boolean getIsShownOnHomeScreen() {
        return isShownOnHomeScreen;
    }

    public void setIsShownOnHomeScreen(boolean shownOnHomeScreen) {
        isShownOnHomeScreen = shownOnHomeScreen;
    }

    public String getCommandLineCommand(){
        String command;

        if(!path.contains("\"")){
           command = "\"" + path + "\"";
        }
        else{
            command = path;
        }

        if(arguments != null) {
            for (String arg : arguments) {
                command += " " + arg;
            }
        }

        return command;
    }
}
