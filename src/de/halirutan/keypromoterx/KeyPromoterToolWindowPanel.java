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

package de.halirutan.keypromoterx;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import de.halirutan.keypromoterx.statistic.*;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Controlling class of the tool-window
 *
 * @author athiele, Patrick Scheibe
 *
 */
class KeyPromoterToolWindowPanel {

    private JPanel panel;
    private JList statisticsList;
    private JButton resetStatisticsButton;
    private JList suppressedList;
    private final KeyPromoterStatistics statService = ServiceManager.getService(KeyPromoterStatistics.class);


    KeyPromoterToolWindowPanel() {
        resetStatisticsButton.addActionListener(e -> resetStats());
    }

    JPanel createToolWindowPanel() {
       return panel;
    }

    private void resetStats() {
        if (Messages.showYesNoDialog(
                KeyPromoterBundle.message("kp.dialog.reset.statistic.text"),
                KeyPromoterBundle.message("kp.dialog.reset.statistic.title"),
                Messages.getQuestionIcon()) == 0) {
            statService.resetStatistic();
        }
    }

    private void createUIComponents() {
        statisticsList = new StatisticsList();
        suppressedList = new SuppressedList();
//        suppressedListModel.getPropertyChangeSupport().addPropertyChangeListener((PropertyChangeListener) suppressedList);
//        suppressedListModel.getPropertyChangeSupport().addPropertyChangeListener((PropertyChangeListener) statisticsList);
    }


}
