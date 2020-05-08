package edu.utexas.ee381v.sudoku.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class FileUtility {
	
	private static final int GRID_SIZE = 9;
	
	public static int[][] retrieveSudokuFromFile(String fileName) throws FileNotFoundException{
			
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			URL resource = classloader.getResource(fileName);
			File file = new File(resource.getFile());
	        Scanner myReader = new Scanner(file);
	        int i = 0;
	        int[][] puzzle = new int[GRID_SIZE][GRID_SIZE];
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	          for(int j = 0; j < data.length(); j++) {
	        	  if(data.charAt(j) == '_') {
	        		  puzzle[i][j] = -1;
	        	  } else {
	        		  puzzle[i][j] = Integer.parseInt(String.valueOf(data.charAt(j)));
	        	  }
	          }
	          i++;
	        }
	        myReader.close();
	        return puzzle;	
	}
}
