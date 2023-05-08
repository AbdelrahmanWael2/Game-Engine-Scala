import scala.collection.mutable.ArrayBuffer
import java.awt.{BorderLayout, Color, Dimension, Font, Graphics, GridLayout, Image, Panel}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{BorderFactory, ImageIcon, JButton, JFrame, JLabel, JPanel}

object Sudoko {

  import scala.util.Random


  def SudokoInit(): Array[Int] ={
    val Board = generateSudoku()
    Board
  }


  def SudokoDrawer(state: Array[Int]): Unit = {
    val frame = new JFrame("Sudoku")
    frame.setSize(500, 500)

    val panel = new JPanel(new GridLayout(9, 9))

    for (i <- 0 until 81) {
      val button = new JButton()
      button.setFocusable(false)

      if (state(i) != 0) {
        button.setText(state(i).toString)
      }


      button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK))
      if ((i - 2) % 3 == 0) {
        button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK))
      }
      if(i > 17 && i < 27){
        button.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK))
        if ((i - 2) % 3 == 0) {
          button.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK))
        }
      }
      if (i > 44 && i < 54) {
        button.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK))
        if ((i - 2) % 3 == 0) {
          button.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK))
        }
      }


      button.setBackground(Color.WHITE)
      panel.add(button)
    }

    // create a panel for the row labels
    val rowLabelsPanel = new JPanel(new GridLayout(9, 1))
    for (i <- 1 to 9) {
      val label = new JLabel(i.toString)
      label.setFont(new Font("Arial", Font.PLAIN, 24))
      rowLabelsPanel.add(label)
    }

    // create a panel for the column labels
    val columnLabelsPanel = new JPanel(new GridLayout(1, 9))
    for (c <- 'a' to 'i') {
      val label = new JLabel(c.toString)
      label.setFont(new Font("Arial", Font.PLAIN, 24))
      columnLabelsPanel.add(label)
    }

    // add the panels to the frame
    val contentPane = frame.getContentPane
    contentPane.add(rowLabelsPanel, BorderLayout.EAST)
    contentPane.add(columnLabelsPanel, BorderLayout.SOUTH)
    contentPane.add(panel, BorderLayout.CENTER)

    frame.setVisible(true)
  }


  def SudokoController(state: Array[Int], pair: (String, Int)): (Boolean, Array[Int]) = {
    val (input, turn) = pair
    val parts = input.split("")
    val num = parts(0).toInt
    val alpha = parts(1).charAt(0)
    val toInsert = parts(2).toInt


    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')
    if (!(columns.contains(alpha)) || num < 1 || num > 9 || !(input.length == 3) ||  toInsert < 1 || toInsert > 9) {
      println("Invalid Input")
      return (false, state)
    }


    val row = num - 1
    val col = alpha - 'a'
    val boxRow = (row / 3) * 3
    val boxCol = (col / 3) * 3
    val boxStartIndex = boxRow * 9 + boxCol

    // check if the number is already present in the same row or column
    for (i <- 0 until 9) {
      if (state(row * 9 + i) == toInsert || state(i * 9 + col) == toInsert) {
        println("Invalid Move: Number already present in the same row or column")
        return (false, state)
      }
    }

    // check if the number is already present in the same 3x3 box
    for (i <- 0 until 3; j <- 0 until 3) {
      val index = boxStartIndex + i * 9 + j
      if (state(index) == toInsert) {
        println("Invalid Move: Number already present in the same 3x3 box")
        return (false, state)
      }
    }
    val index = row * 9 + col
    state(index) = toInsert


    (true, state)
  }



  import scala.util.Random

  def generateSudoku(): Array[Int] = {
    val board = Array.fill(81)(0)
    generateSudokuHelper(board, 0)
    removeNumbers(board, 0.1) // remove 50% of the numbers
    board
  }

  def generateSudokuHelper(board: Array[Int], index: Int): Boolean = {
    if (index == board.length) return true // base case: board is complete
    val rand = new Random()
    var nums = (1 to 9).toList
    rand.shuffle(nums).foreach { num =>
      if (isValid(board, index, num)) {
        board(index) = num
        if (generateSudokuHelper(board, index + 1)) {
          return true // recursive case: board is complete
        }
        board(index) = 0
      }
    }
    false
  }

  def isValid(board: Array[Int], index: Int, value: Int): Boolean = {
    val row = index / 9
    val col = index % 9
    val boxRow = row / 3 * 3
    val boxCol = col / 3 * 3
    for (i <- 0 until 9) {
      if (board(row * 9 + i) == value) return false
      if (board(i * 9 + col) == value) return false
      val boxIndex = (boxRow + i / 3) * 9 + boxCol + i % 3
      if (board(boxIndex) == value) return false
    }
    true
  }

  def removeNumbers(board: Array[Int], percentage: Double): Unit = {
    val rand = new Random()
    var count = (81 * percentage).toInt
    while (count > 0) {
      val index = rand.nextInt(81)
      if (board(index) != 0) {
        board(index) = 0
        count -= 1
      }
    }
  }

}
