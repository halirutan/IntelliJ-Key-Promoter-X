package de.halirutan.keypromoterx.tips;

import com.intellij.CommonBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.util.ui.JBUI;
import de.halirutan.keypromoterx.KeyPromoterBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author patrick (02.03.19).
 */
public class KPXStartupDialog extends DialogWrapper {

  private static KPXStartupDialog ourInstance;
  private TipPanel myTipPanel;

  private KPXStartupDialog() {
    super(WindowManagerEx.getInstanceEx().findVisibleFrame(), true);
    initialize();
  }

  private KPXStartupDialog(Window w) {
    super(w, true);
    initialize();
  }

  public static void showStartupDialog(Project project) {
    Window w = WindowManagerEx.getInstanceEx().suggestParentWindow(project);
    if (ourInstance != null && ourInstance.isVisible()) {
      ourInstance.dispose();
    }
    ourInstance = (w == null) ? new KPXStartupDialog() : new KPXStartupDialog(w);
    ourInstance.show();
  }

  private void initialize() {
    setModal(false);
    setTitle(KeyPromoterBundle.message("startup.message.title"));
    setCancelButtonText(CommonBundle.getCloseButtonText());
    myTipPanel = new TipPanel();
    myTipPanel.setMessageText(loadMessageText());
    setHorizontalStretch(1.33f);
    setVerticalStretch(1.25f);
    init();
  }

  @NotNull
  @Override
  protected Action[] createActions() {
    return new Action[]{getCancelAction()};
  }

  @NotNull
  @Override
  protected DialogStyle getStyle() {
    return DialogStyle.COMPACT;
  }

  @Override
  protected JComponent createSouthPanel() {
    JComponent component = super.createSouthPanel();
    component.setBorder(JBUI.Borders.empty(8, 12));
    return component;
  }


  @Override
  protected JComponent createCenterPanel() {
    return myTipPanel;
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    return myPreferredFocusedComponent;
  }

  private String loadMessageText() {
    ClassLoader classLoader = getClass().getClassLoader();
    final InputStream stream = classLoader.getResourceAsStream("tips/Message.html");
    if (stream != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      StringBuilder text = new StringBuilder();
      reader.lines().forEach(text::append);
      return text.toString();
    }
    return "";
  }

}



