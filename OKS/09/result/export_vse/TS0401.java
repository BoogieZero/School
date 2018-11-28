package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TS0401 {
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
  public void testTS0401() throws Exception {
    driver.findElement(By.linkText("Generování")).click();
    try {
      assertTrue(driver.findElement(By.id("paticka_hlavni_panel")).getText().matches("^Údaje označené [\\s\\S]* jsou povinné\\.$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.01 Fakulta
    try {
      assertEquals("--- neuvedeno ---", driver.findElement(By.id("fakulta_NA")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FAV - Fakulta aplikovaných věd", driver.findElement(By.id("fakulta_FAV")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FDU - Fakulta designu a umění Ladislava Sutnara", driver.findElement(By.id("fakulta_FDU")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FEK - Fakulta ekonomická", driver.findElement(By.id("fakulta_FEK")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FEL - Fakulta elektrotechnická", driver.findElement(By.id("fakulta_FEL")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FF - Fakulta filosofická", driver.findElement(By.id("fakulta_FF")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FPE - Fakulta pedagogická", driver.findElement(By.id("fakulta_FPE")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FPR - Fakulta právnická", driver.findElement(By.id("fakulta_FPR")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FST - Fakulta strojní", driver.findElement(By.id("fakulta_FST")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("FZS - Fakulta zdravotnických studií", driver.findElement(By.id("fakulta_FZS")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_fakulta")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_fakulta")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_fakulta")).getText().matches("^Fakulta[\\s\\S]*:$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("fakulta")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("fakulta")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // ERROR: Caught exception [ERROR: Unsupported command [getSelectedValue | id=fakulta | ]]
    try {
      assertEquals("Vyberte fakultu", driver.findElement(By.id("label_fakulta")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vyberte fakultu", driver.findElement(By.id("fakulta")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.02 Rok nástupu na ZČU
    try {
      assertTrue(driver.findElement(By.id("label_rok_nastupu")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_rok_nastupu")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_rok_nastupu")).getText().matches("^Rok nástupu na ZČU[\\s\\S]*:$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("rok_nastupu")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("rok_nastupu")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // ERROR: Caught exception [ERROR: Unsupported command [isEditable | id=rok_nastupu | ]]
    try {
      assertEquals("08", driver.findElement(By.id("rok_nastupu")).getAttribute("placeholder"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zadejte poslední dvojčíslí kalendářního roku", driver.findElement(By.id("label_rok_nastupu")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zadejte poslední dvojčíslí kalendářního roku", driver.findElement(By.id("rok_nastupu")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.03 Typ studia
    try {
      assertTrue(driver.findElement(By.id("label_typ_studia")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_typ_studia")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_typ_studia")).getText().matches("^Typ studia[\\s\\S]*:$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("bakalářský", driver.findElement(By.id("typ_studia_B")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("navazující", driver.findElement(By.id("typ_studia_N")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("doktorský", driver.findElement(By.id("typ_studia_P")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("magisterský", driver.findElement(By.id("typ_studia_M")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("typ_studia")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("typ_studia")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // ERROR: Caught exception [ERROR: Unsupported command [getSelectedValue | id=typ_studia | ]]
    try {
      assertEquals("Vyberte typ studia", driver.findElement(By.id("label_typ_studia")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vyberte typ studia", driver.findElement(By.id("typ_studia")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.04 Pořadové číslo
    try {
      assertTrue(driver.findElement(By.id("label_poradove_cislo")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_poradove_cislo")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_poradove_cislo")).getText().matches("^Pořadové číslo[\\s\\S]*:$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("poradove_cislo")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("poradove_cislo")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // ERROR: Caught exception [ERROR: Unsupported command [isEditable | id=poradove_cislo | ]]
    try {
      assertEquals("0001", driver.findElement(By.id("poradove_cislo")).getAttribute("placeholder"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zadejte nezáporné celé číslo, max. 4 číslice", driver.findElement(By.id("label_poradove_cislo")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zadejte nezáporné celé číslo, max. 4 číslice", driver.findElement(By.id("poradove_cislo")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.05 Forma studia
    try {
      assertTrue(driver.findElement(By.id("label_forma_studia")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_forma_studia")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_forma_studia")).getText().matches("^Forma studia[\\s\\S]*:$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("prezenční", driver.findElement(By.id("label_forma_prezencni")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("kombinovaná", driver.findElement(By.id("label_forma_kombinovana")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("distanční", driver.findElement(By.id("label_forma_distancni")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_forma_prezencni")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_forma_kombinovana")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("label_forma_distancni")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_forma_prezencni")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_forma_kombinovana")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_forma_distancni")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("forma_prezencni")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("forma_kombinovana")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("forma_distancni")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("forma_prezencni")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("forma_kombinovana")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("forma_distancni")));
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
      assertEquals("Vyberte formu studia", driver.findElement(By.id("label_forma_studia")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vyberte formu studia", driver.findElement(By.id("label_forma_prezencni")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vyberte formu studia", driver.findElement(By.id("label_forma_kombinovana")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vyberte formu studia", driver.findElement(By.id("label_forma_distancni")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.06 Zahraniční student
    try {
      assertTrue(driver.findElement(By.id("label_zahranicni")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("label_zahranicni")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zahraniční student:", driver.findElement(By.id("label_zahranicni")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("zahranicni")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(driver.findElement(By.id("zahranicni")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertFalse(driver.findElement(By.id("zahranicni")).isSelected());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zaškrtněte, jedná-li se o zahraničního studenta", driver.findElement(By.id("label_zahranicni")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Zaškrtněte, jedná-li se o zahraničního studenta", driver.findElement(By.id("zahranicni")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.07 Generuj osobní číslo
    try {
      assertTrue(driver.findElement(By.id("generovani")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("generovani")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Generuj osobní číslo", driver.findElement(By.id("generovani")).getAttribute("value"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Generuj osobní číslo", driver.findElement(By.id("generovani")).getAttribute("value"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Stiskněte po zadání všech hodnot", driver.findElement(By.id("generovani")).getAttribute("title"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // TC.04.01.08 Vymaž
    try {
      assertTrue(driver.findElement(By.id("mazani")).isDisplayed());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertTrue(isElementPresent(By.id("mazani")));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vymaž", driver.findElement(By.id("mazani")).getAttribute("value"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vymaž", driver.findElement(By.id("mazani")).getAttribute("value"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("Vymazání všech hodnot", driver.findElement(By.id("mazani")).getAttribute("title"));
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
