package com.hoteltransito;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class TestAceptacion1 {
        private RemoteWebDriver driver;
        private Actions actions;
        private WebDriverWait wait;
        // Ajusta la ruta a la de tu entorno si es necesario
        private final String HOME_URL = "file:///I:/UCB/7mo Semestre/Gestión/HotelTransitoATDD/front/index.html";

        @BeforeTest
        public void setUp() {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                actions = new Actions(driver);
                wait = new WebDriverWait(driver, 10);
        }

        @AfterTest
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }

        @Test(description = "Prueba 1: Funcionalidad del carrusel en la página de inicio")
        public void testCarruselFuncionalidad() throws Exception {
                driver.get(HOME_URL);

                // Paso 1 – Estado inicial
                WebElement carrusel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("carousel")));
                Assert.assertTrue(carrusel.isDisplayed(), "El carrusel debe estar visible en la parte superior");

                // Verificar primera diapositiva
                WebElement primeraSlide = driver.findElement(By.cssSelector(".carousel-slide.active"));
                Assert.assertTrue(primeraSlide.isDisplayed(), "La primera diapositiva debe aparecer correctamente");

                // Verificar que contiene texto "Hotel Tránsito" en el caption
                WebElement caption = driver.findElement(By.cssSelector(".carousel-caption"));
                String textoCaption = caption.getText();
                Assert.assertTrue(textoCaption.contains("Hotel Tránsito"),
                                "La primera diapositiva debe contener el texto 'Hotel Tránsito'");

                // Paso 2 – Navegar a la siguiente diapositiva
                WebElement flechaDerecha = driver.findElement(By.id("next"));
                Assert.assertTrue(flechaDerecha.isDisplayed(), "La flecha derecha debe estar visible");

                String slideInicial = obtenerSlideActivaBackground();

                flechaDerecha.click();
                waitForSlideChange(slideInicial);

                String slideDespues = obtenerSlideActivaBackground();
                Assert.assertNotEquals(slideInicial, slideDespues,
                                "El contenido debe cambiar al hacer clic en la flecha derecha");

                // Paso 3 – Navegar a la diapositiva anterior
                WebElement flechaIzquierda = driver.findElement(By.id("prev"));
                Assert.assertTrue(flechaIzquierda.isDisplayed(), "La flecha izquierda debe estar visible");

                flechaIzquierda.click();
                waitForSlideChange(slideDespues);

                String slideVuelta = obtenerSlideActivaBackground();
                Assert.assertEquals(slideInicial, slideVuelta, "Debe volver a mostrarse la primera diapositiva");

                // Paso 4 – Auto-rotación
                String slideAntesAutoRotacion = obtenerSlideActivaBackground();
                TimeUnit.SECONDS.sleep(6); // Espera el tiempo del auto-rotado
                String slideDespuesAutoRotacion = obtenerSlideActivaBackground();
                Assert.assertNotEquals(slideAntesAutoRotacion, slideDespuesAutoRotacion,
                                "El carrusel debe avanzar automáticamente después del tiempo de rotación");

                // Paso 5 – Responsive
                Dimension tamañoOriginal = driver.manage().window().getSize();
                driver.manage().window().setSize(new Dimension(375, 667));
                TimeUnit.MILLISECONDS.sleep(700);

                WebElement carruselMobile = driver.findElement(By.id("carousel"));
                Assert.assertTrue(carruselMobile.isDisplayed(), "El carrusel debe seguir visible en modo móvil");

                WebElement flechaDerechaMobile = driver.findElement(By.id("next"));
                WebElement flechaIzquierdaMobile = driver.findElement(By.id("prev"));
                Assert.assertTrue(flechaDerechaMobile.isDisplayed(),
                                "La flecha derecha debe ser accesible en modo móvil");
                Assert.assertTrue(flechaIzquierdaMobile.isDisplayed(),
                                "La flecha izquierda debe ser accesible en modo móvil");

                String slideMobileAntes = obtenerSlideActivaBackground();
                flechaDerechaMobile.click();
                waitForSlideChange(slideMobileAntes);
                String slideMobileDespues = obtenerSlideActivaBackground();
                Assert.assertNotEquals(slideMobileAntes, slideMobileDespues,
                                "Las flechas deben funcionar correctamente en modo móvil");

                int anchoCarrusel = carruselMobile.getSize().getWidth();
                Assert.assertTrue(anchoCarrusel <= 500, "El carrusel debe adaptarse al ancho móvil");

                WebElement captionMobile = driver.findElement(By.cssSelector(".carousel-caption"));
                Assert.assertTrue(captionMobile.isDisplayed(), "El caption debe seguir visible en modo móvil");
                // Restaurar tamaño original
                driver.manage().window().setSize(tamañoOriginal);
                TimeUnit.MILLISECONDS.sleep(300);

                // Paso 6 – Hover (no pausa, pero no debe fallar)
                flechaIzquierda.click();
                waitForSlideChange(slideMobileDespues);
                actions.moveToElement(carrusel).perform();
                TimeUnit.SECONDS.sleep(7); // más de lo normal para autorotación

                String slideDuranteHover = obtenerSlideActivaBackground();
                Assert.assertNotNull(slideDuranteHover, "El carrusel debe seguir funcionando durante el hover");
                actions.moveToElement(driver.findElement(By.tagName("body")), 0, 0).perform();
        }

        /**
         * Método auxiliar para obtener el background-image de la slide activa
         */
        private String obtenerSlideActivaBackground() {
                try {
                        WebElement slideActiva = driver.findElement(By.cssSelector(".carousel-slide.active"));
                        return slideActiva.getCssValue("background-image");
                } catch (NoSuchElementException e) {
                        // Fallback: tomar la primera slide
                        WebElement primeraSlide = driver.findElement(By.cssSelector(".carousel-slide"));
                        return primeraSlide.getCssValue("background-image");
                }
        }

        /**
         * Espera hasta que cambie el background de la slide activa o timeout.
         */
        private void waitForSlideChange(String previousBackground) {
                wait.until(driver -> {
                        String curr = obtenerSlideActivaBackground();
                        return curr != null && !curr.equals(previousBackground);
                });
        }
}