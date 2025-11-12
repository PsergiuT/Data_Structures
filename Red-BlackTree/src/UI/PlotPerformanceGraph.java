package UI;

import Factory.RBTTimedFactory;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class PlotPerformanceGraph extends JPanel {
    private java.util.List<Point2D.Double> points;

    public PlotPerformanceGraph(int chunk_size) {
        RBTTimedFactory timeFactory = new RBTTimedFactory(chunk_size);
        points = timeFactory.getPoints();
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int margin = 80;

        // Axis bounds
        double maxX = points.stream().mapToDouble(p -> p.x).max().orElse(1);
        double maxY = points.stream().mapToDouble(p -> p.y).max().orElse(1);

        double scaleX = (width - 2.0 * margin) / maxX;
        double scaleY = (height - 2.0 * margin) / maxY;

        // Grid lines
        g2.setColor(new Color(230, 230, 230));
        int xDivs = 20;
        int yDivs = 10;

        for (int i = 0; i <= xDivs; i++) {
            int x = (int) (margin + i * (width - 2.0 * margin) / xDivs);
            g2.drawLine(x, height - margin, x, margin);
        }
        for (int i = 0; i <= yDivs; i++) {
            int y = (int) (height - margin - i * (height - 2.0 * margin) / yDivs);
            g2.drawLine(margin, y, width - margin, y);
        }

        // Axes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine( margin, height - margin, width - margin, height - margin); // X axis
        g2.drawLine( margin, height - margin, margin, margin); // Y axis

        // Axis titles
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Number of Elements (times 1000)", width / 2 - 60, height - 20);
        g2.drawString("Execution Time (ms)", -height / 2 + 400, 25);

        // Tick labels
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

        for (int i = 0; i <= xDivs; i++) {
            int x = (int) (margin + i * (width - 2.0 * margin) / xDivs);
            int value = (int) (i * maxX / xDivs);
            int comma = (int) ((value / 100) % 10);
            value = (int) (value / 1000);
            String label = String.format("%d.%dk", value, comma);
            g2.drawString(label, x - 10, height - margin + 20);
        }

        for (int i = 0; i <= yDivs; i++) {
            int y = (int) (height - margin - i * (height - 2.0 * margin) / yDivs);
            double value = i * maxY / yDivs;
            String label = String.format("%.2f", value);
            g2.drawString(label, margin - 50, y + 5);
        }


        //Draw data line
        g2.setColor(new Color(0, 120, 255));
        g2.setStroke(new BasicStroke(2f));
        Point2D.Double prev = null;
        for (Point2D.Double p : points) {
            int x = (int) (margin + p.x * scaleX);
            int y = (int) (height - margin - p.y * scaleY);
            if (prev != null) {
                int px = (int) (margin + prev.x * scaleX);
                int py = (int) (height - margin - prev.y * scaleY);
                g2.drawLine(px, py, x, y);
            }
            prev = p;
        }

        // Draw points
        g2.setColor(Color.RED);
        for (Point2D.Double p : points) {
            int x = (int) (margin + p.x * scaleX);
            int y = (int) (height - margin - p.y * scaleY);
            g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
        }
    }
}
