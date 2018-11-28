namespace Bludiste {
    partial class Form1 {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
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
            this.Plane = new System.Windows.Forms.Panel();
            this.button1 = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.txHorizontal = new System.Windows.Forms.TextBox();
            this.txVertical = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.chbGradual = new System.Windows.Forms.CheckBox();
            this.numDensity = new System.Windows.Forms.NumericUpDown();
            this.label4 = new System.Windows.Forms.Label();
            this.btPlay = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.numDensity)).BeginInit();
            this.SuspendLayout();
            // 
            // Plane
            // 
            this.Plane.Location = new System.Drawing.Point(151, 12);
            this.Plane.Name = "Plane";
            this.Plane.Size = new System.Drawing.Size(795, 508);
            this.Plane.TabIndex = 0;
            this.Plane.Paint += new System.Windows.Forms.PaintEventHandler(this.Plane_Paint);
            // 
            // button1
            // 
            this.button1.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.button1.Location = new System.Drawing.Point(24, 187);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(106, 33);
            this.button1.TabIndex = 0;
            this.button1.Text = "Create";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(11, 43);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(71, 16);
            this.label1.TabIndex = 0;
            this.label1.Text = "Horizontal:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(24, 72);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(56, 16);
            this.label2.TabIndex = 0;
            this.label2.Text = "Vertical:";
            // 
            // txHorizontal
            // 
            this.txHorizontal.Location = new System.Drawing.Point(88, 43);
            this.txHorizontal.Name = "txHorizontal";
            this.txHorizontal.Size = new System.Drawing.Size(57, 20);
            this.txHorizontal.TabIndex = 0;
            // 
            // txVertical
            // 
            this.txVertical.Location = new System.Drawing.Point(88, 72);
            this.txVertical.Name = "txVertical";
            this.txVertical.Size = new System.Drawing.Size(57, 20);
            this.txVertical.TabIndex = 0;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(34, 20);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(96, 20);
            this.label3.TabIndex = 0;
            this.label3.Text = "Dimensions:";
            // 
            // chbGradual
            // 
            this.chbGradual.AutoSize = true;
            this.chbGradual.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.chbGradual.Location = new System.Drawing.Point(45, 161);
            this.chbGradual.Name = "chbGradual";
            this.chbGradual.Size = new System.Drawing.Size(100, 20);
            this.chbGradual.TabIndex = 1;
            this.chbGradual.Text = "Gradual plot";
            this.chbGradual.UseVisualStyleBackColor = true;
            this.chbGradual.CheckedChanged += new System.EventHandler(this.chbGradual_CheckedChanged);
            // 
            // numDensity
            // 
            this.numDensity.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.numDensity.Location = new System.Drawing.Point(12, 133);
            this.numDensity.Maximum = new decimal(new int[] {
            1000,
            0,
            0,
            0});
            this.numDensity.Name = "numDensity";
            this.numDensity.Size = new System.Drawing.Size(133, 22);
            this.numDensity.TabIndex = 0;
            this.numDensity.ValueChanged += new System.EventHandler(this.numDensity_ValueChanged);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(11, 117);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(131, 13);
            this.label4.TabIndex = 0;
            this.label4.Text = "Intensity of random opens:";
            // 
            // btPlay
            // 
            this.btPlay.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btPlay.Location = new System.Drawing.Point(24, 239);
            this.btPlay.Name = "btPlay";
            this.btPlay.Size = new System.Drawing.Size(106, 33);
            this.btPlay.TabIndex = 0;
            this.btPlay.Text = "Play";
            this.btPlay.UseVisualStyleBackColor = true;
            this.btPlay.Click += new System.EventHandler(this.btPlay_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(958, 532);
            this.Controls.Add(this.btPlay);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.numDensity);
            this.Controls.Add(this.chbGradual);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.txVertical);
            this.Controls.Add(this.txHorizontal);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.Plane);
            this.KeyPreview = true;
            this.Name = "Form1";
            this.Text = "Form1";
            this.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Form1_KeyPress);
            ((System.ComponentModel.ISupportInitialize)(this.numDensity)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel Plane;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox txHorizontal;
        private System.Windows.Forms.TextBox txVertical;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.CheckBox chbGradual;
        private System.Windows.Forms.NumericUpDown numDensity;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button btPlay;
    }
}

