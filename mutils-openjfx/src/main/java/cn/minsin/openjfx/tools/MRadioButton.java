package cn.minsin.openjfx.tools;

import com.alibaba.fastjson.JSON;
import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.util.Optional;

/**
 * @author: minton.zhang
 * @since: 2020/4/12 0:57
 */
public class MRadioButton {

    /**
     * 创建单选组
     * 使用方法
     * fxml
     * <div>
     *     <fx:define>
     *         <ToggleGroup fx:id="test" />
     *     </fx:define>
     *     <RadioButton  selected="true" text="是" toggleGroup="$test" userData="true" />
     *     <RadioButton  selected="true" text="否" toggleGroup="$test" userData="false" />
     * </div>
     *
     *
     * @return
     */
    public static ToggleGroup create() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            ObservableList<Toggle> toggles = toggleGroup.getToggles();
            toggles.remove(new_toggle);
            toggles.forEach(e -> e.setSelected(false));
        });
        return toggleGroup;
    }

    /**
     * 获取当前toggleGroup 选中的对象的userData值
     * @param toggleGroup
     * @param userDataClass
     * @param <T>
     * @return
     */
    public static <T> T getUserDataValue(ToggleGroup toggleGroup, Class<T> userDataClass) {
        Object userData = toggleGroup.getSelectedToggle().getUserData();
        return JSON.parseObject(userData.toString(), userDataClass);
    }

    /**
     * 让指定toggleroupG中 userData为指定值的对象选中
     * @param toggleGroup
     * @param userData
     */
    public static void setRadioButtonSelect(ToggleGroup toggleGroup, Object userData) {
        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        Optional<Toggle> first = toggles.stream().filter(e -> e.getUserData().equals(userData.toString())).findFirst();
        first.ifPresent(toggle -> toggle.setSelected(true));
    }
}
