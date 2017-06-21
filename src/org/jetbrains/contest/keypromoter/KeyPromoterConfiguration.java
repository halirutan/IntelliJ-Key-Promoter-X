package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.ColorPanel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static org.jetbrains.contest.keypromoter.KeyPromoterUtils.convertColorToMap;
import static org.jetbrains.contest.keypromoter.KeyPromoterUtils.convertMapToColor;


/**
 * Configuration of plugin saving and editing.
 *
 * @author Dmitry Kashin
 */
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

    public boolean isModified() {
        if (myMenus.isSelected() != keyPromoterSettings.isMenusEnabled()) return true;
        if (myToolbarButtons.isSelected() != keyPromoterSettings.isToolbarButtonsEnabled()) return true;
        if (myToolWindowButtons.isSelected() != keyPromoterSettings.isToolWindowButtonsEnabled()) return true;
        if (myAllButtons.isSelected() != keyPromoterSettings.isAllButtonsEnabled()) return true;
        if (myTextColor.getSelectedColor() != keyPromoterSettings.getTextColor()) return true;
        if (myBackgroundColor.getSelectedColor() != keyPromoterSettings.getBackgroundColor()) return true;
        if (myBorderColor.getSelectedColor() != keyPromoterSettings.getBorderColor()) return true;
        if (!myDisplayTime.getValue().equals(keyPromoterSettings.getDisplayTime())) return true;
        if (!myAnimationDelay.getValue().equals(keyPromoterSettings.getFlashAnimationDelay())) return true;
        if (!myProposeToCreateShortcutCount.getValue().equals(keyPromoterSettings.getProposeToCreateShortcutCount()))
            return true;
        if (!myPopupTemplate.getText().equals(keyPromoterSettings.getPopupTemplate())) return true;
        if (myFixedTipPosition.isSelected() != keyPromoterSettings.isFixedTipPosition()) return true;
        return false;
    }

    public void apply() throws ConfigurationException {
        keyPromoterSettings.setMenusEnabled(myMenus.isSelected());
        keyPromoterSettings.setToolbarButtonsEnabled(myToolbarButtons.isSelected());
        keyPromoterSettings.setToolWindowButtonsEnabled(myToolWindowButtons.isSelected());
        keyPromoterSettings.setAllButtonsEnabled(myAllButtons.isSelected());
        keyPromoterSettings.setTextColor(convertColorToMap(myTextColor.getSelectedColor()));
        keyPromoterSettings.setBackgroundColor(convertColorToMap(myBackgroundColor.getSelectedColor()));
        keyPromoterSettings.setBorderColor(convertColorToMap(myBorderColor.getSelectedColor()));
        keyPromoterSettings.setDisplayTime(new Integer(myDisplayTime.getValue().toString()));
        keyPromoterSettings.setFlashAnimationDelay(new Integer(myAnimationDelay.getValue().toString()));
        keyPromoterSettings.setProposeToCreateShortcutCount(new Integer(myProposeToCreateShortcutCount.getValue().toString()));
        keyPromoterSettings.setPopupTemplate(myPopupTemplate.getText());
        keyPromoterSettings.setFixedTipPosistion(myFixedTipPosition.isSelected());
    }

    public void reset() {
        myMenus.setSelected(keyPromoterSettings.isMenusEnabled());
        myToolbarButtons.setSelected(keyPromoterSettings.isToolbarButtonsEnabled());
        myToolWindowButtons.setSelected(keyPromoterSettings.isToolWindowButtonsEnabled());
        myAllButtons.setSelected(keyPromoterSettings.isAllButtonsEnabled());
        myTextColor.setSelectedColor(convertMapToColor(keyPromoterSettings.getTextColor()));
        myBackgroundColor.setSelectedColor(convertMapToColor(keyPromoterSettings.getBackgroundColor()));
        myBorderColor.setSelectedColor(convertMapToColor(keyPromoterSettings.getBorderColor()));
        myDisplayTime.setValue(keyPromoterSettings.getDisplayTime());
        myAnimationDelay.setValue(keyPromoterSettings.getFlashAnimationDelay());
        myProposeToCreateShortcutCount.setValue(keyPromoterSettings.getProposeToCreateShortcutCount());
        myPopupTemplate.setText(keyPromoterSettings.getPopupTemplate());
        myFixedTipPosition.setSelected(keyPromoterSettings.isFixedTipPosition());
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        myConfigPanel = new JPanel();
        myConfigPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        myConfigPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Appearance"));
        myFixedTipPosition = new JCheckBox();
        myFixedTipPosition.setText("Tip popups always at the bottom of screen");
        myFixedTipPosition.setMnemonic('P');
        myFixedTipPosition.setDisplayedMnemonicIndex(4);
        panel2.add(myFixedTipPosition, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Productivity"));
        myProposeToCreateShortcutCount = new JSpinner();
        myProposeToCreateShortcutCount.setOpaque(true);
        panel3.add(myProposeToCreateShortcutCount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(40, -1), null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("<html>Number of invocations before suggest create shortcut<br>(0 - never)</html>");
        label1.setDisplayedMnemonic('N');
        label1.setDisplayedMnemonicIndex(6);
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        myConfigPanel.add(panel4, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Popup"));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        myPopupTemplate = new JTextPane();
        myPopupTemplate.setText("<html>\n <body>\n  <table>\n   <tr>\n    <td align=\"center\"><font size=8>{0}</font></td>\n   </tr>\n   <tr>\n    <td align=\"center\"><font size=6>{1} time(s)</font></td>\n   </tr>\n  </table>\n </body>\n</html>");
        scrollPane1.setViewportView(myPopupTemplate);
        final JLabel label2 = new JLabel();
        label2.setText("Template");
        label2.setDisplayedMnemonic('E');
        label2.setDisplayedMnemonicIndex(1);
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("<html><body><ul><li>{0} - Shortcut detail</li><li>{1} - stats</li></ul></body></html>");
        panel4.add(label3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        myConfigPanel.add(panel5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Colors"));
        myBackgroundColor = new ColorPanel();
        panel6.add(myBackgroundColor, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        myBorderColor = new ColorPanel();
        panel6.add(myBorderColor, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Text");
        panel6.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Background");
        panel6.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Border");
        panel6.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        myTextColor = new ColorPanel();
        panel6.add(myTextColor, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        myConfigPanel.add(spacer2, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        myConfigPanel.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Enabled for"));
        myMenus = new JCheckBox();
        myMenus.setText("Menus");
        myMenus.setMnemonic('M');
        myMenus.setDisplayedMnemonicIndex(0);
        panel7.add(myMenus, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        myToolbarButtons = new JCheckBox();
        myToolbarButtons.setText("Toolbar buttons");
        myToolbarButtons.setMnemonic('T');
        myToolbarButtons.setDisplayedMnemonicIndex(0);
        panel7.add(myToolbarButtons, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        myToolWindowButtons = new JCheckBox();
        myToolWindowButtons.setText("Tool window buttons");
        myToolWindowButtons.setMnemonic('W');
        myToolWindowButtons.setDisplayedMnemonicIndex(5);
        panel7.add(myToolWindowButtons, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        myAllButtons = new JCheckBox();
        myAllButtons.setText("All buttons(mnemonics)");
        myAllButtons.setMnemonic('A');
        myAllButtons.setDisplayedMnemonicIndex(0);
        panel7.add(myAllButtons, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        myConfigPanel.add(spacer3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        myConfigPanel.add(panel8, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Animation settings"));
        myDisplayTime = new JSpinner();
        panel9.add(myDisplayTime, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Flashing animation delay(ms)");
        label7.setDisplayedMnemonic('F');
        label7.setDisplayedMnemonicIndex(0);
        panel9.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        myAnimationDelay = new JSpinner();
        panel9.add(myAnimationDelay, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(60, -1), null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Display time(ms)");
        label8.setDisplayedMnemonic('D');
        label8.setDisplayedMnemonicIndex(0);
        panel9.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel9.add(spacer4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        label1.setLabelFor(myProposeToCreateShortcutCount);
        label2.setLabelFor(scrollPane1);
        label7.setLabelFor(myAnimationDelay);
        label8.setLabelFor(myDisplayTime);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return myConfigPanel;
    }
}
