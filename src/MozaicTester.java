import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MozaicTester {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("src/test2.txt"));
        Random randomizer = new Random(2);
        FileWriter fw = new FileWriter("src/output.txt");

        //input number of testcase
        int tc = sc.nextInt();
        for (int q = 0; q < tc; q++) {
            int n = sc.nextInt();//input the size of the board
            int[][] board = new int[n][n];
            // input the board if there is no number in the tile then write -1
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    board[i][j]=sc.nextInt();
                }
            }
            // get all the parameter
            Scanner param = new Scanner(new File("src/params.txt"));
            int maxGeneration = param.nextInt();
            int populationSize = param.nextInt();
            double elitismPercentage = param.nextDouble();
            double crossoverRate = param.nextDouble();
            double mutationRate = param.nextDouble();
            // initialize the population
            MozaicPopulation population = new MozaicPopulation(n,populationSize,board,randomizer,elitismPercentage,crossoverRate,mutationRate);
            int cnt = 0;// the generation counter
            population.generateRandom();// get a random population
            population.computeAllFitness();// compute all of the fitness
            Mosaic fittest = population.getFittest();//get the best from the population
            while (cnt!=maxGeneration&&fittest.getHeuristic()>0){
                cnt++;// count the generation
                MozaicPopulation offspring = population.makeOffspring();//make the offspring by elitism
                // get the other of the population by crossover
                while (offspring.population.size()!=offspring.populationSize) {
                    Mosaic[] parents = population.selectParent();//select the parent
                    // calculate crossover by the crossover rate
                    if (randomizer.nextDouble()<population.crossoverRate) {
                        // do crossover
                        Mosaic child = population.crossOver(parents[0],parents[1]);
                        // do mutation by the mutation rate
                        if (randomizer.nextDouble()<offspring.mutationRate) {
                            child.doMutation();
                        }
                        // add the child to the offspring
                        offspring.addMozaic(child);
                    }

                }
                // change the population with the offspring
                population=offspring;
                // compute all the new fitness
                population.computeAllFitness();
                // get the best in the population
                fittest=population.getFittest();
            }
            // if solved
            if (fittest.getHeuristic()==0){
                fw.write(fittest.toString());
                fw.write("Successfull in "+cnt+" generation\n");
                System.out.println(fittest);
                System.out.println("Successfull in "+cnt+" generation");
            }
            // if not solve
            else {
                fw.write(fittest.toString());
                fw.write("the best in "+cnt+" generation\n");
                System.out.println(fittest);
                System.out.println("the best in "+cnt+" generation");
            }

        }
        fw.close();
    }
}
