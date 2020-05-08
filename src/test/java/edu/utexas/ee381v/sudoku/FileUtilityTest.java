package edu.utexas.ee381v.sudoku;

import java.io.FileNotFoundException;

import org.junit.Test;

import edu.utexas.ee381v.sudoku.util.FileUtility;

public class FileUtilityTest {
	
	@Test
	public void testFileLoad() throws FileNotFoundException {
		
		FileUtility.retrieveSudokuFromFile("s1");
		
	}

}
