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

import com.intellij.ide.actions.ActivateToolWindowAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.actionSystem.impl.ActionMenuItem;
import com.intellij.openapi.actionSystem.impl.actionholder.ActionRef;
import com.intellij.toolWindow.StripeButton;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Provides a way to extract the idea action from an AWT event. This is the class where the magic happens. We try hard
 * to extract the IDEA action that was invoked from an AWT event. On our way, we need to extract private fields of
 * IDEA classes and still, there are cases when we won't be able to extract the action that was invoked.
 *
 * @author patrick (22.06.17).
 */
public class KeyPromoterAction {

  private static final String metaKey =
      System.getProperty("os.name").contains("OS X") ? KeyPromoterBundle.message("kp.meta.osx") :
          KeyPromoterBundle.message("kp.meta.default");
  // Fields with actions of supported classes
  private static final Map<Class<?>, Field> myClassFields = new HashMap<>(5);

  private ActionSource mySource = ActionSource.INVALID;
  private int myMnemonic = 0;
  private String myShortcut = "";
  private String myDescription = "";
  private String myIdeaActionID = "";

  private static final Set<String> MUTED_ACTIONS = Set.of(
      "RiderUnitTestPendingTestsFilterAction"
  );

  /**
   * Constructor used when have to fall back to inspect an AWT event instead of actions that are directly provided
   * by IDEA. Tool-window stripe buttons are such a case where I'm not notified by IDEA if one is pressed
   *
   * @param event mouse event that happened
   */
  KeyPromoterAction(AWTEvent event) {
    final Object source = event.getSource();
    if (source instanceof ActionButton) {
      analyzeActionButton((ActionButton) source);
    } else if (source instanceof StripeButton) {
      analyzeStripeButton((StripeButton) source);
    } else if (source instanceof ActionMenuItem) {
      analyzeActionMenuItem((ActionMenuItem) source);
    } else if (source instanceof JButton) {
      analyzeJButton((JButton) source);
    }

  }

  /**
   * Constructor used when we get notified by IDEA through {@link com.intellij.openapi.actionSystem.ex.AnActionListener}
   *
   * @param action action that was performed
   * @param event  event that fired the action
   * @param source the source of the action
   */
  KeyPromoterAction(AnAction action, AnActionEvent event, ActionSource source) {
    if (source == ActionSource.TOOL_WINDOW_BUTTON) {
      String toolWindowName = action.getTemplateText() != null ? action.getTemplateText().replaceAll(" ", "") : "";
      myIdeaActionID = KeyPromoterBundle.message("kp.toolwindow.id", toolWindowName);
    } else {
      myIdeaActionID = ActionManager.getInstance().getId(action);
    }
    myDescription = event.getPresentation().getText();
    mySource = source;
    myShortcut = KeyPromoterUtils.getKeyboardShortcutsText(myIdeaActionID);
    fixDescription();
  }

  /**
   * Information extraction for buttons on the toolbar
   *
   * @param source source of the action
   */
  private void analyzeActionButton(ActionButton source) {
    final AnAction action = source.getAction();
    fixValuesFromAction(action);
    mySource = ActionSource.MAIN_TOOLBAR;
  }

  /**
   * Information extraction for entries in the menu
   *
   * @param source source of the action
   */
  private void analyzeActionMenuItem(ActionMenuItem source) {
    mySource = ActionSource.MENU_ENTRY;
    myDescription = source.getText();
    myMnemonic = source.getMnemonic();
    final Field actionField = findActionField(source);
    if (actionField != null) {
      try {
        final ActionRef<?> o = (ActionRef<?>) actionField.get(source);
        final AnAction action = o.getAction();
        fixValuesFromAction(action);
      } catch (Exception e) {
        // happens..
      }
    }
  }

  /**
   * Information extraction for buttons of tool-windows
   *
   * @param stripeButton stripeButton of the action
   */
  private void analyzeStripeButton(StripeButton stripeButton) {
    mySource = ActionSource.TOOL_WINDOW_BUTTON;
    myDescription = stripeButton.getText();
    myMnemonic = stripeButton.getMnemonic2();
    myIdeaActionID = ActivateToolWindowAction.Manager.getActionIdForToolWindow(stripeButton.getId());
    myShortcut = KeyPromoterUtils.getKeyboardShortcutsText(myIdeaActionID);
  }

  /**
   * Information extraction for all other buttons
   * TODO: This needs to be tested. I couldn't find a button that wasn't inspected with this fallback.
   *
   * @param source source of the action
   */
  private void analyzeJButton(JButton source) {
    mySource = ActionSource.OTHER;
    myMnemonic = source.getMnemonic();
    myDescription = source.getText();
  }

  /**
   * Extracts a private field from a class so that we can access it for getting information
   *
   * @param source Object that contains the field we are interested in
   * @return The field that was found
   */
  private Field findActionField(Object source) {
    Field field;
    if (!myClassFields.containsKey(source.getClass())) {
      field = KeyPromoterUtils.getFieldOfType(source.getClass(), ActionRef.class);
      if (field == null) {
        return null;
      }
      myClassFields.put(source.getClass(), field);
    } else {
      field = myClassFields.get(source.getClass());
    }
    return field;
  }

  /**
   * This method can be used at several places to update shortcut, description and ideaAction from an {@link AnAction}
   *
   * @param anAction action to extract values from
   */
  private void fixValuesFromAction(AnAction anAction) {
    myDescription = anAction.getTemplatePresentation().getText();
    myIdeaActionID = ActionManager.getInstance().getId(anAction);
    myShortcut = KeyPromoterUtils.getKeyboardShortcutsText(myIdeaActionID);
  }

  /**
   * Used to adjust Run and Debug descriptions so that the don't contain the name of the run-configuration
   */
  private void fixDescription() {
    if (myDescription == null || myDescription.isEmpty()) {
      return;
    }
    if ("Debug".equals(myIdeaActionID) || "DebugClass".equals(myIdeaActionID)) {
      myDescription = myDescription.replaceFirst("Debug '.*'", "Debug");
    }
    if ("Run".equals(myIdeaActionID) || "RunClass".equals(myIdeaActionID)) {
      myDescription = myDescription.replaceFirst("Run '.*'", "Run");
    }
  }

  public String getShortcut() {
    if (myShortcut != null && !myShortcut.isEmpty()) {
      return myShortcut;
    }
    if (mySource.equals(ActionSource.TOOL_WINDOW_BUTTON) && myMnemonic > 0) {
      myShortcut = "'" + metaKey + (char) myMnemonic + "'";
    }
    return myShortcut;
  }

  public String getDescription() {
    return myDescription;
  }

  public String getIdeaActionID() {
    return myIdeaActionID;
  }

  /**
   * Checks if we have all necessary information about an action that was invoked
   *
   * @return true if it has a description, an actionID and a shortcut
   */
  boolean isValid() {
    return myDescription != null &&
        myIdeaActionID != null &&
        !Objects.equals(myDescription, "") &&
        !Objects.equals(myIdeaActionID, "");
  }

  /**
   * Mutes some actions that shouldn't be presented.
   *
   * @return True if it doesn't make sense to display anything
   */
  boolean isMutedByDefault() {
    if (MUTED_ACTIONS.contains(myIdeaActionID)) {
      return true;
    }
    return ("EditorCloneCaretBelow".equals(myIdeaActionID) || "EditorCloneCaretAbove".equals(myIdeaActionID)) && myShortcut.isEmpty();
  }

  enum ActionSource {
    MAIN_TOOLBAR,
    TOOL_WINDOW_BUTTON,
    MENU_ENTRY,
    POPUP,
    OTHER,
    INVALID
  }
}
