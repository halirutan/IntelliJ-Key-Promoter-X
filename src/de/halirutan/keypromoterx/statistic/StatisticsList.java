package de.halirutan.keypromoterx.statistic;

import com.intellij.ui.components.JBList;
import de.halirutan.keypromoterx.KeyPromoterBundle;
import de.halirutan.keypromoterx.KeyPromoterToolWindowBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author patrick (25.06.17).
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("stats")) {
            myModel.updateStats();
        }
        updateUI();
    }



    static class StatisticsItemCellRenderer extends JLabel implements ListCellRenderer<StatisticsItem> {


        @Override
        public JLabel getListCellRendererComponent(JList<? extends StatisticsItem> list, StatisticsItem value, int index, boolean isSelected, boolean cellHasFocus) {
            final String message = KeyPromoterBundle.message("kp.list.item", value.count, value.getShortcut(), value.description);
            setText(message);
            return this;
        }
    }



}
