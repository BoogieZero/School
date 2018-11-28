namespace GameLife {
    partial class MainWindow {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if(disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            this.drawBoard = new System.Windows.Forms.Panel();
            this.btStart = new System.Windows.Forms.Button();
            this.btCreate = new System.Windows.Forms.Button();
            this.numHorizontal = new System.Windows.Forms.NumericUpDown();
            this.numVertical = new System.Windows.Forms.NumericUpDown();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btNextCycle = new System.Windows.Forms.Button();
            this.btStop = new System.Windows.Forms.Button();
            this.trackTimer = new System.Windows.Forms.TrackBar();
            this.label3 = new System.Windows.Forms.Label();
            this.gbSimulation = new System.Windows.Forms.GroupBox();
            this.rbDefault = new System.Windows.Forms.RadioButton();
            this.rbAdvanced = new System.Windows.Forms.RadioButton();
            this.gbSimulationParameters = new System.Windows.Forms.GroupBox();
            this.rbDrawBlue = new System.Windows.Forms.RadioButton();
            this.rbDrawRed = new System.Windows.Forms.RadioButton();
            this.gbDraw = new System.Windows.Forms.GroupBox();
            ((System.ComponentModel.ISupportInitialize)(this.numHorizontal)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.numVertical)).BeginInit();
            this.groupBox1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.trackTimer)).BeginInit();
            this.gbSimulation.SuspendLayout();
            this.gbSimulationParameters.SuspendLayout();
            this.gbDraw.SuspendLayout();
            this.SuspendLayout();
            // 
            // drawBoard
            // 
            this.drawBoard.Location = new System.Drawing.Point(191, 12);
            this.drawBoard.Name = "drawBoard";
            this.drawBoard.Size = new System.Drawing.Size(674, 456);
            this.drawBoard.TabIndex = 0;
            this.drawBoard.Paint += new System.Windows.Forms.PaintEventHandler(this.drawBoard_Paint);
            this.drawBoard.MouseDown += new System.Windows.Forms.MouseEventHandler(this.drawBoard_MouseDown);
            this.drawBoard.MouseMove += new System.Windows.Forms.MouseEventHandler(this.drawBoard_MouseMove);
            this.drawBoard.MouseUp += new System.Windows.Forms.MouseEventHandler(this.drawBoard_MouseUp);
            // 
            // btStart
            // 
            this.btStart.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btStart.Location = new System.Drawing.Point(6, 50);
            this.btStart.Name = "btStart";
            this.btStart.Size = new System.Drawing.Size(161, 32);
            this.btStart.TabIndex = 0;
            this.btStart.Text = "Start / Continue";
            this.btStart.UseVisualStyleBackColor = true;
            this.btStart.Click += new System.EventHandler(this.btStart_Click);
            // 
            // btCreate
            // 
            this.btCreate.Location = new System.Drawing.Point(12, 96);
            this.btCreate.Name = "btCreate";
            this.btCreate.Size = new System.Drawing.Size(173, 23);
            this.btCreate.TabIndex = 0;
            this.btCreate.Text = "Create Map";
            this.btCreate.UseVisualStyleBackColor = true;
            this.btCreate.Click += new System.EventHandler(this.btCreate_Click);
            // 
            // numHorizontal
            // 
            this.numHorizontal.Location = new System.Drawing.Point(116, 21);
            this.numHorizontal.Maximum = new decimal(new int[] {
            500,
            0,
            0,
            0});
            this.numHorizontal.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            0});
            this.numHorizontal.Name = "numHorizontal";
            this.numHorizontal.Size = new System.Drawing.Size(51, 22);
            this.numHorizontal.TabIndex = 0;
            this.numHorizontal.Value = new decimal(new int[] {
            1,
            0,
            0,
            0});
            // 
            // numVertical
            // 
            this.numVertical.Location = new System.Drawing.Point(116, 49);
            this.numVertical.Maximum = new decimal(new int[] {
            338,
            0,
            0,
            0});
            this.numVertical.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            0});
            this.numVertical.Name = "numVertical";
            this.numVertical.Size = new System.Drawing.Size(51, 22);
            this.numVertical.TabIndex = 0;
            this.numVertical.Value = new decimal(new int[] {
            1,
            0,
            0,
            0});
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(15, 23);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(71, 16);
            this.label1.TabIndex = 1;
            this.label1.Text = "Horizontal:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(15, 49);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(56, 16);
            this.label2.TabIndex = 0;
            this.label2.Text = "Vertical:";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.numHorizontal);
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Controls.Add(this.numVertical);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.groupBox1.Location = new System.Drawing.Point(12, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(173, 78);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Dimensons";
            // 
            // btNextCycle
            // 
            this.btNextCycle.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btNextCycle.Location = new System.Drawing.Point(92, 19);
            this.btNextCycle.Name = "btNextCycle";
            this.btNextCycle.Size = new System.Drawing.Size(75, 25);
            this.btNextCycle.TabIndex = 0;
            this.btNextCycle.Text = "Next Cycle";
            this.btNextCycle.UseVisualStyleBackColor = true;
            this.btNextCycle.Click += new System.EventHandler(this.btNextCycle_Click);
            // 
            // btStop
            // 
            this.btStop.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btStop.Location = new System.Drawing.Point(6, 88);
            this.btStop.Name = "btStop";
            this.btStop.Size = new System.Drawing.Size(161, 32);
            this.btStop.TabIndex = 1;
            this.btStop.Text = "Stop";
            this.btStop.UseVisualStyleBackColor = true;
            this.btStop.Click += new System.EventHandler(this.btStop_Click);
            // 
            // trackTimer
            // 
            this.trackTimer.Location = new System.Drawing.Point(75, 133);
            this.trackTimer.Maximum = 500;
            this.trackTimer.Minimum = 1;
            this.trackTimer.Name = "trackTimer";
            this.trackTimer.Size = new System.Drawing.Size(92, 45);
            this.trackTimer.TabIndex = 0;
            this.trackTimer.Value = 1;
            this.trackTimer.Scroll += new System.EventHandler(this.trackTimer_Scroll);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(15, 133);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(52, 16);
            this.label3.TabIndex = 0;
            this.label3.Text = "Speed:";
            // 
            // gbSimulation
            // 
            this.gbSimulation.Controls.Add(this.btNextCycle);
            this.gbSimulation.Controls.Add(this.label3);
            this.gbSimulation.Controls.Add(this.btStart);
            this.gbSimulation.Controls.Add(this.trackTimer);
            this.gbSimulation.Controls.Add(this.btStop);
            this.gbSimulation.Enabled = false;
            this.gbSimulation.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.gbSimulation.Location = new System.Drawing.Point(12, 290);
            this.gbSimulation.Name = "gbSimulation";
            this.gbSimulation.Size = new System.Drawing.Size(173, 178);
            this.gbSimulation.TabIndex = 0;
            this.gbSimulation.TabStop = false;
            this.gbSimulation.Text = "Simulation";
            // 
            // rbDefault
            // 
            this.rbDefault.AutoSize = true;
            this.rbDefault.Checked = true;
            this.rbDefault.Location = new System.Drawing.Point(6, 19);
            this.rbDefault.Name = "rbDefault";
            this.rbDefault.Size = new System.Drawing.Size(68, 20);
            this.rbDefault.TabIndex = 2;
            this.rbDefault.TabStop = true;
            this.rbDefault.Text = "Default";
            this.rbDefault.UseVisualStyleBackColor = true;
            this.rbDefault.CheckedChanged += new System.EventHandler(this.rbDefault_CheckedChanged);
            // 
            // rbAdvanced
            // 
            this.rbAdvanced.AutoSize = true;
            this.rbAdvanced.Location = new System.Drawing.Point(79, 19);
            this.rbAdvanced.Name = "rbAdvanced";
            this.rbAdvanced.Size = new System.Drawing.Size(88, 20);
            this.rbAdvanced.TabIndex = 3;
            this.rbAdvanced.Text = "Advanced";
            this.rbAdvanced.UseVisualStyleBackColor = true;
            this.rbAdvanced.CheckedChanged += new System.EventHandler(this.rbAdvanced_CheckedChanged);
            // 
            // gbSimulationParameters
            // 
            this.gbSimulationParameters.Controls.Add(this.gbDraw);
            this.gbSimulationParameters.Controls.Add(this.rbAdvanced);
            this.gbSimulationParameters.Controls.Add(this.rbDefault);
            this.gbSimulationParameters.Enabled = false;
            this.gbSimulationParameters.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.gbSimulationParameters.Location = new System.Drawing.Point(12, 125);
            this.gbSimulationParameters.Name = "gbSimulationParameters";
            this.gbSimulationParameters.Size = new System.Drawing.Size(173, 159);
            this.gbSimulationParameters.TabIndex = 0;
            this.gbSimulationParameters.TabStop = false;
            this.gbSimulationParameters.Text = "Simulation parameters";
            // 
            // rbDrawBlue
            // 
            this.rbDrawBlue.AutoSize = true;
            this.rbDrawBlue.Checked = true;
            this.rbDrawBlue.Location = new System.Drawing.Point(15, 17);
            this.rbDrawBlue.Name = "rbDrawBlue";
            this.rbDrawBlue.Size = new System.Drawing.Size(53, 20);
            this.rbDrawBlue.TabIndex = 4;
            this.rbDrawBlue.TabStop = true;
            this.rbDrawBlue.Text = "Blue";
            this.rbDrawBlue.UseVisualStyleBackColor = false;
            this.rbDrawBlue.CheckedChanged += new System.EventHandler(this.rbDrawBlue_CheckedChanged);
            // 
            // rbDrawRed
            // 
            this.rbDrawRed.AutoSize = true;
            this.rbDrawRed.Location = new System.Drawing.Point(76, 17);
            this.rbDrawRed.Name = "rbDrawRed";
            this.rbDrawRed.Size = new System.Drawing.Size(52, 20);
            this.rbDrawRed.TabIndex = 5;
            this.rbDrawRed.Text = "Red";
            this.rbDrawRed.UseVisualStyleBackColor = true;
            this.rbDrawRed.CheckedChanged += new System.EventHandler(this.rbDrawRed_CheckedChanged);
            // 
            // gbDraw
            // 
            this.gbDraw.Controls.Add(this.rbDrawRed);
            this.gbDraw.Controls.Add(this.rbDrawBlue);
            this.gbDraw.Enabled = false;
            this.gbDraw.Location = new System.Drawing.Point(3, 45);
            this.gbDraw.Name = "gbDraw";
            this.gbDraw.Size = new System.Drawing.Size(164, 43);
            this.gbDraw.TabIndex = 0;
            this.gbDraw.TabStop = false;
            this.gbDraw.Text = "Draw";
            // 
            // MainWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(878, 480);
            this.Controls.Add(this.gbSimulationParameters);
            this.Controls.Add(this.gbSimulation);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.btCreate);
            this.Controls.Add(this.drawBoard);
            this.DoubleBuffered = true;
            this.Name = "MainWindow";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this.numHorizontal)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.numVertical)).EndInit();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.trackTimer)).EndInit();
            this.gbSimulation.ResumeLayout(false);
            this.gbSimulation.PerformLayout();
            this.gbSimulationParameters.ResumeLayout(false);
            this.gbSimulationParameters.PerformLayout();
            this.gbDraw.ResumeLayout(false);
            this.gbDraw.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel drawBoard;
        private System.Windows.Forms.Button btStart;
        private System.Windows.Forms.Button btCreate;
        private System.Windows.Forms.NumericUpDown numHorizontal;
        private System.Windows.Forms.NumericUpDown numVertical;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button btNextCycle;
        private System.Windows.Forms.Button btStop;
        private System.Windows.Forms.TrackBar trackTimer;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.GroupBox gbSimulation;
        private System.Windows.Forms.RadioButton rbDefault;
        private System.Windows.Forms.RadioButton rbAdvanced;
        private System.Windows.Forms.GroupBox gbSimulationParameters;
        private System.Windows.Forms.RadioButton rbDrawRed;
        private System.Windows.Forms.RadioButton rbDrawBlue;
        private System.Windows.Forms.GroupBox gbDraw;
    }
}

