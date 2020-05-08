package edu.utexas.ee381v.sudoku;

import java.io.FileNotFoundException;

import edu.utexas.ee381v.sudoku.util.FileUtility;
import gurobi.*;


/**
 * This class loads a Sudoku puzzle from a specified file and populates Gurobi variables
 * necessary for solving it.
 * @author Allen James
 */
public class SudokuSolver {
	
	private static final int GRID_SIZE = 9;
	private static final int SUB_GRID_SIZE = 3;
	
	private GRBVar[][][] vars = new GRBVar[GRID_SIZE][GRID_SIZE][GRID_SIZE];
	private GRBEnv env;
	private GRBModel model;
	
	private long lastSolveTime;
	
	public SudokuSolver(String file) throws GRBException, FileNotFoundException {
		env = new GRBEnv("sudoku.log");
		model = new GRBModel(env);
		populateVars();
		setConstraints();
		populateKnownValues(file);
	}
	
	public void solve() throws FileNotFoundException, GRBException {
		//Load the Puzzle from a file
	      long start = System.nanoTime() ;
	      //Run the optimization
	      model.optimize();
	      long stop = System.nanoTime() ;
	      this.lastSolveTime = ( stop - start );
	      System.out.format("Elapsed Time: %,8d%n%n NanoSeconds", this.lastSolveTime);
	      
	      model.write("sudoku.lp");
	}
	
	public void printVars() throws GRBException {
	      double[][][] x = model.get(GRB.DoubleAttr.X, vars);
	      
	      System.out.println();
	      for (int i = 0; i < GRID_SIZE; i++) {
	    	  for (int j = 0; j < GRID_SIZE; j++) {
	    		  for (int k = 0; k < GRID_SIZE; k++) {
	    			  if (x[i][j][k] > 0.5) {
	    			  System.out.print(k+1);
	    		  }
	          }
	      }
	    	  System.out.println();
	      }
	}
	
	private void populateVars() throws GRBException {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				for (int k = 0; k < GRID_SIZE; k++) {
	              vars[i][j][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "G_" + i + "_" + j + "_" + k);
	            }
	       }
	   }
	}
	
	private void setConstraints() throws GRBException {
		
		//All squares must have a value
	    for(int i = 0; i < GRID_SIZE; i++) {
	    	for(int j = 0; j < GRID_SIZE; j++) {
	    		GRBLinExpr e = new GRBLinExpr();
	    		e.addTerms(null, vars[i][j]);
	    		model.addConstr(e, GRB.EQUAL, 1.0, "Value_" + i + "_" + j);
	    	}
	    }
	      
	    //Rows must contain all values 1-9 at least once
	    for(int i = 0; i < GRID_SIZE; i++) {
	    	for(int j = 0; j < GRID_SIZE; j++) {
	    		GRBLinExpr e = new GRBLinExpr();
	   		  	for(int k = 0; k < GRID_SIZE; k++) {
	   		  		e.addTerm(1.0, vars[i][k][j]);
	   		  	}
	   		  	model.addConstr(e, GRB.EQUAL, 1.0, "Row_" + i + "_" + j);
	   	  	}
	    }
	      
	    //Cols must contain all values 1-9 at least once
	    for(int i = 0; i < GRID_SIZE; i++) {
	   	  	for(int j = 0; j < GRID_SIZE; j++) {
	   	  		GRBLinExpr e = new GRBLinExpr();
	   	  		for(int k = 0; k < GRID_SIZE; k++) {
	   	  			e.addTerm(1.0, vars[k][i][j]);
	   	  		}
	   	  		model.addConstr(e, GRB.EQUAL, 1.0, "Col_" + i + "_" + j);
	   	  	}
	    }
	      
	    //4. Each value appears only once per sub-grid
	    for(int i = 0; i < GRID_SIZE; i++) {
	   	  	for(int j = 0; j < SUB_GRID_SIZE; j++) {
	   	  		for(int k = 0; k < SUB_GRID_SIZE; k++) {
	   	  			GRBLinExpr e = new GRBLinExpr();
	   	  			for(int l = 0; l < SUB_GRID_SIZE; l++) {
	   	  				for(int m = 0; m < SUB_GRID_SIZE; m++) {
	   	  					e.addTerm(1.0, vars[j*SUB_GRID_SIZE+l][k*SUB_GRID_SIZE+m][i]);
	   	  				}
	   	  			}
	   	  			model.addConstr(e, GRB.EQUAL, 1.0, "SubGrid_" + i + "_" + k);
	   	  		}
	   	  	}
	    }
	}
	
	private void populateKnownValues(String file) throws GRBException, FileNotFoundException {
		int[][] puzzle = FileUtility.retrieveSudokuFromFile(file);
		//Populate the known cells from the puzzle
	    for(int i = 0; i < GRID_SIZE; i++) {
	    	for(int j = 0; j < GRID_SIZE; j++) {
	    		int value = puzzle[i][j];
	    		if(value >= 0) {
	    			  vars[i][j][value-1].set(GRB.DoubleAttr.LB, 1.0);
	    		  }
	    	  }
	      }
	}

    public long getLastSolvedTime() {
    	return this.lastSolveTime;
    }
}