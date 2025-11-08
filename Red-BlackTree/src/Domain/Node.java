package Domain;

public class Node {
    public int value;
    public Node left, right, parent;
    public Color color;

    public Node(int value) {
        this.value = value;
        left = right = parent = null;
        color = Color.RED;
    }

    public boolean isRed() {
        return color == Color.RED;
    }
}
