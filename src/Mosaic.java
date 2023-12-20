import java.util.Random;

public class Mosaic implements Comparable<Mosaic>{
    Random randgenerator;
    private int board[][];
    private int bomBoard[][];
    private int heuristic;
    private int move [][]={{-1,-1},{0,-1},{1,-1},{-1,0},{0,0},{1,0},{-1,1},{0,1},{1,1}};

    public Mosaic(int[][] board, int[][] bomBoard,Random randomizer){
        this.board=board;
        this.bomBoard=bomBoard;
        this.randgenerator = randomizer;
        this.heuristic=calcHeuristic();
    }
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
    private boolean valid(int x,int y){
        if (x>=0&&x<board.length&&y>=0&&y< board.length){
            return true;
        }
        return false;
    }
    public int getHeuristic(){
        return calcHeuristic();
    }
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
                    if (Math.abs(board[i][j]-totalBom)>0){
                        wrongTile++;
                    }
                    res+=Math.abs(board[i][j]-totalBom);
                }

            }
        }
        this.heuristic=res;
        return this.heuristic;
    }

    @Override
    public int compareTo(Mosaic other) {
        return this.heuristic-other.heuristic;
    }

    public int[][] getBomBoard(){
        return this.bomBoard;
    }

    public int[][] getBoard(){
        return this.board;
    }

    //flip a random tile from 1 to 0 or 0 to 1
    public void doMutation(){
        int i =  randgenerator.nextInt(bomBoard.length);
        int j = randgenerator.nextInt(board.length);
        bomBoard[i][j] = bomBoard[i][j] ^ 1;
    }

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
