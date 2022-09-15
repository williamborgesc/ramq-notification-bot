package com.github.ramq.notification.bot.service;

import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Deprecated
public class SeleniumBookingService {
    static {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @SneakyThrows
    public boolean checkAvailability() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://outlook.office365.com/owa/calendar/RAMQ_Bureau_QC@azqmar.onmicrosoft.com/bookings/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("serviceSection")));
        List<WebElement> options = driver.findElements(By.className("serviceCard"));

        Thread.sleep(1000 * 5);

        WebElement option = options.stream()
                .filter(webElement -> webElement.getText().contains("Pays sans entente : 1re inscription, retour au Québec"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("N achou a parada entre as opcpoes: " + options));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("servicePicker")));
        wait.until(ExpectedConditions.visibilityOf(option)).click();

        try {
            return driver.findElementByClassName("bookable").isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        } finally {
            driver.close();
        }
    }
}
