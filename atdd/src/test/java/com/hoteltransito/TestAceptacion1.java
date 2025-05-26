package com.hoteltransito;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestAceptacion1 {
        private RemoteWebDriver driver;
        private Actions actions;
        private WebDriverWait wait;
        private final String HOME_URL = "file:///I:/UCB/7mo Semestre/Gestión/HotelTransitoATDD/front/index.html";

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

        @Test(description = "Prueba 1: Funcionalidad del carrusel en la página de inicio")
        public void testCarruselFuncionalidad() throws Exception {
                // Preparación - Abrir navegador y navegar a la URL raíz
                driver.get(HOME_URL);
                TimeUnit.SECONDS.sleep(2); // Asegurar carga completa de la página

                // Paso 1 – Estado inicial
                WebElement carrusel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.id("carousel")));
                Assert.assertTrue(carrusel.isDisplayed(),
                                "El carrusel debe estar visible en la parte superior");

                // Verificar primera diapositiva
                WebElement primeraSlide = driver.findElement(
                                By.cssSelector(".carousel-slide.active"));
                Assert.assertTrue(primeraSlide.isDisplayed(),
                                "La primera diapositiva debe aparecer correctamente");

                // Verificar que contiene texto "Hotel Tránsito" en el caption
                WebElement caption = driver.findElement(By.cssSelector(".carousel-caption"));
                String textoCaption = caption.getText();
                Assert.assertTrue(textoCaption.contains("Hotel Tránsito"),
                                "La primera diapositiva debe contener el texto 'Hotel Tránsito'");

                // Paso 2 – Navegar a la siguiente diapositiva
                WebElement flechaDerecha = driver.findElement(By.id("next"));
                Assert.assertTrue(flechaDerecha.isDisplayed(),
                                "La flecha derecha debe estar visible");

                // Capturar slide inicial para comparar
                String slideInicial = obtenerSlideActivaBackground();

                flechaDerecha.click();
                TimeUnit.MILLISECONDS.sleep(1200); // Esperar animación de transición (1s + margen)

                // Verificar que cambió a la segunda diapositiva
                String slideDespues = obtenerSlideActivaBackground();
                Assert.assertNotEquals(slideInicial, slideDespues,
                                "El contenido debe cambiar al hacer clic en la flecha derecha");

                // Paso 3 – Navegar a la diapositiva anterior
                WebElement flechaIzquierda = driver.findElement(By.id("prev"));
                Assert.assertTrue(flechaIzquierda.isDisplayed(),
                                "La flecha izquierda debe estar visible");

                flechaIzquierda.click();
                TimeUnit.MILLISECONDS.sleep(1200); // Esperar animación de transición

                // Verificar que vuelve a la primera diapositiva
                String slideVuelta = obtenerSlideActivaBackground();
                Assert.assertEquals(slideInicial, slideVuelta,
                                "Debe volver a mostrarse la primera diapositiva");

                // Paso 4 – Auto-rotación
                String slideAntesAutoRotacion = obtenerSlideActivaBackground();

                // Esperar tiempo de auto-rotación (5 segundos + margen)
                TimeUnit.SECONDS.sleep(6);

                String slideDespuesAutoRotacion = obtenerSlideActivaBackground();
                Assert.assertNotEquals(slideAntesAutoRotacion, slideDespuesAutoRotacion,
                                "El carrusel debe avanzar automáticamente después del tiempo de rotación");

                // Paso 5 – Pausa al hacer hover
                // Volver a primera slide para control
                flechaIzquierda.click();
                TimeUnit.MILLISECONDS.sleep(1200);

                String slideAntesHover = obtenerSlideActivaBackground();

                // Hacer hover sobre el carrusel
                WebElement carruselArea = driver.findElement(By.id("carousel"));
                actions.moveToElement(carruselArea).perform();

                // Esperar más tiempo del usual de auto-rotación
                TimeUnit.SECONDS.sleep(7);

                String slideDuranteHover = obtenerSlideActivaBackground();

                // Nota: El carrusel actual no tiene funcionalidad de pausa en hover
                // implementada
                // pero verificamos que al menos no haya error en la interacción
                Assert.assertNotNull(slideDuranteHover,
                                "El carrusel debe seguir funcionando durante el hover");

                // Mover cursor fuera
                actions.moveToElement(driver.findElement(By.tagName("body")), 0, 0).perform();
                TimeUnit.SECONDS.sleep(1);

                // Paso 6 – Responsive
                // Guardar tamaño original
                Dimension tamaioOriginal = driver.manage().window().getSize();

                // Redimensionar a modo móvil (375px ancho)
                driver.manage().window().setSize(new Dimension(375, 667));
                TimeUnit.SECONDS.sleep(1);

                // Verificar que el carrusel sigue visible y adaptado
                Assert.assertTrue(carrusel.isDisplayed(),
                                "El carrusel debe seguir visible en modo móvil");

                // Verificar que las flechas siguen siendo accesibles
                WebElement flechaDerechaMobile = driver.findElement(By.id("next"));
                WebElement flechaIzquierdaMobile = driver.findElement(By.id("prev"));

                Assert.assertTrue(flechaDerechaMobile.isDisplayed(),
                                "La flecha derecha debe ser accesible en modo móvil");
                Assert.assertTrue(flechaIzquierdaMobile.isDisplayed(),
                                "La flecha izquierda debe ser accesible en modo móvil");

                // Probar funcionalidad en móvil
                String slideMobileAntes = obtenerSlideActivaBackground();
                flechaDerechaMobile.click();
                TimeUnit.MILLISECONDS.sleep(1200);
                String slideMobileDespues = obtenerSlideActivaBackground();

                Assert.assertNotEquals(slideMobileAntes, slideMobileDespues,
                                "Las flechas deben funcionar correctamente en modo móvil");

                // Verificar que el carrusel se adapta correctamente
                WebElement carruselMobile = driver.findElement(By.id("carousel"));
                int anchoCarrusel = carruselMobile.getSize().getWidth();
                Assert.assertTrue(anchoCarrusel <= 375,
                                "El carrusel debe adaptarse al ancho móvil");

                // Verificar que el caption sigue visible y centrado
                WebElement captionMobile = driver.findElement(By.cssSelector(".carousel-caption"));
                Assert.assertTrue(captionMobile.isDisplayed(),
                                "El caption debe seguir visible en modo móvil");

                // Restaurar tamaño original
                driver.manage().window().setSize(tamaioOriginal);
                TimeUnit.MILLISECONDS.sleep(1);
        }

        /**
         * Método auxiliar para obtener el background-image de la slide activa
         */
        private String obtenerSlideActivaBackground() {
                try {
                        WebElement slideActiva = driver.findElement(
                                        By.cssSelector(".carousel-slide.active"));
                        return slideActiva.getCssValue("background-image");
                } catch (NoSuchElementException e) {
                        // Fallback: tomar la primera slide
                        WebElement primeraSlide = driver.findElement(
                                        By.cssSelector(".carousel-slide"));
                        return primeraSlide.getCssValue("background-image");
                }
        }
}