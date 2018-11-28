using System;
using System.Drawing;


using System.Windows.Forms;


namespace GameLife {
    /// <summary>
    /// Class provides methods to operare with simulation.
    /// </summary>
    class Simulation {
        public int actualCycle;
        public Graphics graphics;
        public MainWindow mainWindow;
        public Map map;
        private Map.Cell[,] cellField;
        private Timer timer;
        private int mod = 0;

        /// <summary>
        /// Checks neighbour cells from given coordinates and applys basic rules for Game of Life
        /// </summary>
        /// <param name="x"> X coordinate of cell</param>
        /// <param name="y"> Y coordinate of cell</param>
        private void basicRule(int x, int y) {
            int counterAlive = 0;

            //top-left to bottom-right
            if(cellField[x - 1, y - 1].isAlive) counterAlive++;
            if(cellField[x, y - 1].isAlive) counterAlive++;
            if(cellField[x + 1, y - 1].isAlive) counterAlive++;

            if(cellField[x - 1, y].isAlive) counterAlive++;
            if(cellField[x + 1, y].isAlive) counterAlive++;

            if(cellField[x - 1, y + 1].isAlive) counterAlive++;
            if(cellField[x, y + 1].isAlive) counterAlive++;
            if(cellField[x + 1, y + 1].isAlive) counterAlive++;

            if(cellField[x, y].isAlive) {
                //cell was alive
                if(counterAlive == 2 || counterAlive == 3) {
                    //lives
                    return;
                } else {
                    //dies
                    map.deleteCell(new Point(x, y));
                    return;
                }
            } else {
                //cell was dead
                if(counterAlive == 3) {
                    map.createNewCell(new Point(x, y), 0);
                }
            }

        }

        /// <summary>
        /// Adds up to reds if given cell is red otherwise adds to blues
        /// </summary>
        /// <param name="cell"> cell for examination </param>
        /// <param name="blues"> number of already found blue cells </param>
        /// <param name="reds"> number of already found red cells</param>
        /// <param name="bls"> returns new counter for blue cells</param>
        /// <param name="rds"> returns new counter for red cells</param>
        private void addRedBlues(Map.Cell cell, int blues, int reds, out int bls, out int rds) {
            
            if(cell.race == 0) {
                blues++;
            } else {
                reds++;
            }
            bls = blues;
            rds = reds;
        }

        /// <summary>
        /// Gets neighbour and applys advanced rules to cell given by it's coordinates.
        /// </summary>
        /// <param name="x"> x coordinate</param>
        /// <param name="y"> y coordinate</param>
        private void advancedRule(int x, int y) {
            int counterAlive = 0;
            int blues = 0;
            int reds = 0;

            //top-left to bottom-right
            //top
            if(cellField[x - 1, y - 1].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x - 1, y - 1], blues, reds, out blues, out reds);
            }
            if(cellField[x, y - 1].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x, y - 1], blues, reds, out blues, out reds);
            }
            if(cellField[x + 1, y - 1].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x + 1, y - 1], blues, reds, out blues, out reds);
            }

            //middle
            if(cellField[x - 1, y].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x - 1, y], blues, reds, out blues, out reds);
            }
            if(cellField[x + 1, y].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x + 1, y], blues, reds, out blues, out reds);
            }

            //bottom
            if(cellField[x - 1, y + 1].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x - 1, y + 1], blues, reds, out blues, out reds);
            }
            if(cellField[x, y + 1].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x, y + 1], blues, reds, out blues, out reds);
            }
            if(cellField[x + 1, y + 1].isAlive) {
                counterAlive++;
                addRedBlues(cellField[x + 1, y + 1], blues, reds, out blues, out reds);
            }

            ///////

            /*
            if(cellField[x, y].isAlive) {
                //cell was alive
                if(counterAlive >= 3 && counterAlive <= 6) {
                    //lives
                    return;
                } else {
                    //dies
                    if(map.decreaseCell(new Point(x, y)))
                        map.deleteCell(new Point(x, y));
                    return;
                }
            } else {
                //cell was dead
                if(counterAlive == 3 || counterAlive == 3) {
                    map.createNewCell(new Point(x, y), 0);
                    map.setStrength(new Point(x, y), counterAlive+2);
                }
            }
            */

            //////
            
            if(cellField[x, y].isAlive) {
                //cell was alive

                if(cellField[x, y].race == 0) {
                    //blue
                    if(blues >= 3 && blues <= 7 && reds < 3) {
                        //lives
                        if(actualCycle - cellField[x, y].bornAtCycle >= 300 && reds < 3) {
                            //death after 300 cycles

                            //if(map.decreaseCell(new Point(x, y)))
                                map.deleteCell(new Point(x, y));
                        }
                        return;
                    } else {
                        //dies
                        if(map.decreaseCell(new Point(x, y)))
                            map.deleteCell(new Point(x, y));
                        return;
                    }
                } else {
                    //red
                    if(reds >= 2 && reds <= 4) {
                        //lives
                        if(actualCycle - cellField[x, y].bornAtCycle >= 100) {
                            //possible death after 100 cycles
                            
                            if(map.decreaseCell(new Point(x, y)))
                                map.deleteCell(new Point(x, y));
                        }
                        return;
                    } else {
                        //dies
                        if(map.decreaseCell(new Point(x, y)))
                            map.deleteCell(new Point(x, y));
                        return;
                    }
                }
                
            } else {
                //cell was dead
                if(reds > blues) {
                    if(reds >= 2 && reds <= 3) {
                        //new cells where most of them dies in next cycle
                        map.createNewCell(new Point(x, y), 1);
                        map.setStrength(new Point(x, y), reds -3);
                    }
                } else {
                    if(blues >= 3 && blues <= 3) {
                        map.createNewCell(new Point(x, y), 0);
                        map.setStrength(new Point(x, y), blues);
                    }
                }
            }
            //////
            

        }

        /// <summary>
        /// Iterates through all cells and applys basic rules.
        /// </summary>
        private void rulesClassic() {
            cellField = map.getCellField();
            Boolean[] lives = new Boolean[8];

            for(int i = 1; i < cellField.GetLength(1) - 1; i++)
                for(int j = 1; j < cellField.GetLength(0) - 1; j++) {
                    basicRule(j, i);
                }
        }

        /// <summary>
        /// Iterates through all cells and applys advanced rules.
        /// </summary>
        private void rulesAdvanced() {
            cellField = map.getCellField();
            Boolean[] lives = new Boolean[8];

            for(int i = 1; i < cellField.GetLength(1) - 1; i++)
                for(int j = 1; j < cellField.GetLength(0) - 1; j++) {
                    advancedRule(j, i);
                }
        }

        /// <summary>
        /// Applys rules determined by mod (which is set by UI)
        /// </summary>
        private void applyRules() {
            switch(mod) {
                case 0:
                    rulesClassic();
                    break;
                case 1:
                    rulesAdvanced();
                    break;
            }


        }

        /// <summary>
        /// Moves simulation one cycle foward.
        /// </summary>
        public void nextCycle() {
            actualCycle++;
            applyRules();
            map.drawMap();
        }

        /// <summary>
        /// Method which is called by timer for next cycle in simulation.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void nextCycle(object sender, EventArgs e) {
            nextCycle();
        }

        /// <summary>
        /// Starts simulation.
        /// </summary>
        public void startSimulation() {
            timer.Start();
        }

        /// <summary>
        /// Stops simulation.
        /// </summary>
        public void stopSimulation() {
            timer.Stop();
        }

        /// <summary>
        /// Returns Point with coordinates with coresponding cell on which was clicked in UI.
        /// </summary>
        /// <param name="p"></param>
        /// <returns></returns>
        private Point getPointByMap(Point p) {
            int x = (int)(p.X / map.cellSize);
            int y = (int)(p.Y / map.cellSize);
            Point pc = new Point(x, y);
            return pc;
        }

        /// <summary>
        /// Adds new cell on coresponding possition where was clicked.
        /// </summary>
        /// <param name="p"> position of click </param>
        /// <param name="race"> race for new cell</param>
        public void addCellFromClick(Point p, int race) {
            //Console.Write("PossitionClicked: ["+p.X+", "+p.Y+"]\n");
            //Console.Write("PossitionDivided: [" + (int)(p.X/map.cellSize) + ", " + (int)(p.Y/map.cellSize) + "]\n");
            Point pc = getPointByMap(p);
            if(!map.isValidPoint(pc)) return;

            map.createNewCell(pc, race);
            map.drawMap();
        }

        /// <summary>
        /// Deletes (kills) coresponding cell from location of Point p
        /// </summary>
        /// <param name="p"></param>
        public void clearCellFromClick(Point p) {
            Point pc = getPointByMap(p);
            if(!map.isValidPoint(pc)) return;

            map.deleteCell(pc);
            map.drawMap();
        }

        /// <summary>
        /// Returns state of mod which determines which rules are used in smulation.
        /// </summary>
        /// <returns></returns>
        public int getMod() {
            return mod;
        }

        /// <summary>
        /// Sets state of mod which determines which rules are used in smulation.
        /// </summary>
        /// <param name="mod"> number determining ruleset for simulation </param>
        public void setMod(int mod) {
            this.mod = mod;
        }

        /// <summary>
        /// Sets speed of tick in timer which controlls how often is nextCycle() called
        /// </summary>
        /// <param name="value"></param>
        public void setTimerInterval(int value) {
            timer.Interval = value;
        }

        /// <summary>
        /// Creates new instance of simulation by given parameters.
        /// </summary>
        /// <param name="dimH"> number of cells horizontaly</param>
        /// <param name="dimV"> number of cells horizontaly</param>
        /// <param name="drawWidth"> width of drawable space</param>
        /// <param name="drawHeight"> height of drawable space</param>
        /// <param name="graphics"> graphic context for drawable space</param>
        /// <param name="mainWindow"> reference to main window for calling draw methods</param>
        public Simulation(int dimH, int dimV, float drawWidth, float drawHeight, Graphics graphics, MainWindow mainWindow) {
            this.mainWindow = mainWindow;
            this.graphics = graphics;
            actualCycle = 0;
            map = new Map(dimH, dimV, drawWidth, drawHeight, this);

            timer = new Timer();
            timer.Enabled = false;
            timer.Interval = 1;
            timer.Tick += new EventHandler(nextCycle);
        }
    }
}
