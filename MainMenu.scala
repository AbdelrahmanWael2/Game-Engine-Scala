import java.awt.{Dimension, GridLayout}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import TicTacToe.TicTacToeDrawer
import TicTacToe.TicTacToeController
import TicTacToe.ConnectControler
import TicTacToe.ConnectDrawer

object MainMenu {

  def main(args: Array[String]): Unit = {
    MainMenu()
  }

  def MainMenu(): Unit = {
    val frame = new JFrame()
    frame.setVisible(true)

    // rows, columns, h gap, v gap
    var gridLayout = new GridLayout(10, 10, 10, 10)
    val mainPanel = new JPanel(gridLayout)
    val TicButton = new JButton("Tic-Tac-Toe")
    val ConnectButton = new JButton("Connect 4")
    val CheckersButton = new JButton("Checkers")
    val ChessButton = new JButton("Chess")
    val SudokoButton = new JButton("Sudoku")
    val QueensButton = new JButton("8-Queens")

    mainPanel setBorder BorderFactory.createEmptyBorder(10, 10, 10, 10)
    mainPanel add TicButton;
    mainPanel add ConnectButton
    mainPanel add CheckersButton;
    mainPanel add ChessButton
    mainPanel add SudokoButton
    mainPanel add QueensButton

    frame.setResizable(false)
    frame.setMinimumSize(new Dimension(800, 800))

    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.setContentPane(mainPanel)
    frame.pack()

    val ticListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {

        AbstractGameEngine(TicTacToeController, TicTacToeDrawer)

      }
    }

    val connectListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(ConnectControler, ConnectDrawer)
      }
    }
    //
    //    val queensListener: ActionListener = new ActionListener {
    //      def actionPerformed(e: ActionEvent): Unit = {
    //        new Game(
    //          "queens",
    //          queens.controller,
    //          queens.draw
    //        )
    //        setVisible(false)
    //      }
    //    }
    //    val chessListener: ActionListener = new ActionListener {
    //      def actionPerformed(e: ActionEvent): Unit = {
    //        new Game(
    //          "chess",
    //          Chess.controller,
    //          Chess.draw
    //        )
    //        setVisible(false)
    //      }
    //    }
    //    val sudokoListener: ActionListener = new ActionListener {
    //      def actionPerformed(e: ActionEvent): Unit = {
    //        new Game(
    //          "sudoko",
    //          Sudoko.controller,
    //          Sudoko.draw
    //        )
    //        setVisible(false)
    //      }
    //    }

    TicButton.addActionListener(ticListener)
    ConnectButton.addActionListener(connectListener)
    //    QueensButton.addActionListener(queensListener)
    //    ChessButton.addActionListener(chessListener)
    //    SudokoButton.addActionListener(sudokoListener)
  }

  def AbstractGameEngine(
      Controller: (
          Array[Array[Int]],
          (String, Int)
      ) => (Boolean, Array[Array[Int]]),
      Drawer: Array[Array[Int]] => Unit
  ): Unit = {

    var turn = 0
    var state = Array.ofDim[Int](3, 3)
    while (true) {
      // input from terminal
      var input = scala.io.StdIn.readLine()
      var pair = (input, turn)
      var (isValid, newState) = Controller(state, pair)
      state = newState
      if (isValid) {
        Drawer(state)
        turn = 1 - turn
      }
    }
  }
}
