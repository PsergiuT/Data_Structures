package Domain;

import UI.RBTUI;

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
        while(node.left != NIL){
            node = node.left;
        }

        return node;
    }


    private Node findSibling(Node node){
        return isLeftChild(node) ? node.parent.right : node.parent.left;
    }

    private boolean isLeftChild(Node node){
        return node.parent.left == node;
    }


    public void delete(int data){
        deleteNode(root, data);
    }


    private void deleteNode(Node node, int data) {
        if (node == NIL) {
            return;
        }

        if(data == node.value){
            if(node.right == NIL || node.left == NIL){
                //the node that has to be deleted has only one child
                deleteOneChild(node);
            }
            else{
                //node has 2 children
                //we replace the current node`s data with it`s successor data
                //then we remove the successor
                Node successor = minimum(node.right);
                node.value = successor.value;
                //deleteNode(successor, successor.value);
                deleteNode(node.right, successor.value);
            }
        }

        if (data > node.value)
            deleteNode(node.right, data);
        else
            deleteNode(node.left, data);

    }


    private void deleteOneChild(Node node) {
        Node child = node.right == NIL ? node.left : node.right;

        //move child into parent`s place
        transplant(node, child);

        if(node.color == Color.BLACK){
            if(child.color == Color.RED){
                //if child is Red we make it black
                child.color = Color.BLACK;
            }
            else{
                //we have a double black case
                deleteCase1(child);
            }
        }
        //if the parent`s color is Red we don`t have to do anything
    }


    private void deleteCase1(Node doubleBlack){
        if(doubleBlack == root){
            return;
        }
        deleteCase2(doubleBlack);
    }


    private void deleteCase2(Node doubleBlack){
        Node sibling = findSibling(doubleBlack);
        if(doubleBlack.parent.color == Color.BLACK &&  sibling.color == Color.RED){
            if(isLeftChild(doubleBlack)){
                LeftRotate(doubleBlack.parent);
            }
            else{
                RightRotate(doubleBlack.parent);
            }
            doubleBlack.parent.color = Color.RED;
            sibling.color = Color.BLACK;
            deleteCase4(doubleBlack);
        }
        deleteCase3(doubleBlack);
    }


    private void deleteCase3(Node doubleBlack){
        Node sibling = findSibling(doubleBlack);
        if(doubleBlack.parent.color == Color.BLACK && sibling.color == Color.BLACK &&
                sibling.left.color == Color.BLACK && sibling.right.color == Color.BLACK){

            sibling.color = Color.RED;
            deleteCase1(doubleBlack.parent);
        }
        deleteCase4(doubleBlack);
    }


    private void deleteCase4(Node doubleBlack){
        Node sibling = findSibling(doubleBlack);
        if(doubleBlack.parent.color == Color.RED && sibling.color == Color.BLACK &&
                sibling.left.color == Color.BLACK && sibling.right.color == Color.BLACK){

            sibling.color = Color.RED;
            doubleBlack.parent.color = Color.BLACK;
            return;
        }
        deleteCase5(doubleBlack);
    }


    private void deleteCase5(Node doubleBlack){
        Node sibling = findSibling(doubleBlack);
        if(isLeftChild(doubleBlack)){
            if(sibling.color == Color.BLACK && sibling.left.color == Color.RED &&
                    sibling.right.color == Color.BLACK){

                RightRotate(sibling);
                sibling.color = Color.RED;
                sibling.parent.color = Color.BLACK;
            }
        }
        else{
            if(sibling.color == Color.BLACK && sibling.left.color == Color.BLACK &&
                    sibling.right.color == Color.RED){

                LeftRotate(sibling);
                sibling.color = Color.RED;
                sibling.parent.color = Color.BLACK;
            }
        }
        deleteCase6(doubleBlack);
    }


    private void deleteCase6(Node doubleBlack){
        Node sibling = findSibling(doubleBlack);

        sibling.color = sibling.parent.color;
        doubleBlack.parent.color = Color.BLACK;
        if(isLeftChild(doubleBlack)) {
            sibling.right.color = Color.BLACK;
            LeftRotate(doubleBlack.parent);
        }
        else{
            sibling.left.color = Color.BLACK;
            LeftRotate(doubleBlack.parent);
        }
    }


    public Node getRoot() {
        return root;
    }


    public Node getNIL() {
        return NIL;
    }
}
