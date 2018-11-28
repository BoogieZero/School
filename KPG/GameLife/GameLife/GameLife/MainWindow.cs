using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace GameLife {
    public partial class MainWindow : Form {
        private Bitmap bmp;
        private Graphics graphics;
        private Pen pen;
        public Color backGroundColor = Color.Black;
        private Color defaultColor = Color.White;
        private Map map;
        private Simulation sim;
        private Boolean mouseDown = false;
        private int drawRace = 0;

        public MainWindow() {
            InitializeComponent();
            bmp = new Bitmap(drawBoard.Width, drawBoard.Height);
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(backGroundColor);
            pen = new Pen(defaultColor);

            Console.Write("Panel [" + drawBoard.Width + ", " + drawBoard.Height + "]\n");

        }

        public void draw() {
            drawBoard_Paint(this, null);
        }

        private void drawBoard_Paint(object sender, PaintEventArgs e) {
            if(bmp == null) return;
            Graphics grp = drawBoard.CreateGraphics();
            grp.DrawImage(bmp, 0, 0, drawBoard.Width, drawBoard.Height);
            grp.Dispose();
        }

        public void softClear() {
            graphics.Clear(backGroundColor);
        }

        public void clear() {
            softClear();
            draw();
        }

        private void btCreate_Click(object sender, EventArgs e) {
            if(sim != null)
                sim.stopSimulation();

            trackTimer.Value = 1;
            clear();
            sim = new Simulation(   (int)numHorizontal.Value, 
                                    (int)numVertical.Value, 
                                    drawBoard.Width, 
                                    drawBoard.Height, 
                                    graphics,
                                    this);
            if(rbDefault.Checked) {
                sim.setMod(0);
            } else {
                sim.setMod(1);
            }
            gbSimulation.Enabled = true;
            gbSimulationParameters.Enabled = true;
        }

        private void drawBoard_Click(object sender, EventArgs e) {
            
        }

        private void drawBoard_MouseDown(object sender, MouseEventArgs e) {
            if(sim == null) return;
            switch(e.Button) {
                case MouseButtons.Left:
                    sim.addCellFromClick(e.Location, drawRace);
                    mouseDown = true;
                    break;

                case MouseButtons.Right:
                    sim.clearCellFromClick(e.Location);
                    mouseDown = true;
                    break;
            }
            
            
        }

        private void drawBoard_MouseUp(object sender, MouseEventArgs e) {
            mouseDown = false;
        }

        private void drawBoard_MouseMove(object sender, MouseEventArgs e) {
            if(sim == null) return;
            if(!mouseDown) {
                return;
            }

            switch(e.Button) {
                case MouseButtons.Left:
                    sim.addCellFromClick(e.Location, drawRace);
                    mouseDown = true;
                    break;

                case MouseButtons.Right:
                    sim.clearCellFromClick(e.Location);
                    mouseDown = true;
                    break;
            }
        }

        private void btNextCycle_Click(object sender, EventArgs e) {
            if(sim == null) return;
            sim.nextCycle();
        }

        private void btStart_Click(object sender, EventArgs e) {
            sim.startSimulation();
        }

        private void btStop_Click(object sender, EventArgs e) {
            sim.stopSimulation();
        }

        private void trackTimer_Scroll(object sender, EventArgs e) {
            if(sim == null)
                return;

            sim.setTimerInterval(trackTimer.Value);
        }

        private void rbDefault_CheckedChanged(object sender, EventArgs e) {
            sim.stopSimulation();
            trackTimer.Value = 1;
            clear();
            sim.setMod(0);
            gbDraw.Enabled = false;
            rbDrawBlue.Checked = true;
            drawRace = 0;
        }

        private void rbAdvanced_CheckedChanged(object sender, EventArgs e) {
            sim.stopSimulation();
            trackTimer.Value = 1;
            clear();
            sim = new Simulation((int)numHorizontal.Value,
                                    (int)numVertical.Value,
                                    drawBoard.Width,
                                    drawBoard.Height,
                                    graphics,
                                    this);
            sim.setMod(1);
            gbDraw.Enabled = true;
        }

        private void rbDrawBlue_CheckedChanged(object sender, EventArgs e) {
            drawRace = 0;
        }

        private void rbDrawRed_CheckedChanged(object sender, EventArgs e) {
            drawRace = 1;
        }
    }
}
