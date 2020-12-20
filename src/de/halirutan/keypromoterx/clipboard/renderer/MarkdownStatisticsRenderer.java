package de.halirutan.keypromoterx.clipboard.renderer;

import de.halirutan.keypromoterx.statistic.StatisticsItem;

import java.util.ArrayList;
import java.util.Comparator;

public class MarkdownStatisticsRenderer implements StatisticsRenderer {
    @Override
    public String renderStatistics(ArrayList<StatisticsItem> statistics) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("# Key Promoter X Shortcuts\n\n");
        stringBuilder.append("|Description|Shortcut|\n|---|:---|\n");

        statistics.stream().sorted(Comparator.comparing(StatisticsItem::getCount).reversed()).forEach(
                item -> {
                    stringBuilder.append(item.description);
                    stringBuilder.append("|");
                    stringBuilder.append(item.shortCut);
                    stringBuilder.append("\n");
                }
        );
        return stringBuilder.toString();
    }
}
