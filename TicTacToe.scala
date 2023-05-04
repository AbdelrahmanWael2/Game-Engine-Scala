object TicTacToe {
  def TicTacToeDrawer(state: Array[Array[Int]]): Unit = {
    println("IN DRAWER")
  }


  def TicTacToeController(state: Array[Array[Int]], pair: (String, Int)): (Boolean, Int) = {
    val (input, turn) = pair
    return (true, turn)
  }
}
