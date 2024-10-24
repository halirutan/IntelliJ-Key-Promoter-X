/*
 * Copyright (c) 2020 Patrick Scheibe, Dmitry Kashin, Athiele.
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

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static de.halirutan.keypromoterx.KeyPromoterNotification.ShowMode.showModeFromSettings;

/**
 * The KeyPromoterTipService is responsible for displaying tips and notifications to
 * users regarding the usage of shortcuts in the application.
 */
@Service(Service.Level.APP)
public final class KeyPromoterTipService {

  private static final String distractionFreeModeKey = "editor.distraction.free.mode";
  private final Map<String, Integer> withoutShortcutStats = Collections.synchronizedMap(new HashMap<>());

  /**
   * Displays a tip or notification corresponding to the given action.
   * Depending on the action type and the configured settings, this method
   * will either record the action, show a tip, or propose creating a shortcut
   * for actions that do not have an associated shortcut.
   *
   * @param action The action for which the tip or notification is being shown.
   *               It must be valid and not suppressed, and should not be muted by default.
   * @param type   The type of the action, which influences how the tip or notification is displayed.
   *               The method does nothing for actions of type {@code ActionType.Unknown}.
   */
  public void showTip(KeyPromoterAction action, ActionType type) {
    KeyPromoterStatistics statsService = ApplicationManager.getApplication().getService(KeyPromoterStatistics.class);
    KeyPromoterSettings keyPromoterSettings = ApplicationManager.getApplication().getService(KeyPromoterSettings.class);

    if (action == null
        || !action.isValid()
        || statsService.isSuppressed(action)
        || disabledInPresentationMode()
        || disabledInDistractionFreeMode()
        || SnoozeNotifier.isSnoozed()
        || type == ActionType.Unknown
        || action.isMutedByDefault()
    ) {
      return;
    }

    final String shortcut = action.getShortcut();
    if (!StringUtil.isEmpty(shortcut)) {
      if (type == ActionType.MouseAction) {
        statsService.registerAction(action);
        int count = statsService.get(action).count;
        if (count % keyPromoterSettings.getShowTipsClickCount() == 0) {
          KeyPromoterNotification.displayNotification(action, statsService.get(action).getCount(), showModeFromSettings(keyPromoterSettings));
        }
      } else if (type == ActionType.KeyboardAction) {
        statsService.registerShortcutUsed(action);
      }

    } else {
      final String ideaActionID = action.getIdeaActionID();
      withoutShortcutStats.putIfAbsent(ideaActionID, 0);
      withoutShortcutStats.put(ideaActionID, withoutShortcutStats.get(ideaActionID) + 1);
      if (keyPromoterSettings.getProposeToCreateShortcutCount() > 0 &&
          withoutShortcutStats.get(ideaActionID) % keyPromoterSettings.getProposeToCreateShortcutCount() == 0
      ) {
        if (!(type == ActionType.MouseAction && KeyPromoterUtils.hasMouseShortcut(ideaActionID))) {
          KeyPromoterNotification.askToCreateShortcut(action, showModeFromSettings(keyPromoterSettings));
        }
      }
    }
  }

  /**
   * Checks if the KeyPromoter plugin is disabled in presentation mode.
   *
   * @return {@code true} if the plugin is disabled in presentation mode, {@code false} otherwise
   */
  private boolean disabledInPresentationMode() {
    KeyPromoterSettings keyPromoterSettings = ApplicationManager.getApplication().getService(KeyPromoterSettings.class);
    boolean isPresentationMode = UISettings.getInstance().getPresentationMode();
    return isPresentationMode && keyPromoterSettings.isDisabledInPresentationMode();
  }

  /**
   * Checks if the KeyPromoter plugin is disabled in distraction-free mode.
   *
   * @return {@code true} if the plugin is disabled in distraction-free mode, {@code false} otherwise
   */
  private boolean disabledInDistractionFreeMode() {
    KeyPromoterSettings keyPromoterSettings = ApplicationManager.getApplication().getService(KeyPromoterSettings.class);
    final boolean isDistractionFreeMode = Registry.get(distractionFreeModeKey).asBoolean();
    return isDistractionFreeMode && keyPromoterSettings.isDisabledInDistractionFreeMode();
  }

  /**
   * Represents the type of action performed within the application.
   * This type can influence how tips or notifications are displayed.
   */
  public enum ActionType {
    MouseAction,
    KeyboardAction,
    Unknown
  }

}
