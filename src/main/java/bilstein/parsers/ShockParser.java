package bilstein.parsers;

import bilstein.SileniumUtil;
import bilstein.entities.ProductInfo;
import bilstein.entities.Shock;
import bilstein.entities.Spec;
import bilstein.entities.Detail;
import org.openqa.selenium.*;


import java.util.ArrayList;
import java.util.List;

public class ShockParser {

    private WebDriver driver;
    private Shock rawShock;

    public ShockParser(WebDriver driver, Shock rawShock) {
        this.driver = driver;
        this.rawShock = rawShock;
    }

    public Shock parse(){
        String imgLinks = getImgLinks();
        String productDesc = getProductDesc();
        String buyersGuide = getBuyersGuide();
        String docLinks = getDocLinks();
        List<Spec> specs = getSpecs();
        List<Detail> details = getDetails();
        List<ProductInfo> pInfos = getProductInfo();

        /*System.out.println(imgLinks);
        System.out.println(productDesc);
        System.out.println(buyersGuide);
        System.out.println(docLinks);
        for (Spec spec: specs){
            System.out.println(spec);
        }
        for (Detail detail: details){
            System.out.println(detail);
        }
        for (ProductInfo productInfo: pInfos){
            System.out.println(productInfo);
        }*/

        rawShock.setImgLinks(imgLinks);
        rawShock.setProductDesc(productDesc);
        rawShock.setBuyersGuide(buyersGuide);
        rawShock.setDocLinks(docLinks);
        rawShock.setSpecs(specs);
        rawShock.setDetails(details);
        rawShock.setpInfos(pInfos);
        rawShock.setDetailsParsed(true);

        SileniumUtil.sleepForTimeout(1000); //experienced problems with wrong buyers guide, need check.

        return rawShock;
    }

    private List<ProductInfo> getProductInfo() {

        while (true){
            try {
                WebElement pKeeprEl = null;
                List<ProductInfo> result = new ArrayList<>();
                By pKeeprBy = By.cssSelector("div[class$='summary']");
                pKeeprEl = SileniumUtil.waitForElement(pKeeprBy, driver);

                List<WebElement> rawProdEls = pKeeprEl.findElements(By.tagName("div"));
                for (WebElement prodEl: rawProdEls){
                    if (prodEl.getAttribute("id").length()==0){
                        ProductInfo info = new ProductInfo();
                        info.setShock(rawShock);
                        info.setpName(prodEl.findElement(By.className("label")).getText());
                        try {
                            info.setpValue(prodEl.findElement(By.className("productProperty")).getText());
                        }
                        catch (NoSuchElementException e){
                            info.setpValue(info.getpName());
                            info.setpName("Availability");
                        }
                        result.add(info);
                    }
                }
                return result;
            }
            catch (StaleElementReferenceException ignored){
                while (true){
                    try {
                        driver = SileniumUtil.getShockPage(driver, rawShock.getPartNo());
                        break;
                    }
                    catch (StaleElementReferenceException e){
                    }
                }
            }
        }
    }

    private List<Spec> getSpecs() {
        WebElement specKeeprEl = null;
        List<Spec> result = new ArrayList<>();
        By specKeeprBy = By.cssSelector("div[class='specs backBoxNoHover']");
        specKeeprEl = SileniumUtil.waitForElement(specKeeprBy, driver);
        List<WebElement> specPairEls = specKeeprEl.findElements(By.tagName("div"));
        for (WebElement specPairEl: specPairEls){
            try {
                Spec spec = new Spec();
                String specName = specPairEl.findElement(By.className("productLabel")).getText();
                String specVal = specPairEl.findElement(By.className("productProperty")).getText();
                spec.setShock(rawShock);
                spec.setSpecName(specName);
                spec.setSpecValue(specVal);
                result.add(spec);
            }
            catch (NoSuchElementException e){
                return result;
            }
        }

        return result;
    }

    private List<Detail> getDetails() {
        //Switch to detail El
        By detailTabBy = By.cssSelector("a[href^='#detail']");
        WebElement detailTabEl = SileniumUtil.waitForElementClickable(driver, detailTabBy);
        detailTabEl.click();
        while (!detailsLoaded()){
            SileniumUtil.sleepForTimeout(50);
        }
        By detailsBy = By.id("detail");
        WebElement detailKeeprEl = SileniumUtil.waitForElement(detailsBy,driver);
        List<Detail> result = new ArrayList<>();
        List<WebElement> detailPairEls = detailKeeprEl.findElements(By.tagName("div"));
        for (WebElement detailPairEl: detailPairEls){
            try {
                Detail detail = new Detail();
                String specName = detailPairEl.findElement(By.className("productLabel")).getText();
                String specVal = detailPairEl.findElement(By.className("productProperty")).getText();
                detail.setShock(rawShock);
                detail.setDetailName(specName);
                detail.setDetailValue(specVal);
                result.add(detail);
            }
            catch (NoSuchElementException e){
                return result;
            }
        }

        return result;
    }

    private boolean detailsLoaded() {
        By detailsBy = By.id("detail");
        WebElement detailKeeprEl = SileniumUtil.waitForElement(detailsBy,driver);
        List<WebElement> detailPairEls = detailKeeprEl.findElements(By.tagName("div"));
        for (WebElement element: detailPairEls){
            if (element.findElement(By.className("productLabel")).getText().length()==0){
                return false;
            }
        }

        return true;
    }

    private String getDocLinks() {
        WebElement docsElKeepr = null;
        By docsBy = By.id("downloadHeader");
        docsElKeepr = SileniumUtil.waitForElement(docsBy, driver);
        List<WebElement> docEls = docsElKeepr.findElements(By.className("detailLink"));
        StringBuilder docBuilder = new StringBuilder();
        for (WebElement docEl: docEls){
            docBuilder.append(docEl.getText());
            docBuilder.append("---");
            docBuilder.append(docEl.getAttribute("href"));
            docBuilder.append(System.lineSeparator());
        }

        int length = docBuilder.length();

        if (length>0){
            docBuilder.setLength(length-2);
        }

        return docBuilder.toString();
    }

    private String getBuyersGuide() {
        WebElement bGuideEl = null;
        By bGuideBy = By.id("buyersGuideLbl");
        bGuideEl = SileniumUtil.waitForElement(bGuideBy, driver);

        return bGuideEl.getText();
    }

    private String getProductDesc() {
        WebElement descEl = null;
        By descBy = By.className("productDescription");
        descEl = SileniumUtil.waitForElement(descBy, driver);

        return descEl.getText();
    }

    private String getImgLinks() {
        WebElement picKeeperEl;
        try {
            picKeeperEl = driver.findElement(By.cssSelector("div[class='navigation hidden-print']"));
        }
        catch (NoSuchElementException e){
            return "";
        }
        List<WebElement> picEls = picKeeperEl.findElements(By.tagName("img"));
        StringBuilder imgLinkBuilder = new StringBuilder();

        for (WebElement picEl: picEls){
            imgLinkBuilder.append(picEl.getAttribute("src"));
            imgLinkBuilder.append(System.lineSeparator());
        }
        int length = imgLinkBuilder.length();
        if (length>0){
            imgLinkBuilder.setLength(length-2);
        }

        return imgLinkBuilder.toString();
    }
}