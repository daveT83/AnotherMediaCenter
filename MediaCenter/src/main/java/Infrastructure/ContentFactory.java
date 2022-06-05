package Infrastructure;

import Models.App;
import Models.ContentButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ContentFactory {
private static boolean isRunningApp;

    public static ContentButton CreateButtonFromApp(App app, BackgroundSize backgroundSize, Stage stage, ContentFocus contentFocus){
        ContentButton tempContentButton = null;
        Label tempLabel;
        Button tempButton;

            if(app.getIsShownOnHomeScreen()) {
                tempLabel = new Label();
                tempButton = new Button();
                tempContentButton = new ContentButton();

                tempLabel.setText(app.getName());
                tempButton.setOpacity(0.5);

                try {
                    tempButton.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream(app.getImagePath())), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize)));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                tempButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        stage.getScene().setCursor(Cursor.HAND);
                        contentFocus.SetFocus(tempButton, true);
                    }
                });

                tempButton.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        stage.getScene().setCursor(Cursor.DEFAULT);
                    }
                });

                tempButton.setOnAction(event -> {
                    if(!isRunningApp && tempButton.getOpacity() == 1) {
                        try {
                            isRunningApp = true;
                            app.setProcess(Runtime.getRuntime().exec(app.getCommandLineCommand()));
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        stage.hide();

                                        while(app.getProcess().isAlive()){
                                            Thread.sleep(10);
                                        }

                                        isRunningApp = false;
                                        stage.show();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                        } catch (IOException e) {
                            Dialog<String> dialogWindow = new Dialog<String>();

                            dialogWindow.setContentText("Error opening application.");
                            dialogWindow.setTitle("Error");
                            dialogWindow.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonType.OK.getButtonData()));
                            dialogWindow.show();
                        }
                    }
                });

                tempContentButton.setButton(tempButton);
                tempContentButton.setLabel(tempLabel);
        }

        return tempContentButton;
    }
}
