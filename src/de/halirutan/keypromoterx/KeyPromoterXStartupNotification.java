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

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.util.text.VersionComparatorUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Provides an information balloon at the start of the IDE when a new version is installed.
 */
public class KeyPromoterXStartupNotification implements StartupActivity, DumbAware {
  @Override
  public void runActivity(@NotNull Project project) {
    if (ApplicationManager.getApplication().isUnitTestMode()) return;

    final KeyPromoterSettings settings = ServiceManager.getService(KeyPromoterSettings.class);
    final String installedVersion = settings.getInstalledVersion();

    final IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId("Key Promoter X"));
    if (installedVersion != null && plugin != null) {
      final int compare = VersionComparatorUtil.compare(installedVersion, plugin.getVersion());
//      if (true) { // TODO: Don't forget to remove that! For proofreading.
      if (compare < 0) {
        KeyPromoterNotification.showStartupNotification();
        settings.setInstalledVersion(plugin.getVersion());
      }
    }
  }

}
