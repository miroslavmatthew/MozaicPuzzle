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

    // initiate the population parameter
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
    // compute all of the population fitness
    public void computeAllFitness(){
        for (int i = 0; i < population.size(); i++) {
            population.get(i).calcHeuristic();
        }
        population.sort(Mosaic::compareTo);
    }
    // generate random population
    public void generateRandom(){
        for (int i = 0; i < populationSize; i++) {
            population.add(new Mosaic(n,board,this.randomizer));
        }
    }
    // get the best fitness in the population
    public Mosaic getFittest(){
        return population.get(0);
    }
    // add a single mozaic to the population
    public void addMozaic(Mosaic target){
        population.add(target);
    }
    // select parent with roulette wheel
    public Mosaic[] selectParent(){
        Mosaic[] res = new Mosaic[2];
        int maxHeuristic = population.get(population.size()-1).getHeuristic();
        double[] probabilities = new double[population.size()];
        double total = 0;
        // we want the smaller heuristic to get better chance because smaller is better
        for (int i = 0; i < population.size(); i++) {
            int curr = maxHeuristic + 1 - population.get(i).getHeuristic();// make the smallest heuristic bigger
            probabilities[i] = curr;
            total += curr;
        }
        for (int i = 0; i < population.size(); i++) {
            probabilities[i] /= total;// make the probabilities by divide by total
        }
        for (int i = 0; i < 2; i++) {// select 2 parent
            int j=-1;
            double prob = this.randomizer.nextDouble();
            double sum = 0.0;
            do {
                j++;
                sum = sum + probabilities[j];
            } while(sum<prob);// jika belum sampai range randomnya tambah indeksnya lalu tambah sum dengan probablitiesnya
            // jika sum sudah > dari prob maka sudah di range randomnya
            res[i]=population.get(j);//jadikan parent dari probabilities yang terpilih
        }
        return res;
    }
    // 2 point cross over
//    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
//        int boardSize = parent1.getBomBoard().length;
//        //declare dulu papan hasil crossovernya
//        int[][] child1 = new int[boardSize][boardSize];
//        int[][] child2 = new int[boardSize][boardSize];
//
//        Mosaic[] res = new Mosaic[2];
//        int cnt = 0;
//        //2 point crossover
//        int rd1=3, rd2=28;
//        do {
//            rd1 = this.randomizer.nextInt(boardSize*boardSize-4)+2;//random point potongan
//            rd2 = this.randomizer.nextInt(boardSize*boardSize-4)+2;//random point potongan
//        } while(Math.abs(rd1-rd2)<=2);// sampai selisih potongannya lebih dari 2
//        int pos1 = Math.min(rd1,rd2);// posisi potongan pertama yang lebih kecil
//        int pos2 = Math.max(rd1,rd2);// posisi potongan kedua yang lebih besar
//        for (int i = 0; i < boardSize; i++) {
//            for (int j = 0; j < boardSize; j++) {
//                if (cnt!=pos1){// potongan pertama
//                    child1[i][j]=parent1.getBomBoard()[i][j];
//                    child2[i][j]=parent2.getBomBoard()[i][j];
//                    cnt++;
//                }
//                else if (cnt!=pos2){// potongan kedua
//                    child1[i][j]=parent2.getBomBoard()[i][j];
//                    child2[i][j]=parent1.getBomBoard()[i][j];
//                    cnt++;
//                }
//                else { //potongan ketiga
//                    child1[i][j]=parent1.getBomBoard()[i][j];
//                    child2[i][j]=parent2.getBomBoard()[i][j];
//                }
//            }
//        }
//        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
//        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
//        int randomChild = randomizer.nextInt(2);
//        return res[randomChild];
//    }
    // cross over dengan 1 point memotong secara horizontal
//    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
//        int boardSize = parent1.getBomBoard().length;
//        //declare dulu papan hasil crossovernya
//        int[][] child1 = new int[boardSize][boardSize];
//        int[][] child2 = new int[boardSize][boardSize];
//
//        Mosaic[] res = new Mosaic[2];
//        int crossoverPoint = (int)(randomizer.nextFloat() * (boardSize-1));//memilih memotong pada baris ke berapa
//
//        for(int i = 0; i < boardSize; i++){
//            if(i <= crossoverPoint){// bagian pertama
//                child1[i] = parent1.getBomBoard()[i];
//                child2[i] = parent2.getBomBoard()[i];
//            } else { // bagian kedua
//                child1[i] = parent2.getBomBoard()[i];
//                child2[i] = parent1.getBomBoard()[i];
//            }
//        }
//        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
//        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
//        int randomChild = randomizer.nextInt(2);
//        return res[randomChild];
//    }
    // cross over dengan 1 point memotong secara vertikal
//    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
//        int boardSize = parent1.getBomBoard().length;
//        //declare dulu papan hasil crossovernya
//        int[][] child1 = new int[boardSize][boardSize];
//        int[][] child2 = new int[boardSize][boardSize];
//
//        Mosaic[] res = new Mosaic[2];
//        int crossoverPoint = (int)(randomizer.nextFloat() * (boardSize-1));//memilih memotong pada baris ke berapa
//
//        for(int i = 0; i < boardSize; i++){
//            for (int j = 0; j < boardSize; j++) {
//                if(j <= crossoverPoint){ // bagian pertama
//                    child1[i][j] = parent1.getBomBoard()[i][j];
//                    child2[i][j] = parent2.getBomBoard()[i][j];
//                } else { // bagian kedua
//                    child1[i][j] = parent2.getBomBoard()[i][j];
//                    child2[i][j] = parent1.getBomBoard()[i][j];
//                }
//            }
//        }
//        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
//        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
//        int randomChild = randomizer.nextInt(2);
//        return res[randomChild];
//    }
    // cross over dengan menganti baris secara bergantian
//    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
//        int boardSize = parent1.getBomBoard().length;
//        //declare dulu papan hasil crossovernya
//        int[][] child1 = new int[boardSize][boardSize];
//        int[][] child2 = new int[boardSize][boardSize];
//
//        Mosaic[] res = new Mosaic[2];
//        //cross over selang seling barisnya
//            for(int i = 0; i < boardSize; i++){
//                for (int j = 0; j < boardSize; j++) {
//                    if(i % 2 == 0){// jika baris genap
//                        child1[i][j] = parent1.getBomBoard()[i][j];
//                        child2[i][j] = parent2.getBomBoard()[i][j];
//                    } else { // jika baris ganjil
//                        child1[i][j] = parent2.getBomBoard()[i][j];
//                        child2[i][j] = parent1.getBomBoard()[i][j];
//                    }
//                }
//            }
//        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
//        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
//        int randomChild = randomizer.nextInt(2);
//        return res[randomChild];
//    }
    // cross over dengan menganti kolom secara bergantian
//    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
//        int boardSize = parent1.getBomBoard().length;
//        //declare dulu papan hasil crossovernya
//        int[][] child1 = new int[boardSize][boardSize];
//        int[][] child2 = new int[boardSize][boardSize];
//
//        Mosaic[] res = new Mosaic[2];
//        //cross over selang seling kolomnya
//        for(int i = 0; i < boardSize; i++){
//            for (int j = 0; j < boardSize; j++) {
//                if(j % 2 == 0){// jika kolom genap
//                    child1[i][j] = parent1.getBomBoard()[i][j];
//                    child2[i][j] = parent2.getBomBoard()[i][j];
//                } else { // jika kolom ganjil
//                    child1[i][j] = parent2.getBomBoard()[i][j];
//                    child2[i][j] = parent1.getBomBoard()[i][j];
//                }
//            }
//        }
//        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
//        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
//        int randomChild = randomizer.nextInt(2);
//        return res[randomChild];
//    }
    // cross over ini menggabungkan beberapa crossover menjadi satu
    public Mosaic crossOver(Mosaic parent1, Mosaic parent2){
        int boardSize = parent1.getBomBoard().length;
        //declare dulu papan hasil crossovernya
        int[][] child1 = new int[boardSize][boardSize];
        int[][] child2 = new int[boardSize][boardSize];

        Mosaic[] res = new Mosaic[2];
        int cnt = 0;

        double chooseCrossover = this.randomizer.nextDouble();

        //ini merupakan kombinasi dari beberapa method crossover yang telah kami buat.
        if(chooseCrossover < 0.4){
            //2 point pivot crossover
            int rd1=3, rd2=28;
            do {
                rd1 = this.randomizer.nextInt(boardSize*boardSize-4)+2;//random point potongan
                rd2 = this.randomizer.nextInt(boardSize*boardSize-4)+2;//random point potongan
            } while(Math.abs(rd1-rd2)<=2);// sampai selisih potongannya lebih dari 2
            int pos1 = Math.min(rd1,rd2);// posisi potongan pertama yang lebih kecil
            int pos2 = Math.max(rd1,rd2);// posisi potongan kedua yang lebih besar

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (cnt!=pos1){ // potongan pertama
                        child1[i][j]=parent1.getBomBoard()[i][j];
                        child2[i][j]=parent2.getBomBoard()[i][j];
                        cnt++;
                    }
                    else if (cnt!=pos2){ // potongan kedua
                        child1[i][j]=parent2.getBomBoard()[i][j];
                        child2[i][j]=parent1.getBomBoard()[i][j];
                        cnt++;
                    }
                    else { // potongan ketiga
                        child1[i][j]=parent1.getBomBoard()[i][j];
                        child2[i][j]=parent2.getBomBoard()[i][j];
                    }
                }
            }
        } else if(chooseCrossover < 0.65){
            //single pivot crossover memotong secara horizontal
            int crossoverPoint = (int)(randomizer.nextFloat() * (boardSize-1));//memilih memotong pada baris ke berapa

            for(int i = 0; i < boardSize; i++){
                if(i <= crossoverPoint){// bagian pertama
                    child1[i] = parent1.getBomBoard()[i];
                    child2[i] = parent2.getBomBoard()[i];
                } else {// bagian kedua
                    child1[i] = parent2.getBomBoard()[i];
                    child2[i] = parent1.getBomBoard()[i];
                }
            }
        } else if(chooseCrossover < 0.9){
            //single pivot crossover memotong secara vertical
            int crossoverPoint = (int)(randomizer.nextFloat() * (boardSize-1));// memilih memotong pada kolom ke berapa

            for(int i = 0; i < boardSize; i++){
                for (int j = 0; j < boardSize; j++) {
                    if(j <= crossoverPoint){ // bagian pertama
                        child1[i][j] = parent1.getBomBoard()[i][j];
                        child2[i][j] = parent2.getBomBoard()[i][j];
                    } else { // bagian kedua
                        child1[i][j] = parent2.getBomBoard()[i][j];
                        child2[i][j] = parent1.getBomBoard()[i][j];
                    }
                }
            }
        } else if(chooseCrossover < 0.95){
            //cross over selang seling barisnya
            for(int i = 0; i < boardSize; i++){
                for (int j = 0; j < boardSize; j++) {
                    if(i % 2 == 0){// jika baris genap
                        child1[i][j] = parent1.getBomBoard()[i][j];
                        child2[i][j] = parent2.getBomBoard()[i][j];
                    } else { // jika baris ganjil
                        child1[i][j] = parent2.getBomBoard()[i][j];
                        child2[i][j] = parent1.getBomBoard()[i][j];
                    }
                }
            }
        } else {
            //cross over selang seling kolomnya
            for(int i = 0; i < boardSize; i++){
                for (int j = 0; j < boardSize; j++) {
                    if(j % 2 == 0){// jika kolom genap
                        child1[i][j] = parent1.getBomBoard()[i][j];
                        child2[i][j] = parent2.getBomBoard()[i][j];
                    } else { // jika kolom ganjil
                        child1[i][j] = parent2.getBomBoard()[i][j];
                        child2[i][j] = parent1.getBomBoard()[i][j];
                    }
                }
            }
        }
        res[0] = new Mosaic(parent1.getBoard(), child1,this.randomizer);
        res[1] = new Mosaic(parent2.getBoard(), child2,this.randomizer);
        int randomChild = randomizer.nextInt(2);
        return res[randomChild];
    }
    // membuat populasi dari persentase elitism
    public MozaicPopulation makeOffspring(){
        MozaicPopulation population2 = new MozaicPopulation(n,populationSize,board,randomizer,elitPct,crossoverRate,mutationRate);
        int n = (int) (elitPct*populationSize);// berapa banyak yang akan dibuat elitism nya
        for (int i = 0; i < n; i++) {
            population2.addMozaic(population.get(i));//masukan ke populasi sebanyak n
        }
        return population2;
    }
}