package Infrastructure;

import Models.BasicInformation;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataSaver {

    private final static String path = Thread.currentThread().getContextClassLoader().getResource("SaveData").getPath();
    private final static String seperator = "#:#";
    private final static String listSeperator = ",";

    public static String GetCurrentUserName(){
        BasicInformation bi = new BasicInformation();
        File file = new File(path,GetFolder(bi));
        String currentUserName = null;
        Scanner scanner;


        for(File f:file.listFiles()){
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                return null;
            }

            try {
                ReadObject(bi,scanner);
            } catch (InvocationTargetException e) {
                scanner.close();
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                scanner.close();
                throw new RuntimeException(e);
            }
            scanner.close();

            if(bi.getIsCurrentActive()){
                currentUserName = bi.getUserName();
                break;
            }
        }
        return currentUserName;
    }

    public static List<Object> LoadManyObjects(Object obj, String userName){
        Scanner scanner = null;
        List<Object> objs = new ArrayList<>();

        try {
            scanner = new Scanner(new File(new File(path,GetFolder(obj)).getAbsolutePath(), userName+".txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            objs = ReadManyObjects(obj, scanner);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            scanner.close();
            throw new RuntimeException(e);
        }

        return objs;
    }

    public static Object LoadObject(Object obj, String userName){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(new File(path,GetFolder(obj)).getAbsolutePath(), userName+".txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            ReadObject(obj, scanner);
        } catch (InvocationTargetException e) {
            scanner.close();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            scanner.close();
            throw new RuntimeException(e);
        }

        return obj;
    }

    private static List<Object> ReadManyObjects(Object obj, Scanner scanner) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        String contentsToLoad = "";
        String curLine;
        String objName = GetFolder(obj);
        List<Object> objs = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        Scanner tempScanner;
        boolean hasScanned = false;

        while(scanner.hasNextLine()){
            curLine = scanner.nextLine();
            if(!curLine.contains(objName)){
                contentsToLoad += curLine + '\n';
                hasScanned = true;
            }
            else if (hasScanned){
                hasScanned = false;
                objs.add(obj.getClass().getConstructor().newInstance());
                lines.add(contentsToLoad);
                contentsToLoad = "";
            }
        }
        objs.add(obj.getClass().getConstructor().newInstance());
        lines.add(contentsToLoad);

        for(int i = 0; i < objs.size(); i++){
            tempScanner = new Scanner(lines.get(i));
            ReadObject(objs.get(i), tempScanner);
            tempScanner.close();
        }

        return objs;
    }
    private static void ReadObject(Object obj,Scanner scanner) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = obj.getClass().getMethods();

        while(scanner.hasNextLine()){
            String temp = scanner.nextLine();
            String[] line = temp.split(seperator);

            for(Method method:methods){
                if(method.getName().equals("set"+line[0])){
                    LoadArgument(method, obj, BuildValue(line));
                    break;
                }
            }
        }
    }

    private static void LoadArgument(Method method, Object obj ,String value) throws InvocationTargetException, IllegalAccessException {
        Parameter param = method.getParameters()[0];

        if(param.getType().equals(String.class)) {
            method.invoke(obj, value);
        } else if (param.getType().equals(int.class)) {
            method.invoke(obj, Integer.parseInt(value));
        }else if (param.getType().equals(boolean.class)) {
            method.invoke(obj, Boolean.parseBoolean(value));
        }else if (param.getType().equals(List.class)) {
            List<Object> lst = new ArrayList<>();
            for(String element:value.split(listSeperator)){
                lst.add(element);
            }
            method.invoke(obj, lst);
        }else if(param.getType().equals(Color.class)){
            String[] rgba = value.split(",");
            method.invoke(obj, new Color(Double.parseDouble(rgba[0]),Double.parseDouble(rgba[1]),Double.parseDouble(rgba[2]),Double.parseDouble(rgba[3])));
        }
    }

    private static String BuildValue(String[] strs){
        if(strs.length == 2){
            return strs[1];
        }

        String retVal = "";

        for(int i = 1; i < strs.length; i++){
            retVal += strs[i];
        }

        return retVal;
    }

    public static void SaveObject(String userName, Object... objs){
        for(Object obj:objs) {
            if (IsIterable(obj) && ((List)obj).size() > 0) {
                CheckCreateFolderStructure(path, ((List)obj).get(0));
            } else {
                CheckCreateFolderStructure(path, obj);
            }

            try {
                WriteModelToFile(obj, path, userName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static void WriteModelToFile(Object model, String path, String userName) throws FileNotFoundException {
        String subDir;

        if(IsIterable(model)){
            subDir = GetFolder((((List)model).get(0)));
        }
        else {
            subDir = GetFolder(model);
        }

        WriteModelToFile(model, new File(new File(path, subDir).getAbsolutePath(), userName + ".txt").getPath());
    }

    private static void WriteModelToFile(Object model, String path) throws FileNotFoundException {
        PrintStream ps = new PrintStream(path);

        if(IsIterable(model)){
            ArrayList iterable = (ArrayList)model;

            for(int i = 0; i < iterable.size(); i++) {
                ps.println(GetFolder(model)+i);
                WriteModelToFile(iterable.get(i),path,ps,iterable.get(0).getClass().getMethods());
            }
        }
        else {
            WriteModelToFile(model,path,ps,model.getClass().getMethods());
        }

        ps.close();
    }

    private static boolean IsIterable(Object obj){
        return obj instanceof Iterable;
    }

    private static void WriteModelToFile(Object model, String path, PrintStream ps, Method[] methods){
        String toWrite;

        for (Method method : methods) {
            toWrite = "";
            if (method.getName().contains("get")) {
                try {
                    if (method.getReturnType().equals(String.class)) {
                        toWrite = method.getName().replace("get", "") + seperator + (String) method.invoke(model);
                    } else if (method.getReturnType().equals(int.class)) {
                        toWrite = method.getName().replace("get", "") + seperator + (int) method.invoke(model);
                    }else if (method.getReturnType().equals(boolean.class)) {
                        toWrite = method.getName().replace("get", "") + seperator + (boolean) method.invoke(model);
                    } else if (method.getReturnType().equals(List.class)) {
                        toWrite = method.getName().replace("get", "") + seperator+listSeperator;
                        for (Object obj : (List<Object>) method.invoke(model)) {
                            toWrite += listSeperator + obj.toString();
                        }
                        toWrite = toWrite.replace(listSeperator+listSeperator, "");
                    } else if (method.getReturnType().equals(Color.class)) {
                        Color color = (Color) method.invoke(model);
                        toWrite = method.getName().replace("get", "") + seperator + color.getRed()+","+color.getGreen()+","+ color.getBlue()+","+ color.getOpacity();
                    }
                } catch (IllegalAccessException e) {
                    ps.close();
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    ps.close();
                    throw new RuntimeException(e);
                }
                if (!toWrite.equals("")) {
                    ps.println(toWrite);
                }
            }
        }
    }

    private static String GetFolder(Object model){
        return model.getClass().getName();
    }

    private static void CheckCreateFolderStructure(String path, Object... models){

        for(Object model:models){
            File dir = new File(path,GetFolder(model));

            if(!dir.exists()){
                dir.mkdir();
            }
        }

        //Path folderPath = Path.of(Thread.currentThread().getContextClassLoader().getResource("SaveData").getPath());


    }
}
