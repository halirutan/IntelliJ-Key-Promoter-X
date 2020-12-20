/*
 * Copyright (c) 2017 Patrick Scheibe, Dmitry Kashin, Athiele.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.halirutan.keypromoterx.statistic;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import com.intellij.util.xmlb.annotations.Transient;
import de.halirutan.keypromoterx.KeyPromoterAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
 *
 * @author Patrick Scheibe
 */
@State(
    name = "KeyPromoterXStatistic",
    storages = {
        @Storage(
            value = "KeyPromoterXStatistic.xml",
            roamingType = RoamingType.DISABLED
        )
    }
)
public class KeyPromoterStatistics implements PersistentStateComponent<KeyPromoterStatistics> {

  @Transient
  static final String STATISTIC = "add";
  @Transient
  static final String SUPPRESS = "suppress";

  @MapAnnotation(surroundKeyWithTag = false, surroundValueWithTag = false, surroundWithTag = false, entryTagName = "Statistic", keyAttributeName = "Action")
  private final Map<String, StatisticsItem> statistics = Collections.synchronizedMap(new HashMap<>());

  @MapAnnotation(surroundKeyWithTag = false, surroundValueWithTag = false, surroundWithTag = false, entryTagName = "Statistic", keyAttributeName = "Action")
  private final Map<String, StatisticsItem> suppressed = Collections.synchronizedMap(new HashMap<>());

  @Transient
  private final PropertyChangeSupport myChangeSupport = new PropertyChangeSupport(this);

  @Nullable
  @Override
  public KeyPromoterStatistics getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull KeyPromoterStatistics stats) {
    XmlSerializerUtil.copyBean(stats, this);
  }


  @Transient
  void registerPropertyChangeSupport(PropertyChangeListener listener) {
    myChangeSupport.addPropertyChangeListener(listener);
  }

  @Transient
  public void registerAction(KeyPromoterAction action) {
    synchronized (statistics) {
      statistics.putIfAbsent(action.getDescription(), new StatisticsItem(action));
      statistics.get(action.getDescription()).registerEvent();
    }
    myChangeSupport.firePropertyChange(STATISTIC, null, null);
  }

  @Transient
  public void registerShortcutUsed(KeyPromoterAction action) {
    synchronized (statistics) {
      if (statistics.containsKey(action.getDescription())) {
        statistics.get(action.getDescription()).registerShortcutUsed();
        myChangeSupport.firePropertyChange(STATISTIC, null, null);
      }
    }
  }

  @Transient
  public void resetStatistic() {
    synchronized (statistics) {
      statistics.clear();
    }
    synchronized (suppressed) {
      suppressed.clear();
    }
    myChangeSupport.firePropertyChange(STATISTIC, null, null);
  }

  @Transient
  public void suppressItem(KeyPromoterAction action) {
    StatisticsItem removed;
    synchronized (statistics) {
      removed = statistics.remove(action.getDescription());
      removed = removed == null ? new StatisticsItem(action) : removed;
    }
    synchronized (suppressed) {
      suppressed.putIfAbsent(action.getDescription(), removed);
    }
    myChangeSupport.firePropertyChange(SUPPRESS, null, null);
    myChangeSupport.firePropertyChange(STATISTIC, null, null);
  }

  @Transient
  public StatisticsItem get(KeyPromoterAction action) {
    return statistics.get(action.getDescription());
  }

  @Transient
  public ArrayList<StatisticsItem> getStatisticItems() {
    final ArrayList<StatisticsItem> items = new ArrayList<>(statistics.values());
    Collections.sort(items);
    return items;
  }

  @Transient
  ArrayList<StatisticsItem> getSuppressedItems() {
    return new ArrayList<>(suppressed.values());
  }

  @Transient
  public boolean isSuppressed(KeyPromoterAction action) {
    return suppressed.containsKey(action.getDescription());
  }

  /**
   * Puts an item from the suppress list back into the statistics.
   *
   * @param item Item to unsuppress
   */
  @Transient
  void unsuppressItem(StatisticsItem item) {
    final StatisticsItem statisticsItem = suppressed.remove(item.getDescription());
    if (statisticsItem != null && statisticsItem.count > 0) {
      statistics.putIfAbsent(statisticsItem.getDescription(), statisticsItem);
    }
    myChangeSupport.firePropertyChange(SUPPRESS, null, null);
    myChangeSupport.firePropertyChange(STATISTIC, null, null);
  }
}
