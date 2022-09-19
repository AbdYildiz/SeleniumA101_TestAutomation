import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class MainSite {
    //selenium-java-4.3
    //testng-7.1.0

    //I'm not taking any responsibility for any WebElement that has not been working properly
    public WebDriver driver = new ChromeDriver();
    public Actions act = new Actions(driver);
    public SoftAssert soft = new SoftAssert();
    public WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @BeforeSuite
    public void beforeSuite(){
       driver.manage().window().maximize();
       driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
       driver.get("https://www.a101.com.tr/");
    }

    @Test public void selectCategory() throws InterruptedException {
        WebElement cookies = driver.findElement(By.xpath("//div[@id='CybotCookiebotDialog']//button[.='Kabul Et']"));
        wait.until(ExpectedConditions.visibilityOf(cookies));
        cookies.click();
        act.moveToElement(driver.findElement(By.xpath("(//a[@href='/giyim-aksesuar/'])[1]"))).build().perform();
        WebElement dizalti = driver.findElement(By.xpath("(//div[@class='col-sm-10 submenu-items'])[4]//a[.='Dizaltı Çorap']"));
        wait.until(ExpectedConditions.visibilityOf(dizalti));
        act.moveToElement(dizalti).click().build().perform();
    }

    @Test(dependsOnMethods = "selectCategory") public void checkColor(){
        driver.findElement(By.xpath("(//h3[contains(text(),'Siyah')])[1]")).click();
        String color = driver.findElement(By.xpath("//div[@class='selected-variant-text']//span")).getText();
        Assert.assertEquals(color,"SİYAH");
    }

    @Test(dependsOnMethods = "checkColor") public void addToCart(){
        driver.findElement(By.xpath("//button[contains(@class,'add-to-basket')]")).click();
        WebElement gotoCart = driver.findElement(By.xpath("//a[@class='go-to-shop']"));
        wait.until(ExpectedConditions.visibilityOf(gotoCart));
        gotoCart.click();
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='basket']//div[@class='title']")).getText(),"SEPETİM");

        driver.findElement(By.xpath("(//a[@title='Sepeti Onayla'])[2]")).click();
        driver.findElement(By.xpath("//a[@title='ÜYE OLMADAN DEVAM ET']")).click();
        String tempMail = "gibow84073@dnitem.com";
        driver.findElement(By.cssSelector("input[name='user_email']")).sendKeys(tempMail, Keys.ENTER);
    }

    @Test(dependsOnMethods = "addToCart") public void newAddress() throws InterruptedException {
        driver.findElement(By.xpath("(//a[@title='Yeni adres oluştur'])[1]")).click();
        WebElement adresBasligi = driver.findElement(By.xpath("(//form[@class='js-address-form']//input)[4]"));
        WebElement ad = driver.findElement(By.xpath("(//form[@class='js-address-form']//input)[5]"));
        WebElement soyad = driver.findElement(By.xpath("(//form[@class='js-address-form']//input)[6]"));
        WebElement telefon = driver.findElement(By.xpath("(//form[@class='js-address-form']//input)[7]"));
        WebElement postaKodu = driver.findElement(By.xpath("(//form[@class='js-address-form']//input)[9]"));

        WebElement il = driver.findElement(By.xpath("(//form[@class='js-address-form']//select)[1]"));
        WebElement ilce = driver.findElement(By.xpath("(//form[@class='js-address-form']//select)[2]"));
        WebElement mahalle = driver.findElement(By.xpath("(//form[@class='js-address-form']//select)[3]"));
        WebElement adres = driver.findElement(By.xpath("//form[@class='js-address-form']//textarea"));

        adresBasligi.sendKeys("ev");
        ad.sendKeys("ABDULLAH");
        soyad.sendKeys("YILDIZ");
        telefon.sendKeys("5555555555");
        il.click();
        driver.findElement(By.xpath("(//form[@class='js-address-form']//select)[1]//option[@value='29']")).click();
        ilce.click();
        driver.findElement(By.xpath("(//form[@class='js-address-form']//select)[2]//option[@value='334']")).click();
        act.click(mahalle).sendKeys("s").build().perform();
        WebElement mahalleSelect = driver.findElement(By.xpath("//option[contains(text(),'SÜRSÜRÜ MAH')]"));
        wait.until(ExpectedConditions.visibilityOf(mahalleSelect));
        mahalleSelect.click();
        adres.sendKeys("test test test");
        postaKodu.sendKeys("23000");

        WebElement saveAddress = driver.findElement(By.xpath("(//form[@class='js-address-form']//button)[1]"));
        wait.until(ExpectedConditions.invisibilityOf(saveAddress));
        saveAddress.click();

        WebElement continueToPayment = driver.findElement(By.xpath("//button[contains(text(),'Kaydet ve Devam Et')]"));
        wait.until(ExpectedConditions.visibilityOf(continueToPayment));
        continueToPayment.click();
    }

    @Test(dependsOnMethods = "newAddress") public void completeOrder(){
        //I don't find it very secure to providing real payment values here
        driver.findElement(By.xpath("(//input)[15]")).sendKeys("ABDULLAH YILDIZ");
        act.sendKeys(Keys.TAB, "2222222222222222").build().perform();
        act.sendKeys(Keys.TAB).build().perform();
        act.sendKeys(Keys.TAB, Keys.ARROW_RIGHT).build().perform();
        act.sendKeys(Keys.TAB, "222").build().perform();
        WebElement checkBox = driver.findElement(By.cssSelector("label[for=agrement2]"));
        checkBox.click();
        soft.assertTrue(checkBox.isSelected());
        driver.findElement(By.xpath("(//span[.='Siparişi Tamamla'])[2]")).click();

        soft.assertTrue(driver.findElement(By.xpath("//span[@class='order-spinner']")).isDisplayed());
        soft.assertAll();
    }

    @AfterSuite public void close() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }
}