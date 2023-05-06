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
import scala.collection.mutable.ArrayBuffer
import javax.swing.{JFrame, JPanel, JButton, JLabel, ImageIcon, BorderFactory}

object Connect {
  def ConnectControler(
      state: ArrayBuffer[Int],
      pair: (String, Int)
  ): (Boolean, ArrayBuffer[Int]) = {
    var tempState = ArrayBuffer.fill(42)(0)
    if (state.length != 9) {
      tempState = state
    }
    val (input, turn) = pair
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g')
    if (
      !(columns.contains(input(1))) || !input(0).isDigit || !(input.length == 2)
    )
      return (false, tempState)

    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)

    if (x > 5 || y > 6)
      return (false, tempState)
    if (!(tempState(x * 6 + y) == 0))
      return (false, tempState)
    if ((x != 0) && (tempState((x - 1) * 6 + y)) == 0)
      return (false, tempState)

    if (turn % 2 == 0) {
      tempState(x * 7 + y) = 1
    } else {
      tempState(x * 7 + y) = 2
    }
    return (true, tempState)
  }

  def ConnectDrawer(state: ArrayBuffer[Int]): Unit = {
    var tempState = ArrayBuffer.fill(42)(0)
    if (state.length != 9) {
      tempState = state
    }

    val frame = new JFrame("Connect 4")
    frame.setSize(800, 600)

    val panel = new JPanel() {
      override def paint(gfx: Graphics) {
        super.paintComponent(gfx)
        drawBoard(gfx, tempState)

      }
    }
    panel setPreferredSize new Dimension(800, 600)
    frame.add(panel)
    frame.setVisible(true)
  }

  def drawBoard(gfx: Graphics, state: ArrayBuffer[Int]) {
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

}
