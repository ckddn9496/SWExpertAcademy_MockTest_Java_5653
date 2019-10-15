import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

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

class DPSolutionTimeOut {
	static final int DEAD = -1;
	static final int NOTHING = 0;
	static final int IDLE = 1;
	static final int ALIVE = 2;

	static class Cell {
		int power;
		int status;
		int time;

		public Cell(int power, int status, int time) {
			this.power = power;
			this.status = status;
			this.time = time;
		}
	}

	static final int MAX = 350;

	static Cell[][] map = new Cell[MAX][MAX];

	public static void main(String args[]) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {
			init();
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken()); // 세로 크기 <= 50
			int M = Integer.parseInt(st.nextToken()); // 가로 크기 <= 50
			int K = Integer.parseInt(st.nextToken()); // K시간 후 <= 300

			int centerN = (MAX - N) / 2;
			int centerM = (MAX - M) / 2;

			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < M; j++) {
					int power = Integer.parseInt(st.nextToken());
					if (power != 0)
						map[i + centerN][j + centerM] = new Cell(power, IDLE, power);
				}
			}

			for (int i = 0; i < K; i++) {
				oneDayPassed();
			}

			System.out.println("#" + test_case + " " + countNotDead());
		}
	}

	private static void oneDayPassed() {
		for (int i = 1; i < MAX - 1; i++) {
			for (int j = 1; j < MAX - 1; j++) {
				if (map[i][j].status == NOTHING) {
					int max = 0;
					if (map[i - 1][j].status == ALIVE && map[i - 1][j].power == map[i - 1][j].time) {
						max = Math.max(max, map[i - 1][j].power);
					}
					if (map[i + 1][j].status == ALIVE && map[i + 1][j].power == map[i + 1][j].time) {
						max = Math.max(max, map[i + 1][j].power);
					}
					if (map[i][j - 1].status == ALIVE && map[i][j - 1].power == map[i][j - 1].time) {
						max = Math.max(max, map[i][j - 1].power);
					}
					if (map[i][j + 1].status == ALIVE && map[i][j + 1].power == map[i][j + 1].time) {
						max = Math.max(max, map[i][j + 1].power);
					}

					if (max != 0) {
						map[i][j] = new Cell(max, IDLE, max + 1);
					}
				}
			}
		}

		for (int i = 1; i < MAX - 1; i++) {
			for (int j = 1; j < MAX - 1; j++) {
				if (map[i][j].status == IDLE) {
					map[i][j].time--;
					if (map[i][j].time == 0) {
						map[i][j].status = ALIVE;
						map[i][j].time = map[i][j].power;
					}
				} else if (map[i][j].status == ALIVE) {
					map[i][j].time--;
					if (map[i][j].time == 0) {
						map[i][j].status = DEAD;
					}
				}

			}
		}
	}

	private static void init() {
		for (int i = 0; i < MAX; i++) {
			for (int j = 0; j < MAX; j++) {
				map[i][j] = new Cell(0, NOTHING, 0);
			}
		}
	}

	private static int countNotDead() {
		int count = 0;
		for (int i = 0; i < MAX; i++) {
			for (int j = 0; j < MAX; j++) {
				if (map[i][j].status > IDLE)
					count++;
			}
		}
		return count;
	}
}