package nye.progtech.torpedo;
import java.util.*;

public class Torpedo {
    public static int numRows = 10;
    public static int numCols = 10;
    public static int playerShips;
    public static int aiShips;
    public static String[][] grid = new String[numRows][numCols];
    public static int[][] missedGuesses = new int[numRows][numCols];

    public static void main(String[] args){
        System.out.println("~~~~~ Üdvözöllek a Torpedó játékban ~~~~~");
        System.out.println("A játék elkezdéséhez adja meg a hajók mezejét\n");

        //Pálya generálás
        createOceanMap();

        //Játékos hajók bekérése
        deployPlayerShips();

        //Az AI hajók bekérése
        deployAIShips();

        //Csata fázis
        do {
            Battle();
        }while(Torpedo.playerShips != 0 && Torpedo.aiShips != 0);

        gameOver();
    }

    public static void createOceanMap(){
        //Pálya mezőinek értékét hozzuk itt létre, plusz ezeknek a választó vonalai
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = " ";
                if (j == 0)
                    System.out.print(i + "|" + grid[i][j]);
                else if (j == grid[i].length - 1)
                    System.out.print(grid[i][j] + "|" + i);
                else
                    System.out.print(grid[i][j]);
            }
            System.out.println();
        }

        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }

    public static void deployPlayerShips(){
        Scanner input = new Scanner(System.in);

        System.out.println("\nRakd le a hajóidat:");
        //Az öt hajó bekérése a játékosnak
        Torpedo.playerShips = 5;
        for (int i = 1; i <= Torpedo.playerShips; ) {
            System.out.print("Add meg az X kordinátát a " + i + ". hajódnak: ");
            int x = input.nextInt();
            System.out.print("Add meg az Y kordinátát a " + i + ". hajódnak: ");
            int y = input.nextInt();

            //Itt hozzuk létre a saját hajónknak a jelölést a tábla mezején (#)
            if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (grid[x][y] == " "))
            {
                grid[x][y] =   "#";
                i++;
            }
            else if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && grid[x][y] == "#")
                System.out.println("Nem rakhatsz két hajót ugyanarra a mezőre");
            else if((x < 0 || x >= numRows) || (y < 0 || y >= numCols))
                System.out.println("Nem rakhatsz hajót a " + numRows + " és a " + numCols + ". mezőn kívűlre");
        }
        printOceanMap();
    }

    public static void deployAIShips(){
        //Az öt hajó bekérése az AI-nak
        System.out.println("\nAz AI lerakja a hajóit");

        Torpedo.aiShips = 5;
        for (int i = 1; i <= Torpedo.aiShips; ) {
            int x = (int)(Math.random() * 10);
            int y = (int)(Math.random() * 10);

            if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (grid[x][y] == " "))
            {
                grid[x][y] =   "x";
                System.out.println(i + ". hajó lerakva");
                i++;
            }
        }
        printOceanMap();
    }

    public static void Battle(){
        playerTurn();
        aiTurn();
        printOceanMap();

        System.out.println();
        System.out.println("A hajóid: " + Torpedo.playerShips + " | Az AI hajói: " + Torpedo.aiShips);
        System.out.println();
    }

    public static void playerTurn(){
        System.out.println("\nA te köröd van!");
        int x = -1, y = -1;
        do {
            Scanner input = new Scanner(System.in);
            System.out.print("Adja meg az X kordinátát: ");
            x = input.nextInt();
            System.out.print("Adja meg az Y kordinátát: ");
            y = input.nextInt();

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) //Érvényes találgatások
            {
                if (grid[x][y] == "x") //Ha az ai hajója ott van akkor elveszíti
                {
                    System.out.println("Talált, süllyedt!");
                    grid[x][y] = "+"; //A találat jelző
                    --Torpedo.aiShips;
                }
                else if (grid[x][y] == "#") {
                    System.out.println("A saját hajódat süllyesztetted el!");
                    grid[x][y] = "%";
                    --Torpedo.playerShips;
                }
                else if (grid[x][y] == " ") {
                    System.out.println("Nem talált!");
                    grid[x][y] = "-";
                }
            }
            else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols))  //Helytelen találat
                System.out.println("Nem lehet lőni a " + numRows + " és a " + numCols + ". mezőn kívűlre");
        }while((x < 0 || x >= numRows) || (y < 0 || y >= numCols));  //Addig ismétli amég jó választ nem adunk
    }

    public static void aiTurn(){
        //Az AI találgatja a kordinátát
        System.out.println("\nAz Ai köre!");

        int x = -1, y = -1;
        do {
            x = (int)(Math.random() * 10);
            y = (int)(Math.random() * 10);

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) //Helyes találgatás
            {
                if (grid[x][y] == "#") //Ha a játékos hajója ott van akkor elsüllyeszti
                {
                    System.out.println("Az AI eltalálta és süllyesztette az egyik hajódat!");
                    grid[x][y] = "+";
                    --Torpedo.playerShips;
                }
                else if (grid[x][y] == "x") {
                    System.out.println("Az AI elsüllyesztette az egyik hajóját!");
                    grid[x][y] = "%";
                }
                else if (grid[x][y] == " ") {
                    System.out.println("Az AI nem talált!");
                    //Elmenti az AI nem talált lövéseit
                    if(missedGuesses[x][y] != 1)
                        missedGuesses[x][y] = 1;
                }
            }
        }while((x < 0 || x >= numRows) || (y < 0 || y >= numCols));  //Addig próbálkozik amddig nem ad jó választ
    }

    public static void gameOver(){
        System.out.println("Te hajóid: " + Torpedo.playerShips + " | AI hajói: " + Torpedo.aiShips);
        if(Torpedo.playerShips > 0 && Torpedo.aiShips <= 0)
            System.out.println("Gratulálok, a csatát megnyerted!");
        else
            System.out.println("Sajnálom, a csatát elvesztetted");
        System.out.println();
    }
    //A mező kimutatása
    public static void printOceanMap(){
        System.out.println();

        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        for(int x = 0; x < grid.length; x++) {
            System.out.print(x + "|");

            for (int y = 0; y < grid[x].length; y++){
                System.out.print(grid[x][y]);
            }

            System.out.println("|" + x);
        }

        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }
}