using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Bludiste {
    /// <summary>
    /// Main window of the application.
    /// </summary>
    public partial class Form1 : Form {
        /// <summary>
        /// Only instance of this form.
        /// </summary>
        private static Form1 formInst = new Form1();
        
        private Map map;
        public static Graphics planeGraphics;
        private Bitmap bmp;

        /// <summary>
        /// Returns only instance of this form.
        /// </summary>
        /// <returns>Single instance of this form</returns>
        public static Form1 getInstance() {
            return formInst;
        }

        /// <summary>
        /// Constructor for this form.
        /// Prepares grahic context for drawing Plane and initialize components.
        /// </summary>
        private Form1() {
            InitializeComponent();
            bmp = new Bitmap(Plane.Width, Plane.Height);
            planeGraphics = Graphics.FromImage(bmp);
            planeGraphics.Clear(Color.White);
            formInst = this;

            planeGraphics.TranslateTransform(Map.SHIFT_MAP / 2, Map.SHIFT_MAP / 2);
        }

        /// <summary>
        /// Draws changes into this Plane
        /// </summary>
        public void Draw() {
            //planeGraphics.Clear(Color.White);
            Plane_Paint(this, null);
        }

        /// <summary>
        /// Makes changes visible in Plane.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Plane_Paint(object sender, PaintEventArgs e) {
            if(bmp == null) return;
            Graphics grp = Plane.CreateGraphics();
            grp.DrawImage(bmp, 0, 0, Plane.Width, Plane.Height);
            grp.Dispose();
        }

        /// <summary>
        /// Clears graphics contex over bitmap but doesn't make changes visible.
        /// </summary>
        public static void softClear() {
            planeGraphics.Clear(Color.White);
        }

        /// <summary>
        /// Clears Plane and makes clear visible.
        /// </summary>
        public void clear() {
            softClear();
            Draw();
        }

        /// <summary>
        /// Reads selected Dimensions, calculates apropriate size of the cell and creates new map.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click(object sender, EventArgs e) {
            clear();
            int h, v;

            if(!Int32.TryParse(txHorizontal.Text, out h)) {
                Console.WriteLine("Invalid horizontal value!");
                return;
            }

            if(!Int32.TryParse(txVertical.Text, out v)) {
                Console.WriteLine("Invalid vertical value!");
                return;
            }

            if(h > Map.MAX_CELLS_H || h < 1) {
                Console.WriteLine("Valid horizontal value range is [1 to " + Map.MAX_CELLS_H + " ]!");
                return;
            }
            if(v > Map.MAX_CELLS_V || v < 1) {
                Console.WriteLine("Valid vertical value range is [1 to " + Map.MAX_CELLS_V + " ]!");
                return;
            }


            float cellSize;
            float numH = (float)(h + 2) / (float)Plane.Width;
            float numV = (float)(v + 2) / (float)Plane.Height;


            if(numH > numV) {
                cellSize = (Plane.Width - Map.SHIFT_MAP) / (h + 2f);
            } else {
                cellSize = (Plane.Height - Map.SHIFT_MAP) / (v + 2f);
            }

            map = new Map(h, v, cellSize, planeGraphics);
            map.drawMap();
        }
        
        /// <summary>
        /// Changes value of maps indicator gradualDraw
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void chbGradual_CheckedChanged(object sender, EventArgs e) {
            Map.gradualDraw = chbGradual.Checked;
        }

        /// <summary>
        /// Changes value of maps variable for desity of preopened spaces in map.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void numDensity_ValueChanged(object sender, EventArgs e) {
            Map.RND_MAX_OPENED = (int)numDensity.Value;
        }

        /// <summary>
        /// Catches keystrokes from W,S,A,D for movement of player and calls draw for movement to take effect.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Form1_KeyPress(object sender, KeyPressEventArgs e) {
            if(!Map.generated) {
                return;
            }
            switch(e.KeyChar) {
                case 'w':
                    map.movePlayer(Map.Cell.Orientation.N);
                    break;
                case 's':
                    map.movePlayer(Map.Cell.Orientation.S);
                    break;
                case 'a':
                    map.movePlayer(Map.Cell.Orientation.W);
                    break;
                case 'd':
                    map.movePlayer(Map.Cell.Orientation.E);
                    break;
                default:
                    break;
            }
            Draw();
        }

        /// <summary>
        /// Starts "game" or restarts player to it's starting possition and clears his path.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btPlay_Click(object sender, EventArgs e) {
            if(Map.generated) {
                map.restart();
                Draw();
            } else {
                Console.Write("Map is not generated yet!");
            }
        }
    }
}
        