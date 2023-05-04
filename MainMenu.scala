import java.awt.{BorderLayout, Dimension, GridLayout}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import TicTacToe.TicTacToeDrawer
import TicTacToe.TicTacToeController
import sun.security.krb5.KrbException
import sun.security.krb5.KrbException.errorMessage

import java.util.Scanner
import scala.collection.mutable.ArrayBuffer


object MainMenu {

  def main(args: Array[String]): Unit = {
    MainMenu()
  }

  def MainMenu(): Unit = {
    val frame = new JFrame()
    frame.setVisible(true)

    //rows, columns, h gap, v gap
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

    //    val connectListener: ActionListener = new ActionListener {
    //      def actionPerformed(e: ActionEvent): Unit = {
    //        new Game(
    //          "connect 4",
    //          connect.controller,
    //          connect.draw
    //        )
    //        setVisible(false)
    //      }
    //    }
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
    //    ConnectButton.addActionListener(connectListener)
    //    QueensButton.addActionListener(queensListener)
    //    ChessButton.addActionListener(chessListener)
    //    SudokoButton.addActionListener(sudokoListener)
  }

  def AbstractGameEngine(Controller: (ArrayBuffer[Int], (String, Int)) => (Boolean,ArrayBuffer[Int]),
                         Drawer: ArrayBuffer[Int] => Unit): Unit = {

    var turn = 0
    var state = ArrayBuffer.fill(9)(0)

    createInputWindow()

    def createInputWindow(): Unit = {
      val frame = new JFrame("Input Window")
      frame.setSize(400, 200)
      frame.setLayout(new BorderLayout())
      frame.setLocation(800,400)

      val panel = new JPanel()
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))

      val inputField = new JTextField(20)
      panel.add(inputField)

      val submitButton = new JButton("Submit")
      Drawer(state)

      //
      submitButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent): Unit = {
          var input = ""

          val (valid, newState) = Controller(state, (inputField.getText(), turn))
          if(!valid){
            println("INVALID INPUT")
            createInputWindow()
            frame.dispose()
          }
          else{
            println("VALID MOVE")
            Drawer(newState)
            state = newState
            turn = turn + 1
          }

          frame.dispose()

          createInputWindow()

          inputField.setText("")
        }
      })


      //
      panel.add(submitButton)

      frame.add(panel, BorderLayout.CENTER)
      frame.setVisible(true)
    }


  }




}


