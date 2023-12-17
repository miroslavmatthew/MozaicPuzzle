import java.util.Arrays;
import java.util.Random;

public class MozaicPopulation {
    public Mosaic[] population;
    public int fittest;
    public int totalHeuristic;

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
                totalHeuristic += population[i].getHeuristic();
                idx=i;
            }
        }
        this.fittest=min;
        return population[idx];
    }

    public Mosaic[] selectParent(){
        Mosaic[] res = new Mosaic[2];
        Arrays.sort(population);
        double[] probabilities = new double[population.length];
        double newTotal = 0;
        for (int i = 0; i < population.length; i++) {
            int curr = totalHeuristic + 1 - population[i].getHeuristic();
            probabilities[i] = curr;
            newTotal += curr;
        }
        for (int i = 0; i < population.length; i++) {
            probabilities[i] /= newTotal;
        }
        int seed = 123;
        Random randomizer = new Random(seed);
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

        i = 0;
        float parent2 = randomizer.nextFloat();
        while(parent2 > probabilities[i]){
            parent2 -= (probabilities[i] + (probabilities[i] * (temp / (newTotal - temp))));
            i++;
        }
        res[1] = population[i];
        return res;
    }

    public void crossOver(Mosaic parent1, Mosaic parent2){

    }
}