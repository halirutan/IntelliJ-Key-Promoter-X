package de.halirutan.keypromoterx;

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
 * Provides a way to extract the idea action from an AWT event. This is the class where the magic happens. We try hard
 * to extract the IDEA action that was invoked from an AWT event. On our way, we need to extract private fields of
 * IDEA classes and still, there are cases when we won't be able to extract the action that was invoked.
 * @author patrick (22.06.17).
 */
public class KeyPromoterAction {

    private static final String metaKey = System.getProperty("os.name").contains("OS X") ? KeyPromoterBundle.message("kp.meta.osx") : KeyPromoterBundle.message("kp.meta.default");
    // Fields with actions of supported classes
    private static final Map<Class, Field> myClassFields = new HashMap<>(5);
    private ActionSource mySource = ActionSource.INVALID;
    private int myMnemonic = 0;
    private String myShortcut = "";
    private String myDescription = "";
    private String myIdeaActionID = "";

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
     * Information extraction for buttons on the toolbar
     * @param source source of the action
     */
    private void analyzeActionButton(ActionButton source) {
        final AnAction action = source.getAction();
        if (action != null) {
            fixValuesFromAction(action);
        }
        mySource = ActionSource.TOOLBAR_BUTTON;
    }
    /**
     * Information extraction for entries in the menu
     * @param source source of the action
     */
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

    /**
     * Information extraction for buttons of tool-windows
     * @param source source of the action
     */
    private void analyzeStripeButton(StripeButton source) {
        mySource = ActionSource.TOOL_WINDOW_BUTTON;
        myDescription = source.getText();
        myMnemonic = source.getMnemonic2();
        if (myMnemonic > 0) {
            myDescription = myDescription.replaceFirst("\\d: ","");
        }
        // This is hack, but for IDEA stripe buttons it doesn't seem possible to extract the IdeaActionID.
        // We turn e.g. "9: Version Control" to "ActivateVersionControlToolWindow" which seems to work for all tool windows
        // in a similar way.
        myIdeaActionID = KeyPromoterBundle.message("kp.stripe.actionID", StringUtils.replace(myDescription, " ", ""));
    }

    /**
     * Information extraction for all other buttons
     * TODO: This needs to be tested. I couldn't find a button that wasn't inspected with this fallback.
     * @param source source of the action
     */
    private void analyzeJButton(JButton source) {
        mySource = ActionSource.OTHER;
        myMnemonic = source.getMnemonic();
        myDescription = source.getText();
    }

    /**
     * Extracts a private field from a class so that we can access it for getting information
     * @param source Object that contains the field we are interested in
     * @param target Class of the field we try to extract
     * @return The field that was found
     */
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

    /**
     * This method can be used at several places to update shortcut, description and ideaAction from an {@link AnAction}
     * @param anAction action to extract values from
     */
    private void fixValuesFromAction(AnAction anAction) {
        myShortcut = anAction.getShortcutSet() != null ? KeyPromoterUtils.getKeyboardShortcutsText(anAction) : "";
        myDescription = anAction.getTemplatePresentation().getText();
        myIdeaActionID = ActionManager.getInstance().getId(anAction);
    }

    ActionSource getSource() {
        return mySource;
    }

    public String getShortcut() {
        if (myMnemonic > 0) {
            myShortcut = "\'" + metaKey + (char) myMnemonic + "\'";
        }
        return myShortcut;
    }

    public String getDescription() {
        return myDescription;
    }

    public String getIdeaActionID() {
        return myIdeaActionID;
    }

    enum ActionSource {
        TOOLBAR_BUTTON,
        TOOL_WINDOW_BUTTON,
        MENU_ENTRY,
        OTHER,
        INVALID
    }
}
