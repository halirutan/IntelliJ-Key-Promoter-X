package de.halirutan.keypromoterx;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;
import com.intellij.util.IconUtil;

/**
 * @author patrick (22.06.17).
 */
class KeyPromoterNotification {

    private static final NotificationGroup GROUP = new NotificationGroup(
            KeyPromoterBundle.message("kp.notification.group"),
            NotificationDisplayType.BALLOON,
            false,
            KeyPromoterBundle.message("kp.tool.window.name"),
            IconUtil.getEditIcon()
            );

    static void showTip(KeyPromoterAction action, int count) {
        String message = KeyPromoterBundle.message("kp.notification.tip", action.getDescription(), count);
        GROUP.createNotification(KeyPromoterBundle.message(
                "kp.notification.group"),
                message,
                NotificationType.INFORMATION,null)
                .addAction(new EditKeymapAction(action, action.getShortcut()))
                .notify(null);

//                StringUtil.wrapWithDoubleQuote(description), message, NotificationType.INFORMATION).notify(null);
    }

    static void askToCreateShortcut(KeyPromoterAction action) {
        GROUP.createNotification(
                KeyPromoterBundle.message("kp.notification.group"),
                KeyPromoterBundle.message("kp.notification.ask.new.shortcut", action.getDescription()),
                NotificationType.INFORMATION,
                null
        ).addAction(new EditKeymapAction(action)).notify(null);

    }


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
