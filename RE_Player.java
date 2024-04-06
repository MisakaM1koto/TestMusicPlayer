package com.Misaka.Music_GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class RE_Player extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private double offsetX;
    private double offsetY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //可以放在src文件夹下
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Java FXML Main UI.fxml"));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("=========" + sdf.format(date) + "=========");
        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + "/Java FXML Main UI.fxml");
        writeResourcesOnCurrentDirectory();

        System.out.println(file.getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));

        FXMLLoader fxmlLoader = new FXMLLoader(file.toURI().toURL());
        SplitPane root = fxmlLoader.load();
        Scene scene = new Scene(root);

        Controller controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        controller.setHostServices(getHostServices());
        controller.setCurrentDir(currentPath);
        //设置全屏拖动
        scene.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getSceneX() - offsetX);
            primaryStage.setY(event.getSceneY() - offsetY);
        });

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

    public void writeResourcesOnCurrentDirectory() throws InterruptedException {
        List<String> list = new ArrayList<>();
        File mainFxmlFile = new File(getCurrentPath() + "/Java FXML Main UI.fxml");
        File cssFile = new File(getCurrentPath() + "/JavaFX.css");
        File settingsFxmlFile = new File(getCurrentPath() + "/Java FXML Settings UI.fxml");
        File imageDir = new File(getCurrentPath() + "/Image");
        File imageFile = new File(getCurrentPath() + "/Image");
        File rootConfigINI = new File(getCurrentPath() + "/rootConfig.ini");
        System.out.println("fxml" + mainFxmlFile.getAbsolutePath());
        System.out.println("css" + cssFile.getAbsolutePath());
        System.out.println("set" + settingsFxmlFile.getAbsolutePath());
        if (!mainFxmlFile.exists()) {
            releaseResourceToCurrentDir(mainFxmlFile, "fxml", "/Java FXML Main UI.fxml", "char");
            updateURLPath(mainFxmlFile, "Image url=", "../", "");
            Thread.sleep(666);
        }

        if (!cssFile.exists()) {
            releaseResourceToCurrentDir(cssFile, "fxml", "/JavaFX.css", "char");
            Thread.sleep(666);
        }

        if (!settingsFxmlFile.exists()) {
            releaseResourceToCurrentDir(settingsFxmlFile, "fxml", "/Java FXML Settings UI.fxml", "char");
            System.out.println("13");
            Thread.sleep(666);
        }

        if (!imageDir.exists()) {
            //releaseResource();
            imageDir.mkdir();
            getResourceFileList(list, mainFxmlFile, "Image");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
//              imageFile = new File(imageFile + list.get(i));
                System.out.println(imageFile);
                releaseResourceToCurrentDir(new File(imageFile + list.get(i)), "Image", list.get(i), "byte");
            }
            Thread.sleep(666);
        }

        if (!rootConfigINI.exists()) {
            try {
                rootConfigINI.createNewFile();
                Properties properties = new Properties();
                Reader fr = new FileReader(rootConfigINI, StandardCharsets.UTF_8);
                properties.load(fr);
                properties.setProperty("userConfigDir", "C:\\ProgramData\\Misaka Mikoto");
                properties.setProperty("isFirstLaunch", "true");
                Writer fw = new FileWriter(rootConfigINI, StandardCharsets.UTF_8);
                properties.store(fw, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(666);
        } else if (rootConfigINI.exists()) {
            Properties properties = new Properties();
            try {
                Reader fr = new FileReader(rootConfigINI, StandardCharsets.UTF_8);
                properties.load(fr);
                properties.setProperty("isFirstRunning", "false");
                Writer fw = new FileWriter(rootConfigINI, StandardCharsets.UTF_8);
                properties.store(fw, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getResourceFileList(List<String> list, File file, String resourceName) {
        try {
            Reader fr = new FileReader(file, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String temp;
            while ((line = br.readLine()) != null) {
                if (line.contains("url=")) {
                    temp = line;
                    temp = temp.substring(line.indexOf("<"), line.indexOf(">"));
                    temp = temp.replace(resourceName + " url=\"@", "");
                    temp = temp.substring(temp.indexOf("/"), temp.indexOf("\""));
                    list.add(temp);
                    temp = null;
                }
            }
            fr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateURLPath(File file, String keyVal, String oldVal, String newVal) {
                    try {
                        Reader fr = new FileReader(file, StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(fr);
                        String line;
                        List<String> allLine = new ArrayList<>();
                        while ((line = br.readLine()) != null) {
                            if (line.contains(keyVal)) {
                                System.out.println("1");
                                if (line.contains(oldVal)) {
                                    System.out.println("2");
                                    System.out.println(line = line.replace(oldVal, newVal));
                                }
                            }
                            allLine.add(line);
            }
            fr.close();
            br.close();

            Writer fw = new FileWriter(file, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < allLine.size(); i++) {
                bw.write(allLine.get(i) + "\r\n");
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseResourceToCurrentDir(File file, String resourceDirName, String fileName, String type) {
        try {
            InputStream is = this.getClass().getResourceAsStream(resourceDirName + fileName);
            //InputStream bis = new BufferedInputStream(is);
            OutputStream os = new FileOutputStream(file);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            System.out.println("file:  " + file);
            Writer osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            //System.out.println("this.getClass.getR(fxml+fileName): " + this.getClass().getResource(resourceDirName + "/" + fileName).getFile());
            int len;
            if (type.equals("char")) {
                char[] buffer = new char[1024];
                while ((len = isr.read(buffer)) != -1) {
                    osw.write(buffer, 0, len);
                }
            } else if (type.equals("byte")) {
                System.out.println("this.getClass.getR(fxml+fileName): " + this.getClass().getResource(resourceDirName + fileName));
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }
            osw.flush();
            os.flush();
            is.close();
            isr.close();
            os.close();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentPath() {
        String path = System.getProperty("user.dir");
        Properties properties = new Properties();
        try (
                Writer fw = new FileWriter("C:\\ProgramData\\Misaka Mikoto\\CurrentPath.ini", StandardCharsets.UTF_8);
        ) {
            properties.setProperty("当前程序路径", path);
            properties.store(fw, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
