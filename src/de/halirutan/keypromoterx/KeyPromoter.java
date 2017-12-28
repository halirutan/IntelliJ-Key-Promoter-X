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

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.impl.StripeButton;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
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
public class KeyPromoter implements ApplicationComponent, AWTEventListener, AnActionListener {

    private static final Logger LOG = Logger.getInstance("#de.halirutan.keypromoterx.KeyPromoter");

    private final Map<String, Integer> withoutShortcutStats = Collections.synchronizedMap(new HashMap<String, Integer>());
    private final KeyPromoterStatistics statsService = ServiceManager.getService(KeyPromoterStatistics.class);
    private final KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);
    // Presentation and stats fields.

    private static volatile boolean wasMouseClick = false;

    @Override
    public void initComponent() {
        ActionManager.getInstance().addAnActionListener(this);
        long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
    }

    @Override
    public void disposeComponent() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    @Override
    @NotNull
    public String getComponentName() {
        return KeyPromoterBundle.message("component.name");
    }

    /**
     * Catches all UI events from the main IDEA AWT making it possible to inspect all mouse-clicks.
     * Note that on OSX this will not catch clicks on the (detached) menu bar.
     *
     * @param e event that is caught
     */
    @Override
    public void eventDispatched(AWTEvent e) {
        if (e.getID() == MouseEvent.MOUSE_RELEASED && ((MouseEvent) e).getButton() == MouseEvent.BUTTON1) {
            LOG.info("Found mouse event and trying to handle it");
            handleMouseEvent(e);
        }
    }


    /**
     * Transfers the event to {@link KeyPromoterAction} and inspects the results. Then, depending on the result and the
     * Key Promoter X settings, a balloon is shown with the shortcut tip and the statistic is updated.
     *
     * @param e event that is handled
     */
    private void handleMouseEvent(AWTEvent e) {
        wasMouseClick = true;
        if (e.getSource() instanceof StripeButton && keyPromoterSettings.isToolWindowButtonsEnabled()) {
            LOG.info("Mouse click was a Stripe button for tool windows");
            KeyPromoterAction action = new KeyPromoterAction(e);
            showTip(action);
        }
    }

    @Override
    public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        final InputEvent input = event.getInputEvent();
        if (input instanceof MouseEvent && wasMouseClick) {
            final String place = event.getPlace();
            KeyPromoterAction kpAction;
            if ("MainMenu".equals(place)) {
                LOG.info("Main menu entry was pressed");
                if (keyPromoterSettings.isMenusEnabled()) {
                    kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.MENU_ENTRY);
                    showTip(kpAction);
                }
            } else if ("MainToolbar".equals(place)) {
                LOG.info("Main toolbar entry was pressed");
                if (keyPromoterSettings.isToolbarButtonsEnabled()) {
                    kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.MAIN_TOOLBAR);
                    showTip(kpAction);
                }
            } else if (place.matches(".*Popup")) {
                LOG.info("Popup entry was pressed");
                if (keyPromoterSettings.isEditorPopupEnabled()) {
                    kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.POPUP);
                    showTip(kpAction);
                }
            } else if (keyPromoterSettings.isAllButtonsEnabled()) {
                LOG.info("Unknown button was pressed. Trying to handle it");
                kpAction = new KeyPromoterAction(action, event, KeyPromoterAction.ActionSource.OTHER);
                showTip(kpAction);
            }
        }
        wasMouseClick = false;
    }

    private void showTip(KeyPromoterAction action) {
        if (action == null || !action.isValid() || statsService.isSuppressed(action)) {
            LOG.info("Button-action is invalid or null. Cannot show anything");
            return;
        }

        final String shortcut = action.getShortcut();
        if (!StringUtil.isEmpty(shortcut)) {
            statsService.registerAction(action);
            LOG.info("Found ShortCut for action: " + shortcut);
            KeyPromoterNotification.showTip(action, statsService.get(action).getCount());

        } else {
            LOG.info(
                "No ShortCut available. Registering action to ask the user after several clicks if he wants to create one.");
            final String ideaActionID = action.getIdeaActionID();
            withoutShortcutStats.putIfAbsent(ideaActionID, 0);
            withoutShortcutStats.put(ideaActionID, withoutShortcutStats.get(ideaActionID) + 1);
            if (keyPromoterSettings.getProposeToCreateShortcutCount() > 0 && withoutShortcutStats.get(ideaActionID) % keyPromoterSettings.getProposeToCreateShortcutCount() == 0) {
                KeyPromoterNotification.askToCreateShortcut(action);

            }
        }
    }

    @Override
    public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {

    }

    @Override
    public void beforeEditorTyping(char c, DataContext dataContext) {

    }
}
