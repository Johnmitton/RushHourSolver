package rushhour;

import java.io.File;
import java.io.FileFilter;

public class App {
    public static void main(String[] args) {
        Solver solver = new Solver();
        File folder = new File("./boards");

        File[] listOfFiles = folder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }

        });

        String solFile;

        for(File file : listOfFiles) {
            solFile = file.getName().replace(".txt", ".sol");
            solver.solveFromFile(file.toString(), solFile);
        }
    }
}
