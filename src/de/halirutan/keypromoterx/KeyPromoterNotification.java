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

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;

import java.util.LinkedList;

/**
 * A custom notification class that allows for creating 1. tips if a short cut was missed and 2. a balloon asking if
 * the user wants to create a shortcut for an action that doesn't have one.
 * @author Patrick Scheibe.
 */
class KeyPromoterNotification {

    private final static LinkedList<Notification> NOTIFICATION_LIST = new LinkedList<>();
    private static KeyPromoterSettings settings = ServiceManager.getService(KeyPromoterSettings.class);

    private static final NotificationGroup GROUP = new NotificationGroup(
            KeyPromoterBundle.message("kp.notification.group"),
            NotificationDisplayType.BALLOON,
            false,
            KeyPromoterBundle.message("kp.tool.window.name"),
            KeyPromoterIcons.KP_ICON
            );

    static void showTip(KeyPromoterAction action, int count) {
        String message = KeyPromoterBundle.message("kp.notification.tip", action.getDescription(), count);
        final int maxTips = settings.getMaxNumberOfTips();
        if (maxTips != 0 && NOTIFICATION_LIST.size() >= maxTips) {
            final Notification notification = NOTIFICATION_LIST.removeFirst();
            notification.expire();
        }
        final Notification notification = GROUP.createNotification(KeyPromoterBundle.message(
                "kp.notification.group"),
                message,
                NotificationType.INFORMATION, null).setIcon(KeyPromoterIcons.KP_ICON)
                .addAction(new EditKeymapAction(action, action.getShortcut()));
        NOTIFICATION_LIST.addLast(notification);
        notification.notify(null);
    }

    static void askToCreateShortcut(KeyPromoterAction action) {
        GROUP.createNotification(
                KeyPromoterBundle.message("kp.notification.group"),
                KeyPromoterBundle.message("kp.notification.ask.new.shortcut", action.getDescription()),
                NotificationType.INFORMATION,
                null
        ).setIcon(KeyPromoterIcons.KP_ICON).addAction(new EditKeymapAction(action)).notify(null);

    }


    /**
     * Provides click-able links to IDEA actions. On click, the keymap editor is opened showing the exact line where
     * the shortcut of an action can be edited/created.
     */
    private static class EditKeymapAction extends AnAction {
        private KeyPromoterAction myAction;

        EditKeymapAction(KeyPromoterAction action) {
            super(action.getDescription());
            this.myAction = action;
        }

        EditKeymapAction(KeyPromoterAction action, String buttonText) {
            super(buttonText);
            this.myAction = action;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            EditKeymapsDialog dialog = new EditKeymapsDialog(null, myAction.getIdeaActionID());
            dialog.show();
        }
    }


}
