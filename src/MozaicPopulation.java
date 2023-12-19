import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MozaicPopulation {
    public ArrayList<Mosaic> population;
    public ArrayList<Mosaic> offSpring;
    public int fittest;
    public Random randomizer = new Random(123);
    public int populationSize;

    //initiate the population with a random board
    public MozaicPopulation(int n,int size,int[][] board) {
        this.population = new ArrayList<>();
        this.offSpring = new ArrayList<>();
        this.populationSize = size;
        for (int i = 0; i < size; i++) {
            population.add(new Mosaic(n,board));
        }
    }

    public Mosaic getFittest(){
        int min = Integer.MAX_VALUE;
        int idx = 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getHeuristic()<min){
                min=population.get(i).getHeuristic();
                idx=i;
            }
        }
        this.fittest=min;
        return population.get(idx);
    }

    public Mosaic[] selectParent(){
        Mosaic[] res = new Mosaic[2];
        Collections.sort(population);
        int maxHeuristic = population.get(population.size()-1).getHeuristic();
        double[] probabilities = new double[population.size()];
        double total = 0;
        for (int i = 0; i < population.size(); i++) {
            int curr = maxHeuristic + 1 - population.get(i).getHeuristic();
            probabilities[i] = curr;
            total += curr;
        }
        for (int i = 0; i < population.size(); i++) {
            probabilities[i] /= total;
        }
        //ambil parent ke-1
        int i = 0;
        double parent1 = randomizer.nextDouble();
        while(parent1 > probabilities[i]){
            parent1 -= probabilities[i];
            i++;
        }
        res[0] = population.get(i);
        double temp = probabilities[i];
        probabilities[i] = 0;
        //ambil parent ke-2
        i = 0;
        double parent2 = randomizer.nextDouble();
        while(parent2 > probabilities[i]){
//            parent2 -= (probabilities[i] + (probabilities[i] * (temp / (total - temp))));
            parent2 -= probabilities[i]/temp;
            i++;
        }
        res[1] = population.get(i);
        return res;
    }

    public Mosaic[] crossOver(Mosaic parent1, Mosaic parent2,boolean mutate){
        int boardSize = parent1.getBomBoard().length;
        Mosaic[] res = new Mosaic[2];

        //single line crossover
        int crossoverPoint = randomizer.nextInt(boardSize);
        int[][] child1 = new int[boardSize][boardSize];
        int[][] child2 = new int[boardSize][boardSize];

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
        if (mutate){
            int randomChild = randomizer.nextInt(2);
            res[randomChild].doMutation();
        }
        population.add(res[0]);
        population.add(res[1]);
        return res;
    }
    public void makeOffspring(){
        while (offSpring.size()!=populationSize){
            Collections.sort(population);
            int maxHeuristic = population.get(population.size()-1).getHeuristic();
            double[] probabilities = new double[population.size()];
            double total = 0;
            for (int i = 0; i < population.size(); i++) {
                int curr = maxHeuristic + 1 - population.get(i).getHeuristic();
                probabilities[i] = curr;
                total += curr;
            }
            for (int i = 0; i < population.size(); i++) {
                probabilities[i] /= total;
            }
            //ambil parent ke-1
            int i = 0;
            double parent1 = randomizer.nextDouble();
            while(parent1 > probabilities[i]){
                parent1 -= probabilities[i];
                i++;
            }
            offSpring.add(population.get(i));
        }
        population.clear();
        population = new ArrayList<>(offSpring);
        offSpring.clear();
    }
}