package web_selenium;

public class CloseWindow extends SeleniumTestAction{

    @Override
    public void run() {
        super.run();

        driver.close();
    }
}
