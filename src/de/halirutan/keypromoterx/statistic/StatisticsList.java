package de.halirutan.keypromoterx.statistic;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.ui.components.JBList;
import de.halirutan.keypromoterx.KeyPromoterBundle;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Provides a custom JBList for displaying how often a button was pressed that could have been replaced by a shortcut.
 * The list is backed by {@link StatisticsListModel} that allows for automatic updating of the values and synchronization
 * with {@link KeyPromoterStatistics} that keeps the values persistent through restarts.
 * @author Patrick Scheibe
 */
public class StatisticsList extends JBList<StatisticsItem> implements PropertyChangeListener {
    private StatisticsListModel myModel;
    public StatisticsList(@NotNull StatisticsListModel dataModel) {
        myModel = dataModel;
        setModel(myModel);
        myModel.getPropertyChangeSupport().addPropertyChangeListener(this);
        setCellRenderer(new StatisticsItemCellRenderer());
        myModel.updateStats();
    }

    @Override
    public ListModel<StatisticsItem> getModel() {
        return myModel;
    }

    /**
     * Catches events to keep the Key Promoter tool-window up-to-date with the underlying statistic that is updated
     * each time a shortcut was missed.
     * @param evt The event indicating a change in the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("stats")) {
            myModel.updateStats();
        }
        updateUI();
    }


    /**
     * Provides custom rendering of items in the Key Promoter X statistic tool-window.
     */
    static class StatisticsItemCellRenderer extends JLabel implements ListCellRenderer<StatisticsItem> {

        @Override
        public JLabel getListCellRendererComponent(JList<? extends StatisticsItem> list, StatisticsItem value, int index, boolean isSelected, boolean cellHasFocus) {
            final Color foreground = list.getForeground();
            final String message = KeyPromoterBundle.message(
                    "kp.list.item",
                    value.getShortcut(),
                    value.description,
                    value.count,
                    value.count == 1 ? "time" : "times"
                    );
            setText(message);
            setForeground(foreground);
            setBorder(new EmptyBorder(2,10,2,10));
            final AnAction action = ActionManager.getInstance().getAction(value.ideaActionID);
            if (action != null) {
                final Icon icon = action.getTemplatePresentation().getIcon();
                if (icon != null) {
                    setIcon(icon);
                }
            }
            return this;
        }
    }



}
