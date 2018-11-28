using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace GameLife {

    /// <summary>
    /// Provides map and required methods operating with map for simulation.
    /// </summary>
    class Map {
        private Color DEFAULT_CELL_COLOR = Color.FromArgb(255, 0, 0, 100);
        private Color DEFAULT_CELL_COLOR_1 = Color.FromArgb(255, 100, 0, 0);
        private const int MAX_CELL_CYCLES_HALF = 5;
        public float cellSize;
        private Simulation sim;
        private Cell[,] map;
        //private Dictionary<Point, Cell> hashMap;
        private Brush brushCell;
        private Brush brushCellClear;
        private Pen penBorder = new Pen(Color.DarkRed);

        /// <summary>
        /// Returns copy of actual map.
        /// </summary>
        /// <returns></returns>
        public Cell[,] getCellField() {
            //Cell[,] old = (Cell[,])map.Clone();
            Cell[,] old = new Cell[map.GetLength(0), map.GetLength(1)];

            for(int i = 0; i < map.GetLength(1); i++)
                for(int j = 0; j < map.GetLength(0); j++)
                     old[j, i] = map[j, i].getCopy();

            return old;
        }

        /// <summary>
        /// Returns true if given coordinates in Point are valid cell in map.
        /// </summary>
        /// <param name="p"> examined coordinates</param>
        /// <returns></returns>
        public Boolean isValidPoint(Point p) {
            if( (p.X >= map.GetLength(0)-1 || p.X <= 0) ||
                (p.Y >= map.GetLength(1)-1 || p.Y <= 0)) {

                return false;
            }
            return true;
        }

        /// <summary>
        /// Sets new alive cell at given position p with given race
        /// </summary>
        /// <param name="p"> position for new cell</param>
        /// <param name="race"> race of new cell</param>
        public void createNewCell(Point p, int race) {
            map[p.X, p.Y].setCell(true, sim.actualCycle);
            map[p.X, p.Y].race = race;
        }

        /// <summary>
        /// Sets new alive cell at given position p with given race
        /// </summary>
        /// <param name="p"> position for new cell</param>
        /// <param name="race"> race of new cell</param>
        /// <param name="cycles"> cycles at which is cell set when is born</param>
        public void createNewCell(Point p, int race, int cycles) {
            map[p.X, p.Y].setCell(true, sim.actualCycle + cycles);
            map[p.X, p.Y].race = race;
        }

        /// <summary>
        /// Deletes cell at given position
        /// </summary>
        /// <param name="p"> position of cell for deleting</param>
        public void deleteCell(Point p) {
            map[p.X, p.Y].setCell(false, 0);
            sim.graphics.FillEllipse(   brushCellClear,
                                        p.X * cellSize,
                                        p.Y * cellSize,
                                        cellSize, cellSize);
        }

        /// <summary>
        /// If strength of cell at given position is 0 or less returns true otherwise false and decreses
        /// strength of the cell.
        /// </summary>
        /// <param name="p"> position of cell for decrease</param>
        /// <returns></returns>
        public Boolean decreaseCell(Point p) {
            
            if(map[p.X, p.Y].strength <= 0) {
                return true;
            }


            map[p.X, p.Y].strength -= 1;
            /*
            if(sim.actualCycle - map[p.X, p.Y].bornAtCycle >= 20) {
                map[p.X, p.Y].strength -= 1;
                return true;
            }
            */
            //map[p.X, p.Y].bornAtCycle -= 5;
            /*
            if(map[p.X, p.Y].bornAtCycle < 0) {
                return true;
            }
            
            if(sim.actualCycle - map[p.X, p.Y].bornAtCycle <= 0) {
                map[p.X, p.Y].bornAtCycle = sim.actualCycle;
                return false;
            }

            if(sim.actualCycle - map[p.X, p.Y].bornAtCycle >= 10) {
                map[p.X, p.Y].bornAtCycle--;// = sim.actualCycle;
                return false;
            }
            *

            //map[p.X, p.Y].bornAtCycle = 1;
            map[p.X, p.Y].bornAtCycle =- 10;// = sim.actualCycle;
            //map[p.X, p.Y].bornAtCycle--;
            */
            return false;

        }

        /// <summary>
        /// Sets strength of cell at given position.
        /// </summary>
        /// <param name="p"> position of cell</param>
        /// <param name="value"> new value of strength</param>
        public void setStrength(Point p, int value) {
            map[p.X, p.Y].strength = value;
        }

        /// <summary>
        /// Sets race to cell at given position
        /// </summary>
        /// <param name="p"> position of cell</param>
        /// <param name="race"> new race</param>
        public void setRace(Point p, int race) {
            map[p.X, p.Y].race = race;
        }

        /// <summary>
        /// Returns color for given cell by it's age (cycles)
        /// </summary>
        /// <param name="cell"> cell for coloring</param>
        /// <returns></returns>
        private Color getColorByCell(Cell cell) {
            float val = sim.actualCycle - cell.bornAtCycle;
            if(val > 10) val = 10;

            val = (val / (MAX_CELL_CYCLES_HALF))*0.7f;

            Color defColor;
            switch(cell.race) {
                case 0:
                    defColor = DEFAULT_CELL_COLOR;
                    break;
                case 1:
                    defColor = DEFAULT_CELL_COLOR_1;
                    break;

                default:
                    defColor = DEFAULT_CELL_COLOR;
                    break;
            }

            Color newCol = ControlPaint.Light(defColor, val);

            return newCol;
        }

        /// <summary>
        /// Draws whole map into graphics context
        /// </summary>
        public void drawMap() {

            for(int i = 1; i < map.GetLength(1)-1; i++)
                for(int j = 1; j < map.GetLength(0)-1; j++) {

                    if(map[j, i].isAlive) {
                        sim.graphics.FillEllipse(   new SolidBrush(getColorByCell(map[j, i])),
                                                j * cellSize,
                                                i * cellSize,
                                                cellSize, cellSize);
                    }
                }

            sim.graphics.DrawRectangle( penBorder, 
                                        cellSize,
                                        cellSize,
                                        (map.GetLength(0) - 2) * cellSize,
                                        (map.GetLength(1) - 2) * cellSize);
            sim.mainWindow.draw();
        }

        /// <summary>
        /// Method for testing purposes fills map
        /// </summary>
        private void testFill() {
            for(int i = 0; i < map.GetLength(0); i++)
                for(int j = 0; j < map.GetLength(1); j++) {
                    Cell newCell = new Cell();
                    map[j, i] = new Cell();
                }
        }

        /// <summary>
        /// Initilize borders of map
        /// </summary>
        private void initializeBorders() {
            for(int i = 0; i < map.GetLength(0); i++) {
                createNewCell(new Point(i,0), 0);                      //top
                createNewCell(new Point(i, map.GetLength(1) - 1), 0);  //bottom
            }
            for(int i = 0; i < map.GetLength(1); i++) {
                createNewCell(new Point(0, i), 0);                    //left
                createNewCell(new Point(map.GetLength(0) - 1, i), 0);  //right
            }


        }

        /// <summary>
        /// Initializes map (fills map with "dead" cells)
        /// </summary>
        private void initializeMap() {
            for(int i = 0; i < map.GetLength(1); i++)
                for(int j = 0; j < map.GetLength(0); j++) {
                    Cell newCell = new Cell();
                    newCell.setCell(false, 0);
                    map[j, i] = new Cell();
                }
        }

        /// <summary>
        /// Creates new map by given parameters
        /// </summary>
        /// <param name="dimH"> number of cells horizontaly</param>
        /// <param name="dimV"> number of cells horizontaly</param>
        /// <param name="drawWidth"> width of drawable space</param>
        /// <param name="drawHeight"> height of drawable space</param>
        /// <param name="sim"> referent to simulation coresponding with this map</param>
        public Map(int dimH, int dimV, float drawWidth, float drawHeight, Simulation sim) {
            this.sim = sim;
            brushCell = new SolidBrush(DEFAULT_CELL_COLOR);
            brushCellClear = new SolidBrush(sim.mainWindow.backGroundColor);

            dimH += 2;  //dummy borders
            dimV += 2;  //dummy borders

            map = new Cell[dimH, dimV];
            //hashMap = new Dictionary<Point, Cell>();
            
            float numH = (float)(dimH) / drawWidth;
            float numV = (float)(dimV) / drawHeight;

            if(numH > numV) {
                cellSize = 1 / numH; //(Plane.Width - Map.SHIFT_MAP) / (h + 2f);
            } else {
                cellSize = 1 / numV; //(Plane.Height - Map.SHIFT_MAP) / (v + 2f);
            }
            //testFill();
            //initializeBorders();
            initializeMap();
            drawMap();
        }

        /// <summary>
        /// Class representing single cell in map
        /// </summary>
        public class Cell {
            public Boolean isAlive;
            public int bornAtCycle;
            public int race;
            public int strength;

            /// <summary>
            /// Sets if cell is alive and cycle at which cell was born
            /// </summary>
            /// <param name="isAlive"></param>
            /// <param name="cycle"></param>
            public void setCell(Boolean isAlive, int cycle) {
                this.isAlive = isAlive;
                bornAtCycle = cycle;
            }

            /// <summary>
            /// Returns deep copy of this cell
            /// </summary>
            /// <returns></returns>
            public Cell getCopy() {
                Cell copy = new Cell();
                copy.isAlive = this.isAlive;
                copy.bornAtCycle = this.bornAtCycle;
                copy.race = this.race;
                copy.strength = this.strength;
                return copy;
            }

            /// <summary>
            /// Creates default new dead cell
            /// </summary>
            public Cell() {
                isAlive = false;
                bornAtCycle = -1;
                strength = 1;
            }
        }
    }
}
