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
        Random randomizer = new Random(1);
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

            MozaicPopulation population = new MozaicPopulation(n,100,board,randomizer,0.05,0.85,0.001);
            int cnt = 0;
            population.generateRandom();
            population.computeAllFitness();
            Mosaic fittest = population.getFittest();
            while (fittest.getHeuristic()>=1){
                cnt++;
//                System.out.println(fittest.getHeuristic());
                MozaicPopulation offspring = population.makeOffspring();
                while (offspring.population.size()!=offspring.populationSize) {
                    Mosaic[] parents = population.selectParent();
                    if (randomizer.nextDouble()<population.crossoverRate) {
                        //System.out.println("crossed");
                        Mosaic child = population.crossOver(parents[0],parents[1]);
                        if (randomizer.nextDouble()<offspring.mutationRate) {
                            //System.out.println("mutate");
                            child.doMutation();
                        }
                        offspring.addMozaic(child);
                    }
                    //else System.out.println("not crossed");
                }
                population=offspring;
                population.computeAllFitness();
                fittest=population.getFittest();
            }
            System.out.println(fittest);
            System.out.println("Successfull in "+cnt+" generation");

        }

    }
}
