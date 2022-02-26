package de.halirutan.keypromoterx;

import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.BorderLayout;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

public class KeyPromoterDialog extends DialogWrapper {
    private static final KeyPromoterSettings mySettings = getApplication().getService(KeyPromoterSettings.class);

    private final KeyPromoterAction action;
    private final JLabel label;

    public KeyPromoterDialog(String title, String message, KeyPromoterAction action) {
        super(true); // use current window as parent
        this.action = action;
        this.label = new JLabel(message, SwingConstants.CENTER);
        setTitle(title);
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        final JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(label, BorderLayout.CENTER);

        final Keymap activeKeymap = KeymapManager.getInstance().getActiveKeymap();
        final Shortcut[] shortcuts = activeKeymap.getShortcuts(action.getIdeaActionID());
        DumbAwareAction.create((e) -> close(OK_EXIT_CODE))
                .registerCustomShortcutSet(new CustomShortcutSet(shortcuts), dialogPanel, getDisposable());

        return dialogPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return label;
    }

    @Override
    public void show() {
        if (mySettings.hardMode) {
            getApplication().invokeLater(super::show);
        }
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[0];
    }
}
