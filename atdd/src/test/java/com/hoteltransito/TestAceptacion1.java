package com.hoteltransito;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestAceptacion1 {

    private RemoteWebDriver driver;
    
    @BeforeTest
    public void setDriver() throws Exception{

        //https://googlechromelabs.github.io/chrome-for-testing/   descarguen de aqui hehe
        
    	String path = "C:/WebDrivers/chromedriver-win64";
        
        System.setProperty("webdriver.chrome.driver", path);
        
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        
    }
    
    @AfterTest
    public void closeDriver() throws Exception{
        driver.quit();
    }
    
    @Test
    public void paginaPrincipalHotelTransito() {
        String url = "file:///C:/Users/Jhuly/Documents/Proyectos/HotelTransitoATDD/front/index.html";
        driver.get(url);
        String titulo = driver.getTitle();
        assert titulo.contains("Hotel Tránsito");
    }
}