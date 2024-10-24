/*
 * Copyright (c) 2021 Patrick Scheibe, Dmitry Kashin, Athiele.
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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static de.halirutan.keypromoterx.KeyPromoterTipService.ActionType;

/**
 * KeyPromoterActionListener listens for actions performed within the application
 * and processes them based on the action type and the place where the
 * action occurred.
 */
public class KeyPromoterActionListener implements AnActionListener {


  /**
   * Handles actions performed within the application before they are executed,
   * evaluating their type and origin to determine whether a Key Promoter tip should be shown.
   *
   * @param action the action that was performed
   * @param event  the event that triggered the action
   */
  @Override
  public void beforeActionPerformed(@NotNull AnAction action, AnActionEvent event) {
    final InputEvent input = event.getInputEvent();
    ActionType type;
    if (input instanceof MouseEvent) {
      type = ActionType.MouseAction;
    } else if (input instanceof KeyEvent) {
      type = ActionType.KeyboardAction;
    } else {
      return;
    }

    final String place = event.getPlace();
    KeyPromoterAction kpAction;
    KeyPromoterSettings keyPromoterSettings = ApplicationManager.getApplication().getService(KeyPromoterSettings.class);
    KeyPromoterTipService tipService = ApplicationManager.getApplication().getService(KeyPromoterTipService.class);
    if ("ToolwindowToolbar".equals(place)) {
      if (keyPromoterSettings.isToolWindowButtonsEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.TOOL_WINDOW_BUTTON);
        tipService.showTip(kpAction, type);
      }
    } else if ("MainMenu".equals(place)) {
      if (keyPromoterSettings.isMenusEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.MENU_ENTRY);
        tipService.showTip(kpAction, type);
      }
    } else if ("MainToolbar".equals(place)) {
      if (keyPromoterSettings.isToolbarButtonsEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.MAIN_TOOLBAR);
        tipService.showTip(kpAction, type);
      }
    } else if (place.matches(".*Popup")) {
      if (keyPromoterSettings.isEditorPopupEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.POPUP);
        tipService.showTip(kpAction, type);
      }
    } else if (keyPromoterSettings.isAllButtonsEnabled()) {
      kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.OTHER);
      tipService.showTip(kpAction, type);
    }
  }
}
