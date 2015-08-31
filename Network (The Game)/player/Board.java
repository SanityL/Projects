package player;

public class Board {
	protected int[][] newboard;
  protected int whiteChips = 0;
  protected int blackChips = 0;
  protected final static int DIMENSION = 8;
  protected final static int empty = 0;
  protected final static int white = 1;
  protected final static int black = 2;

// Board() constructs an empty game board with DIMENSION = 8 as specified in the data fields
	public Board() {
		newboard = new int[DIMENSION][DIMENSION];
	} 

 /* myNeighbor takes in a "color," representing the player, and a move. This is a helper function to validmoves and returns 
  the total number of adjacent chips to the current move passed in. */
  public int myNeighbor (int color, Move m) {
    int totalneighbors = 0;
    for (int i=-1; i<=1; i++) {
      for (int j=-1; j<=1; j++) {
        if (color == newboard[m.x1+i][m.y1+j]) {
          totalneighbors += 1;
        }
      }
    }
    return totalneighbors;
  }

/*This method will return true or false depending on if the move is valid or not based on the rules of legal play. 
  It takes in a "color" (player) and a move. */
	public boolean validmoves(int color, Move m) {
    if (m.moveKind == 2) {
      removeChip(m, color);
    }
		if (m.x1 == 0 && m.y1 == 0 || m.x1 == 7 && m.y1 == 0 || m.x1 == 0 && m.y1 == 7 || m.x1 == 7 && m.y1 == 7) {
      return false; //corner
    } else if (color == white && m.y1 == 0 || m.y1 == 7) {
      return false; // white opp goal
    } else if (color == black && m.x1 == 0 || m.x1 == 7) {
      return false; // black opp goal
    } else if (color != empty) {
      return false; //has chip
    } else if (myNeighbor(color, m) > 1) {
      return false; //cluster (2+ chips all adjacent)
    } else {

    if (m.moveKind == 2) {
      resumeChip(m, color);
    }

    return true;
    }
  } 

 //This method takes in a "color" (player) and will return a ChipList of all valid moves that can made by the current player.
  public ChipList possiblemoves(int color) {
    ChipList possmoves = new ChipList();
    for (int i=0; i<DIMENSION; i++) {
      for (int j=0; j<DIMENSION; j++) {
        if (whiteChips < 10 && blackChips < 10){
          Move addmove = new Move(i, j);
          if (validmoves (color, addmove)) {
            possmoves.insertFront(color, addmove);              
          }
        } else {
            ChipList l = position(color);
            ChipListNode r = l.front();
            while (r.isValidNode()) {
              Move stepmove = new Move(r.x,r.y,i,j);
              if (validmoves (color, stepmove)) {
                if (r.x != i || r.y != j) {
                  possmoves.insertFront(color, stepmove);
                }
              }
              r = r.next;
            }  
        }
      }
    }
    return possmoves;
  }

// This method will return a ChipList with all positions of where the current player's chips are.
  public ChipList position(int color) {
    ChipList positions = new ChipList();
    if (whiteChips == 10 || blackChips == 10) {
      for (int i=0; i<DIMENSION; i++) {
        for (int j=0; j<DIMENSION; j++) {
          Move stepmove = new Move();
          if (newboard[i][j] == color) {
            positions.insertFront(color,i,j);
          }
        }
      }
    }
    return positions;
  } 

 //"findconnection" will take in a position x, y and a color and return a list of all connections to the specified location.
  public ChipList findconnection(int color, int x, int y) {
    int radius;
    boolean notfound;
    ChipList connect = new ChipList();
    int[] direction = new int[] {-1, 0, 1};
    for (int hor : direction){
      for (int ver : direction){
        radius = 1;
        notfound = true;
        while (notfound){ //searching in 8 directions (each position surrounding the current location)
          int inx = x + hor*radius;
          int iny = y + ver*radius;
          if (isOutOfnewboard(inx, iny) || (inx == x && iny == y)){ //takes care of weird cases
            notfound = false;
          }else if(newboard[inx][iny] > 0){ //found a non empty chip
            if (newboard[inx][iny] == color){
              connect.insertFront(color, inx, iny);
            }
            notfound = false;  
          }else{ //hasn't found a non empty chip, increments search radius by 1
            radius++;
          }
        }
      }
    }
    if(connect.length() == 0){
      return null;
    }
    return connect;
  }

//"isOutOfnewboard" takes in a position x, y and returns a boolean depending on if the chip is placed within the dimensions of the game board.
  public boolean isOutOfnewboard(int x, int y){
    return (x<0 || y<0 || x>=DIMENSION || y>=DIMENSION) || (x==0 && y==0) || (x==0 && y==7) || (x==7 && y==0) || (x==7 && y==7);
  }

  public boolean inGoal(ChipListNode chip){
    if (chip.color == white){
      return chip.x == 0;
    }else{
      return chip.y == 7;
    }
  }

// "hasNoChipsInGoal" is a boolean that checks to see whether or not chips are in the current player's goal, returns a boolean.
  public boolean hasNoChipsInGoal(int color){
    boolean noChips1 = true;
    boolean noChips2 = true;
    if (color == white){
      for(int y=1; y<(DIMENSION-1);y++){
        if (newboard[7][y] != empty){
          noChips1 = false;
        }
        if (newboard[0][y] != empty){
          noChips2 = false;
        }
      }
    }else{
      for(int x=1; x<(DIMENSION-1);x++){
        if (newboard[x][0] != empty){
          noChips1 = false;
        }
        if (newboard[x][7] != empty){
          noChips2 = false;
        }
      }      
    }
    return noChips1 || noChips2;
  } 

// "ChipsInOneGoal" takes in a color and returns a ChipList of all chips in the player's goal. 
  public ChipList ChipsInOneGoal(int color){
    ChipList starters = new ChipList();
      if (color == white){
        for(int y=1;y<(DIMENSION-1);y++){
          if (newboard[7][y] != empty){
            starters.insertFront(color, 7, y);
          }
        }
      } else{
          for(int x=1;x<(DIMENSION-1);x++){
            if (newboard[x][0] != empty){
              starters.insertFront(color, x, 0);
            }
          }
      }
    return starters;
  } 

 //"network" takes in a player and returns true or false, depending on whether or not the player has a network.
  public boolean network(int color) {
    if (hasNoChipsInGoal(color)){ //if there s no chips in either one of the goal field, return false
      return false;
    }else{
      try {
        ChipList goalChips = ChipsInOneGoal(color); //return all the chips in either top goal field(for black) or right goal field(for white)
        ChipListNode iter = goalChips.front();
        while (iter.isValidNode()){ 
          ChipList line = new ChipList();
          line.insertFront(color, iter.x, iter.y);  //create a new chiplist, and insert the goalchip as the first chip for connection
          if (hasNetWork(line, 1)){ //check if this chip will leads to network
            return true;
          }
          iter = iter.next();
        }
      } catch (InvalidNodeException e) {

      }
    }
    return false;
  }


  //"hasNetWork" is the helper for network
  //Used is a ChipList that stores a potential connection, starting with a chip in goal.
  //Each new chip is added to the front of (ChipList used), so the front of the list is always the newest chip(the head of the connection so far)
  public boolean hasNetWork(ChipList used, int level){
    try {   
      ChipListNode curr = used.front(); //curr is the newest chip in the connection
      if (level >= 6 && inGoal(curr)){  //if level(number of chips in this network) >= 6 and my last chips is in goal, return true
        return true;
      }
      ChipList next = findconnection(curr.color, curr.x, curr.y); //find all the possible connection from curr
      ChipListNode candidate = next.front();  //candidates are all the possible chips to connect to
      while (candidate.isValidNode()){
        if (used.hasNode(candidate.color, candidate.x, candidate.y)){ //if candidate is already used by the connection, ignores it
          continue;
        }
        used.insertFront(candidate.color, candidate.x, candidate.y);  //add candidate to used
        if (hasNetWork(used, level+1)){ //if there is a connection now, return true
          return true;
        }
        used.front().remove();  //if not, remove the candidate, restore used for future function call
        candidate = candidate.next();
      }  
    } catch (InvalidNodeException e) {

    }
    return false;
  }

//This method is our minimax search tree, in which it takes in a player and returns the best move for that player.
  public ChipListNode chooseMove(int color, int alpha, int beta, int depth) {
    ChipList allMoves = possiblemoves(color);
    ChipListNode curr;
    ChipListNode myBest = new ChipListNode(); // myBest has score and move
    ChipListNode reply; // Opponent’s best reply
    if (network(color) || depth==0) { //the current Grid is full or has a win, evaluate 1 is a win.
      System.out.println("1");
      myBest.score = evaluate(this); //Grid’s score
      myBest.move = allMoves.front().move;
      if (myBest.move==null) {
        System.out.println("2");
        myBest.move = allMoves.front().move;
        System.out.println(myBest.move);
        makeMove(color, myBest.move);
        myBest.score = evaluate(this);
        undoMove(color, myBest.move);
      }
      System.out.println("3");
      return myBest; //a Best with the Grid’s score, no move;
      // return null;
    } else {
      System.out.println("4");
        curr = allMoves.front();
    }
    if (color == white) {
      System.out.println("5");
      myBest.score = alpha;
    } else {
      System.out.println("6");
      myBest.score = beta;
    }
    myBest.move = curr.move;
    while (curr.isValidNode()) {
      System.out.println("7");
      try {
        makeMove(color, curr.move); // Modifies "this" Grid
        reply = chooseMove(3-color, alpha, beta, depth--);
        undoMove(color, curr.move); // Restores "this" Grid
        if ((color == white) && (reply.score > myBest.score)) {
          myBest.move = curr.move;
          myBest.score = reply.score;
          alpha = reply.score;
        } else if ((color == black) && (reply.score < myBest.score)) {
            myBest.move = curr.move;
            myBest.score = reply.score;
            beta = reply.score;
        }
        if (alpha >= beta) {
          System.out.println("8");
          return myBest;
        }
        curr = curr.next(); // Iteration
      } catch (InvalidNodeException e) {
          // e.printStackTrace();
        }
    }
    System.out.println("9");
    return myBest;
  }

//This method will be our AI evaluation function that takes in the board and returns the score of that board.
  public int evaluate(Board newboard) {
    return 0;//int score;
  } 

// This method is our makeMove function that takes in a player and a move, and it will update the newboard, adding the specified move.
  public void makeMove(int color, Move m) {
  	if (m.moveKind == 1) {
  		newboard[m.x1][m.y1] = color;
      if (color == white) {
        whiteChips++;
      } else {
        blackChips++;
      }
  	} else if (m.moveKind == 2) {
  		newboard[m.x1][m.y1] = color;
  		newboard[m.x2][m.y2] = empty;
  	}
  }

// This method is our undoMove function that takes in a player and a move, and it will update the newboard, undoing the specified move.
  public void undoMove(int color, Move m) {
    if (m.moveKind == 1) {
      newboard[m.x1][m.y1] = empty;
      if (color == white){
        whiteChips--;
      }else{
        blackChips--;
      }
    } else if (m.moveKind == 2) {
      newboard[m.x1][m.y1] = empty;
      newboard[m.x2][m.y2] = color;
    }
  }
  
  //removes a chip from the board
  public void removeChip(Move m, int color) {
    newboard[m.x2][m.y2] = empty;
  } 

//reinserts a chip onto the board
  public void resumeChip(Move m, int color) {
    newboard[m.x2][m.y2] = color;
  }


  //assume using black node, helper function for allNetWork
  public void getNetWork(ChipList[] all, ChipList net){
    ChipListNode curr = net.front();  //keep track of front of net
    if (net.length() == 6){ //if net is already length 6
      if (curr.y == 0){ // check if the last chip is inside top goal => this is a network(assume starting from the bottom goal)
        int index = 0;
        while (curr.isValidNode()){  //insertion
          all[index].insertFront(curr.color, curr.x, curr.y);
          try {
            curr = curr.next();
          } catch (InvalidNodeException e) {

          }
          index++;
        }
      }
    } else if (curr.y != 0) {
      ChipList next = getAllPossConnection(net);
      ChipListNode poss = next.front();
      while (poss.isValidNode()){
        net.insertFront(poss.color, poss.x, poss.y);
        getNetWork(all, net);
        try {
          net.front().remove();
          poss = poss.next();
        } catch (InvalidNodeException e) {

        }
      }
    }
  }


  public ChipList[] allNetWork(){
    ChipList[] all = new ChipList[6];
    ChipList net = new ChipList();
    for (int goal=1; goal<7; goal++){
      net.insertFront(black, goal, 7);
      getNetWork(all, net);
      try {
        net.front().remove();
      } catch (InvalidNodeException e) {

      }
    }
    return all;
  }



  public ChipList getAllPossConnection(ChipList board){
    ChipListNode curr = board.front();
    boolean notEnd;
    int radius;
    int inx, iny;
    int x=curr.x;
    int y=curr.y;
    ChipList possConnection = new ChipList();
    int[] direction = new int[] {-1, 0, 1};
    for (int hor : direction){
      for (int ver : direction){
        radius = 1;
        notEnd = true;
        while(notEnd){
          try {
            inx = x + hor*radius;
            iny = y + ver*radius;
            if (isOutOfnewboard(inx, iny) || (inx==x && iny==y) || inx==0 || inx==7){
              notEnd = true;
            }else if (board.hasNode(curr.color, inx, iny)){
              notEnd = true;
            }else {
              possConnection.insertFront(curr.color, inx, iny);
              radius++;
            }
          } catch (InvalidNodeException e) {
            
          }
        }
      }
    }
    return possConnection;
  }   
}