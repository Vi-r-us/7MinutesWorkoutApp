# 7 Minutes Workout App

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.10-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-green)](https://en.wikipedia.org/wiki/MIT_License)

7-Minutes Workout Android App built using Kotlin based on modern Android application tech-stacks.

## Features
* Workout of 12 exercises (30 seconds each).
* Screen to rest after each exercise (10 seconds).
* Have a Play/Pause button for each exercise.
* Can skip and return button for the exercises.
* Play aloud the name of the exercise.
* Play a sound to indicate that rest for next exercise.
* History of past workouts.
* BMI Calculator with 2 units system (Metrics and US).

## Screenshots


## Tech Stack & Open-source libraries
* Minimum SDK level 21.
* [Kotlin](https://kotlinlang.org/) - official programming language for Android development.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - for asynchronous programming.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - collection of libraries that help you design robust, testable, and maintainable apps.
  - [Data Binding](https://developer.android.com/topic/libraries/data-binding) - data objects to bind UI components in your layouts to data sources in your app.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - stores UI-related data that isn't destroyed on UI changes. 
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - access your app's SQLite database with in-app objects and compile-time checks.
  - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - components to perform actions in response to a change in the lifecycle status of another component, such as activities and fragments.
* [Navigation](https://developer.android.com/guide/navigation) - interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
* [Fragment](https://developer.android.com/guide/components/fragments) - represents a behavior or a portion of user interface in a FragmentActivity.
* [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) - for display a scrolling list of elements based on large data sets.
* [Material-Components](https://github.com/material-components/material-components-android) - Material design components.
  - [Material Dialogs](https://github.com/afollestad/material-dialogs) - library with extensible dialogs API for Kotlin & Android.
