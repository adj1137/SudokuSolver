package edu.utexas.ee381v.sudoku;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

import org.junit.Test;

import gurobi.GRBException;

public class SudokuSolverTest {
	
	@Test
	public void testP1() throws GRBException, IOException {
		
		List<String> list = Arrays.asList("easy_1", "easy_2", "easy_3", "easy_4", "easy_5", "easy_6", "easy_7",
							"medium_1", "medium_2", "medium_3", "medium_4", "medium_5", "medium_6", "medium_7",
							"hard_1", "hard_2", "hard_3", "hard_4", "hard_5", "hard_6", "hard_7",
							"expert_1", "expert_2", "expert_3", "expert_4", "expert_5", "expert_6", "expert_7");
		
		Map<String,List<Long>> runtime = new HashMap<>();
		
		list.forEach(run ->{
			try {
				for(int i = 0; i < 10; i++) {
					SudokuSolver solver = new SudokuSolver(run);
					solver.solve();
					if(runtime.containsKey(run)) {
						runtime.get(run).add(solver.getLastSolvedTime());
					} else {
						List<Long> l = new ArrayList<>();
						l.add(solver.getLastSolvedTime());
						runtime.put(run, l);
					}
				}
			} catch (FileNotFoundException | GRBException e) {
				e.printStackTrace();
			}
		});
		
		System.out.format("%12s%16s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s\n", "Run ", "Average ", "Min ", "Max ", "Run 1 ", "Run 2 ", "Run 3 ", "Run 4 ", "Run 5 ", "Run 6 ", "Run 7 ", "Run 8 ", "Run 9 ", "Run 10 ");
		runtime.forEach((key, value)->{
			LongSummaryStatistics stats = value.stream().mapToLong((x) -> x).summaryStatistics();
			System.out.format("%12s%16f%10d%10d%10d%10d%10d%10d%10d%10d%10d%10d%10d%10d\n", key, stats.getAverage(), stats.getMin(), stats.getMax(), value.get(0), value.get(1), value.get(2), value.get(3), value.get(4), value.get(5), value.get(6), value.get(7), value.get(8), value.get(9));
		});
	}

}
