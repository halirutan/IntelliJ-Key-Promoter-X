/*
 * Copyright (c) 2019 Patrick Scheibe, Dmitry Kashin, Athiele.
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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.Topic;

/**
 * Provides notification to all opened IDEA windows that the user has disabled the Key Promoter X balloons in one of the
 * tool-windows.
 * <p>
 * In depth explanation: With one IDEA running, you can have several projects (and windows) opened. Each IDEA window
 * gets its own instance of the panel inside the Key Promoter X tool-window and therefore changes need to be synchronized
 * among all tool-window instances. When someone enables the "disable notifications" checkbox, this change needs to be
 * published to all other tool-windows. Therefore, all Key Promoter X tool-windows subscribe to
 * {@link Handler#SNOOZE_TOPIC} and when in one of them the "disable notification" checkbox is clicked, it calls
 * {@link #setSnoozed(boolean)} which notifies all other subscribers about the change.
 * </p>
 */
class SnoozeNotifier {
  private static boolean isSnoozed = false;
  private static MessageBus messageBus = ApplicationManager.getApplication().getMessageBus();

  static boolean isSnoozed() {
    return isSnoozed;
  }

  static void setSnoozed(boolean state) {
    if (isSnoozed != state) {
      isSnoozed = state;
      final Handler snoozeNotifier = messageBus.syncPublisher(Handler.SNOOZE_TOPIC);
      snoozeNotifier.snoozeAction(isSnoozed);
    }
  }

  /**
   * Business interface and topic for snoozing notifications.
   */
  public interface Handler {
    Topic<SnoozeNotifier.Handler> SNOOZE_TOPIC =
        Topic.create(KeyPromoterBundle.message("kp.snooze.topic"), SnoozeNotifier.Handler.class);

    void snoozeAction(boolean state);
  }
}