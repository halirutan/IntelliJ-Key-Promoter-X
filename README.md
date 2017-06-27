# Key Promoter X Plugin - For ![logo][logo-image] development

![Teaser](http://i.imgur.com/2zBdMT8.gif)

This is an almost completely rewritten version of the original Key Promoter plugin by Dmitry Kashin. 
The *Key Promoter X Plugin* is intended to show hints when using the mouse for something that can be done with a keyboard shortcut. This is crucial to replace tedious mouse interaction if you intend to become efficient IntelliJ IDEA developer.

## ![Docs][doc-image] Features and Documentation

The plugin can be installed with *Settings* -> *Plugins* -> *Browse Repositories* and no further set up is required. When a button is clicked with the mouse, a notification pops up that shows the shortcut which can be used instead. If a button has no shortcut and is pressed several times, a notification is shown that lets you easily create a shortcut for this action.

The Key Promoter X comes with built in tool-window on the left side that gives you access to your hit-list of missed shortcuts. Settings for the plugin can be found under *Settings* -> *Tools* -> *Key Promoter X*.

### Features

- non-intrusive notifications about shortcuts using IDEA's built-in message framework
- easy shortcut customization for buttons by active links in the notification
- easy creation of shortcuts for buttons that don't have one
- a persistent hit-list of missed shortcuts as IDEA tool-window


## ![dev image][dev-image] Development  [![Build Status](https://travis-ci.org/halirutan/IntelliJ-Key-Promoter.svg?branch=KeyPromoterX)](https://travis-ci.org/halirutan/IntelliJ-Key-Promoter)

The plugin is written in Java using IntelliJ's plugin framework. The code-base is extremely small and contains only a handful of well documented classes. The code is hosted on GitHub and has an [Travis-CI](https://travis-ci.org/) integration for automatic building. Compilation is done with Gradle using the IntelliJ Gradle plugin and should work out of the box with a recent (v3.5) version.

## ![bug image][issues-image] Reporting issues

If you experience bugs or weird behavior please create an issue [on the bug tracker](https://github.com/halirutan/IntelliJ-Key-Promoter/issues).

## Known issues

- On OSX it is currently not possible to catch menu mouse clicks 


## ![contact image][contact-image] Credits, Contact and Licensing

The initial version was [implemented by
Dmitry Kashin](https://code.google.com/p/key-promoter/)
who unfortunately stopped maintaining the code and pushed the
last version in 2012, more than 5 years ago.

[User athiele](https://github.com/athiele/key-promoter-fork/commits/master)
took the time to fork the original code, fix issues and provide
a version that can be used on recent IDEA versions.

The Key Promoter X logo bases on a mouse icon from [Zlatko Najdenovski](http://www.flaticon.com/authors/zlatko-najdenovski) found on [www.flaticon.com](http://www.flaticon.com/free-icon/mouse_181831).

[logo-image]: http://i.imgur.com/p3u3ehU.png
[doc-image]: http://i.stack.imgur.com/erf8e.png
[dev-image]: http://i.stack.imgur.com/D9G2G.png
[issues-image]: http://i.stack.imgur.com/K4fGd.png
[contact-image]: http://i.stack.imgur.com/tCbmW.png
