package de.halirutan.keypromoterx.clipboard.renderer;

import de.halirutan.keypromoterx.statistic.StatisticsItem;

import java.util.ArrayList;

public interface StatisticsRenderer {
    String renderStatistics(ArrayList<StatisticsItem> statistics);
}
