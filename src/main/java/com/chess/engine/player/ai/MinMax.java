package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MinMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;

    public MinMax() {
        this.boardEvaluator = null;
    }

    @Override
    public Move execute(Board board, int depth) {
        return null;
    }

    @Override
    public String toString() {
        return "MinMax";
    }
}
