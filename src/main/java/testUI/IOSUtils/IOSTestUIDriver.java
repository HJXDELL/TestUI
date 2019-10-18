package testUI.IOSUtils;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIDriver;
import testUI.Utils.TestUIException;

import java.net.MalformedURLException;
import java.net.URL;

import static testUI.Configuration.getUsePort;
import static testUI.IOSUtils.IOCapabilities.setIOSCapabilities;
import static testUI.TestUIDriver.*;
import static testUI.TestUIDriver.getDriver;
import static testUI.UIUtils.putLog;
import static testUI.Utils.AppiumHelps.sleep;

public class IOSTestUIDriver {
    protected static void startFirstIOSDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            try {
                putLog("Starting appium driver...");
                if (getDrivers().size() == 0) {
                    TestUIDriver.setDriver(new IOSDriver(
                                    new URL(url), desiredCapabilities) {});
                } else {
                    TestUIDriver.setDriver(new IOSDriver(
                                    new URL(url), desiredCapabilities) {}, 0);
                }
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new TestUIException(
                            "Could not create driver! check that the devices are " +
                                    "correctly connected and in debug mode",
                            e);
                }
            }
        }
    }

    protected static void startFirstIOSBrowserDriver(String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            DesiredCapabilities cap = setIOSCapabilities(true);
            try {
                putLog("Starting appium driver...");
                if (getDrivers().size() == 0) {
                    TestUIDriver.setDriver(
                            new IOSDriver(new URL(url), cap) {});
                } else {
                    TestUIDriver.setDriver(
                            new IOSDriver(
                                    new URL(url), cap) {}, 0);
                }
                Configuration.driver = 1;
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startBrowserIOSDriver(
            DesiredCapabilities desiredCapabilities, String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(getUsePort().size() - 1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            try {
                TestUIDriver.setDriver(new IOSDriver(
                        new URL(url), desiredCapabilities) {});
                Configuration.driver = getDrivers().size();
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }
}