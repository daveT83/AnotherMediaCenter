package davethms83.mediacenter;

import Infrastructure.ContentFocus;
import Infrastructure.DataLoader;
import Models.App;
import Models.ColorInformation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoad = new FXMLLoader(BasicInformationController.class.getResource("MainWindow.fxml"));
        Scene mainScene = new Scene(fxmlLoad.load(), 320, 240);

        /*
        FXMLLoader fxmlLoader = new FXMLLoader(BasicInformationController.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        BasicInformationController bic = fxmlLoader.getController();
        final ContentFocus contentFocus;
        List<App> apps = DataLoader.LoadApps();
        ColorInformation colorInformation = DataLoader.LoadColorInformation();
        contentFocus = new ContentFocus(colorInformation);

        bic.basicInfo = DataLoader.LoadBasicData();
        bic.UpdateColorInformation(colorInformation);
        bic.UpdateBackground( false, true,true,Screen.getPrimary().getBounds());
        bic.SetContentFocus(contentFocus);
        bic.SetStage(stage);

        stage.setTitle("Media Center");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        contentFocus.UpdateScreenWidth(Screen.getPrimary().getBounds().getWidth());
        contentFocus.UpdateBorderColor(colorInformation);
        bic.InitializeContentScroller(Screen.getPrimary().getBounds());
        bic.InitTopButtons();
        bic.AddContentToContentScroller(apps, Screen.getPrimary().getBounds());
        bic.UpdateUserPicture();
        bic.StartClock();
        bic.UpdateUserName();
        bic.InitSettingsMenu();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(stage.isShowing()) {
                    if (keyEvent.getCode().isArrowKey()) {
                        contentFocus.UpdateCurrentFocused(keyEvent.getCode());
                    } else if (keyEvent.getCode().getName().equals("Enter")) {
                        contentFocus.GetFocusedButton().fire();
                    }
                }
            }
        });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                bic.StopClock();
                Platform.exit();
                System.exit(0);
            }
        });
        */
        FXMLLoader fxmlLoader = new FXMLLoader(BasicInformationController.class.getResource("SettingsWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        SettingsController settingsController = fxmlLoader.getController();

        stage.setTitle("Media Center");
        stage.setFullScreen(true);

        settingsController.SetBasicInformation(DataLoader.LoadBasicData());
        settingsController.SetStage(stage);
        settingsController.SetScene(mainScene);
        settingsController.UpdateBackground( false, true,true,Screen.getPrimary().getBounds());
        settingsController.SetColorInformation(DataLoader.LoadColorInformation());
        settingsController.InitSettings();
        settingsController.SetView(SettingsController.ACCOUNT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}