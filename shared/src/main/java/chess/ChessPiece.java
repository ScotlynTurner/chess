package chess;

import chess.pieces.*;

import javax.swing.text.Position;
import java.util.HashSet;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        this.type=type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessPosition> endPositions = new HashSet<ChessPosition>();
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        ChessMove move;
        Boolean promote = Boolean.FALSE;
        if(this.type == PieceType.KING) {
            KingMovesCalculator calc = new KingMovesCalculator(board, myPosition, this.pieceColor);
            endPositions = calc.pieceMoves();
        } else if(this.type == PieceType.QUEEN) {
            QueenMovesCalculator calc = new QueenMovesCalculator(board, myPosition, this.pieceColor);
            endPositions = calc.pieceMoves();
        } else if(this.type == PieceType.BISHOP) {
            BishopMovesCalculator calc = new BishopMovesCalculator(board, myPosition, this.pieceColor);
            endPositions = calc.pieceMoves();
        } else if(this.type == PieceType.KNIGHT) {
            KnightMovesCalculator calc = new KnightMovesCalculator(board, myPosition, this.pieceColor);
            endPositions = calc.pieceMoves();
        } else if(this.type == PieceType.ROOK) {
            RookMovesCalculator calc = new RookMovesCalculator(board, myPosition, this.pieceColor);
            endPositions = calc.pieceMoves();
        } else if(this.type == PieceType.PAWN) {
            PawnMovesCalculator calc = new PawnMovesCalculator(board, myPosition, this.pieceColor);
            promote = calc.promoteStatus();
            endPositions = calc.pieceMoves();
        }
        for(ChessPosition i: endPositions) {
            if (promote) {
                move=new ChessMove(myPosition, i, PieceType.QUEEN);
                validMoves.add(move);
                move=new ChessMove(myPosition, i, PieceType.KNIGHT);
                validMoves.add(move);
                move=new ChessMove(myPosition, i, PieceType.BISHOP);
                validMoves.add(move);
                move=new ChessMove(myPosition, i, PieceType.ROOK);
                validMoves.add(move);
            } else {
                move=new ChessMove(myPosition, i, null);
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            if (this.type == PieceType.KING) {
                return "K";
            } else if (this.type == PieceType.QUEEN) {
                return "Q";
            } else if (this.type == PieceType.BISHOP) {
                return "B";
            } else if (this.type == PieceType.KNIGHT) {
                return "N";
            } else if (this.type == PieceType.ROOK) {
                return "R";
            } else if (this.type == PieceType.PAWN) {
                return "P";
            }
        } else {
            if (this.type == PieceType.KING) {
                return "k";
            } else if (this.type == PieceType.QUEEN) {
                return "q";
            } else if (this.type == PieceType.BISHOP) {
                return "b";
            } else if (this.type == PieceType.KNIGHT) {
                return "n";
            } else if (this.type == PieceType.ROOK) {
                return "r";
            } else if (this.type == PieceType.PAWN) {
                return "p";
            }
        }
      return " ";
    }
}
