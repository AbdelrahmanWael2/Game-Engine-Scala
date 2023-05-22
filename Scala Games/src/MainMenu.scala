import java.awt.{BorderLayout, Color, Dimension, FlowLayout, Font, Graphics, GridBagConstraints, GridBagLayout, GridLayout, Image, Panel}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{BorderFactory, ImageIcon, JButton, JFrame, JPanel}
import javax.swing.{BorderFactory, ImageIcon, JButton, JFrame, JLabel, JPanel}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import scala.math.abs
import scala.util.Random
import org.jpl7._

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
      "/home/ahmed/level2term2/paradigms/testjpl/src/main/scala/Assets/Chess/138004-chess_pieces_on_wooden_table_during_sunset-3840x2160.jpg"
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

        AbstractGameEngine(TicTacToeController, TicTacToeDrawer, TicTacToeInit, null)

      }
    }

    val connectListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(ConnectControler, ConnectDrawer, ConnectInit, null)
      }
    }

    val queensListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(QueensController, QueensDrawer, QueensInit, solveQueens)
      }
    }

    val checkersListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(CheckersController, CheckersDrawer, CheckersInit, null)
      }
    }
    val chessListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(ChessController, ChessDrawer, ChessInit, null)
      }
    }
    val sudokoListener: ActionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        AbstractGameEngine(SudokoController, SudokoDrawer, SudokoInit, solveSuduko)
      }
    }

    TicButton.addActionListener(ticListener)
    ConnectButton.addActionListener(connectListener)
    QueensButton.addActionListener(queensListener)
    ChessButton.addActionListener(chessListener)
    SudokoButton.addActionListener(sudokoListener)
    CheckersButton.addActionListener(checkersListener)
  }

  def AbstractGameEngine(Controller: (Array[Int], (String, Int)) => (Boolean, Array[Int], Int),
                         Drawer: Array[Int] => Unit,
                         Init: () => Array[Int] , solver: (Array[Int] => Array[Int])): Unit = {


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


      panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20))

      val inputField = new JTextField(20)
      inputField.setSize(300,200)
      panel.add(inputField)

      val submitButton = new JButton("Submit")
      val solveButton = new JButton("Solve")
      solveButton.setForeground(Color.RED)


      solveButton.addActionListener(new ActionListener() {
        def actionPerformed(e: ActionEvent): Unit = {
          if(solver == null)
            println(("No solver for that game"))
          else
            Drawer(solver(state))
        }
      })


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

      panel.add(solveButton)
      panel.add(submitButton)
      frame.add(panel)
      frame.setVisible(true)
    }

  }


  def solveSuduko(state:Array[Int]): Array[Int] = {
    val q1 = new Query("consult('/home/abdelrahman/Desktop/ScalaGames/src/main/scala/sudoku.pl')")
    q1.hasSolution

    for (i <- 0 until state.length) {
      if (state(i) < 0) {
        state(i) = -state(i)
      }
    }

    // Using map function
    val positiveNumbers = state.map(num => if (num < 0) -num else num)

    val boardArray = positiveNumbers

    def formatValue(value: Int): String = {
      if (value == 0) "_"
      else value.toString
    }

    def termToList(term: Term): List[List[Int]] = term.toTermArray.toList.map { rowTerm =>
      rowTerm.toTermArray.toList.map(_.intValue())
    }

    def flattenBoard(board: List[List[Int]]): List[Int] = board.flatten

    val queryBody = boardArray.sliding(9, 9).map(row => s"[${row.map(formatValue).mkString(", ")}]").mkString(",\n")
    val query = s"YourBoard = [\n$queryBody\n],\n solve_sudoku(YourBoard, Solution)."
    val q2 = new Query(query)

    if (q2.hasSolution) {
      println("Has solution")
      val solutionTerm = q2.oneSolution().get("Solution").asInstanceOf[Term]
      val solutionList = termToList(solutionTerm)
      val flatBoard = flattenBoard(solutionList)
      val arrayBoard = flatBoard.toArray

      //      println("Sudoku Board as Array:")
      //      arrayBoard.foreach(print)
      //      println()

      arrayBoard
    } else {
      println("Has no solution !")
      state
    }




  }

  def solveQueens(state: Array[Int]): Array[Int] = {
    // init prolog file
    val q = new Query("consult('src/main/scala/solveQueens.pl')")
    q.hasSolution

    // put queens from state to row arr to be sent for prolog
    val boardArray = new Array[Int](8)
    for (i <- 0 until 8) {
      for (j <- 0 until 8) {
        if (state(i * 8 + j) == 1) {
          boardArray(j) = i + 1
        }
      }
    }

    // if there is a column without a queen put a variable
    var count = 0
    val lett = Array("A", "B", "C", "D", "E", "F", "G", "H")

    def formatValue(value: Int): String = {
      if (value == 0) {
        count = count + 1
        lett(count - 1)
      }
      else value.toString
    }

    val queryBody = boardArray.sliding(9, 9).map(row => s"[${row.map(formatValue).mkString(", ")}]").mkString(",\n")
    // ask prolog to solve
    val query = s"n_queens($queryBody)."
    val q2 = new Query(query)
    if (q2.hasSolution) {
      count = 0
      println("Has solution")
      val sol = q2.oneSolution()
      for (i <- 0 until 8) {
        if (boardArray(i) == 0) {
          val solutionTerm = q2.oneSolution().get(lett(count))
          state((solutionTerm.toString.toInt - 1) * 8 + i) = 1
          count = count + 1
        }
      }
    }
    else
      println("no solution")
    return state
  }

  ///////////////Tic Tac Toe

  def TicTacToeInit(): Array[Int] = {
    val Board = Array.fill(9)(0)
    Board
  }


  def TicTacToeDrawer(state: Array[Int]): Unit = {
    val XSymbol: Image =
      ImageIO.read(new File("src/main/scala/Assets/TicTacToe/x.png"))
        .getScaledInstance(140, 140, Image.SCALE_SMOOTH)

    val OSymbol: Image =
      ImageIO.read(new File("src/main/scala/Assets/TicTacToe/o.jpeg"))
        .getScaledInstance(140, 140, Image.SCALE_SMOOTH)

    val frame = new JFrame("Tic Tac Toe")
    frame.setSize(500, 500)

    val panel = new JPanel(new GridLayout(3, 3))

    for (i <- 0 until 9) {
      val button = new JButton()
      button.setFont(new Font("Arial", Font.PLAIN, 72))
      button.setFocusable(false)

      state(i) match {
        case 1 => button.setIcon(new ImageIcon(XSymbol))
        case 2 => button.setIcon(new ImageIcon(OSymbol))
        case _ => button.setText("")
      }

      button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5))
      panel.add(button)
    }

    // create a panel for the row labels
    val rowLabelsPanel = new JPanel(new GridLayout(3, 1))
    for (i <- 1 to 3) {
      val label = new JLabel(i.toString)
      label.setFont(new Font("Arial", Font.PLAIN, 48))
      rowLabelsPanel.add(label)
    }

    // create a panel for the column labels
    val columnLabelsPanel = new JPanel(new GridLayout(1, 3))
    for (c <- 'a' to 'c') {
      val label = new JLabel(c.toString)
      label.setFont(new Font("Arial", Font.PLAIN, 48))

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


  def TicTacToeController(state: Array[Int], pair: (String, Int)): (Boolean, Array[Int], Int) = {
    var (input, turn) = pair
    val parts = input.split("")
    val num = parts(0).toInt
    val alpha = parts(1).charAt(0)

    val columns = Array('a', 'b', 'c')
    if (!(columns.contains(alpha)) || !num.isInstanceOf[Int] || !(input.length == 2))
      return (false, state, turn)

    var index = (num - 1) * 3
    alpha match {
      case 'a' => index = index + 0
      case 'b' => index = index + 1
      case 'c' => index = index + 2
    }

    if (num > 3)
      return (false, state, turn)
    //already exist
    if (!(state(index) == 0))
      return (false, state, turn)


    if (turn % 2 == 0) {
      state(index) = 1
    } else {
      state(index) = 2
    }

    //toggle turn
    turn = turn ^ 1


    (true, state, turn)
  }



  ////////////////Connect 4

  def ConnectInit(): Array[Int] = {
    Array.fill(42)(0)
  }

  def ConnectControler(state: Array[Int], pair: (String, Int)): (Boolean, Array[Int], Int) = {
    val (input, turn) = pair
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g')
    if (
      !(columns.contains(input(1))) || !input(0).isDigit || !(input.length == 2)
    )
      return (false, state, turn)

    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)

    if (x > 5 || y > 6)
      return (false, state, turn)
    if (!(state(x * 6 + y) == 0))
      return (false, state, turn)
    if ((x != 0) && (state((x - 1) * 6 + y)) == 0)
      return (false, state, turn)

    if (turn % 2 == 0) {
      state(x * 7 + y) = 1
    } else {
      state(x * 7 + y) = 2
    }
    var newturn = turn + 1;
    return (true, state, newturn)
  }


  def ConnectDrawer(state: Array[Int]): Unit = {
    val frame = new JFrame("Connect 4")
    frame.setSize(800, 600)

    val panel = new JPanel() {
      override def paint(gfx: Graphics): Unit = {
        super.paintComponent(gfx)
        connectDrawBoard(gfx, state)

      }
    }
    panel setPreferredSize new Dimension(800, 600)
    frame.add(panel)
    frame.setVisible(true)
  }

  def connectDrawBoard(gfx: Graphics, state: Array[Int]): Unit = {
    val backGroundColor = new Color(51, 153, 255)
    val initCircleColor = new Color(255, 255, 255)
    val redColor = new Color(255, 0, 0)
    val yellowColor = new Color(255, 255, 0)
    val whiteColor = new Color(0, 0, 0)
    // init dimentions
    val boarder: Int = 5
    val space: Int = 2
    val cellDimention = 75
    val boardWidth = 2 * boarder + 6 * space + 7 * cellDimention
    val boardHeight = 2 * boarder + 5 * space + 6 * cellDimention
    val startX: Int = (800 - boardWidth) / 2
    val startY: Int = 30
    val font = new Font("default", Font.BOLD, 13)
    // draw blue background
    gfx.setFont(font)
    gfx.setColor(backGroundColor)
    gfx.fillRect(startX, startY, boardWidth, boardHeight)
    // draw empty cells

    for (i <- 0 to 5) {
      for (j <- 0 to 6) {
        if (state(i * 7 + j) == 0)
          gfx.setColor(initCircleColor)
        else if (state(i * 7 + j) == 1)
          gfx.setColor(redColor)
        else if (state(i * 7 + j) == 2)
          gfx.setColor(yellowColor)
        gfx.fillOval(
          startX + boarder + j * (cellDimention + space),
          startY + boarder + (5 - i) * (cellDimention + space),
          cellDimention,
          cellDimention
        )
      }

    }

    // draw rows names
    gfx.setColor(whiteColor)
    val alpha = Array("a", "b", "c", "d", "e", "f", "g")
    for (i <- 0 to 6) {
      gfx.drawString(
        alpha(i),
        startX + boarder + 38 + (cellDimention + space) * i,
        startY - 5
      )
    }

    // draw cols numbers
    for (i <- 0 to 5) {
      gfx.drawString(
        (6 - i).toString(),
        startX - 10,
        startY + boarder + 38 + (cellDimention + space) * i
      )
    }
  }


  ////////////////Checkers

  def CheckersInit(): Array[Int] = {
    var state = Array.fill(64)(0)
    for (i <- 0 to 7) {
      for (j <- 0 to 7) {
        if (i % 2 != j % 2) {
          if (i < 3) {
            state(i * 8 + j) = 1;
          } else if (i > 4) {
            state(i * 8 + j) = -1;
          }
        }
      }
    }
    return state
  }

  def CheckersController(
                          state: Array[Int],
                          pair: (String, Int)
                        ): (Boolean, Array[Int], Int) = {
    val (input, turn) = pair
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    if (
      !(columns.contains(input(1)))
        || !input(0).isDigit
        || !(input.length == 4)
        || !(columns.contains(input(3)))
        || !input(2).isDigit
    )
      return (false, state, turn)

    val x1 = 8 - input.substring(0, 1).toInt
    val y1 = columns.indexOf(input(1), 0)
    val x2 = 8 - input.substring(2, 3).toInt
    val y2 = columns.indexOf(input(3), 0)

    // check src
    var (valid, jump) = selectSrc(x1, y1, state, turn)
    if (!valid) {
      println("wrong src")
      return (false, state, turn)
    }
    // check dest

    if (!selectDest(x1, y1, x2, y2, state, jump)) {
      println("wrong dest")
      return (false, state, turn)
    }
    // not on the diagonal
    if (Math.abs(y2 - y1) != Math.abs(x2 - x1)) {
      println("not diagonal")
      return (false, state, turn);
    }
    // not forward
    if ((x2 < x1 && turn % 2 == 0) || (x2 > x1 && turn % 2 == 1)) {
      println("not forward")
      return (false, state, turn);
    }
    // do not jump over a friend
    if (Math.abs(x2 - x1) != 1 && !jump) {
      return (false, state, turn);
    }
    var newturn = turn + 1
    state(x2 * 8 + y2) = state(x1 * 8 + y1);
    state(x1 * 8 + y1) = 0;
    if (jump) {
      state((x1 + state(x2 * 8 + y2)) * 8 + (y1 + (y2 - y1) / 2)) = 0;
    }
    if (jump) {
      var (t, j) = getTarget(state, turn)
      if (j) {
        newturn = newturn - 1
      }
    }
    return (true, state, newturn)
  }

  def selectSrc(
                 i1: Int,
                 j1: Int,
                 state: Array[Int],
                 turn: Int
               ): (Boolean, Boolean) = {
    var (t, jump) = getTarget(state, turn);
    // there is no possible jump only check turn
    if (!jump) {
      if (
        (turn % 2 == 0 && state(i1 * 8 + j1) != 1) || (turn % 2 == 1 && state(
          i1 * 8 + j1
        ) != -1)
      )
        return (false, jump);
      else
        return (true, jump);
    }
    // there must be a jump check that the src is valid
    else {
      var direction = state(i1 * 8 + j1);
      // stand on correct place
      if (
        i1 + direction == t.substring(0, 1).toInt && Math.abs(
          j1 - t.substring(1, 2).toInt
        ) == 1
      )
        return (true, jump)
      else {
        println(i1 + direction + " " + t(0))
        return (false, jump)
      }

    }
  }

  def selectDest(
                  i1: Int,
                  j1: Int,
                  i2: Int,
                  j2: Int,
                  state: Array[Int],
                  jump: Boolean
                ): Boolean = {
    // destination not empty
    if (state(i2 * 8 + j2) != 0) {
      println("not empty")
      return false;
    }
    // may be valid
    else {
      // there is a jump
      if (jump) {
        // correct jump
        if (Math.abs(i2 - i1) == Math.abs(j2 - j1) && Math.abs(j2 - j1) == 2) {
          return true;
        }
        // not correct jump
        return false;
      }
      // empty and no jump
      // more check will happen in isValidMove
      return true;
    }
  }

  def getTarget(state: Array[Int], turn: Int): (String, Boolean) = {
    var target = "";
    var jump = false;
    for (i <- 0 to 7) {
      for (j <- 0 to 7) {
        var direction = state(i * 8 + j);
        // not empty and balck
        if (
          direction != 0 &&
            i % 2 != j % 2 &&
            i + direction != -1 &&
            i + direction != 8
        ) {
          // has turn
          if (
            (turn % 2 == 0 && direction == 1) ||
              (turn % 2 == 1 && direction == -1)
          ) {
            if (
              j != 7 && state((i + direction) * 8 + (j + 1)) == -1 * direction
            ) {
              var ti = i + direction;
              var tj = j + 1;

              if (
                ti + direction > -1 &&
                  2 * tj - j > -1 &&
                  ti + direction < 8 &&
                  2 * tj - j < 8 &&
                  state((ti + direction) * 8 + (tj + (tj - j))) == 0
              ) {
                target = "" + ti + tj;
                return (target, true);
              }
            }
            if (
              j != 0 && state((i + direction) * 8 + (j - 1)) == -1 * direction
            ) {
              var ti = i + direction;
              var tj = j - 1;

              if (
                ti + direction > -1 &&
                  2 * tj - j > -1 &&
                  ti + direction < 8 &&
                  2 * tj - j < 8 &&
                  state((ti + direction) * 8 + (tj + (tj - j))) == 0
              ) {
                target = "" + ti + tj;
                return (target, true);
              }
            }
          }
        }
      }
    }
    return (target, false)
  }

  def CheckersDrawer(state: Array[Int]): Unit = {

    for (i <- 0 to 7) {
      for (j <- 0 to 7) {
        print(state(i * 8 + j) + " ")
      }
      println()
    }
    // println(state(1))
    val frame = new JFrame("Checkers")
    frame.setSize(800, 600)

    val panel = new JPanel() {
      override def paint(gfx: Graphics): Unit = {
        super.paintComponent(gfx)
        drawBoard(gfx, state)

      }
    }
    panel setPreferredSize new Dimension(800, 600)
    frame.add(panel)
    frame.setVisible(true)
  }

  def drawBoard(gfx: Graphics, state: Array[Int]): Unit = {
    val writeColor = new Color(0, 0, 0)
    val black = new Color(128, 128, 128)
    val white = new Color(255, 255, 255)
    val startY: Int = 15
    val cellDimention = 50
    val startX: Int = (800 - 8 * cellDimention) / 2
    val font = new Font("default", Font.BOLD, 13)
    val whiteCircle: Image =
      ImageIO
        .read(new File("src/main/scala/Assets/Checkers/whiteCircle.png"))
        .getScaledInstance(40, 40, Image.SCALE_SMOOTH)
    val blackCircle: Image =
      ImageIO
        .read(new File("src/main/scala/Assets/Checkers/blackCircle.png"))
        .getScaledInstance(40, 40, Image.SCALE_SMOOTH)
    gfx.setFont(font)

    // draw rows names
    gfx.setColor(writeColor)
    val alpha = Array("a", "b", "c", "d", "e", "f", "g", "h")
    for (i <- 0 to 7) {
      gfx.drawString(
        alpha(i),
        startX + 25 + (cellDimention) * i,
        startY - 5
      )
    }

    // draw cols numbers
    for (i <- 0 to 7) {
      gfx.drawString(
        (8 - i).toString(),
        startX - 10,
        startY + 25 + (cellDimention) * i
      )
    }

    for (i <- 0 to 7) {
      for (j <- 0 to 7) {
        if (i % 2 != j % 2)
          gfx.setColor(black)
        else
          gfx.setColor(white)
        gfx.fillRect(
          startX + j * cellDimention,
          startY + (i) * cellDimention,
          cellDimention,
          cellDimention
        )
        if (state(i * 8 + j) == -1) {
          gfx.drawImage(
            whiteCircle,
            startX + j * cellDimention + 5,
            startY + i * cellDimention + 5,
            null
          )

        } else if (state(i * 8 + j) == 1) {
          gfx.drawImage(
            blackCircle,
            startX + j * cellDimention + 5,
            startY + i * cellDimention + 5,
            null
          )
        }

      }
    }
  }


  ///////////////Chess

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
      ImageIO.read(new File("src/main/scala/Assets/Chess/black/pawn.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whitepawn: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/white/pawn.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackking: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/black/king.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whiteking: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/white/king.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)


    val blackqueen: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/black/queen.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whitequeen: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/white/queen.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackrook: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/black/rook.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whiterook: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/white/rook.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackbishop: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/black/bishop.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whitebishop: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/white/bishop.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val blackknight: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/black/knight.png"))
        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)

    val whiteknight: Image =
      ImageIO.read(new File("src/main/scala/Assets/Chess/white/knight.png"))
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


  def ChessController(state: Array[Int], pair: (String, Int)): (Boolean, Array[Int], Int) = {

    val (input, turn) = pair
    val parts = input.split("")
    val num = parts(0).toInt
    val alpha = parts(1).charAt(0)
    val num2 = parts(2).toInt
    val alpha2 = parts(3).charAt(0)

    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    if (!(columns.contains(alpha)) || num < 1 || num > 8 || !(columns.contains(alpha2)) || num2 < 1 || num2 > 8 || !(input.length == 4)) {
      return (false, state, turn)
    }

    val startRow = 8 - num
    val startCol = alpha - 'a'
    val endRow = 8 - num2
    val endCol = alpha2 - 'a'

    if (startRow < 0 || startRow > 7 || startCol < 0 || startCol > 7) {
      return (false, state, turn)
    }

    val piece = state(startRow * 8 + startCol)


    if ((turn % 2 == 0 && piece < 0) || (turn % 2 == 1 && piece > 0)) { // Check if the starting position contains a piece of the correct color
      println("Not ur turn")
      return (false, state, turn)
    }

    val validMoves = getValidMoves(state, startRow, startCol)

    if (!validMoves.contains((endRow, endCol)))
      return (false, state, turn)
    state(endRow * 8 + endCol) = state(startRow * 8 + startCol)
    state(startRow * 8 + startCol) = 0
    val newturn = turn + 1

    (true, state, newturn)
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


  ///////////////Sudoko


  def SudokoInit(): Array[Int] = {
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
        button.setText(abs(state(i)).toString)
      }


      button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK))
      if ((i - 2) % 3 == 0) {
        button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK))
      }
      if (i > 17 && i < 27) {
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

  def SudokoController(state: Array[Int], pair: (String, Int)): (Boolean, Array[Int], Int) = {
    val (input, turn) = pair
    val parts = input.split("")
    val num = parts(0).toInt
    val alpha = parts(1).charAt(0)
    var toInsert = 0
    if (parts(2).charAt(0) == 'x') {
      toInsert = 120;
    } else {
      toInsert = parts(2).toInt
    }


    if ((toInsert > 9 && toInsert != 120) || toInsert < 1) {
      return (false, state, turn)
    }


    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')
    if (!(columns.contains(alpha)) || num < 1 || num > 9 || !(input.length == 3)) {
      println("Invalid Input")
      return (false, state, turn)
    }


    val row = num - 1
    val col = alpha - 'a'
    val boxRow = (row / 3) * 3
    val boxCol = (col / 3) * 3
    val boxStartIndex = boxRow * 9 + boxCol

    // check if the number is already present in the same row or column
    for (i <- 0 until 9) {
      if (abs(state(row * 9 + i)) == toInsert || abs(state(i * 9 + col)) == toInsert) {
        println("Invalid Move: Number already present in the same row or column")
        return (false, state, turn)
      }
    }
    //check if the number is already present in the same 3x3 box
    for (i <- 0 until 3; j <- 0 until 3) {
      val index = boxStartIndex + i * 9 + j
      if (abs(state(index)) == toInsert) {
        println("Invalid Move: Number already present in the same 3x3 box")
        return (false, state, turn)
      }
    }

    val index = row * 9 + col
    if (toInsert == 120) {
      if (state(index) < 0) {
        println("Cannot delete this number")
        return (false, state, turn)
      } else {
        state(index) = 0
        return (true, state, turn)
      }
    }


    state(index) = toInsert


    (true, state, turn)
  }

  import scala.util.Random

  def generateSudoku(): Array[Int] = {
    val board = Array.fill(81)(0)
    generateSudokuHelper(board, 0)
    removeNumbers(board, 0.4)
    for (i <- 0 until 80) {
      board(i) = board(i) * -1
    } // remove 50% of the numbers
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




  //////////////Queens

  def QueensInit(): Array[Int] = {
    return Array.fill(64)(0)
  }

  def QueensController(
                        state: Array[Int],
                        pair: (String, Int)
                      ): (Boolean, Array[Int], Int) = {
    val (input, turn) = pair
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    if (
      !(columns.contains(input(1))) || !input(0).isDigit || !(input.length == 2)
    )
      return (false, state, turn)

    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)

    if (x > 7 || y > 7)
      return (false, state, turn)

    for (i <- 0 to 63) {
      if (state(i) == 1) {
        if (
          i % 8 == y || i / 8 == x || math.abs(i % 8 - y) == math.abs(i / 8 - x)
        )
          return (false, state, turn)
      }
    }
    state(x * 8 + y) = 1
    return (true, state, turn)
  }

  def QueensDrawer(state: Array[Int]): Unit = {
    val frame = new JFrame("8 Queens")
    frame.setSize(800, 600)

    val panel = new JPanel() {
      override def paint(gfx: Graphics): Unit = {
        super.paintComponent(gfx)
        queenDrawBoard(gfx, state)

      }
    }
    panel setPreferredSize new Dimension(800, 600)
    frame.add(panel)
    frame.setVisible(true)
  }

  def queenDrawBoard(gfx: Graphics, state: Array[Int]): Unit = {
    val writeColor = new Color(0, 0, 0)
    val black = new Color(128, 128, 128)
    val white = new Color(255, 255, 255)
    val startY: Int = 15
    val cellDimention = 50
    val startX: Int = (800 - 8 * cellDimention) / 2
    val font = new Font("default", Font.BOLD, 13)
    val queen: Image =
      ImageIO
        .read(new File("/home/abdelrahman/Desktop/ScalaGames/src/Assets/Queen/queen.png"))
        .getScaledInstance(40, 40, Image.SCALE_SMOOTH)
    gfx.setFont(font)

    // draw rows names
    gfx.setColor(writeColor)
    val alpha = Array("a", "b", "c", "d", "e", "f", "g", "h")
    for (i <- 0 to 7) {
      gfx.drawString(
        alpha(i),
        startX + 25 + (cellDimention) * i,
        startY - 5
      )
    }

    // draw cols numbers
    for (i <- 0 to 7) {
      gfx.drawString(
        (8 - i).toString(),
        startX - 10,
        startY + 25 + (cellDimention) * i
      )
    }

    for (i <- 0 to 7) {
      for (j <- 0 to 7) {
        if (i % 2 == j % 2)
          gfx.setColor(white)
        else
          gfx.setColor(black)
        gfx.fillRect(
          startX + j * cellDimention,
          startY + (7 - i) * cellDimention,
          cellDimention,
          cellDimention
        )
        if (state(i * 8 + j) == 1)
          gfx.drawImage(
            queen,
            startX + j * cellDimention + 5,
            startY + (7 - i) * cellDimention + 5,
            null
          )

      }
    }
  }

}
