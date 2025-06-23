package icu.xiii.app;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Date;
import java.util.Set;

public class CookieManager {
    private static final String COOKIE_FILE = "cookies.data";

    public static void saveCookies(WebDriver driver) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COOKIE_FILE))) {
            oos.writeObject(driver.manage().getCookies());
            System.out.println("✅ Cookies сохранены в файл.");
        } catch (IOException e) {
            System.err.println("❌ Не удалось сохранить cookies: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadCookies(WebDriver driver) {
        File file = new File(COOKIE_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ Cookie файл не найден, нужно сначала залогиниться вручную.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COOKIE_FILE))) {
            Set<Cookie> cookies = (Set<Cookie>) ois.readObject();
            for (Cookie cookie : cookies) {
                Cookie adjusted = new Cookie(
                        cookie.getName(),
                        cookie.getValue(),
                        cookie.getDomain(),
                        cookie.getPath(),
                        cookie.getExpiry(),
                        cookie.isSecure(),
                        cookie.isHttpOnly(),
                        cookie.getSameSite()
                );
                driver.manage().addCookie(adjusted);
            }
            System.out.println("✅ Cookies загружены.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Ошибка при загрузке cookies: " + e.getMessage());
        }
    }
}

