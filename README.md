![Downloads Badge](https://img.shields.io/jetbrains/plugin/d/9792-key-promoter-x.svg)
[![Build Status](https://github.com/halirutan/IntelliJ-Key-Promoter-X/actions/workflows/build.yml/badge.svg)](https://github.com/halirutan/IntelliJ-Key-Promoter-X/actions/workflows/build.yml)
[![Slack](https://img.shields.io/badge/Slack-%23plugin--keypromoter--x-blue)](https://plugins.jetbrains.com/slack)
[![Banner](doc/img/kpxBanner.png)](https://github.com/sponsors/halirutan)

---

I'm grateful for the community support that keeps Key Promoter X alive. This project currently receives sponsorship of
\$4/month from
[m2ger](https://github.com/m2ger),
\$2/month from
[JensAstrup](https://github.com/JensAstrup),
[franklinharper](https://github.com/franklinharper),
[macalac](https://github.com/macalac),
and \$1/month from
[0x1DOCD00D](https://github.com/0x1DOCD00D),
[objectx](https://github.com/objectx),
[j-walker23](https://github.com/j-walker23),
[carstenlex](https://github.com/carstenlex),
[r-k-b](https://github.com/r-k-b),
[sprak3000](https://github.com/sprak3000),
[goerge](https://github.com/goerge),
and [bishiboosh](https://github.com/bishiboosh).

It also received a \$5 one-time sponsorship from [kuhmuh](https://github.com/kuhmuh).
Four additional sponsors support the project privately, and I deeply appreciate their generosity while fully respecting
their choice to remain anonymous.

- [Become a GitHub Sponsor and support the Key Promoter X development](https://github.com/sponsors/halirutan)

---

Key Promoter X is a plugin for IntelliJ-based IDEs such as IntelliJ IDEA, Android Studio, and CLion. It helps you learn
essential keyboard shortcuts from the mouse actions you use while working.
When you click a button in the IDE with the mouse, Key Promoter X shows the keyboard shortcut you could have used
instead.
This makes it easier to replace repetitive mouse actions with shortcuts and gradually move toward a faster, mouse-free
workflow.
Currently, it supports toolbar buttons, menu actions, tool windows, and actions inside those tool windows.

![Teaser video](doc/img/keypromoter-demo.apng)

## ![Docs][doc-image] Features and Documentation

### Features

- non-intrusive shortcut notifications using the IDE's built-in message framework
- easy shortcut customization through links in the notification
- easy creation of shortcuts for buttons that do not have one yet
- a persistent list of missed shortcuts in an IDE tool window
- a list of suppressed tips for shortcuts you do not want to use

## Installation

You can install the plugin from **Settings | Plugins | Marketplace** by searching for `Key Promoter X`.
When you click a button with the mouse, a notification shows the shortcut you could have used instead. If a button has
no shortcut and
you press it several times, the plugin can suggest creating one for that action.

## Documentation

### How does it work?

When you click something with the mouse, the plugin tries to detect whether that click triggered an IntelliJ action. If
so,
and if it can identify the corresponding `ActionID`, it records the event. If the action already has a shortcut, the
plugin shows
the action name (for example, `Open Settings`) together with its keyboard shortcut.

![Notification](doc/img/notification.png)

Additionally, Key Promoter X records these mouse actions in its statistics tool window so you can see which actions you
use most often
and which shortcuts are worth learning first.

If the action does not yet have a shortcut but does have an `ActionID`, the plugin still records the click. Depending on
your settings
under **Settings | Tools | Key Promoter X**, it can suggest creating a shortcut after the same action has been invoked a
configurable
number of times.

Some mouse interactions cannot be inspected reliably, even when they trigger behavior in the IDE. In those cases, Key
Promoter X cannot
provide a shortcut hint. This can happen, for example, with certain buttons inside tool windows.

### The Key Promoter X tool window

Key Promoter X includes a built-in tool window on the right side of the IDE that gives you access to your list of missed
shortcuts and
suppressed items.

![Tool-window](doc/img/tool-window.png)

Every mouse click associated with an action that has a shortcut is recorded in the statistics. The items are ordered by
how often you
missed each shortcut.

Some mouse actions may be ones you do not want to replace with a shortcut. In those cases, click *Don't show again* in
the notification,
and the suppressed shortcut will appear in the list below the statistics. You can reactivate a suppressed item by
double-clicking it there.

The Key Promoter X tool window also lets you clear your statistics and start fresh. You can also snooze notifications
until the next IDE
restart, or enable them again sooner if you prefer.

### Settings for the Key Promoter X

Settings for the plugin can be found under **Settings | Tools | Key Promoter X**.

![Settings Panel](doc/img/settings.png)

#### General

- Show only keyboard shortcuts prevents notifications for mouse shortcuts. One example is the
  *Go to declaration* action, which can also be invoked with a mouse gesture. Enabling this option ensures that only
  keyboard shortcuts are shown.
- Disable in presentation or distraction free mode turns off Key Promoter X when you are giving a presentation or
  explicitly do not want to be disturbed.

#### Settings

- Clicks before notification is shown controls how often Key Promoter X displays a notification when you click
  a button with the mouse that already has a keyboard shortcut.
  For example, with a setting of 2, every second click will show a notification.
- Number of invocations before suggesting to create controls how often a button without a shortcut needs to be clicked
  before you see a message suggesting that you create one for it.

#### Enable for

This setting controls which buttons are allowed to show a tip. Note that the *All Buttons* checkbox will try to capture
as many mouse
clicks as possible, even if you are not pressing a real button. For example, it can show a tip when you hold Ctrl and
click a Java
method to jump to its declaration, which can also be done with Ctrl+B.

## ![dev image][dev-image] Development

The plugin is written in Java using the IntelliJ Platform plugin framework.
The codebase is relatively small and contains only a handful of well-documented classes, which makes it a good project
for learning how
IntelliJ plugins are implemented.

The code is hosted on GitHub and built with Gradle.

## ![bug image][issues-image] Reporting issues

If you experience bugs or unusual behavior, please create an
issue [on the bug tracker](https://github.com/halirutan/IntelliJ-Key-Promoter/issues).


## ![contact image][contact-image] Credits

The initial version was [implemented by Dmitry Kashin](https://code.google.com/p/key-promoter/), who later stopped
maintaining the project.

[athiele](https://github.com/athiele/key-promoter-fork/commits/master) then took the time to fork the original code, fix
issues, and provide
a version that worked with newer IntelliJ releases before that effort was also discontinued.


[logo-image]: http://i.imgur.com/p3u3ehU.png
[doc-image]: http://i.stack.imgur.com/erf8e.png
[dev-image]: http://i.stack.imgur.com/D9G2G.png
[issues-image]: http://i.stack.imgur.com/K4fGd.png
[contact-image]: http://i.stack.imgur.com/tCbmW.png
