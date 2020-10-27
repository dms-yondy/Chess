package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MinMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    
    public MinMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + "THINKING with depth " + this.searchDepth);
        int numMoves = board.getCurrentPlayer().getLegalMoves().size();

        for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1) :
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1);
                if(board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                } else if(board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
                bestMove = move;
            }
        }

        final long executionTime = System.currentTimeMillis();
        return bestMove;
    }

    public int min(final Board board, final int depth) {
        if(depth == 0 || isEndGameScenario(board)) return this.boardEvaluator.evaluate(board, depth);
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                lowestSeenValue = Math.min(lowestSeenValue, currentValue);
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board, final int depth) {
        if(depth == 0 || isEndGameScenario(board)) return this.boardEvaluator.evaluate(board, depth);
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                highestSeenValue = Math.max(highestSeenValue, currentValue);
            }
        }
        return highestSeenValue;
    }

    private boolean isEndGameScenario(Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().isInStaleMate();
    }

    @Override
    public String toString() {
        return "MinMax";
    }
}
