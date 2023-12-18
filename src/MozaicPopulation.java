import java.util.Arrays;
import java.util.Random;

public class MozaicPopulation {
    public Mosaic[] population;
    public int fittest;
    public Random randomizer = new Random(123);


    public MozaicPopulation(int n,int size,int[][] board) {
        this.population = new Mosaic[size];
        for (int i = 0; i < size; i++) {
            population[i]=new Mosaic(n,board);
        }
    }

    public Mosaic getFittest(){
        int min = Integer.MAX_VALUE;
        int idx = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].getHeuristic()<min){
                min=population[i].getHeuristic();
                idx=i;
            }
        }
        this.fittest=min;
        return population[idx];
    }

    public Mosaic[] selectParent(){
        Mosaic[] res = new Mosaic[2];
        Arrays.sort(population);
        int maxHeuristic = population[population.length-1].getHeuristic();
        double[] probabilities = new double[population.length];
        double total = 0;
        for (int i = 0; i < population.length; i++) {
            int curr = maxHeuristic + 1 - population[i].getHeuristic();
            probabilities[i] = curr;
            total += curr;
        }
        for (int i = 0; i < population.length; i++) {
            probabilities[i] /= total;
        }
        //ambil parent ke-1
        int i = 0;
        float parent1 = randomizer.nextFloat();
        while(parent1 > probabilities[i]){
            parent1 -= probabilities[i];
            i++;
        }
        res[0] = population[i];
        double temp = probabilities[i];
        probabilities[i] = 0;
        //ambil parent ke-2
        i = 0;
        float parent2 = randomizer.nextFloat();
        while(parent2 > probabilities[i]){
            parent2 -= (probabilities[i] + (probabilities[i] * (temp / (total - temp))));
            i++;
        }
        res[1] = population[i];
        return res;
    }

    public Mosaic[] crossOver(Mosaic parent1, Mosaic parent2){
        int boardSize = parent1.getBomBoard().length;
        Mosaic[] res = new Mosaic[2];

        //single line crossover
        int crossoverPoint = (int)(randomizer.nextFloat() * boardSize);
        int child1[][] = new int[boardSize][boardSize];
        int child2[][] = new int[boardSize][boardSize];

        for(int i = 0; i < boardSize; i++){
            if(i <= crossoverPoint){
                child1[i] = parent1.getBomBoard()[i];
                child2[i] = parent2.getBomBoard()[i];
            } else {
                child1[i] = parent2.getBomBoard()[i];
                child2[i] = parent1.getBomBoard()[i];
            }
        }
        res[0] = new Mosaic(parent1.getBoard(), child1);
        res[1] = new Mosaic(parent2.getBoard(), child2);

        return res;
    }
}