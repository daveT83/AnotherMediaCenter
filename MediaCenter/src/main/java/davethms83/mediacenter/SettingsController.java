package davethms83.mediacenter;

import Infrastructure.StyleFactory;
import Models.BasicInformation;
import Models.ColorInformation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SettingsController {
    public static final int ACCOUNT = 0;
    public static final int GENERAL = 1;
    public static final int APPEARANCE = 2;

    @FXML
    private Panel background;
    @FXML
    private TabPane tabs;
    @FXML
    private Tab accountTab;
    @FXML
    private ImageView profilePicImage;
    @FXML
    private TextField profilePicPath;
    @FXML
    private Button profilePicSelectPathButton;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmNewPasswordField;
    @FXML
    private CheckBox isRequiredPasswordCheckbox;

    @FXML
    private Label userName;
    @FXML
    private Label newPassword;
    @FXML
    private Label confirmNewPassword;
    @FXML
    private Label requirePassword;
    @FXML
    private ScrollPane accountScroll;
    @FXML
    private Button applyButton;
    @FXML
    private Button cancelButton;

    private BasicInformation basicInfo;
    private ColorInformation colorInfo;
    private int currentView = -1;
    private Stage stage;
    private Scene mainScene;


    public void SetScene(Scene scene){
        this.mainScene = scene;
    }
    public void SetStage(Stage stage){
        this.stage = stage;
    }

    public void SetBasicInformation(BasicInformation basicInfo){
        this.basicInfo = basicInfo;
    }

    public void SetColorInformation(ColorInformation colorInfo){
        this.colorInfo = colorInfo;
    }

    public void InitSettings(){
        InitAccountSettings();
    }

    private void InitAccountSettings(){
        accountScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        accountScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
       // profilePicImage.setImage(new Image(basicInfo.getUserPicturePath()));
        profilePicPath.setText(basicInfo.getUserPicturePath());
        profilePicSelectPathButton.setText("...");
        userNameField.setText(basicInfo.getUserName());
        isRequiredPasswordCheckbox.setSelected(basicInfo.getIsPasswordRequired());

        SetTextFieldStyle(profilePicPath);
        SetTextFieldStyle(userNameField);
        SetTextFieldStyle(newPasswordField);
        SetTextFieldStyle(confirmNewPasswordField);

        SetTextStyle(userName);
        SetTextStyle(newPassword);
        SetTextStyle(confirmNewPassword);
        SetTextStyle(requirePassword);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(mainScene);
            }
        });

        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean canExit = true;
                basicInfo.setUserName(userNameField.getText());
                basicInfo.setUserPicturePath(profilePicPath.getText());
                basicInfo.setPasswordRequired(isRequiredPasswordCheckbox.isSelected());

                if(newPasswordField.getText().equals(confirmNewPassword.getText())){
                    if(!newPasswordField.getText().isEmpty()){
                        basicInfo.setPassword(newPasswordField.getText());
                    }
                }
                else{
                    Dialog<String> dialogWindow = new Dialog<String>();

                    dialogWindow.setContentText("Passwords do not match.");
                    dialogWindow.setTitle("Error");
                    dialogWindow.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonType.OK.getButtonData()));
                    dialogWindow.show();
                }

                if(canExit) {
                    stage.setScene(mainScene);
                }
            }
        });
    }

    private void SetTextFieldStyle(TextField tf){
        String style = "-fx-border-color: COLOR; -fx-border-width: 0 0 1 0; -fx-background-color: transparent;"+StyleFactory.GetTextStyle(colorInfo);
        style = style.replace("COLOR",StyleFactory.FormatRGBA(colorInfo.getBorderColor())).replace(";;",";");
        tf.setStyle(style);
    }

    private void SetTextStyle(Label text){
        text.setStyle(StyleFactory.GetTextStyle(colorInfo));
    }

    public void SetView(int view){
        if(view != currentView){
            currentView = view;
            if(view == ACCOUNT){
                tabs.getSelectionModel().select(accountTab);
            }
            else if(view == APPEARANCE){
            }
            else if(view == GENERAL){
            }
        }


    }

    public void UpdateBackground(boolean isStretchToFitScreen,boolean isRepeatedHorizontally, boolean isRepeatedVertically, Rectangle2D screen){
        BackgroundRepeat horizontal;
        BackgroundRepeat vertical;
        BackgroundSize size;

        if(isRepeatedHorizontally){
            horizontal = BackgroundRepeat.REPEAT;
        }
        else{
            horizontal = BackgroundRepeat.NO_REPEAT;
        }

        if(isRepeatedVertically){
            vertical = BackgroundRepeat.REPEAT;
        }
        else{
            vertical = BackgroundRepeat.NO_REPEAT;
        }
        if(isStretchToFitScreen){
            size = new BackgroundSize(screen.getWidth(),screen.getHeight(),false,false,false,false);
        }
        else {
            size = BackgroundSize.DEFAULT;
        }

        try {
            background.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(basicInfo.getBackgroundPath())),horizontal,vertical, BackgroundPosition.CENTER,size)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
