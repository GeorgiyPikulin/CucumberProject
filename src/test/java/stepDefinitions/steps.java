package stepDefinitions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilities.ExcelDataReader;

public class steps {
    WebDriver driver;
    HomePage homePage;
    LoginPage loginPage;
    MyAccountPage myAccountPage;

    Logger logger; //for logging
    ResourceBundle resourceBundle; // for reading properties file
    String browser; //to store browser name
    List<HashMap<String, String>> dataMap; //Data driven


    @Before //Junit hook - executes once before starting
    public void setup() {
        logger = LogManager.getLogger(this.getClass()); //for logging
        resourceBundle = ResourceBundle.getBundle("config"); //Reading config.properties (for browser)
        browser = resourceBundle.getString("browser");
    }

    @Given("User launches browser")
    public void user_launch_browser() {
        switch (browser) {
            case "chrome" -> driver = new ChromeDriver();
            case "firefox" -> driver = new FirefoxDriver();
            case "edge" -> driver = new EdgeDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Given("opens URL {string}")
    public void opens_url(String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("Scenario status ======> " + scenario.getStatus());
        if (scenario.isFailed()) {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            byte[] screenshot = takesScreenshot.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        driver.quit();
    }

//    Login.feature

    @When("User navigates to MyAccount menu")
    public void user_navigate_to_my_account() {
        homePage = new HomePage(driver);
        homePage.clickMyAccount();
        logger.info("Clicked on My Account ");
    }

    @When("clicks on Login link")
    public void click_on_login() {
        homePage.clickLogin();
        logger.info("Clicked on Login ");
    }

    @When("User types in Email {string} and Password {string}")
    public void user_enters_email_and_password(String email, String pwd) {
        loginPage = new LoginPage(driver);
        loginPage.setEmail(email);
        logger.info("Provided Email ");
        loginPage.setPassword(pwd);
        logger.info("Provided Password ");
    }

    @When("clicks on Login button")
    public void click_on_login_button() {
        loginPage.clickLogin();
        logger.info("Clicked on Login button");
    }

    @Then("User is navigated to MyAccount page")
    public void user_navigates_to_myAccount_page() {
        myAccountPage = new MyAccountPage(driver);
        boolean targetPage = myAccountPage.isMyAccountPageExists();

        if (targetPage) {
            logger.info("Login Success ");
            Assert.assertTrue(true);
        } else {
            logger.error("Login Failed ");
            Assert.fail();
        }
    }

//  DDTExcel

    @Then("check User is navigated to MyAccount page by passing Email and Password with excel row {string}")
    public void check_user_navigates_to_my_account_page_by_passing_email_and_password_with_excel_data(String rows) throws InterruptedException {
        dataMap = ExcelDataReader.data(System.getProperty("user.dir")+"\\testData\\Opencart_LoginData.xlsx", "Sheet1");
        int index = Integer.parseInt(rows) - 1;

        String email = dataMap.get(index).get("username");
        String pwd = dataMap.get(index).get("password");
        String expectedResult = dataMap.get(index).get("result");

        loginPage = new LoginPage(driver);
        loginPage.setEmail(email);
        loginPage.setPassword(pwd);
        loginPage.clickLogin();

        try {
            myAccountPage = new MyAccountPage(driver);
            boolean targetPage = myAccountPage.isMyAccountPageExists();
            logger.info("Is My Account Page Exists? " + targetPage); // Add this logging statement

            if (expectedResult.equals("Valid")) {
                if (targetPage) {
                    MyAccountPage myAccPage = new MyAccountPage(driver);
                    myAccPage.clickLogout();
                    logger.info("Logged out successfully"); // Add this logging statement
                    Assert.assertTrue(true);
                } else {
                    logger.error("Login failed due to wrong credentials"); // Add this logging statement
                    Assert.fail("wrong credentials");
                }
            } else if (expectedResult.equals("Invalid")) {
                if (targetPage) {
                    myAccountPage.clickLogout();
                    logger.error("Login should have failed but it did not"); // Add this logging statement
                    Assert.fail();
                } else {
                    logger.info("Login failed as expected"); // Add this logging statement
                    Assert.assertTrue(true);
                }
            } else {
                logger.error("Invalid 'result' value in Excel data."); // Add this logging statement
                Assert.fail("Invalid 'result' value in Excel data.");
            }
        } catch (Exception e) {
            logger.error("Exception occurred: " + e.getMessage()); // Add this logging statement
            Assert.fail("try-catch block");
        }
        driver.close();
    }
};
