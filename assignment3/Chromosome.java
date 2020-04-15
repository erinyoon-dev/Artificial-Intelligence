public class Chromosome {
	private int[] gene;
	private int index;
	
	public Chromosome(int n ) {
		gene = new int[n];
		index = n;
	}

	// Index, gene를 가져오는 메서드
	public int getIndex() {
		return index;
	}	
	
	public int[] getState() {
		return gene;
	}

	// [Method] 현재 유전자의 Queen 배열을 설정하는 메서드.
	public void setGene(int value, int position) {
		if (index == 0 || position >= index)
			return;
		this.gene[position] = value;
	}
	
	// [Method] current 배열 순서를 임의로 섞는 메서드. 
	public void randomize() {
		for (int i = 0; i < index; i++) {
			int x = (int)(Math.random() * index);
			int y = (int)(Math.random() * index);
			int tmp = gene[x];
			gene[x] = gene[y];
			gene[y] = tmp;
		}
	}
	
	// [Method] 현재 배열을 겹치지 않게 정의한 후, 임의로 섞는 메서드. 
	public void init() {
		for (int i = 0; i < index; i++)
			gene[i] = i;
		
		randomize();
	}
	
	// [Method] 현재 유전자의 Queen 배열을 출력하는 메서드. 
	public void printState() {
		int i;
		if (gene.length == 0) {
			return;
		}
		for (i = 0; i < index; i++)
			System.out.print(gene[i] + " ");
		System.out.println();
	}

	// [Method] 현재 상태의 conflict를 계산하는 메서드.
	// count = 공격 가능한 column의 수.
	// Conflict가 발생하면 count가 올라가고, good gene/bad gene가 선택될 확률의 차이가 증가한다.
	public int countConflict() {
		int[] arr = new int[index];
		int count = 0;
		
		if(gene.length == 0)
			return -1;
		
		for (int i = 0; i < index; i++) {
			for (int j = i + 1; j < index; j++) {
				//  가로/세로 충돌 여부 비교 
			    if (gene[i] == gene[j])
			    	arr[i] = 1;
			    //  대각선 충돌 여부 비교 
			    else if (Math.abs(j - 1) == Math.abs(gene[j] - gene[i]))
			    	arr[i] = 1;
			}
		}
		for (int i = 0; i < index; i++) {
			if (arr[i] == 1)
				count++;
		}
		
		return index * index - count;
	}
}