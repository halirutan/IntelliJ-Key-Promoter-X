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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;
import de.halirutan.keypromoterx.KeyPromoterIcons;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import java.util.HashMap;

/**
 * Provides a custom JBList for displaying how often a button was pressed that could have been replaced by a shortcut.
 * The list is backed by {@link KeyPromoterStatistics} that keeps the values persistent through restarts.
 */
public class StatisticsList extends JBList<StatisticsItem> implements PropertyChangeListener {
  @Serial
  private static final long serialVersionUID = 20212;
  private final DefaultListModel<StatisticsItem> myModel;
  private final KeyPromoterStatistics myStats = ApplicationManager.getApplication().getService(KeyPromoterStatistics.class);

  public StatisticsList() {
    myModel = new DefaultListModel<>();
    setModel(myModel);
    myStats.registerPropertyChangeSupport(this);
    setCellRenderer(new StatisticsItemCellRenderer());
    updateStats();
    addMouseListener(new MouseInputListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
          suppressItem();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }

      @Override
      public void mouseDragged(MouseEvent e) {
      }

      @Override
      public void mouseMoved(MouseEvent e) {
      }
    });
  }

  private void suppressItem() {
    final StatisticsItem selectedValue = getSelectedValue();
    if (selectedValue != null) {
      myStats.suppressItem(selectedValue);
    }
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
  static class StatisticsItemCellRenderer extends JBLabel implements ListCellRenderer<StatisticsItem> {

    @Serial
    private static final long serialVersionUID = 20212;
    private final static HashMap<String, Icon> iconCache = new HashMap<>();
    //    private final Logger logger = Logger.getInstance(StatisticsItemCellRenderer.class);
    private static final String message = "%s for %s (%dx missed, %dx used)";

    private Icon getCachedIcon(String id) {
      if (iconCache.containsKey(id)) {
        return iconCache.get(id);
      }
      iconCache.put(id, ActionIconRetriever.getActionIconOrDefault(id));
      return iconCache.get(id);
    }


    @Override
    public JLabel getListCellRendererComponent(JList<? extends StatisticsItem> list, StatisticsItem value, int index, boolean isSelected, boolean cellHasFocus) {
      final Color foreground = list.getForeground();
      setText(message.formatted(value.getShortcut(), value.getDescription(), value.count, value.hits));
      setForeground(foreground);
      setBorder(JBUI.Borders.empty(2, 10));

      if (value.ideaActionID != null && !value.ideaActionID.isEmpty()) {
        setIcon(getCachedIcon(value.ideaActionID));
      } else {
        setIcon(KeyPromoterIcons.KP_ICON);
      }
      return this;
    }
  }

}
