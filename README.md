# Foodies - Modern Android Architecture

Foodies is a sample project that presents a modern 2021 approach to Android app development. 

The project tries to combine popular Android tools and to demonstrate best developement practices by utilizing up to date tech-stack like Compose and Kotlin Flow while also presenting modern Android application Architecture that is scalable and maintainable through a MVVM blend with MVI.

## Description

<img src="misc/demo_gif_1.gif" width="336" align="right" hspace="20">

* UI 
   * [Compose](https://developer.android.com/jetpack/compose) is used as declarative UI framework for Android.
   * [Material design](https://material.io/design)

* Tech/Tools
    * [Kotlin](https://kotlinlang.org/) 100% coverage
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [Flow](https://developer.android.com/kotlin/flow) for async operations
    * [Retrofit](https://square.github.io/retrofit/) for networking
    * [Jetpack](https://developer.android.com/jetpack)
        * [Compose](https://developer.android.com/jetpack/compose) 
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) for navigation between composables
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that stores, exposes and manages UI state, events and side-effects
    * [Coil](https://github.com/coil-kt/coil) for image loading
    
* Modern Architecture
    * Single activity architecture (with [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)) that defines navigation graphs and injects ViewModel components
    * MVVM blend with MVI to expose reactive state, events and side-effects.
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation))
    * [Android KTX](https://developer.android.com/kotlin/ktx) - Jetpack Kotlin extensions


## Architecture
The project is layered traditionally with a View, Presentation and Model separation.

* View - Composable screens that consume state, apply effects and delegate events.
* ViewModel - [AAC ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that manages and reduces the state of the corresponding screen. Additionally, it intercepts UI events and produces effects. The ViewModel lifecycle scope is tied to the corresponding screen composable.
* Model - Repository classes that retrieve data. In a clean architecture context, one should use use-cases that tap into repositories.

![](https://i.imgur.com/GNA1hMa.png)

As the architecture blends MVVM with MVI, there are a three main components described:
* State - data class that holds the state content of the corresponding screen e.g. list of `FoodItem`, loading status etc.
* Event - plain object that is sent through callbacks from the UI to the presentation layer. Events should reflect UI events caused by the user.
* Effect - plain object that signals one-time side-effect actions that should impact the UI e.g. triggering a navigation action, showing a Toast, SnackBar etc.

Every screen/flow defines a contract class that states all the core components described above: state content, events and effects.

### Decoupling Compose
Since Compose is a standalone declarative UI framework, one must try to decouple it from the Android framework as much as possible. In order to achieve this, the project uses an `EntryPointActivity` that defines a navigation graph where every screen is a composable.

The `EntryPointActivity` also collects `state` flows and passes them together with the `Effect` flows to each Screen composable. This way, the Activity is coupled with the navigation component and only screen (root level) composables. This causes the screen composables to only receive and interact with plain objects and Kotlin flows, therefore being platform agnostic.  
