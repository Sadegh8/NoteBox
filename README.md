# NoteBox 📝

**NoteBox** is a secure, offline-first notes management application built with **Kotlin Multiplatform (KMP)**. It demonstrates the power of sharing a single codebase across Android, Desktop (JVM), and iOS while maintaining platform-specific capabilities like native file system access and adaptive layouts.

---

## 📱 Demos

| Android (Mobile) | Desktop (Adaptive Window) |
|:---:|:---:|
| <br> *Shows secure list/detail navigation* | <br> *Shows adaptive resizing & system file picker* |

---

## 🚀 Features

- **Cross-Platform**: Runs natively on Android, Desktop, and iOS sharing ~90% of code.
- **Secure Storage**: 
  - **Android**: Database encrypted with **SQLCipher** (256-bit AES).
  - **Local-First**: No internet required; all data lives on the device.
- **Adaptive UI**: Uses **Material 3 Adaptive** (`ListDetailPaneScaffold`) to automatically switch between a list-view (mobile) and split-view (tablet/desktop) based on window size.
- **Data Export**:
  - **Android**: Exports notes to Downloads via Scoped Storage (MediaStore API on Android 10+).
  - **Desktop**: Native System File Picker (AWT) for saving files.
- **Dynamic Theming**: Auto-detects system Light/Dark mode.

---

## 🛠 Tech Stack & Architecture

We leverage the absolute latest in the Kotlin ecosystem to ensure scalability, testability, and performance.

### Architecture
The project follows **Clean Architecture** principles with **SOLID** design patterns, separating concerns into distinct layers:
- **Domain Layer**: Pure Kotlin code containing business logic (UseCases) and models. Completely independent of frameworks.
- **Data Layer**: Handles data sourcing (Room Database) and repository implementations.
- **Presentation Layer**: **MVVM** pattern using Compose Multiplatform for UI and Koin for ViewModel injection.

### Libraries & Tools
- **Language**: [Kotlin 2.0+](https://kotlinlang.org/)
- **UI Framework**: [Compose Multiplatform 1.7+](https://www.jetbrains.com/lp/compose-multiplatform/)
  - **Adaptive Layouts**: `androidx.compose.material3.adaptive` for responsive design.
  - **Navigation**: Jetpack Compose Navigation (Type-safe).
- **Dependency Injection**: [Koin](https://insert-koin.io/) (Native support for KMP).
- **Database**: [Room Multiplatform 2.8+](https://developer.android.com/jetpack/androidx/releases/room) with SQLite Bundled.
- **Asynchrony**: Kotlin Coroutines & Flow.

---

## 🔒 Security & Limitations

We prioritize transparency in our security architecture:

- **Android**: Fully encrypted. We use a custom `SupportOpenHelperFactory` with **SQLCipher** to ensure data at rest is unreadable without the passphrase.
- **Desktop**: Currently uses **Standard SQLite (Unencrypted)**. 
  - *Note*: The official Room Multiplatform `BundledSQLiteDriver` does not yet support SQLCipher for JVM targets out of the box. 
  - *Mitigation*: The desktop target is currently intended for developer testing or non-sensitive data. In a production desktop release, we would implement a custom driver wrapper for SQLCipher or application-level field encryption.

---

## 🏗 Build & Run

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
