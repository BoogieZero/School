using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bludiste {
    /// <summary>
    /// Class contains information of map of the labyrinth and provides methods for creating drawing labyrinth
    /// and player including his movements.
    /// </summary>
    class Map {
        public const int SHIFT_MAP = 0;
        public const int MAX_CELLS_H = 360;
        public const int MAX_CELLS_V = 205;

        public static Boolean generated = false;
        public static Boolean gradualDraw = false;
        public static int RND_MAX_OPENED = 0;
        private int RND_OPEN_MIN;
        private int RND_OPEN_MAX;

        private int CELL_SIZE;
        private int PLAYER_SIZE;

        private Cell[,] map;
        private Stack stack;
        private Random rnd = new Random();
        private Graphics graphics;

        private Pen pen = new Pen(Color.Black, 1);
        private Pen penP = new Pen(Color.Red, 1);
        private Brush brushP = new SolidBrush(Color.Red);
        private Brush brushPC = new SolidBrush(Color.White);

        private Cell startCell;
        private Cell finishCell;
        private Rectangle player;
        private Cell playerCell;
        private Point first, second;

        /// <summary>
        /// Constructor for new map which is created by given parameters.
        /// </summary>
        /// <param name="dimH">Horizontal dimension</param>
        /// <param name="dimV">Vertical dimension</param>
        /// <param name="cellSize">Size of the single cell side</param>
        /// <param name="graphics">Graphics context to which is map drawn to</param>
        public Map(int dimH, int dimV, float cellSize, Graphics graphics) {
            dimH += 2;
            dimV += 2;

            int allCells = dimH * dimV;
            if(RND_MAX_OPENED != 0) {
                RND_OPEN_MIN = (allCells / RND_MAX_OPENED)+1;
                RND_OPEN_MAX = RND_OPEN_MIN * 2;
            }
            
            int counter = randomIndexOpen();
            map = new Cell[dimH, dimV];
            for (int i = 0; i < dimH; i++)
                for (int j = 0; j < dimV; j++) {
                    map[i, j] = new Cell(i, j);
                    counter--;
                    if(RND_MAX_OPENED !=0 && counter == 0) {
                        randomOpen(i, j);
                        counter = randomIndexOpen();
                    }
                }

            this.graphics = graphics;
            this.CELL_SIZE = (int)cellSize;
            this.PLAYER_SIZE = (int)(3f / 4f * CELL_SIZE);
            
            initializeMap();
            createMaze();
        }

        /// <summary>
        /// Clears old possition of player and initialize player at starting possition.
        /// </summary>
        public void initializePlayer() {
            clearPlayer();

            Point playerPosition = new Point();
            playerPosition.X = startCell.posX * CELL_SIZE + CELL_SIZE / 2 - PLAYER_SIZE / 2;
            playerPosition.Y = startCell.posY * CELL_SIZE + CELL_SIZE / 2 - PLAYER_SIZE / 2;
            playerCell = startCell;

            player = new Rectangle( playerPosition.X, 
                                    playerPosition.Y, 
                                    PLAYER_SIZE, PLAYER_SIZE);

        }

        /// <summary>
        /// ćlears acual possition of player
        /// </summary>
        private void clearPlayer() {
            graphics.FillEllipse(brushPC, player);
            drawLineBehindPlayer();
        }

        /// <summary>
        /// Generates number for counter which determines possition of random preopen cell.
        /// </summary>
        /// <returns>Random number in the interval [RND_OPEN_MIN, RND_OPEN_MAX]</returns>
        private int randomIndexOpen() {
            return rnd.Next(RND_OPEN_MIN, RND_OPEN_MAX);
        }

        /// <summary>
        /// Creates random preopen if possible.
        /// </summary>
        /// <param name="i">horizontal index in map</param>
        /// <param name="j">vertical index in map</param>
        private void randomOpen(int i, int j) {
            if(     i <= 1 || j < 1 ||
                    i == map.GetLength(0) - 1 ||
                    j >= map.GetLength(1) - 1) {
                return;
            }
            map[i, j].left = false;
        }

        /// <summary>
        /// Initialize default borders of the map as visited.
        /// </summary>
        private void initializeMap() {
            int horizontal = map.GetLength(0);
            int vertical = map.GetLength(1);

            //vertical
            for(int i = 0; i < vertical; i++) {
                map[horizontal - 1, i].setVisited();
                map[0, i].setVisited();
            }
            
            //horizontal
            for(int i = 0; i < horizontal; i++) {
                map[i, vertical - 1].setVisited();
                map[i, 0].setVisited();
            }
            
        }

        /// <summary>
        /// Checks if wall to Cell neighbour in dirrection of given orientation is open.
        /// </summary>
        /// <param name="cell">Examined cell</param>
        /// <param name="orientation">Determines wall from this cell</param>
        /// <returns>true if wall is open</returns>
        public Boolean isOpen(Cell cell, Cell.Orientation orientation) {
            switch(orientation) {
                case Cell.Orientation.N:    return !map[cell.posX,       cell.posY].top;
                case Cell.Orientation.W:    return !map[cell.posX,       cell.posY].left;
                case Cell.Orientation.S:    return !map[cell.posX,   cell.posY + 1].top;
                case Cell.Orientation.E:    return !map[cell.posX+ 1,    cell.posY].left;

                default:
                    Console.Write("Wrong Orientation!");
                    return false;
            }
        }
        
        /// <summary>
        /// Returns neighbour cell from given cell in given direction.
        /// </summary>
        /// <param name="cell">default Cell</param>
        /// <param name="orientation">direction of wanted neighbour</param>
        /// <returns>neighbour Cell found in given orientation from given Cell</returns>
        private Cell nextToCell(Cell cell, Cell.Orientation orientation) {
            switch(orientation) {
                case Cell.Orientation.N:    return map[cell.posX,   cell.posY-1];
                case Cell.Orientation.W:    return map[cell.posX-1, cell.posY];
                case Cell.Orientation.S:    return map[cell.posX,   cell.posY+1];
                case Cell.Orientation.E:    return map[cell.posX+1, cell.posY];

                default:
                    Console.Write("Wrong Orientation!");
                    return null;
            }
        }

        /// <summary>
        /// Moves player in given direction. Updates points first and second for drawing following line behind player.
        /// </summary>
        /// <param name="orientation">Direction for player movement</param>
        public void movePlayer(Cell.Orientation orientation) {
            if(!isOpen(playerCell, orientation) || playerCell == finishCell) {
                return;
            }
            
            clearPlayer();
            switch(orientation) {
                case Cell.Orientation.N:
                    player.Y -= CELL_SIZE;
                    break; //map[cell.posX, cell.posY - 1];
                case Cell.Orientation.W:
                    player.X -= CELL_SIZE;
                    break; //map[cell.posX - 1, cell.posY];
                case Cell.Orientation.S:
                    player.Y += CELL_SIZE;
                    break; //map[cell.posX, cell.posY + 1];
                case Cell.Orientation.E:
                    player.X += CELL_SIZE;
                    break; //map[cell.posX + 1, cell.posY];

                default:
                    Console.Write("Wrong player shift!");
                    return;
            }
            first = new Point(playerCell.posX * CELL_SIZE + CELL_SIZE/2,
                                    playerCell.posY * CELL_SIZE + CELL_SIZE / 2);

            playerCell = nextToCell(playerCell, orientation);

            second = new Point(   playerCell.posX * CELL_SIZE + CELL_SIZE / 2,
                                        playerCell.posY * CELL_SIZE + CELL_SIZE / 2);

           
            drawPlayer();
            drawLineBehindPlayer();
        }

        /// <summary>
        /// Draws line between Points first and second
        /// </summary>
        private void drawLineBehindPlayer() {
            graphics.DrawLine(penP, first, second);
        }

        /// <summary>
        /// Opens wall from given cell to direction in which this cell was found and marks this cell as visited.
        /// </summary>
        /// <param name="cell">Cell to which is removed wall</param>
        private void makeWay(Cell cell) {
            switch(cell.foundFrom) {
                case Cell.Orientation.N:
                    cell.top = false;
                    break;
                case Cell.Orientation.W:
                    cell.left = false;
                    break;
                case Cell.Orientation.S:
                    nextToCell(cell, Cell.Orientation.S).top = false;
                    break;
                case Cell.Orientation.E:
                    nextToCell(cell, Cell.Orientation.E).left = false;
                    break;

                default:
                    Console.Write("Wrong Orientation!");
                    return;
            }
            cell.visited = true;
        }

        /// <summary>
        /// Draws top wall of the given cell.
        /// </summary>
        /// <param name="x">horizontal index of cell in map</param>
        /// <param name="y">vertical index of cell in map</param>
        private void drawCellTop(int x, int y) {
            if(!map[x, y].top) return;

            //horizontal line
            Point p1 = new Point(x * CELL_SIZE, y * CELL_SIZE);
            Point p2 = new Point(p1.X + CELL_SIZE, p1.Y);
            graphics.DrawLine(pen, p1, p2);
        }

        /// <summary>
        /// Draws left wall of given cell.
        /// </summary>
        /// <param name="x">horizontal index of cell in map</param>
        /// <param name="y">vertical index of cell in map</param>
        private void drawCellLeft(int x, int y) {
            if(!map[x, y].left) return;

            //vertical line
            Point p1 = new Point(x * CELL_SIZE, y * CELL_SIZE);
            Point p2 = new Point(p1.X, p1.Y + CELL_SIZE);
            graphics.DrawLine(pen, p1, p2);
        }

        /// <summary>
        /// Draws left and top wall of given cell.
        /// </summary>
        /// <param name="x">horizontal index of cell in map</param>
        /// <param name="y">vertical index of cell in map</param>
        private void drawCell(int x, int y) {
            drawCellTop(x, y);
            drawCellLeft(x, y);  
        }

        /// <summary>
        /// Draws whole map including closing walls on the side and 
        /// excluding cells which are not a part of the labyrinth.
        /// </summary>
        public void drawMap() {
            int horizontal = map.GetLength(0);
            int vertical = map.GetLength(1);

            //draw cells
            for(int i = 1; i < vertical-1; i++) {
                //rows
                for(int j = 1; j < horizontal-1; j++) {
                    //columns
                    drawCell(j, i);
                }
            }


            //draw right closure
            for(int i = 1; i < vertical-1; i++) {
                drawCellLeft(horizontal-1, i);
            }
            
            //draw bottom closure
            for(int i = 1; i < horizontal-1; i++) {
                drawCellTop(i, vertical - 1);
            }

            Form1.getInstance().Draw();
        }

        /// <summary>
        /// Returns starting cell
        /// </summary>
        /// <returns>starting cell</returns>
        private Cell getStartCell() {
            startCell = map[1, 1];
            return map[1, 1];
        }

        /// <summary>
        /// Marks and draw start and finish possition for player.
        /// </summary>
        private void createFinish() {
            //map[map.GetLength(0) - 1, map.GetLength(1) - 2].left = false;
            map[1, 1].left = true;
            finishCell = map[map.GetLength(0) - 2, map.GetLength(1) - 2];


            Rectangle startTile = new Rectangle(startCell.posX * CELL_SIZE+2, 
                                                startCell.posY * CELL_SIZE+2, 
                                                CELL_SIZE-3, CELL_SIZE-3);

            Rectangle finishTile = new Rectangle(   finishCell.posX * CELL_SIZE+2,
                                                    finishCell.posY * CELL_SIZE+2,
                                                    CELL_SIZE-3, CELL_SIZE-3);

            graphics.FillRectangle(new SolidBrush(Color.LightBlue), startTile);
            graphics.FillRectangle(new SolidBrush(Color.LightGreen), finishTile);

        }

        /// <summary>
        /// Adds valid neighbours of given cell to stack in random order.
        /// </summary>
        /// <param name="cell">cell which neighbours are to be added into stack</param>
        private void addNeighboursFromCell(Cell cell) {
            List<Cell> neighbours = new List<Cell>();
            Cell found;
            foreach(Cell.Orientation ori in Enum.GetValues(typeof(Cell.Orientation)).Cast<Cell.Orientation>()) {
                found = nextToCell(cell, ori);
                if(!found.visited) {
                    neighbours.Add(found);
                    found.found(Cell.reverse(ori));
                }
            }

            int rndIndex;
            while(neighbours.Count != 0) {
                rndIndex = rnd.Next(0, neighbours.Count);
                stack.push(neighbours[rndIndex]);
                neighbours.RemoveAt(rndIndex);
            }
            
        }

        /// <summary>
        /// Creates actual labyrinth in map.
        /// </summary>
        private void createMaze() {
            stack = new Stack();
            Cell actual = getStartCell();
            actual.found(Cell.Orientation.W);
            stack.push(actual);

            while(!stack.isEmpty()) {
                while(actual.visited && !stack.isEmpty()) {
                    actual = stack.pop();
                }
                makeWay(actual);
                if(gradualDraw) {
                    Form1.softClear();
                    drawMap();
                }    
                addNeighboursFromCell(actual);
            }
            createFinish();
            initializePlayer();
            generated = true;
        }

        /// <summary>
        /// Draws player in his possition.
        /// </summary>
        public void drawPlayer() {
            graphics.FillEllipse(brushP, player);
        }
        
        /// <summary>
        /// Clears player and paths and draws clear map.
        /// </summary>
        public void restart() {
            Form1.softClear();
            first = new Point();
            second = new Point();
            drawMap();
            initializePlayer();
            createFinish();
            drawPlayer();

        }

        /// <summary>
        /// Class represents one cell in map.
        /// </summary>
        public class Cell {
            public int posX;
            public int posY;
            public Boolean top;
            public Boolean left;
            private Boolean inStack;
            public Boolean visited;
            
            public Orientation foundFrom;

            /// <summary>
            /// Creates new instance of cell by given parameters.
            /// </summary>
            /// <param name="posX">horizontal index in map</param>
            /// <param name="posY">vertical index in map</param>
            public Cell(int posX, int posY) {
                top = true;
                left = true;
                inStack = false;
                visited = false;
               
                this.posX = posX;
                this.posY = posY;
            }

            /// <summary>
            /// Sets orientation from which was this cell found. (from this cell's perspective)
            /// </summary>
            /// <param name="orientation">Direction from which was this cell found</param>
            public void found(Orientation orientation) {
                foundFrom = orientation;
                inStack = true;
            }

            /// <summary>
            /// Sets this cell as visited.
            /// </summary>
            public void setVisited() {
                visited = true;
            }

            /// <summary>
            /// Convenient string describing this cell for testing purposes.
            /// </summary>
            /// <returns>string describing this cell</returns>
            public override String ToString() {
                String walls = "";
                if(top) {
                    walls += "T ";
                }
                if(left) {
                    walls += "L ";
                }
                return ("[" + posX + ", " + posY + "]" +
                        walls +
                        " visited:   " + inStack) +
                        " from: " + foundFrom+"\n";
            }

            /// <summary>
            /// Enumeration class representing orientations in map.
            /// (North, West, South, East)
            /// </summary>
            public enum Orientation{
                N,W,S,E
            }

            public static Orientation reverse(Orientation orientation) {
                switch(orientation) {
                    case Orientation.N: return Orientation.S;
                    case Orientation.W: return Orientation.E;
                    case Orientation.S: return Orientation.N;
                    case Orientation.E: return Orientation.W;

                    default:
                        Console.Write("Wrong Orientation to reverse!");
                        return Orientation.N;
                }
            }

        }

    }
}
