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
public class SuppressedListModel implements ListModel<StatisticsItem> {

    private final SwingPropertyChangeSupport propertyChangeSupport;
    private final ArrayList<StatisticsItem> myData = new ArrayList<>();
    private final KeyPromoterStatistics myStats = ServiceManager.getService(KeyPromoterStatistics.class);

    public SuppressedListModel() {
        propertyChangeSupport = new SwingPropertyChangeSupport(this);
        myStats.registerPropertyChangeSupport(propertyChangeSupport);
        updateSuppressed();
    }

    void updateSuppressed() {
        myData.clear();
        myData.addAll(myStats.getSuppressedItems());
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
