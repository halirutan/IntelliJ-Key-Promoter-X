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

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.JBUI;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;
import de.halirutan.keypromoterx.statistic.StatisticsItem;
import de.halirutan.keypromoterx.statistic.StatisticsList;
import de.halirutan.keypromoterx.statistic.SuppressedList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Controlling class of the tool-window
 *
 * @author athiele, Patrick Scheibe
 */
@SuppressWarnings("unused")
class KeyPromoterToolWindowPanel implements SnoozeNotifier.Handler {

  private final KeyPromoterStatistics statService = ApplicationManager.getApplication().getService(KeyPromoterStatistics.class);
  private final JPanel panel;
  private JList<StatisticsItem> statisticsList;
  private JButton resetStatisticsButton;
  private JList<StatisticsItem> suppressedList;
  private JCheckBox snoozeCheckBox;
  private JButton supportButton;
  private JPanel actionsPanel;

  KeyPromoterToolWindowPanel() {
    panel = buildUi();
    resetStatisticsButton.addActionListener(e -> resetStats());

    supportButton.setText(KeyPromoterBundle.message("kp.toolwindow.support.title"));
    supportButton.setToolTipText(KeyPromoterBundle.message("kp.notification.startup"));
    supportButton.addActionListener(e -> openSupportPage());
    snoozeCheckBox.setSelected(SnoozeNotifier.isSnoozed());
    SnoozeNotifier.addHandler(this);
    snoozeCheckBox.addItemListener(e -> SnoozeNotifier.setSnoozed(snoozeCheckBox.isSelected()));
  }

  @SuppressWarnings("WeakerAccess")
  public JPanel getContent() {
    return panel;
  }

  private static JScrollPane createTitledScrollPane(JComponent component, String title) {
    JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(component);
    scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title));
    return scrollPane;
  }

  private void resetStats() {
    if (Messages.showYesNoDialog(
        KeyPromoterBundle.message("kp.dialog.reset.statistic.text"),
        KeyPromoterBundle.message("kp.dialog.reset.statistic.title"),
        Messages.getQuestionIcon()) == Messages.YES) {
      statService.resetStatistic();
    }
  }

  private void openSupportPage() {
    BrowserUtil.browse(KeyPromoterBundle.message("kp.notification.startup.link"));
  }

  public JComponent getPreferredFocusableComponent() {
    return statisticsList;
  }

  private JPanel buildUi() {
    statisticsList = new StatisticsList();
    suppressedList = new SuppressedList();
    resetStatisticsButton = new JButton(KeyPromoterBundle.message("kp.toolwindow.reset.statistics"));
    resetStatisticsButton.setMnemonic(KeyEvent.VK_R);
    supportButton = new JButton();
    snoozeCheckBox = new JCheckBox(KeyPromoterBundle.message("kp.toolwindow.snooze.notifications"));
    actionsPanel = createActionsPanel();

    JBSplitter splitter = new JBSplitter(true);
    splitter.setHonorComponentsMinimumSize(true);
    splitter.setFirstComponent(createTitledScrollPane(statisticsList, KeyPromoterBundle.message("kp.toolwindow.statistics.title")));
    splitter.setSecondComponent(createTitledScrollPane(suppressedList, KeyPromoterBundle.message("kp.toolwindow.suppressed.title")));
    splitter.getDivider().setBackground(JBUI.CurrentTheme.CustomFrameDecorations.separatorForeground());

    JPanel content = new JPanel(new BorderLayout(0, JBUI.scale(5)));
    content.setBorder(JBUI.Borders.empty(5));
    content.add(splitter, BorderLayout.CENTER);
    content.add(actionsPanel, BorderLayout.SOUTH);
    return content;
  }

  private JPanel createActionsPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(IdeBorderFactory.createTitledBorder(KeyPromoterBundle.message("kp.toolwindow.panel.title")));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.weightx = 0;
    constraints.insets = JBUI.insetsBottom(2);
    panel.add(resetStatisticsButton, constraints);

    constraints.gridy = 1;
    panel.add(supportButton, constraints);

    constraints.gridy = 2;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = JBUI.emptyInsets();
    panel.add(snoozeCheckBox, constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.gridheight = 3;
    constraints.weightx = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    panel.add(Box.createHorizontalStrut(0), constraints);
    return panel;
  }

  @Override
  public void snoozeAction(boolean state) {
    snoozeCheckBox.setSelected(state);
  }
}
