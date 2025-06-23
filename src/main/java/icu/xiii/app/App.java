package icu.xiii.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Scanner;
import java.time.Duration;

public class App {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-data-dir=/Users/xiii/Library/Application Support/Google/Chrome");
            options.addArguments("profile-directory=Default");

            driver.get("https://chat.openai.com/");

            // Загружаем cookie
            CookieManager.loadCookies(driver);

            // Перезагружаем страницу после добавления cookies
            driver.navigate().refresh();
            Thread.sleep(5000);

            // Проверка, вошли ли мы в аккаунт (например, по наличию текстового поля)
            boolean loggedIn;
            try {
                driver.findElement(By.xpath("//textarea[@data-testid='prompt-textarea']"));
                loggedIn = true;
            } catch (Exception e) {
                loggedIn = false;
            }

            if (!loggedIn) {
                System.out.println("🔐 Войдите вручную, затем нажмите Enter (cookies будут сохранены)");
                new Scanner(System.in).nextLine();
                CookieManager.saveCookies(driver);
            }

            // Ждём появления поля ввода
            Thread.sleep(5000);
            WebElement inputField = driver.findElement(By.xpath("//textarea[@data-testid='prompt-textarea']"));

            // Отправляем сообщение
            String message = "Привет, чем ты можешь помочь разработчику?";
            inputField.sendKeys(message);
            inputField.sendKeys("\n");

            // Ждём ответа
            Thread.sleep(10000);

            List<WebElement> responses = driver.findElements(By.cssSelector("[data-message-author-role='assistant']"));
            WebElement lastResponse = responses.get(responses.size() - 1);

            System.out.println("\n💬 Ответ ChatGPT:\n");
            System.out.println(lastResponse.getText());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Оставим браузер открытым
            // driver.quit();
        }
    }
}