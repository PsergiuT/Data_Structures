package Factory;

import Domain.RedBlackTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class RedBlackTreeFactory {
    private final RedBlackTree redBlackTree;

    public RedBlackTreeFactory(){
        this.redBlackTree = new RedBlackTree();
        try(BufferedReader br = new BufferedReader(new FileReader("D:\\Data_Structures\\Red-BlackTree\\src\\File\\RedBlackTree.csv"))){
            String line;
            line = br.readLine();
            String[] parts = line.split(",");

            for(int i = 0; i < parts.length; i++){
                redBlackTree.insert(Integer.parseInt(parts[i]));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RedBlackTree getRedBlackTree() {
        return redBlackTree;
    }
}
