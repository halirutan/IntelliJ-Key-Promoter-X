/*
 * Copyright (c) 2017 Patrick Scheibe, Dmitry Kashin, Athiele.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.halirutan.keypromoterx;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Settings for KeyPromoter plugin.
 *
 * @author Dmitry Kashin, Patrick Scheibe
 */
@State(
        name = "KeyPromoterXSettings",
        storages = {@Storage("KeyPromoterXSettings.xml")}
)
public class KeyPromoterSettings implements PersistentStateComponent<KeyPromoterSettings> {


    /**
     * Whether to show only shortcuts for the keyboard
     */
    private boolean showKeyboardShortcutsOnly = true;
    /**
     * Whether popup enabled or disabled on menus clicks.
     */
    private boolean menusEnabled = true;
    /**
     * Whether popup enabled or disabled on toolbar buttons clicks.
     */
    private boolean toolbarButtonsEnabled = true;
    /**
     * Whether popup enabled or disabled on tool-window buttons clicks.
     */
    private boolean toolWindowButtonsEnabled = true;
    /**
     * Editor popups are the context menu that can be opened with right-click on the mouse
     */
    private boolean editorPopupEnabled = true;
    /**
     * Whether popup enabled or disabled on all buttons with mnemonics clicks.
     */
    private boolean allButtonsEnabled = true;

    /**
     * If the user clicks a button, usually each time a notification is shown (setting 1). If setting is 2, only
     * each second click will trigger a notification, and so on.
     */
    private int showTipsClickCount = 1;

    private int proposeToCreateShortcutCount = 3;


    boolean isShowKeyboardShortcutsOnly() {
        return showKeyboardShortcutsOnly;
    }

    void setShowKeyboardShortcutsOnly(boolean enabled) {
        this.showKeyboardShortcutsOnly = enabled;
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

    boolean isEditorPopupEnabled() { return editorPopupEnabled; }

    void setEditorPopupEnabled(boolean editorPopupEnabled) {
        this.editorPopupEnabled = editorPopupEnabled;
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


    int getShowTipsClickCount() {
        return showTipsClickCount;
    }

    void setShowTipsClickCount(int showTipsClickCount) {
        this.showTipsClickCount = showTipsClickCount;
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
        if (editorPopupEnabled != that.editorPopupEnabled) return false;
        if (showTipsClickCount != that.showTipsClickCount) return false;
        if (showKeyboardShortcutsOnly != that.showKeyboardShortcutsOnly) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (menusEnabled ? 1 : 0);
        result = 31 * result + (toolbarButtonsEnabled ? 1 : 0);
        result = 31 * result + (toolWindowButtonsEnabled ? 1 : 0);
        result = 31 * result + (editorPopupEnabled ? 1 : 0);
        result = 31 * result + (allButtonsEnabled ? 1 : 0);
        result = 31 * result + proposeToCreateShortcutCount;
        result = 31 * result + showTipsClickCount;
        result = 31 * result + (showKeyboardShortcutsOnly ? 1 : 0);
        return result;
    }

    @Nullable
    @Override
    public KeyPromoterSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull KeyPromoterSettings keyPromoterSettings) {
        XmlSerializerUtil.copyBean(keyPromoterSettings, this);
    }

}
