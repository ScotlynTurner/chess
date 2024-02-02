package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeam;
    private ChessBoard currentBoard;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves;
        ChessPiece startPiece = new ChessPiece(currentTeam, currentBoard.getPiece(startPosition).getPieceType());
        validMoves = startPiece.pieceMoves(currentBoard, startPosition);
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!(validMoves(move.getStartPosition()).contains(move) && !isInCheck(currentTeam))) {
            throw new InvalidMoveException("Invalid Move");
        } else {
            if (currentBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
                currentBoard.addPiece(move.getEndPosition(), new ChessPiece(currentTeam, move.getPromotionPiece()));
            } else {
                currentBoard.addPiece(move.getEndPosition(), currentBoard.getPiece(move.getStartPosition()));
            }
            currentBoard.addPiece(move.getStartPosition(), null);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition position;
        ChessPosition teamKing;
        HashSet<ChessMove> oppositionMoves = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                position = new ChessPosition(i, j);
                if (currentBoard.getPiece(position).pieceColor != teamColor && currentBoard.getPiece(position) != null){
                    for (ChessMove chessMove : currentBoard.getPiece(position).pieceMoves(currentBoard, position)) {
                        oppositionMoves.add(chessMove);
                    }

                } else if (currentBoard.getPiece(position).pieceColor == teamColor && currentBoard.getPiece(position) != null){
                    if (currentBoard.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                        teamKing = position;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }
}
