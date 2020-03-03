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
    storages = {
        @Storage(
            value = "KeyPromoterXSettings.xml"
        )
    }
)
public class KeyPromoterSettings implements PersistentStateComponent<KeyPromoterSettings> {

  /**
   * Whether to show only shortcuts for the keyboard
   */
  public boolean showKeyboardShortcutsOnly = true;
  /**
   * Whether popup enabled or disabled on menus clicks.
   */
  public boolean menusEnabled = true;
  /**
   * Whether popup enabled or disabled on toolbar buttons clicks.
   */
  public boolean toolbarButtonsEnabled = true;
  /**
   * Whether popup enabled or disabled on tool-window buttons clicks.
   */
  public boolean toolWindowButtonsEnabled = true;
  /**
   * Editor popups are the context menu that can be opened with right-click on the mouse
   */
  public boolean editorPopupEnabled = true;
  /**
   * Whether popup enabled or disabled on all buttons with mnemonics clicks.
   */
  public boolean allButtonsEnabled = true;

  /**
   * If the user clicks a button, usually each time a notification is shown (setting 1). If setting is 2, only each
   * second click will trigger a notification, and so on.
   */
  public int showTipsClickCount = 1;

  public int proposeToCreateShortcutCount = 3;

  /**
   * Whether to show notification when the IDE is in Presentation Mode
   */
  public boolean disabledInPresentationMode = false;

  /**
   * Whether to show notification when the IDE is in Distraction Free Mode
   */
  public boolean disabledInDistractionFreeMode = false;

  public String installedVersion = "1.0";


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

  boolean isEditorPopupEnabled() {
    return editorPopupEnabled;
  }

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

  public String getInstalledVersion() {
    return installedVersion;
  }

  public void setInstalledVersion(String installedVersion) {
    this.installedVersion = installedVersion;
  }

  public boolean isDisabledInPresentationMode() {
    return disabledInPresentationMode;
  }

  public void setDisabledInPresentationMode(boolean disabledInPresentationMode) {
    this.disabledInPresentationMode = disabledInPresentationMode;
  }

  public boolean isDisabledInDistractionFreeMode() {
    return disabledInDistractionFreeMode;
  }

  public void setDisabledInDistractionFreeMode(boolean disabledInDistractionFreeMode) {
    this.disabledInDistractionFreeMode = disabledInDistractionFreeMode;
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
