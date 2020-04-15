// Artificial Intelligence_Programming Assignment 03
// N-Queens : Genetic Algorithm
// 2019. 10. 16 윤세령 


import java.util.Random;

public class Genetic {
	public static int POPULATION_SIZE = 100;			// Population size
	public static double MUTATION_RATE = 0.08;			// 돌연변이 확률 
	public static double CROSSOVER_RATE = 0.8;      	// crossover 확률 (Range: 0.0 < CROSSOVER_RATE < 1.0) 

	int generation;										// 현재 generation
	Chromosome[] current;								// 현재 유전자의 배열 
	float[] proportional;								// Proportional selection에 쓰이는 변수 
	
	public Genetic() {
		generation = 0;
		current = new Chromosome[POPULATION_SIZE];
		proportional = new float[POPULATION_SIZE];
	}
	
	public Chromosome[] getCurrent() {
		return current;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	// [Method] 현재 유전자 배열을 출력하는 메서드. 
	public void printCurrent() {
		for (int i = 0; i < POPULATION_SIZE; i++)
			current[i].printState();
		System.out.println();
	}
	
	// [Method] parents를 생성하는 메서드. 
	// 무작위로 population size만큼의 배열을 생성한 후, proportion select에 의해 선택된 배열을 가지고 wheel을 만듦. 
	public void init(int n) {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			Chromosome chromo = new Chromosome(n);
			chromo.init();
			current[i] = chromo;
		}
		makeWheel();
	}
	
	// [Method] 선택된 parents로 wheel을 만드는 메서드.
	// score가 높으면 선택될 확률도 높아진다. 
	public void makeWheel() {
		int sum = 0;
		float score;
		for (int i = 0; i < POPULATION_SIZE; i++)
			sum += current[i].countConflict();
		
		for (int j = 0; j < POPULATION_SIZE; j++) {
			// score = 해당 유전자의 점수 / 총 유전자의 점수 
			score = (float)current[j].countConflict() / (float)sum;
			if (j == 0)
				proportional[j] = score;
			else
				proportional[j] = proportional[j - 1] + score;
		}
	}
	
	// [Method] proportional selection에서의 현재 wheel을 출력하는 메서드.
	public void printProportional() {
		for (int i = 0; i < POPULATION_SIZE; i++)
			System.out.print(proportional[i] + " ");
		System.out.println();
	}
	
	// [Method] 유전자를 선택하는 메서드. 
	// fixedPoint(0~1 사이의 임의의 수)로 Roulette Wheel에서 해당 범위의 유전자를 선택한다. 
	// Roulette Wheel은 해당 유전자가 선택될 확률을 담고 있다. 
	public int selectChromo() {
		float fixedPoint = (float)(Math.random());
		
		if ( fixedPoint < proportional[0] && fixedPoint >= 0)
			return 0;
		
		for (int i = 1; i < POPULATION_SIZE; i++) {
			if (i != POPULATION_SIZE - 1) {
				if (fixedPoint >= proportional[i] && fixedPoint < proportional[i + 1])
					return i;
			}
			else
				return i;
		}
		return -1;
	}


	// [Method] 선택된 두 염색체를 crossover하는 메서드. Uniform crossover를 채택하였다.
	// 각각의 유전자 위치에 대한 난수를 발생한 후, 임계확률(0.5)를 넘으면 parentA의 같은 위치로부터 유전자를 복사해오고,
	// 그렇지 않으면 parentB의 같은 위치로부터 복사를 한다.
	public void crossover(int n) {
		Chromosome[] tmpCurrent = new Chromosome[POPULATION_SIZE];
		Random random = new Random();
		int parentA = 0;
		int parentB = 0;
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			parentA = this.selectChromo();
			parentB = this.selectChromo();
			
			// 중복 제거
			while (parentB == parentA)
				parentB = this.selectChromo();
			
			// crossover rate을 넘으면 부모를 주고 그렇지 않으면 자식을 준다. 
			if (Math.random() > CROSSOVER_RATE) {
				if (Math.random() < 0.5)
					tmpCurrent[i] = current[parentA];
				else
					tmpCurrent[i] = current[parentB];
			}

			else {
				Chromosome offspring = new Chromosome(n);
				
				int[] equal = new int[n];
				int[] number = new int[n];
				
				// 같은 열을 찾아서 1로 새 배열에 저장한다. 
				for (int j = 0; j < n; j++) {
					if (current[parentA].getState()[j] == current[parentB].getState()[j])
						equal[j] = 1;
					else
						number[current[parentA].getState()[j]] = 1;
				}
				
				// 같은 값은 그대로 유지한다. 
				for (int j = 0; j < n; j++) {
					if (equal[j] == 1)
						offspring.setGene(j, current[parentA].getState()[j]);
				}
				
				// 다른 값은 다시 랜덤으로 생성한다. 
				for (int j = 0; j < n; j++) {
					if (equal[j] == 0) {
						while(true) {
							int k = random.nextInt(n);
							if (number[k] == 1) {
								offspring.setGene(j, k);
								number[k] = 0;
								break;
							}
						}
					}
				}
				
				mutation(offspring, n);
				tmpCurrent[i] = offspring;
				System.out.println("score : " + offspring.countConflict());
				offspring.printState();
			}
			}
		
		for (int i = 0; i < POPULATION_SIZE; i++)
			current[i] = tmpCurrent[i];
		makeWheel();
		generation += 1;
	}
	
	// [Method] 답을 찾는 메서드이다.
	// current 배열을 돌면서 maxScore에 해당하는 값이 있으면 반환한다.
	public Chromosome findSolution(int maxScore) {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			if (current[i].countConflict() == maxScore)
				return current[i];
		}
		return null;
	}
	
	// [Method] 돌연변이를 발생시키는 메서드이다. (MUTATION_RATE = 0.08(%))
	// 임의의 두 column 값을 교환한다.
	public void mutation(Chromosome offspring, int n) {
		Random random = new Random();
		if (Math.random() < MUTATION_RATE) {
			// 임의의 두 column
			int x = random.nextInt(n);
			int y = random.nextInt(n);
			
			// 중복 제거 
			while (x == y)
				y = random.nextInt(n);
			
			// column 교환 
			int tmp = offspring.getState()[x];
			offspring.getState()[x] = offspring.getState()[y];
			offspring.getState()[y] = tmp;
		}
	}



	// [Method] Partially Mapped Crossover를 활용한 crossOver 메서드. 
	// 두 부모 parentA, parentB에 임의로 두 개의 자름선을 정한 후 그 사이부분을 parentA로부터 복사한다.
	// 나머지 위치는 parentB로부터 복사하되, 만일 해당 값이 이미 사용되었다면 같은 값을 가진 A의 위치를 찾아 같은 위치의 B로부터 복사한다.
	// 마지막으로 이미 사용되어 중복이 일어난 유전자는 고쳐준다.
	// @param: parent A, parent B, child A, child B
	// @param: parent B
//	public void partiallyMappedCrossover(int chromA, int chromB, int child1, int child2) {
//        int j = 0;
//        int item1 = 0;
//        int item2 = 0;
//        int pos1 = 0;
//        int pos2 = 0;
//        Chromosome thisChromo = population.get(chromA);
//        Chromosome thatChromo = population.get(chromB);
//        Chromosome newChromo1 = population.get(child1);
//        Chromosome newChromo2 = population.get(child2);
//        int crossPoint1 = getRandomNumber(0, MAX_LENGTH - 1);
//        int crossPoint2 = getExclusiveRandomNumber(MAX_LENGTH - 1, crossPoint1);
//        
//        //gets the crosspoint from where to swap
//        if(crossPoint2 < crossPoint1) {
//            j = crossPoint1;
//            crossPoint1 = crossPoint2;
//            crossPoint2 = j;
//        }
//
//        // Copy Parent genes to offspring.
//        for(int i = 0; i < MAX_LENGTH; i++) {
//            newChromo1.setGene(i, thisChromo.getGene(i));
//            newChromo2.setGene(i, thatChromo.getGene(i));
//        }
//
//        for(int i = crossPoint1; i <= crossPoint2; i++) {
//            // Get the two items to swap.
//            item1 = thisChromo.getGene(i);
//            item2 = thatChromo.getGene(i);
//
//            // Get the items//  positions in the offspring.
//            for(j = 0; j < MAX_LENGTH; j++) {
//                if(newChromo1.getGene(j) == item1) {
//                    pos1 = j;
//                } else if (newChromo1.getGene(j) == item2) {
//                    pos2 = j;
//                }
//            } // j
//
//            // Swap them.
//            if(item1 != item2) {
//                newChromo1.setGene(pos1, item2);
//                newChromo1.setGene(pos2, item1);
//            }
//
//            // Get the items//  positions in the offspring.
//            for(j = 0; j < MAX_LENGTH; j++) {
//                if(newChromo2.getGene(j) == item2) {
//                    pos1 = j;
//                } else if(newChromo2.getGene(j) == item1) {
//                    pos2 = j;
//                }
//            } // j
//
//            // Swap them.
//            if(item1 != item2) {
//                newChromo2.setGene(pos1, item1);
//                newChromo2.setGene(pos2, item2);
//            }
//
//        } // i
//	}
}