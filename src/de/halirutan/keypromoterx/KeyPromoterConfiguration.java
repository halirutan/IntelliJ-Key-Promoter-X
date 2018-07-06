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
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.ColorPanel;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


/**
 * Configuration of plugin saving and editing for the Key Promoter X
 *
 * @author Patrick Scheibe, Dmitry Kashin
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class KeyPromoterConfiguration extends BaseConfigurable implements SearchableConfigurable, PersistentStateComponent<KeyPromoterConfiguration> {

    JPanel myConfigPanel;
    private JSpinner myDisplayTime;
    private JCheckBox myAllButtons;
    private JCheckBox myToolWindowButtons;
    private JCheckBox myToolbarButtons;
    private JCheckBox myMenus;
    private JCheckBox myFixedTipPosition;

    private JSpinner myAnimationDelay;
    private ColorPanel myTextColor;
    private ColorPanel myBackgroundColor;
    private ColorPanel myBorderColor;
    private JSpinner myProposeToCreateShortcutCount;
    private JSpinner myNumberOfTipsShown;
    private JCheckBox myEditorPopupButtons;
    private JSpinner myShowClickCount;
    private JTextPane myPopupTemplate;

    private KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);

    @NotNull
    public String getId() {
        return "KeyPromoterConfiguration";
    }

    public Runnable enableSearch(String s) {
        return null;
    }

    public String getDisplayName() {
        return "KeyPromoter";
    }

    public Icon getIcon() {
        return KeyPromoterIcons.KP_ICON;
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        return myConfigPanel;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean isModified() {
        if (myMenus.isSelected() != keyPromoterSettings.isMenusEnabled()) return true;
        if (myToolbarButtons.isSelected() != keyPromoterSettings.isToolbarButtonsEnabled()) return true;
        if (myToolWindowButtons.isSelected() != keyPromoterSettings.isToolWindowButtonsEnabled()) return true;
        if (myEditorPopupButtons.isSelected() != keyPromoterSettings.isEditorPopupEnabled()) return true;
        if (myAllButtons.isSelected() != keyPromoterSettings.isAllButtonsEnabled()) return true;
        if (!myProposeToCreateShortcutCount.getValue().equals(keyPromoterSettings.getProposeToCreateShortcutCount()))
            return true;
        if (!myNumberOfTipsShown.getValue().equals(keyPromoterSettings.getMaxNumberOfTips())) return true;
        if (!myShowClickCount.getValue().equals(keyPromoterSettings.getShowTipsClickCount())) return true;
        return false;
    }

    public void apply() {
        keyPromoterSettings.setMenusEnabled(myMenus.isSelected());
        keyPromoterSettings.setToolbarButtonsEnabled(myToolbarButtons.isSelected());
        keyPromoterSettings.setToolWindowButtonsEnabled(myToolWindowButtons.isSelected());
        keyPromoterSettings.setEditorPopupEnabled(myEditorPopupButtons.isSelected());
        keyPromoterSettings.setAllButtonsEnabled(myAllButtons.isSelected());
        keyPromoterSettings.setProposeToCreateShortcutCount(new Integer(myProposeToCreateShortcutCount.getValue().toString()));
        keyPromoterSettings.setMaxNumberOfTips(new Integer(myNumberOfTipsShown.getValue().toString()));
        keyPromoterSettings.setShowTipsClickCount(new Integer(myShowClickCount.getValue().toString()));
    }

    public void reset() {
        myMenus.setSelected(keyPromoterSettings.isMenusEnabled());
        myToolbarButtons.setSelected(keyPromoterSettings.isToolbarButtonsEnabled());
        myToolWindowButtons.setSelected(keyPromoterSettings.isToolWindowButtonsEnabled());
        myEditorPopupButtons.setSelected(keyPromoterSettings.isEditorPopupEnabled());
        myAllButtons.setSelected(keyPromoterSettings.isAllButtonsEnabled());
        myProposeToCreateShortcutCount.setValue(keyPromoterSettings.getProposeToCreateShortcutCount());
        myNumberOfTipsShown.setValue(keyPromoterSettings.getMaxNumberOfTips());
        myShowClickCount.setValue(keyPromoterSettings.getShowTipsClickCount());
    }

    public void disposeUIResources() {
    }

    public void setSettings(KeyPromoterSettings settings) {
        keyPromoterSettings = settings;
    }

    public KeyPromoterSettings getSettings() {
        return keyPromoterSettings;
    }

    public KeyPromoterConfiguration getState() {
        return this;
    }

    public void loadState(KeyPromoterConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private void createUIComponents() {
        myNumberOfTipsShown = new JSpinner(new SpinnerNumberModel(0, 0, 9, 1));
        myProposeToCreateShortcutCount = new JSpinner(new SpinnerNumberModel(0, 0, 9, 1));
        myShowClickCount = new JSpinner(new SpinnerNumberModel(1, 1, 9, 1));
    }
}
