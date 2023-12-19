import java.util.ArrayList;
import java.util.Random;

public class MozaicPopulation {
    public ArrayList<Mosaic> population;
    public Random randomizer;
    public int populationSize;
    public int n;
    public int[][] board;
    public double elitPct;
    public double crossoverRate;
    public double mutationRate;

    //initiate the population with a random board
    public MozaicPopulation(int n,int size,int[][] board,Random randomizer,double elitPct,double crossoverRate,double mutationRate) {
        this.population = new ArrayList<>();
        this.populationSize = size;
        this.randomizer = randomizer;
        this.elitPct = elitPct;
        this.crossoverRate=crossoverRate;
        this.mutationRate=mutationRate;
        this.n= n;
        this.board=board;
    }
    public void computeAllFitness(){
        for (int i = 0; i < population.size(); i++) {
            population.get(i).calcHeuristic();
        }
        population.sort(Mosaic::compareTo);
    }
    public void generateRandom(){
        for (int i = 0; i < populationSize; i++) {
            population.add(new Mosaic(n,board,this.randomizer));
        }
    }
    public Mosaic getFittest(){
        return population.get(0);
    }
    public void addMozaic(Mosaic target){
        population.add(target);
    }
    public Mosaic[] selectParent(){
        Mosaic[] res = new Mosaic[2];
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
            System.out.println(parent2+" "+probabilities[i]);
            parent2 -= probabilities[i]/temp;
            System.out.println(parent2);
            i++;
        }
        res[1] = population.get(i);
        return res;
    }

    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
        int boardSize = parent1.getBomBoard().length;
        Mosaic[] res = new Mosaic[2];

        //single line crossover
        int crossoverPoint = randomizer.nextInt(boardSize - 1);
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
        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
        int randomChild = randomizer.nextInt(2);
        return res[randomChild];
    }
    public MozaicPopulation makeOffspring(){
        MozaicPopulation population2 = new MozaicPopulation(n,populationSize,board,randomizer,elitPct,crossoverRate,mutationRate);
        int n = (int) (elitPct*populationSize);
        computeAllFitness();
        for (int i = 0; i < n; i++) {
            population2.addMozaic(population.get(i));
        }
        return population2;
    }
}