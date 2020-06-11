package cn.minsin.openjfx.tools;

import javafx.scene.control.ButtonType;
import lombok.Getter;

/**
 * 默认按钮
 *
 * @author: minton.zhang
 * @since: 2020/4/6 18:34
 */
public enum MButtonType {


    OK("OK", "确认"),
    CANCEL("CANCEL", "取消");

    @Getter
    private String key;
    @Getter
    private String name;

    MButtonType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public ButtonType toButton() {
        return new ButtonType(this.name);
    }


    public static MButtonType findConfirmTypeName(String name) {
        for (MButtonType value : MButtonType.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }


    /**
     * 比较两个按钮是否相同
     *
     * @param type
     * @return
     */
    public boolean equals(MButtonType type) {
        return type != null && type.getKey().equals(this.key);
    }

}
