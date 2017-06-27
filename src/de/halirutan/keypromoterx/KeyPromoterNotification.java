package de.halirutan.keypromoterx;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;
import de.halirutan.KeyPromoterIcons;

/**
 * A custom notification class that allows for creating 1. tips if a short cut was missed and 2. a balloon asking if
 * the user wants to create a shortcut for an action that doesn't have one.
 * @author Patrick Scheibe.
 */
class KeyPromoterNotification {

    private static final NotificationGroup GROUP = new NotificationGroup(
            KeyPromoterBundle.message("kp.notification.group"),
            NotificationDisplayType.BALLOON,
            false,
            KeyPromoterBundle.message("kp.tool.window.name"),
            KeyPromoterIcons.KP_ICON
            );

    static void showTip(KeyPromoterAction action, int count) {
        String message = KeyPromoterBundle.message("kp.notification.tip", action.getDescription(), count);
        GROUP.createNotification(KeyPromoterBundle.message(
                "kp.notification.group"),
                message,
                NotificationType.INFORMATION,null).setIcon(KeyPromoterIcons.KP_ICON)
                .addAction(new EditKeymapAction(action, action.getShortcut()))
                .notify(null);
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
