public class NegaMaxPlayer extends Player{
    final int DEPTH = 3;

    public NegaMaxPlayer(DiskColor color, String evaluationStrategy) {
        super(color);
        this.evaluationStrategy = evaluationStrategy;
    }

    @Override
    public Move play(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : board.getValidMoves(color)) {
            Board tempBoard = new Board(board);  // Clone the board to simulate moves
            tempBoard.placeDisk(move.getRow(), move.getCol(), color);

            //NEGAMAX
            int score = negamax(tempBoard, DEPTH, color); // or negamaxAlphaBeta if you want to test that

            if (score >= bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int negamax(Board board, int depth, DiskColor currentColor) {
        if (depth == 0 || !board.hasValidMoves(DiskColor.BLACK) && !board.hasValidMoves(DiskColor.WHITE)) {
            return board.evaluate(currentColor, evaluationStrategy) * (currentColor == color ? 1 : -1);
        }

        int maxEval = Integer.MIN_VALUE;
        DiskColor opponentColor = (currentColor == DiskColor.BLACK) ? DiskColor.WHITE : DiskColor.BLACK;

        for (Move move : board.getValidMoves(currentColor)) {
            Board tempBoard = new Board(board);
            tempBoard.placeDisk(move.getRow(), move.getCol(), currentColor);
            int eval = -negamax(tempBoard, depth - 1, opponentColor);
            maxEval = Math.max(maxEval, eval);
        }

        return maxEval;
    }
}
