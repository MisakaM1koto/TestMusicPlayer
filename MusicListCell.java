package com.Misaka.Music_GUI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import java.io.File;
import java.util.List;

public class MusicListCell extends ListCell<File> {
    private final Label label;
    private final BorderPane pane;
    private List<String> cellList;

    public MusicListCell(List<String> list) {
        this.cellList = list;
        pane = new BorderPane();
        label = new Label();
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        Button delMusic = new Button();
        delMusic.getStyleClass().add("remove-btn");
        delMusic.setGraphic(new Region());
        pane.setCenter(label);
        pane.setLeft(delMusic);

        //删除
        delMusic.setOnAction(event -> {
            Controller controller = new Controller();
            File item = getItem();
            System.out.println(item);
            getListView().getItems().remove(getItem());
            System.out.println("cellList:" + cellList);
            int getIndex = findMusic(String.valueOf(item));
            cellList.remove(getIndex);
            controller.setList(cellList);
            controller.saveMusicList();

        });
    }
    public int findMusic(String musicName) {
        for (int i = 0; i < cellList.size(); i++) {
            if (cellList.get(i).contains(musicName)) {
                System.out.println(i);
                return i;
            }
        }
        return -1;
    }


    @Override
    protected void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null || empty){
            setGraphic(null);
            setText("");
        }else {
            String name = item.getName();
            label.setText(name.substring(0, name.length() - 4 ));
            setGraphic(pane);
        }
    }
}
