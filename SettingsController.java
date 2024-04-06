package com.Misaka.Music_GUI;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SettingsController {
    private Stage settingsStage;
    private String currentDir;

    @FXML
    private Button applyButton;

    @FXML
    private RadioButton exitJVM;

    @FXML
    private Button okButton;

    @FXML
    private RadioButton systemTray;

    @FXML
    private Button cancelButton;
    private ToggleGroup toggleGroup;

    public String getCurrentDir() {
        return currentDir;
    }

    public Optional<ButtonType> confirmAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }


    @FXML
    void apply_SetOnActionListener(ActionEvent event) {
        //saveConfig();
        settingsStage.setAlwaysOnTop(false);
        AlertUtil.infoAlert("213","41f2");
    }

    @FXML
    void ok_SetOnActionListener(ActionEvent event) {
        settingsStage.setAlwaysOnTop(false);
        Optional<ButtonType> buttonType = confirmAlert("null", "确定退出？");
        if(buttonType.get() == ButtonType.OK){
            System.out.println("u 482957");
        }
        //saveConfig();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        settingsStage.hide();
        settingsStage.close();
    }

    @FXML
    void cancel_SetOnActionListener(ActionEvent event) {
        settingsStage.setAlwaysOnTop(false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setHeaderText("null");
        alert.setContentText("123df1");
        alert.showAndWait();
        settingsStage.hide();
        System.out.println("123");
        settingsStage.close();
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public void initialize() {
        initController();
    }

    private void initController() {
        toggleGroup = new ToggleGroup();
        exitJVM.setToggleGroup(toggleGroup);
        systemTray.setToggleGroup(toggleGroup);
    }

    public void setSettingsStage(Stage stage) {
        settingsStage = stage;
    }

    private void saveConfig() {
        try (
                Writer pathToProgramData = new FileWriter("C:\\ProgramData\\Misaka Mikoto\\UserSettings", StandardCharsets.UTF_8);
                Writer mainFXML = new FileWriter( currentDir + "", StandardCharsets.UTF_8);
                Writer settingFXML = new FileWriter(currentDir + "", StandardCharsets.UTF_8);
                Writer css = new FileWriter(currentDir + "", StandardCharsets.UTF_8);
        ) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
