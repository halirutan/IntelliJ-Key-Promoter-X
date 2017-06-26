package de.halirutan.keypromoterx.statistic;

import com.intellij.openapi.components.ServiceManager;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.SwingPropertyChangeSupport;
import java.util.ArrayList;

public class StatisticsListModel implements ListModel<StatisticsItem> {

    private SwingPropertyChangeSupport propertyChangeSupport;
    private ArrayList<StatisticsItem> myData = new ArrayList<>();
    private KeyPromoterStatistics myStats = ServiceManager.getService(KeyPromoterStatistics.class);

    public StatisticsListModel() {
        propertyChangeSupport = new SwingPropertyChangeSupport(this);
        myStats.registerProperteryChangeSupport(propertyChangeSupport);
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
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

}
