package tictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    Board board = new Board();

    enum players {
        X,
        O,
    }

    enum PlayerTypes {
        user,
        easy,
        medium,
        hard,
    }

    private final Map<players, PlayerTypes> playersMap = new HashMap<>();

    private players currentPlayer = players.X;

    public Game() {
        // board.enterCells();
        // Map<Board.LABEL, Integer> initialState = board.getInitialState();
        /*if (initialState.containsKey(Board.LABEL.X) && initialState.containsKey(Board.LABEL.O)) {
            currentPlayer = initialState.get(Board.LABEL.X) > initialState.get(Board.LABEL.O) ? players.O : players.X;
        } else {
            currentPlayer = players.X;
        }*/
        start();
    }

    private void start() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input command: ");
        String[] cmd = sc.nextLine().split(" ");
        String action = cmd[0];
        if (action.equals("start") && cmd.length == 3) {
            this.setup(cmd[1], cmd[2]);
        }
        if (action.equals("exit")) {
            System.exit(0);
        }
        System.out.println("Bad parameters!");
        start();
    }

    private void setup(String player1, String player2) {
        playersMap.put(players.X, PlayerTypes.valueOf(player1));
        playersMap.put(players.O, PlayerTypes.valueOf(player2));
        step();
    }

    private void step() {
        PlayerTypes type = playersMap.get(currentPlayer);
        if (!type.equals(PlayerTypes.user)) {
            System.out.printf("Making move level \"%s\"\n", type);
        }
        switch (type) {
            case easy:
                board.randomMove(currentPlayer);
                break;
            case medium:
            case hard:
                board.mediumAIMove(currentPlayer, false, false);
                //todo: add minimax algorithm
                break;
            default:
                System.out.print("Enter the coordinates: ");
                board.playerMove(currentPlayer);
        }
        this.checkGameStatus();
    }

    private Map<players, Boolean> checkForWin() {
        Map<players, Boolean> win = new java.util.HashMap<>(Map.of(players.X, false, players.O, false));

        int size = this.board.getSIZE();
        Board.LABEL[][] fields = this.board.getBoard();
        //loops through rows checking if win-condition exists
        for (int r = 0; r < size; r++) {
            if (equals3(fields[r][0], fields[r][1], fields[r][2])) {
                win.put(players.valueOf(fields[r][0].toString()), true);
            }
        }
        //loops through columns checking if win-condition exists
        for (int c = 0; c < 3; c++) {
            if (equals3(fields[0][c], fields[1][c], fields[0][c])) {
                win.put(players.valueOf(fields[0][c].toString()), true);
            }
        }
        //checks diagonals for win-condition
        if (equals3(fields[0][0], fields[1][1], fields[2][2])) {
            win.put(players.valueOf(fields[0][0].toString()), true);
        }

        if (equals3(fields[0][2], fields[1][1], fields[2][0])) {
            win.put(players.valueOf(fields[0][2].toString()), true);
        }

        return win;
    }

    private boolean equals3(Board.LABEL a, Board.LABEL b, Board.LABEL c) {
        return Objects.equals(a, b) && Objects.equals(b, c) && !Board.LABEL.NULL.equals(c);
    }

    private void checkGameStatus() {
        Map<players, Boolean> winners = this.checkForWin();

        if (winners.get(players.X) && winners.get(players.O)) {
            System.out.println("Impossible");
            return;
        }
        if (winners.get(players.X) || winners.get(players.O)) {
            players winPlayer = winners.get(players.X) ? players.X : players.O;
            System.out.println(winPlayer + " wins");
            return;
        }

        Board.LABEL[][] fields = this.board.getBoard();
        int countX = 0;
        int countO = 0;
        int countEmpty = 0;

        for (Board.LABEL[] line :
                fields) {
            for (Board.LABEL cell :
                    line) {
                if (cell.equals(Board.LABEL.X)) {
                    countX++;
                    continue;
                }
                if (cell.equals(Board.LABEL.O)) {
                    countO++;
                    continue;
                }
                countEmpty++;
            }
        }

        /*if (Math.abs(countX - countO) >= 2) {
            System.out.println("Impossible");
            return;
        }*/

        if (countEmpty == 0) {
            System.out.println("Draw");
            return;
        }

        // System.out.println("Game not finished");
        currentPlayer = currentPlayer.equals(players.O) ? players.X : players.O;
        this.step();
    }
}
