package com.Misaka.Music_GUI;

import com.leewyatt.rxcontrols.controls.RXAudioSpectrum;
import com.leewyatt.rxcontrols.controls.RXLrcView;
import com.leewyatt.rxcontrols.pojo.LrcDoc;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Controller {
    private final String MY_BAIDUDISK_URL = "https://pan.baidu.com/s/1OqSXq6emqWIvpeVzUG3UeQ?pwd=vlzb";
    private final String MY_GITHUB = "https://github.com/MisakaM1koto/Misaka";
    private final String KUGOUMUSIC_SEARCH = "https://www.kugou.com/yy/html/search.html#searchType=song&searchKeyWord=";
    private final String NETEASE_SEARCH = "https://music.163.com/#/search/m/?s=";
    private final String NETEASE_ARTIST = "&type=100";//最后
    private final String NETEASE_ALBUM = "&type=10";
    private final String KUWOMUSIC_SEARCH = "http://www.kuwo.cn/search/list?key=";
    private final String QQMUSIC_SEARCH_LEFT = "https://y.qq.com/n/ryqq/search?w=";
    private final String QQMUSIC_SEARCH_RIGHT = "&t=song&remoteplace=txt.yqq.top";
    private final String LYRIC_PATH = "C:\\ProgramData\\Misaka Mikoto\\lyric\\";
    private Stage myStage;
    private Stage settingsStage;
    private HostServices myHostServices;
    private boolean bind = true;
    private Tooltip tooltip;
    private List<String> musicList = new ArrayList<>();
    private Media media;
    private MediaPlayer mediaPlayer;
    private ObservableList<File> items;
    private SimpleDateFormat sdf;
    private double volumeVal = 1.0;
    private int index;
    private int size;
    private String currentDir;

    private String searchOption = null;

    @FXML
    private Button addMusicButton;

    @FXML
    private Button searchMusicButton;

    @FXML
    private Text addMusicLyricButton;

    @FXML
    private MenuItem appSettingButton;

    @FXML
    private MenuItem checkUpdateButton;

    @FXML
    private MenuItem moreInfoButton;

    @FXML
    private MenuItem checkUpdateButton_GitHub;

    @FXML
    private MenuItem aboutButton;

    @FXML
    private MenuButton circularSelection;

    @FXML
    private MenuButton menuButton;

    @FXML
    private ImageView lastMusicPic;

    @FXML
    private ImageView volumeView;

    @FXML
    private ImageView miniWindowButton;

    @FXML
    private ImageView musicAlbumPic;

    @FXML
    private ImageView nextMusicPic;

    @FXML
    private ToggleGroup loopChoiceGroup;

    @FXML
    private RadioMenuItem loopListPlay;

    @FXML
    private RadioMenuItem loopSinglePlay;

    @FXML
    private RadioMenuItem singlePlay;

    @FXML
    private ListView<File> musicListView;

    @FXML
    private RXAudioSpectrum musicSpectrum;

    @FXML
    private ToggleButton playAndPauseButton;

    @FXML
    private Slider playSlider;

    @FXML
    private Slider volumeSlider;

    @FXML
    private SplitPane root;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleButton showLyricButton = new ToggleButton("词");

    @FXML
    private RXLrcView showMusicLyric;

    @FXML
    private Label showMusicArtistLabel;
    @FXML
    private Label showMusicNameLabel;

    @FXML
    private Label showMusicTimeLabel;

    @FXML
    private Label showMusicAlbumLabel;

    @FXML
    private Label title;


    public void initialize() {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initRuntimeDirectory();
        initRuntimeFile();
        initReadUserSettings();
        initStage();
        initListView();
        initReadMusicList();
        initMusicListView();
    }

    private void initRuntimeDirectory() {
        File MisakaMikotoDir = new File("C:\\ProgramData\\Misaka Mikoto");
        File lyricDir = new File("C:\\ProgramData\\Misaka Mikoto\\lyric");
        if (!MisakaMikotoDir.exists()) {
            boolean mkdir = MisakaMikotoDir.mkdir();
        }
        if (!(lyricDir.exists())) {
            lyricDir.mkdir();
        }
    }

    private void initRuntimeFile() {
        File userConfigFile = new File("C:\\ProgramData\\Misaka Mikoto\\UserConfig.ini");
        File musicListFile = new File("C:\\ProgramData\\Misaka Mikoto\\MusicList.ini");
        try {
            if (!userConfigFile.exists()) {
                userConfigFile.createNewFile();
            }
            if (!musicListFile.exists()) {
                musicListFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void test() throws IOException {
//        File file = new File("C:\\ProgramData\\Misaka Mikoto\\UserConfig.ini");
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        String ipconfig = "ipconfig/all";
////        try {
////            Process process = Runtime.getRuntime().exec(ipconfig);
////            InputStream is = process.getInputStream();
////            InputStreamReader isr = new InputStreamReader(is,"GBK");
////            BufferedReader br = new BufferedReader(isr);
////            String content = br.readLine();
////            while (content != null) {
//                System.out.println(content);
//                content = br.readLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        File file = new File("C:\\Users\\MisakaMikoto\\Desktop\\新建 文本文档 (3).cmd");
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            runtime.exec(String.valueOf(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    private void initReadUserSettings() {
        try (
                Reader fr = new FileReader("C:\\ProgramData\\Misaka Mikoto\\UserConfig.ini", StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr);
        ) {
            String line;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initReadMusicList() {
        try (
                Reader fr = new FileReader("C:\\ProgramData\\Misaka Mikoto\\MusicList.ini", StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr);
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                musicList.add(line);
                File file = new File(line);
                items = musicListView.getItems();
                items.add(new File(file.getName()));
                String str = "123";
                str.contains(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initStage() {
        searchField.setPromptText("搜索音乐");
        loopListPlay.setSelected(true);
        musicListView.setFixedCellSize(40);
        musicAlbumPic.setPickOnBounds(true);
        musicAlbumPic.setPreserveRatio(true);
    }

    private String getFileName(File file) {
        if (file != null) {
            String temp = file.getName();
            return temp.substring(0, temp.lastIndexOf("."));
        }
        return null;
    }

    /**
     * 各种与MediaPlayer相关的组件准备
     *
     * @param voiceFile
     */
    private void initMediaPlayer(File voiceFile) {
        String lrcName = voiceFile.getName().substring(0, voiceFile.getName().lastIndexOf(".")) + ".lrc";
        String lrcPath = LYRIC_PATH + lrcName;
        System.out.println(lrcPath);
        File file = new File(lrcPath);
        if (file.exists()) {
            try {
                byte[] bytes = Files.readAllBytes(Path.of(lrcPath));
                //解析歌词
                showMusicLyric.setLrcDoc(LrcDoc.parseLrcDoc(new String(bytes, EncodingDetect.detect(bytes))));
                //设置歌词进度
                showMusicLyric.currentTimeProperty().bind(mediaPlayer.currentTimeProperty());
                showMusicLyric.setAnimationTime(Duration.seconds(0.5));
                showMusicLyric.setMinWidth(680);
                showMusicLyric.setPrefWidth(680);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //频谱可视化
        mediaPlayer.setAudioSpectrumListener(new AudioSpectrumListener() {
            @Override
            public void spectrumDataUpdate(double v, double v1, float[] magnitudes, float[] phases) {
                musicSpectrum.setMagnitudes(magnitudes);
                musicSpectrum.setPhases(phases);
                musicSpectrum.setOpacity(1);
                musicSpectrum.setVisible(true);

            }
        });

        //音量条与进度条
        volumeSlider.setMax(1.0);
        volumeSlider.setValue(volumeVal);
        mediaPlayer.setVolume(volumeSlider.getValue());
        volumeSlider.valueProperty().addListener((ob, ov, nv) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(nv.doubleValue());
                volumeVal = volumeSlider.getValue();
            }
        });
        //------
        playSlider.setMin(mediaPlayer.getStartTime().toSeconds());
        playSlider.setMax(mediaPlayer.getStopTime().toSeconds());
        playSlider.setOnMousePressed(event -> bind = false);
        playSlider.setOnMouseReleased(event -> {
            mediaPlayer.seek(Duration.seconds(playSlider.getValue()));
            bind = true;
        });
        //playSlider.setShowTickMarks(true);

        //监听当前时间与设置当前时间
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                if (bind) {
                    playSlider.setValue(t1.toSeconds());
                }
                if (mediaPlayer != null) {
                    sdf = new SimpleDateFormat("mm:ss");
                    String currentTime = sdf.format(t1.toMillis());
                    String totalTime = sdf.format(mediaPlayer.getBufferProgressTime().toMillis());
                    showMusicTimeLabel.setText(currentTime + " / " + totalTime);

                    if (loopListPlay.isSelected()) {
                        if (currentTime.equals(totalTime)) {
                            int size = musicListView.getItems().size();
                            if (size < 2) {
                                return;
                            }
                            int index = musicListView.getSelectionModel().getSelectedIndex();
                            index = (index == size - 1) ? 0 : index + 1;
                            musicListView.getSelectionModel().select(index);
                        }
                    }
                }
            }
        });
    }

    private void startMediaPlayer(File newFile) {
        if (newFile != null) {
            if (mediaPlayer != null) {
                mediaPlayerDispose();
            }
            int index = findMusic(String.valueOf(newFile));
            if (index == -1) return;
            File voiceFile = new File(musicList.get(index));
            media = new Media(voiceFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
                myStage.setTitle("正在播放 " + getFileName(newFile));
                ObservableMap<String, Object> map = media.getMetadata();
                Image songImage = (Image) map.get("image");
                if (songImage == null) {
                    songImage = (Image) map.get("Image");
                }
                String musicArtist = (String) map.get("artist");
                String musicAlbum = (String) map.get("album");
                musicAlbumPic.setImage(songImage);
                showMusicNameLabel.setText(getFileName(newFile));
                showMusicArtistLabel.setText(musicArtist);
                showMusicAlbumLabel.setText(musicAlbum);
                initMediaPlayer(voiceFile);
            });
            playAndPauseButton.setSelected(true);
            showLyricButton.setSelected(true);
            showMusicLyric.setVisible(true);
            System.out.println("showLyricButton.isSelected(): " + showLyricButton.isSelected());
            mediaPlayer.play();
        }
    }

    /**
     * 选中ListView
     */
    private void initMusicListView() {
        musicListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observableValue, File oldFile, File newFile) {
                if (singlePlay.isSelected() || loopListPlay.isSelected()) {
                    startMediaPlayer(newFile);
                }
                if (loopSinglePlay.isSelected()) {
                    startMediaPlayer(newFile);
                    if (mediaPlayer != null) {
                        mediaPlayer.setCycleCount(100);
                    }
                }
            }
        });
    }

    private void mediaPlayerDispose() {
        mediaPlayer.stop();
        showMusicLyric.setLrcDoc(null);
        showMusicLyric.currentTimeProperty().unbind();
        showMusicLyric.setCurrentTime(Duration.ZERO);
        showMusicTimeLabel.setText("00:00 / 00:00");
        playAndPauseButton.setSelected(false);
        showLyricButton.setSelected(false);
        mediaPlayer.dispose();
        mediaPlayer = null;

    }

    public int findMusic(String musicName) {
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).contains(musicName)) {
                System.out.println(i);
                return i;
            }
        }
        return -1;
    }

    private void initListView() {
        musicListView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> fileListView) {
                return new MusicListCell(musicList);
            }
        });
    }

    private void copyFile(File path, String musicName) {
        try (
                InputStream is = new FileInputStream(path);
                InputStream bis = new BufferedInputStream(is);
                OutputStream os = new FileOutputStream(LYRIC_PATH + musicName + ".lrc");
                OutputStream bos = new BufferedOutputStream(os);
        ) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            System.out.println("copy: " + musicName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMusicList() {
        try (
                Writer fw = new FileWriter("C:\\ProgramData\\Misaka Mikoto\\MusicList.ini", StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(fw);
        ) {
            for (int i = 0; i < musicList.size(); i++) {
                bw.write(musicList.get(i) + "\r\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        myStage = stage;
    }

    public void setCurrentDir(String s) {
        currentDir = s;
    }

    public void setHostServices(HostServices hostServices) {
        myHostServices = hostServices;
    }

    public List<String> getList() {
        return this.musicList;
    }

    public void setList(List<String> musicList) {
        this.musicList = musicList;
    }

    @FXML
    void addLyric_SetOnMouseClickListener(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("歌词文件(.lrc)", "*.lrc"));
        File file = fileChooser.showOpenDialog(myStage);
        String lyricFileName = getFileName(file);
        String musicName = "";
        boolean flag = false;
        System.out.println("lyricFileName: " + lyricFileName);
//        for (int i = 0; i < musicListView.getItems().size(); i++) {
//            String temp = items.get(i).getName().substring(0, items.get(i).getName().lastIndexOf("."));
//            temp = temp.substring(temp.lastIndexOf("-"));
//            temp = temp.trim();
//            System.out.println("temp: " + temp);
//            if (temp.equalsIgnoreCase(lyricFileName) || lyricFileName.contains(temp) || temp.contains(lyricFileName)) {
//                musicName = items.get(i).getName();
//                flag = true;
//                musicName = musicName.substring(0, musicName.lastIndexOf("."));
//                System.out.println("true");
//            }
//            System.out.println("false");
//        }
//        System.out.println("addLyric: " + musicName);
//        if (String.valueOf(file) != null && flag) {
//            copyFile(file, musicName);
//        }
        copyFile(file, lyricFileName);
    }

    @FXML
    void addLyric_SetOnMouseEnterListener(MouseEvent event) {
        addMusicLyricButton.setCursor(Cursor.HAND);
    }

    @FXML
    void addMusic_SetOnMouseClickListener(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("音频文件(.mp3 .wav)", "*.mp3", "*.wav"));
        List<File> fileList = fileChooser.showOpenMultipleDialog(myStage);
        items = musicListView.getItems();
        if (fileList != null) {
            fileList.forEach(file -> {
                if (!items.contains(file)) {
                    items.add(new File(file.getName()));
                }
            });
            for (int i = 0; i < fileList.size(); i++) {
                musicList.add(fileList.get(i).toString());
            }
        }
        saveMusicList();
    }

    @FXML
    void addMusic_SetOnMouseEnterListener(MouseEvent event) {
        addMusicButton.setCursor(Cursor.HAND);
    }

    @FXML
    void appSetting_SetOnActionListener(ActionEvent event) throws IOException {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setHeaderText(null);
//        alert.setContentText("占位符");
//        Optional<ButtonType> buttonType = alert.showAndWait();
        File file = new File(currentDir + "/Java FXML Settings UI.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(file.toURI().toURL());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        SettingsController controller = fxmlLoader.getController();
        controller.setCurrentDir(currentDir);

        if (settingsStage == null || !settingsStage.isShowing()) {
            settingsStage = new Stage();
            settingsStage.setResizable(false);
            settingsStage.setScene(scene);
            settingsStage.setAlwaysOnTop(true);
            settingsStage.show();
            controller.setSettingsStage(settingsStage);
        }
    }

    @FXML
    void checkUpdate_SetOnActionListener(ActionEvent event) {
        myHostServices.showDocument(MY_BAIDUDISK_URL);
    }

    @FXML
    void circularSelection_SetOnMouseEnterListener(MouseEvent event) {
        circularSelection.setCursor(Cursor.HAND);
    }

    @FXML
    void prveMusic_SetOnMouseClickListener(MouseEvent event) {
        int size = musicListView.getItems().size();
        if (size < 2) {
            return;
        }
        int index = musicListView.getSelectionModel().getSelectedIndex();
        index = (index == 0) ? size - 1 : index - 1;
        musicListView.getSelectionModel().select(index);
    }

    @FXML
    void lastMusic_SetOnMouseEnterListener(MouseEvent event) {
        lastMusicPic.setCursor(Cursor.HAND);
    }

    @FXML
    void listView_SetMouseClickListener(MouseEvent event) {

    }

    @FXML
    void miniWindow_SetOnMouseClickListener(MouseEvent event) {

        SystemTray systemTray = SystemTray.getSystemTray();
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage("E:\\头像\\-6abfa99803778cb0.jpg");
        image.flush();

        PopupMenu menu = new PopupMenu();
        String str = "最小化到系统托盘";

        java.awt.MenuItem item1 = new java.awt.MenuItem("show");
        java.awt.MenuItem item2 = new java.awt.MenuItem("quit");
        menu.add(item1);
        menu.add(item2);

        TrayIcon trayIcon = new TrayIcon(image, str, menu);
        trayIcon.setImageAutoSize(true);


        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        myStage.show();
                    }
                });
                systemTray.remove(trayIcon);
            }
        });

        item2.addActionListener(e -> {
            Platform.setImplicitExit(true);
            Platform.runLater(() -> {
                myStage.close();
            });
            systemTray.remove(trayIcon);
            Platform.exit();
        });
        myStage.hide();
    }

    @FXML
    void menuButton_SetOnMouseEnterListener(MouseEvent event) {
        menuButton.setCursor(Cursor.HAND);
    }

    @FXML
    void miniWindow_SetOnMouseEnterListener(MouseEvent event) {
        miniWindowButton.setCursor(Cursor.HAND);
    }

    @FXML
    void moreInfo_SetOnActionListener(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("占位符");
        Optional<ButtonType> buttonType = alert.showAndWait();

    }

    @FXML
    void musicArtist_SetOnMouseClickListener(MouseEvent event) {
        myHostServices.showDocument(NETEASE_SEARCH + showMusicArtistLabel.getText() + NETEASE_ARTIST);
        System.out.println(showMusicArtistLabel.getText());
    }

    @FXML
    void musicArtist_SetOnMouseEnterListener(MouseEvent event) {
        showMusicArtistLabel.setCursor(Cursor.HAND);
    }

    @FXML
    void musicNameLabel_SetOnMouseClickListener(MouseEvent event) {
        myHostServices.showDocument(NETEASE_SEARCH + showMusicNameLabel.getText());
    }

    @FXML
    void musicNameLabel_SetOnMouseEnterListener(MouseEvent event) {
        showMusicNameLabel.setCursor(Cursor.HAND);
    }

    @FXML
    void musicAlbum_SetOnMouseClickListener(MouseEvent event) {
        myHostServices.showDocument(NETEASE_SEARCH + showMusicAlbumLabel.getText() + NETEASE_ALBUM);
        System.out.println(showMusicAlbumLabel.getText());
    }

    @FXML
    void musicAlbum_SetOnMouseEnterListener(MouseEvent event) {
        showMusicAlbumLabel.setCursor(Cursor.HAND);
    }

    @FXML
    void nextMusic_SetOnMouseClickListener(MouseEvent event) {
        int size = musicListView.getItems().size();
        if (size < 2) {
            return;
        }
        int index = musicListView.getSelectionModel().getSelectedIndex();
        index = (index == size - 1) ? 0 : index + 1;
        musicListView.getSelectionModel().select(index);
    }

    @FXML
    void nextMusic_SetOnMouseEnterListener(MouseEvent event) {
        nextMusicPic.setCursor(Cursor.HAND);
    }

    @FXML
    void playAndPause_SetOnActionListener(ActionEvent event) {
        System.out.println(playAndPauseButton.isSelected());
        if (mediaPlayer != null) {
            if (playAndPauseButton.isSelected()) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        }
    }

    @FXML
    void playAndPause_SetOnMouseEnterListener(MouseEvent event) {
        playAndPauseButton.setCursor(Cursor.HAND);
    }

    @FXML
    void root_SetOnKeyPressedListener(KeyEvent event) {
        Object obj = event.getCode();
        System.out.println(obj.toString());
        if (obj.equals(KeyCode.PAGE_UP)) {
            prveMusic_SetOnMouseClickListener(null);
        }
        if (obj.equals(KeyCode.PAGE_DOWN)) {
            nextMusic_SetOnMouseClickListener(null);
        }
        if (obj.equals(KeyCode.PAUSE) || obj.equals(KeyCode.PLAY)) {
            playAndPause_SetOnActionListener(null);
        }
    }

    @FXML
    void searchButton_SetOnMouseEnterListener(MouseEvent event) {

    }

    @FXML
    void searchMusicButton_SetOnActionListener(ActionEvent event) {
        searchMusicButton.setTooltip(new Tooltip("搜索"));
        myHostServices.showDocument(NETEASE_SEARCH + searchField.getText());
    }

    @FXML
    void searchMusic_SetOnKeyPressedListener(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.equals(KeyCode.ENTER)) {
            myHostServices.showDocument(NETEASE_SEARCH + searchField.getText());
        }
    }

    @FXML
    void showLyric_SetOnMouseClickListener(MouseEvent event) {
        System.out.println("showLyricButton.isSelected() before: " + showLyricButton.isSelected());
        if (mediaPlayer != null) {
            if (showLyricButton.isSelected()) {
                showMusicLyric.setVisible(true);
            } else {
                showMusicLyric.setVisible(false);
            }
        }
        System.out.println("showLyricButton.isSelected() after: " + showLyricButton.isSelected());
    }

    @FXML
    void showLyric_SetOnMouseEnterListener(MouseEvent event) {
        showLyricButton.setCursor(Cursor.HAND);
        showLyricButton.setTooltip(new Tooltip("歌词显示"));
    }

    @FXML
    void checkUpdateGitHub_SetOnActionListener(ActionEvent event) {
        myHostServices.showDocument(MY_GITHUB);
    }

    @FXML
    void about_SetOnActionListener(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("占位符");
        Optional<ButtonType> buttonType = alert.showAndWait();
    }
}
