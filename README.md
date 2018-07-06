# Key Promoter X Plugin - For ![logo][logo-image] development

[![Join the chat at https://gitter.im/IntelliJ-Key-Promoter-X/Lobby](https://badges.gitter.im/IntelliJ-Key-Promoter-X/Lobby.svg)](https://gitter.im/IntelliJ-Key-Promoter-X/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/halirutan/IntelliJ-Key-Promoter.svg?branch=KeyPromoterX)](https://travis-ci.org/halirutan/IntelliJ-Key-Promoter)

![Teaser](http://i.imgur.com/2zBdMT8.gif)

This is an almost completely rewritten version of the original Key Promoter plugin by Dmitry Kashin. 
The *Key Promoter X Plugin* is intended to show hints when using the mouse
for something that can be done with a keyboard shortcut.
This is crucial to replace tedious mouse interaction if you intend to become efficient IntelliJ IDEA developer.

## ![Docs][doc-image] Features and Documentation

### Features

- non-intrusive notifications about shortcuts using IDEA's built-in message framework
- easy shortcut customization for buttons by active links in the notification
- easy creation of shortcuts for buttons that don't have one
- a persistent hit-list of missed shortcuts as IDEA tool-window
- list of suppressed tips for certain shortcuts you don't want to use

## Installation

The plugin can be installed with *Settings* -> *Plugins* -> *Browse Repositories* and no further set up is required. When a button is
clicked with the mouse, a notification pops up that shows the shortcut which can be used instead. If a button has no shortcut and
is pressed several times, a notification is shown that lets you easily create a shortcut for this action.

## Documentation

### How does it work?

If you click something with the mouse, the plugin will try to find out if your mouse-click invoked some IDEA action. If this is the case
and we can find a so-called `ActionID`, the plugin registers this event. If your action is already connected to a shortcut, the plugin will
show you how the invoked action is called (e.g. Open Settings) and what the shortcut for the action is. 

![Notification](doc/img/notification.png)

Additionally, it will save your mouse-click
in the Key Promoter X statistics tool-windows so that you see which actions you are using the most and which shortcuts you should learn first.

If the action is not connected to a shortcut but has an `ActionID` (which basically means we can assign a shortcut), the plugin still registers
your click. Depending on your settings under *Settings* -> *Tools* -> *Key Promoter X*, you will get a notification every x clicks on the
same action that asks you if you want to create a shortcut for this.

Be aware that there are certain mouse-clicks that, although they do something, cannot be successfully inspected and the plugin won't be able
to help you with those. This for instance happens for some of the buttons in the tool-windows.

### The *Key Promoter X* tool-window

The Key Promoter X comes with built in tool-window on the right side that gives you access to your hit-list of missed shortcuts and to the
the list of suppressed items.

![Tool-window](doc/img/tool-window.png)

Every mouse click that is connected to an action with a shortcut will be registered in the statistic. The items in this list are ordered by
 how often you missed this shortcut. There might be some mouse actions that you don't want to replace by its shortcut. For those, you can
 press *Don't show again* in the notification and all suppressed shortcuts will appear in the list below the statistics.

You can re-activate a suppressed item by double-clicking on it in the suppressed list.

### Settings

 Settings for the plugin can be found under *Settings* -> *Tools* -> *Key Promoter X*.

![Settings Panel](doc/img/settings.png)

The first box lets you adjust how many *Key Promoter X* notifications are allowed to be shown at the same time. If you have set this to a
number, say 3, and you are a heavy clicker, then each new notification will make the oldest disappear so that only 3 messages are shown
at max.

With the second spinner, you can adjust whether the Key Promoter X should display a notification each time you press
a button with the mouse that has a key combination available.
If you adjust this setting to 2, only every second click will show a notification, etc.

The last spinner lets you adjust how often you have to click a button with no shortcut before you see a message suggesting to create
a shortcut for it.

In the *Enabled for* box it is possible to adjust which buttons are allowed to show a tip. Note that the *All Buttons* checkbox will try
to catch as many mouse-clicks as possible, even if you are not pressing a real button. This will show you for instance a tip when you
hold Ctrl and press on a Java method to jump to its declaration which can be replaced by Ctrl+B.


## ![dev image][dev-image] Development  [![Build Status](https://travis-ci.org/halirutan/IntelliJ-Key-Promoter.svg?branch=KeyPromoterX)](https://travis-ci.org/halirutan/IntelliJ-Key-Promoter)

The plugin is written in Java using IntelliJ's plugin framework. The code-base is extremely small and contains only a handful of well documented classes. The code is hosted on GitHub and has a [Travis-CI](https://travis-ci.org/) integration for automatic building. Compilation is done with Gradle using the IntelliJ Gradle plugin and should work out of the box with a recent (v3.5) version.

## ![bug image][issues-image] Reporting issues

If you experience bugs or weird behavior please create an issue [on the bug tracker](https://github.com/halirutan/IntelliJ-Key-Promoter/issues).


## ![contact image][contact-image] Credits

The initial version was [implemented by
Dmitry Kashin](https://code.google.com/p/key-promoter/)
who unfortunately stopped maintaining the code and pushed the
last version in 2012, more than 6 years ago.

[User athiele](https://github.com/athiele/key-promoter-fork/commits/master)
took the time to fork the original code, fix issues and provide
a version that can be used on recent IDEA versions.

The Key Promoter X logo bases on a mouse icon from [Zlatko Najdenovski](http://www.flaticon.com/authors/zlatko-najdenovski) found on [www.flaticon.com](http://www.flaticon.com/free-icon/mouse_181831).

[logo-image]: http://i.imgur.com/p3u3ehU.png
[doc-image]: http://i.stack.imgur.com/erf8e.png
[dev-image]: http://i.stack.imgur.com/D9G2G.png
[issues-image]: http://i.stack.imgur.com/K4fGd.png
[contact-image]: http://i.stack.imgur.com/tCbmW.png
