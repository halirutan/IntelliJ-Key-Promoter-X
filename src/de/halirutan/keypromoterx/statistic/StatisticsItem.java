package de.halirutan.keypromoterx.statistic;

import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import de.halirutan.keypromoterx.KeyPromoterAction;

/**
 * @author patrick (25.06.17).
 */
@SuppressWarnings("WeakerAccess")
@Tag("Item")
public class StatisticsItem implements Comparable<StatisticsItem>{
    @Tag("IdeaActionID")
    public String ideaActionID;
    @Tag("ShortCut")
    public String shortCut;
    @Tag("Description")
    public String description;
    @Tag("Count")
    public int count;

    public StatisticsItem() {
        ideaActionID = "";
        shortCut = "";
        description = "";
        count = 0;
    }

    public StatisticsItem(KeyPromoterAction myAction) {
        description = myAction.getDescription();
        shortCut = myAction.getShortcut();
        ideaActionID = myAction.getIdeaActionID();
        count = 0;
    }

    @Transient
    public void registerEvent() {
        count++;
    }

    @Transient
    public int getCount() {
        return count;
    }

    @Transient
    @Override
    public int compareTo(@NotNull StatisticsItem o) {
        return o.count - count;
    }

    @Transient
    public String getShortcut() {
        return shortCut;
    }
}
