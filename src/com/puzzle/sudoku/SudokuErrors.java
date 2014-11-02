package com.puzzle.sudoku;

public enum SudokuErrors {
 inv("invalid input"),
 erFileType("ERROR: Only .csv/.txt files are allowed as inputs"),
 NoSolution("No Solution Found"),
 IOERROR("ERROR: An I/O error occured, please try again");
private final String value;
private SudokuErrors(final String value){
	this.value=value;
}

public String getErrorDescription(){
	return value;
}
}
