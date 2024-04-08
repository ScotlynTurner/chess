package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeam;
    private ChessBoard currentBoard = new ChessBoard();

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
        HashSet<ChessMove> validMoves = new HashSet<>();
        HashSet<ChessMove> potentialMoves = validMovesHelper(startPosition);
        for (ChessMove move : potentialMoves) {
            if (checkMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private HashSet<ChessMove> validMovesHelper(ChessPosition startPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        if (currentBoard.getPiece(startPosition) != null) {
            ChessPiece startPiece = new ChessPiece(currentBoard.getPiece(startPosition).pieceColor,
                    currentBoard.getPiece(startPosition).getPieceType());
            validMoves=(HashSet<ChessMove>) startPiece.pieceMoves(currentBoard, startPosition);
        }
        return validMoves;
    }

    private boolean checkMove(ChessMove move) {
        HashSet<ChessMove> possibleMoves = validMovesHelper(move.getStartPosition());
        Boolean isMoveValid = false;
        for (ChessMove validMove : possibleMoves) {
            if (areSameMoves(validMove, move)) {
                isMoveValid = true;
                break;
            }
        }

        ChessPiece tempPiece = currentBoard.getPiece(move.getStartPosition());
        ChessPiece stolenPiece;
        if (!isMoveValid) {
            return false;
        } else {
            stolenPiece = currentBoard.getPiece(move.getEndPosition());
            currentBoard.addPiece(move.getEndPosition(), tempPiece);
            currentBoard.addPiece(move.getStartPosition(), null);
          if (isInCheck(currentBoard.getPiece(move.getEndPosition()).pieceColor)) {
                currentBoard.addPiece(move.getStartPosition(), tempPiece);
                currentBoard.addPiece(move.getEndPosition(), stolenPiece);
                return false;
            }
            currentBoard.addPiece(move.getStartPosition(), tempPiece);
            currentBoard.addPiece(move.getEndPosition(), stolenPiece);
        }
        return true;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        HashSet<ChessMove> possibleMoves = validMovesHelper(move.getStartPosition());
        ChessPiece tempPiece = currentBoard.getPiece(move.getStartPosition());
        if (tempPiece.pieceColor != currentTeam) {
            throw new InvalidMoveException("Not your turn");
        }

        Boolean isMoveValid = false;
        for (ChessMove validMove : possibleMoves) {
            if (areSameMoves(validMove, move)) {
                isMoveValid = true;
                break;
            }
        }

        if (!isMoveValid) {
            throw new InvalidMoveException("Invalid Move");
        } else {
            if (move.getPromotionPiece() != null) {
                currentBoard.addPiece(move.getEndPosition(), new ChessPiece(currentTeam, move.getPromotionPiece()));
            } else {
                currentBoard.addPiece(move.getEndPosition(), tempPiece);
            }
            currentBoard.addPiece(move.getStartPosition(), null);
            if (isInCheck(currentTeam)) {
                currentBoard.addPiece(move.getStartPosition(), tempPiece);
                currentBoard.addPiece(move.getEndPosition(), null);
                throw new InvalidMoveException("Invalid Move, would be put in check");
            }
        }

        if (currentTeam == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    private boolean areSameMoves(ChessMove move1, ChessMove move2) {
        if (move1.getStartPosition().getRow() == move2.getStartPosition().getRow() &&
                move1.getStartPosition().getColumn() == move2.getStartPosition().getColumn() &&
                move1.getEndPosition().getRow() == move2.getEndPosition().getRow() &&
                move1.getEndPosition().getColumn() == move2.getEndPosition().getColumn()) {

            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition position;
        ChessPosition teamKing = new ChessPosition(0,0);
        HashSet<ChessMove> oppositionMoves = new HashSet<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                position = new ChessPosition(i, j);
                if (currentBoard.getPiece(position) != null && currentBoard.getPiece(position).pieceColor != teamColor){
                    for (ChessMove chessMove : currentBoard.getPiece(position).pieceMoves(currentBoard, position)) {
                        oppositionMoves.add(chessMove);
                    }

                } else if (currentBoard.getPiece(position) != null && currentBoard.getPiece(position).pieceColor == teamColor){
                    if (currentBoard.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                        teamKing = position;
                    }
                }
            }
        }
        for (ChessMove moves : oppositionMoves) {
            if (moves.getEndPosition().getRow() == teamKing.getRow() && moves.getEndPosition().getColumn() == teamKing.getColumn()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        HashSet<ChessMove> kingMoves = new HashSet<>();
        HashSet<ChessPosition> oppositionEndPositions = new HashSet<>();
        if (!isInCheck(teamColor)) {
            return false;
        }
        return checkmateHelper(teamColor, kingMoves, oppositionEndPositions);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition position;
        HashSet<ChessMove> kingMoves = new HashSet<>();
        HashSet<ChessPosition> oppositionEndPositions = new HashSet<>();
        if (isInCheck(teamColor)) {
            return false;
        }
        return checkmateHelper(teamColor, kingMoves, oppositionEndPositions);
    }

    private boolean checkmateHelper(TeamColor teamColor, HashSet<ChessMove> kingMoves, HashSet<ChessPosition> oppositionEndPositions) {
        ChessPosition position;
        for (int i=1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                position = new ChessPosition(i, j);
                ChessPiece currentPiece = currentBoard.getPiece(position);
                if (currentPiece != null && currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.pieceColor == teamColor){
                    for (ChessMove chessMove : currentPiece.pieceMoves(currentBoard, position)) {
                        kingMoves.add(chessMove);
                    }

                } else if (currentPiece != null && currentPiece.pieceColor != teamColor){
                    for (ChessMove chessMove : currentBoard.getPiece(position).pieceMoves(currentBoard, position)) {
                        oppositionEndPositions.add(chessMove.getEndPosition());
                    }
                }
            }
        }

        for (ChessMove kingMove : kingMoves) {
            if (checkMove(kingMove)) {
                return false;
            }
        }

        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return currentTeam == chessGame.currentTeam && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeam, currentBoard);
    }
}
