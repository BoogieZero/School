package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TC04020502 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = " http://oks.kiv.zcu.cz/OsobniCislo/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testTC04020502() throws Exception {
    driver.findElement(By.id("generovani")).click();
    // TC.04.02.05.02 Kombinovaná
    new Select(driver.findElement(By.id("fakulta"))).selectByVisibleText("FZS - Fakulta zdravotnických studií");
    driver.findElement(By.id("rok_nastupu")).clear();
    driver.findElement(By.id("rok_nastupu")).sendKeys("20");
    driver.findElement(By.id("poradove_cislo")).clear();
    driver.findElement(By.id("poradove_cislo")).sendKeys("0101");
    driver.findElement(By.id("forma_kombinovana")).click();
    driver.findElement(By.id("generovani")).click();
    try {
      assertTrue(driver.findElement(By.id("label_vysledek")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Z20B0101K", driver.findElement(By.id("vysledek")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}