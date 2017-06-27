package de.halirutan.keypromoterx.statistic;

import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import de.halirutan.keypromoterx.KeyPromoterAction;

/**
 * The core class that stores information about an action and is used to count how often the shortcut was missed.
 * @author Patrick Scheibe
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

    /**
     * We need a default constructor to make it work with the persistent state framework.
     * {@see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html}
     */
    @SuppressWarnings("unused")
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

    /**
     * Comparator that is used to sort the entries by how often a shortcut was missed
     * @param o other item
     * @return result of the comparison
     */
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
