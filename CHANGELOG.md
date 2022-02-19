# Key Promoter X Changelog

## [Unreleased]
### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## [2022.1.1]
### Added
- Another try to build for IntelliJ Platform version 221
- Update version of Gradle and Changelog plugins
- Removed awful hack to inspect tool window buttons

### Changed

### Deprecated

### Removed

### Fixed
- Recognizing the stripe buttons for tool windows as they moved from package `com.intellij.openapi.wm.impl`
  to `com.intellij.toolWindow`
- Issue that showed a notification for creating a shortcut, when there was already a mouse-shortcut defined and the same
  action was invoked (see [Issue #76](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/76)).

### Security

## [2020.1]
- Add scrollbars in the toolwindow for bottom
  placement ([Issue 55](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/55))
- Revert back to non-dynamic plugin for now
- Fix icon sizes
- Make extension reloadable without requiring a restart of the IDE
- Fix all deprecation issues
- Fix bug that prevents settings from being
  saved ([Issue 51](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/51))
- Include "Organization" requirement for plugins
- Make compatible with 2020.1 EAP products

## [2019.3]
- Make compatible with 2019.3 EAP products
- Fix display of Run and Debug actions which will from now not display the specific run configuration and therefore
  count all these as one entry ([Issue 44](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/44))
- Fix regression where editor popup action won't show
  up ([Issue 43](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/42))
- Add features that lets you snooze the KPX
  notifications ([Issue 42](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/42))
- Add features that lets you turn off KPX in Presentation and Distraction Free
  Mode ([Issue 39](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/39))
- Fix instances where the KPX would not recognize a mouse-click.
  See [this IDEA issue.](https://youtrack.jetbrains.com/issue/IDEA-219133)
- Fix wrong icon for actions without icon ([Issue 37](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/37))

## [2019.2]
- Make Key Promoter X work on EAP versions like PhpStorm 2019.2
- Increase minimum required IntelliJ version to get rid of deprecated code
- "Do not show again" action dismisses the tip
  immediately ([Issue 34](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/34))
- Increase possible settings for notification limits
  ([Issue 30](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/30))
- Add plugin icon shown in the settings and the repository
- New icons that better integrate into the UI
- Implemented a setting to only show "Keyboard" shortcuts. This is default and can be adjusted.
- Fixed
  exception [that occurred on closing Keymap dialog in 2018.3](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/27)
  .
- Removed setting for maximum visible notifications. IDEA 2018.3 seems to restrict this itself.
- Implemented feature-request:
  [Minimum count before notification](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/20#event-1720427835)
- Fixed empty tool-window with several windows open
- Fixed version range to make it work for EAP
- Fixed null-action tip on tool-window title buttons
- Fixed exception with wrong entries in the statistic
- Should work on Android Studio now
- Fixed re-appearing tips when custom shortcuts are used
- Made tips for context menus more consistent
- Fixed issue that would show suppressed shortcuts
- Implemented "do not show again"
- Tool-window updates automatically
- Icons for the Key Promoter X
- Tool-window shows now icons of the actions that were called
- Better visualization of the statistics tool-window
- Fix bug to catch tool-window buttons successfully
- Re-implemented the algorithm that catches button clicks and extracts their information
- Change to internal notification system instead of a custom AWT window