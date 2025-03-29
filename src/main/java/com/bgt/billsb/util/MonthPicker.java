package com.bgt.billsb.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 自定义的MonthPicker类，继承自DatePicker，用于仅选择月份
 * 该类通过设置自定义的StringConverter和CSS样式来确保用户只能选择月份
 */
public class MonthPicker extends DatePicker {
    // 定义用于格式化月份的DateTimeFormatter
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * 构造函数，初始化MonthPicker
     * 设置当前日期为本月的第一天，然后配置StringConverter，强制选择月份，并注入自定义CSS样式
     */
    public MonthPicker() {
        super(LocalDate.now().withDayOfMonth(1));
        setupConverter();
        forceMonthSelection();
        injectCSS();
    }

    /**
     * 设置自定义的StringConverter，用于将LocalDate与字符串相互转换
     * 转换时仅保留年月信息，确保用户只能选择到月份
     */
    private void setupConverter() {
        setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? MONTH_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string == null ? null : YearMonth.parse(string, MONTH_FORMATTER).atDay(1);
            }
        });
    }

    /**
     * 强制用户只能选择月份
     * 监听valueProperty，当选择的日期不是本月的第一天时，自动将其设置为本月的第一天
     */
    private void forceMonthSelection() {
        valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getDayOfMonth() != 1) {
                setValue(newVal.withDayOfMonth(1));
            }
        });
    }

    /**
     * 注入自定义CSS样式以隐藏日期网格和周数列
     * 通过覆盖DatePickerSkin的getPopupContent方法，动态添加CSS规则
     */
    private void injectCSS() {
        // 通过CSS彻底隐藏日期网格（兼容所有JavaFX版本）
        this.setSkin(new DatePickerSkin(this) {
            @Override
            public Node getPopupContent() {
                Node content = super.getPopupContent();
                // 延迟注入CSS规则确保节点加载完成（关键修正）
                Platform.runLater(() -> {
                    content.lookupAll(".calendar-grid").forEach(node ->
                            node.setStyle("-fx-opacity: 0; -fx-max-height: 0;")
                    );
                    content.lookupAll(".day-cell").forEach(node ->
                            node.setVisible(false)
                    );
                });
                return content;
            }
        });

        // 全局CSS规则（隐藏周数列）
        this.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/css/month-picker.css")
                ).toExternalForm()
        );
    }

    /**
     * 获取选择的年月
     * @return YearMonth对象，表示选择的年月
     */
    public YearMonth getSelectedYearMonth() {
        return YearMonth.from(getValue());
    }

    /**
     * 切换到下一个月
     * 增加当前选择的月份，用于用户界面的“下一个月”操作
     */
    public void nextMonth() {
        setValue(getValue().plusMonths(1));
    }

    /**
     * 切换到上一个月
     * 减少当前选择的月份，用于用户界面的“上一个月”操作
     */
    public void previousMonth() {
        setValue(getValue().minusMonths(1));
    }
}
