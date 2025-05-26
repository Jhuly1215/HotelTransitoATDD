package com.hoteltransito;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestAceptacion2 {
    private RemoteWebDriver driver;
    private Actions actions;
    private final String HOME_URL = "file:///C:\\Users\\Hp\\Documents\\HotelTransitoATDD\\front\\index.html"; //link del index del front

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        actions = new Actions(driver);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Prueba 2: Visualización y contenido de las cards de la sección “Bienvenidos”")
    public void testWelcomeCards() throws Exception {
        driver.get(HOME_URL);
        TimeUnit.SECONDS.sleep(1); // Espera breve para asegurar carga

        WebElement section = driver.findElement(
            By.xpath("//h2[text()='Bienvenidos a nuestro hotel CI/CD']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);
        TimeUnit.MILLISECONDS.sleep(500); // Espera para scroll

        // Paso 1 – Número de cards
        List<WebElement> cards = driver.findElements(By.cssSelector(".services .card"));
        Assert.assertEquals(cards.size(), 3, "Debe haber exactamente 3 cards");

        // Paso 2 – Imagen en cada card
        for (WebElement card : cards) {
            WebElement img = card.findElement(By.tagName("img"));
            Long width = (Long) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].naturalWidth;", img);
            Assert.assertTrue(width > 0, "La imagen debe cargarse sin estar rota");
        }

        // Paso 3 – Título y descripción
        for (WebElement card : cards) {
            WebElement titulo = card.findElement(By.tagName("h3"));
            WebElement desc = card.findElement(By.tagName("p"));
            Assert.assertFalse(titulo.getText().isEmpty(), "Debe tener un <h3> con texto");
            Assert.assertFalse(desc.getText().isEmpty(), "Debe tener un <p> descriptivo");
        }

        // Paso 4 – Estilos y espaciado
        String expectedBorderRadius = "10px";
        String expectedBoxShadowPart = "rgba(0, 0, 0, 0.1)";
        String prevMarginRight = null;
        for (WebElement card : cards) {
            String br = card.getCssValue("border-radius");
            String bs = card.getCssValue("box-shadow");
            Assert.assertEquals(br, expectedBorderRadius, "Borde redondeado incorrecto");
            Assert.assertTrue(bs.contains(expectedBoxShadowPart), "Debe tener sombra ligera");

            String marginRight = card.getCssValue("margin-right");
            if (prevMarginRight != null) {
                Assert.assertEquals(marginRight, prevMarginRight, "Margins entre cards deben ser iguales");
            }
            prevMarginRight = marginRight;
        }

        // Paso 5 – Hover/Focus
        WebElement primera = cards.get(0);
        String shadowAntes = primera.getCssValue("box-shadow");
        actions.moveToElement(primera).perform();
        TimeUnit.MILLISECONDS.sleep(300);
        String shadowDespues = primera.getCssValue("box-shadow");
        Assert.assertNotEquals(shadowAntes, shadowDespues, "Al hacer hover debe cambiar la sombra");

        // Paso 6 – Accesibilidad
        driver.findElement(By.tagName("body")).click();
        int cardFocusCount = 0;
        while (cardFocusCount < cards.size()) {
            actions.sendKeys(Keys.TAB).perform();
            TimeUnit.MILLISECONDS.sleep(200);
            WebElement focused = driver.switchTo().activeElement();
            // Solo cuenta si el enfocado es una card
            boolean isCardFocused = cards.stream().anyMatch(card ->
                card.getAttribute("outerHTML").equals(focused.getAttribute("outerHTML"))
            );
            if (isCardFocused) {
                String outline = focused.getCssValue("outline-style");
                Assert.assertNotEquals(outline, "none", "Debe mostrar contorno visible al recibir focus");
                cardFocusCount++;
            }
        }
    }
}
