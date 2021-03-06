package com.puzzle.sudoku;

public enum SudokuErrors {
 inv("invalid input"),
 erFileType("ERROR: Only .csv/.txt files are allowed as inputs"),
 NoSolution("No Solution Found"),
 IOERROR("ERROR: An I/O error occured, please try again"),
 InputRangeError("Numbers in sudoku should be between 1-9 for filled cells\n and 0 for un-filled "),
 NaN("Not a number"),
 fnf("ERROR: File not found"),
 nsudoku("Not a sudoku,\n numbers repeated in rows/columns\n or in square blocks");
private final String value;
private SudokuErrors(final String value){
	this.value=value;
}

public String getErrorDescription(){
	return value;
}
}
