package de.halirutan.keypromoterx;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;

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


    private final Map<String, Integer> withoutShortcutStats = Collections.synchronizedMap(new HashMap<String, Integer>());
    private final KeyPromoterStatistics statsService = ServiceManager.getService(KeyPromoterStatistics.class);
    private final KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);
    // Presentation and stats fields.

    public void initComponent() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK/* | AWTEvent.KEY_EVENT_MASK*/);
    }

    public void disposeComponent() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    @NotNull
    public String getComponentName() {
        return KeyPromoterBundle.message("component.name");
    }

    // AWT magic
    public void eventDispatched(AWTEvent e) {
        if (e.getID() == MouseEvent.MOUSE_RELEASED && ((MouseEvent) e).getButton() == MouseEvent.BUTTON1) {
            handleMouseEvent(e);
        }
    }


    private void handleMouseEvent(AWTEvent e) {
        KeyPromoterAction action = new KeyPromoterAction(e);
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
            statsService.registerAction(action);
            KeyPromoterNotification.showTip(action, statsService.get(action).getCount());

        } else {
            final String ideaActionID = action.getIdeaActionID();
            if (StringUtil.isEmpty(ideaActionID)) {
                return;
            }
            withoutShortcutStats.putIfAbsent(ideaActionID, 0);
            withoutShortcutStats.put(ideaActionID, withoutShortcutStats.get(ideaActionID) + 1);
            if (keyPromoterSettings.getProposeToCreateShortcutCount() > 0 && withoutShortcutStats.get(ideaActionID) % keyPromoterSettings.getProposeToCreateShortcutCount() == 0) {
                KeyPromoterNotification.askToCreateShortcut(action);

            }
        }
    }

}
