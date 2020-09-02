package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.Collection;
import java.util.List;


public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    protected Move getMoveType(Board board, Piece movePiece, int candidateDestinationCoordinate) {
        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
        if(!candidateDestinationTile.isTileOccupied()) {
            return new Move.MajorMove(board, movePiece, candidateDestinationCoordinate);
        }
        else {
            final Piece pieceAtDestinationTile = candidateDestinationTile.getPiece();
            final Alliance pieceAlliance = pieceAtDestinationTile.getPieceAlliance();
            if(this.pieceAlliance != pieceAlliance) {
                return new Move.AttackMove(board, this,
                        candidateDestinationCoordinate, pieceAtDestinationTile);
            }
        }
        return null;
    }

    protected boolean isFirstMove() {
        return isFirstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
