package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.util.text.StringUtil;

import java.awt.Color;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 05.10.2006
 * Time: 15:01:47
 */
class KeyPromoterUtils {

    private static KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);

    /**
     * Get first field of class with target type to use during click source handling.
     *
     * @param aClass      class to inspect
     * @param targetClass target class to check field to plug
     * @return field
     */
    static Field getFieldOfType(Class<?> aClass, Class<?> targetClass) {
        do {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().equals(targetClass)) {
                    declaredField.setAccessible(true);
                    return declaredField;
                }
            }
        } while ((aClass = aClass.getSuperclass()) != null);
        return null;
    }

    /**
     * Creates popup message body from template.
     *
     * @param description  action description
     * @param shortcutText key combination
     * @param count        number of counted invocations
     * @return the message
     */
    static String renderMessage(String description, String shortcutText, Integer count) {

        return MessageFormat.format(keyPromoterSettings.getPopupTemplate(),
                (StringUtil.isEmpty(description) ? shortcutText : shortcutText + "<br>(" + description + ")"),
                count);
    }

    static String getKeyboardShortcutsText(AnAction anAction) {
        Shortcut[] shortcuts = anAction.getShortcutSet().getShortcuts();
        if (shortcuts.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < shortcuts.length; i++) {
            Shortcut shortcut = shortcuts[i];
            if (i > 0) {
                buffer.append(" or ");
            }
            buffer.append("'").append(KeymapUtil.getShortcutText(shortcut)).append("'");
        }
        return buffer.toString();
    }

    static Map<String, Integer> convertColorToMap(Color color) {
        HashMap<String, Integer> returnValue = new HashMap<>();
        returnValue.put("red", color.getRed());
        returnValue.put("green", color.getGreen());
        returnValue.put("blue", color.getBlue());
        returnValue.put("alpha", color.getAlpha());
        return returnValue;
    }

    static Color convertMapToColor(Map<String, Integer> map) {
        return new Color(map.get("red"), map.get("green"), map.get("blue"), map.get("alpha"));
    }
}
