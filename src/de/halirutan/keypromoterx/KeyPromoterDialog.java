package de.halirutan.keypromoterx;

import com.intellij.CommonBundle;
import com.intellij.openapi.keymap.impl.ui.EditKeymapsDialog;
import com.intellij.openapi.ui.DialogWrapper;
import de.halirutan.keypromoterx.statistic.KeyPromoterStatistics;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import static com.intellij.openapi.application.ApplicationManager.getApplication;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

public class KeyPromoterDialog extends DialogWrapper {
    private static final KeyPromoterSettings mySettings = getApplication().getService(KeyPromoterSettings.class);
    private static final int EXIT_CODE = 0;

    private final String message;
    private final KeyPromoterAction action;

    public KeyPromoterDialog(String title, String message, KeyPromoterAction action) {
        super(true); // use current window as parent
        this.message = message;
        this.action = action;
        setTitle(title);
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(message);
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }

    @Override
    public void show() {
        if (mySettings.hardMode) {
            getApplication().invokeLater(super::show);
        }
    }

    protected class SuppressTipAction extends DialogWrapperExitAction {

        private static final long serialVersionUID = 6960825521938776621L;

        SuppressTipAction() {
            super(KeyPromoterBundle.message("kp.notification.disable.message"), EXIT_CODE);
        }

        @Override
        public final void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            getApplication().getService(KeyPromoterStatistics.class).suppressItem(action);
        }
    }

    protected class OkEditKeymapAction extends EditKeymapAction {
        private static final long serialVersionUID = 7008457927107593064L;

        OkEditKeymapAction() {
            super(CommonBundle.getOkButtonText());
        }
    }

    protected class ShortcutEditKeymapAction extends EditKeymapAction {
        private static final long serialVersionUID = -2992315185577057068L;

        ShortcutEditKeymapAction() {
            super(createShortcutTextButton(action));
        }
    }

    private abstract class EditKeymapAction extends DialogWrapperExitAction {
        private static final long serialVersionUID = 7435917064219089718L;

        EditKeymapAction(String name) {
            super(name, EXIT_CODE);
            putValue(DEFAULT_ACTION, Boolean.TRUE);
        }

        @Override
        public final void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            EditKeymapsDialog dialog = new EditKeymapsDialog(null, action.getIdeaActionID());
            getApplication().invokeLater(dialog::show);
        }
    }

    private static String createShortcutTextButton(KeyPromoterAction action) {
        String formattedShortcut = ofNullable(action.getShortcut())
                .filter(not(String::isBlank))
                .or(() -> ofNullable(action.getDescription()))
                .filter(not(String::isBlank))
                .orElse("");
        return KeyPromoterBundle.message("kp.notification.dialog.ask.edit.shortcut", formattedShortcut).trim();
    }
}
