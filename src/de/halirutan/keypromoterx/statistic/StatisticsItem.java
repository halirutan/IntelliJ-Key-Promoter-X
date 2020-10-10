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

import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import de.halirutan.keypromoterx.KeyPromoterAction;
import org.jetbrains.annotations.NotNull;

/**
 * The core class that stores information about an action and is used to count how often the shortcut was missed.
 *
 * @author Patrick Scheibe
 */
@SuppressWarnings("WeakerAccess")
@Tag("Item")
public class StatisticsItem implements Comparable<StatisticsItem> {
    @Tag("IdeaActionID")
    public final String ideaActionID;
    @Tag("ShortCut")
    public final String shortCut;
    @Tag("Description")
    public final String description;
    @Tag("Count")
    public int count;
    @Tag("Hit")
    public int hits;

    /**
     * We need a default constructor to make it work with the persistent state framework.
     * {@see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html}
     */
    public StatisticsItem() {
        ideaActionID = "";
        shortCut = "";
        description = "";
        count = 0;
        hits = 0;
    }

    public StatisticsItem(KeyPromoterAction myAction) {
        description = myAction.getDescription();
        shortCut = myAction.getShortcut();
        ideaActionID = myAction.getIdeaActionID();
        count = 0;
        hits = 0;
    }

    @Transient
    public void registerEvent() {
        count++;
    }

    @Transient
    public void registerShortcutUsed() {
        hits++;
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

    public String getDescription() {
        return description;
    }
}
