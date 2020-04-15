import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class main {
	public static void main(String[] args) throws IOException {
		// String형 숫자인 첫번째 인자를 int형으로 바꿔줌
		int n = Integer.parseInt(args[0]);
		String path = args[1];
	   
    	// 입력받는 두번째 파라미터(절대경로)와 첫번째 파라미터()을 이용하여 "resultN.txt" 파일을 생성
		File file = new File(args[1] + "/result" + Integer.toString(n) + ".txt");
	    FileWriter fw = null;
	    
	    try {
	    	fw = new FileWriter(file, false);

			// GA를 위한 클래스를 선언하고 초기화한다. 
			Genetic genetic = new Genetic();
			
			genetic.init(n);
			
			// maxScore = n * n
			int maxScore = n * n;
			
			// 위에서 정의한 findSolution을 통해 maxScore에 해당하는 유전자가 있으면 반환한다. 
			Chromosome current = genetic.findSolution(maxScore);
			
			long preFinding = System.currentTimeMillis();
			
			// 해당 유전자를 찾을 때까지 반복한다. 
			while (current == null) {
				// Uniform crossover를 통해 새로운 세대를 생성한다. 
				genetic.crossover(n);
				// 새로운 세대에서 maxScore를 가진 유전자가 있는지 확인한다.
				current = genetic.findSolution(maxScore);
			}
			
			long afterFinding = System.currentTimeMillis();
			
			// MaxScore에 해당하는 유전자를 출력한다.
			current.printState();
			System.out.println("Generation: " + genetic.getGeneration());
			
			double elapsedTime = (afterFinding - preFinding) / 1000.0;
			
			// buffer에 쓰기 작업 
			fw.write(String.format(">Genetic Algorithm\n"));
				for (int tmp: current.getState())
					fw.write(tmp + " ");
				fw.write("\n");
				fw.write("=> Total Elapsed Time: " + String.format("%.3f", elapsedTime) + "\n");
			fw.flush();
			} catch (IOException e) {
	        e.printStackTrace();
	        } finally {
	        if (fw != null)
	            fw.close();
	        }
	}
}
