package de.halirutan.keypromoterx;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.ColorPanel;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


/**
 * Configuration of plugin saving and editing for the Key Promoter X
 *
 * @author Patrick Scheibe, Dmitry Kashin
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class KeyPromoterConfiguration extends BaseConfigurable implements SearchableConfigurable, PersistentStateComponent<KeyPromoterConfiguration> {

    JPanel myConfigPanel;
    private JSpinner myDisplayTime;
    private JCheckBox myAllButtons;
    private JCheckBox myToolWindowButtons;
    private JCheckBox myToolbarButtons;
    private JCheckBox myMenus;
    private JCheckBox myFixedTipPosition;

    private JSpinner myAnimationDelay;
    private ColorPanel myTextColor;
    private ColorPanel myBackgroundColor;
    private ColorPanel myBorderColor;
    private JSpinner myProposeToCreateShortcutCount;
    private JTextPane myPopupTemplate;

    private KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);

    @NotNull
    public String getId() {
        return "KeyPromoterConfiguration";
    }

    public Runnable enableSearch(String s) {
        return null;
    }

    public String getDisplayName() {
        return "KeyPromoter";
    }

    public Icon getIcon() {
        return null;
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        return myConfigPanel;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean isModified() {
        if (myMenus.isSelected() != keyPromoterSettings.isMenusEnabled()) return true;
        if (myToolbarButtons.isSelected() != keyPromoterSettings.isToolbarButtonsEnabled()) return true;
        if (myToolWindowButtons.isSelected() != keyPromoterSettings.isToolWindowButtonsEnabled()) return true;
        if (myAllButtons.isSelected() != keyPromoterSettings.isAllButtonsEnabled()) return true;
        if (!myProposeToCreateShortcutCount.getValue().equals(keyPromoterSettings.getProposeToCreateShortcutCount()))
            return true;
        return false;
    }

    public void apply() throws ConfigurationException {
        keyPromoterSettings.setMenusEnabled(myMenus.isSelected());
        keyPromoterSettings.setToolbarButtonsEnabled(myToolbarButtons.isSelected());
        keyPromoterSettings.setToolWindowButtonsEnabled(myToolWindowButtons.isSelected());
        keyPromoterSettings.setAllButtonsEnabled(myAllButtons.isSelected());
        keyPromoterSettings.setProposeToCreateShortcutCount(new Integer(myProposeToCreateShortcutCount.getValue().toString()));
    }

    public void reset() {
        myMenus.setSelected(keyPromoterSettings.isMenusEnabled());
        myToolbarButtons.setSelected(keyPromoterSettings.isToolbarButtonsEnabled());
        myToolWindowButtons.setSelected(keyPromoterSettings.isToolWindowButtonsEnabled());
        myAllButtons.setSelected(keyPromoterSettings.isAllButtonsEnabled());
        myProposeToCreateShortcutCount.setValue(keyPromoterSettings.getProposeToCreateShortcutCount());
    }

    public void disposeUIResources() {
    }

    public void setSettings(KeyPromoterSettings settings) {
        keyPromoterSettings = settings;
    }

    public KeyPromoterSettings getSettings() {
        return keyPromoterSettings;
    }

    public KeyPromoterConfiguration getState() {
        return this;
    }

    public void loadState(KeyPromoterConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
