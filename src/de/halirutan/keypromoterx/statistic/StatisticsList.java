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

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;
import de.halirutan.keypromoterx.KeyPromoterBundle;
import de.halirutan.keypromoterx.KeyPromoterIcons;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Provides a custom JBList for displaying how often a button was pressed that could have been replaced by a shortcut.
 * The list is backed by {@link KeyPromoterStatistics} that keeps the values persistent through restarts.
 *
 * @author Patrick Scheibe
 */
public class StatisticsList extends JBList<StatisticsItem> implements PropertyChangeListener {
    private final DefaultListModel<StatisticsItem> myModel;
    private final KeyPromoterStatistics myStats = ServiceManager.getService(KeyPromoterStatistics.class);

    public StatisticsList() {
        myModel = new DefaultListModel<>();
        setModel(myModel);
        myStats.registerPropertyChangeSupport(this);
        setCellRenderer(new StatisticsItemCellRenderer());
        updateStats();
    }

    @Override
    public ListModel<StatisticsItem> getModel() {
        return myModel;
    }

    /**
     * Catches events to keep the Key Promoter tool-window up-to-date with the underlying statistic that is updated
     * each time a shortcut was missed.
     *
     * @param evt The event indicating a change in the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(KeyPromoterStatistics.STATISTIC)) {
            updateStats();
        }
    }

    private void updateStats() {
        myModel.removeAllElements();
        for (StatisticsItem statisticsItem : myStats.getStatisticItems()) {
            myModel.addElement(statisticsItem);
        }
    }


    /**
     * Provides custom rendering of items in the Key Promoter X statistic tool-window.
     */
    static class StatisticsItemCellRenderer extends JLabel implements ListCellRenderer<StatisticsItem> {

        @Override
        public JLabel getListCellRendererComponent(JList<? extends StatisticsItem> list, StatisticsItem value, int index, boolean isSelected, boolean cellHasFocus) {
            final Color foreground = list.getForeground();
            final String message = KeyPromoterBundle.message(
                    "kp.list.item",
                    value.getShortcut(),
                    value.description,
                    value.count,
                    value.hits
            );
            setText(message);
            setIcon(KeyPromoterIcons.KP_ICON);
            setForeground(foreground);
            setBorder(JBUI.Borders.empty(2, 10));
            if (value.ideaActionID != null && !"".equals(value.ideaActionID)) {
                final AnAction action = ActionManager.getInstance().getAction(value.ideaActionID);
                if (action != null) {
                    final Icon icon = action.getTemplatePresentation().getIcon();
                    if (icon != null) {
                        setIcon(icon);
                    }
                }
            }
            return this;
        }
    }


}
