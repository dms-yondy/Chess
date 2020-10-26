package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MinMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;

    public MinMax() {
        this.boardEvaluator = null;
    }

    @Override
    public Move execute(Board board, int depth) {
        return null;
    }

    public int min(final Board board, final int depth) {
        if(depth == 0) return this.boardEvaluator.evaluate(board, depth);
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
        if(depth == 0) return this.boardEvaluator.evaluate(board, depth);
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

    @Override
    public String toString() {
        return "MinMax";
    }
}
