# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
      A reflex agent chooses an action at each choice point by examining
      its alternatives via a state evaluation function.

      The code below is provided as a guide.  You are welcome to change
      it in any way you see fit, so long as you don't touch our method
      headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {North, South, West, East, Stop}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
        
        newGhostPositions = successorGameState.getGhostPositions()

        "*** YOUR CODE HERE ***"
        # food locations and ghosts location
        newX, newY = newPos
        disFromClosestGhost = 10000
        for ghost in newGhostPositions: #calculate the distance from the closest ghost
          disFromGhost = manhattanDistance(newPos, ghost)
          if disFromGhost < disFromClosestGhost:
            disFromClosestGhost = disFromGhost
        
        currFood = currentGameState.getFood()
        foodList = currFood.asList() #list of (x,y) positions of the food pellets.
        disFromClosestFood = 10000
        for food in foodList:
          disFromFood = manhattanDistance(newPos, food)
          if disFromFood < disFromClosestFood:
            disFromClosestFood = disFromFood

        if disFromClosestGhost<=1: # if the closest ghost from the new pacman position is within 1, 
          return -100              # meaning pacman could be eaten by taking this action, return bad score.
        if currFood[newX][newY]: # if there is a food at the new pacman position, return good score.
          return 100             # otherwise, return good score minus distance to the closest food,
                                 # which would return higher score if the new pacman position is closer to the closest food.
        return 100 - disFromClosestFood

def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

def value(self, state, agentIndex, lastAgent, numAgents, depth):
  if state.isWin() or state.isLose() or depth==0:
    return (self.evaluationFunction(state), "HI")

  if agentIndex==0: #choose whether to use max or min
    return max_value(self, state, agentIndex, lastAgent, numAgents, depth)
  return min_value(self, state, agentIndex, lastAgent, numAgents, depth)

def max_value(self, state, agentIndex, lastAgent, numAgents, depth):
  if agentIndex == lastAgent: #reach one pile
    depth = depth-1
  v = (-10000, "None")
  legalActions = state.getLegalActions(agentIndex) #legal actions of the given state with agentIndex
  for action in legalActions:
    currValue = value(self, state.generateSuccessor(agentIndex, action), ((agentIndex+1)%numAgents), lastAgent, numAgents, depth)
    if currValue[0] > v[0]:
      v = (currValue[0], action) #return a tuple of the highest value and its corresponding action
  return v

def min_value(self, state, agentIndex, lastAgent, numAgents, depth):
  if agentIndex == lastAgent:
    depth = depth-1
  v = (10000, "None")
  legalActions = state.getLegalActions(agentIndex) #legal actions of the given state with agentIndex
  for action in legalActions:
    currValue = value(self, state.generateSuccessor(agentIndex, action), ((agentIndex+1)%numAgents), lastAgent, numAgents, depth)
    if currValue[0] < v[0]:
      v = (currValue[0], action)
  return v

class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """


    def getAction(self, gameState):
        """
          Returns the minimax action from the current gameState using self.depth
          and self.evaluationFunction.

          Here are some method calls that might be useful when implementing minimax.

          gameState.getLegalActions(agentIndex):
            Returns a list of legal actions for an agent
            agentIndex=0 means Pacman, ghosts are >= 1

          gameState.generateSuccessor(agentIndex, action):
            Returns the successor game state after an agent takes an action

          gameState.getNumAgents():
            Returns the total number of agents in the game

          gameState.isWin():
            Returns whether or not the game state is a winning state

          gameState.isLose():
            Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"

        firstAgent = 0
        numAgents = gameState.getNumAgents()
        lastAgent = numAgents-1
        score, action = value(self, gameState, firstAgent, lastAgent, numAgents, self.depth)       
        return action

        util.raiseNotDefined()

def ab_value(self, state, agentIndex, lastAgent, numAgents, depth, alpha, beta):
  # print "(alpha: " + str(alpha) + " , beta: " + str(beta) + ")"
  if state.isWin() or state.isLose() or depth==0:
    return (self.evaluationFunction(state), "HI")

  if agentIndex==0: #choose whether to use max or min
    return ab_max_value(self, state, agentIndex, lastAgent, numAgents, depth, alpha, beta)
  return ab_min_value(self, state, agentIndex, lastAgent, numAgents, depth, alpha, beta)

def ab_max_value(self, state, agentIndex, lastAgent, numAgents, depth, alpha, beta):
  if agentIndex == lastAgent: #reach one pile
    depth = depth-1
  v = (-10000, "None")
  legalActions = state.getLegalActions(agentIndex) #legal actions of the given state with agentIndex
  for action in legalActions:
    currValue = ab_value(self, state.generateSuccessor(agentIndex, action), ((agentIndex+1)%numAgents), lastAgent, numAgents, depth, alpha, beta)
    if currValue[0] > v[0]:
      v = (currValue[0], action) #return a tuple of the highest value and its corresponding action
    if v[0] > beta: # if v > b, return v
      return v
    if v[0] > alpha: # a = max(a, v)
      alpha = v[0]
  return v

def ab_min_value(self, state, agentIndex, lastAgent, numAgents, depth, alpha, beta):
  if agentIndex == lastAgent:
    depth = depth-1
  v = (10000, "None")
  legalActions = state.getLegalActions(agentIndex) #legal actions of the given state with agentIndex
  for action in legalActions:
    currValue = ab_value(self, state.generateSuccessor(agentIndex, action), ((agentIndex+1)%numAgents), lastAgent, numAgents, depth, alpha, beta)
    if currValue[0] < v[0]:
      v = (currValue[0], action)
    if v[0] < alpha: # if v < a, return v
      return v
    if v[0] < beta: # b = min(b, v)
      beta = v[0]
  return v

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
      Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
          Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"

        firstAgent = 0
        numAgents = gameState.getNumAgents()
        lastAgent = numAgents-1
        alpha = -10000
        beta = 10000
        score, action = ab_value(self, gameState, firstAgent, lastAgent, numAgents, self.depth, alpha, beta)       
        return action

        util.raiseNotDefined()

def exp_value(self, state, agentIndex, lastAgent, numAgents, depth):
  if state.isWin() or state.isLose() or depth==0:
    return (self.evaluationFunction(state), "HI")

  if agentIndex==0: #choose whether to use max or min
    return exp_max_value(self, state, agentIndex, lastAgent, numAgents, depth)
  return exp_exp_value(self, state, agentIndex, lastAgent, numAgents, depth)

def exp_max_value(self, state, agentIndex, lastAgent, numAgents, depth):
  if agentIndex == lastAgent: #reach one pile
    depth = depth-1
  v = (-10000, "None")
  legalActions = state.getLegalActions(agentIndex) #legal actions of the given state with agentIndex
  for action in legalActions:
    currValue = exp_value(self, state.generateSuccessor(agentIndex, action), ((agentIndex+1)%numAgents), lastAgent, numAgents, depth)
    if currValue[0] > v[0]:
      v = (currValue[0], action) #return a tuple of the highest value and its corresponding action
  return v

def exp_exp_value(self, state, agentIndex, lastAgent, numAgents, depth):
  if agentIndex == lastAgent:
    depth = depth-1
  v = (0, "None")
  i = 0 #counter
  legalActions = state.getLegalActions(agentIndex) #legal actions of the given state with agentIndex
  for action in legalActions:
    i = i + 1
    currValue = exp_value(self, state.generateSuccessor(agentIndex, action), ((agentIndex+1)%numAgents), lastAgent, numAgents, depth)
    v = (v[0] + currValue[0], "None")
  v = (float(v[0])/i, "None")
  return v

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
          Returns the expectimax action using self.depth and self.evaluationFunction

          All ghosts should be modeled as choosing uniformly at random from their
          legal moves.
        """
        "*** YOUR CODE HERE ***"

        firstAgent = 0
        numAgents = gameState.getNumAgents()
        lastAgent = numAgents-1
        score, action = exp_value(self, gameState, firstAgent, lastAgent, numAgents, self.depth)       
        return action

        util.raiseNotDefined()

def betterEvaluationFunction(currentGameState):
    """
      Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
      evaluation function (question 5).

      DESCRIPTION: <What I did is basically split the evaluation into three conditionals:

                    Factors that I consider:
                    1. distance to the closest food or capsule (prioritize the closest food/capsule for pacman to eat)
                    2. remaining food and capsule (this makes pacman takes the actual action to eat)
                    3. living penalty (this makes pacman finish the game as earlier as possible)

                    * First If: if pacman has eaten a capsule, count the factor that the closer to the ghost the better
                              in addition to the already existing three factors.
                    
                    * Second If: if the closest ghost to pacman is within 2 steps away,
                               return a really bad score, because pacman might get eaten.

                    * Else: use the three factors listed above.>
    """
    "*** YOUR CODE HERE ***"
    currGhostStates = currentGameState.getGhostStates()
    currScaredTimes = [ghostState.scaredTimer for ghostState in currGhostStates]
    currPac = currentGameState.getPacmanPosition()
    currFood = currentGameState.getFood()
    currGhost = currentGameState.getGhostPositions()

    disFromClosestGhost = 1000
    for ghost in currGhost: #compute the distance from the closest ghost (the bigger the better)
      disFromGhost = manhattanDistance(currPac, ghost)
      if disFromGhost < disFromClosestGhost:
        disFromClosestGhost = disFromGhost

    foodList = currFood.asList() #list of (x,y) positions of the food pellets.
    remainingFood = currentGameState.getNumFood() #return the number of food remaining (the smaller the better)

    capsuleList = currentGameState.getCapsules()
    remainingCapsules = len(capsuleList)

    disToClosestFoodOrCapsule = 0
    if remainingFood!=0 or remainingCapsules!=0:
      disToClosestFoodOrCapsule = 1000
      for food in foodList: #compute the distance to the closest food (the smaller the better)
        disToFood = manhattanDistance(currPac, food)
        if disToFood < disToClosestFoodOrCapsule:
          disToClosestFoodOrCapsule = disToFood
      for capsule in capsuleList:
        disToCapsule = manhattanDistance(currPac, capsule)
        if disToCapsule < disToClosestFoodOrCapsule:
          disToClosestFoodOrCapsule = disToCapsule

    if currScaredTimes[0]>=2:
      return 5*(50-disFromClosestGhost) + 3*(50-disToClosestFoodOrCapsule) + 5*(60-(remainingFood+remainingCapsules)) + 3*currentGameState.getScore()
    if disFromClosestGhost<=2: # if the closest ghost from the new pacman position is within 1, 
      return -1000 # meaning pacman could be eaten by taking this action, return bad score.
    return 3*(50-disToClosestFoodOrCapsule) + 5*(60-(remainingFood+remainingCapsules)) + 3*currentGameState.getScore()

    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction

