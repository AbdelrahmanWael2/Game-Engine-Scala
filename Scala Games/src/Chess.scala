import java.awt.{BorderLayout, Color, Dimension, Font, Graphics, GridLayout, Image, Panel}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{BorderFactory, ImageIcon, JButton, JFrame, JLabel, JPanel}

import scala.collection.mutable. ArrayStack

import scala.util.control.Breaks.break


object Chess {

  def ChessInit(): Array[Int] = {
    val Board = Array.fill(64)(0)
    for (i <- 0 until 8; j <- 0 until 8) {
      val linearIndex = i * 8 + j
      Board(linearIndex) = linearIndex match {
        case 0 | 7 => -3 // black rook
        case 1 => -5 // black knight
        case 6 => -5
        case 2 | 5 => -4 // black bishop
        case 3 => -2 // black queen
        case 4 => -1 // black king
        case n if n >= 8 && n <= 15 => -6 // black pawn
        case n if n >= 48 && n <= 55 => 6 // white pawn
        case 56 | 63 => 3 // white rook
        case 57 | 62 => 5 // white knight
        case 58 | 61 => 4 // white bishop
        case 59 => 2 // white queen
        case 60 => 1 // white king
        case _ => 0 // empty
      }
    }
    Board
  }
  
  def ChessDrawer(state: Array[Int]): Unit = {

    val blackpawn: Image =
      ImageIO.read(new File("src/Assets/Chess/black/pawn.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whitepawn: Image =
      ImageIO.read(new File("src/Assets/Chess/white/pawn.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackking: Image =
      ImageIO.read(new File("src/Assets/Chess/black/king.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whiteking: Image =
      ImageIO.read(new File("src/Assets/Chess/white/king.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)


    val blackqueen: Image =
      ImageIO.read(new File("src/Assets/Chess/black/queen.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whitequeen: Image =
      ImageIO.read(new File("src/Assets/Chess/white/queen.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackrook: Image =
      ImageIO.read(new File("src/Assets/Chess/black/rook.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whiterook: Image =
      ImageIO.read(new File("src/Assets/Chess/white/rook.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackbishop: Image =
      ImageIO.read(new File("src/Assets/Chess/black/bishop.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whitebishop: Image =
      ImageIO.read(new File("src/Assets/Chess/white/bishop.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackknight: Image =
      ImageIO.read(new File("src/Assets/Chess/black/knight.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whiteknight: Image =
      ImageIO.read(new File("src/Assets/Chess/white/knight.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
    

    val frame = new JFrame("Chess Board")
    frame.setSize(800, 800)

    val panel = new JPanel(new GridLayout(8, 8))

    for (i <- 0 until 8; j <- 0 until 8) {
      val button = new JButton()
      button.setFocusable(false)

      state(i * 8 + j) match {

        case 1 => button.setIcon(new ImageIcon(whiteking))
        case 2 => button.setIcon(new ImageIcon(whitequeen))
        case 3 => button.setIcon(new ImageIcon(whiterook))
        case 4 => button.setIcon(new ImageIcon(whitebishop))
        case 5 => button.setIcon(new ImageIcon(whiteknight))
        case 6 => button.setIcon(new ImageIcon(whitepawn))
        case -1 => button.setIcon(new ImageIcon(blackking))
        case -2 => button.setIcon(new ImageIcon(blackqueen))
        case -3 => button.setIcon(new ImageIcon(blackrook))
        case -4 => button.setIcon(new ImageIcon(blackbishop))
        case -5 => button.setIcon(new ImageIcon(blackknight))
        case -6 => button.setIcon(new ImageIcon(blackpawn))
        case _ => button.setText("")
      }

      button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2))

      if ((i + j) % 2 == 0) {
        button.setBackground(Color.WHITE)
      } else {
        button.setBackground(Color.GRAY)
      }

      panel.add(button)
    }

    // create a panel for the row labels
    val rowLabelsPanel = new JPanel(new GridLayout(8, 1))
    for (i <- 1 to 8) {
      val label = new JLabel((9 - i).toString)
      label.setFont(new Font("Arial", Font.PLAIN, 24))
      rowLabelsPanel.add(label)
    }

    // create a panel for the column labels
    val columnLabelsPanel = new JPanel(new GridLayout(1, 8))
    for (c <- 'a' to 'h') {
      val label = new JLabel(c.toString)
      label.setFont(new Font("Arial", Font.PLAIN, 24))

      columnLabelsPanel.add(label)
    }

    // add the panels to the frame
    val contentPane = frame.getContentPane
    contentPane.add(rowLabelsPanel, BorderLayout.EAST)
    contentPane.add(columnLabelsPanel, BorderLayout.SOUTH)
    contentPane.add(panel, BorderLayout.CENTER)

    frame.add(panel)
    frame.setVisible(true)
  }


  def ChessController(state: Array[Int], pair: (String, Int)): (Boolean, Array[Int]) = {

    val (input, turn) = pair
    val parts = input.split("")
    val num = parts(0).toInt
    val alpha = parts(1).charAt(0)
    val num2 = parts(2).toInt
    val alpha2 = parts(3).charAt(0)

    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    if (!(columns.contains(alpha)) || num < 1 || num > 8 || !(columns.contains(alpha2)) || num2 < 1 || num2 > 8 || !(input.length == 4)) {
      return (false, state)
    }

    val startRow = 8 - num
    val startCol = alpha - 'a'
    val endRow = 8 - num2
    val endCol = alpha2 - 'a'

    if (startRow < 0 || startRow > 7 || startCol < 0 || startCol > 7) {
      return (false, state)
    }

    val piece = state(startRow * 8 + startCol)


    if ((turn % 2 == 0 && piece < 0) || (turn % 2 == 1 && piece > 0)) { // Check if the starting position contains a piece of the correct color
      println("Not ur turn")
      return (false, state)
    }

    val validMoves = getValidMoves(state, startRow, startCol)

    if (!validMoves.contains((endRow, endCol)))
      return (false, state)
    state(endRow * 8 + endCol) = state(startRow * 8 + startCol)
    state(startRow * 8 + startCol) = 0

    (true, state)
  }

  def getValidMoves(state: Array[Int], row: Int, col: Int): Set[(Int, Int)] = {
    val piece = state(row * 8 + col)
    val player = if (piece > 0) 1 else -1

    piece.abs match {
      case 1 => getValidMovesForKing(state, row, col, player)
      case 2 => getValidMovesForQueen(state, row, col, player)
      case 3 => getValidMovesForRook(state, row, col, player)
      case 4 => getValidMovesForBishop(state, row, col, player)
      case 5 => getValidMovesForKnight(state, row, col, player)
      case 6 => getValidMovesForPawn(state, row, col, player)
      case _ => Set.empty[(Int, Int)]
    }
  }

  def getValidMovesForKing(state: Array[Int], row: Int, col: Int, player: Int): Set[(Int, Int)] = {
    val validMoves = scala.collection.mutable.Set.empty[(Int, Int)]
    for (i <- -1 to 1; j <- -1 to 1 if i != 0 || j != 0) { // Loop through all adjacent squares
      val newRow = row + i
      val newCol = col + j
      if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) { // Make sure the square is on the board
        val piece = state(newRow * 8 + newCol)
        if (piece * player <= 0) { // Check if the square is empty or occupied by an opponent's piece
          validMoves += ((newRow, newCol))
        }
      }
    }
    validMoves.toSet
  }

  def getValidMovesForPawn(state: Array[Int], row: Int, col: Int, player: Int): Set[(Int, Int)] = {
    val forwardStep = if (player == 1) -1 else 1 // Pawns move in opposite directions depending on the player
    val startingRow = if (player == 1) 6 else 1 // Pawns start in different rows depending on the player

    val validMoves = scala.collection.mutable.Set.empty[(Int, Int)]

    // Check if the pawn can move one or two spaces forward
    if (state((row + forwardStep) * 8 + col) == 0) { // The space in front of the pawn is empty
      validMoves += ((row + forwardStep, col))
      if (row == startingRow && state((row + forwardStep * 2) * 8 + col) == 0) { // The pawn is in its starting position and the space two spaces in front of it is empty
        validMoves += ((row + forwardStep * 2, col))
      }
    }

    // Check if the pawn can capture diagonally
    if (col > 0 && state((row + forwardStep) * 8 + col - 1) * player < 0) { // The diagonal to the left is occupied by an opponent's piece
      validMoves += ((row + forwardStep, col - 1))
    }
    if (col < 7 && state((row + forwardStep) * 8 + col + 1) * player < 0) { // The diagonal to the right is occupied by an opponent's piece
      validMoves += ((row + forwardStep, col + 1))
    }

    validMoves.toSet
  }

  def getValidMovesForQueen(state: Array[Int], row: Int, col: Int, player: Int): Set[(Int, Int)] = {
    val rookMoves = getValidMovesForRook(state, row, col, player)
    val bishopMoves = getValidMovesForBishop(state, row, col, player)
    rookMoves.union(bishopMoves)
  }

  def getValidMovesForRook(state: Array[Int], row: Int, col: Int, player: Int): Set[(Int, Int)] = {
    val validMoves = scala.collection.mutable.Set.empty[(Int, Int)]

    // Check for valid moves in the horizontal direction
    for (c <- 0 to 7 if c != col) {
      val dest = row * 8 + c
      if (state(dest) == 0 || state(dest) * player < 0) { // The destination is empty or occupied by an opponent's piece
        if (!hasTeamPieceInBetween(state, row, col, row, c)) { // Check if there is a piece of the same team in between the rook and the destination
          validMoves += ((row, c))
        }
      }
    }

    // Check for valid moves in the vertical direction
    for (r <- 0 to 7 if r != row) {
      val dest = r * 8 + col
      if (state(dest) == 0 || state(dest) * player < 0) { // The destination is empty or occupied by an opponent's piece
        if (!hasTeamPieceInBetween(state, row, col, r, col)) { // Check if there is a piece of the same team in between the rook and the destination
          validMoves += ((r, col))
        }
      }
    }

    validMoves.toSet
  }

  def hasTeamPieceInBetween(state: Array[Int], startRow: Int, startCol: Int, endRow: Int, endCol: Int): Boolean = {
    // Check if there is a piece of the same team in between (startRow, startCol) and (endRow, endCol) horizontally
    if (startRow == endRow) {
      val (col1, col2) = if (startCol < endCol) (startCol, endCol) else (endCol, startCol)
      for (c <- col1 + 1 until col2) {
        if (state(startRow * 8 + c) != 0) {
          return true
        }
      }
    }
    // Check if there is a piece of the same team in between (startRow, startCol) and (endRow, endCol) vertically
    else if (startCol == endCol) {
      val (row1, row2) = if (startRow < endRow) (startRow, endRow) else (endRow, startRow)
      for (r <- row1 + 1 until row2) {
        if (state(r * 8 + startCol) != 0) {
          return true
        }
      }
    }

    false
  }

  def getValidMovesForBishop(state: Array[Int], row: Int, col: Int, player: Int): Set[(Int, Int)] = {
    val validMoves = scala.collection.mutable.Set.empty[(Int, Int)]

    // check diagonal moves in all four directions
    var r = row - 1
    var c = col - 1
    while (r >= 0 && c >= 0 && state(r * 8 + c) * player <= 0) {
      if (state(r * 8 + c) == 0 || state(r * 8 + c) * player < 0) validMoves += ((r, c))
      if (state(r * 8 + c) * player < 0) {
        c = -1
        r = -1
      }
      r -= 1
      c -= 1
    }

    r = row - 1
    c = col + 1
    while (r >= 0 && c <= 7 && state(r * 8 + c) * player <= 0) {
      if (state(r * 8 + c) == 0 || state(r * 8 + c) * player < 0) validMoves += ((r, c))
      if (state(r * 8 + c) * player < 0) {
        c = 8
        r = -1
      }
      r -= 1
      c += 1
    }

    r = row + 1
    c = col - 1
    while (r <= 7 && c >= 0 && state(r * 8 + c) * player <= 0) {
      if (state(r * 8 + c) == 0 || state(r * 8 + c) * player < 0) validMoves += ((r, c))
      if (state(r * 8 + c) * player < 0) {
        c = -1
        r = 8
      }
      r += 1
      c -= 1
    }

    r = row + 1
    c = col + 1
    while (r <= 7 && c <= 7 && state(r * 8 + c) * player <= 0) {
      if (state(r * 8 + c) == 0 || state(r * 8 + c) * player < 0) validMoves += ((r, c))
      if (state(r * 8 + c) * player < 0) {
        c = 8
        r = 8
      }
      r += 1
      c += 1
    }

    validMoves.toSet
  }

  def getValidMovesForKnight(state: Array[Int], row: Int, col: Int, player: Int): Set[(Int, Int)] = {
    val potentialMoves = Set(
      (row - 2, col + 1),
      (row - 1, col + 2),
      (row + 1, col + 2),
      (row + 2, col + 1),
      (row + 2, col - 1),
      (row + 1, col - 2),
      (row - 1, col - 2),
      (row - 2, col - 1)
    )

    potentialMoves.filter { case (r, c) => r >= 0 && r <= 7 && c >= 0 && c <= 7 && (state(r * 8 + c) * player) <= 0 }
  }


}

