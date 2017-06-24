package org.jetbrains.contest.keypromoterx;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.IconUtil;

import java.text.MessageFormat;

/**
 * @author patrick (22.06.17).
 */
class KeyPromoterNotification {

    private static final NotificationGroup GROUP = new NotificationGroup(
            "Key Promoter",
            NotificationDisplayType.BALLOON,
            false,
            "Key Promoter",
            IconUtil.getEditIcon()
            );

    static void showTip(String description, String shortcut, int count) {
        String template = "{0}<br>(pressed {1} time(s))";
        final String message = MessageFormat.format(template, shortcut, count);
        GROUP.createNotification("Key Promoter", StringUtil.wrapWithDoubleQuote(description), message, NotificationType.INFORMATION).notify(null);
    }

}
