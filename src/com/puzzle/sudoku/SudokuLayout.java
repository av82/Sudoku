package com.puzzle.sudoku;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
	private final int  rows=9;
	private final int  cols=9;
	private JFileChooser chooser= new JFileChooser();
	private int[][] inputMatrix;
	
	
	public SudokuLayout(){
		setLayout();
	}
	/**
	 * Sets the initial layout placing the sudoku grid, other operational buttons - file browser, solve command button
	 */
	private void setLayout(){
		this.cells= new JTextField[rows][cols];
		JFrame jframe =new JFrame("Sudoku Puzzle");
		GridLayout framelayout= new GridLayout(3,3);
		jframe.setLayout(framelayout);
		jframe.setPreferredSize(new Dimension(900,900));
		JPanel cellPanel= new JPanel();
		cellPanel.setLayout(new GridLayout(rows, cols));
		for(int i=0;i<cells.length;i++){
			for(int j=0;j<cells[i].length;j++){
				cells[i][j]=new JTextField();
				cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				cellPanel.add(cells[i][j]);
			}
		}
		btnSolve.addActionListener(this);
		btnSolve.setPreferredSize(new Dimension(50,50));
		btnFiledialog.addActionListener(this);
		btnFiledialog.setPreferredSize(new Dimension(50,50));
		lblError.setVisible(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(new JLabel());
		jframe.add(cellPanel);	
		jframe.add(new JLabel());
		jframe.add(lblError);
		jframe.add(btnSolve);
		btnSolve.setVisible(false);
		jframe.add(btnFiledialog);
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
				showGame(inputMatrix);
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
	public void showGame(int[][] inputMatrix){
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				this.cells[i][j].setText(String.valueOf(inputMatrix[i][j]));
				if(inputMatrix[i][j]==0){
					this.cells[i][j].setText("");
					this.cells[i][j].setBackground(Color.GRAY);
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
		      this.lblError.setText(String.format("solution written to %s",file.getAbsolutePath()));
	      }
	      catch(IOException e){
	    	  this.lblError.setText(SudokuErrors.IOERROR.getErrorDescription());
	     }
	      this.lblError.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==btnSolve){
			SudokuManager smgr= new SudokuManager();
			if(smgr.getSolution(inputMatrix)){
				showGame(smgr.getOutputMatrix());
				writeSolution(smgr.getOutputMatrix());
			}
			else this.lblError.setText(SudokuErrors.NoSolution.getErrorDescription());
		}
		else if(e.getSource()==btnFiledialog){
			handleFile();
		}
	}

	public static void main(String[] args)
	{
		new SudokuLayout();
	}
}
