package com.hoteltransito;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class TestAceptacion3 {
    private RemoteWebDriver driver;
    private final String HOME_URL = "file:///C:/Users/Jhuly/Documents/Proyectos/HotelTransitoATDD/front/index.html";

    @BeforeTest
    public void setDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterTest
    public void closeDriver() {
        driver.quit();
    }

    @Test
    public void redireccionHabitaciones() {
        // Paso 1 – Abrir la página de inicio
        driver.get(HOME_URL);
        // Paso 2 – Verificar título de la página principal
        String tituloHome = driver.getTitle();
        Assert.assertTrue(tituloHome.contains("Hotel Tránsito"),
                "El título de la página principal debe contener 'Hotel Tránsito'");
        // Paso 3 – Localizar enlace “Habitaciones” en la barra de navegación
        WebElement enlaceHabitaciones = driver.findElement(By.linkText("Habitaciones"));
        Assert.assertTrue(enlaceHabitaciones.isDisplayed(),
                "El enlace 'Habitaciones' debe estar visible en la barra de navegación");
        // Paso 4 – Hacer clic en el enlace “Habitaciones”
        enlaceHabitaciones.click();
        // Paso 5 – Verificar que la URL cambia a .../habitaciones.html
        String urlActual = driver.getCurrentUrl();
        Assert.assertTrue(urlActual.endsWith("habitaciones.html"),
                "La URL después de clicar debe terminar en 'habitaciones.html'");
        // Paso 6 – Verificar que aparece el encabezado “Nuestras Habitaciones”
        WebElement encabezado = driver.findElement(By.tagName("h2"));
        Assert.assertEquals(encabezado.getText(), "Nuestras Habitaciones",
                "El encabezado de la página de habitaciones debe ser 'Nuestras Habitaciones'");
        // Paso 7 – Verificar que se muestran 3 cards de habitaciones
        List<WebElement> cards = driver.findElements(By.cssSelector(".room-card"));
        Assert.assertEquals(cards.size(), 3,
                "Deben mostrarse exactamente 3 cards de habitaciones");
        // Paso 8 – Para cada card, comprobar imagen, título (<h3>) y descripción (<p>)
        for (WebElement card : cards) {
            WebElement img = card.findElement(By.tagName("img"));
            Assert.assertTrue(img.isDisplayed(),
                    "Cada card debe contener una imagen");

            WebElement tituloRoom = card.findElement(By.tagName("h3"));
            Assert.assertFalse(tituloRoom.getText().isEmpty(),
                    "Cada card debe tener un título");

            WebElement descripcion = card.findElement(By.tagName("p"));
            Assert.assertFalse(descripcion.getText().isEmpty(),
                    "Cada card debe tener una descripción");
        }
    }
}
