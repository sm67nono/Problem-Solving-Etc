import java.util.*;
class Pizza
{
  int rows;
  int columns;
  String layout[][];
}

class PizzaSlice
{
  //Storing all Combination possible to slice the Pizza vs Single combination possible to slice the pizza
  ArrayList<ArrayList<String>> allPossibleCombinations;
  ArrayList<String> storeOneCombination;
  int areOfOriginalPizza;

  //Take parameters including slice dimensions and check if a slice is possible
  public boolean isASlice(Pizza pizza, int maxSliceSize, int minPiecesPerSlice, String sliceDims)
  {

    int rowsinSlice=Integer.parseInt(sliceDims.substring(0,1));
    int columnsinSlice=Integer.parseInt(sliceDims.substring(2));
    int tomatoes=0,mushrooms=0;

    for(int i=0;i<rowsinSlice;i++)
    {
      for (int j=0;j<columnsinSlice;j++)
      {
        String check=pizza.layout[i][j];
        if(check=="T"){tomatoes++;
        }else{mushrooms++;}
      }

    }
    if(tomatoes>=1&&mushrooms>=1)
    {
      return true;
    }
    return false;
  }

  public Pizza remainingPizza(Pizza pizza, String possibleSlice,int newRows, int newCols, int rowStartIndex, int columnsStartIndex)
  {
    //The slicing always starts from 0.0
    Pizza remPizza = new Pizza();


    int currentPizzaRows=pizza.rows;
    int currentPizzaColumns=pizza.columns;

    int rowSlice=Integer.parseInt(possibleSlice.substring(0,1));
    int columnsSlice=Integer.parseInt(possibleSlice.substring(2));

    /*System.out.println(possibleSlice);
    System.out.println(currentPizzaRows);
    System.out.println(currentPizzaColumns);
    System.out.println(rowStartIndex);
    System.out.println(columnsStartIndex);
    System.out.println(newRows);
    System.out.println(newCols);*/

    remPizza.layout = new String[newRows][newCols];

    //Copying the values from remainder of the Pizza
    for(int x=0,i=rowStartIndex;i<rowStartIndex+newRows;x++,i++)
    {
      for(int y=0,j=columnsStartIndex;j<columnsStartIndex+newCols;y++,j++)
      {
        System.out.print(pizza.layout[i][j]);
          remPizza.layout[x][y]=pizza.layout[i][j];
      }
      System.out.println();

    }

    remPizza.rows = newRows;
    remPizza.columns = newCols;

    return remPizza;
  }

  public List<String> findPossibleSlices(int rows, int columns, int maxSizePerSlice)
  {
    //Generating possible slices
    List<String> lst = new ArrayList<String>();
    for(int i=2;i<=maxSizePerSlice;i++)
    {
        for(int j=1;j<=i;j++)
        {
          if(i%j==0)
          {
            String factor=j+"x"+(i/j);

            if(j<=rows && (i/j)<=columns)
            {
              System.out.println("Factor of"+i+" --> "+factor);
              lst.add(factor);
            }
          }
        }
    }
    return lst;
  }

  public boolean validateSlice(List<String> slices)
  {
    int totalCells=0;
    for(String sliceDims:slices)
    {
      int rowsinSlice=Integer.parseInt(sliceDims.substring(0,1));
      int columnsinSlice=Integer.parseInt(sliceDims.substring(2));
      totalCells=totalCells+(rowsinSlice+columnsinSlice);
    }
    if(totalCells==areOfOriginalPizza)
    {
      return true;
    }
    return false;
  }

  public void pizzaCutter(Pizza pizza, int maxSliceSize, int minPiecesPerSlice, List<String> possibleSlices)
  {
    //Perform a possible cut start using the first combination
    for(String possibleSlice:possibleSlices)
    {

      if(isASlice(pizza, maxSliceSize, minPiecesPerSlice,possibleSlice))
      {
        System.out.println(possibleSlice);
        storeOneCombination.add(possibleSlice);
        int rowsinSlice=Integer.parseInt(possibleSlice.substring(0,1));
        int columnsinSlice=Integer.parseInt(possibleSlice.substring(2));


        //if one of the dims are 1 for a slice then remaining pizza can be divided as one single piece
        if(rowsinSlice==pizza.rows || columnsinSlice==pizza.columns)
        {
          //Get remainder Pizza dimensions 1
          int rowsRemPizza;
          int columnsRemPizza;
          int rowStartIndex;
          int columnsStartIndex;

          if(rowsinSlice==pizza.rows)
          {
            rowsRemPizza=pizza.rows;
            columnsRemPizza=pizza.columns-columnsinSlice;
            rowStartIndex=0;
            columnsStartIndex=columnsinSlice;
          }
          else//Columns in SLice==pizza.columns
          {
            rowsRemPizza=pizza.rows-rowsinSlice;
            columnsRemPizza=pizza.columns;
            rowStartIndex=rowsinSlice;
            columnsStartIndex=0;
          }
          Pizza remPizza = remainingPizza(pizza,possibleSlice,rowsRemPizza,columnsRemPizza,rowStartIndex,columnsStartIndex);
          pizzaCutter(remPizza, maxSliceSize, minPiecesPerSlice, findPossibleSlices(remPizza.rows,remPizza.columns,maxSliceSize));
        }
        else//The remaining pizza must be divided into two smaller pieces
        {
          //Remainder Pizza dimensions 1
          int rowsRemPizza1;
          int columnsRemPizza1;
          int rowStartIndex1=0;
          int columnsStartIndex1=columnsinSlice;

          rowsRemPizza1=rowsinSlice;
          columnsRemPizza1=pizza.columns-columnsinSlice;

          Pizza remPizza1 = remainingPizza(pizza,possibleSlice,rowsRemPizza1,columnsRemPizza1,rowStartIndex1,columnsStartIndex1);
          pizzaCutter(remPizza1, maxSliceSize, minPiecesPerSlice, findPossibleSlices(remPizza1.rows,remPizza1.columns,maxSliceSize));

          //Remainder Pizza dimensions 2
          int rowsRemPizza2;
          int columnsRemPizza2;
          int rowStartIndex2=rowsinSlice;
          int columnsStartIndex2=0;

          rowsRemPizza2=pizza.rows-rowsinSlice;
          columnsRemPizza2=pizza.columns;

          Pizza remPizza2 = remainingPizza(pizza,possibleSlice,rowsRemPizza2,columnsRemPizza2,rowStartIndex2,columnsStartIndex2);
          pizzaCutter(remPizza2, maxSliceSize, minPiecesPerSlice, findPossibleSlices(remPizza2.rows,remPizza2.columns,maxSliceSize));
        }

        //Validate slice to check if it contains all the cells
        if(validateSlice(storeOneCombination))
        {
          allPossibleCombinations.add(storeOneCombination);
        }

        storeOneCombination = new ArrayList<String>();
      }
    }
  }

  public static void main(String args[])
  {
    //Simple test case
    int r=3,c=5,l=1,h=6;
    Pizza pizza=new Pizza();
    pizza.rows=r;
    pizza.columns=c;
    //pizza.layout=new String[pizza.columns][pizza.rows];
    pizza.layout=new String[][]{{"T","T","T","T","T"},
                  {"T","M","M","M","T"},
                  {"T","T","T","T","T"}};

    PizzaSlice pslice = new PizzaSlice();
    List<String> possibleSlices = pslice.findPossibleSlices(pizza.rows,pizza.columns,h);

    //Initializing global variables
    pslice.storeOneCombination = new ArrayList<String>();
    pslice.allPossibleCombinations = new ArrayList<ArrayList<String>>();
    pslice.areOfOriginalPizza = r*c;
    //Core algorithm
    pslice.pizzaCutter(pizza,h,l,possibleSlices);

    //Check results
    System.out.println("Printing the possible combinations");
    for(List<String> combination:pslice.allPossibleCombinations)
    {
      for(String value:combination)
      {
        System.out.print(value);
      }
        System.out.println();
    }

  }
}
