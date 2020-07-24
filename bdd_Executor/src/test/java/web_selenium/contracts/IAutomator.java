package web_selenium.contracts;

public interface IAutomator {
    public void mouseClick(int x, int y);
    
    public void mouseMove(int x, int y);
    
    public void mouseMove(int x, int y, int mouseMoveTimeMs);
}
