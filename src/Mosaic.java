import java.util.Random;

public class Mosaic {
    Random randgenerator = new Random(10);
    private int board[][];
    private int bomBoard[][];
    private int heuristic;
    private int move [][]={{-1,-1},{0,-1},{1,-1},{-1,0},{0,0},{1,0},{-1,1},{0,1},{1,1}};

    public Mosaic(int n,int board[][]) {
        this.board = board;
        this.bomBoard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bomBoard[i][j]=randgenerator.nextInt(2);
            }
        }
        this.heuristic=calcHeuristic();
    }
    private boolean valid(int x,int y){
        if (x>=0&&x<board.length&&y>=0&&y< board.length){
            return true;
        }
        return false;
    }
    public int getHeuristic(){
        return heuristic;
    }
    private int calcHeuristic(){
        int res = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j]!=-1){
                    int totalBom = 0;
                    for (int k = 0; k < 8; k++) {
                        if (valid(i+move[k][0],j+move[k][1])){
                            if (bomBoard[i+move[k][0]][j+move[k][1]]==1){
                                totalBom++;
                            }
                        }
                    }
                    res+=Math.abs(board[i][j]-totalBom);
                }

            }
        }
        return res;
    }
}
