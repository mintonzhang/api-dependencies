package cn.minsin.openjfx.tools;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author: minton.zhang
 * @since: 2020/4/12 18:08
 */
public class MFileDialog {


    /**
     * 选择目录并且绑定数据到文本框
     * @param textField
     */
    public static void chooseDirectory(TextField textField) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("请选择目录");
        Stage stage = new Stage();
        File file = fileChooser.showDialog(stage);
        if (file != null) {
            File absoluteFile = file.getAbsoluteFile();
            textField.setText(absoluteFile.getAbsolutePath());
        }
    }

    /**
     * 选择文件并且绑定到文本框
     * @param textField
     * @param fileTypes
     */
    public static void chooseFile(TextField textField, FileChooser.ExtensionFilter... fileTypes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择文件");
        fileChooser.getExtensionFilters().addAll(fileTypes.length==0?FileExtensionUtils.all():fileTypes);
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            File absoluteFile = file.getAbsoluteFile();
            textField.setText(absoluteFile.getAbsolutePath());
        }
    }

    /**
     * 选择目录如果有文件,则返回文件数据到文本框
     */
    public static File chooseDirectory() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("请选择保存目录");
        Stage stage = new Stage();
        return fileChooser.showDialog(stage);

    }

    /**
     * 选择文件并且绑定到文本框
     * @param textField
     * @param fileTypes
     */
    public static File chooseFile(FileChooser.ExtensionFilter... fileTypes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择文件");
        fileChooser.getExtensionFilters().addAll(fileTypes.length==0?FileExtensionUtils.all():fileTypes);
        Stage stage = new Stage();
        return fileChooser.showOpenDialog(stage);
    }
}
