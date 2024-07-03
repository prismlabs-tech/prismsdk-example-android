# Prism Reference App for Android

The Prism Reference app demos the entire end-to-end flor of capturing a scan and viewing the details.
Use this as a dempo of the full features of the SDK for Android.

# Getting Started

Before getting started, make sure you have the latest version of Android Studio installed. At the time of writing this, that is `Android Studio Iguana | 2023.2.1`

## Prerequisites

- Android Studio Iguana | 2023.2.1 or Later
- Compatible Android Device
- PrismSDK Cloned down locally

### PrismSDK

This is a required local dependency to compile the Reference App. To speed up the development process, you must clone the `prismsdk-android` repo into the same root folder. This should looke something like this:

```sh
/Path/To/ParentFolder/PrismReference-Android
```

and

```sh
/Path/To/ParentFolder/prismsdk-android
```

Notice how both of these folders are contained within the `ParentFolder`. This allows Android Studio to look for the SDK and allows you to work inside of it and compile it. More information on the PrismSDK can be found inside of its repo.

## Building and Running

In order to get the app compiling and running on your device. You must enable Developer Mode first. This is done different for each android device, so you may need to result to the manufacturers website or a google search for your specific device.

Once you enable developer mode, open up the Android Studio Project and select your device. Clicking the Play button will then configure the app and run it on your device.

If yorue having issues with getting it to run, you may need to sync your gradle file to trigger a fresh download of all packages needed.

# Platform Configuration

Currently, the Android app is configured for dev, but there are 3 flavors of the app.

- Dev
- Stage
- Production

You can update the API URL and API Key inside of the `build.gradle` file under the `productFlavors` section. From there you can update things accordingly.

# Distribution

Currently Firebase is the only service we have configured to share the app. In order to get things deployed, you have to compile the `apk` in Android Studio first, then upload it to Firebase. You can compile the APK by simply navigating to the `Build` menu item, then choosing `Build Bundle(s) / Build APK(s)` -> `Build APK(s)`. This will compile a distributable binary that you can use to upload.

One the compiling is complete, a little notification will appear in Android Studio tell you it was successful. From there, you can tap the `Locate` button and it will open the file path in finder.
