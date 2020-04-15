// Artificial Intelligence_Programming Assignment 02
// N-Queens : Hill Climbing
// 2019. 10. 01 윤세령 

import java.util.Random;
import java.io.FileWriter;
import java.io.*;

public class HillClimbing {
  private static int n;
  private static int heuristic = 0;
  private static int randomRestarts = 0;

  // [Method] 새로 체스보드를 랜덤하게 생성해주는 메서드 
  public static NQueens[] generateBoard() {
      NQueens[] startBoard = new NQueens[n];
      Random rndm = new Random();
      for(int i=0; i<n; i++){
          startBoard[i] = new NQueens(rndm.nextInt(n), i);
      }
      return startBoard;
  }
  // [Method] 정답인 queen state를 출력해주는 메서드 
  public static String printState (NQueens[] state) {
      String answer = "";
      System.out.println();
      // 0행부터 차례대로 행의 값을 받아와서 정답에 추가
      for (int i = 0; i < state.length; i++) {
    	  answer += state[i].getRow() + " ";
    	  }
      return answer;
  }
  
  // [Method] 현재 상태의 heuristic을 찾아주는 메서드. i와 j에서 충돌이 발생한다면 heuristic변수를 증가시킴. 
  // 최종적으로 heuristic변수의 수가 0에 가장 가까운 체스보드가 정답 
  public static int findHeuristic (NQueens[] state) {
      int heuristic = 0;
      for (int i = 0; i< state.length; i++) {
          for (int j=i+1; j< state.length; j++ ) {
              if (state[i].ifConflict(state[j]))
                  heuristic++;
          }
      }
      return heuristic;
  }

  // [Method] 다음 단계(더 올라갈 단계)가 있다면 찾는 메서드(best heuristic값 갱신) 
  public static NQueens[] nextBoard (NQueens[] presentBoard) {
      NQueens[] nextBoard = new NQueens[n];
      NQueens[] tmpBoard = new NQueens[n];
      int presentHeuristic = findHeuristic(presentBoard);
      int bestHeuristic = presentHeuristic;
      int tempH;

      for (int i=0; i<n; i++) {
          // presentBoard를 nextBoard와 tmpBoard로 복사 
          nextBoard[i] = new NQueens(presentBoard[i].getRow(), presentBoard[i].getColumn());
          tmpBoard[i] = nextBoard[i];
      }
      //  행 반복
      for (int i=0; i<n; i++) {
          if (i>0)
              tmpBoard[i-1] = new NQueens (presentBoard[i-1].getRow(), presentBoard[i-1].getColumn());
          tmpBoard[i] = new NQueens (0, tmpBoard[i].getColumn());
          // 열 반복
          for (int j=0; j<n; j++) {
              // tmpBoard의 Heuristic 값 추출 
              tempH = findHeuristic(tmpBoard);
              
              // presentBoard의 휴리스틱 값보다 tmpBoard의 휴리스틱 값이 더 작으면, tmpBoard 휴리스틱 값을 bestHeuristic으로 교체
              if (tempH < bestHeuristic) {
                  bestHeuristic = tempH;
                  // tmpBoard가 best Board가 되도록 nextBoard의 값을 바꿔줌 
                  for (int k=0; k<n; k++) {
                      nextBoard[k] = new NQueens(tmpBoard[k].getRow(), tmpBoard[k].getColumn());
                  }
              }
              
              // tmpBoard의 queen값 이동하기 
              if (tmpBoard[i].getRow()!= n-1)
                  tmpBoard[i].move();
          }
      }
      // presentBoard의 휴리스틱 값과 bestBoard의 휴리스틱 값이 일치하는 경우 : Local Optimum
      // random restart하기 위해 새로 체스보드를 생성
      if (bestHeuristic == presentHeuristic) {
          randomRestarts++;
          nextBoard = generateBoard();
          heuristic = findHeuristic(nextBoard);
      } else
          heuristic = bestHeuristic;
      return nextBoard;
  }
 
  public static void main(String[] args) throws IOException {
	  int presentHeuristic;
      FileWriter fw = null;
      
	  // String형 숫자인 첫번째 인자를 int형으로 바꿔줌
	  n = Integer.parseInt(args[0]);
      
      // 초기 board 생성 
      NQueens[] presentBoard = generateBoard();
      presentHeuristic = findHeuristic(presentBoard);
      // presentBoard가 정답인지 검사 
      while (presentHeuristic != 0) {
          // presentBoard가 정답이 아닌 경우, nextBoard를 presentBoard로 변경 
          presentBoard = nextBoard(presentBoard);
          presentHeuristic  = heuristic;
      }
      // 파일에 출력하기 위해 String 변수 answer에 정답을 저장 
      String answer = printState(presentBoard);
      
      try {
    	  // 입력받는 두번째 파라미터(절대경로)와 첫번째 파라미터()을 이용하여 "resultN.txt" 파일을 생성
		  fw = new FileWriter(args[1] + "/result" + args[0] + ".txt");
		  fw.write(String.format(">Hill Climbing\n"));
		  fw.write(answer + "\n");
		  fw.write(String.format("=> with %d random restarts", randomRestarts));
		  fw.flush();
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          if (fw != null)
              fw.close();
      }
  }
}