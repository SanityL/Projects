/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  protected int myColor;
  protected int oppColor;
  protected int searchDepth = 3;
  protected int alpha = -100;
  protected int beta = 100;
  Board gameBoard;


  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    this.myColor = color;
    this.oppColor = 3-color;
    gameBoard = new Board();
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this.myColor = color;
    this.oppColor = 3-color;
    gameBoard = new Board();
    this.searchDepth = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    return gameBoard.chooseMove(myColor, alpha, beta, searchDepth).move;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    if (!(gameBoard.validmoves(oppColor, m))){
      return false;
    }else {
      gameBoard.makeMove(oppColor, m);
      return true;
    }
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if (!(gameBoard.validmoves(myColor, m))){
      return false;
    }else {
      gameBoard.makeMove(myColor, m);
      return true;
    }
  }
}