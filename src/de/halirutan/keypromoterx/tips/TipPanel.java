package de.halirutan.keypromoterx.tips;

import com.intellij.ide.util.TipUIUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;
import de.halirutan.keypromoterx.KeyPromoterIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.util.SystemInfo.isWin10OrNewer;
import static com.intellij.ui.Gray.xD0;
import static com.intellij.util.ui.UIUtil.isUnderDarcula;

/**
 * @author patrick (02.03.19).
 */
public class TipPanel extends JPanel {
  private static final JBColor DIVIDER_COLOR = new JBColor(0xd9d9d9, 0x515151);
  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 200;
  private final TipUIUtil.Browser myBrowser;
  private final JLabel myPoweredByLabel;
  private final String text = "<html>\n" +
      "<head>\n" +
      "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/tips.css\">\n" +
      "</head>\n" +
      "<body>\n" +
      "    <p>\n" +
      "        To open any class or file in the editor at the desired line, press <span class=\"shortcut\">&shortcut:GotoFile;</span>\n" +
      "        (<span class=\"control\">Navigate | File</span>),\n" +
      "        start typing the name, and choose the one from the suggestion list.\n" +
      "        Then type the colon (<span class=\"code_emphasis\">:</span>) and a line number.</p>\n" +
      "    <p>\n" +
      "        The selected file will open with the caret at the specified line.</p>\n" +
      "    <p class=\"image\">\n" +
      "        <img src=\"images/gotoFileLineNumber.png\"></p>\n" +
      "</body>\n" +
      "</html>\n";

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


    myPoweredByLabel = new JBLabel(IconUtil.scale(KeyPromoterIcons.KP_ICON, this, 3.0f));
    myPoweredByLabel.setSize(128, 128);
    myPoweredByLabel.setBorder(JBUI.Borders.empty(0, 10));
    myPoweredByLabel.setForeground(SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES.getFgColor());

    add(myPoweredByLabel, BorderLayout.NORTH);
  }

  void setMessageText(@NotNull String text) {
    myBrowser.setText(text);
  }

  @Override
  public Dimension getPreferredSize() {
    return new JBDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }


}
