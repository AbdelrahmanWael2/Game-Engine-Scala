object TicTacToe {
  def TicTacToeDrawer(state: Array[Array[Int]]): Unit = {
    println("IN DRAWER")
  }

  def TicTacToeController(
      state: Array[Array[Int]],
      pair: (String, Int)
  ): (Boolean, Array[Array[Int]]) = {
    val (input, turn) = pair
    return (true, state)
  }

  def ConnectControler(
      state: Array[Array[Int]],
      pair: (String, Int)
  ): (Boolean, Array[Array[Int]]) = {
    val (input, turn) = pair
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g')
    if (
      !(columns.contains(input(1))) || !input(0).isDigit || !(input.length == 2)
    )
      return (false, state)

    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)

    if (x > 5 || y > 6)
      return (false, state)
    if (!(state(x)(y) == 0))
      return (false, state)
    if ((x != 0) && (state(x - 1)(y) == 0))
      return (false, state)

    val i = y
    val j = 6 - x
    if (turn == 0) {
      state(x)(y) = 1
    } else {
      state(x)(y) = 2
    }
    return (true, state)
  }

  def ConnectDrawer(state: Array[Array[Int]]): Unit = {
    println("IN DRAWER")
  }
}
