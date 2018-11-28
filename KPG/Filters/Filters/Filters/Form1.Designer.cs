namespace Filters {
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
            this.btLoadImage = new System.Windows.Forms.Button();
            this.btFilter1 = new System.Windows.Forms.Button();
            this.btSquare = new System.Windows.Forms.Button();
            this.btGauss = new System.Windows.Forms.Button();
            this.btSquareM = new System.Windows.Forms.Button();
            this.btGaussM = new System.Windows.Forms.Button();
            this.btMotionM = new System.Windows.Forms.Button();
            this.btSharp = new System.Windows.Forms.Button();
            this.button1 = new System.Windows.Forms.Button();
            this.btExpo = new System.Windows.Forms.Button();
            this.btBack = new System.Windows.Forms.Button();
            this.btOrig = new System.Windows.Forms.Button();
            this.btSave = new System.Windows.Forms.Button();
            this.btEdgeVertical = new System.Windows.Forms.Button();
            this.btMess = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // drawBoard
            // 
            this.drawBoard.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.drawBoard.Location = new System.Drawing.Point(272, 18);
            this.drawBoard.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.drawBoard.Name = "drawBoard";
            this.drawBoard.Size = new System.Drawing.Size(1068, 674);
            this.drawBoard.TabIndex = 0;
            this.drawBoard.Paint += new System.Windows.Forms.PaintEventHandler(this.drawBoard_Paint);
            this.drawBoard.Resize += new System.EventHandler(this.drawBoard_Resize);
            // 
            // btLoadImage
            // 
            this.btLoadImage.Location = new System.Drawing.Point(18, 18);
            this.btLoadImage.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btLoadImage.Name = "btLoadImage";
            this.btLoadImage.Size = new System.Drawing.Size(244, 35);
            this.btLoadImage.TabIndex = 0;
            this.btLoadImage.Text = "Load Image";
            this.btLoadImage.UseVisualStyleBackColor = true;
            this.btLoadImage.Click += new System.EventHandler(this.btLoadImage_Click);
            // 
            // btFilter1
            // 
            this.btFilter1.Enabled = false;
            this.btFilter1.Location = new System.Drawing.Point(13, 468);
            this.btFilter1.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btFilter1.Name = "btFilter1";
            this.btFilter1.Size = new System.Drawing.Size(201, 35);
            this.btFilter1.TabIndex = 0;
            this.btFilter1.Text = "Embos";
            this.btFilter1.UseVisualStyleBackColor = true;
            this.btFilter1.Click += new System.EventHandler(this.btEmbos_Click);
            // 
            // btSquare
            // 
            this.btSquare.Enabled = false;
            this.btSquare.Location = new System.Drawing.Point(18, 63);
            this.btSquare.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btSquare.Name = "btSquare";
            this.btSquare.Size = new System.Drawing.Size(201, 35);
            this.btSquare.TabIndex = 0;
            this.btSquare.Text = "Square Blur 3x3";
            this.btSquare.UseVisualStyleBackColor = true;
            this.btSquare.Click += new System.EventHandler(this.btSquare_Click);
            // 
            // btGauss
            // 
            this.btGauss.Enabled = false;
            this.btGauss.Location = new System.Drawing.Point(18, 153);
            this.btGauss.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btGauss.Name = "btGauss";
            this.btGauss.Size = new System.Drawing.Size(201, 35);
            this.btGauss.TabIndex = 0;
            this.btGauss.Text = "Gaussian Blur 3x3";
            this.btGauss.UseVisualStyleBackColor = true;
            this.btGauss.Click += new System.EventHandler(this.btGauss_Click);
            // 
            // btSquareM
            // 
            this.btSquareM.Enabled = false;
            this.btSquareM.Location = new System.Drawing.Point(18, 108);
            this.btSquareM.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btSquareM.Name = "btSquareM";
            this.btSquareM.Size = new System.Drawing.Size(201, 35);
            this.btSquareM.TabIndex = 0;
            this.btSquareM.Text = "Square Blur 5x5";
            this.btSquareM.UseVisualStyleBackColor = true;
            this.btSquareM.Click += new System.EventHandler(this.btSquareM_Click);
            // 
            // btGaussM
            // 
            this.btGaussM.Enabled = false;
            this.btGaussM.Location = new System.Drawing.Point(18, 198);
            this.btGaussM.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btGaussM.Name = "btGaussM";
            this.btGaussM.Size = new System.Drawing.Size(201, 35);
            this.btGaussM.TabIndex = 0;
            this.btGaussM.Text = "Gaussian Blur 5x5";
            this.btGaussM.UseVisualStyleBackColor = true;
            this.btGaussM.Click += new System.EventHandler(this.btGaussM_Click);
            // 
            // btMotionM
            // 
            this.btMotionM.Enabled = false;
            this.btMotionM.Location = new System.Drawing.Point(13, 243);
            this.btMotionM.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btMotionM.Name = "btMotionM";
            this.btMotionM.Size = new System.Drawing.Size(201, 35);
            this.btMotionM.TabIndex = 0;
            this.btMotionM.Text = "Motion Blur 5x5";
            this.btMotionM.UseVisualStyleBackColor = true;
            this.btMotionM.Click += new System.EventHandler(this.btMotionM_Click);
            // 
            // btSharp
            // 
            this.btSharp.Enabled = false;
            this.btSharp.Location = new System.Drawing.Point(18, 288);
            this.btSharp.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btSharp.Name = "btSharp";
            this.btSharp.Size = new System.Drawing.Size(201, 35);
            this.btSharp.TabIndex = 0;
            this.btSharp.Text = "Edge 5x5";
            this.btSharp.UseVisualStyleBackColor = true;
            this.btSharp.Click += new System.EventHandler(this.btSharp_Click);
            // 
            // button1
            // 
            this.button1.Enabled = false;
            this.button1.Location = new System.Drawing.Point(18, 333);
            this.button1.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(201, 35);
            this.button1.TabIndex = 0;
            this.button1.Text = "Edge 3x3";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // btExpo
            // 
            this.btExpo.Enabled = false;
            this.btExpo.Location = new System.Drawing.Point(13, 423);
            this.btExpo.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btExpo.Name = "btExpo";
            this.btExpo.Size = new System.Drawing.Size(201, 35);
            this.btExpo.TabIndex = 0;
            this.btExpo.Text = "MyExposure";
            this.btExpo.UseVisualStyleBackColor = true;
            this.btExpo.Click += new System.EventHandler(this.btExpo_Click);
            // 
            // btBack
            // 
            this.btBack.Enabled = false;
            this.btBack.Location = new System.Drawing.Point(13, 566);
            this.btBack.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btBack.Name = "btBack";
            this.btBack.Size = new System.Drawing.Size(244, 35);
            this.btBack.TabIndex = 0;
            this.btBack.Text = "Back";
            this.btBack.UseVisualStyleBackColor = true;
            this.btBack.Click += new System.EventHandler(this.btBack_Click);
            // 
            // btOrig
            // 
            this.btOrig.Enabled = false;
            this.btOrig.Location = new System.Drawing.Point(13, 611);
            this.btOrig.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btOrig.Name = "btOrig";
            this.btOrig.Size = new System.Drawing.Size(244, 35);
            this.btOrig.TabIndex = 0;
            this.btOrig.Text = "Original";
            this.btOrig.UseVisualStyleBackColor = true;
            this.btOrig.Click += new System.EventHandler(this.button2_Click);
            // 
            // btSave
            // 
            this.btSave.Enabled = false;
            this.btSave.Location = new System.Drawing.Point(13, 656);
            this.btSave.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btSave.Name = "btSave";
            this.btSave.Size = new System.Drawing.Size(244, 35);
            this.btSave.TabIndex = 1;
            this.btSave.Text = "Save";
            this.btSave.UseVisualStyleBackColor = true;
            this.btSave.Click += new System.EventHandler(this.btSave_Click_1);
            // 
            // btEdgeVertical
            // 
            this.btEdgeVertical.Enabled = false;
            this.btEdgeVertical.Location = new System.Drawing.Point(18, 378);
            this.btEdgeVertical.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btEdgeVertical.Name = "btEdgeVertical";
            this.btEdgeVertical.Size = new System.Drawing.Size(201, 35);
            this.btEdgeVertical.TabIndex = 2;
            this.btEdgeVertical.Text = "Edge 3x3 Vertical";
            this.btEdgeVertical.UseVisualStyleBackColor = true;
            this.btEdgeVertical.Click += new System.EventHandler(this.btEdgeVertical_Click);
            // 
            // btMess
            // 
            this.btMess.Enabled = false;
            this.btMess.Location = new System.Drawing.Point(13, 513);
            this.btMess.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btMess.Name = "btMess";
            this.btMess.Size = new System.Drawing.Size(201, 35);
            this.btMess.TabIndex = 3;
            this.btMess.Text = "Mess";
            this.btMess.UseVisualStyleBackColor = true;
            this.btMess.Click += new System.EventHandler(this.btMess_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1358, 711);
            this.Controls.Add(this.btMess);
            this.Controls.Add(this.btEdgeVertical);
            this.Controls.Add(this.btLoadImage);
            this.Controls.Add(this.btSave);
            this.Controls.Add(this.btOrig);
            this.Controls.Add(this.btBack);
            this.Controls.Add(this.btExpo);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.btSharp);
            this.Controls.Add(this.btMotionM);
            this.Controls.Add(this.btGaussM);
            this.Controls.Add(this.btSquareM);
            this.Controls.Add(this.btGauss);
            this.Controls.Add(this.btSquare);
            this.Controls.Add(this.btFilter1);
            this.Controls.Add(this.drawBoard);
            this.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.Name = "Form1";
            this.Text = "Form1";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel drawBoard;
        private System.Windows.Forms.Button btLoadImage;
        private System.Windows.Forms.Button btFilter1;
        private System.Windows.Forms.Button btSquare;
        private System.Windows.Forms.Button btGauss;
        private System.Windows.Forms.Button btSquareM;
        private System.Windows.Forms.Button btGaussM;
        private System.Windows.Forms.Button btMotionM;
        private System.Windows.Forms.Button btSharp;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button btExpo;
        private System.Windows.Forms.Button btBack;
        private System.Windows.Forms.Button btOrig;
        private System.Windows.Forms.Button btSave;
        private System.Windows.Forms.Button btEdgeVertical;
        private System.Windows.Forms.Button btMess;
    }
}

