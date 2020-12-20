/*
 * Copyright (c) 2019 Patrick Scheibe, Dmitry Kashin, Athiele.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.halirutan.keypromoterx;

import com.intellij.application.Topics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import de.halirutan.keypromoterx.clipboard.CopyToClipboardAction;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;
import de.halirutan.keypromoterx.statistic.StatisticsList;
import de.halirutan.keypromoterx.statistic.SuppressedList;

import javax.swing.*;

/**
 * Controlling class of the tool-window
 *
 * @author athiele, Patrick Scheibe
 */
@SuppressWarnings("unused")
class KeyPromoterToolWindowPanel implements Disposable, SnoozeNotifier.Handler {

  private final KeyPromoterStatistics statService = ServiceManager.getService(KeyPromoterStatistics.class);
  private JPanel panel;
  private JList statisticsList;
  private JButton resetStatisticsButton;
  private JButton copyStatisticsToClipboardButton;
  private JList suppressedList;
  private JCheckBox snoozeCheckBox;
  private JCheckBox useMarkdownFormatCheckBox;

  KeyPromoterToolWindowPanel() {
    resetStatisticsButton.addActionListener(e -> resetStats());
    copyStatisticsToClipboardButton.addActionListener(e -> copyToClipboardStats());
    Topics.subscribe(SnoozeNotifier.Handler.SNOOZE_TOPIC, this, this);
    snoozeCheckBox.addItemListener(e -> SnoozeNotifier.setSnoozed(snoozeCheckBox.isSelected()));
  }

  @SuppressWarnings("WeakerAccess")
  public JPanel getContent() {
    return panel;
  }

  private void resetStats() {
    if (Messages.showYesNoDialog(
        KeyPromoterBundle.message("kp.dialog.reset.statistic.text"),
        KeyPromoterBundle.message("kp.dialog.reset.statistic.title"),
        Messages.getQuestionIcon()) == Messages.YES) {
      statService.resetStatistic();
    }
  }

  private void copyToClipboardStats() {
    CopyToClipboardAction.copyStatisticsToClipboard(useMarkdownFormatCheckBox.isSelected());
  }

  private void createUIComponents() {
    statisticsList = new StatisticsList();
    suppressedList = new SuppressedList();
  }

  @Override
  public void dispose() {

  }

  @Override
  public void snoozeAction(boolean state) {
    snoozeCheckBox.setSelected(state);
  }

}
