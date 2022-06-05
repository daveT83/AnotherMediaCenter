package davethms83.mediacenter;

import Infrastructure.ButtonManager;
import Infrastructure.ContentFactory;
import Infrastructure.ContentFocus;
import Infrastructure.StyleFactory;
import Models.App;
import Models.BasicInformation;
import Models.ColorInformation;
import Models.ContentButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BasicInformationController {
    @FXML
    private Label dateTime;

    @FXML
    private Label userName;
    @FXML
    private Pane background;
    @FXML
    private Button userPictureButton;
    @FXML
    private AnchorPane contentHouse;
    @FXML
    private ScrollPane contentScroller;
    @FXML
    private HBox content;
    @FXML
    private VBox slideMenu;
    @FXML
    private ImageView menuArrows;
    @FXML
    private Pane slideMenuBack;
    @FXML
    private HBox otherInformation;
    @FXML
    private HBox userInformation;

    public BasicInformation basicInfo;
    private Thread clock;
    private boolean isClockTicking = false;
    private ContentFocus contentFocus;
    private Stage stage;
    private ColorInformation colorInformation;
    private TranslateTransition menuTranslation;

    public void SetContentFocus(ContentFocus contentFocus){
        this.contentFocus = contentFocus;
    }

    public void SetStage(Stage stage){
        this.stage = stage;
    }
    public void InitTopButtons(){
        userInformation.setPadding(new Insets(0,50,0,20));
        otherInformation.setPadding(new Insets(0,50,0,50));
        contentFocus.GetButtonManager().AddTopButton(userPictureButton);

        userPictureButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Button button = contentFocus.GetButtonManager().SetToSpecialSection(ButtonManager.SPECIAL_SECTION_SETTINGS_MENU);
                contentFocus.SetFocus(button,true,button.getStyle(), false);
                menuTranslation.setRate(1);
                menuTranslation.play();
            }
        });
    }

    public void UpdateColorInformation(ColorInformation colorInformation){
        this.colorInformation = colorInformation;

        //top
        userName.setStyle(StyleFactory.GetTextStyle(colorInformation));
        dateTime.setStyle(StyleFactory.GetTextStyle(colorInformation));

        //settings menu
        for(Node node:slideMenu.getChildren()){
            node.setStyle(StyleFactory.GetTextStyle(colorInformation));
        }

        //contentFocus
        if(contentFocus != null) {
            contentFocus.UpdateBorderColor(colorInformation);
        }

        //apps (content scroller)
        for(Node node:content.getChildren()){
            ((Label)((VBox)((Panel)node).getChildren()).getChildren().get(1)).setStyle(StyleFactory.GetTextStyle(colorInformation));
        }
    }

    public void InitSettingsMenu(){
        Button account = new Button("Account");
        Button general = new Button("General");
        Button signOut = new Button("Sign Out");

        int prefWidth = 300;
        int prefHeight = 100;
        final int[] predSection = {0};
        final int[] predPostion = {0};
        final Button[] predButton = {new Button()};

        account.setPrefWidth(prefWidth);
        account.setPrefHeight(prefHeight);
        account.setStyle(StyleFactory.GetTextStyle(colorInformation)+"-fx-background-color:transparent;");
        general.setPrefWidth(prefWidth);
        general.setPrefHeight(prefHeight);
        general.setStyle(StyleFactory.GetTextStyle(colorInformation)+"-fx-background-color:transparent;");
        signOut.setPrefWidth(prefWidth);
        signOut.setPrefHeight(prefHeight);
        signOut.setStyle(StyleFactory.GetTextStyle(colorInformation)+"-fx-background-color:transparent;");
        slideMenu.prefHeightProperty().bind(stage.heightProperty());
        slideMenu.setPrefWidth(prefWidth);
        slideMenu.setPadding(new Insets(100,0,100,0));

        Image image = new Image("file:C:\\Users\\davet\\Desktop\\RightArrow.png");
        menuArrows.setImage(image);

        slideMenu.getChildren().addAll(account,general,signOut);
        menuTranslation = new TranslateTransition(Duration.millis(500), slideMenuBack);

        menuTranslation.setFromX((prefWidth*-1));
        menuTranslation.setToX(0);
        menuTranslation.setRate(prefWidth*-1);
        menuTranslation.play();

        contentFocus.GetButtonManager().AddNewSpecialSection(ButtonManager.SPECIAL_SECTION_SETTINGS_MENU,account,general,signOut);

        slideMenu.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(contentFocus.GetButtonManager().IsInSpecialSection() && (account.isFocused() || general.isFocused() || signOut.isFocused()) && keyEvent.getCode().getName().equals("Backspace")){
                    contentFocus.GetButtonManager().LeaveSpecialSection(predSection[0], predPostion[0]);
                    contentFocus.SetFocus(predButton[0], true, contentFocus.GetButtonManager().GetPreviousButton().getStyle().replace(StyleFactory.GetBorderStyle(colorInformation),""), false);
                    menuTranslation.setRate(-1);
                    menuTranslation.play();
                }else if(!contentFocus.GetButtonManager().IsInSpecialSection() && (account.isFocused() || general.isFocused() || signOut.isFocused()) && keyEvent.getCode().getName().equals("Enter")){
                    predButton[0] = contentFocus.GetButtonManager().GetPreviousButton();
                    predPostion[0] = contentFocus.GetButtonManager().GetPreviousPosition();
                    predSection[0] = contentFocus.GetButtonManager().GetPreviousSection();
                }
            }
        });

        account.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.HAND);
                contentFocus.SetFocus(account, true, account.getStyle(), true);
            }
        });
        general.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.HAND);
                contentFocus.SetFocus(general, true, general.getStyle(), true);
            }
        });
        signOut.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.HAND);
                contentFocus.SetFocus(signOut, true, signOut.getStyle(), true);
            }
        });

        account.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.DEFAULT);
            }

        });
        general.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.DEFAULT);
            }

        });
        signOut.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.getScene().setCursor(Cursor.DEFAULT);
            }

        });

        slideMenuBack.setOnMouseEntered(evt -> {
            general.setVisible(true);
            account.setVisible(true);
            signOut.setVisible(true);

            account.setPrefWidth(prefWidth);
            account.setPrefHeight(prefHeight);
            account.setStyle(StyleFactory.GetTextStyle(colorInformation)+"-fx-background-color:transparent;");
            general.setPrefWidth(prefWidth);
            general.setPrefHeight(prefHeight);
            general.setStyle(StyleFactory.GetTextStyle(colorInformation)+"-fx-background-color:transparent;");
            signOut.setPrefWidth(prefWidth);
            signOut.setPrefHeight(prefHeight);
            signOut.setStyle(StyleFactory.GetTextStyle(colorInformation)+"-fx-background-color:transparent;");

            menuTranslation.setRate(1);
            menuTranslation.play();

        });
        slideMenuBack.setOnMouseExited(evt -> {
            menuTranslation.setRate(-1);
            menuTranslation.play();
            general.setVisible(false);
            account.setVisible(false);
            signOut.setVisible(false);
        });
    }

    public void RemoveContentFromContentScroller(List<ContentButton> buttons){
        VBox toRemove;
        for(ContentButton button : buttons) {
            toRemove = null;
            for(Node pane : content.getChildren()){
                if(pane.getId().equals(button.getLabel().getText())){
                    toRemove = (VBox)pane;
                    break;
                }
            }

            if(toRemove != null){
                content.getChildren().remove(toRemove);
            }
        }
    }
    public ContentFocus AddContentToContentScroller(List<App> apps, Rectangle2D screen){
        Background bg = new Background(new BackgroundFill(new Color(0,0,0,.5),new CornerRadii(10.0),null));
        double height = screen.getHeight()/5;
        double width = screen.getWidth()/6;
        double buttonHeight = screen.getHeight()/7;
        double labelHeight = height-buttonHeight;
        BackgroundSize backgroundSize = new BackgroundSize(width,buttonHeight,false,false,false,false);
        List<ContentButton> buttons = new ArrayList<ContentButton>();

        for(App app : apps) {
            Panel p = new Panel();
            VBox vBox = new VBox();
            ContentButton button = ContentFactory.CreateButtonFromApp(app, backgroundSize, stage, contentFocus);
            buttons.add(button);

            p.setBackground(bg);
            vBox.setId(button.getLabel().getText());
            button.getLabel().setStyle(StyleFactory.GetTextStyle(colorInformation));
            button.getLabel().setAlignment(Pos.CENTER);

            button.getButton().setMinSize(width,buttonHeight);
            button.getLabel().setMinSize(width,labelHeight);

            vBox.setMinSize(width, height);
            p.setMinSize(width,height);

            vBox.getChildren().addAll(button.getButton(),button.getLabel());
            p.getChildren().add(vBox);
            content.getChildren().add(p);
        }

        contentFocus.UpdateContentButtons(buttons);
        return contentFocus;
    }
    public void InitializeContentScroller(Rectangle2D screen){
        double width = screen.getWidth()/6;
        double modifiedWidth = width*5;
        int spacing = 75;

        contentScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroller.setVmax(0);
        contentScroller.setMaxWidth(modifiedWidth);

        content.setSpacing(spacing);

        AnchorPane.setTopAnchor(content, screen.getHeight()/2);
        AnchorPane.setBottomAnchor(content, screen.getHeight()/3);
        AnchorPane.setLeftAnchor(content, width);
        AnchorPane.setRightAnchor(content, width);

        content.setMaxWidth(modifiedWidth);

        contentFocus.UpdateScrollPane(contentScroller);
    }

    public void UpdateUserPicture(){
        try {
            int height = 75;
            int width = 75;
            BackgroundSize backgroundSize = new BackgroundSize(height,width,false,false,false,false);
            userPictureButton.setMinSize(height,width);
            userPictureButton.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(basicInfo.getUserPicturePath())), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
    public void UpdateUserName(){
        userName.setText(basicInfo.getUserName());
    }

    public void StartClock() {
        isClockTicking = true;
        clock = new Thread() {
            public void run() {
                while (isClockTicking) {
                    if (!dateTime.getText().equals(basicInfo.getDate())) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                dateTime.setText(basicInfo.getDate());
                            }
                        });
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        clock.start();
    }

    public void StopClock(){
        if(clock != null && clock.isAlive() && isClockTicking){
            isClockTicking = false;
        }
    }
}
