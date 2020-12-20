package de.halirutan.keypromoterx.clipboard.renderer;

import de.halirutan.keypromoterx.statistic.StatisticsItem;

import java.util.ArrayList;
import java.util.Comparator;

public class TextStatisticsRenderer implements StatisticsRenderer {

    private static final int DEFAULT_INDENTION = 25;

    @Override
    public String renderStatistics(ArrayList<StatisticsItem> statistics) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Key Promoter X Shortcuts\n");
        stringBuilder.append("==========================================\n\n");
        stringBuilder.append("Description              Shortcut\n");
        stringBuilder.append("------------------------------------------\n\n");

        statistics.stream().sorted(Comparator.comparing(StatisticsItem::getCount).reversed()).forEach(
                item -> {
                    stringBuilder.append(item.description);
                    stringBuilder.append(fillWithWhitespaces(item.description));
                    stringBuilder.append(item.shortCut);
                    stringBuilder.append("\n");
                }
        );
        return stringBuilder.toString();
    }

    private String fillWithWhitespaces(String text) {
        int missingWhitespacesCount = DEFAULT_INDENTION - text.length();
        StringBuilder builder = new StringBuilder(" ");
        for (int i=0; i<missingWhitespacesCount-1; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }
}
