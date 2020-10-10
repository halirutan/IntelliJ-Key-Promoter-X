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

import com.intellij.application.Topics;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.impl.StripeButton;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The main component that is registered in plugin.xml. It will take care of catching UI events
 * and transfers the them to {@link KeyPromoterAction} for inspection. Depending on the type of action (tool-window button,
 * menu entry, etc.) a balloon is shown and the statistic is updated.
 *
 * @author Patrick Scheibe, Dmitry Kashin
 */
public class KeyPromoter implements AWTEventListener, AnActionListener, Disposable {

  /**
   * Transfers the event to {@link KeyPromoterAction} and inspects the results. Then, depending on the result and the
   * Key Promoter X settings, a balloon is shown with the shortcut tip and the statistic is updated.
   *
   * @param e event that is handled
   */
  private void handleMouseEvent(AWTEvent e) {
    if (e.getSource() instanceof StripeButton && keyPromoterSettings.isToolWindowButtonsEnabled()) {
      KeyPromoterAction action = new KeyPromoterAction(e);
      showTip(action, ActionType.MouseAction);
    }
  }

  private final Map<String, Integer> withoutShortcutStats = Collections.synchronizedMap(new HashMap<>());
  private final KeyPromoterStatistics statsService = ServiceManager.getService(KeyPromoterStatistics.class);
  // Presentation and stats fields.
  private final KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);
  private static final String distractionFreeModeKey = "editor.distraction.free.mode";
  private long lastEventTime = -1;
  private boolean mouseDrag = false;


  public KeyPromoter() {
    Topics.subscribe(AnActionListener.TOPIC, this, this);
    long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK;
    Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
  }

  @Override
  public void dispose() {
    Toolkit.getDefaultToolkit().removeAWTEventListener(this);
  }

  /**
   * Catches all UI events from the main IDEA AWT making it possible to inspect all mouse-clicks.
   * Note that on OSX this will not catch clicks on the (detached) menu bar.
   * It also takes care of handling drag events that we're not interested in.
   *
   * @param e event that is caught
   */
  @Override
  public void eventDispatched(AWTEvent e) {
    int id = e.getID();
    if (id == MouseEvent.MOUSE_DRAGGED) {
      mouseDrag = true;
      return;
    }

    if (id == MouseEvent.MOUSE_RELEASED && ((MouseEvent) e).getButton() == MouseEvent.BUTTON1) {
      if (!mouseDrag) {
        handleMouseEvent(e);
      }
      mouseDrag = false;
    }
  }

  @Override
  public void beforeActionPerformed(@NotNull AnAction action, @NotNull DataContext dataContext, AnActionEvent event) {
    final InputEvent input = event.getInputEvent();

    ActionType type;
    if (input instanceof MouseEvent) {
      type = ActionType.MouseAction;
    } else if (input instanceof KeyEvent) {
      type = ActionType.KeyboardAction;
    } else {
      return;
    }

    // The following is a hack to work around an issue with IDEA, where certain events arrive
    // twice. See https://youtrack.jetbrains.com/issue/IDEA-219133
    if (input.getWhen() != 0 && lastEventTime == input.getWhen()) {
      return;
    }
    lastEventTime = input.getWhen();

    final String place = event.getPlace();
    KeyPromoterAction kpAction;
    if ("MainMenu".equals(place)) {
      if (keyPromoterSettings.isMenusEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.MENU_ENTRY);
        showTip(kpAction, type);
      }
    } else if ("MainToolbar".equals(place)) {
      if (keyPromoterSettings.isToolbarButtonsEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.MAIN_TOOLBAR);
        showTip(kpAction, type);
      }
    } else if (place.matches(".*Popup")) {
      if (keyPromoterSettings.isEditorPopupEnabled()) {
        kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.POPUP);
        showTip(kpAction, type);
      }
    } else if (keyPromoterSettings.isAllButtonsEnabled()) {
      kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.OTHER);
      showTip(kpAction, type);
    }
  }

  private void showTip(KeyPromoterAction action, ActionType type) {
    if (action == null
        || !action.isValid()
        || statsService.isSuppressed(action)
        || disabledInPresentationMode()
        || disabledInDistractionFreeMode()
        || SnoozeNotifier.isSnoozed()
        || type == ActionType.Unknown
    ) {
      return;
    }

    final String shortcut = action.getShortcut();
    if (!StringUtil.isEmpty(shortcut)) {
      if (type == ActionType.MouseAction) {
        statsService.registerAction(action);
        int count = statsService.get(action).count;
        if (count % keyPromoterSettings.getShowTipsClickCount() == 0) {
          KeyPromoterNotification.showTip(action, statsService.get(action).getCount());
        }
      } else if (type == ActionType.KeyboardAction) {
        statsService.registerShortcutUsed(action);
      }

    } else {
      final String ideaActionID = action.getIdeaActionID();
      withoutShortcutStats.putIfAbsent(ideaActionID, 0);
      withoutShortcutStats.put(ideaActionID, withoutShortcutStats.get(ideaActionID) + 1);
      if (keyPromoterSettings.getProposeToCreateShortcutCount() > 0
          &&
          withoutShortcutStats.get(ideaActionID) % keyPromoterSettings.getProposeToCreateShortcutCount() == 0) {
        KeyPromoterNotification.askToCreateShortcut(action);

      }
    }
  }

  private boolean disabledInPresentationMode() {
    boolean isPresentationMode = UISettings.getInstance().getPresentationMode();
    return isPresentationMode && keyPromoterSettings.isDisabledInPresentationMode();
  }

  private boolean disabledInDistractionFreeMode() {
    final boolean isDistractionFreeMode = Registry.get(distractionFreeModeKey).asBoolean();
    return isDistractionFreeMode && keyPromoterSettings.isDisabledInDistractionFreeMode();
  }

  enum ActionType {
    MouseAction,
    KeyboardAction,
    Unknown
  }
}
