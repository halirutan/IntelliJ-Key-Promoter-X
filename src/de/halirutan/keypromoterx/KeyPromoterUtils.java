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

package de.halirutan.keypromoterx;

import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Provides some utility functions.
 *
 * @author Dmitry Kashin, Patrick Scheibe
 */
class KeyPromoterUtils {

    private static final KeymapManager keyMapManager = KeymapManager.getInstance();
    private static final KeyPromoterSettings mySettings = ServiceManager.getService(KeyPromoterSettings.class);

    /**
     * Get first field of class with target type to use during click source handling.
     *
     * @param aClass      class to inspect
     * @param targetClass target class to check field to plug
     * @return field
     */
    static Field getFieldOfType(Class<?> aClass, Class<?> targetClass) {
        do {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().equals(targetClass)) {
                    declaredField.setAccessible(true);
                    return declaredField;
                }
            }
        } while ((aClass = aClass.getSuperclass()) != null);
        return null;
    }

    /**
     * Uses an actionID to access the key-map of IDEA and find possible short-cuts
     * @param myIdeaActionID ActionID to look the shortcut up
     * @return a string combining one or more shortcuts
     */
    static String getKeyboardShortcutsText(String myIdeaActionID) {
        final Keymap activeKeymap = keyMapManager.getActiveKeymap();
        Shortcut[] shortcuts;
        if (mySettings.isShowKeyboardShortcutsOnly()) {
            shortcuts = Arrays.stream(
                activeKeymap.getShortcuts(myIdeaActionID)
            ).filter(Shortcut::isKeyboard).toArray(Shortcut[]::new);
        } else {
            shortcuts = activeKeymap.getShortcuts(myIdeaActionID);
        }


        if (shortcuts.length == 0) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < shortcuts.length; i++) {
            Shortcut shortcut = shortcuts[i];
            if (i > 0) {
                buffer.append(" or ");
            }
            buffer.append("'").append(KeymapUtil.getShortcutText(shortcut)).append("'");
        }
        return buffer.toString();
    }

}
