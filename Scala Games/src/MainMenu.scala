import Chess.{ChessController, ChessDrawer, ChessInit}
import Sudoko.{SudokoController, SudokoDrawer, SudokoInit}

import java.awt.{
  BorderLayout,
  Color,
  Dimension,
  GridBagConstraints,
  GridBagLayout,
  GridLayout
}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import TicTacToe.{TicTacToeController, TicTacToeDrawer, TicTacToeInit}
import Connect.ConnectDrawer
import Queens.QueensController
import Queens.QueensDrawer
import Checkers.CheckersDrawer
import Checkers.CheckersController
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
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)

    // Create a label and set its properties
    val label = new JLabel("Games", SwingConstants.CENTER)
    label.setFont(label.getFont.deriveFont(36f))
    label.setForeground(Color.RED)
    label.setBackground(Color.BLACK)

    // Create a panel for the buttons and set its layout
    val buttonPanel = new JPanel(new GridBagLayout())

    // Create the buttons and set their properties
    val TicButton = new JButton("Tic-Tac-Toe")
    TicButton.setPreferredSize(new Dimension(150, 50))
    val ConnectButton = new JButton("Connect 4")
    ConnectButton.setPreferredSize(new Dimension(150, 50))
    val CheckersButton = new JButton("Checkers")
    CheckersButton.setPreferredSize(new Dimension(150, 50))
    val ChessButton = new JButton("Chess")
    ChessButton.setPreferredSize(new Dimension(150, 50))
    val SudokoButton = new JButton("Sudoku")
    SudokoButton.setPreferredSize(new Dimension(150, 50))
    val QueensButton = new JButton("8-Queens")
    QueensButton.setPreferredSize(new Dimension(150, 50))

    // Add the buttons to the panel
    val gbc = new GridBagConstraints()
    gbc.gridx = 0
    gbc.gridy = 0
    gbc.gridwidth = GridBagConstraints.REMAINDER
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.insets = new java.awt.Insets(10, 10, 10, 10)
    buttonPanel.add(TicButton, gbc)
    gbc.gridy += 1
    buttonPanel.add(ConnectButton, gbc)
    gbc.gridy += 1
    buttonPanel.add(CheckersButton, gbc)
    gbc.gridy += 1
    buttonPanel.add(ChessButton, gbc)
    gbc.gridy += 1
    buttonPanel.add(SudokoButton, gbc)
    gbc.gridy += 1
    buttonPanel.add(QueensButton, gbc)

    // Load the background image and set it as the content pane
    val centerPanel = new JPanel(new BorderLayout())
    centerPanel.setOpaque(false)

    // Add the label and button panel to the center panel
    centerPanel.add(label, BorderLayout.NORTH)
    centerPanel.add(buttonPanel, BorderLayout.CENTER)

    // Load the background image and set it as the content pane
    val backgroundImage = new ImageIcon(
      "src/Assets/Chess/138004-chess_pieces_on_wooden_table_during_sunset-3840x2160.jpg"
    )
    val backgroundPanel = new JPanel(new BorderLayout())
    backgroundPanel.add(new JLabel(backgroundImage), BorderLayout.CENTER)
    backgroundPanel.add(centerPanel, BorderLayout.CENTER)

    frame.setContentPane(backgroundPanel)

    // Set the frame properties and display it
    frame.setResizable(false)
    frame.setMinimumSize(new Dimension(800, 800))
    frame.setLocationRelativeTo(null)
    frame.pack()
    frame.setVisible(true)

    val ticListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {

        AbstractGameEngine(TicTacToeController, TicTacToeDrawer, TicTacToeInit)

      }
    }

    val connectListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(ConnectControler, ConnectDrawer)
      }
    }

    val queensListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(QueensController, QueensDrawer)
      }
    }

    val checkersListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(CheckersController, CheckersDrawer)
      }
    }
    val chessListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(ChessController, ChessDrawer, ChessInit)
      }
    }
    val sudokoListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(SudokoController, SudokoDrawer, SudokoInit)
      }
    }

    TicButton.addActionListener(ticListener)
   ConnectButton.addActionListener(connectListener)
   QueensButton.addActionListener(queensListener)
    ChessButton.addActionListener(chessListener)
    SudokoButton.addActionListener(sudokoListener)
    CheckersButton.addActionListener(checkersListener)
  }

  def AbstractGameEngine(
      Controller: (Array[Int], (String, Int)) => (Boolean, Array[Int], Int),
      Drawer: Array[Int] => Unit,
      Init: () => Array[Int]
  ): Unit = {

    var turn = 0
    var state = Init()

    Drawer(state)
    createInputWindow()

    def createInputWindow(): Unit = {
      val frame = new JFrame("Input Window")
      frame.setSize(400, 200)
      frame.setLayout(new BorderLayout())
      frame.setLocationRelativeTo(null)

      val panel = new JPanel()
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))

      val inputField = new JTextField(20)
      panel.add(inputField)

      val submitButton = new JButton("Submit")

      //
      submitButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent): Unit = {
          var input = ""
          val (valid, newState, newTurn) =
            Controller(state, (inputField.getText(), turn))
          if (!valid) {
            println("INVALID MOVE")
            // createInputWindow()
            // frame.dispose()
          } else {
            println("VALID MOVE")
            Drawer(newState)
            state = newState
            turn = newTurn
          }
          frame.dispose()
          createInputWindow()
          inputField.setText("")
        }
      })

      panel.add(submitButton)
      frame.add(panel, BorderLayout.CENTER)
      frame.setVisible(true)
    }

  }

}
