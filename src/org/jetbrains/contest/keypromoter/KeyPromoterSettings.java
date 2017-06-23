package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

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



    /** Whether popup enabled or disabled on menus clicks. */
    private boolean menusEnabled = true;
    /** Whether popup enabled or disabled on toolbar buttons clicks. */
    private boolean toolbarButtonsEnabled = true;
    /** Whether popup enabled or disabled on toolwindow buttons clicks. */
    private boolean toolWindowButtonsEnabled = true;
    /** Whether popup enabled or disabled on all buttons with mnemonics clicks. */
    private boolean allButtonsEnabled = false;

    private int proposeToCreateShortcutCount = 3;
    /** Popup position fixed or folow the mouse clicks. */

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

    int getProposeToCreateShortcutCount() {
        return proposeToCreateShortcutCount;
    }

    void setProposeToCreateShortcutCount(int proposeToCreateShortcutCount) {
        this.proposeToCreateShortcutCount = proposeToCreateShortcutCount;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyPromoterSettings that = (KeyPromoterSettings) o;

        if (allButtonsEnabled != that.allButtonsEnabled) return false;
        if (menusEnabled != that.menusEnabled) return false;
        if (proposeToCreateShortcutCount != that.proposeToCreateShortcutCount) return false;
        if (toolWindowButtonsEnabled != that.toolWindowButtonsEnabled) return false;
        if (toolbarButtonsEnabled != that.toolbarButtonsEnabled) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (menusEnabled ? 1 : 0);
        result = 31 * result + (toolbarButtonsEnabled ? 1 : 0);
        result = 31 * result + (toolWindowButtonsEnabled ? 1 : 0);
        result = 31 * result + (allButtonsEnabled ? 1 : 0);
        result = 31 * result + proposeToCreateShortcutCount;
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
