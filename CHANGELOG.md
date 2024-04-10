# Key Promoter X Changelog

## [Unreleased]

### Fixed

- Add support for JetBrains IDEs 2024.1
- Update description

## [2023.3.0] - 2023-11-12

### Added

- Support for JetBrains IDEs version 2023.3

### Fixed

- Icons for new UI
  ([Issue 101](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/104))

## [2023.2.0] - 2023-07-30

### Added

- Support for JetBrains IDEs version 2023.2

### Changed

- Update GH actions for building and deploying the plugin
- Update plugin build setup to match with the recent IntelliJ Plugin Template

### Fixed

- Tool window buttons are working now with the new UI
  ([Issue 101](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/101))

## [2022.3.1]

### Changed

- Update build system

### Fixed

- Issue with notifications on Win11 where the shortcut and the suppress action is not directly visible in the
  notification
  ([Issue 100](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/100))
- Possible fix for tool window buttons not firing a Key Promoter notification with the new UI
  ([Issue 101](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/101))

## [2019.3.0]

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

## [2019.2.0]

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

## [2020.1.0]

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

[Unreleased]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/compare/v2023.3.0...HEAD
[2023.3.0]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/compare/v2023.2.0...v2023.3.0
[2023.2.0]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/compare/v2022.3.1...v2023.2.0
[2022.3.1]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/compare/v2019.3.0...v2022.3.1
[2020.1.0]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/commits/v2020.1.0
[2019.3.0]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/compare/v2019.2.0...v2019.3.0
[2019.2.0]: https://github.com/halirutan/IntelliJ-Key-Promoter-X/compare/v2020.1.0...v2019.2.0
