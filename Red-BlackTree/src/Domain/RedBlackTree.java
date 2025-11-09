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

    private void LeftRotate(Node parent) {
        Node son = parent.right;

        // 1) configure parent`s right connection
        parent.right = son.left;
        son.left.parent = parent;

        // 2) configure son`s parent
        if(parent.parent == NIL){
            //parent is the root
            root = son;
            son.parent = NIL;
        }
        else if(parent.parent.left == parent){
            //parent is in the left
            parent.parent.left = son;
            son.parent = parent.parent;
        }
        else{
            //parent is in the right
            parent.parent.right = son;
            son.parent = parent.parent;
        }

        // 3) configure son`s left child
        son.left = parent;
        parent.parent = son;
    }

    private void RightRotate(Node parent) {
        Node son = parent.left;

        // 1) configure parent`s left child
        parent.left = son.right;
        son.right.parent = parent;

        // 2) configure son`s new parent
        if(parent.parent == NIL){
            // old parent is the root
            root = son;
            son.parent = NIL;
        }
        else if(parent.parent.right == parent){
            //old parent is the right child
            parent.parent.right = son;
            son.parent = parent.parent;
        }
        else{
            parent.parent.left = son;
            son.parent = parent.parent;
        }

        // 3) configure son`s right child
        son.right = parent;
        parent.parent = son;
    }


    private void transplant(Node old, Node newNode){
        // updates the child of the parent of old to point to new
        // updates the parent of new to point to old`s parent

        if(old.parent == NIL)
            root = newNode;
        else if(old.parent.right == old)
            old.parent.right = newNode;
        else
            old.parent.left = newNode;

        newNode.parent = old.parent;
    }


    private Node minimum(Node node){
        if(node.left != NIL)
            minimum(node.left);

        return node;
    }

    private void deleteNode(Node z){
        // z - node to be deleted
        // y - placeholder for z (the smallest number after z) only in case 3
        // x - placeholder for z in case 1 and 2 || placeholder for y in case 3
        Color OriginalColor = z.color;
        Node x;
        Node y;

        if(z.left == NIL){
            // z has no left child
            x = z.right;
            transplant(z, x);
            if(z.color == Color.BLACK && x.color == Color.RED) {
                //red child -> black child
                x.color = Color.BLACK;
                return;
            }
        }
        else if(z.right == NIL){
            // z has no right child
            x = z.left;
            transplant(z, x);
            if(z.color == Color.BLACK && x.color == Color.RED) {
                //red child -> black child
                x.color = Color.BLACK;
                return;
            }
        }
        else{
            // z has both left and right children
            y = minimum(z.right);
            OriginalColor = y.color;
            x = y.right;

            if(z.color == Color.BLACK && y.color == Color.RED) {
                //red child -> black child
                y.color = Color.BLACK;
            }

            if(y.parent != z){
                // transplant the right node of y into y`s place
                transplant(y, x);

                // connect y`s right child to z.right
                y.right = z.right;
                z.right.parent = y;
            }
            else
                //if x is a leaf
                x.parent = y;

            transplant(z, y);
            y.left = z.left;
            z.left.parent = y;
            y.color = z.color;

            if(y.color == Color.BLACK && x.color == Color.RED) {
                //black node with red child
                x.color = Color.BLACK;
                return;
            }
        }

        if(OriginalColor == Color.BLACK)
            deleteFixUp(x);
    }



    private void deleteFixUp(Node x){
        //implement the 6 cases

    }



    public void delete(int key){
        Node current = root;

        while(current.value != key && current != NIL){
            if(key < current.value)
                current = current.left;
            else
                current = current.right;
        }

        if(current == NIL)
            return;
        deleteNode(current);
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
        if(node == root){
            //always make root black
            root.color = Color.BLACK;
            return;
        }

        if(node.parent.color == Color.BLACK){
            return;
        }
        if(node.parent.parent.left == node.parent){
            // we are on the left side of the subtree
            // U has a RED parent
            if(node.parent.parent.right.color == Color.RED){
                // U has RED uncle
                node.parent.color = Color.BLACK;
                node.parent.parent.right.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                // pass the problem up the tree
                insertFix(node.parent.parent);
            }
            else{
                // U has BLACK uncle
                if(node.parent.right == node){
                    // U is close to the uncle
                    node = node.parent;
                    LeftRotate(node);
                }
                // U is far away from uncle
                // current node is the parent, after the left rotation
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                RightRotate(node.parent.parent);
            }
        }
        else{
            // we are on the right side of the subtree
            // U has a RED parent
            if(node.parent.parent.left.color == Color.RED){
                // U has RED uncle
                node.parent.color = Color.BLACK;
                node.parent.parent.left.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                // pass the problem up the tree
                insertFix(node.parent.parent);
            }
            else{
                // U has BLACK uncle
                if(node.parent.left == node){
                    // U is close to the uncle
                    node = node.parent;
                    RightRotate(node);
                }
                // U is far away from uncle
                // current node is the parent, after the right rotation
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                LeftRotate(node.parent.parent);
            }
        }
    }

    public Node getRoot() {
        return root;
    }

    public Node getNIL() {
        return NIL;
    }
}
