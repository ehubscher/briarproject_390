module.exports = {
    port: 4723,
    desiredCapabilities: {
      platformName: "Android",
      platformVersion: "8.0",
      deviceName: "Android Emulator",
      app: "../briar-android-debug.apk",
      appPackage: "org.briarproject.briar.android.login",
      appActivity: "PasswordActivity",
      automationName: "UiAutomator2"
    }
};