namespace Transformations {
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
            this.rbTranslation = new System.Windows.Forms.RadioButton();
            this.rbScale = new System.Windows.Forms.RadioButton();
            this.rbMirror = new System.Windows.Forms.RadioButton();
            this.rbBevel = new System.Windows.Forms.RadioButton();
            this.rbRotation = new System.Windows.Forms.RadioButton();
            this.gbMatrix = new System.Windows.Forms.GroupBox();
            this.btTransform = new System.Windows.Forms.Button();
            this.textBox9 = new System.Windows.Forms.TextBox();
            this.textBox8 = new System.Windows.Forms.TextBox();
            this.textBox7 = new System.Windows.Forms.TextBox();
            this.textBox6 = new System.Windows.Forms.TextBox();
            this.textBox5 = new System.Windows.Forms.TextBox();
            this.textBox4 = new System.Windows.Forms.TextBox();
            this.textBox3 = new System.Windows.Forms.TextBox();
            this.textBox2 = new System.Windows.Forms.TextBox();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.gbConvert = new System.Windows.Forms.GroupBox();
            this.label1 = new System.Windows.Forms.Label();
            this.txDegree = new System.Windows.Forms.TextBox();
            this.txCos = new System.Windows.Forms.TextBox();
            this.txSin = new System.Windows.Forms.TextBox();
            this.btConvert = new System.Windows.Forms.Button();
            this.chbManual = new System.Windows.Forms.CheckBox();
            this.chbStackTransform = new System.Windows.Forms.CheckBox();
            this.gbMatrix.SuspendLayout();
            this.gbConvert.SuspendLayout();
            this.SuspendLayout();
            // 
            // drawBoard
            // 
            this.drawBoard.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.drawBoard.Location = new System.Drawing.Point(248, 12);
            this.drawBoard.Name = "drawBoard";
            this.drawBoard.Size = new System.Drawing.Size(410, 303);
            this.drawBoard.TabIndex = 0;
            this.drawBoard.Paint += new System.Windows.Forms.PaintEventHandler(this.drawBoard_Paint);
            this.drawBoard.Resize += new System.EventHandler(this.drawBoard_Resize);
            // 
            // rbTranslation
            // 
            this.rbTranslation.AutoSize = true;
            this.rbTranslation.Checked = true;
            this.rbTranslation.Location = new System.Drawing.Point(12, 22);
            this.rbTranslation.Name = "rbTranslation";
            this.rbTranslation.Size = new System.Drawing.Size(72, 17);
            this.rbTranslation.TabIndex = 0;
            this.rbTranslation.TabStop = true;
            this.rbTranslation.Text = "Translace";
            this.rbTranslation.UseVisualStyleBackColor = true;
            this.rbTranslation.CheckedChanged += new System.EventHandler(this.rbTranslation_CheckedChanged);
            // 
            // rbScale
            // 
            this.rbScale.AutoSize = true;
            this.rbScale.Location = new System.Drawing.Point(12, 45);
            this.rbScale.Name = "rbScale";
            this.rbScale.Size = new System.Drawing.Size(52, 17);
            this.rbScale.TabIndex = 1;
            this.rbScale.Text = "Scale";
            this.rbScale.UseVisualStyleBackColor = true;
            this.rbScale.CheckedChanged += new System.EventHandler(this.rbScale_CheckedChanged);
            // 
            // rbMirror
            // 
            this.rbMirror.AutoSize = true;
            this.rbMirror.Location = new System.Drawing.Point(12, 68);
            this.rbMirror.Name = "rbMirror";
            this.rbMirror.Size = new System.Drawing.Size(113, 17);
            this.rbMirror.TabIndex = 2;
            this.rbMirror.Text = "Osová soumernost";
            this.rbMirror.UseVisualStyleBackColor = true;
            this.rbMirror.CheckedChanged += new System.EventHandler(this.rbMirror_CheckedChanged);
            // 
            // rbBevel
            // 
            this.rbBevel.AutoSize = true;
            this.rbBevel.Location = new System.Drawing.Point(12, 91);
            this.rbBevel.Name = "rbBevel";
            this.rbBevel.Size = new System.Drawing.Size(65, 17);
            this.rbBevel.TabIndex = 3;
            this.rbBevel.Text = "Zkosení";
            this.rbBevel.UseVisualStyleBackColor = true;
            this.rbBevel.CheckedChanged += new System.EventHandler(this.rbBevel_CheckedChanged);
            // 
            // rbRotation
            // 
            this.rbRotation.AutoSize = true;
            this.rbRotation.Location = new System.Drawing.Point(12, 114);
            this.rbRotation.Name = "rbRotation";
            this.rbRotation.Size = new System.Drawing.Size(60, 17);
            this.rbRotation.TabIndex = 4;
            this.rbRotation.Text = "Rotace";
            this.rbRotation.UseVisualStyleBackColor = true;
            this.rbRotation.CheckedChanged += new System.EventHandler(this.rbRotation_CheckedChanged);
            // 
            // gbMatrix
            // 
            this.gbMatrix.Controls.Add(this.btTransform);
            this.gbMatrix.Controls.Add(this.textBox9);
            this.gbMatrix.Controls.Add(this.textBox8);
            this.gbMatrix.Controls.Add(this.textBox7);
            this.gbMatrix.Controls.Add(this.textBox6);
            this.gbMatrix.Controls.Add(this.textBox5);
            this.gbMatrix.Controls.Add(this.textBox4);
            this.gbMatrix.Controls.Add(this.textBox3);
            this.gbMatrix.Controls.Add(this.textBox2);
            this.gbMatrix.Controls.Add(this.textBox1);
            this.gbMatrix.Location = new System.Drawing.Point(12, 147);
            this.gbMatrix.Name = "gbMatrix";
            this.gbMatrix.Size = new System.Drawing.Size(156, 131);
            this.gbMatrix.TabIndex = 0;
            this.gbMatrix.TabStop = false;
            this.gbMatrix.Text = "Matice";
            // 
            // btTransform
            // 
            this.btTransform.Location = new System.Drawing.Point(13, 97);
            this.btTransform.Name = "btTransform";
            this.btTransform.Size = new System.Drawing.Size(129, 23);
            this.btTransform.TabIndex = 0;
            this.btTransform.Text = "Transofmuj";
            this.btTransform.UseVisualStyleBackColor = true;
            this.btTransform.Click += new System.EventHandler(this.btTransform_Click);
            // 
            // textBox9
            // 
            this.textBox9.Enabled = false;
            this.textBox9.Location = new System.Drawing.Point(103, 71);
            this.textBox9.Name = "textBox9";
            this.textBox9.Size = new System.Drawing.Size(39, 20);
            this.textBox9.TabIndex = 8;
            this.textBox9.Text = "1";
            // 
            // textBox8
            // 
            this.textBox8.Location = new System.Drawing.Point(58, 71);
            this.textBox8.Name = "textBox8";
            this.textBox8.Size = new System.Drawing.Size(39, 20);
            this.textBox8.TabIndex = 7;
            // 
            // textBox7
            // 
            this.textBox7.Location = new System.Drawing.Point(13, 71);
            this.textBox7.Name = "textBox7";
            this.textBox7.Size = new System.Drawing.Size(39, 20);
            this.textBox7.TabIndex = 6;
            // 
            // textBox6
            // 
            this.textBox6.Enabled = false;
            this.textBox6.Location = new System.Drawing.Point(103, 45);
            this.textBox6.Name = "textBox6";
            this.textBox6.Size = new System.Drawing.Size(39, 20);
            this.textBox6.TabIndex = 5;
            this.textBox6.Text = "0";
            // 
            // textBox5
            // 
            this.textBox5.Location = new System.Drawing.Point(58, 45);
            this.textBox5.Name = "textBox5";
            this.textBox5.Size = new System.Drawing.Size(39, 20);
            this.textBox5.TabIndex = 4;
            // 
            // textBox4
            // 
            this.textBox4.Location = new System.Drawing.Point(13, 45);
            this.textBox4.Name = "textBox4";
            this.textBox4.Size = new System.Drawing.Size(39, 20);
            this.textBox4.TabIndex = 3;
            // 
            // textBox3
            // 
            this.textBox3.Enabled = false;
            this.textBox3.Location = new System.Drawing.Point(103, 19);
            this.textBox3.Name = "textBox3";
            this.textBox3.Size = new System.Drawing.Size(39, 20);
            this.textBox3.TabIndex = 2;
            this.textBox3.Text = "0";
            // 
            // textBox2
            // 
            this.textBox2.Location = new System.Drawing.Point(58, 19);
            this.textBox2.Name = "textBox2";
            this.textBox2.Size = new System.Drawing.Size(39, 20);
            this.textBox2.TabIndex = 1;
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(13, 19);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(39, 20);
            this.textBox1.TabIndex = 0;
            // 
            // gbConvert
            // 
            this.gbConvert.Controls.Add(this.label1);
            this.gbConvert.Controls.Add(this.txDegree);
            this.gbConvert.Controls.Add(this.txCos);
            this.gbConvert.Controls.Add(this.txSin);
            this.gbConvert.Controls.Add(this.btConvert);
            this.gbConvert.Location = new System.Drawing.Point(131, 12);
            this.gbConvert.Name = "gbConvert";
            this.gbConvert.Size = new System.Drawing.Size(111, 129);
            this.gbConvert.TabIndex = 0;
            this.gbConvert.TabStop = false;
            this.gbConvert.Text = "Převod stupňů";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 22);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(39, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "stupně";
            // 
            // txDegree
            // 
            this.txDegree.Location = new System.Drawing.Point(42, 19);
            this.txDegree.Name = "txDegree";
            this.txDegree.Size = new System.Drawing.Size(62, 20);
            this.txDegree.TabIndex = 0;
            // 
            // txCos
            // 
            this.txCos.Location = new System.Drawing.Point(6, 102);
            this.txCos.Name = "txCos";
            this.txCos.Size = new System.Drawing.Size(100, 20);
            this.txCos.TabIndex = 1;
            // 
            // txSin
            // 
            this.txSin.Location = new System.Drawing.Point(6, 76);
            this.txSin.Name = "txSin";
            this.txSin.Size = new System.Drawing.Size(100, 20);
            this.txSin.TabIndex = 0;
            // 
            // btConvert
            // 
            this.btConvert.Enabled = false;
            this.btConvert.Location = new System.Drawing.Point(6, 45);
            this.btConvert.Name = "btConvert";
            this.btConvert.Size = new System.Drawing.Size(98, 25);
            this.btConvert.TabIndex = 0;
            this.btConvert.Text = "Převeď";
            this.btConvert.UseVisualStyleBackColor = true;
            this.btConvert.Click += new System.EventHandler(this.btConvert_Click);
            // 
            // chbManual
            // 
            this.chbManual.AutoSize = true;
            this.chbManual.Location = new System.Drawing.Point(12, 284);
            this.chbManual.Name = "chbManual";
            this.chbManual.Size = new System.Drawing.Size(139, 17);
            this.chbManual.TabIndex = 0;
            this.chbManual.Text = "Manual Transofrmations";
            this.chbManual.UseVisualStyleBackColor = true;
            // 
            // chbStackTransform
            // 
            this.chbStackTransform.AutoSize = true;
            this.chbStackTransform.Location = new System.Drawing.Point(12, 307);
            this.chbStackTransform.Name = "chbStackTransform";
            this.chbStackTransform.Size = new System.Drawing.Size(131, 17);
            this.chbStackTransform.TabIndex = 0;
            this.chbStackTransform.Text = "Skládání transofrmací";
            this.chbStackTransform.UseVisualStyleBackColor = true;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(670, 327);
            this.Controls.Add(this.chbStackTransform);
            this.Controls.Add(this.chbManual);
            this.Controls.Add(this.gbConvert);
            this.Controls.Add(this.gbMatrix);
            this.Controls.Add(this.rbRotation);
            this.Controls.Add(this.rbBevel);
            this.Controls.Add(this.rbMirror);
            this.Controls.Add(this.rbScale);
            this.Controls.Add(this.rbTranslation);
            this.Controls.Add(this.drawBoard);
            this.Name = "Form1";
            this.Text = "Transformations";
            this.gbMatrix.ResumeLayout(false);
            this.gbMatrix.PerformLayout();
            this.gbConvert.ResumeLayout(false);
            this.gbConvert.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel drawBoard;
        private System.Windows.Forms.RadioButton rbTranslation;
        private System.Windows.Forms.RadioButton rbScale;
        private System.Windows.Forms.RadioButton rbMirror;
        private System.Windows.Forms.RadioButton rbBevel;
        private System.Windows.Forms.RadioButton rbRotation;
        private System.Windows.Forms.GroupBox gbMatrix;
        private System.Windows.Forms.Button btTransform;
        private System.Windows.Forms.TextBox textBox9;
        private System.Windows.Forms.TextBox textBox8;
        private System.Windows.Forms.TextBox textBox7;
        private System.Windows.Forms.TextBox textBox6;
        private System.Windows.Forms.TextBox textBox5;
        private System.Windows.Forms.TextBox textBox4;
        private System.Windows.Forms.TextBox textBox3;
        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.GroupBox gbConvert;
        private System.Windows.Forms.TextBox txCos;
        private System.Windows.Forms.TextBox txSin;
        private System.Windows.Forms.Button btConvert;
        private System.Windows.Forms.CheckBox chbManual;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txDegree;
        private System.Windows.Forms.CheckBox chbStackTransform;
    }
}

