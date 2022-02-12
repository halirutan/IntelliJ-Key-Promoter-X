package de.halirutan.keypromoterx;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.Action;

public class KeyPromoterDialogFactory {
    public static DialogWrapper askToCreateShortcutDialog(String title, String message, KeyPromoterAction action) {
        return new KeyPromoterDialog(title, message, action) {
            @Override
            protected Action @NotNull [] createActions() {
                return new Action[] {
                        new OkEditKeymapAction(), getCancelAction(), new SuppressTipAction()
                };
            }
        };
    }

    public static DialogWrapper tipDialog(String title, String message, KeyPromoterAction action) {
        return new KeyPromoterDialog(title, message, action) {
            @Override
            protected Action @NotNull [] createActions() {
                return new Action[] {
                        getOKAction(), new ShortcutEditKeymapAction(), new SuppressTipAction()
                };
            }
        };
    }
}
