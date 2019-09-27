package testUI.AndroidUtils;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIConfiguration;

import static testUI.TestUIDriver.getDesiredCapabilities;
import static testUI.UIUtils.*;
import static testUI.UIUtils.putAllureParameter;

public class AndroidCapabilities extends Configuration {
    private static ADBUtils adbUtils = new ADBUtils();

    public static DesiredCapabilities setAppAndroidCapabilities(TestUIConfiguration configuration) {
        if (configuration.getEmulatorName().isEmpty()
                && !adbUtils.getDeviceStatus(getDevice()).equals("device")) {
            System.err.println("The device status is " + adbUtils.getDeviceStatus(getDevice()) +
                    " to use usb, you must allow usb debugging for this device: " + getDevice());
            throw new Error();
        }
        getDevModel(configuration);
        String deviceVersion = Configuration.androidVersion.isEmpty() &&
                configuration.getEmulatorName().isEmpty() ?
                adbUtils.getDeviceVersion(getDevice()) : Configuration.androidVersion;
        // Created object of DesiredCapabilities class.
        DesiredCapabilities cap = new DesiredCapabilities();
        if (getDesiredCapabilities() == null) {
            if (configuration.getEmulatorName().isEmpty()) {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, getDevice());
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceVersion);
            } else {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME,
                        configuration.getEmulatorName());
                cap.setCapability(AndroidMobileCapabilityType.AVD, configuration.getEmulatorName());
            }
            cap.setCapability(AndroidMobileCapabilityType.APP_WAIT_DURATION,
                    Configuration.launchAppTimeout);
            if (Configuration.AutomationName.isEmpty()) {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            } else {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, Configuration.AutomationName);
            }
            if (!Configuration.chromeDriverPath.isEmpty()) {
                String chromePath = Configuration.chromeDriverPath.charAt(0) == '/'
                        ? Configuration.chromeDriverPath
                        : System.getProperty("user.dir") + "/" + Configuration.chromeDriverPath;
                cap.setCapability(AndroidMobileCapabilityType.CHROMEDRIVER_EXECUTABLE, chromePath);
            }
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            if (!Configuration.appActivity.isEmpty() && !Configuration.appPackage.isEmpty()) {
                cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                        Configuration.appActivity);
                cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                        Configuration.appPackage);
            }
            if (!Configuration.androidAppPath.isEmpty()) {
                String appPath = Configuration.androidAppPath.charAt(0) == '/'
                        ? Configuration.androidAppPath
                        : System.getProperty("user.dir") + "/" + Configuration.androidAppPath;
                cap.setCapability("androidInstallPath", appPath);
                cap.setCapability("app", appPath);
            }
            int systemPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 10;
            cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
        } else {
            cap = getDesiredCapabilities();
        }
        // ADD CUSTOM CAPABILITIES
        if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
            for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                cap.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
            }
            addMobileDesiredCapabilities = new DesiredCapabilities();
        }
        Configuration.desiredCapabilities = cap;
        return cap;
    }

    public static DesiredCapabilities setAndroidBrowserCapabilities(
            TestUIConfiguration configuration) {
        if (configuration.getEmulatorName().isEmpty() && getDevices().size() == 0) {
            throw new Error("There is no device available to run the automation!");
        }
        if (configuration.getEmulatorName().isEmpty()
                && !adbUtils.getDeviceStatus(getDevice()).equals("device")) {
            System.err.println("The device status is " + adbUtils.getDeviceStatus(getDevice()) +
                    " to use usb, you must allow usb debugging for this device: " + getDevice());
            throw new Error();
        }
        getDevModel(configuration);
        String deviceVersion = Configuration.androidVersion.isEmpty() &&
                configuration.getEmulatorName().isEmpty() ?
                adbUtils.getDeviceVersion(getDevice()) :
                Configuration.androidVersion;
        String browserFirstLetter = Configuration.browser.subSequence(0, 1).toString().toUpperCase();
        String browser = browserFirstLetter + Configuration.browser.substring(1);
        // Created object of DesiredCapabilities class.
        DesiredCapabilities cap = new DesiredCapabilities();
        if (!configuration.getChromeDriverPath().isEmpty()) {
            String chromePath = configuration.getChromeDriverPath().charAt(0) == '/'
                    ? configuration.getChromeDriverPath()
                    : System.getProperty("user.dir") + "/" + configuration.getChromeDriverPath();
            cap.setCapability(AndroidMobileCapabilityType.CHROMEDRIVER_EXECUTABLE, chromePath);
        }
        if (getDesiredCapabilities() == null) {
            if (configuration.getEmulatorName().isEmpty()) {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, getDevice());
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceVersion);
            } else {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME,
                        configuration.getEmulatorName());
                cap.setCapability(AndroidMobileCapabilityType.AVD, configuration.getEmulatorName());
            }
            if (Configuration.AutomationName.isEmpty()) {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            } else {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                        Configuration.AutomationName);
            }
            int systemPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 10;
            int chromeDriverPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 15;
            cap.setCapability("chromeDriverPort", chromeDriverPort);
            cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
            cap.setCapability(MobileCapabilityType.NO_RESET, true);
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            cap.setCapability(MobileCapabilityType.BROWSER_NAME, browser);
            cap.setCapability(AndroidMobileCapabilityType.NATIVE_WEB_SCREENSHOT, true);
        } else {
            cap = getDesiredCapabilities();
        }
        // ADD CUSTOM CAPABILITIES
        if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
            for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                cap.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
            }
            addMobileDesiredCapabilities = new DesiredCapabilities();
        }
        Configuration.desiredCapabilities = cap;
        return cap;
    }

    private static void getDevModel(TestUIConfiguration configuration) {
        String devModel;
        if (configuration.getEmulatorName().isEmpty()) {
            devModel = (getDeviceName().equals(getDevice()) ?
                    adbUtils.getDeviceModel(getDevice()) : getDeviceName());
        } else {
            if (Configuration.driver == 1) {
                Configuration.firstEmulatorName.set(configuration.getEmulatorName());
            }
            devModel = configuration.getEmulatorName();
        }
        if (Configuration.driver == 1 && Configuration.firstEmulatorName.get() != null) {
            putAllureParameter("Device Model", Configuration.firstEmulatorName.get());
        } else {
            putAllureParameter("Device Model", devModel);
        }
    }
}
