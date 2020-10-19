package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.Collection;


public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
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
                return new Move.MajorAttackMove(board, this,
                        candidateDestinationCoordinate, pieceAtDestinationTile);
            }
        }
        return null;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(!(other instanceof Piece)) return false;
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.pieceType &&
               pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType {

        PAWN("P", 100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K", 1000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }


        public int getPieceValue() {
            return this.pieceValue;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
