/*
 * Copyright (c) 2020 Patrick Scheibe, Dmitry Kashin, Athiele.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.halirutan.keypromoterx;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.toolWindow.StripeButton;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class KeyPromoterAWTListener implements AppLifecycleListener, AWTEventListener {

  private boolean mouseDrag = false;

  /**
   * Adds the current instance as an AWT event listener, observing mouse events.
   *
   * @param commandLineArgs Ignored here
   */
  @Override
  public void appFrameCreated(@NotNull List<String> commandLineArgs) {
    long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK;
    Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask);
  }

  /**
   * Listens for and dispatches AWT events, particularly mouse events.
   * Handles events, and invokes corresponding methods based on the
   * event types and states.
   * It takes care to not mistake drag and drop events for mouse clicks.
   *
   * @param e the AWT event to be dispatched
   */
  @Override
  public void eventDispatched(AWTEvent e) {
    int id = e.getID();
    if (id == MouseEvent.MOUSE_DRAGGED) {
      mouseDrag = true;
      return;
    }

    if (id == MouseEvent.MOUSE_RELEASED && ((MouseEvent) e).getButton() == MouseEvent.BUTTON1) {
      if (!mouseDrag) {
        handleMouseEvent(e);
      }
      mouseDrag = false;
    }
  }

  /**
   * Handles mouse events specifically related to tool window buttons.
   * This method is triggered when a mouse event occurs and checks
   * if the event source is an instance of {@code StripeButton}.
   * If tool window button tips are enabled in the settings, it
   * creates an instance of {@code KeyPromoterAction} and uses
   * {@code KeyPromoterTipService} to show the tip.
   *
   * @param e the AWT event to handle, typically a mouse event
   */
  private void handleMouseEvent(AWTEvent e) {
    if (e.getSource() instanceof StripeButton) {
      KeyPromoterSettings keyPromoterSettings = ApplicationManager.getApplication().getService(KeyPromoterSettings.class);
      if (keyPromoterSettings.isToolWindowButtonsEnabled()) {
        KeyPromoterAction action = new KeyPromoterAction(e);
        KeyPromoterTipService tipService = ApplicationManager.getApplication().getService(KeyPromoterTipService.class);
        tipService.showTip(action, KeyPromoterTipService.ActionType.MouseAction);
      }
    }
  }

  /**
   * This method is called when the application is about to be closed.
   * It removes this instance as an AWT event listener.
   *
   * @param isRestart is ignored in this override
   */
  @Override
  public void appWillBeClosed(boolean isRestart) {
    Toolkit.getDefaultToolkit().removeAWTEventListener(this);
  }
}
