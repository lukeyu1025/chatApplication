# Realtime Chat App for Android

This repository contains a lightweight chat client written in Kotlin. It uses Firebase Authentication, Realtime Database and Storage so users can register, upload a profile picture and exchange messages instantly. The project is intended as a concise example of basic Firebase integration on Android.

## Features

- Email and password authentication
- User registration with profile image upload
- Password reset via email
- Real-time messaging using Firebase Realtime Database
- User profile editing
- Emoji picker and image placeholder in chats
- Basic settings screen

## Project Structure

```
chatApplication/
├── app/                # Android application module
│   ├── src/main/java/  # Kotlin source code
│   ├── src/main/res/   # Layouts and resources
│   └── ...
├── build.gradle        # Top level Gradle build file
├── gradle/             # Gradle wrapper
└── settings.gradle
```

## Building and Running

1. Install **Android Studio** or ensure you have the Android SDK and JDK 8+
   available.
2. Clone this repository.
3. Replace `app/google-services.json` with your own Firebase configuration if
   you want to connect to a different project.
4. From the project root run:

```bash
./gradlew assembleDebug
```

This will compile the app. You can also open the project in Android Studio and
use the Run button to install it on an emulator or device.

## Firebase Setup

The app uses Firebase Authentication, Realtime Database, and Storage. The
current configuration relies on the included `google-services.json` file. If you
fork the project or create a new Firebase project, generate a new
`google-services.json` and place it under `app/`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to
discuss what you would like to change.

## License

This project is provided as-is without a specific license. Feel free to modify
and use it for your own educational purposes.
