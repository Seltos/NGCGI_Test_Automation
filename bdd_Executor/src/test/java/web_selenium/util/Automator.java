package web_selenium.util;


import web_selenium.contracts.IAutomator;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Automator implements IAutomator {

    private int mouseMoveTimeMs;

    public Automator() {
        this.mouseMoveTimeMs = 500;
    }

    @Override
    public void mouseClick(int x, int y) {
        try {
            Robot robot = new Robot();
            
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException ex) {
            Logger.getLogger(Automator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseMove(int x, int y) {
        mouseMove(x, y, this.mouseMoveTimeMs);
    }

    @Override
    public void mouseMove(int x, int y, int mouseMoveTimeMs) {
        try {
            Robot robot = new Robot();

            if (mouseMoveTimeMs <= 0) {
                robot.mouseMove(x, y);
                return;
            }

            Point startLoc = MouseInfo.getPointerInfo().getLocation();
            double startX = startLoc.x;
            double distanceX = Math.abs(startX - x);
            double currentX = startLoc.x;
            double currentY = startLoc.y;
            long startMillis = System.currentTimeMillis();
            int currentMillis = 0;
            double durationMillis = 1000;
            int sign = startX < x ? 1 : -1;

            // m = (y2 - y1) / (x2 - x1)
            double slope = ((double) y - startLoc.y) / (x - startLoc.x);

            while (currentMillis < durationMillis) {
                currentMillis = (int) (System.currentTimeMillis() - startMillis);
                currentX = startX + easeOutCubic(currentMillis / durationMillis) * distanceX * sign;
                // m = (y2 - ym) / (x2 - xm)
                // ym = y2 - m (x2 - xm)
                currentY = y - (slope * (x - currentX));
                robot.mouseMove((int) currentX, (int) currentY);
//                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }

            robot.mouseMove(x, y);
        } catch (AWTException ex) {
            Logger.getLogger(Automator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private double easeOutCubic(double currentTime, double startVal, double stepVal, double duration) {
//        currentTime /= duration;
//        --currentTime;
//        return stepVal * (currentTime * currentTime * currentTime + 1) + startVal;
//    }
    private double easeOutCubic(double currentPos) {
        return currentPos * (2 - currentPos);
    }
}
