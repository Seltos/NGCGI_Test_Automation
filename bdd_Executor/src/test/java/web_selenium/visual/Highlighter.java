package web_selenium.visual;

import java.awt.*;

/**
 * Creates a highlighted region on the screen.
 */
public class Highlighter {

    Color color;

    Circle highlightCircle;

    Rectangle highlightRectangle;

    double opacity;

    Window window;

    private Highlighter() {
        this.color = new Color(178, 0, 255, (int) (0.5 * 255));
        this.opacity = 0.5;
        this.highlightRectangle = highlightRectangle;
    }

    public Highlighter(Rectangle highlightRectangle) {
        this();
        this.highlightRectangle = highlightRectangle;
    }

    public Highlighter(Circle highlightCircle) {
        this();
        this.highlightCircle = highlightCircle;
    }

    public Color getColor() {
        return this.color;
    }

    public double getOpacity() {
        return this.opacity;
    }

    public void hide() {
        this.window.dispose();
    }

    private void paintCircle(Graphics g, Circle highlightCircle) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Highlighter.this.color);
        g2.setStroke(new BasicStroke(12));
//        g2.drawOval(
//                highlightCircle.x - highlightCircle.radius,
//                highlightCircle.y - highlightCircle.radius,
//                2 * highlightCircle.radius,
//                2 * highlightCircle.radius);
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.fillOval(
                highlightCircle.x - highlightCircle.radius,
                highlightCircle.y - highlightCircle.radius,
                2 * highlightCircle.radius,
                2 * highlightCircle.radius);
    }

    private void paintRectangle(Graphics g, Rectangle highlightRectangle) {
        g.setColor(Highlighter.this.color);
        g.fillRect(
                highlightRectangle.x,
                highlightRectangle.y,
                highlightRectangle.width,
                highlightRectangle.height);
    }

    public void setColor(Color color) {
        this.color = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int) (this.opacity * 255));
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
        setColor(new Color(
                this.color.getRed(),
                this.color.getGreen(),
                this.color.getBlue(),
                (int) (opacity * 255)));
    }

    public void show(Integer waitTimeMs) {
        this.window = new Window(null) {
            @Override
            public void paint(Graphics g) {
                this.setLocation(0, 0);
                if (Highlighter.this.highlightRectangle != null) {
                    paintRectangle(g, Highlighter.this.highlightRectangle);
                } else if (Highlighter.this.highlightCircle != null) {
                    paintCircle(g, highlightCircle);
                }
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }
        };

        this.window.setEnabled(false);
        this.window.setAlwaysOnTop(true);
        this.window.setBounds(this.window.getGraphicsConfiguration().getBounds());
        this.window.setBackground(new Color(0, true));
        this.window.setVisible(true);

        if (waitTimeMs != null) {
            try {
                Thread.sleep(waitTimeMs);
            } catch (InterruptedException ex) {
            }

            this.hide();
        }
    }
};
