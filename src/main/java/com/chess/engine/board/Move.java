package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

    protected final Board board;
    protected final Piece movePiece;
    protected final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    Move(final Board board, final Piece movePiece, final int destinationCoordinate) {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    private Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movePiece = null;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(!(other instanceof Move)) return false;
        Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate()
                && getCurrentCoordinate() == otherMove.getCurrentCoordinate()
                && getMovePiece().equals(otherMove.getMovePiece());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime *  result + this.destinationCoordinate;
        result = prime * result + this.movePiece.hashCode();
        result = prime * result + this.movePiece.getPiecePosition();
        return result;
    }

    public int getCurrentCoordinate() {
        return this.getMovePiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public Piece getMovePiece() {
        return movePiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }
    public Board execute() {
        final Board.Builder builder = new Board.Builder();

        for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if(!this.movePiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movePiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorAttackMove extends AttackMove {

        public MajorAttackMove(final Board board, final Piece pieceMoved, final int destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof MajorAttackMove && super.equals(other));
        }

        @Override
        public String toString() {
            return movePiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final Piece movePiece, final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movePiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movePiece, final int destinationCoordinate,
                   final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean equals(final Object other) {
            if(this == other) {
                return true;
            }
            if(!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public static final class PawnMove extends Move {

        public PawnMove(final Board board, final Piece movePiece, final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof AttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static final class PawnAttackMove extends AttackMove {

        final Piece attackedPiece;

        public PawnAttackMove(final Board board, final Piece movePiece, final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate, attackedPiece);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movePiece.getPiecePosition()).substring(0, 1) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnEnPassantPawnAttackMove extends AttackMove {

        final Piece attackedPiece;

        public PawnEnPassantPawnAttackMove(final Board board, final Piece movePiece, final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate, attackedPiece);
            this.attackedPiece = attackedPiece;
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, final Piece movePiece, final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public Board execute() {

            final Board.Builder builder = new Board.Builder();

            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movePiece.equals(piece)) builder.setPiece(piece);
            }
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movePiece.movePiece(this);
            builder.setPiece(this.movePiece.movePiece(this));
            builder.setEnPassant(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(destinationCoordinate);
        }
    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movePiece, final int destinationCoordinate, Rook castleRook,
                          int castleRookStart, int castleRookDestination) {
            super(board, movePiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movePiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movePiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board, final Piece movePiece, final int destinationCoordinate,  Rook castleRook,
                                  int castleRookStart, int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board, final Piece movePiece, final int destinationCoordinate,  Rook castleRook,
                                   int castleRookStart, int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {

        public NullMove() {
            super(null, null, 65);
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute the null move!");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for(final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate
                        && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
