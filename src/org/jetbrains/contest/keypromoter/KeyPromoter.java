package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 04.09.2006
 * Time: 14:11:03
 */
public class KeyPromoter implements ApplicationComponent, AWTEventListener {


    // Presentation and stats fields.
    private Map<String, Integer> stats = Collections.synchronizedMap(new HashMap<String, Integer>());
    private final Map<String, Integer> withoutShortcutStats = Collections.synchronizedMap(new HashMap<String, Integer>());

    private final KeyPromoterPersistentStats statsService = ServiceManager.getService(KeyPromoterPersistentStats
            .class);

    private final KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);

    public void initComponent() {
        stats = statsService.getStats();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK/* | AWTEvent.KEY_EVENT_MASK*/);
    }

    public void disposeComponent() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    @NotNull
    public String getComponentName() {
        return "KeyPromoter";
    }

    // AWT magic
    public void eventDispatched(AWTEvent e) {
        if (e.getID() == MouseEvent.MOUSE_RELEASED && ((MouseEvent) e).getButton() == MouseEvent.BUTTON1) {
            handleMouseEvent(e);
        }
    }


    private void handleMouseEvent(AWTEvent e) {
        KeyPromoterActionAnalyzer action = new KeyPromoterActionAnalyzer(e);
        switch (action.getSource()) {
            case TOOLWINDOW_BUTTON:
                if (!keyPromoterSettings.isToolWindowButtonsEnabled()) {
                    return;
                }
                break;
            case TOOLBAR_BUTTON:
                if (!keyPromoterSettings.isToolbarButtonsEnabled()) {
                    return;
                }
                break;
            case MENU_ENTRY:
                if (!keyPromoterSettings.isMenusEnabled()) {
                    return;
                }
                break;
            case OTHER:
                if (!keyPromoterSettings.isAllButtonsEnabled()) {
                    return;
                }
            case INVALID:
                return;
        }

        final String shortcut = action.getShortcut();
        final String description = action.getDescription();
        if (!StringUtil.isEmpty(shortcut)) {
            final String statText = shortcut + " for " + description;
            stats.putIfAbsent(statText, 0);
            stats.put(statText, stats.get(statText) + 1);
            KeyPromoterNotification.showTip(description, shortcut, stats.get(statText));
        } else {
            final String ideaActionID = action.getIdeaActionID();
            if (StringUtil.isEmpty(ideaActionID)) {
                return;
            }
            withoutShortcutStats.putIfAbsent(ideaActionID, 0);
            withoutShortcutStats.put(ideaActionID, withoutShortcutStats.get(ideaActionID) + 1);
            if (keyPromoterSettings.getProposeToCreateShortcutCount() > 0 && withoutShortcutStats.get(ideaActionID) % keyPromoterSettings.getProposeToCreateShortcutCount() == 0) {
                if (Messages.showYesNoDialog("Would you like to assign shortcut to '" + description + "' action cause we noticed it was used " + withoutShortcutStats.get(ideaActionID) + " time(s) by mouse?",
                        "[KeyPromoter Said]: Keyboard Usage More Productive!", Messages.getQuestionIcon()) == 0) {
                    EditKeymapsDialog dialog = new EditKeymapsDialog(null, ideaActionID);
                    dialog.show();
                }

            }
        }
    }
}
