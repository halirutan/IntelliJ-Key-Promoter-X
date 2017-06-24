package org.jetbrains.contest.keypromoterx;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides storing the statistics persistently
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

    Map<String, Integer> getStats() {
        return myStats;
    }

    void setStats(Map<String, Integer> myStats) {
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
