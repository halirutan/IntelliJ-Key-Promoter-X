package de.halirutan.keypromoterx;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Core class to create the Key Promoter X tool-window. Nothing interesting here except of template code.
 *
 * @author athiele, Patrick Scheibe
 */
public class KeyPromoterToolWindowFactory implements ToolWindowFactory {

    private KeyPromoterToolWindowBuilder toolWindowBuilder = new KeyPromoterToolWindowBuilder();
    private ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel toolWindowContent = toolWindowBuilder.createToolWindowPanel();
        Content content = contentFactory.createContent(toolWindowContent, KeyPromoterBundle.message("kp.tool.window.title"), false);
        toolWindow.getContentManager().addContent(content);
    }
}
