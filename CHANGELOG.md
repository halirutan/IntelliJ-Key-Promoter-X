# Key Promoter X Changelog

## [Unreleased]

- Update build system and use ideas from the
  [IntelliJ Platform Plugin Template repository](https://github.com/JetBrains/intellij-platform-plugin-template)
- Implement resizable split-pane and scroll-panes for Key Promoter tool window
  ([Issue 72](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/72))
- Support recent IntelliJ Platform 2021
  ([Issue 77](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/77))
- Make message for disabling a single shortcut alert clearer
  ([Issue 75](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/75))

## [2020.2.2]

- Add support for recent IDEs like CLion 2020.3
  ([Issue 68](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/68))

## [2020.2]

### Added

- Build for 2020.2
- Add feature that shows how often correct shortcuts where
  used ([Issue 61](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/61))

### Fixed

- Fix that actions with no icon get always the general KPX
  icon ([Issue 70](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/70))
- Fix that suppressed list is also cleared when resetting the
  statistics ([Issue 69](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/69))
- Fix that dragging tool-window buttons does not trigger
  KPX ([Issue 65](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/65))
- Fix display of RunClass and DebugClass which will now not show an entry for every single run
  configuration ([Issue 57](https://github.com/halirutan/IntelliJ-Key-Promoter-X/issues/57))

### Changed

- Update GitHub Action to test both IC and
  IU ([PR 66, thanks Chris!](https://github.com/halirutan/IntelliJ-Key-Promoter-X/pull/66))
- Make icons conform to Action Icons color
  palette ([PR 60, thanks Alex!](https://github.com/halirutan/IntelliJ-Key-Promoter-X/pull/60))

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
