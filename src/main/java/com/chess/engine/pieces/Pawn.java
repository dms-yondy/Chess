package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition
                    + currentCandidateOffset * this.getPieceAlliance().getDirection();

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) continue;
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO more work is needed here!
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            }else if(currentCandidateOffset == 16 &&
                    ((BoardUtils.SECOND_RANK[this.piecePosition] && (this.getPieceAlliance().isWhite()))
                    || (BoardUtils.SEVENTH_RANK[this.piecePosition] && (this.getPieceAlliance().isBlack())))) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
            }
            else if((currentCandidateOffset == 7 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())
                    || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.getPieceAlliance() != this.pieceAlliance) {
                        legalMoves.add(new Move.MajorMove(board, this,
                                candidateDestinationCoordinate));
                    }
                }
            }else if(currentCandidateOffset == 9 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())
                    || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.getPieceAlliance() != this.pieceAlliance) {
                        legalMoves.add(new Move.MajorMove(board, this,
                                candidateDestinationCoordinate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovePiece().getPieceAlliance());
    }
}
