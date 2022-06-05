package Infrastructure;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ButtonManager {
    public final static int TOP = 0;
    public final static int MID = 1;

    public final static int NA = 100;

    public final static int UP = 3;
    public final static int DOWN = 2;

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int SPECIAL_SECTION_SETTINGS_MENU = -1;


    private List<Button> top;
    private List<Button> mid;
    private List<Integer> sections;
    private HashMap<Integer,List<Button>> specialSections;
    private boolean isInSpecialSection;
    private boolean wasInSpecialSection;
    private int currentSection;
    private int previousSection;
    private int currentPosition;
    private int previousPosition;

    public ButtonManager(){
        top = new ArrayList<>();
        mid = new ArrayList<>();
        specialSections = new HashMap<Integer,List<Button>>();
        isInSpecialSection = false;
        wasInSpecialSection = false;
        sections = InitSections();
        currentPosition = 0;
        currentSection = MID;
        previousPosition = 0;
        previousSection = MID;
    }

    private ArrayList<Integer> InitSections(){
        ArrayList<Integer> ints = new ArrayList<>();

        ints.add(TOP);
        ints.add(MID);

        return ints;
    }

    public void SetTopButtons(List<Button> top){
        this.top = top;
    }
    public List<Button> GetTopButtons(){
        return  top;
    }

    public void AddTopButton(Button button){
        top.add(button);
    }

    public void AddTopButtons(List<Button> buttons){
        top.addAll(buttons);
    }

    public void ClearTopButtons(){
        top.clear();
    }

    public void AddMidButton(Button button){
        mid.add(button);
    }

    public void AddMidButtons(List<Button> buttons){
        mid.addAll(buttons);
    }

    public void SetMidButtons(List<Button> mid){
        this.mid = mid;
    }

    public void ClearMidButtons(){
        mid.clear();
    }

    public List<Button> GetMidButtons(){
        return  mid;
    }

    public int ChangePosition(int direction){
        boolean isChange = false;
        int predPos = currentPosition;

        if(isInSpecialSection && (direction == RIGHT || direction == DOWN) && currentPosition < GetSectionSize(currentSection)-1){
            currentPosition++;
            isChange = true;
        }
        else if(isInSpecialSection && (direction == LEFT || direction == UP) && currentPosition > 0){
            currentPosition--;
            isChange = true;
        }
        else if(direction == RIGHT && currentPosition < GetSectionSize(currentSection)-1){
            currentPosition++;
            isChange = true;
        }else if(direction == LEFT && currentPosition > 0){
            currentPosition--;
            isChange = true;
        }
        else if (ChangeSection(direction) && direction == UP){
            return UP;
        }
        else if (ChangeSection(direction) && direction == DOWN){
            return DOWN;
        }

        if(isChange){
            previousPosition = predPos;
            previousSection = currentSection;
        }

        if(isInSpecialSection && previousSection == currentSection){
            wasInSpecialSection = true;
        }else if(!isInSpecialSection && !specialSections.containsKey(previousSection)){
            wasInSpecialSection = false;
        }

            return NA;

    }

    private int GetSectionSize(int section){
        if(section == TOP){
            return top.size();
        }
        else if(section == MID){
            return mid.size();
        }else if(isInSpecialSection){
            return specialSections.get(currentSection).size();
        }
        return 0;
    }

    public int GetCurrentSection(){
        if(isInSpecialSection){
            return currentSection;
        }
        return sections.get(currentSection);
    }

    public int GetPreviousSection(){
        if(specialSections.containsKey(previousSection)){
            return previousSection;
        }
        return sections.get(previousSection);
    }

    public List<Button> GetCurrentSectionButtons(){
        if(isInSpecialSection){
            return specialSections.get(currentSection);
        }
        else if(currentSection == MID){
            return mid;
        }
        else{
            return top;
        }
    }

    public boolean ChangeSection(int direction){
        boolean isChange = false;
        int predSect = currentSection;

        if(!isInSpecialSection) {
            if (direction == UP && currentSection > 0) {
                currentSection--;
                isChange = true;
            } else if (direction == DOWN && currentSection < sections.size() - 1) {
                currentSection++;
                isChange = true;
            }
        }

        if(isChange){
            previousSection = predSect;
            previousPosition = currentPosition;
            currentPosition = 0;
        }

        return isChange;
    }

    public Button GetCurrentButton(){
        if (isInSpecialSection) {
            return specialSections.get(currentSection).get(currentPosition);
        }
        else if(sections.get(currentSection) == MID){
            return mid.get(currentPosition);
        } else if(sections.get(currentSection) == TOP){
            return top.get(currentPosition);
        }
        return null;
    }

    public Button GetPreviousButton(){
        if (specialSections.containsKey(previousSection)) {
            return specialSections.get(previousSection).get(previousPosition);
        }
        else if(sections.get(previousSection) == MID){
            return mid.get(previousPosition);
        }else if(sections.get(previousSection) == TOP){
            return top.get(previousPosition);
        }
        return null;
    }

    public Button SetToSpecialSection(int key){
        previousSection = currentSection;
        previousPosition = currentPosition;
        currentPosition = 0;
        currentSection = key;
        isInSpecialSection = true;

        return specialSections.get(key).get(currentPosition);

    }

    public void LeaveSpecialSection(int section, int position){
        int tempPredSection = currentSection;

        currentPosition = position;
        currentSection = section;
        previousSection = tempPredSection;
        isInSpecialSection = false;
    }

    public void AddNewSpecialSection(int key, List<Button> buttons){
        specialSections.put(key,buttons);
    }
    public void AddNewSpecialSection(int key, Button... buttons){
        List<Button> bs = new ArrayList<>();
        for(Button b:buttons){
            bs.add(b);
        }

        specialSections.put(key,bs);
    }

    public boolean IsInSpecialSection(){
        return isInSpecialSection;
    }
    public boolean WasInSpecialSection(){
        return wasInSpecialSection;
    }

    public int GetPreviousPosition(){
        return previousPosition;
    }

    public boolean IsSectionChanged(){
        if(previousSection == currentSection){
            return false;
        }

        return true;
    }
}
