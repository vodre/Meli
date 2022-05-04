# Mercado Libre - MeLi
MeLi Code Challenge

## Screenshots

![ezgif com-gif-maker](https://user-images.githubusercontent.com/7502465/166641588-d3dcf221-63f9-4651-ad0e-69ca27c761dd.gif)

# Features
The functionality presents solutions to the different scenarios required to have the functionality.

#### [Timber](https://github.com/JakeWharton/timber).
Logger with a small, extensible API which provides utilities on Android’s normal Log. class.

#### [Cycler](https://github.com/square/cycler). 
API that allows to simplify the RecyclerView implementation.

###FragmentViewBindingDelegate
Eliminates boilerplate code when is about implementing view models.

#### [Retrofit](https://github.com/square/retrofit). 
A type-safe HTTP client for Android and the JVM.

#### [Lottie](https://github.com/airbnb/lottie-android). 
Loading animations from a json file.

#### [Coil](https://github.com/coil-kt/coil). 
Image loading for Android backed by Kotlin Coroutines.


## App Architecture
<img width="634" alt="Screen Shot 2022-03-21 at 15 12 07" src="https://user-images.githubusercontent.com/7502465/159364622-83b48229-0800-40b6-9fbe-dd59977b18d9.png">

## MVI - Model View Intend with Kotlin Flow
Model View Intend builds upon Unidirectional Data Flow, better organizing the UI and business logic into view states and creating a clear contract between UI and business logic with an interface.

- Ui Modularization : Define the final UI results emitted and rendered in an interface contract and view state.
- Scaling: Reuse code with clear organization.
- Unit test simply: Build tests by observing the existing view state results from the business logic.
- Kotlin Flow: Quick setup, lifecycle management, threading management, avoid nesting, open for customization and simple implementation.

<img width="686" alt="Screen Shot 2022-03-09 at 16 21 40" src="https://user-images.githubusercontent.com/7502465/157547814-5fcea9af-6572-4ce3-b332-980346b29bb4.png">



### Android tools
- ViewBinding
- ConstraintLayout
- Google Playservices
- Android Coroutines
- Flows
- Kotlin
- Hilt
- Services
- Kotlin Extensions
- ViewModel
- Custom Views (Text Inputs)
- Lint


### Improvements
- Unit Testing (not enought time to complete this)
- Improvements on Network Aware Screen/Retry
- Navigation improvements
