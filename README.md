# SWExpertAcademy_MockTest_Java_5653

## SW Expert Academy  5653. [모의 SW 역량테스트] 줄기세포배양

### 1. 문제설명

출처: https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AWXRJ8EKe48DFAUo

input으로 세개의 정수 `N`, `M`, `K`가 들어온다. 각 정수들이 뜻하는 것은 제약사항에 있다. `N * M`개의 숫자가 들어오면 `0`이 아닌 값은 비활성화 상태의 줄기세포의 생명력을 뜻한다. 각 줄기세포는 생명력을 가지며, 생명력 수치가 `X`일 경우 `X`시간이 지나면 활성상태가 된다. 활성세포가 되면 `X`시간 후 세포는 죽게된다. 또한, 활성화된 줄기 세포는 첫 `1`시간 동안 상, 하, 좌, 우 네 방향으로 동시에 번식을 하며 처음 번식된 줄기세포는 비활성 상태이다. 만약 같은 공간에 여러 세포가 번식한다면 가장 생명력높은 세포의 생명력을 가지며 번식한다. 배양 용기의 크기가 무한하다고 할때 `K`시칸 배양시킨 후 배양 용기에 살아있는 줄기세포의 개수를 출력하는 문제.

[제약 사항]

> 초기 상태에서 줄기 세포가 분포된 영역의 넓이는 세로 크기 `N`, 가로 크기 `M`이며 `N`, `M`은 각각 `1` 이상 `50` 이하의 정수이다. `(1≤N≤50, 1≤M≤50)`
> 배양 시간은 `K`시간으로 주어지며 `K`는 `1` 이상 `300` 이하의 정수이다. `(1≤K≤300)`
> 배양 용기의 크기는 무한하다. 따라서 줄기 세포가 배양 용기 가장자리에 닿아서 번식할 수 없는 경우는 없다.
> 줄기 세포의 생명력 `X`는 `1` 이상 `10` 이하의 정수이다. `(1≤X≤10)`
 

[입력]
 
> 입력의 가장 첫 줄에는 총 테스트 케이스의 개수 T가 주어진다.
> 그 다음 줄부터는 각 테스트 케이스가 주어지며
> 각 테스트 케이스의 첫째 줄에는 초기 상태에서줄기 세포가 분포된 세로 크기 N, 가로 크기 M, 배양 시간 K가 순서대로 주어진다.
> 다음 N 줄에는 각 줄마다 M개의 그리드 상태 정보가 주어진다.
> 1~10까지의 숫자는 해당 그리드 셀에 위치한 줄기 세포의 생명력을 의미하며,
> 0인 경우 줄기 세포가 존재하지 않는 그리드이다.

[출력]

> 테스트 케이스 `T`에 대한 결과는 `#T`을 찍고,
> 배양을 `K`시간 시킨 후 배양 용기에 있는 살아있는 줄기 세포(비활성 상태 + 활성 상태)의 개수를 출력한다. (`T`는 테스트케이스의 번호를 의미하며 `1`부터 시작한다. )



### 2. 풀이

#### 1. 첫 시도 DP - 시간초과

`MAX = 350`으로 하고 `Cell`형으로 된 이차원배열의 map을 선언했다. `350`의 이유로는 `N, M, K`의 범위를 고려하였을 때 나올 수 있는 커질수 있는 map의 최대의 길이이기 때문이다. 각 배열의 요소들을 탐색하여 비활성, 활성, 죽은 상태일때, 세포가 없을 때 일어날 행동들에 대해 처리하였다. 하지만 결과는 시간초과... 처음에 map의 크기가 크고 그에 비해 배양된 세포가 존재하는 위치는 적기 때문에 아무것도 없는 값을 많이 검사하기 때문에 그런점이 비효율 적이였다.

#### 2. BFS

위에서 잠깐 언급한 `Cell`클래스는 생명력, 상태, 상태를 유지할 수 있는 시간을 담아두었다. 하지만 모든 세포가 없는 공간도 검사하는 것이 비효율적이었기 때문에 추가적으로 클래스에 정보를 넣어 이를 해결하도록 하였다. 세포의 위치와 해당 세포를 등록한 시간 `k`를 추가해주었다. `N * M`개의 세포에 대한 정보가 올때 생겨나는 세포들의 `k`값을 `0`으로 하여 우선수위 큐에 담아주었다. 이때 우선순위 큐는 `Comparator`를 사용하여 첫째, 등록된 순서에 오름차순, 둘째, 생명력에 대해 내림차순으로 정렬되게 하였다. 세포가 생성된 시간 순서에 맞게 상태를 갱신해주어야하며, 같은 시간대에 생명력이 높은 세포가 먼저 번식하면 낮은 생명력의 세포들이 그 공간에 번식을 하지 않아도 되기 때문이다. 해당 세포에 대한 로직이 수행되면 시점을 뜻하는 `k`값을 증가시키고 상태를 갱신하여 다시 우선순위 큐에 넣어주며, 죽은세포일경우 넣지않는다.

위의 작업을 수행하다 시간`K`에 만들어진 세포를 처음 검사할 때 검사를 중지하고 `우선순위 큐에 남아있는 세포들의 개수 + 1`을 출력해준다. `+1`은 `K`세포를 poll하였기 때문에 한개를 더 카운트하여 출력하는것이다.


```java

class Solution {
	static final int DEAD = -1;
	static final int NOTHING = 0;
	static final int IDLE = 1;
	static final int ALIVE = 2;

	static class Cell {
		int x, y;
		int power;
		int status;
		int time;
		int k;

		public Cell(int x, int y, int power, int status, int time, int k) {
			this.x = x;
			this.y = y;
			this.power = power;
			this.status = status;
			this.time = time;
			this.k = k;
		}
	}

	static final int MAX = 350;

	static Comparator<Cell> compByKAndPower = new Comparator<Cell>() {
		public int compare(Cell c1, Cell c2) {
			if (c1.k != c2.k)
				return c1.k - c2.k;
			else
				return c2.power - c1.power;
		}
	};

	static int[][] map;
	static int N, M, K;

	public static void main(String args[]) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {

			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken()); // 세로 크기 <= 50
			M = Integer.parseInt(st.nextToken()); // 가로 크기 <= 50
			K = Integer.parseInt(st.nextToken()); // K시간 후 <= 300

			int centerN = (MAX - N) / 2;
			int centerM = (MAX - M) / 2;

			map = new int[MAX][MAX];
			List<Cell> list = new ArrayList<>();

			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < M; j++) {
					int power = Integer.parseInt(st.nextToken());
					if (power != 0) {
						Cell cell = new Cell(i + centerN, j + centerM, power, IDLE, power, 0);
						map[i + centerN][j + centerM] = power;
						list.add(cell);
					}
				}
			}

			System.out.println("#" + test_case + " " + runUntilK(list));
		}
	}

	private static int runUntilK(List<Cell> list) {
		PriorityQueue<Cell> cells = new PriorityQueue<>(compByKAndPower);
		for (Cell c : list) {
			cells.add(c);
		}

		while (!cells.isEmpty()) {
			Cell cell = cells.poll();

			if (cell.k == K)
				break;

			cell.time--;
			if (cell.status == IDLE) {
				cell.k++;
				if (cell.time == 0) {
					cell.status = ALIVE;
					map[cell.x][cell.y] = ALIVE;
					cell.time = cell.power;
				}
				cells.add(cell);

			} else if (cell.status == ALIVE) {

				cell.k++;

				if (cell.time + 1 == cell.power) { // 번식
					if (map[cell.x + 1][cell.y] == NOTHING) {
						map[cell.x + 1][cell.y] = IDLE;
						cells.add(new Cell(cell.x + 1, cell.y, cell.power, IDLE, cell.power, cell.k));
					}
					if (map[cell.x - 1][cell.y] == NOTHING) {
						map[cell.x - 1][cell.y] = IDLE;
						cells.add(new Cell(cell.x - 1, cell.y, cell.power, IDLE, cell.power, cell.k));
					}
					if (map[cell.x][cell.y + 1] == NOTHING) {
						map[cell.x][cell.y + 1] = IDLE;
						cells.add(new Cell(cell.x, cell.y + 1, cell.power, IDLE, cell.power, cell.k));
					}
					if (map[cell.x][cell.y - 1] == NOTHING) {
						map[cell.x][cell.y - 1] = IDLE;
						cells.add(new Cell(cell.x, cell.y - 1, cell.power, IDLE, cell.power, cell.k));
					}
				}

				if (cell.time == 0) {
					cell.status = DEAD;
					map[cell.x][cell.y] = DEAD;
				} else {
					cells.add(cell);
				}

			}
		}
		return cells.size()+1;
	}
}

```
