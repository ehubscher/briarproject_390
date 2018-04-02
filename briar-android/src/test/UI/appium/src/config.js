module.exports = {
    port: 4723,
    desiredCapabilities: {
      platformName: "Android",
      platformVersion: "8.0",
      deviceName: "Android Emulator",
      app: "..\\..\\..\\..\\..\\build\\outputs\\apk\\debug\\briar-android-debug.apk",
      automationName: "UiAutomator2"
    }
};