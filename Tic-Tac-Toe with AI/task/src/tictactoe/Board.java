package tictactoe;

import java.util.*;

public class Board {
    private Scanner scanner = new Scanner(System.in);
    enum LABEL {
        X,
        O,
        NULL,
    }
    private Map<String, LABEL> labelMap = Map.of("X", LABEL.X, "O", LABEL.O, "_", LABEL.NULL);

    private final int SIZE = 3;

    private final LABEL[][] board = new LABEL[this.SIZE][this.SIZE];
    // private final Map<LABEL, Integer> initialState = new HashMap<>();

    public Board() {
        for (LABEL[] labels : this.board) {
            Arrays.fill(labels, LABEL.NULL);
        }
    }
    
    public void print() {
        System.out.println("---------");
        for (LABEL[] labels : this.board) {
            System.out.print("| ");
            for (LABEL label : labels) {
                System.out.printf("%s ", label.equals(LABEL.NULL) ? "_" : label);
            }
            System.out.print("|\n");
        }
        System.out.println("---------");
    }

    public void enterCells() {
        System.out.print("Enter cells: ");
        String[] input = this.scanner.nextLine().split("");
        int counter = 0;
        for (LABEL[] labels : this.board) {
            for (int i = 0; i < labels.length; i++) {
                LABEL label = labelMap.get(input[counter]);
                labels[i] = label;
                /*if (this.initialState.containsKey(label)) {
                    this.initialState.put(label, this.initialState.get(label) + 1);
                } else {
                    this.initialState.put(label, 1);
                }*/
                counter++;
            }
        }
        System.out.print("\n");
        this.print();
    }

    public void playerMove(Game.players player) {
        int[] input = new int[2];

        String[] inputString = this.scanner.nextLine().split(" ");
        for (int i = 0; i < inputString.length; i++) {
            input[i] = Integer.parseInt(inputString[i]);
        }

        int line, col;

        try {
            line = this.SIZE - input[1];
            col = input[0] - 1;
        } catch (Exception e) {
            System.out.println("You should enter numbers!");
            this.playerMove(player);
            return;
        }

        if (line < 0 || line >= this.SIZE || col < 0 || col >= this.SIZE) {
            System.out.println("Coordinates should be from 1 to 3!");
            this.playerMove(player);
            return;
        }

        if (!this.board[line][col].equals(LABEL.NULL)) {
            System.out.println("This cell is occupied! Choose another one!");
            this.playerMove(player);
            return;
        }

        this.board[line][col] = LABEL.valueOf(player.toString());
        this.print();
    }

    /*public Map<LABEL, Integer> getInitialState() {
        return initialState;
    }*/

    public void randomMove(Game.players player) {
        int[] move = getRandomMove();
        this.board[move[0]][move[1]] = LABEL.valueOf(player.toString());
        this.print();
    }

    private int[] getRandomMove() {
        ArrayList<int[]> acceptedMoves = new ArrayList<>();
        for (int i = 0; i < this.board.length; i++) {
            for (int i1 = 0; i1 < this.board[i].length; i1++) {
                if (this.board[i][i1].equals(LABEL.NULL)) {
                    acceptedMoves.add(new int[] {i, i1});
                }
            }
        }
        return acceptedMoves.get(this.getRandomNumber(acceptedMoves.size() - 1));
    }

    public void mediumAIMove(Game.players player, boolean notLoseMove, boolean moveRandom) {
        LABEL currentLabel = LABEL.valueOf(player.toString());
        LABEL moveLabel = LABEL.valueOf(player.toString());

        if (notLoseMove) {
            currentLabel = currentLabel.equals(LABEL.X) ? LABEL.O : LABEL.X;
        }

        for (int i = 0; i < this.board.length; i++) {
            LABEL[] line = this.board[i];
            int currentLabelCounter = 0;
            int[] emptyCell = {-1, -1};
            for (int i1 = 0; i1 < line.length; i1++) {
                if (line[i1].equals(currentLabel)) {
                    currentLabelCounter++;
                }
                if (line[i1].equals(LABEL.NULL)) {
                    emptyCell[0] = i;
                    emptyCell[1] = i1;
                }
            }
            if (currentLabelCounter == 2 && emptyCell[0] >= 0 && emptyCell[1] >= 0) {
                this.board[emptyCell[0]][emptyCell[1]] = moveLabel;
                this.print();
                return;
            }
        }

        for (int colIndex = 0; colIndex < this.board.length; colIndex++) {
            int currentLabelCounter = 0;
            int[] emptyCell = {-1, -1};
            for (int rowIndex = 0; rowIndex < this.SIZE; rowIndex++) {
                LABEL cell = this.board[rowIndex][colIndex];
                if (cell.equals(currentLabel)) {
                    currentLabelCounter++;
                }
                if (cell.equals(LABEL.NULL)) {
                    emptyCell[0] = rowIndex;
                    emptyCell[1] = colIndex;
                }
            }
            if (currentLabelCounter == 2 && emptyCell[0] >= 0 && emptyCell[1] >= 0) {
                this.board[emptyCell[0]][emptyCell[1]] = moveLabel;
                this.print();
                return;
            }
        }

        if (this.board[0][0].equals(currentLabel) && this.board[1][1].equals(currentLabel) && this.board[2][2].equals(LABEL.NULL)) {
            this.board[2][2] = moveLabel;
            this.print();
            return;
        }
        if (this.board[0][0].equals(currentLabel) && this.board[1][1].equals(LABEL.NULL) && this.board[2][2].equals(currentLabel)) {
            this.board[1][1] = moveLabel;
            this.print();
            return;
        }
        if (this.board[0][0].equals(LABEL.NULL) && this.board[1][1].equals(currentLabel) && this.board[2][2].equals(currentLabel)) {
            this.board[0][0] = moveLabel;
            this.print();
            return;
        }

        if (this.board[0][2].equals(currentLabel) && this.board[1][1].equals(currentLabel) && this.board[2][0].equals(LABEL.NULL)) {
            this.board[2][0] = moveLabel;
            this.print();
            return;
        }
        if (this.board[0][2].equals(currentLabel) && this.board[1][1].equals(LABEL.NULL) && this.board[2][0].equals(currentLabel)) {
            this.board[1][1] = moveLabel;
            this.print();
            return;
        }
        if (this.board[0][2].equals(LABEL.NULL) && this.board[1][1].equals(currentLabel) && this.board[2][0].equals(currentLabel)) {
            this.board[0][2] = moveLabel;
            this.print();
            return;
        }

        if (moveRandom) {
            randomMove(player);
        } else {
            mediumAIMove(player, true, true);
        }
    }

    private int getRandomNumber(int end) {
        return (int) (Math.random() * end);
    }

    public int getSIZE() {
        return SIZE;
    }

    public LABEL[][] getBoard() {
        return this.board;
    }
}
