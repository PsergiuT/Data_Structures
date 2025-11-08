package Domain;

public class RedBlackTree {
    private Node root;
    private final Node NIL;

    public RedBlackTree() {
        NIL = new Node(0);
        NIL.color = Color.BLACK;
        root = NIL;
        root.parent = NIL;
    }

    private void LeftRotate(Node x) {
        Node y = x.right;

        // 1) configure x`s right connection
        x.right = y.left;
        y.left.parent = x;

        // 2) configure y`s parent
        if(x.parent == NIL){
            //x is the root
            root = y;
        }
        else if(x.parent.left == x){
            //x is in the left
            x.parent.left = y;
            y.parent = x.parent;
        }
        else{
            //x is in the right
            x.parent.right = y;
            y.parent = x.parent;
        }

        // 3) configure y`s left child
        y.left = x;
        x.parent = y;
    }

    private void RightRotate(Node y) {
        Node x = y.left;

        // 1) configure y`s left connection
        x.right.parent = y;
        y.left = x.right;

        // 2) configure x`s parent
        if(y.parent == NIL){
            //y is the root
            root = x;
        }
        else if(y.parent.right == y){
            //y is on the right
            y.parent.right = x;
            x.parent = y.parent;
        }
        else{
            //y is on the left
            y.parent.left = x;
            x.parent = y.parent;
        }

        // 3) configure x`s left child
        x.right = y;
        y.parent = x;
    }



    public void insert(int value){
        Node newNode = new Node(value);
        newNode.left = this.NIL;
        newNode.right = this.NIL;

        Node current = root;
        Node parent = NIL;

        while (current != NIL){
            parent = current;
            if(value < current.value){
                current = current.left;
            }else{
                current = current.right;
            }
        }

        newNode.parent = parent;
        if(parent == NIL)
            root = newNode;
        else{
            if(value < parent.value)
                parent.left = newNode;
            else
                parent.right = newNode;
        }

        insertFix(newNode);
    }



    private void insertFix(Node node){
        if(node.value == 3){
            System.out.println("dsjnj");
        }
        while(node.parent.color == Color.RED){
            // red to red conflict

            if(node.parent.parent.left == node.parent){
                //cases 1, 3, 5
                Node uncle = node.parent.parent.right;
                if(uncle.color == Color.RED){
                    //case 1
                    node.parent.parent.color = Color.RED;
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node = node.parent.parent;
                }
                else{
                    if(node.parent.right == node){
                        //case 5
                        node = node.parent;
                        LeftRotate(node);
                    }
                    //case 3
                    node.parent.parent.color = Color.RED;
                    node.parent.color = Color.BLACK;
                    RightRotate(node.parent.parent);
                }
            }
            else{
                //cases 2, 4, 6
                Node uncle = node.parent.parent.left;
                if(uncle.color == Color.RED){
                    //case 2
                    node.parent.parent.color = Color.RED;
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node = node.parent.parent;
                }
                else{
                    if(node.parent.left == node){
                        //case 6
                        node = node.parent;
                        RightRotate(node);
                    }
                    //case 4
                    node.parent.parent.color = Color.RED;
                    node.parent.color = Color.BLACK;
                    LeftRotate(node.parent.parent);
                }
            }
        }

        //always make
        root.color = Color.BLACK;
    }

    public Node getRoot() {
        return root;
    }

    public Node getNIL() {
        return NIL;
    }
}
