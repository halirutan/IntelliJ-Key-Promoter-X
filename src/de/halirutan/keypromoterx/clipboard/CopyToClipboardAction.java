package de.halirutan.keypromoterx.clipboard;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import de.halirutan.keypromoterx.KeyPromoterBundle;
import de.halirutan.keypromoterx.clipboard.renderer.MarkdownStatisticsRenderer;
import de.halirutan.keypromoterx.clipboard.renderer.StatisticsRenderer;
import de.halirutan.keypromoterx.clipboard.renderer.TextStatisticsRenderer;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;
import de.halirutan.keypromoterx.statistic.StatisticsItem;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

public class CopyToClipboardAction {

    public static Clipboard getSystemClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public static void copyStatisticsToClipboard(boolean useMarkdownFormat) {
        final KeyPromoterStatistics statService = ServiceManager.getService(KeyPromoterStatistics.class);
        ArrayList<StatisticsItem> statistics = statService.getStatisticItems();

        StatisticsRenderer statisticsRenderer;
        if (useMarkdownFormat) {
            statisticsRenderer = new MarkdownStatisticsRenderer();
        } else {
            statisticsRenderer = new TextStatisticsRenderer();
        }
        String clipboardContent = statisticsRenderer.renderStatistics(statistics);

        Clipboard clipboard = getSystemClipboard();
        clipboard.setContents(new StringSelection(clipboardContent), null);
        Messages.showInfoMessage(
            KeyPromoterBundle.message("kp.dialog.copyclipboard.statistic.text"),
            KeyPromoterBundle.message("kp.dialog.copyclipboard.statistic.title")
        );
    }

}
