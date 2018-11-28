namespace LettersRecognition {
    partial class FragmentDetailForm {
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
            this.lbInfoTitle = new System.Windows.Forms.Label();
            this.txInfo = new System.Windows.Forms.TextBox();
            this.btDrawMinRectangle = new System.Windows.Forms.Button();
            this.btOriginal = new System.Windows.Forms.Button();
            this.btShowEdge = new System.Windows.Forms.Button();
            this.btAvgOrigin = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panel1.Location = new System.Drawing.Point(348, 14);
            this.panel1.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(836, 610);
            this.panel1.TabIndex = 1;
            this.panel1.Paint += new System.Windows.Forms.PaintEventHandler(this.panel1_Paint);
            this.panel1.Resize += new System.EventHandler(this.panel1_Resize);
            // 
            // lbInfoTitle
            // 
            this.lbInfoTitle.AutoSize = true;
            this.lbInfoTitle.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbInfoTitle.Location = new System.Drawing.Point(13, 166);
            this.lbInfoTitle.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.lbInfoTitle.Name = "lbInfoTitle";
            this.lbInfoTitle.Size = new System.Drawing.Size(49, 25);
            this.lbInfoTitle.TabIndex = 2;
            this.lbInfoTitle.Text = "info:";
            // 
            // txInfo
            // 
            this.txInfo.AcceptsReturn = true;
            this.txInfo.AcceptsTab = true;
            this.txInfo.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left)));
            this.txInfo.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.txInfo.Location = new System.Drawing.Point(18, 196);
            this.txInfo.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.txInfo.Multiline = true;
            this.txInfo.Name = "txInfo";
            this.txInfo.ReadOnly = true;
            this.txInfo.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.txInfo.Size = new System.Drawing.Size(306, 428);
            this.txInfo.TabIndex = 5;
            // 
            // btDrawMinRectangle
            // 
            this.btDrawMinRectangle.Location = new System.Drawing.Point(21, 49);
            this.btDrawMinRectangle.Name = "btDrawMinRectangle";
            this.btDrawMinRectangle.Size = new System.Drawing.Size(303, 29);
            this.btDrawMinRectangle.TabIndex = 6;
            this.btDrawMinRectangle.Text = "Show Minimal Rectangle";
            this.btDrawMinRectangle.UseVisualStyleBackColor = true;
            this.btDrawMinRectangle.Click += new System.EventHandler(this.btDrawMinRectangle_Click);
            // 
            // btOriginal
            // 
            this.btOriginal.Location = new System.Drawing.Point(21, 14);
            this.btOriginal.Name = "btOriginal";
            this.btOriginal.Size = new System.Drawing.Size(303, 29);
            this.btOriginal.TabIndex = 7;
            this.btOriginal.Text = "Show Original";
            this.btOriginal.UseVisualStyleBackColor = true;
            this.btOriginal.Click += new System.EventHandler(this.btOriginal_Click);
            // 
            // btShowEdge
            // 
            this.btShowEdge.Location = new System.Drawing.Point(21, 84);
            this.btShowEdge.Name = "btShowEdge";
            this.btShowEdge.Size = new System.Drawing.Size(303, 29);
            this.btShowEdge.TabIndex = 8;
            this.btShowEdge.Text = "Show Edge";
            this.btShowEdge.UseVisualStyleBackColor = true;
            this.btShowEdge.Click += new System.EventHandler(this.btShowEdge_Click);
            // 
            // btAvgOrigin
            // 
            this.btAvgOrigin.Location = new System.Drawing.Point(21, 119);
            this.btAvgOrigin.Name = "btAvgOrigin";
            this.btAvgOrigin.Size = new System.Drawing.Size(303, 29);
            this.btAvgOrigin.TabIndex = 9;
            this.btAvgOrigin.Text = "Show Center of Gravity";
            this.btAvgOrigin.UseVisualStyleBackColor = true;
            this.btAvgOrigin.Click += new System.EventHandler(this.btAvgOrigin_Click);
            // 
            // FragmentDetailForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1197, 638);
            this.Controls.Add(this.btAvgOrigin);
            this.Controls.Add(this.btShowEdge);
            this.Controls.Add(this.btOriginal);
            this.Controls.Add(this.btDrawMinRectangle);
            this.Controls.Add(this.lbInfoTitle);
            this.Controls.Add(this.txInfo);
            this.Controls.Add(this.panel1);
            this.Name = "FragmentDetailForm";
            this.Text = "FragmentDetailForm";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label lbInfoTitle;
        private System.Windows.Forms.TextBox txInfo;
        private System.Windows.Forms.Button btDrawMinRectangle;
        private System.Windows.Forms.Button btOriginal;
        private System.Windows.Forms.Button btShowEdge;
        private System.Windows.Forms.Button btAvgOrigin;
    }
}