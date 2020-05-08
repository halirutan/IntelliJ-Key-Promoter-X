package de.halirutan.keypromoterx.tips;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.intellij.ide.util.TipUIUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;
import de.halirutan.keypromoterx.KeyPromoterBundle;
import de.halirutan.keypromoterx.KeyPromoterIcons;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.util.SystemInfo.isWin10OrNewer;
import static com.intellij.ui.Gray.xD0;
import static com.intellij.util.ui.UIUtil.isUnderDarcula;

/**
 * @author patrick (02.03.19).
 */
public class TipPanel extends JPanel {
  private static final JBColor DIVIDER_COLOR = new JBColor(0xd9d9d9, 0x515151);
  private static final int DEFAULT_WIDTH = 450;
  private static final int DEFAULT_HEIGHT = 200;
  private final TipUIUtil.Browser myBrowser;

  TipPanel() {
    setLayout(new BorderLayout());
    if (isWin10OrNewer && !isUnderDarcula()) {
      setBorder(JBUI.Borders.customLine(xD0, 1, 0, 0, 0));
    }
    myBrowser = TipUIUtil.createBrowser();
    myBrowser.getComponent().setBorder(JBUI.Borders.empty(8, 12));
    JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(myBrowser.getComponent(), true);
    scrollPane.setBorder(JBUI.Borders.customLine(DIVIDER_COLOR, 0, 0, 1, 0));
    add(scrollPane, BorderLayout.CENTER);

    JLabel kpxIcon = new JBLabel(IconUtil.scale(KeyPromoterIcons.KP_ICON, this, 3.0f));
    kpxIcon.setSize(128, 128);
    kpxIcon.setBorder(JBUI.Borders.empty(0, 10));
    kpxIcon.setForeground(SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES.getFgColor());

    JLabel versionLabel = new JBLabel(KeyPromoterBundle.message("kp.tool.window.name"));
    versionLabel.setFont(new Font(versionLabel.getName(), Font.BOLD, 24));
    versionLabel.setForeground(JBUI.CurrentTheme.Label.foreground(false));
    JBBox horizontalBox = JBBox.createHorizontalBox();
    horizontalBox.setAlignmentX(.5f);
    horizontalBox.add(kpxIcon);
    horizontalBox.add(versionLabel);

    add(horizontalBox, BorderLayout.NORTH);
  }

  void setMessageText(@NotNull String text) {
    myBrowser.setText(text);
  }

  @Override
  public Dimension getPreferredSize() {
    return new JBDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }


}
