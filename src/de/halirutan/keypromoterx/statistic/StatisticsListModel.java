package de.halirutan.keypromoterx.statistic;

import com.intellij.openapi.components.ServiceManager;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.SwingPropertyChangeSupport;
import java.util.ArrayList;

/**
 * Provides the underlying model for the JBList that is displayed in the Key Promoter X tool window.
 * This model is synchronized with the underlying persistent state data that stores all information.
 *
 * @author Patrick Scheibe
 */
public class StatisticsListModel implements ListModel<StatisticsItem> {

    private SwingPropertyChangeSupport propertyChangeSupport;
    private ArrayList<StatisticsItem> myData = new ArrayList<>();
    private KeyPromoterStatistics myStats = ServiceManager.getService(KeyPromoterStatistics.class);

    public StatisticsListModel() {
        propertyChangeSupport = new SwingPropertyChangeSupport(this);
        myStats.registerPropertyChangeSupport(propertyChangeSupport);
        updateStats();
    }

    void updateStats() {
        myData.clear();
        myData.addAll(myStats.getStatisticItems());
    }

    public SwingPropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @Override
    public int getSize() {
        return myData.size();
    }

    @Override
    public StatisticsItem getElementAt(int index) {
        return myData.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) { }

    @Override
    public void removeListDataListener(ListDataListener l) { }

}
