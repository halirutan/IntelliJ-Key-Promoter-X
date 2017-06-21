package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;

/**
 * Created by athiele on 05.01.2015.
 *
 */
public class KeyPromoterToolWindowFactory implements ToolWindowFactory {


  private KeyPromoterToolWindowBuilder toolWindowBuilder = new KeyPromoterToolWindowBuilder();
  private ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

    JPanel toolWindowContent = toolWindowBuilder.createToolWindowPanel();
    Content content = contentFactory.createContent(toolWindowContent, "Top Ten", false);
    toolWindow.getContentManager().addContent(content);

  }



}
