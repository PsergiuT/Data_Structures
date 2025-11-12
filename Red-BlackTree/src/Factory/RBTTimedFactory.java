package Factory;

import Domain.RedBlackTree;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RBTTimedFactory {
    private final RedBlackTree redBlackTree;
    private List<Double> times = new ArrayList<>();
    private List<Point2D.Double> points = new ArrayList<>();

    public List<Point2D.Double> getPoints() {
        return points;
    }

    public RBTTimedFactory(int chunk_size){
        this.redBlackTree = new RedBlackTree();
        try(BufferedReader br = new BufferedReader(new FileReader("D:\\Data_Structures\\Red-BlackTree\\src\\File\\RedBlackTree.csv"))){
            String line;
            line = br.readLine();
            String[] parts = line.split(",");

            double time = 0.0;
            int chunks = parts.length / chunk_size;
            for(int i = 0; i < chunks; i++){
                long start = System.nanoTime();
                for(int j = 0; j < chunk_size; j++ ) {
                    redBlackTree.insert(Integer.parseInt(parts[chunk_size*i + j]));
                }
                long end = System.nanoTime();
                time += (end - start) / 1_000_000.0;
                times.add(time);
                points.add(new Point2D.Double(chunk_size * (i + 1), time));
            }

            for(int i = chunks * chunk_size; i < parts.length; i++){
                redBlackTree.insert(Integer.parseInt(parts[i]));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RedBlackTree getRedBlackTree() {
        return redBlackTree;
    }

    public List<Double> getTimes() {
        return times;
    }
}
