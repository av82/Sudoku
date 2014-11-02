package com.puzzle.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class SudokuManager {
	private static final int rowSize=9;
	private static final int colSize=9;
	private static final int squareSize=3;
	private int[][] outputMatrix=null;
	
	public SudokuManager(){
		outputMatrix=new int[rowSize][colSize];
	}
	
	/*
	 * initial entry function to obtain solution for a sudoku puzzle post load
	 * if a solution is found returns true
	 */
	public boolean getSolution(int[][] inputMatrix){
		copytheMatrix(inputMatrix);
		if(solve()){
				return true;
		}
		return false;
	}
    /*
     * backtracking solve function tries to fill each empty cell in valid fashion
     */
	public boolean solve() {
		Cell curCell = getNextEmptyCell();
		if (curCell == null)
			return true;
		int i = curCell.row, j = curCell.column;
		for (int val = 1; val < 10; val++) {
			//System.out.println(String.format("trying cell %d,%d for value %d",i,j,val)); //for logging check
			if (isValid(i, j, val,outputMatrix)) {
				outputMatrix[i][j] = val;
				if (solve())
					return true;
				outputMatrix[i][j] = 0;
			}
		}
		return false;
	}
	/*
	 * finds next empty cell available in solution matrix
	 * if no empty cells are found returns null
	 */
	public Cell getNextEmptyCell(){
		for(int row=0;row<rowSize;row++){
			for(int col=0;col<colSize;col++){
				if(outputMatrix[row][col]==0){
					return new Cell(row,col);
				}
			}
		}
		return null;
	}
	/*
	 * helper function to load inputMatrix to outputMatrix
	 */
	public void copytheMatrix(int[][] inputMatrix){
		for(int i=0;i<rowSize;i++){
			for(int j=0;j<colSize;j++){
				this.outputMatrix[i][j]=inputMatrix[i][j];
			}
		}
	}
	/**
	 * 
	 * @param row -current row that is being checked 
	 * @param val - current value that is being tried for @row
	 * @return true when @row does not contain @val  else false
	 */
	 boolean isvalidRow(int row,int val,int[][] checkMatrix){
		 for (int i=0;i<colSize;i++){
			 if (checkMatrix[row][i]==val)
				 return false;
		 }
		 return true;
	 }
	 /**
	  * 
	  * @param col-current column that is being checked
	  * @param val - current value that is being tried for @col
	  * @return true when @col does not contain @val else false
	  */
	 boolean isvalidColumn(int col,int val,int[][] checkMatrix){
		 for (int i=0;i<rowSize;i++){
			 if (checkMatrix[i][col]==val)
				 return false;
		 }
		 return true;
	 }
	 /**
	  * 
	  * @param rowstart -starting row number for current square being checked
	  * @param colstart -starting column number for current square being checked
	  * @param val - value being checked
	  * @return true when current SQUARE does not contain @val else false
	  */
	 boolean isvalidSquareBlock(int rowstart, int colstart,int val,int[][] checkMatrix){
		 for (int r = 0; r < squareSize; r++)
		        for (int c = 0; c < squareSize; c++)
		            if (checkMatrix[r+rowstart][c+colstart] == val)
		                return false;
		    return true;
	 }
	 
	 /**
	  * 
	  * @param row row number that is being checked
	  * @param col column number that is being checked
	  * @param val value that is being tried to be filled at outputMatrix[@row][@col]
	  * @return returns true when isvalidRow, isValidColumn, isvalidSquareBlock, all these helpers return true
	  */
	 boolean isValid(int row, int col, int val,int[][] checkMatrix) {
		if(isvalidRow(row, val,checkMatrix)&&isvalidColumn(col, val,checkMatrix)&&isvalidSquareBlock(row-row%squareSize, col-col%squareSize, val,checkMatrix)){
			return true;
		}
		else return false;
	  }
	
	 /**
	  * 
	  * @param f file to be read
	  * @return a 2D matrix with input sudoku puzzle
	  * @throws Exception
	  */
	public int[][] readSudokuFile(File f) throws Exception{
		BufferedReader br =null;
		String splitter=",";
		String line="";
		int[][] returnInputMatrix=null;
		if (!(f.getName().endsWith(".csv")||f.getName().endsWith(".txt"))){
			throw new Exception(SudokuErrors.erFileType.getErrorDescription());
		}
		try{
			br=new BufferedReader(new FileReader(f));
			returnInputMatrix=new int[rowSize][colSize];
			int row=0;
			while((line=br.readLine())!=null && row<rowSize){
				String[] columns=line.split(splitter);
				
				for(int i=0;i<colSize;i++){
					if(isInteger(columns[i])){
						int num=Integer.parseInt(columns[i]);
						if(num>=0&&num<10){
							   if(num==0) returnInputMatrix[row][i]=num;
							   else{
								   if(isValid(row,i,num,returnInputMatrix)){
									   returnInputMatrix[row][i]=num;
								   }
								   else throw new Exception(SudokuErrors.nsudoku.getErrorDescription());
							   }
							     	  
						}
						else{
							throw new Exception(String.format(SudokuErrors.InputRangeError.getErrorDescription() +" at row %d, column %d", row,i));
						}
					}
					else throw new Exception(String.format(SudokuErrors.NaN.getErrorDescription()+ " at row %d, column %d", row,i));
				}
				row++;
			}
			
		}
		catch(FileNotFoundException e){
			throw new Exception(SudokuErrors.fnf.getErrorDescription());
		}
		catch(IOException e){
			throw new Exception(SudokuErrors.IOERROR.getErrorDescription());
		}
		finally{
			if(br!=null){
				try{
					br.close();
				}
				catch(IOException e){
					throw new Exception(SudokuErrors.IOERROR.getErrorDescription());
				}
			}
		}
		return returnInputMatrix;
		
	}
	public  boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	      return true;
	}
	public int[][] getOutputMatrix() {
		return outputMatrix;
	}

	public void setOutputMatrix(int[][] outputMatrix) {
		this.outputMatrix = outputMatrix;
	}

}
