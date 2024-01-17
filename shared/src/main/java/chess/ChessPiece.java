package chess;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
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
        ArrayList<ChessPosition> endPositions = new ArrayList<ChessPosition>();
        if(this.type == PieceType.KING) {

        } else if(this.type == PieceType.QUEEN) {

        } else if(this.type == PieceType.BISHOP) {
            ChessPosition coordinate = myPosition;
            while((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
                if (board.getPiece(coordinate) == null) {
                    endPositions.add(coordinate);
                    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() + 1);
                } else if(board.getPiece(coordinate).pieceColor != this.pieceColor) {
                    endPositions.add(coordinate);
                    break;
                } else {break;}
            }
            while((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
                if (board.getPiece(coordinate) == null) {
                    endPositions.add(coordinate);
                    coordinate = new ChessPosition(coordinate.getRow() + 1, coordinate.getColumn() - 1);
                } else if(board.getPiece(coordinate).pieceColor != this.pieceColor) {
                    endPositions.add(coordinate);
                    break;
                } else {break;}
            }
            while((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
                if (board.getPiece(coordinate) == null) {
                    endPositions.add(coordinate);
                    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() + 1);
                } else if(board.getPiece(coordinate).pieceColor != this.pieceColor) {
                    endPositions.add(coordinate);
                    break;
                } else {break;}
            }
            while((1 <= coordinate.getRow() && coordinate.getRow()<= 8) && (1 <= coordinate.getColumn() && coordinate.getColumn()<= 8)) {
                if (board.getPiece(coordinate) == null) {
                    endPositions.add(coordinate);
                    coordinate = new ChessPosition(coordinate.getRow() - 1, coordinate.getColumn() - 1);
                } else if(board.getPiece(coordinate).pieceColor != this.pieceColor) {
                    endPositions.add(coordinate);
                    break;
                } else {break;}
            }


        } else if(this.type == PieceType.KNIGHT) {

        } else if(this.type == PieceType.ROOK) {

        } else if(this.type == PieceType.PAWN) {

        }
        Collection<ChessMove> validMoves = new Collection<ChessMove>();
        return endPositions;
    }
}
