public class NQueens {
  int row;
  private int column;

  public NQueens(int row, int column) {
      this.row = row;
      this.column = column;
  }

  // Queen의 행을 증가시키면서 이동해서 이웃을 만듬 
  public void move () {
      row++;
  }

  public boolean ifConflict(NQueens q){
      //  가로/세로 충돌 여부 비교 
      if(row == q.getRow() || column == q.getColumn())
          return true;
          //  대각선 충돌 여부 비교 
      else if(Math.abs(column-q.getColumn()) == Math.abs(row-q.getRow()))
          return true;
      return false;
  }
  public int getRow() {
      return row;
  }
  public int getColumn() {
      return column;
  }
}