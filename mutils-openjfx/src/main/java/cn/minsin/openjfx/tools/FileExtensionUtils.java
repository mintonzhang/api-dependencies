package cn.minsin.openjfx.tools;

import javafx.stage.FileChooser;

/**
 *
 * @author: minton.zhang
 * @since: 2020/4/4 22:01
 */
public class FileExtensionUtils {

    /**
     * excel文件
     * @return
     */
    public static FileChooser.ExtensionFilter[] excel() {
        return new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("All Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx")};
    }

    /**
     * sql文件
      * @return
     */
    public static FileChooser.ExtensionFilter[] sql() {
        return new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("数据库脚本", "*.sql")};
    }

    /**
     * jar文件
     * @return
     */
    public static FileChooser.ExtensionFilter[] jar() {
        return new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("jar", "*.jar")};
    }

    /**
     * 所有文件
     * @return
     */
    public static FileChooser.ExtensionFilter[] all() {
        return new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("所有文件", "*")};
    }
    /**
     * 图片文件
     * @return
     */
    public static FileChooser.ExtensionFilter[] image() {
        return new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("图片文件", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG", "*.JPG","*.JPGE"),
                new FileChooser.ExtensionFilter("PNG", "*.PNG")};
    }


}
