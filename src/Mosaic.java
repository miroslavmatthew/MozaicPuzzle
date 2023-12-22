import java.util.Random;

public class Mosaic implements Comparable<Mosaic>{
    Random randgenerator;
    private int board[][];//board angka dari kasus
    private int bomBoard[][];// board berisi bom (1) dan kosong (0)
    private int heuristic; // nilai heuristic dari mosaic
    private int move [][]={{-1,-1},{0,-1},{1,-1},{-1,0},{0,0},{1,0},{-1,1},{0,1},{1,1}};// untuk mengecek ke sekeliling termasuk dirinya sendiri
    // initiate mosaic with the board
    public Mosaic(int[][] board, int[][] bomBoard,Random randomizer){
        this.board=board;
        this.bomBoard=bomBoard;
        this.randgenerator = randomizer;
        this.heuristic=calcHeuristic();
    }
    // initiate a random mosaic board
    public Mosaic(int n, int[][] board,Random randomizer) {
        this.board = board;
        this.bomBoard = new int[n][n];
        this.randgenerator=randomizer;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bomBoard[i][j]=randgenerator.nextInt(2);
            }
        }
        calcHeuristic();
    }
    // x and y valid inside the 2d array
    private boolean valid(int x,int y){
        if (x>=0&&x<board.length&&y>=0&&y< board.length){
            return true;
        }
        return false;
    }
    public int getHeuristic(){
        return calcHeuristic();
    }

    // menghitung heuristic dengan cara mencari selisih kotak kotak yang memiliki angka
    // jika kota tersebut lebih / kurang selisihnya dijumlahkan
    // semakin kecil semakin bagus karena jumlah bom dan angka sesuai
    // semakin besar semakin jelek karena ketidak sesuaian semakin besar
//    public int calcHeuristic(){
//        int res = 0;
//        int wrongTile =0;
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board.length; j++) {
//                if (board[i][j]!=-1){
//                    int totalBom = 0;
//                    for (int k = 0; k <= 8; k++) {
//                        if (valid(i+move[k][0],j+move[k][1])){
//                            if (bomBoard[i+move[k][0]][j+move[k][1]]==1){
//                                totalBom++;
//                            }
//                        }
//                    }
//                    res+=Math.abs(board[i][j]-totalBom);
//                }
//
//            }
//        }
//        this.heuristic=res;
//        return this.heuristic;
//    }

    // menghitung heuristic dengan cara mencari selisih kotak kotak yang memiliki angka
    // jika kotak memiliki bom yang lebih banyak dihitung lebih jelek dibanding kotak yang memiliki bom yang kurang
    // semakin kecil semakin bagus karena jumlah bom dan angka sesuai
    // semakin besar semakin jelek karena ketidak sesuaian semakin besar
    public int calcHeuristic(){
        int res = 0;
        int wrongTile =0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j]!=-1){
                    int totalBom = 0;
                    for (int k = 0; k <= 8; k++) {
                        if (valid(i+move[k][0],j+move[k][1])){
                            if (bomBoard[i+move[k][0]][j+move[k][1]]==1){
                                totalBom++;
                            }
                        }
                    }
                    //cek apakah tile kelebihan, kalau iya, maka bobot heuristicnya akan lebih besar(lebih jelek)
                    if (Math.abs(board[i][j]-totalBom)>0){
                        wrongTile++;
                        res += Math.abs(board[i][j]-totalBom) * 1.5;
                    } else {
                        res+=Math.abs(board[i][j]-totalBom);
                    }
                }

            }
        }
        this.heuristic=res;
        return this.heuristic;
    }
    // method untuk sorting
    @Override
    public int compareTo(Mosaic other) {
        return this.heuristic-other.heuristic;
    }
    // untuk mendapatkan bomboard
    public int[][] getBomBoard(){
        return this.bomBoard;
    }
    // untuk mendapatkan board angka
    public int[][] getBoard(){
        return this.board;
    }
    //flip a random tile from 1 to 0 or 0 to 1
//    public void doMutation(){
//        int i =  randgenerator.nextInt(bomBoard.length);
//        int j = randgenerator.nextInt(board.length);
//        bomBoard[i][j] = bomBoard[i][j] ^ 1;
//    }
    //flip bit pada satu cell random di tiap barisnya
    public void doMutation(){
        for (int k = 0; k < board.length; k++) {
            int j = randgenerator.nextInt(board.length);
            bomBoard[k][j] = bomBoard[k][j] ^ 1;
        }
    }
    // method untuk print bomboard pada mosaic
    @Override
    public String toString() {
        String a="";
        for (int i = 0; i < bomBoard.length; i++) {
            for (int j = 0; j < bomBoard.length; j++) {
                a+=(bomBoard[i][j]+" ");
            }
            a+='\n';
        }
        return a;
    }
}
