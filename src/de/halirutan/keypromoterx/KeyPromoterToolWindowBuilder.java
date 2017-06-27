package de.halirutan.keypromoterx;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;
import de.halirutan.keypromoterx.statistic.StatisticsList;
import de.halirutan.keypromoterx.statistic.StatisticsListModel;

import javax.swing.*;

/**
 * Controlling class of the tool-window
 *
 * @author athiele, Patrick Scheibe
 *
 */
public class KeyPromoterToolWindowBuilder {

    private JPanel panel;
    private StatisticsList statisticsList;
    private JButton resetStatisticsButton;
    private KeyPromoterStatistics statService = ServiceManager.getService(KeyPromoterStatistics.class);


    KeyPromoterToolWindowBuilder() {
        resetStatisticsButton.addActionListener(e -> resetStats());
    }

    JPanel createToolWindowPanel() {
        return panel;
    }

    private void resetStats() {
        if (Messages.showYesNoDialog("Do you really want to reset your Key Promoter statistics? This cannot be undone!", "Reset Statistics", Messages.getQuestionIcon()) == 0) {
            statService.resetStatistic();
        }
    }

    private void createUIComponents() {
        StatisticsListModel model = new StatisticsListModel();
        statisticsList = new StatisticsList(model);
        model.getPropertyChangeSupport().addPropertyChangeListener(statisticsList);
    }

}
