package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TC040401 {
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
  public void testTC040401() throws Exception {
    driver.findElement(By.linkText("Generování")).click();
    // TC.04.04.01 Vymazání kompletního formuláře
    new Select(driver.findElement(By.id("fakulta"))).selectByVisibleText("FAV - Fakulta aplikovaných věd");
    driver.findElement(By.id("rok_nastupu")).clear();
    driver.findElement(By.id("rok_nastupu")).sendKeys("16");
    driver.findElement(By.id("poradove_cislo")).clear();
    driver.findElement(By.id("poradove_cislo")).sendKeys("0123");
    driver.findElement(By.id("forma_prezencni")).click();
    driver.findElement(By.id("zahranicni")).click();
    driver.findElement(By.id("generovani")).click();
    driver.findElement(By.id("mazani")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [getSelectedValue | id=fakulta | ]]
    try {
      assertEquals("08", driver.findElement(By.id("rok_nastupu")).getAttribute("placeholder"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // ERROR: Caught exception [ERROR: Unsupported command [getSelectedValue | id=typ_studia | ]]
    try {
      assertEquals("0001", driver.findElement(By.id("poradove_cislo")).getAttribute("placeholder"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertFalse(driver.findElement(By.id("forma_prezencni")).isSelected());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertFalse(driver.findElement(By.id("forma_kombinovana")).isSelected());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertFalse(driver.findElement(By.id("forma_distancni")).isSelected());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertFalse(driver.findElement(By.id("zahranicni")).isSelected());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertFalse(driver.findElement(By.id("label_vysledek")).isDisplayed());
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
