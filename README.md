# 📱 MangaVision – Manga & Face Detection App

**MangaVision** is a modular Android application built with modern development standards. It integrates Jetpack Compose UI, Clean Architecture, and MVVM pattern, supporting dynamic features like real-time face recognition and API-based manga listing. The app emphasizes performance, offline support, and robust user session handling with a seamless user experience.

## ✨ Tech Stack

* **Jetpack Compose** – Modern UI Toolkit
* **MVVM + Clean Architecture** – Scalable code structure
* **Single Activity Architecture** – Jetpack Navigation Component
* **Room Database** – Local persistence for user and manga data
* **MediaPipe** – Real-time face detection
* **Firebase Crashlytics** – Real-time crash monitoring
* **Firebase Realtime Database** – API usage tracking and rate-limiting
* **Kotlin + Coroutines + Flow** – Asynchronous and reactive programming
* **Hilt** – Dependency Injection
* **Modularization** – Core, App, and Feature-based modules


## 🧩 Architecture Overview

The app is modularized into:

* **`app`** – Entry point and dependency initializer
* **`core`** – Reusable utilities: UI, themes, constants, extensions
* **`feature`** – Contains independent modules: `auth`, `manga`, `face_recognition`, `profile`

Each module adheres to **MVVM** with clearly separated layers for UI, Domain, and Data, enabling dynamic feature delivery, testability, and easier maintainability.


## 🔐 User Authentication

* Users sign in with email and password using a local Room database.
* If an account doesn't exist, it's created automatically.
* Session persistence ensures users remain logged in unless explicitly logged out.


## 📚 Manga Listing & Caching

* Manga data is fetched from the MangaVerse API.
* Supports pagination for efficient scrolling.
* Offline access via Room caching.
* Data auto-syncs when the network is restored.
* Detailed view screen for each manga.


## 🧠 Real-Time Face Detection

* Integrated MediaPipe face detection using the front camera.
* Displays a live camera feed with a reference rectangle.
* The rectangle turns **green** if the user's face is correctly positioned, otherwise **red**.
* Camera permission toggle and state handling included.


## 👤 User Profile & Logout

* Additional profile screen displaying logged-in user info.
* Secure logout functionality with session clearing.


## 📊 Analytics & API Rate Limiting

* **Firebase Crashlytics** provides real-time crash reporting.
* **Firebase Realtime Database** tracks API request count per day.
* Enforces a soft limit of **100 API calls/day**


## 📷 Screenshots & Demo

Screenshots of all key screens:

1. Sign In
   ![WhatsApp Image 2025-05-06 at 19 07 29_0d81689e](https://github.com/user-attachments/assets/a0088a17-4643-4b14-9494-536ca81c1a0e)
2. Sign Up
   ![WhatsApp Image 2025-05-06 at 19 07 29_5047ed6f](https://github.com/user-attachments/assets/52aeebca-36fc-4a8d-9117-35e7831e588b)
4. Home (Bottom Navigation)
   ![WhatsApp Image 2025-05-06 at 19 07 29_cb32babe](https://github.com/user-attachments/assets/fc262e4b-4e19-492c-acaf-9376ce75d653)
6. Manga Details
   ![WhatsApp Image 2025-05-06 at 19 17 05_d1f4d527](https://github.com/user-attachments/assets/fa0f4de5-4a95-48b4-8fd5-8d011e2f6f84)
8. Face Detection
   Face Detected:
     ![WhatsApp Image 2025-05-06 at 19 14 48_466d9105](https://github.com/user-attachments/assets/be64cdcb-0406-421a-8978-3cf5fabe1b8a)
   Face Not Detected:
     ![WhatsApp Image 2025-05-06 at 19 14 49_068ad201](https://github.com/user-attachments/assets/426371f1-60d1-4bd1-9656-b5b4e125df59)




## 🚀 Getting Started

1. Clone the repo:
   `git clone https://github.com/rohitjoshi1231/MangaVision.git`
2. Open in Android Studio.
3. Add your **Firebase config files**.
4. Build and run the app.


📦 APK & Submission
✅ Download APK from: /app/release/app-release.apk

✅ Demo video and screenshots attached.

📹 A video demo showcasing the app's core features is available in the repository.
Demo video: /app/demo/demo-video.mp4
