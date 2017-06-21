package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by athiele on 09.01.2015.
 */
@State(
        name = "KeyPromoterStats",
        storages = {
                @Storage(
                        file = "$APP_CONFIG$/KeyPromoterStats.xml",
                        id = "KeyPromoterStats"
                )}
)
public class KeyPromoterPersistentStats implements PersistentStateComponent<KeyPromoterPersistentStats> {

  private Map<String, Integer> myStats = Collections.synchronizedMap(new HashMap<String, Integer>());

  public Map<String, Integer> getStats() {
    return myStats;
  }

  public void setStats(Map<String, Integer> myStats) {
    this.myStats = myStats;
  }

  @Nullable
  @Override
  public KeyPromoterPersistentStats getState() {
    return this;
  }

  @Override
  public void loadState(KeyPromoterPersistentStats keyPromoterPersistentStats) {
    XmlSerializerUtil.copyBean(keyPromoterPersistentStats, this);
  }

}
