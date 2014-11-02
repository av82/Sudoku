package com.puzzle.sudoku;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

@SuppressWarnings("serial")
public class SudokuLayout extends JComponent implements ActionListener{
	private JTextField[][] cells; 
	private JLabel lblError=new JLabel();
	private JButton btnFiledialog=new JButton("Load Input File");
	private JButton btnSolve=new JButton("Find Solution");
	private JButton btnClear=new JButton("Clear");
	private final int  rows=9;
	private final int  cols=9;
	private JFileChooser chooser= new JFileChooser();
	private int[][] inputMatrix;
	private Set<Cell> emptyCells;
	
	public SudokuLayout(){
		setLayout();
		emptyCells=new HashSet<Cell>();
	}
	/**
	 * Sets the initial layout placing the sudoku grid, other operational buttons - file browser, solve command button
	 */
	private void setLayout(){
		this.cells= new JTextField[rows][cols];
		JFrame jframe =new JFrame("Sudoku Puzzle");
		JPanel sudokuPanel= new JPanel();
		JPanel buttonPanel = new JPanel(new BorderLayout());
		sudokuPanel.setLayout(new GridLayout(rows, cols));
		sudokuPanel.setPreferredSize(new Dimension(200,200));
		for(int i=0;i<cells.length;i++){
			for(int j=0;j<cells[i].length;j++){
				cells[i][j]=new JTextField();
				cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				sudokuPanel.add(cells[i][j]);
			}
		}
		btnSolve.addActionListener(this);
		btnSolve.setPreferredSize(new Dimension(50,50));
		btnFiledialog.addActionListener(this);
		btnFiledialog.setPreferredSize(new Dimension(50,50));
		btnClear.addActionListener(this);
		lblError.setVisible(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(sudokuPanel,"North");	
		buttonPanel.add(lblError,"North");
		buttonPanel.add(btnSolve,"Center");
		btnSolve.setVisible(false);
		btnClear.setVisible(false);
		buttonPanel.add(btnClear,"East");
		buttonPanel.add(btnFiledialog,"South");
		buttonPanel.setMaximumSize(new Dimension(200, 200));
		jframe.add(buttonPanel,"South");
		jframe.setSize(520,600);
	    jframe.setMinimumSize(new Dimension(520,600));
		jframe.pack();
		jframe.setVisible(true);
	}
	/*
	 * clear the sudoku matrix on jframe
	 */
	public void clearBoard(){
		this.lblError.setText("");
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				this.cells[i][j].setText("");
				this.cells[i][j].setBackground(Color.white);
			}
		}
		this.btnSolve.setVisible(false);
	}
	/*
	 * file handler helper function to browse a file and pass to SudokuManager
	 * only accepts .csv, .txt files throws errors for other file types
	 */
	public void handleFile(){
		int choice = chooser.showOpenDialog(this);
		if (choice != JFileChooser.APPROVE_OPTION) return;
		clearBoard();
		File chosenFile = chooser.getSelectedFile();
		SudokuManager smgr= new SudokuManager();
		try {
			inputMatrix = smgr.readSudokuFile(chosenFile);
			if(inputMatrix==null){
				this.lblError.setText(SudokuErrors.inv.getErrorDescription());
			}
			else {
				showGame(inputMatrix,false);
				btnSolve.setVisible(true);
			}
		} catch (Exception e1) {
			this.lblError.setText(e1.getMessage());
			this.lblError.setVisible(true);
			
		}
	}
	/*
	 * fill the cells on jframe (jlabel grid with values from inputMatrix)
	 */
	public void showGame(int[][] inputMatrix,boolean solved){
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				this.cells[i][j].setText(String.valueOf(inputMatrix[i][j]));
				if(inputMatrix[i][j]==0 && !solved){
					this.cells[i][j].setText("");
					this.cells[i][j].setBackground(Color.GRAY);
					emptyCells.add(new Cell(i,j));
				}
				else if(solved){
					if(emptyCells.contains(new Cell(i,j))){
						this.cells[i][j].setBackground(Color.green);
					}
				}
				this.cells[i][j].setEditable(false);
				
			}
		}
	}
	public void writeSolution(int[][] outputMatrix){

	      File file = new File("solution.csv");
	      int fno=0;
	      while(file.exists()){
	    	  file=new File("solution"+fno+".csv");
	    	  fno++;
	      }
	      try{
	    	  file.createNewFile();
	    	  FileWriter writer = new FileWriter(file); 
	    	  for(int i=0;i<rows;i++){
	    		  for (int j=0;j<cols;j++){
	    			  writer.write(String.valueOf(outputMatrix[i][j])); 
	    			  if(j!=cols-1) writer.write(",");
	    		  }
	    		writer.write("\n");
	    	  }
		      writer.flush();
		      writer.close();
		      this.lblError.setText(String.format("solution written to \n%s",file.getAbsolutePath()));
	      }
	      catch(IOException e){
	    	  this.lblError.setText(SudokuErrors.IOERROR.getErrorDescription());
	     }
	      this.lblError.setVisible(true);
	}
	public void markSolvedCells(){
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnSolve){
			SudokuManager smgr= new SudokuManager();
			if(smgr.getSolution(inputMatrix)){
				showGame(smgr.getOutputMatrix(),true);
				markSolvedCells();
				writeSolution(smgr.getOutputMatrix());
			}
			else this.lblError.setText(SudokuErrors.NoSolution.getErrorDescription());
		}
		else if(e.getSource()==btnFiledialog){
			handleFile();
			btnClear.setVisible(true);
		}
		else if(e.getSource()==btnClear){
			btnSolve.setVisible(false);
			lblError.setText("");
			lblError.setVisible(false);
			clearBoard();
			btnClear.setVisible(false);
		}
	}

	public static void main(String[] args)
	{
		new SudokuLayout();
	}
}
