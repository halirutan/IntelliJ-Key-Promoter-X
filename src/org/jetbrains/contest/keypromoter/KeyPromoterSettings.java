package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;

import static org.jetbrains.contest.keypromoter.KeyPromoterUtils.convertColorToMap;
import static org.jetbrains.contest.keypromoter.KeyPromoterUtils.convertMapToColor;

/**
 * Settings for KeyPromoter plugin.
 * @author Dmitry Kashin
 */
@State(
        name = "KeyPromoterSettings",
        storages = {
                @Storage(
                        file = "$APP_CONFIG$/KeyPromoterSettings.xml",
                        id = "KeyPromoterSettings"
                )}
)
public class KeyPromoterSettings implements PersistentStateComponent<KeyPromoterSettings> {

    /* Had to make these colors "transient", as somehow the Color class cannot be serialized properly anymore
       Therefore, you cannot persist the color settings. Without marking this as transient, all settings will
       get lost when changing one of the colours. */
    /** Color of text in popup */
    private Color textColor = Color.BLACK;
    /** Color of border of popup */
    private Color borderColor = Color.RED;
    /** Background Color of popup */
    private Color backgroundColor = new Color(0x202040);

    /** Whether popup enabled or disabled on menus clicks. */
    private boolean menusEnabled = true;
    /** Whether popup enabled or disabled on toolbar buttons clicks. */
    private boolean toolbarButtonsEnabled = true;
    /** Whether popup enabled or disabled on toolwindow buttons clicks. */
    private boolean toolWindowButtonsEnabled = true;
    /** Whether popup enabled or disabled on all buttons with mnemonics clicks. */
    private boolean allButtonsEnabled = false;

    /** Time of popup display. */
    private long displayTime = 3000;
    /** Animation delay time. */
    private long flashAnimationDelay = 150;
    /** Count of invocations after which ask for creation of shortcut for actions without them. */
    private int proposeToCreateShortcutCount = 3;
    /** Popup position fixed or folow the mouse clicks. */
    private boolean fixedTipPosistion = false;
    /** Popup template. */
    private String popupTemplate = "<html>\n" +
            " <body>\n" +
            "  <table>\n" +
            "   <tr>\n" +
            "    <td align=\"center\"><font size=8>{0}</font></td>\n" +
            "   </tr>\n" +
            "   <tr>\n" +
            "    <td align=\"center\"><font size=6>{1} time(s)</font></td>\n" +
            "   </tr>\n" +
            "  </table>\n" +
            " </body>\n" +
            "</html>";

    long getDisplayTime() {
        return displayTime;
    }

    void setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
    }

    long getFlashAnimationDelay() {
        return flashAnimationDelay;
    }

    void setFlashAnimationDelay(long flashAnimationDelay) {
        this.flashAnimationDelay = flashAnimationDelay;
    }

    boolean isMenusEnabled() {
        return menusEnabled;
    }

    void setMenusEnabled(boolean menusEnabled) {
        this.menusEnabled = menusEnabled;
    }

    boolean isToolbarButtonsEnabled() {
        return toolbarButtonsEnabled;
    }

    void setToolbarButtonsEnabled(boolean toolbarButtonsEnabled) {
        this.toolbarButtonsEnabled = toolbarButtonsEnabled;
    }

    boolean isToolWindowButtonsEnabled() {
        return toolWindowButtonsEnabled;
    }

    void setToolWindowButtonsEnabled(boolean toolWindowButtonsEnabled) {
        this.toolWindowButtonsEnabled = toolWindowButtonsEnabled;
    }

    boolean isAllButtonsEnabled() {
        return allButtonsEnabled;
    }

    void setAllButtonsEnabled(boolean allButtonsEnabled) {
        this.allButtonsEnabled = allButtonsEnabled;
    }

    Map<String, Integer> getTextColor() {
        return convertColorToMap(textColor);
    }

    void setTextColor(Map<String, Integer> textColor) {
        this.textColor = convertMapToColor(textColor);
    }

    Map<String, Integer> getBorderColor() {
        return convertColorToMap(borderColor);
    }

    void setBorderColor(Map<String, Integer> borderColor) {
        this.borderColor = convertMapToColor(borderColor);
    }

    Map<String, Integer> getBackgroundColor() {
        return convertColorToMap(backgroundColor);
    }

    void setBackgroundColor(Map<String, Integer> backgroundColor) {
        this.backgroundColor = convertMapToColor(backgroundColor);
    }

    boolean isFixedTipPosition() {
        return this.fixedTipPosistion;
    }

    void setFixedTipPosistion(boolean fixedTipPosistion) {
        this.fixedTipPosistion = fixedTipPosistion;
    }

    int getProposeToCreateShortcutCount() {
        return proposeToCreateShortcutCount;
    }

    void setProposeToCreateShortcutCount(int proposeToCreateShortcutCount) {
        this.proposeToCreateShortcutCount = proposeToCreateShortcutCount;
    }

    String getPopupTemplate() {
        return popupTemplate;
    }

    void setPopupTemplate(String popupTemplate) {
        this.popupTemplate = popupTemplate;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyPromoterSettings that = (KeyPromoterSettings) o;

        if (allButtonsEnabled != that.allButtonsEnabled) return false;
        if (displayTime != that.displayTime) return false;
        if (fixedTipPosistion != that.fixedTipPosistion) return false;
        if (flashAnimationDelay != that.flashAnimationDelay) return false;
        if (menusEnabled != that.menusEnabled) return false;
        if (proposeToCreateShortcutCount != that.proposeToCreateShortcutCount) return false;
        if (toolWindowButtonsEnabled != that.toolWindowButtonsEnabled) return false;
        if (toolbarButtonsEnabled != that.toolbarButtonsEnabled) return false;
        if (backgroundColor != null ? !backgroundColor.equals(that.backgroundColor) : that.backgroundColor != null)
            return false;
        if (borderColor != null ? !borderColor.equals(that.borderColor) : that.borderColor != null) return false;
        if (popupTemplate != null ? !popupTemplate.equals(that.popupTemplate) : that.popupTemplate != null)
            return false;
        if (textColor != null ? !textColor.equals(that.textColor) : that.textColor != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (textColor != null ? textColor.hashCode() : 0);
        result = 31 * result + (borderColor != null ? borderColor.hashCode() : 0);
        result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
        result = 31 * result + (menusEnabled ? 1 : 0);
        result = 31 * result + (toolbarButtonsEnabled ? 1 : 0);
        result = 31 * result + (toolWindowButtonsEnabled ? 1 : 0);
        result = 31 * result + (allButtonsEnabled ? 1 : 0);
        result = 31 * result + (int) (displayTime ^ (displayTime >>> 32));
        result = 31 * result + (int) (flashAnimationDelay ^ (flashAnimationDelay >>> 32));
        result = 31 * result + proposeToCreateShortcutCount;
        result = 31 * result + (fixedTipPosistion ? 1 : 0);
        result = 31 * result + (popupTemplate != null ? popupTemplate.hashCode() : 0);
        return result;
    }

    @Nullable
    @Override
    public KeyPromoterSettings getState() {
        return this;
    }

    @Override
    public void loadState(KeyPromoterSettings keyPromoterSettings) {
        XmlSerializerUtil.copyBean(keyPromoterSettings, this);
    }
}
