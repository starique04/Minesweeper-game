package finalproject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.util.concurrent.ThreadLocalRandom;

public class MineSweapPart extends JFrame
{
  private static final long serialVersionUID = 1L;
  private static final int WINDOW_HEIGHT = 760;
  private static final int WINDOW_WIDTH = 760;
  private static final int TOTAL_MINES = 16;
  
  
  private static int guessedMinesLeft = TOTAL_MINES;
  private static int actualMinesLeft = TOTAL_MINES;

  private static final String INITIAL_CELL_TEXT = "";
  private static final String UNEXPOSED_FLAGGED_CELL_TEXT = "@";
    private static final String EXPOSED_MINE_TEXT = "M";
  
  // visual indication of an exposed MyJButton
  private static final Color EXPOSED_CELL_BACKGROUND_COLOR = Color.lightGray;
  // colors used when displaying the getStateStr() String
  private static final Color EXPOSED_CELL_FOREGROUND_COLOR_MAP[] = {Color.lightGray, Color.blue, Color.green, Color.cyan, Color.yellow, 
                                           Color.orange, Color.pink, Color.magenta, Color.red, Color.red};

  
  // holds the "number of mines in perimeter" value for each MyJButton 
  private static final int MINEGRID_ROWS = 16;
  private static final int MINEGRID_COLS = 16;
  private int[][] mineGrid = new int[MINEGRID_ROWS][MINEGRID_COLS];

  private static final int NO_MINES_IN_PERIMETER_MINEGRID_VALUE = 0;
  private static final int ALL_MINES_IN_PERIMETER_MINEGRID_VALUE = 8;
  private static final int IS_A_MINE_IN_MINEGRID_VALUE = 9;
  
  private boolean running = true;
  
  public MineSweapPart()
  {
    this.setTitle("MineSweap                                                         " + 
                  MineSweapPart.guessedMinesLeft +" Mines left");
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setResizable(false);
    this.setLayout(new GridLayout(MINEGRID_ROWS, MINEGRID_COLS, 0, 0));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    // set the grid of MyJbuttons
    this.createContents();
    
    // place MINES number of mines in sGrid and adjust all of the "mines in perimeter" values
    this.setMines();
    
    this.setVisible(true);
  }

  public void createContents()
  {
    for (int mgr = 0; mgr < MINEGRID_ROWS; ++mgr)
    {  
      for (int mgc = 0; mgc < MINEGRID_COLS; ++mgc)
      {  
        // set sGrid[mgr][mgc] entry to 0 - no mines in it's perimeter
        this.mineGrid[mgr][mgc] = NO_MINES_IN_PERIMETER_MINEGRID_VALUE; 
        
        // create a MyJButton that will be at location (mgr, mgc) in the GridLayout
        MyJButton but = new MyJButton(INITIAL_CELL_TEXT, mgr, mgc); 
        
        // register the event handler with this MyJbutton
        but.addActionListener(new MyListener());
        
        // add the MyJButton to the GridLayout collection
        this.add(but);
      }  
    }
  }


  // begin nested private class
  private class MyListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if ( running )
      {
        // used to determine if ctrl or alt key was pressed at the time of mouse action
        int mod = event.getModifiers();
        MyJButton mjb = (MyJButton)event.getSource();
        
        // is the MyJbutton that the mouse action occurred in flagged
        boolean flagged = mjb.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT);
        
        // is the MyJbutton that the mouse action occurred in already exposed
        boolean exposed = mjb.getBackground().equals(EXPOSED_CELL_BACKGROUND_COLOR);
       
        // flag a cell : ctrl + left click
        if ( !flagged && !exposed && (mod & ActionEvent.CTRL_MASK) != 0 )
        {
            
          mjb.setText(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT);
          --MineSweapPart.guessedMinesLeft;
          
          // if the MyJbutton that the mouse action occurred in is a mine
          if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
          {
            // what else do you need to adjust?
            // could the game be over?
        	//Adjust the actual mines left

            MineSweapPart.actualMinesLeft --;


           

            //If there are no actual mine left

              //are left, then show the winning

              //message in the dialogue box.

            if(actualMinesLeft == 0) {

                JOptionPane.showMessageDialog(null, "You WIN!");

           

            //And, exit from the program.

            System.exit(0);
            }
          }
          setTitle("MineSweap                                                         " + 
                   MineSweapPart.guessedMinesLeft +" Mines left");
        }
       
        // unflag a cell : alt + left click
        else if ( flagged && !exposed && (mod & ActionEvent.ALT_MASK) != 0 )
        {
          mjb.setText(INITIAL_CELL_TEXT);
          ++MineSweapPart.guessedMinesLeft;
          
          // if the MyJbutton that the mouse action occurred in is a mine
          if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
          {
            // what else do you need to adjust?
            // could the game be over?
        	//Increment the actual mine left.

            ++MineSweapPart.actualMinesLeft;
          }
          setTitle("MineSweap                                                         " + 
                    MineSweapPart.guessedMinesLeft +" Mines left");
        }
     
        // expose a cell : left click
        else if ( !flagged && !exposed )
        {
          exposeCell(mjb);
        }  
      }
    }
    
    public void exposeCell(MyJButton mjb)
    {
      if ( !running )
        return;
      
      // expose this MyJButton 
      mjb.setBackground(EXPOSED_CELL_BACKGROUND_COLOR);
      mjb.setForeground(EXPOSED_CELL_FOREGROUND_COLOR_MAP[mineGrid[mjb.ROW][mjb.COL]]);
      mjb.setText(getGridValueStr(mjb.ROW, mjb.COL));
      
      // if the MyJButton that was just exposed is a mine
      if ( mineGrid[mjb.ROW][mjb.COL] == IS_A_MINE_IN_MINEGRID_VALUE )
      {  
        // what else do you need to adjust?
        // could the game be over?
    	//Show the losing message in the dialogue

          //box and exit from the program.

           JOptionPane.showMessageDialog(null,"Mine Exploded, You lost!");

           System.exit(0);
       
      }
      
      // if the MyJButton that was just exposed has no mines in its perimeter
      if ( mineGrid[mjb.ROW][mjb.COL] == NO_MINES_IN_PERIMETER_MINEGRID_VALUE )
      {
        // lots of work here - must expose all MyJButtons in its perimeter
        // and so on
        // and so on
        // .
        // .
        // .
    	// Hint: MyJButton mjbn = (MyJButton)mjb.getParent().getComponent(indn);
    	// where indn is a linearized version of a row, col index pair 
    	  for (int x = -1; x < 2; x++) {
    		  for (int y = -1; y < 2; y++) {
    			  if (!(x == 0 && y == 0) &&
                          (mjb.ROW + x >= 0) && (mjb.ROW + x < MineSweapPart.MINEGRID_ROWS) &&
                          (mjb.COL + y >= 0) && (mjb.COL + y < MineSweapPart.MINEGRID_COLS)) {
    				  int index = ((mjb.ROW + x) * MineSweapPart.MINEGRID_ROWS) + (mjb.COL + y);
    				  MyJButton newButton = (MyJButton) (mjb.getParent().getComponent(index));
    				  if (!newButton.getText().equals(MineSweapPart.UNEXPOSED_FLAGGED_CELL_TEXT) &&
                              !newButton.getBackground().equals(EXPOSED_CELL_BACKGROUND_COLOR)) {
    					  exposeCell(newButton);
    				  }
    			  }
    		  }
    	  }
      }
    }
  }
  // end nested private class


  public static void main(String[] args)
  {
    new MineSweapPart();
  }

  
  //************************************************************************************************

  // place MINES number of mines in sGrid and adjust all of the "mines in perimeter" values
  private void setMines()
  {
    // your code here ...
	  for(int i=0; i<TOTAL_MINES; i++)

      {

      //Generate a random row and random column using

        //ThreadLocalRandom class.

          int random_Row = ThreadLocalRandom.current().nextInt(0, MINEGRID_ROWS);

          int random_Column = ThreadLocalRandom.current().nextInt(0, MINEGRID_COLS);

         

          //Check if the current cell is not a mine.

          if(!(this.mineGrid[random_Row][random_Column] == 9))

          {

             //Make the current cell a mine.

              this.mineGrid[random_Row][random_Column] = 9;

             

              //Check if the row above the mine is less

             //than the grid's length.

              if(random_Row + 1 < this.mineGrid.length)

              {

                  //Check if the next cell is not a mine.

                  if(this.mineGrid[random_Row+1][random_Column] != 9)

                  {

                       //Increment the cell.

                      this.mineGrid[random_Row+1]

                        [random_Column]++;

                  }

                 

                  //If the next column is less than the

                   //grid length and next cell in the next

                   //row is not a mine.

                  if(random_Column + 1 <

                  this.mineGrid[random_Row+1].length &&

                  this.mineGrid[random_Row+1][random_Column+1] != 9)

                  {

                       //Increment the current cell

                        //mine's value.

                      this.mineGrid[random_Row+1]

                        [random_Column+1]++;

                  }

                 

                //If the previous column is greater than

                   //or equal to 0 and previous cell in

                   //the next row is not a mine.

                  if(random_Column- 1 >= 0 && this.mineGrid[random_Row+1][random_Column-1] != 9)

                  {

                       //Increment the current cell's

                        //mine.

                      this.mineGrid[random_Row+1][random_Column- 1]++;

                  }

              }

             

              //If the previous row is greater than or

             //equal to 0.

              if(random_Row - 1 >= 0 )

              {

                  //Check if the current cell is not a

                   //mine.

                  if(this.mineGrid[random_Row-1][random_Column] != 9)

                  {

                      this.mineGrid[random_Row- 1][random_Column]++;

                  }

                 

                  //If the next row is less than the

                   //previous row' cell length and the

                   //current cell is not mine.

                  if(random_Column + 1 < this.mineGrid[random_Row-1].length && this.mineGrid[random_Row-1][random_Column+1] !=9)

                  {

                     this.mineGrid[random_Row- 1][random_Column+1]++;

                  }

                 

                  //If the previous column is greater

                   //than or equal to 0 and current cell

                   //is not mine.

                  if(random_Column- 1 >= 0 && this.mineGrid[random_Row-1][random_Column-1] != 9)

                  {

                      this.mineGrid[random_Row-1][random_Column- 1]++;

                  }

              }

             

              //If the next column is less than the

             //current row's cell length and the current

             //cell is not mine.

             if(random_Column + 1 < this.mineGrid[random_Row].length && this.mineGrid[random_Row][random_Column+1] != 9)

              {

                   this.mineGrid[random_Row][random_Column+1]++;

              }

             

              //If the previus column is greater tahn or

             //equal to 0 and current cell is not a mine.

              if(random_Column- 1 >= 0 && this.mineGrid[random_Row][random_Column-1] != 9 )

              {

                   this.mineGrid[random_Row][random_Column-1]++;

              }

          }

         

          //Otherwise, decrement the index of for loop.

          else

          {

             i--;

          }

      }
  }
  
  private String getGridValueStr(int row, int col)
  {
    // no mines in this MyJbutton's perimeter
    if ( this.mineGrid[row][col] == NO_MINES_IN_PERIMETER_MINEGRID_VALUE )
      return INITIAL_CELL_TEXT;
    
    // 1 to 8 mines in this MyJButton's perimeter
    else if ( this.mineGrid[row][col] > NO_MINES_IN_PERIMETER_MINEGRID_VALUE && 
              this.mineGrid[row][col] <= ALL_MINES_IN_PERIMETER_MINEGRID_VALUE )
      return "" + this.mineGrid[row][col];
    
    // this MyJButton in a mine
    else // this.mineGrid[row][col] = IS_A_MINE_IN_GRID_VALUE
      return MineSweapPart.EXPOSED_MINE_TEXT;
  }
  
}
