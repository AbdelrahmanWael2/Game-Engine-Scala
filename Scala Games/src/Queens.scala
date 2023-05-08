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

object Queens {
  def QueensInit(): Array[Int] = {
    return Array.fill(42)(0)
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
    val queen: Image =
      ImageIO
        .read(new File("src/main/scala/Assets/Queen/queen.png"))
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
