public class MozaicPopulation {
    public Mosaic[] population;
    public int fittest;

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

}
