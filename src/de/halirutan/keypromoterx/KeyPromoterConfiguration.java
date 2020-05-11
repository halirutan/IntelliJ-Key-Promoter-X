/*
 * Copyright (c) 2019 Patrick Scheibe, Dmitry Kashin, Athiele.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.halirutan.keypromoterx;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.ColorPanel;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Configuration of plugin saving and editing for the Key Promoter X
 *
 * @author Patrick Scheibe, Dmitry Kashin
 */
@SuppressWarnings("unused")
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
  private JCheckBox myShowKeyboardShortcutsOnly;
  private JCheckBox myDisabledInPresentationMode;
  private JCheckBox myDisabledInDistractionFreeMode;
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
    if (myShowKeyboardShortcutsOnly.isSelected() != keyPromoterSettings.isShowKeyboardShortcutsOnly()) return true;
    if (!myProposeToCreateShortcutCount.getValue().equals(keyPromoterSettings.getProposeToCreateShortcutCount())) {
      return true;
    }
    if (!myShowClickCount.getValue().equals(keyPromoterSettings.getShowTipsClickCount())) return true;
    if (myDisabledInPresentationMode.isSelected() != keyPromoterSettings.isDisabledInPresentationMode()) return true;
    if (myDisabledInDistractionFreeMode.isSelected() != keyPromoterSettings.isDisabledInDistractionFreeMode()) return true;

    return false;
  }

  public void apply() {
    keyPromoterSettings.setMenusEnabled(myMenus.isSelected());
    keyPromoterSettings.setToolbarButtonsEnabled(myToolbarButtons.isSelected());
    keyPromoterSettings.setToolWindowButtonsEnabled(myToolWindowButtons.isSelected());
    keyPromoterSettings.setEditorPopupEnabled(myEditorPopupButtons.isSelected());
    keyPromoterSettings.setAllButtonsEnabled(myAllButtons.isSelected());
    keyPromoterSettings.setShowKeyboardShortcutsOnly(myShowKeyboardShortcutsOnly.isSelected());
    keyPromoterSettings.setDisabledInPresentationMode(myDisabledInPresentationMode.isSelected());
    keyPromoterSettings.setDisabledInDistractionFreeMode(myDisabledInDistractionFreeMode.isSelected());
    keyPromoterSettings
            .setProposeToCreateShortcutCount(Integer.parseInt(myProposeToCreateShortcutCount.getValue().toString()));
    keyPromoterSettings.setShowTipsClickCount(Integer.parseInt(myShowClickCount.getValue().toString()));
  }

  public void reset() {
    myMenus.setSelected(keyPromoterSettings.isMenusEnabled());
    myToolbarButtons.setSelected(keyPromoterSettings.isToolbarButtonsEnabled());
    myToolWindowButtons.setSelected(keyPromoterSettings.isToolWindowButtonsEnabled());
    myEditorPopupButtons.setSelected(keyPromoterSettings.isEditorPopupEnabled());
    myAllButtons.setSelected(keyPromoterSettings.isAllButtonsEnabled());
    myShowKeyboardShortcutsOnly.setSelected(keyPromoterSettings.isShowKeyboardShortcutsOnly());
    myDisabledInPresentationMode.setSelected(keyPromoterSettings.isDisabledInPresentationMode());
    myDisabledInDistractionFreeMode.setSelected(keyPromoterSettings.isDisabledInDistractionFreeMode());
    myProposeToCreateShortcutCount.setValue(keyPromoterSettings.getProposeToCreateShortcutCount());
    myShowClickCount.setValue(keyPromoterSettings.getShowTipsClickCount());
  }

  public void disposeUIResources() {
  }

  public KeyPromoterSettings getSettings() {
    return keyPromoterSettings;
  }

  public void setSettings(KeyPromoterSettings settings) {
    keyPromoterSettings = settings;
  }

  public KeyPromoterConfiguration getState() {
    return this;
  }

  public void loadState(@NotNull KeyPromoterConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  private void createUIComponents() {
    myProposeToCreateShortcutCount = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
    myShowClickCount = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
  }
}
