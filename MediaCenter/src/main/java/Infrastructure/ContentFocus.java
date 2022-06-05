package Infrastructure;

import Models.ColorInformation;
import Models.ContentButton;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.util.List;

public class ContentFocus {
    private ColorInformation color;
    private ScrollPane scrollPane;
    private double screenWidth;
    private ButtonManager buttonManager;
    private Button previousMouseButton;

    public ContentFocus(ColorInformation color){
        buttonManager = new ButtonManager();
        this.color = color;
    }

    public ButtonManager GetButtonManager(){
        return buttonManager;
    }

    public void UpdateScreenWidth(double screenWidth){
        this.screenWidth = screenWidth;
    }

    public void DisablePane(){
        scrollPane.setDisable(true);
    }
    public void EnablePane(){
        scrollPane.setDisable(false);
    }

    public void UpdateScrollPane(ScrollPane scrollPane){
        this.scrollPane = scrollPane;
    }

    public void UpdateContentButtons(List<ContentButton> buttons){
        List<Button> bs = new ArrayList<Button>();
        for(ContentButton cb : buttons) {
            bs.add(cb.getButton());
        }
        buttonManager.SetMidButtons(bs);

        if(scrollPane != null) {
            scrollPane.setVvalue(0);
        }

        if(buttons.size() > 0) {
            SetFocus();
        }
    }

    public void UpdateBorderColor(ColorInformation color){
        this.color = color;
    }

    public void UpdateCurrentFocused(KeyCode key){
        if (key.getName().equals("Left")) {
            buttonManager.ChangePosition(ButtonManager.LEFT);
            if(buttonManager.IsInSpecialSection()){
                //SetFocus(buttonManager.GetCurrentButton().getStyle());
            }else {
                SetFocus();
            }
            ScrollToButtonCenter(false);
        } else if (key.getName().equals("Right")) {
            buttonManager.ChangePosition(ButtonManager.RIGHT);
            if(buttonManager.IsInSpecialSection()){
                //SetFocus(buttonManager.GetCurrentButton().getStyle());
            }else {
                SetFocus();
            }
            ScrollToButtonCenter(true);
        } else if (key.getName().equals("Up")) {
            if(buttonManager.ChangePosition(ButtonManager.UP) == ButtonManager.UP) {
                SetFocus();
                ResetScrollPanelView();
            }
            else{
                SetFocus(buttonManager.GetCurrentButton().getStyle());
                ResetScrollPanelView();
            }
        }else if (key.getName().equals("Down")) {
            if(buttonManager.ChangePosition(ButtonManager.DOWN) == ButtonManager.DOWN){
                SetFocus();
            }
            else{
                SetFocus(buttonManager.GetCurrentButton().getStyle());
                ResetScrollPanelView();
            }
        }
    }

    public void SetFocus(Button button, boolean isUpdatePreviousbutton, String style, boolean isMouse) {
        if (isMouse) {
            if (previousMouseButton != null) {
                previousMouseButton.setStyle(style);
                previousMouseButton.setDisable(true);

                if (buttonManager.GetPreviousSection() != ButtonManager.MID) {
                    previousMouseButton.setDisable(false);
                } else {
                    previousMouseButton.setDisable(false);
                    previousMouseButton.setOpacity(0.5);
                }
            }


            if (isUpdatePreviousbutton) {
                previousMouseButton = button;
            }

        }else{
            buttonManager.GetPreviousButton().setStyle(style.replace(StyleFactory.GetBorderStyle(color),""));
            buttonManager.GetPreviousButton().setDisable(true);

            if(buttonManager.GetPreviousSection() != ButtonManager.MID){
                buttonManager.GetPreviousButton().setDisable(false);
            }
            else{
                buttonManager.GetPreviousButton().setDisable(false);
                buttonManager.GetPreviousButton().setOpacity(0.5);
            }
        }

        buttonManager.GetCurrentButton().setStyle(style);
        buttonManager.GetCurrentButton().setDisable(true);
        buttonManager.GetCurrentButton().setDisable(false);
        buttonManager.GetCurrentButton().setOpacity(0.5);

        button.setOpacity(1);
        button.requestFocus();
        button.setStyle(style + StyleFactory.GetBorderStyle(color));
    }

    public void SetFocus(Button button, boolean isUpdatePreviousbutton) {
        if (previousMouseButton != null) {
            previousMouseButton.setStyle("");
            previousMouseButton.setDisable(true);

            if(buttonManager.GetPreviousSection() != ButtonManager.MID) {
                previousMouseButton.setDisable(false);
            }else {
                previousMouseButton.setDisable(false);
                previousMouseButton.setOpacity(0.5);
            }
        }

        if (isUpdatePreviousbutton) {
            previousMouseButton = button;
        }

        buttonManager.GetCurrentButton().setStyle("");
        buttonManager.GetCurrentButton().setDisable(true);
        buttonManager.GetCurrentButton().setDisable(false);
        buttonManager.GetCurrentButton().setOpacity(0.5);

        button.setOpacity(1);
        button.requestFocus();
        button.setStyle(StyleFactory.GetBorderStyle(color));
    }

    private void SetFocus(String style){
        buttonManager.GetPreviousButton().setStyle(style.replace(StyleFactory.GetBorderStyle(color),""));
        buttonManager.GetPreviousButton().setDisable(true);

        if(buttonManager.GetPreviousSection() != ButtonManager.MID){
            buttonManager.GetPreviousButton().setDisable(false);
        }
        else{
            buttonManager.GetPreviousButton().setDisable(false);
            buttonManager.GetPreviousButton().setOpacity(0.5);
        }

        buttonManager.GetCurrentButton().setOpacity(1);
        buttonManager.GetCurrentButton().requestFocus();
        buttonManager.GetCurrentButton().setStyle(style+StyleFactory.GetBorderStyle(color));
    }

    private void SetFocus(){
        if(buttonManager.WasInSpecialSection()) {
            buttonManager.GetPreviousButton().setStyle(buttonManager.GetPreviousButton().getStyle().replace(StyleFactory.GetBorderStyle(color),""));
        }
        else{
            buttonManager.GetPreviousButton().setStyle("");
        }
        buttonManager.GetPreviousButton().setDisable(true);

        if(buttonManager.GetPreviousSection() != ButtonManager.MID){
            buttonManager.GetPreviousButton().setDisable(false);
        }
        else{
            buttonManager.GetPreviousButton().setDisable(false);
            buttonManager.GetPreviousButton().setOpacity(0.5);
        }

        buttonManager.GetCurrentButton().setOpacity(1);
        buttonManager.GetCurrentButton().requestFocus();
        buttonManager.GetCurrentButton().setStyle(StyleFactory.GetBorderStyle(color));
    }

    private void ResetScrollPanelView(){
        scrollPane.setHvalue(0);
    }

    private void ScrollToButtonCenter(boolean isRight){
        if(isRight && scrollPane.getHvalue() < scrollPane.getHmax()){
            double move = scrollPane.getHvalue() + (buttonManager.GetCurrentButton().getWidth()/screenWidth);

            if(move <= scrollPane.getHmax()){
                scrollPane.setHvalue(move);
            }
        }
        else if(!isRight){
            double move = scrollPane.getHvalue() - (buttonManager.GetCurrentButton().getWidth()/screenWidth);

            if(move >= 0){
                scrollPane.setHvalue(move);
            }

        }
    }

    public Button GetFocusedButton(){
        return buttonManager.GetCurrentButton();
    }
}
