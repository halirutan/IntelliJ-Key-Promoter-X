package de.halirutan.keypromoterx.statistic;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.*;
import org.jetbrains.annotations.Nullable;
import de.halirutan.keypromoterx.KeyPromoterAction;

import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Provides storing the statistics persistently
 * @author patrick
 */
@State(
        name = "KeyPromoterStatistic",
        storages = {
                @Storage(
                        file = "$APP_CONFIG$/KeyPromoterStatistic.xml",
                        id = "KeyPromoterStatistic"
                )}
)
public class KeyPromoterStatistics implements PersistentStateComponent<KeyPromoterStatistics> {

    @MapAnnotation(surroundKeyWithTag = false, surroundValueWithTag = false, surroundWithTag = false, entryTagName = "Statistic", keyAttributeName = "Action")
    private Map<String , StatisticsItem> statistics = Collections.synchronizedMap(new HashMap<String, StatisticsItem>());

    @Transient
    private PropertyChangeSupport myChangeSupport;

    @Nullable
    @Override
    public KeyPromoterStatistics getState() {
        return this;
    }

    @Override
    public void loadState(KeyPromoterStatistics stats) {
        XmlSerializerUtil.copyBean(stats, this);
    }

    @Transient
    void registerProperteryChangeSupport(PropertyChangeSupport support) {
        myChangeSupport = support;
    }

    @Transient
    public void registerAction(KeyPromoterAction action) {
        statistics.putIfAbsent(action.getDescription(), new StatisticsItem(action));
        statistics.get(action.getDescription()).registerEvent();
        myChangeSupport.firePropertyChange("stats", null, null);
    }

    @Transient
    public void resetStatistic() {
        statistics.clear();
        myChangeSupport.firePropertyChange("stats", null, null);
    }

    @Transient
    public StatisticsItem get(KeyPromoterAction action) {
        return statistics.get(action.getDescription());
    }

    @Transient
    ArrayList<StatisticsItem> getStatisticItems() {
        final ArrayList<StatisticsItem> items = new ArrayList<>(statistics.values());
        Collections.sort(items);
        return items;
    }
}
