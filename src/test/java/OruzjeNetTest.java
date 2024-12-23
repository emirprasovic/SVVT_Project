

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OruzjeNetTest {
    private static WebDriver webDriver;
    private static String baseUrl;
    private static WebDriverWait webDriverWait;
    private static JavascriptExecutor javascriptExecutor;

    private void scrollToY(int val) {
        javascriptExecutor.executeScript("window.scrollTo(0, arguments[0]);", val);
    }

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Selenium\\chromedriver-win64\\chromedriver.exe"); // specify the path to chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://oruzje.net/";
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        javascriptExecutor = (JavascriptExecutor) webDriver;
    }

    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    // svvtTest, svvttest123
    // emirprasovic, dobrasifra

    // TEST SCENARIO: LOGIN
    public void login(String usernameVal, String passwordVal) throws InterruptedException {
        webDriver.get(baseUrl);

        WebElement loginButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/section[1]/div/div/div[2]/a[3]")));
        loginButton.click();


        WebElement username = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
        username.sendKeys(usernameVal);
        WebElement password = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='password']")));
        password.sendKeys(passwordVal);

        WebElement login = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div/div[2]/form/div/button")));
        login.click();

        Thread.sleep(2000);
    }

    @Test
    public void testValidUnverifiedUserLogin() throws InterruptedException {
        login("svvtTest", "svvttest123");
    }

    @Test
    public void testValidVerifiedUserLogin() throws InterruptedException {
        login("emirprasovic", "dobrasifra");
    }

    @Test
    public void testInvalidLogin() throws InterruptedException {
        login("InvalidUsername", "invalidpwd");
    }

    // TEST SCENARIO: REGISTRATION
    @Test
    public void testRegistration() throws InterruptedException {
        webDriver.get(baseUrl);
//        WebElement registrationButton = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='https://oruzje.net/registracija']")));
        WebElement registrationButton = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[1]/div/div/div[2]/a[1]")));
        registrationButton.click();

        scrollToY(200);
        Thread.sleep(1000);

        WebElement basicPacketButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='https://oruzje.net/registracija-osnovni-nalog?p=1']")));
        basicPacketButton.click();

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3)); // Might be unnecessary

        WebElement nameInput = webDriver.findElement(By.name("name"));
        WebElement dayInput = webDriver.findElement(By.name("day"));
        WebElement monthInput = webDriver.findElement(By.name("month"));
        WebElement yearInput = webDriver.findElement(By.name("year"));
        WebElement usernameInput = webDriver.findElement(By.name("username"));
        WebElement emailInput = webDriver.findElement(By.name("email"));
        WebElement passwordInput = webDriver.findElement(By.name("password"));
        WebElement confirmPasswordInput = webDriver.findElement(By.name("password_confirmation"));
        WebElement phoneNumberInput = webDriver.findElement(By.name("phone_number"));
        Select citySelect = new Select(webDriver.findElement(By.name("city_id")));
        // WebElement privacyCheckbox = webDriver.findElement(By.name("privacy"));
        // Selecting the checkbox doesn't work, so we select the label
        WebElement privacyInputLabel = webDriver.findElement(By.xpath("//label[@for='check']"));

        nameInput.sendKeys("SVVT Test");
        dayInput.sendKeys("10");
        monthInput.sendKeys("10");
        yearInput.sendKeys("2000");
        usernameInput.sendKeys("svvtTest1");
        emailInput.sendKeys("svvt@testing.com");
        passwordInput.sendKeys("svvt1234");
        confirmPasswordInput.sendKeys("svvt1234");
        phoneNumberInput.sendKeys("38761111111");

        scrollToY(600);
        Thread.sleep(1000);

        citySelect.selectByValue("28");
        // privacyCheckbox.click();
        privacyInputLabel.click();

        WebElement createAccountButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div/div/form/div[2]/button")));
        createAccountButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: SEARCH
    @ParameterizedTest
    @CsvSource({"Colt Python, python", "Glock 19, glock", "Sig Sauer, sig"})
    public void testSearch(String searchTermInput, String expectedPartialResult) throws InterruptedException {
        webDriver.get(baseUrl);

        // WebElement searchInput = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("pretraga")));
        WebElement searchInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[1]/div/div/form/div/input")));
        searchInput.sendKeys(searchTermInput);
        searchInput.sendKeys(Keys.ENTER);

        WebElement searchResult = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div[2]/article[1]/div[3]/div[1]/h3")));
        String productTitle = searchResult.getText();
        // System.out.println(productTitle);

        assertTrue(productTitle.toLowerCase().contains(expectedPartialResult), "Search results should contain the search term");

        Thread.sleep(3000);
    }

    // TEST SCENARIO: FILTER RESULTS
    @Test
    public void testFilterByCategory() throws InterruptedException {
        webDriver.get(baseUrl);

        scrollToY(800);
        Thread.sleep(1000);

        Select categorySelect = new Select(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("kategorija"))));
        Select subcategorySelect = new Select(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("podkategorija"))));

        categorySelect.selectByValue("51");
        // subcategory is dependent on category, so we need to wait for the subcategory data to fetch
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        subcategorySelect.selectByValue("56");

        Select brandSelect = new Select(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("brend"))));
        Select caliberSelect = new Select(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("kalibar"))));

        brandSelect.selectByValue("Gamo");
        caliberSelect.selectByValue("4,5");

        WebElement filterButton = webDriver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/form/div/div[3]/div[2]/div[2]/button"));
        filterButton.click();

        WebElement searchResult = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div[2]/article[1]/div[3]/div[1]/h3")));
        String productTitle = searchResult.getText().toLowerCase();
        System.out.println(productTitle);

        assertTrue(
                productTitle.contains("dijabola") || productTitle.contains("dijabole") &&
                        productTitle.contains("gamo") &&
                        productTitle.contains("4.5mm")
        );


        Thread.sleep(3000);
    }

    @Test
    public void testFilterByPrice() throws InterruptedException {
        webDriver.get(baseUrl);

        scrollToY(800);
        Thread.sleep(1000);

        WebElement priceFromInput = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("cijena_od")));
        WebElement priceUptoInput = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("cijena_do")));

        // Iz nekog razloga ne prikazuje na stranici kako se inputi unose, ali radi
        priceFromInput.sendKeys("2000");
        priceUptoInput.sendKeys("3000");
        priceUptoInput.sendKeys(Keys.ENTER);

        List<WebElement> productPriceDivs = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='product__price-new font-semibold color-primary']")));
        ArrayList<Double> prices = new ArrayList<>();
        for (WebElement el : productPriceDivs) {
            String text = el.getText();
            text = text.replace("KM", "").replace(" ", "");

            Double price = Double.parseDouble(text);
            prices.add(price);

            System.out.println(price);
        }

        for (Double price : prices) {
            assertTrue(price >= 2000 && price <= 3000);
        }

        Thread.sleep(3000);
    }

    @Test // Ne radi, selektovanje ikona ne valja
    public void testFilterByCountry() throws InterruptedException {
        webDriver.get(baseUrl);

        scrollToY(800);
        Thread.sleep(1000);

        Select countrySelect = new Select(webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("drzava"))));
        countrySelect.selectByValue("4");

        WebElement filterButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[1]/form/div/div[3]/div[2]/div[2]/button")));
        filterButton.click();

        // List<WebElement> countryIconSvgs = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//use[@href='https://oruzje.net/images/icons.svg#icon--hr']")));
        List<WebElement> countryIconSvgs = webDriver.findElements(By.xpath("//use[@*[name()='xlink:href' and .='https://oruzje.net/images/icons.svg#icon--hr']]"));


        List<WebElement> articesList = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//article[@class='product']")));

        System.out.println("Icon list size: " + countryIconSvgs.size());
        System.out.println("Article list size: " + articesList.size());

        Thread.sleep(3000);
    }

    // TEST SCENARIO: ORDER RESULTS
    @Test
    public void testOrderByPriceDesc() throws InterruptedException {
        webDriver.get(baseUrl);

        scrollToY(800);
        Thread.sleep(1000);

        Select orderSelect = new Select(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("sortiraj_po"))));
        orderSelect.selectByValue("najvisoj_cijeni");

        WebElement filterButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[1]/form/div/div[3]/div[2]/div[2]/button")));
        filterButton.click();

        List<WebElement> productPriceDivs = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='product__price-new font-semibold color-primary']")));
        //for(WebElement el : productPriceDivs) {
        //    System.out.println(el.getText());
        //}

        ArrayList<Double> prices = new ArrayList<>();
        for (WebElement el : productPriceDivs) {
            String text = el.getText();
            text = text.replace("KM", "").replace(" ", "");

            Double price = Double.parseDouble(text);
            prices.add(price);
            System.out.println(price);

        }

        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices, Collections.reverseOrder());
        assertEquals(sortedPrices, prices, "Results should be sorted by price in descending order");
    }

    @Test // Filter price from 1KM to filter out "negotiable" price
    public void testOrderByPriceAsc() throws InterruptedException {
        webDriver.get(baseUrl);

        scrollToY(800);
        Thread.sleep(1000);

        WebElement priceFromInput = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("cijena_od")));
        priceFromInput.sendKeys("1");

        Select orderSelect = new Select(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("sortiraj_po"))));
        orderSelect.selectByValue("najnizoj_cijeni");

        WebElement filterButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[1]/form/div/div[3]/div[2]/div[2]/button")));
        filterButton.click();

        List<WebElement> productPriceDivs = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='product__price-new font-semibold color-primary']")));

        ArrayList<Double> prices = new ArrayList<>();
        for (WebElement el : productPriceDivs) {
            String text = el.getText();
            text = text.replace("KM", "").replace(" ", "");

            Double price = Double.parseDouble(text);
            prices.add(price);
            System.out.println(price);

        }

        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        assertEquals(sortedPrices, prices, "Results should be sorted by price in ascending order");
    }

    // TEST SCENARIO: SAVE PRODUCT
    @Test
    public void testSaveProduct() throws InterruptedException {
        testValidUnverifiedUserLogin();
        // PROBLEM: Ako je item vec dodan u saved products, onda imamo error

        scrollToY(1100);
        Thread.sleep(1000);

        WebElement firstProduct = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[2]/article[1]/a")));
        firstProduct.click();

        WebElement productTitleElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div[1]/div[2]/div[3]/div[1]/h1")));
        String productTitle = productTitleElement.getText();

        WebElement saveButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div[1]/div[1]/a")));
        saveButton.click();

        webDriver.get(baseUrl + "profil/spaseni-oglasi");

        scrollToY(500);

        WebElement savedProductElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div[2]/article/div[3]/div[1]/h3")));
        String savedProductTitle = savedProductElement.getText();

        assertEquals(productTitle, savedProductTitle, "Saved product title should be equal to the added product title");

        Thread.sleep(3000);
    }

    // TEST SCENARIO: NAVIGATE LINKS
    @Test
    public void _testNavigationLinks() throws InterruptedException {
        webDriver.get(baseUrl);

        JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(3000);

        List<WebElement> navLinks = webDriver.findElements(By.xpath("//a[@class='menu__item']")); // Adjust XPath for your navigation links

        for (WebElement link : navLinks) {
            String linkText = link.getText();
            String linkHref = link.getAttribute("href");

            if (!linkHref.equals("mailto:info@oruzje.net") && !linkHref.equals("/")) {
                link.click();

                assertTrue(webDriver.getCurrentUrl().contains(linkHref), "URL is not good for link: " + linkText);

                String pageTitle = webDriver.getTitle();
                System.out.println("Current page: " + pageTitle);

                webDriver.navigate().back();
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"https://oruzje.net/lovacko-oruzje", "https://oruzje.net/polovno-oruzje", "https://oruzje.net/zastava-oruzje", "https://oruzje.net/o-nama"})
    public void testNavigationLinks(String link) throws InterruptedException {
        webDriver.get(baseUrl);

        JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1000);

        WebElement linkElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='" + link + "']")));
        linkElement.click();

        String url = webDriver.getCurrentUrl();
        assertEquals(link, url);
    }

    // TEST SCENARIO: ADD PRODUCT
    @Test
    public void testAddProduct() throws InterruptedException {
        testValidVerifiedUserLogin();

        // WebElement addProductButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='https://oruzje.net/novi-oglas/osnovne-informacije']")));
        WebElement addProductButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/a")));
        addProductButton.click();

        WebElement titleInput = webDriver.findElement(By.name("title"));
        Select categorySelect = new Select(webDriver.findElement(By.name("category_id")));
        Select subcategorySelect = new Select(webDriver.findElement(By.name("subcategory_id")));
        WebElement priceInput = webDriver.findElement(By.name("price"));
        // WebElement stateInput = webDriver.findElement(By.name("state"));
        WebElement stateInputLabel = webDriver.findElement(By.xpath("//label[@for='radio-1']"));

        scrollToY(600);
        Thread.sleep(1000);

        titleInput.sendKeys("Test Product");
        categorySelect.selectByValue("38");
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        subcategorySelect.selectByValue("85");
        priceInput.sendKeys("100");
        // stateInput.click();
        stateInputLabel.click();

        scrollToY(800);
        Thread.sleep(1000);

        WebElement buttonNext = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div[2]/form/div/div/div[2]/div/button")));
        buttonNext.click();

        scrollToY(700);
        Thread.sleep(1000);

        // Select the iframe so we can switch the context to it and select the input paragraph
        WebElement iframe = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div/div[2]/form/div/div[1]/div/div/div/div/div/iframe")));
        webDriver.switchTo().frame(iframe); // Switch context

        WebElement paragraphInput = webDriver.findElement(By.xpath("/html/body/p"));
        javascriptExecutor.executeScript("arguments[0].innerHTML ='Test Input For Description';", paragraphInput);

        webDriver.switchTo().defaultContent(); // Switch back context

        buttonNext = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div/div[2]/form/div/div[2]/div/button")));
        buttonNext.click();

        scrollToY(1100);
        Thread.sleep(1000);

        // Cannot use webDriverWait for image input since it is hidden
        WebElement imageInput = webDriver.findElement(By.xpath("/html/body/input"));
        imageInput.sendKeys("C:\\Users\\User\\OneDrive\\Slike\\Snimke zaslona\\testImage.png");
        // Wait for image to post
        Thread.sleep(4000);

        WebElement postProductButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div/div[2]/form/div/div[2]/div/button")));
        postProductButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: UPDATE PRODUCT
    @Test
    public void testUpdateProductTitle() throws InterruptedException {
        testValidVerifiedUserLogin();

        webDriver.get("https://oruzje.net/profil/oglasi");

        scrollToY(600);
        Thread.sleep(1000);

        WebElement expandOptionsButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='product__edit']")));
        expandOptionsButton.click();

        // Cannot use href selector since it is dependent on the product name
        WebElement editProductButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div[2]/article/div[3]/ul/li[1]/a")));
        editProductButton.click();

        scrollToY(600);
        Thread.sleep(1000);

        WebElement titleInput = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("title")));
        titleInput.clear();
        titleInput.sendKeys("Test Product Auto Update");

        scrollToY(1200);
        Thread.sleep(1000);

        WebElement saveChangesButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div[2]/form/div/div/div[2]/div/button")));
        saveChangesButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: FINALIZE PRODUCT
    @Test
    public void testProductFinalization() throws InterruptedException {
        testValidVerifiedUserLogin();

        webDriver.get("https://oruzje.net/profil/oglasi");

        scrollToY(600);
        Thread.sleep(1000);

        // Compound class names not permitted -> when using By.className, so we use By.xpath
        WebElement productTitleElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@class='product__title font-semibold color-primary']")));
        String title = productTitleElement.getText();

        WebElement expandOptionsButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='product__edit']")));
        expandOptionsButton.click();

        // Cannot use href selector since it is dependent on the product name
        WebElement finalizeProductButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div[2]/article/div[3]/ul/li[3]/a")));
        finalizeProductButton.click();

        WebElement confirmFinalizationButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div[2]/button[1]")));
        confirmFinalizationButton.click();

        Thread.sleep(1000);
        webDriver.get("https://oruzje.net/profil/zavrseni-oglasi");

        scrollToY(400);
        Thread.sleep(1000);

        WebElement finalizedProductTitle = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@class='product__title font-semibold color-primary']")));
        String finalizedTitle = finalizedProductTitle.getText();

        assertEquals(title, finalizedTitle, "Title of finalized product should match before and after finalization");

        Thread.sleep(3000);
    }

    // TEST SCENARIO: DELETE PRODUCT
    @Test
    public void testDeleteProduct() throws InterruptedException {
        testValidVerifiedUserLogin();

        webDriver.get("https://oruzje.net/profil/oglasi");

        scrollToY(600);
        Thread.sleep(1000);

        WebElement expandOptionsButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='product__edit']")));
        expandOptionsButton.click();

        // Cannot use href selector since it is dependent on the product name
        WebElement deleteProductButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div/div[2]/article/div[3]/ul/li[4]/a")));
        deleteProductButton.click();

        WebElement confirmDeleteButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div[2]/button[1]")));
        confirmDeleteButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: SEND MESSAGE
    @Test
    public void testSendMessage() throws InterruptedException {
        testValidUnverifiedUserLogin();

        // This is the product that we added with the above test
        webDriver.get("https://oruzje.net/oglas/test-product-3");

        scrollToY(600);
        Thread.sleep(1000);

        WebElement sendMessageButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div[1]/div[2]/div[5]/div[2]/div[2]/a")));
        sendMessageButton.click();

        WebElement textAreaInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("message")));
        textAreaInput.sendKeys("Hello there, this is a test message sent by an automated test");

        WebElement sendButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div/div/div/div[2]/div[2]/div/form/div/button")));
        sendButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: POST QUESTION
    @Test
    public void testPostQuestion() throws InterruptedException {
        testValidUnverifiedUserLogin();

        webDriver.get("https://oruzje.net/oglas/test-product-3");

        scrollToY(1800);
        Thread.sleep(1000);

        // WebElement textAreaInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("content")));
        // WebElement textAreaInput = webDriver.findElement(By.name("content"));
        WebElement textAreaInput = webDriver.findElement(By.xpath("/html/body/div[1]/div/div/div/section/div/form/textarea"));
        textAreaInput.sendKeys("Hi, this is a message sent from an automated test :)");

        WebElement postQuestionButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div/section/div/form/button")));
        postQuestionButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: EDIT PROFILE
    @Test
    public void testEditProfile() throws InterruptedException {
        testValidUnverifiedUserLogin();

        WebElement profileIcon = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='header__user has-message']")));
        profileIcon.click();

        WebElement editProfileLink = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='https://oruzje.net/profil/informacije']")));
        editProfileLink.click();

        scrollToY(800);
        Thread.sleep(1000);

        WebElement nameInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        WebElement yearInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("year")));

        // We need to clear default values so our new values don't just get appended
        nameInput.clear();
        yearInput.clear();

        nameInput.sendKeys("Svvt Test Updated");
        yearInput.sendKeys("1999");

        WebElement saveChangesButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='c-form__button button button--primary unset-width']")));
        saveChangesButton.click();

        Thread.sleep(3000);
    }

    // TEST SCENARIO: CHANGE PASSWORD
    @Test
    public void testChangePassword() throws InterruptedException {
        testValidUnverifiedUserLogin();

        WebElement profileIcon = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='header__user has-message']")));
        profileIcon.click();

        WebElement editProfileLink = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='https://oruzje.net/profil/promjeni-sifru']")));
        editProfileLink.click();

        WebElement newPasswordInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        WebElement confirmPasswordInput = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password_confirmation")));

        newPasswordInput.sendKeys("newpassword123");
        confirmPasswordInput.sendKeys("newpassword1234");

        WebElement saveChangesButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/div/div/form/div/div/div/button")));
        saveChangesButton.click();

        Thread.sleep(3000);
    }

    // SCENARIO: SECURITY
    @Test
    public void testForHttps() {
        webDriver.get(baseUrl);
        String currentUrl = webDriver.getCurrentUrl();
        assertTrue(currentUrl.startsWith("https"), "Website should be using HTTPS");
    }

    // TEST SCENARIO: PRODUCT DETAILS

    @ParameterizedTest
    @CsvSource({"profil/oglasi", "profil/zavrseni-oglasi", "profil/spaseni-oglasi", "novi-oglas/osnovne-informacije", "poruke", "profil/informacije"})
    public void testProtectedRoutes(String protectedRoute) {
        // Protected route
        webDriver.get(baseUrl + protectedRoute);
        String currentUrl = webDriver.getCurrentUrl();
        assertEquals("https://oruzje.net/prijava", currentUrl, "Guest should be redirected to the login page");
    }
}
