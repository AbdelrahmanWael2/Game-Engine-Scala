import java.awt.{
  BorderLayout,
  Color,
  Dimension,
  Font,
  Graphics,
  GridLayout,
  Image,
  Panel
}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{BorderFactory, ImageIcon, JButton, JFrame, JPanel}
import javax.swing.{JFrame, JPanel, JButton, JLabel, ImageIcon, BorderFactory}

object Checkers {

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
      override def paint(gfx: Graphics) {
        super.paintComponent(gfx)
        drawBoard(gfx, state)

      }
    }
    panel setPreferredSize new Dimension(800, 600)
    frame.add(panel)
    frame.setVisible(true)
  }

  private def drawBoard(gfx: Graphics, state: Array[Int]) {
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
}
