package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    public  HomePage(WebDriver driver) {
        super(driver);
    }

    //WebElements - Locators+Identification
    @FindBy(xpath = "//span[text()='My Account']")
    WebElement lnkMyAccount;
    @FindBy(linkText = "Register")
    WebElement lnkRegister;
    @FindBy(linkText = "Login")
    WebElement linkLogin;

    //ActionMethods
    public void clickMyAccount() {
        lnkMyAccount.click();
    }
    public void clickRegister() {
        lnkRegister.click();
    }
    public void clickLogin() {
        linkLogin.click();
    }
}
