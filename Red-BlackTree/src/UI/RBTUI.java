package UI;
import Domain.Node;

import javax.swing.*;
import java.awt.*;

public class RBTUI extends JPanel
{
    private Node root;
    private Node NIL;

    public void setRoot(Node root, Node NIL) {
        this.root = root;
        this.NIL = NIL;
    }

    public void redraw(){
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        if (root != NIL) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            drawNode(g2, root, getWidth() / 2, 50, getWidth() / 4);
        }
    }


    private void drawNode(Graphics2D g, Node node, int x, int y, int xOffset) {
        if (node == NIL)
            return;

        // Draw edges first
        if (node.left != NIL) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, x - xOffset, y + 80);
            drawNode(g, node.left, x - xOffset, y + 80, xOffset / 2);
        }

        if (node.right != NIL) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, x + xOffset, y + 80);
            drawNode(g, node.right, x + xOffset, y + 80, xOffset / 2);
        }

        // Draw the node (circle)
        int radius = 9;
        g.setColor(node.isRed() ? Color.RED : Color.BLACK);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // Draw border and value
        g.setColor(Color.BLACK);
        g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(node.value), x - 6, y + 4);
    }
}
