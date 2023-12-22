/*
* kelompok 8
* Miroslav Matthew Alva 6182101004
* Kevin Christian 6182101018
* Vico Pratama 6182101011
* referensi kode contoh genetic algorithm di ms teams
* kami juga mengupload kode kami pada github https://github.com/miroslavmatthew/MozaicPuzzle.git
* kode akan mengeluarkan output pada file output.txt
* beberapa kode pada kode ini di comment karena digunakan saat eksperiment
* jika ingin memakai bisa comment kode yang akan diganti dan uncomment code yang ingin dipakai
* test.txt berisi 100 testcase puzzle 5x5
* test2.txt berisi 10 testcase puzzle dengan variasi 7x7, 10x10, 15x15, dan 20x20
* kode berjalan sekitar 2 menit 35 detik untuk test.txt
* kode berjalan sekitar 6 menit 10 detik untuk test2.txt
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MozaicTester {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("test.txt"));
//        Scanner sc = new Scanner(new File("src/test2.txt"));
        Random randomizer = new Random(2);
        FileWriter fw = new FileWriter("output.txt");

        //input number of testcase
        int tc = sc.nextInt();
        int ac = 0;//menghitung jumlah benar untuk menghitung akurasi
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
            Scanner param = new Scanner(new File("params.txt"));
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
                ac++;
            }
            // if not solve
            else {
                fw.write(fittest.toString());
                fw.write("the best in "+cnt+" generation\n");
            }

        }
        double accuracy = ac * 1.0 / tc;// count the accuracy
        fw.write("Acurracy = "+accuracy+"\n"+"right "+ac+" of "+tc+"\n");
        fw.close();
    }
}
