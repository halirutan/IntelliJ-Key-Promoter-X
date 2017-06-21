package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import java.util.*;

/**
 * Created by athiele on 10.01.2015.
 *
 */
public class KeyPromoterToolWindowBuilder {

    private KeyPromoterPersistentStats statsService = ServiceManager.getService(KeyPromoterPersistentStats
            .class);

    private JButton refreshButton;
    private JPanel panel;
    private JList<String> list1;
    private JButton resetStatisticsButton;
    private String[] topTen = new String[10];

    KeyPromoterToolWindowBuilder() {
        refreshButton.addActionListener(e -> updateTopTen());
        resetStatisticsButton.addActionListener(e -> resetStats());
    }

    JPanel createToolWindowPanel() {
        updateTopTen();
        return panel;
    }

    private void resetStats() {
        if (Messages.showYesNoDialog("Do you really want to reset your Key Promoter statistics? This cannot be undone!", "Reset Statistics", Messages.getQuestionIcon()) == 0) {
            Map<String, Integer> stats = statsService.getStats();
            stats.clear();
            statsService.setStats(stats);
            updateTopTen();
        }
    }


    private void updateTopTen() {
        Map<String, Integer> stats = statsService.getStats();

        if (!stats.isEmpty()) {
            List<Map.Entry<String, Integer>> sortedEntries = sortByValues(stats);

            int i = 0;
            for (Map.Entry entry : sortedEntries) {
                topTen[i] = "[" + (i + 1) + "] " + entry.getValue() + " times: " + entry.getKey();
                i++;
                if (i == 10) break;
            }
        } else {
            topTen = new String[10];
        }
        list1.setListData(topTen);
    }

    // Sort function
    private static <K, V extends Comparable<? super V>>
    List<Map.Entry<K, V>> sortByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries,
                (e1, e2) -> e2.getValue().compareTo(e1.getValue())
        );

        return sortedEntries;
    }

}
