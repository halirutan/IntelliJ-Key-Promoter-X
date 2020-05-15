package de.halirutan.keypromoterx.tips;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.util.text.VersionComparatorUtil;
import de.halirutan.keypromoterx.KeyPromoterSettings;
import org.jetbrains.annotations.NotNull;

/**
 * @author patrick (02.03.19).
 */
public class KPXStartupNotification implements StartupActivity, DumbAware {
  @Override
  public void runActivity(@NotNull Project project) {
    if (ApplicationManager.getApplication().isUnitTestMode()) return;

    final Application application = ApplicationManager.getApplication();
    final KeyPromoterSettings settings = ServiceManager.getService(KeyPromoterSettings.class);
    final String installedVersion = settings.getInstalledVersion();

    final IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId("Key Promoter X"));
    if (installedVersion != null && plugin != null) {
      final int compare = VersionComparatorUtil.compare(installedVersion, plugin.getVersion());
//      if (true) { // TODO: Don't forget to remove that! For proofreading.
      if (compare < 0) {
        application.invokeLater(() -> KPXStartupDialog.showStartupDialog(project));
        settings.setInstalledVersion(plugin.getVersion());
      }
    }
  }

}
