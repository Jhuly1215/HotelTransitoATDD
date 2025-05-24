# Hotel Tránsito – Pruebas ATDD

Repositorio de pruebas de aceptación automatizadas (ATDD) para el sitio estático del Hotel Tránsito, usando Java, Maven, TestNG y Selenium WebDriver.

---

## 📖 Descripción

Este proyecto aplica la metodología **Acceptance Test–Driven Development (ATDD)** para validar, de forma automatizada, que el front-end HTML/CSS/JS del sitio web del Hotel Tránsito cumple con los criterios de calidad acordados (carrusel, cards de servicios, navegación a “Habitaciones”, etc.).

- La parte visual (HTML/CSS/JS) está en la carpeta `front/`.
- Los tests en Java (TestNG + Selenium) residen en el módulo `atdd/`.
- ChromeDriver puede configurarse manualmente (variable de sistema) o automáticamente con WebDriverManager.

---

## ⚙️ Tecnologías

| Herramienta            | Versión típica      |
|------------------------|---------------------|
| Java                   | 1.8+                |
| Maven                  | 3.x                 |
| TestNG                 | 7.x                 |
| Selenium WebDriver     | 3.x–4.x             |
| WebDriverManager       | 5.x (opcional)      |
| HTML / CSS / JavaScript| Puro (sin framework)|

---

## 📂 Estructura de carpetas

---

```
HotelTransitoATDD/
├── atdd/   #modulo de pruebas de aceptación automatizadas
│   ├── src/
│   │   └── test/
│   │       ├── java/com/hoteltransito
│   │           └── Test.java         # Aquí van los tests    
│   └── pom.xml  #Configuración de build y dependencias
├── front/
│   ├── habitaciones.html
│   └── index.html   
└── README.md

## 🚀 Cómo ejecutar

1. **Clonar el repositorio**  
    ```bash
    git clone https://tu.git.server/HotelTransitoATDD.git
    cd HotelTransitoATDD
    ```

2. **Configurar ChromeDriver**  
   1. Descarga el binario desde:  
      https://sites.google.com/chromium.org/driver/  
   2. Coloca el ejecutable donde prefieras, por ejemplo:  
      - **Windows**: `C:\WebDrivers\chromedriver.exe`  
      - **macOS/Linux**: `/usr/local/bin/chromedriver`

3. **Compilar el proyecto**  
    ```bash
    mvn clean compile
    ```

4. **Ejecutar los tests**  
    ```bash
    mvn test
    ```
    - Los tests abrirán `front/index.html` (y otras páginas) en Chrome, validarán títulos y elementos del sitio.

5. **Verificar los reportes**  
   Tras la ejecución, encontrarás los informes en:
    ```
    atdd/target/surefire-reports/
    ```
   Abre `index.html` allí para ver el reporte en tu navegador.

---

## 🔧 Detalle de la prueba de ejemplo

En tu clase de TestNG defines tres bloques generales:

1. **@BeforeTest** — preparar el driver  
2. **@Test** — ejecutar la verificación  
3. **@AfterTest** — cerrar el driver


## Equipo
- Franz Carvajal
- Daniela Guzman
- Jhulianna Tarqui
