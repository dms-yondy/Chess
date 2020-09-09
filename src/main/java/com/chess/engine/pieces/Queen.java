package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {
    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        Piece bishop = new Bishop(this.piecePosition, this.pieceAlliance);
        Piece rook = new Rook(this.piecePosition, this.pieceAlliance);
        legalMoves.addAll(bishop.calculateLegalMoves(board));
        legalMoves.addAll(rook.calculateLegalMoves(board));
        return legalMoves;
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovePiece().getPieceAlliance());
    }
}
