📍 **Locato: Smart Location Picker App**


🚀 **Overview**

**Locato** is a simple, modern Android application that allows users to select their current location either automatically using GPS or manually using a map interface. Once selected, the location is submitted to a dummy API endpoint in JSON format. This app is ideal for learning how to use location services, maps, and permissions in a clean MVVM architecture.


🛠️ **Features**

✅ Auto Location Detection – Get your current location using GPS with one tap.

✅ Manual Location Picker – Select a location by tapping anywhere on the interactive Google Map.

✅ Google Maps SDK Integration – Leverage the Maps SDK for a smooth, interactive map experience.

✅ Location Submission – Send the selected location (latitude & longitude) as a POST request to a predefined API.

✅ MVVM Architecture – Cleanly structured app with ViewModel and State Management.

✅ Permissions Handling – Uses Accompanist library to request and handle runtime location permissions.

✅ Toast Feedback – Instantly notify users of actions like location selection or submission status.


📂 **Tech Stack**

✨ **Kotlin + Jetpack Compose** – Modern UI and logic handling

📝 **MVVM Architecture** – Clean separation of concerns

📅 **Google Maps SDK** – Smooth map interaction and marker placement

📲 **Android Permissions API** + Accompanist – Runtime permissions management

🚀 **Retrofit** – For sending location to API (or use URLConnection for simplicity)


📊 **How It Works**

1. ✅ **Get Current Location** – Tap the button to fetch location via GPS.
2. 🌍 **Select on Map** – Open map and tap to place a marker.
3. 🚊 **View Location** – Selected latitude and longitude shown on screen.
4. ✉️ **Submit** – Send location data to dummy API endpoint.

Example payload:
```json
{
  "latitude": "23.456789",
  "longitude": "78.123456"
}
```


🚀 **Getting Started**

1. Clone the repository:
```bash
git clone https://github.com/yourusername/Locato.git
```

2. Open in Android Studio:
- Sync Gradle
- Add your **Google Maps API Key** to the `local.properties` or `AndroidManifest.xml`

3. Run the app:
- Connect an Android device or emulator
- Hit Run ▶️


🤝 **Contributions**

We welcome contributions! Feel free to fork the repo, open issues, or submit pull requests to improve the project.

---

📆 **About**

Locato – Simple, Intuitive & Useful Location Picker for Android Devs 🚀🌍

