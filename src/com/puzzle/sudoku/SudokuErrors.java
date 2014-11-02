package com.puzzle.sudoku;

public enum SudokuErrors {
 inv("invalid input"),
 erFileType("ERROR: Only .csv/.txt files are allowed as inputs"),
 NoSolution("No Solution Found");
private final String value;
private SudokuErrors(final String value){
	this.value=value;
}

public String getCodeValue(){
	return value;
}
}
