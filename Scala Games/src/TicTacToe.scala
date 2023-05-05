import java.awt.{BorderLayout, Color, Dimension, Font, Graphics, GridLayout, Image, Panel}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{BorderFactory, ImageIcon, JButton, JFrame, JPanel}
import scala.collection.mutable.ArrayBuffer
import javax.swing.{JFrame, JPanel, JButton, JLabel, ImageIcon, BorderFactory}


object TicTacToe {



  def TicTacToeDrawer(state: ArrayBuffer[Int]): Unit = {
    val XSymbol: Image =
      ImageIO.read(new File("src/Assets/TicTacToe/x.png"))
        .getScaledInstance(140, 140, Image.SCALE_SMOOTH)

    val OSymbol: Image =
      ImageIO.read(new File("src/Assets/TicTacToe/o.jpeg"))
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




  def TicTacToeController(state: ArrayBuffer[Int], pair: (String, Int)): (Boolean, ArrayBuffer[Int]) = {
    var (input, turn) = pair
    val parts = input.split("")
    val num = parts(0).toInt
    val alpha = parts(1).charAt(0)

    val columns = Array('a', 'b', 'c')
    if (!(columns.contains(alpha)) || !num.isInstanceOf[Int] || !(input.length == 2))
      return (false, state)


    var index = (num - 1) * 3
    alpha match {
      case 'a' => index = index + 0
      case 'b' => index = index + 1
      case 'c' => index = index + 2
    }

    if (num > 3)
      return (false, state)

    //already exist
    if (!(state(index) == 0))
      return (false, state)


    if (turn % 2 == 0) {
      state(index) = 1
    } else {
      state(index) = 2
    }

    //toggle turn
    turn = turn ^ 1


    (true, state)
  }
}
