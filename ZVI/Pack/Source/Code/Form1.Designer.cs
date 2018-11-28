namespace LettersRecognition {
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
            this.panel1 = new System.Windows.Forms.Panel();
            this.btEvaluate = new System.Windows.Forms.Button();
            this.btOpenImg = new System.Windows.Forms.Button();
            this.btTrain = new System.Windows.Forms.Button();
            this.btLoad = new System.Windows.Forms.Button();
            this.lbxObjects = new System.Windows.Forms.ListBox();
            this.lbObjects = new System.Windows.Forms.Label();
            this.btDetail = new System.Windows.Forms.Button();
            this.btDilattion = new System.Windows.Forms.Button();
            this.btErosion = new System.Windows.Forms.Button();
            this.btBlur = new System.Windows.Forms.Button();
            this.btBack = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panel1.Location = new System.Drawing.Point(213, 18);
            this.panel1.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(883, 644);
            this.panel1.TabIndex = 0;
            this.panel1.Paint += new System.Windows.Forms.PaintEventHandler(this.panel1_Paint);
            this.panel1.Resize += new System.EventHandler(this.panel1_Resize);
            // 
            // btEvaluate
            // 
            this.btEvaluate.Enabled = false;
            this.btEvaluate.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btEvaluate.Location = new System.Drawing.Point(16, 288);
            this.btEvaluate.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.btEvaluate.Name = "btEvaluate";
            this.btEvaluate.Size = new System.Drawing.Size(186, 43);
            this.btEvaluate.TabIndex = 1;
            this.btEvaluate.Text = "Evaluate";
            this.btEvaluate.UseVisualStyleBackColor = true;
            this.btEvaluate.Click += new System.EventHandler(this.btEvaluate_Click);
            // 
            // btOpenImg
            // 
            this.btOpenImg.Enabled = false;
            this.btOpenImg.Location = new System.Drawing.Point(15, 88);
            this.btOpenImg.Name = "btOpenImg";
            this.btOpenImg.Size = new System.Drawing.Size(187, 43);
            this.btOpenImg.TabIndex = 0;
            this.btOpenImg.Text = "Open Image";
            this.btOpenImg.UseVisualStyleBackColor = true;
            this.btOpenImg.Click += new System.EventHandler(this.btOpenImg_Click);
            // 
            // btTrain
            // 
            this.btTrain.Location = new System.Drawing.Point(16, 18);
            this.btTrain.Name = "btTrain";
            this.btTrain.Size = new System.Drawing.Size(187, 29);
            this.btTrain.TabIndex = 2;
            this.btTrain.Text = "Train Classifier";
            this.btTrain.UseVisualStyleBackColor = true;
            this.btTrain.Click += new System.EventHandler(this.btTrain_Click);
            // 
            // btLoad
            // 
            this.btLoad.Location = new System.Drawing.Point(16, 53);
            this.btLoad.Name = "btLoad";
            this.btLoad.Size = new System.Drawing.Size(187, 29);
            this.btLoad.TabIndex = 3;
            this.btLoad.Text = "Load Classifier";
            this.btLoad.UseVisualStyleBackColor = true;
            this.btLoad.Click += new System.EventHandler(this.btLoad_Click);
            // 
            // lbxObjects
            // 
            this.lbxObjects.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left)));
            this.lbxObjects.FormattingEnabled = true;
            this.lbxObjects.ItemHeight = 20;
            this.lbxObjects.Location = new System.Drawing.Point(15, 378);
            this.lbxObjects.Name = "lbxObjects";
            this.lbxObjects.Size = new System.Drawing.Size(187, 284);
            this.lbxObjects.TabIndex = 4;
            this.lbxObjects.SelectedIndexChanged += new System.EventHandler(this.lbxObjects_SelectedIndexChanged);
            // 
            // lbObjects
            // 
            this.lbObjects.AutoSize = true;
            this.lbObjects.Location = new System.Drawing.Point(17, 339);
            this.lbObjects.Name = "lbObjects";
            this.lbObjects.Size = new System.Drawing.Size(67, 20);
            this.lbObjects.TabIndex = 0;
            this.lbObjects.Text = "Objects:";
            // 
            // btDetail
            // 
            this.btDetail.Enabled = false;
            this.btDetail.Location = new System.Drawing.Point(90, 339);
            this.btDetail.Name = "btDetail";
            this.btDetail.Size = new System.Drawing.Size(112, 33);
            this.btDetail.TabIndex = 0;
            this.btDetail.Text = "Detail";
            this.btDetail.UseVisualStyleBackColor = true;
            this.btDetail.Click += new System.EventHandler(this.btDetail_Click);
            // 
            // btDilattion
            // 
            this.btDilattion.Enabled = false;
            this.btDilattion.Location = new System.Drawing.Point(15, 181);
            this.btDilattion.Name = "btDilattion";
            this.btDilattion.Size = new System.Drawing.Size(187, 29);
            this.btDilattion.TabIndex = 5;
            this.btDilattion.Text = "Dilatation";
            this.btDilattion.UseVisualStyleBackColor = true;
            this.btDilattion.Click += new System.EventHandler(this.btDilattion_Click);
            // 
            // btErosion
            // 
            this.btErosion.Enabled = false;
            this.btErosion.Location = new System.Drawing.Point(16, 216);
            this.btErosion.Name = "btErosion";
            this.btErosion.Size = new System.Drawing.Size(187, 29);
            this.btErosion.TabIndex = 6;
            this.btErosion.Text = "Erosion";
            this.btErosion.UseVisualStyleBackColor = true;
            this.btErosion.Click += new System.EventHandler(this.btErosion_Click);
            // 
            // btBlur
            // 
            this.btBlur.Enabled = false;
            this.btBlur.Location = new System.Drawing.Point(15, 146);
            this.btBlur.Name = "btBlur";
            this.btBlur.Size = new System.Drawing.Size(187, 29);
            this.btBlur.TabIndex = 7;
            this.btBlur.Text = "Blur 5x5";
            this.btBlur.UseVisualStyleBackColor = true;
            this.btBlur.Click += new System.EventHandler(this.btBlur_Click);
            // 
            // btBack
            // 
            this.btBack.Enabled = false;
            this.btBack.Location = new System.Drawing.Point(16, 251);
            this.btBack.Name = "btBack";
            this.btBack.Size = new System.Drawing.Size(187, 29);
            this.btBack.TabIndex = 8;
            this.btBack.Text = "Step back";
            this.btBack.UseVisualStyleBackColor = true;
            this.btBack.Click += new System.EventHandler(this.btBack_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1115, 681);
            this.Controls.Add(this.btBack);
            this.Controls.Add(this.btBlur);
            this.Controls.Add(this.btErosion);
            this.Controls.Add(this.btDilattion);
            this.Controls.Add(this.btDetail);
            this.Controls.Add(this.lbObjects);
            this.Controls.Add(this.lbxObjects);
            this.Controls.Add(this.btLoad);
            this.Controls.Add(this.btTrain);
            this.Controls.Add(this.btOpenImg);
            this.Controls.Add(this.btEvaluate);
            this.Controls.Add(this.panel1);
            this.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.Name = "Form1";
            this.Text = "Form1";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button btEvaluate;
        private System.Windows.Forms.Button btOpenImg;
        private System.Windows.Forms.Button btTrain;
        private System.Windows.Forms.Button btLoad;
        private System.Windows.Forms.ListBox lbxObjects;
        private System.Windows.Forms.Label lbObjects;
        private System.Windows.Forms.Button btDetail;
        private System.Windows.Forms.Button btDilattion;
        private System.Windows.Forms.Button btErosion;
        private System.Windows.Forms.Button btBlur;
        private System.Windows.Forms.Button btBack;
    }
}

