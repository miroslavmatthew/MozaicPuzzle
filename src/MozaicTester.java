//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MozaicTester {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Random randomizer = new Random(9);
//        System.setProperty("webdriver.chrome.driver","./chromedriver");
//        WebDriver driver = new ChromeDriver();
//        driver.get("https://www.puzzle-minesweeper.com/mosaic-5x5-easy/");
//        Document doc = Jsoup.parse(driver.getPageSource());
//        Elements board = doc.select("div.mosaic-cell-back");
//        Elements x = board.select(".number");
        int tc = sc.nextInt();
        for (int q = 0; q < tc; q++) {
            int n = sc.nextInt();
            int[][] board = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    board[i][j]=sc.nextInt();
                }
            }
            MozaicPopulation population = new MozaicPopulation(n,100,board);
            int cnt = 0;
            while (population.getFittest().getHeuristic()>1){
                cnt++;
                System.out.println(population.getFittest().getHeuristic());
                int childCounter = 0;
                while (childCounter!=50) {
                    Mosaic[] parent = population.selectParent();
                    //
                    double crossoverChance = randomizer.nextFloat();
                    if (crossoverChance>0.05) {
                        double mutationChance = randomizer.nextFloat();
                        if (mutationChance<0.001){
                            Mosaic[] child = population.crossOver(parent[0], parent[1],true);
                        }
                        else {
                            Mosaic[] child = population.crossOver(parent[0], parent[1],false);
                        }
                        childCounter++;
                    }
                }
                population.makeOffspring();
            }
            System.out.println("Successfull in "+cnt+" generation");

        }

    }
}
