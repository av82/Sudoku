Sudoku
======

1. The project facilitates a UI to load a Sudoku puzzle from CSV / TXT file in the following format

0,3,5,2,9,0,8,6,4
0,8,2,4,1,0,7,0,3
7,6,4,3,8,0,0,9,0
2,1,8,7,3,9,0,4,0
0,0,0,8,0,4,2,3,0
0,4,3,0,5,2,9,7,0
4,0,6,5,7,1,0,0,9
3,5,9,0,2,8,4,1,7
8,0,0,9,0,0,5,2,6

provides an out to a file in csv format as follows

1,3,5,2,9,7,8,6,4
9,8,2,4,1,6,7,5,3
7,6,4,3,8,5,1,9,2
2,1,8,7,3,9,6,4,5
5,9,7,8,6,4,2,3,1
6,4,3,1,5,2,9,7,8
4,2,6,5,7,1,3,8,9
3,5,9,6,2,8,4,1,7
8,7,1,9,4,3,5,2,6


2. The Sudoku puzzle ui handles file errors
      2.1 A puzzle that has non-digit characters in csv files throw errors. 
      2.2 A puzzle that does not follow the Sudoku rules ( un repeated 1-9 numbers in row and column and square block) throws error.
      2.3 A puzzle that does not follow the above mentioned format of csv throws error

3. The sudoku puzzle ui shows empty cells in loaded puzzle in GRAY color. 
4. The sudoku puzzle ui shows solved cells in GREEN color. 
5. The sudoku puzzle solution is written to a solution.csv file at installed location.

Instructions: 
1. start the application (run application inside eclipse OR a jar file to run standalone using (java -jar sudokuArun.jar))
2. Click button - Load the csv file that contains the sudoku puzzle. 
3. Click button - Find Solution to find a solution and write to a file in current location of (jar/ project)


