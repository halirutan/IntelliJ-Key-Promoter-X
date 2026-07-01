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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.TitledSeparator;
import com.intellij.util.ui.JBUI;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * Configuration of plugin saving and editing for the Key Promoter X
 *
 * @author Patrick Scheibe, Dmitry Kashin
 */
@SuppressWarnings("unused")
public class KeyPromoterConfiguration extends BaseConfigurable implements SearchableConfigurable, PersistentStateComponent<KeyPromoterConfiguration> {

  private JPanel myConfigPanel;
  private JCheckBox myAllButtons;
  private JCheckBox myToolWindowButtons;
  private JCheckBox myToolbarButtons;
  private JCheckBox myMenus;
  private JSpinner myProposeToCreateShortcutCount;
  private JCheckBox myEditorPopupButtons;
  private JSpinner myShowClickCount;
  private JCheckBox myShowKeyboardShortcutsOnly;
  private JCheckBox myDisabledInPresentationMode;
  private JCheckBox myDisabledInDistractionFreeMode;
  private JCheckBox myHardMode;

  private KeyPromoterSettings keyPromoterSettings = ApplicationManager.getApplication().getService(KeyPromoterSettings.class);

  @NotNull
  public String getId() {
    return "KeyPromoterConfiguration";
  }

  public Runnable enableSearch(String s) {
    return null;
  }

  private static JPanel createSectionPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    return panel;
  }

  private static TitledSeparator createSectionHeader(String title) {
    TitledSeparator separator = new TitledSeparator(title);
    separator.setBorder(JBUI.Borders.emptyBottom(8));
    separator.setAlignmentX(Component.LEFT_ALIGNMENT);
    return separator;
  }

  public Icon getIcon() {
    return KeyPromoterIcons.KP_ICON;
  }

  @Nullable
  @NonNls
  public String getHelpTopic() {
    return null;
  }

  private static JCheckBox createCheckBox(String text, int mnemonic) {
    JCheckBox checkBox = new JCheckBox(text);
    checkBox.setMnemonic(mnemonic);
    return checkBox;
  }

  private static void addVerticalComponent(JPanel panel, JComponent component, int row) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = row;
    constraints.weightx = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.WEST;
    panel.add(component, constraints);
  }

  public String getDisplayName() {
    return KeyPromoterBundle.message("kp.configurable.display.name");
  }

  public JComponent createComponent() {
    if (myConfigPanel == null) {
      myConfigPanel = buildUi();
    }
    return myConfigPanel;
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

  @SuppressWarnings("RedundantIfStatement")
  public boolean isModified() {
    if (myConfigPanel == null) {
      return false;
    }
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
    if (myHardMode.isSelected() != keyPromoterSettings.isHardMode()) return true;

    return false;
  }

  public void apply() {
    if (myConfigPanel == null) {
      return;
    }
    keyPromoterSettings.setMenusEnabled(myMenus.isSelected());
    keyPromoterSettings.setToolbarButtonsEnabled(myToolbarButtons.isSelected());
    keyPromoterSettings.setToolWindowButtonsEnabled(myToolWindowButtons.isSelected());
    keyPromoterSettings.setEditorPopupEnabled(myEditorPopupButtons.isSelected());
    keyPromoterSettings.setAllButtonsEnabled(myAllButtons.isSelected());
    keyPromoterSettings.setShowKeyboardShortcutsOnly(myShowKeyboardShortcutsOnly.isSelected());
    keyPromoterSettings.setDisabledInPresentationMode(myDisabledInPresentationMode.isSelected());
    keyPromoterSettings.setDisabledInDistractionFreeMode(myDisabledInDistractionFreeMode.isSelected());
    keyPromoterSettings.setHardMode(myHardMode.isSelected());
    keyPromoterSettings
            .setProposeToCreateShortcutCount(Integer.parseInt(myProposeToCreateShortcutCount.getValue().toString()));
    keyPromoterSettings.setShowTipsClickCount(Integer.parseInt(myShowClickCount.getValue().toString()));
  }

  public void reset() {
    if (myConfigPanel == null) {
      return;
    }
    myMenus.setSelected(keyPromoterSettings.isMenusEnabled());
    myToolbarButtons.setSelected(keyPromoterSettings.isToolbarButtonsEnabled());
    myToolWindowButtons.setSelected(keyPromoterSettings.isToolWindowButtonsEnabled());
    myEditorPopupButtons.setSelected(keyPromoterSettings.isEditorPopupEnabled());
    myAllButtons.setSelected(keyPromoterSettings.isAllButtonsEnabled());
    myShowKeyboardShortcutsOnly.setSelected(keyPromoterSettings.isShowKeyboardShortcutsOnly());
    myDisabledInPresentationMode.setSelected(keyPromoterSettings.isDisabledInPresentationMode());
    myDisabledInDistractionFreeMode.setSelected(keyPromoterSettings.isDisabledInDistractionFreeMode());
    myHardMode.setSelected(keyPromoterSettings.isHardMode());
    myProposeToCreateShortcutCount.setValue(keyPromoterSettings.getProposeToCreateShortcutCount());
    myShowClickCount.setValue(keyPromoterSettings.getShowTipsClickCount());
  }

  @Override
  public void disposeUIResources() {
    myConfigPanel = null;
    myMenus = null;
    myToolbarButtons = null;
    myToolWindowButtons = null;
    myEditorPopupButtons = null;
    myAllButtons = null;
    myShowKeyboardShortcutsOnly = null;
    myDisabledInPresentationMode = null;
    myDisabledInDistractionFreeMode = null;
    myHardMode = null;
    myShowClickCount = null;
    myProposeToCreateShortcutCount = null;
  }

  private JPanel buildUi() {
    myProposeToCreateShortcutCount = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
    myShowClickCount = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));

    myMenus = createCheckBox(KeyPromoterBundle.message("kp.configurable.menus.actions"), KeyEvent.VK_M);
    myToolbarButtons = createCheckBox(KeyPromoterBundle.message("kp.configurable.toolbar.buttons"), KeyEvent.VK_T);
    myToolWindowButtons = createCheckBox(KeyPromoterBundle.message("kp.configurable.tool.window.buttons"), KeyEvent.VK_W);
    myEditorPopupButtons = createCheckBox(KeyPromoterBundle.message("kp.configurable.popup.menu.items"), KeyEvent.VK_P);
    myAllButtons = createCheckBox(KeyPromoterBundle.message("kp.configurable.all.other.buttons"), KeyEvent.VK_A);
    myShowKeyboardShortcutsOnly = new JCheckBox(KeyPromoterBundle.message("kp.configurable.show.keyboard.shortcuts.only"));
    myDisabledInPresentationMode = new JCheckBox(KeyPromoterBundle.message("kp.configurable.disable.presentation.mode"));
    myDisabledInDistractionFreeMode = new JCheckBox(KeyPromoterBundle.message("kp.configurable.disable.distraction.free.mode"));
    myHardMode = new JCheckBox(KeyPromoterBundle.message("kp.configurable.hard.mode"));
    myHardMode.setToolTipText(KeyPromoterBundle.message("kp.configurable.hard.mode.tooltip"));

    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.add(createSectionHeader(KeyPromoterBundle.message("kp.configurable.section.general")));
    content.add(createGeneralPanel());
    content.add(Box.createVerticalStrut(JBUI.scale(12)));
    content.add(createSettingsPanel());
    content.add(Box.createVerticalStrut(JBUI.scale(12)));
    content.add(createSectionHeader(KeyPromoterBundle.message("kp.configurable.section.enabled.for")));
    content.add(createEnabledForPanel());

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(content, BorderLayout.NORTH);
    return panel;
  }

  private JPanel createGeneralPanel() {
    JPanel panel = createSectionPanel();
    addVerticalComponent(panel, myShowKeyboardShortcutsOnly, 0);
    addVerticalComponent(panel, myDisabledInPresentationMode, 1);
    addVerticalComponent(panel, myDisabledInDistractionFreeMode, 2);
    addVerticalComponent(panel, myHardMode, 3);
    return panel;
  }

  private JPanel createSettingsPanel() {
    JPanel panel = createSectionPanel();

    JLabel showClickCountLabel = new JLabel(KeyPromoterBundle.message("kp.configurable.show.click.count"));
    JLabel proposeShortcutLabel = new JLabel(KeyPromoterBundle.message("kp.configurable.propose.shortcut.count"));
    proposeShortcutLabel.setDisplayedMnemonic(KeyEvent.VK_N);
    proposeShortcutLabel.setLabelFor(myProposeToCreateShortcutCount);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.insets = JBUI.insetsBottom(6);
    panel.add(showClickCountLabel, constraints);

    constraints.gridy = 1;
    constraints.insets = JBUI.emptyInsets();
    panel.add(proposeShortcutLabel, constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.EAST;
    constraints.insets = JBUI.insets(0, 12, 6, 0);
    panel.add(myShowClickCount, constraints);

    Dimension spinnerSize = myProposeToCreateShortcutCount.getPreferredSize();
    myProposeToCreateShortcutCount.setMinimumSize(new Dimension(JBUI.scale(40), spinnerSize.height));
    constraints.gridy = 1;
    constraints.insets = JBUI.insetsLeft(12);
    panel.add(myProposeToCreateShortcutCount, constraints);

    constraints.gridx = 2;
    constraints.gridy = 0;
    constraints.gridheight = 2;
    constraints.weightx = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = JBUI.emptyInsets();
    panel.add(Box.createHorizontalStrut(0), constraints);
    return panel;
  }

  private JPanel createEnabledForPanel() {
    JPanel panel = createSectionPanel();
    addVerticalComponent(panel, myMenus, 0);
    addVerticalComponent(panel, myToolbarButtons, 1);
    addVerticalComponent(panel, myToolWindowButtons, 2);
    addVerticalComponent(panel, myEditorPopupButtons, 3);
    addVerticalComponent(panel, myAllButtons, 4);
    return panel;
  }
}
