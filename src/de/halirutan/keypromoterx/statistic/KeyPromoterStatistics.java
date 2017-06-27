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
 * Provides storing the statistics persistently. Note that the only thing we store persistently is the list of
 * {@link StatisticsItem}. All other functionality that is used e.g. to communicate changes in the statistic to the UI
 * or methods to register new button-presses are marked as @Transient meaning they are ignored by the persistent state
 * framework.
 * </br>
 * The @MapAnnotation defines how the statistics map is laid out as xml file on disk. This is pure cosmetics in our case.
 * </br>
 * Furthermore, since we use {@link StatisticsItem} as underlying data-structure, it is very easy to add further features.
 * One could for instance include the times when buttons are pressed to create a graph that shows if the user really
 * progresses in replacing mouse actions with shortcuts.
 * @author Patrick Scheibe
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
    void registerPropertyChangeSupport(PropertyChangeSupport support) {
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
