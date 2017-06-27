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
 * The main {@link ApplicationComponent} that is registered in plugin.xml. It will take care of catching UI events
 * and transfers the them to {@link KeyPromoterAction} for inspection. Depending on the type of action (tool-window button,
 * menu entry, etc.) a balloon is shown and the statistic is updated.
 *
 * @author Patrick Scheibe, Dmitry Kashin
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

    /**
     * Catches all UI events from the main IDEA AWT making it possible to inspect all mouse-clicks.
     * Note that on OSX this will not catch clicks on the (detached) menu bar.
     * @param e event that is caught
     */
    public void eventDispatched(AWTEvent e) {
        if (e.getID() == MouseEvent.MOUSE_RELEASED && ((MouseEvent) e).getButton() == MouseEvent.BUTTON1) {
            handleMouseEvent(e);
        }
    }


    /**
     * Transfers the event to {@link KeyPromoterAction} and inspects the results. Then, depending on the result and the
     * Key Promoter X settings, a balloon is shown with the shortcut tip and the statistic is updated.
     * @param e event that is handled
     */
    private void handleMouseEvent(AWTEvent e) {
        KeyPromoterAction action = new KeyPromoterAction(e);
        switch (action.getSource()) {
            case TOOL_WINDOW_BUTTON:
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
