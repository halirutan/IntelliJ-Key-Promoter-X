package org.jetbrains.contest.keypromoter;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import static org.jetbrains.contest.keypromoter.KeyPromoterUtils.convertMapToColor;

/**
 * Popup window with information about missed shortcut. Contains shortcut keys and number of invocations by mouse.
 * @author Dmitry Kashin
 */
class TipWindow extends JWindow {

    private KeyPromoterSettings keyPromoterSettings = ServiceManager.getService(KeyPromoterSettings.class);

    TipWindow(Frame owner, String text, Component sourceComponent) {
        super(owner);
        setAlwaysOnTop(true);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
        setContentPane(contentPane);
        TipLabel myTip = new TipLabel(text, keyPromoterSettings);
        add(myTip);
        pack();
        // If fixed position to show then place it at the bottom of screen, otherwise show it at the mouse click position
        if (keyPromoterSettings.isFixedTipPosition()) {
            setLocation(owner.getX() + (int) (owner.getWidth() - myTip.getSize().getWidth()) / 2,
                    owner.getY() + (int) (owner.getHeight() - myTip.getSize().getHeight() - 100));
        } else {
            // Trying to fit the screen
            Point locationPoint = SwingUtilities.convertPoint(sourceComponent,
                    new Point(sourceComponent.getWidth() + 2, sourceComponent.getHeight() + 2), owner);
            locationPoint.x = owner.getX() + (int) Math.min(locationPoint.getX(), owner.getWidth() - myTip.getSize().getWidth());
            locationPoint.y = owner.getY() + (int) Math.min(locationPoint.getY(), owner.getHeight() - myTip.getSize().getHeight());
            setLocation(locationPoint);
        }

    }

    /**
     * Component for displaying tip with some simple animation.
     */
    private class TipLabel extends JLabel {
        private float myAlphaValue;
        private static final float ALPHA_STEP = 0.1f;
        private static final float START_ALPHA = 0f;
        private KeyPromoterSettings keyPromoterSettings;

        TipLabel(String text, KeyPromoterSettings keyPromoterSettings) {
            super();
            this.keyPromoterSettings = keyPromoterSettings;
            myAlphaValue = 0.5f;
            setOpaque(false);
            setText(text);
            setForeground(convertMapToColor(keyPromoterSettings.getTextColor()));
        }

        // some painting fun
        public void paintComponent(Graphics g) {
            // Cast to Graphics2D so we can set composite information.
            Graphics2D g2d = (Graphics2D)g;

            // Save the original composite.
            Composite oldComp = g2d.getComposite();

            // Create an AlphaComposite
            Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, myAlphaValue);
            // Increase or rotate
            if (myAlphaValue < 1 - ALPHA_STEP) {
                myAlphaValue += ALPHA_STEP;
            } else {
                myAlphaValue = START_ALPHA;
            }

            // Set the composite on the Graphics2D object.
            g2d.setColor(convertMapToColor(keyPromoterSettings.getBorderColor()));
            g2d.setComposite(alphaComp);

            Area border = new Area(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
            border.subtract(new Area(new Rectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4)));
            g2d.fill(border);

            // Restore the old composite.
            g2d.setComposite(oldComp);
            Color backgroundColor = convertMapToColor(keyPromoterSettings.getBackgroundColor());
            g2d.setColor(new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 64));
            g2d.fillRect(3, 3, getWidth() - 6, getHeight() - 6);
            super.paintComponent(g);
        }

    }

}
