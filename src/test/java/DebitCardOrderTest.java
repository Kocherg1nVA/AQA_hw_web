import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitCardOrderTest {
    private ChromeDriver driver;

    @BeforeAll
    static void setUpAll(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendForm(){
        List<WebElement> elements = driver.findElements(By.cssSelector("input"));
        elements.get(0).sendKeys("Кочергин Вадим");
        elements.get(1).sendKeys("+79991951481");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String result = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.trim());
    }

    @Test
    public void shouldSendFormAnother(){
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Кочергин Вадим");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991951481");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String result = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.trim());
    }

    @Test
    public void shouldWrongNameForm(){
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Kochergin Vadim");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991951481");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actual.trim());
    }

    @Test
    public void shouldWrongPhoneNumber(){
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Кочергин Вадим");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("481");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actual.trim() );
    }
}
