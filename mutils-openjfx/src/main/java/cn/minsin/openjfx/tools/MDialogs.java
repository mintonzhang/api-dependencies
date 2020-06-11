package cn.minsin.openjfx.tools;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: minton.zhang
 * @since: 2020/4/6 18:20
 */
public class MDialogs {

    public static void showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("提示");
        alert.setHeaderText("");
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("警告");
        alert.setHeaderText("");
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("错误");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showException(String message, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("异常");
        alert.setHeaderText("");
        alert.setContentText(message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("详情:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static void showException(Exception exception) {
        showException("详细信息如下", exception);
    }

    public static boolean showConfirm(String message, MButtonType trueButton, MButtonType... options) {
        MButtonType buttonType = showConfirm(message, options);
        return buttonType.equals(trueButton);
    }

    public static MButtonType showConfirm(String message, MButtonType... options) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("询问");
        alert.setHeaderText("");
        alert.setContentText(message);

        if (options == null || options.length == 0) {
            options = new MButtonType[]{MButtonType.OK, MButtonType.CANCEL};
        }

        List<ButtonType> buttons = new ArrayList<>();
        for (MButtonType option : options) {
            buttons.add(option.toButton());
        }

        alert.getButtonTypes().setAll(buttons);
        Optional<ButtonType> result = alert.showAndWait();
        AtomicReference<MButtonType> buttonType = new AtomicReference<>();
        result.ifPresent(e -> buttonType.set(MButtonType.findConfirmTypeName(e.getText())));
        return buttonType.get();
    }

    public static String showTextInput(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("输入");
        dialog.setHeaderText(title);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);

    }
}
