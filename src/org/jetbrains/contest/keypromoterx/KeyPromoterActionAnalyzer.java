package org.jetbrains.contest.keypromoterx;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.actionSystem.impl.ActionMenuItem;
import com.intellij.openapi.actionSystem.impl.actionholder.ActionRef;
import com.intellij.openapi.wm.impl.StripeButton;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a way to extract the idea action from an AWT event.
 * @author patrick (22.06.17).
 */
public class KeyPromoterActionAnalyzer {

    private static final String metaKey = System.getProperty("os.name").contains("OS X") ? "⌘" : "Alt+";
    // Fields with actions of supported classes
    private final Map<Class, Field> myClassFields = new HashMap<>(5);
    private ActionSource mySource = ActionSource.INVALID;
    private int myMnemonic = 0;
    private String myShortcut = "";
    private String myDescription = "";
    private String myIdeaActionID = "";

    KeyPromoterActionAnalyzer(AWTEvent event) {
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

    private void analyzeActionButton(ActionButton source) {
        final AnAction action = source.getAction();
        if (action != null) {
            fixValuesFromAction(action);
        }
        mySource = ActionSource.TOOLBAR_BUTTON;
    }

    private void analyzeActionMenuItem(ActionMenuItem source) {
        mySource = ActionSource.MENU_ENTRY;
        myDescription = source.getText();
        myMnemonic = source.getMnemonic();
        final Field actionField = findActionField(source, ActionRef.class);
        if (actionField != null) {
            try {
                final ActionRef o = (ActionRef) actionField.get(source);
                final AnAction action = o.getAction();
                if (action != null) {
                    fixValuesFromAction(action);
                }
            } catch (Exception e) {
                // happens..
            }
        }
    }

    private void analyzeStripeButton(StripeButton source) {
        mySource = ActionSource.TOOLWINDOW_BUTTON;
        myDescription = source.getText();
        myMnemonic = source.getMnemonic2();
        // This is a hack, but I don't see a way how to get the IDEA Id of the action from a stripe button
        myIdeaActionID = "Activate" + StringUtils.replace(myDescription, " ", "") + "ToolWindow";
    }

    private void analyzeJButton(JButton source) {
        mySource = ActionSource.OTHER;
        myMnemonic = source.getMnemonic();
        myDescription = source.getText();
    }

    private Field findActionField(Object source, Class<?> target) {
        Field field;
        if (!myClassFields.containsKey(source.getClass())) {
            field = KeyPromoterUtils.getFieldOfType(source.getClass(), target);
            if (field == null) {
                return null;
            }
            myClassFields.put(source.getClass(), field);
        } else {
            field = myClassFields.get(source.getClass());
        }
        return field;
    }

    private void fixValuesFromAction(AnAction anAction) {
        myShortcut = anAction.getShortcutSet() != null ? KeyPromoterUtils.getKeyboardShortcutsText(anAction) : "";
        myDescription = anAction.getTemplatePresentation().getText();
        myIdeaActionID = ActionManager.getInstance().getId(anAction);
    }

    ActionSource getSource() {
        return mySource;
    }

    public int getMnemonic() {
        return myMnemonic;
    }

    String getShortcut() {
        if (myMnemonic > 0) {
            myShortcut = "\'" + metaKey + (char) myMnemonic + "\'";
        }
        return myShortcut;
    }

    String getDescription() {
        return myDescription;
    }

    String getIdeaActionID() {
        return myIdeaActionID;
    }

    enum ActionSource {
        TOOLBAR_BUTTON,
        TOOLWINDOW_BUTTON,
        MENU_ENTRY,
        OTHER,
        INVALID
    }
}
