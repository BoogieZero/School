using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

namespace PictureKey
{
	/// <summary>
	/// Zusammendfassende Beschreibung für ImagesDialog.
	/// </summary>
	public class ImageFilesDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.ListView lvImages;
		private System.Windows.Forms.ColumnHeader clmPixels;
		private System.Windows.Forms.Panel panel1;
		private System.Windows.Forms.Splitter splitter1;
		private System.Windows.Forms.Splitter splitter2;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.Button btnAdd;
		private System.Windows.Forms.Button btnOk;
		private System.Windows.Forms.GroupBox grpAddImage;
		private System.Windows.Forms.TextBox txtImageFile;
		private System.Windows.Forms.Button btnImageFile;
		private System.Windows.Forms.Button btnCancel;
		private System.Windows.Forms.PictureBox picSelectedImage;
		private System.Windows.Forms.TextBox txtDstFile;
		private System.Windows.Forms.Button btnDstFile;
		private System.Windows.Forms.ColumnHeader clmSrcFileName;
		private System.Windows.Forms.ColumnHeader clmDstFileName;
		private System.Windows.Forms.ColumnHeader clmGrayscale;
		private System.Windows.Forms.Label lblDstFile;
		/// <summary>
		/// Erforderliche Designervariable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		private bool showSaveAsFields;

		public ImageFilesDialog(CarrierImage[] initialImages, bool showSaveAsFields){
			//
			// Erforderlich für die Windows Form-Designerunterstützung
			//
			InitializeComponent();

			//adapt ListView
			this.showSaveAsFields = showSaveAsFields;
			if( ! showSaveAsFields){
				lvImages.Columns.Remove(clmDstFileName);
				lvImages.Columns.Remove(clmPixels);
				lvImages.Columns.Remove(clmGrayscale);
				lvImages.CheckBoxes = false;
				clmSrcFileName.Width = lvImages.Width - 10;
				lblDstFile.Enabled = txtDstFile.Enabled = btnDstFile.Enabled = false;
			}

			//list initial items
			ListViewItem item;
			foreach(CarrierImage file in initialImages){
				if(showSaveAsFields){
					item = new ListViewItem(
						new String[4]{ String.Empty, file.sourceFileName, file.resultFileName, file.countPixels.ToString() }
						);
					item.Checked = file.useGrayscale;
				}else{
					item = new ListViewItem(
						new String[1]{ file.sourceFileName }
						);
				}
				item.Tag = file;
				lvImages.Items.Add(item);
			}

		}

		/// <summary>
		/// Die verwendeten Ressourcen bereinigen.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if(components != null)
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Erforderliche Methode für die Designerunterstützung. 
		/// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
		/// </summary>
		private void InitializeComponent()
		{
			this.lvImages = new System.Windows.Forms.ListView();
			this.clmGrayscale = new System.Windows.Forms.ColumnHeader();
			this.clmSrcFileName = new System.Windows.Forms.ColumnHeader();
			this.clmDstFileName = new System.Windows.Forms.ColumnHeader();
			this.clmPixels = new System.Windows.Forms.ColumnHeader();
			this.panel1 = new System.Windows.Forms.Panel();
			this.btnOk = new System.Windows.Forms.Button();
			this.btnCancel = new System.Windows.Forms.Button();
			this.grpAddImage = new System.Windows.Forms.GroupBox();
			this.txtDstFile = new System.Windows.Forms.TextBox();
			this.btnDstFile = new System.Windows.Forms.Button();
			this.lblDstFile = new System.Windows.Forms.Label();
			this.txtImageFile = new System.Windows.Forms.TextBox();
			this.btnImageFile = new System.Windows.Forms.Button();
			this.label1 = new System.Windows.Forms.Label();
			this.btnAdd = new System.Windows.Forms.Button();
			this.splitter1 = new System.Windows.Forms.Splitter();
			this.splitter2 = new System.Windows.Forms.Splitter();
			this.picSelectedImage = new System.Windows.Forms.PictureBox();
			this.panel1.SuspendLayout();
			this.grpAddImage.SuspendLayout();
			this.SuspendLayout();
			// 
			// lvImages
			// 
			this.lvImages.CheckBoxes = true;
			this.lvImages.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
																					   this.clmGrayscale,
																					   this.clmSrcFileName,
																					   this.clmDstFileName,
																					   this.clmPixels});
			this.lvImages.Dock = System.Windows.Forms.DockStyle.Fill;
			this.lvImages.FullRowSelect = true;
			this.lvImages.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
			this.lvImages.MultiSelect = false;
			this.lvImages.Name = "lvImages";
			this.lvImages.Size = new System.Drawing.Size(584, 268);
			this.lvImages.TabIndex = 0;
			this.lvImages.View = System.Windows.Forms.View.Details;
			this.lvImages.KeyDown += new System.Windows.Forms.KeyEventHandler(this.lvImages_KeyDown);
			this.lvImages.SelectedIndexChanged += new System.EventHandler(this.lvImages_SelectedIndexChanged);
			// 
			// clmGrayscale
			// 
			this.clmGrayscale.Text = "Grayscale noise";
			this.clmGrayscale.Width = 120;
			// 
			// clmSrcFileName
			// 
			this.clmSrcFileName.Text = "Image file";
			this.clmSrcFileName.Width = 200;
			// 
			// clmDstFileName
			// 
			this.clmDstFileName.Text = "Save result as";
			this.clmDstFileName.Width = 200;
			// 
			// clmPixels
			// 
			this.clmPixels.Text = "Pixels";
			// 
			// panel1
			// 
			this.panel1.Controls.AddRange(new System.Windows.Forms.Control[] {
																				 this.btnOk,
																				 this.btnCancel,
																				 this.grpAddImage});
			this.panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.panel1.Location = new System.Drawing.Point(0, 271);
			this.panel1.Name = "panel1";
			this.panel1.Size = new System.Drawing.Size(912, 192);
			this.panel1.TabIndex = 1;
			// 
			// btnOk
			// 
			this.btnOk.Location = new System.Drawing.Point(728, 160);
			this.btnOk.Name = "btnOk";
			this.btnOk.Size = new System.Drawing.Size(80, 23);
			this.btnOk.TabIndex = 8;
			this.btnOk.Text = "OK";
			this.btnOk.Click += new System.EventHandler(this.btnOk_Click);
			// 
			// btnCancel
			// 
			this.btnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.btnCancel.Location = new System.Drawing.Point(816, 160);
			this.btnCancel.Name = "btnCancel";
			this.btnCancel.Size = new System.Drawing.Size(80, 23);
			this.btnCancel.TabIndex = 9;
			this.btnCancel.Text = "Cancel";
			this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
			// 
			// grpAddImage
			// 
			this.grpAddImage.Controls.AddRange(new System.Windows.Forms.Control[] {
																					  this.txtDstFile,
																					  this.btnDstFile,
																					  this.lblDstFile,
																					  this.txtImageFile,
																					  this.btnImageFile,
																					  this.label1,
																					  this.btnAdd});
			this.grpAddImage.Location = new System.Drawing.Point(16, 16);
			this.grpAddImage.Name = "grpAddImage";
			this.grpAddImage.Size = new System.Drawing.Size(880, 128);
			this.grpAddImage.TabIndex = 6;
			this.grpAddImage.TabStop = false;
			this.grpAddImage.Text = "Add Image";
			// 
			// txtDstFile
			// 
			this.txtDstFile.Location = new System.Drawing.Point(112, 64);
			this.txtDstFile.Name = "txtDstFile";
			this.txtDstFile.Size = new System.Drawing.Size(512, 22);
			this.txtDstFile.TabIndex = 2;
			this.txtDstFile.Text = "";
			// 
			// btnDstFile
			// 
			this.btnDstFile.Location = new System.Drawing.Point(624, 64);
			this.btnDstFile.Name = "btnDstFile";
			this.btnDstFile.TabIndex = 3;
			this.btnDstFile.Text = "Browse...";
			this.btnDstFile.Click += new System.EventHandler(this.btnDstFile_Click);
			// 
			// lblDstFile
			// 
			this.lblDstFile.Location = new System.Drawing.Point(16, 64);
			this.lblDstFile.Name = "lblDstFile";
			this.lblDstFile.Size = new System.Drawing.Size(96, 23);
			this.lblDstFile.TabIndex = 6;
			this.lblDstFile.Text = "Save result as";
			// 
			// txtImageFile
			// 
			this.txtImageFile.Location = new System.Drawing.Point(112, 32);
			this.txtImageFile.Name = "txtImageFile";
			this.txtImageFile.Size = new System.Drawing.Size(512, 22);
			this.txtImageFile.TabIndex = 0;
			this.txtImageFile.Text = "";
			// 
			// btnImageFile
			// 
			this.btnImageFile.Location = new System.Drawing.Point(624, 32);
			this.btnImageFile.Name = "btnImageFile";
			this.btnImageFile.TabIndex = 1;
			this.btnImageFile.Text = "Browse...";
			this.btnImageFile.Click += new System.EventHandler(this.btnImageFile_Click);
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(16, 32);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(96, 23);
			this.label1.TabIndex = 1;
			this.label1.Text = "Source file";
			// 
			// btnAdd
			// 
			this.btnAdd.Location = new System.Drawing.Point(504, 96);
			this.btnAdd.Name = "btnAdd";
			this.btnAdd.Size = new System.Drawing.Size(192, 23);
			this.btnAdd.TabIndex = 4;
			this.btnAdd.Text = "Add to image files";
			this.btnAdd.Click += new System.EventHandler(this.btnAdd_Click);
			// 
			// splitter1
			// 
			this.splitter1.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.splitter1.Location = new System.Drawing.Point(0, 268);
			this.splitter1.Name = "splitter1";
			this.splitter1.Size = new System.Drawing.Size(912, 3);
			this.splitter1.TabIndex = 2;
			this.splitter1.TabStop = false;
			// 
			// splitter2
			// 
			this.splitter2.Dock = System.Windows.Forms.DockStyle.Right;
			this.splitter2.Location = new System.Drawing.Point(581, 0);
			this.splitter2.Name = "splitter2";
			this.splitter2.Size = new System.Drawing.Size(3, 268);
			this.splitter2.TabIndex = 3;
			this.splitter2.TabStop = false;
			// 
			// picSelectedImage
			// 
			this.picSelectedImage.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
			this.picSelectedImage.Dock = System.Windows.Forms.DockStyle.Right;
			this.picSelectedImage.Location = new System.Drawing.Point(584, 0);
			this.picSelectedImage.Name = "picSelectedImage";
			this.picSelectedImage.Size = new System.Drawing.Size(328, 268);
			this.picSelectedImage.TabIndex = 4;
			this.picSelectedImage.TabStop = false;
			// 
			// ImageFilesDialog
			// 
			this.AcceptButton = this.btnAdd;
			this.AutoScaleBaseSize = new System.Drawing.Size(6, 15);
			this.CancelButton = this.btnCancel;
			this.ClientSize = new System.Drawing.Size(912, 463);
			this.ControlBox = false;
			this.Controls.AddRange(new System.Windows.Forms.Control[] {
																		  this.splitter2,
																		  this.lvImages,
																		  this.picSelectedImage,
																		  this.splitter1,
																		  this.panel1});
			this.Name = "ImageFilesDialog";
			this.Text = "Manage Carrier Images";
			this.panel1.ResumeLayout(false);
			this.grpAddImage.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		public CarrierImage[] GetImages(){
			CarrierImage[] result = new CarrierImage[lvImages.Items.Count];
			for(int n=0; n<lvImages.Items.Count; n++){
				result[n] = (CarrierImage)lvImages.Items[n].Tag;
				result[n].useGrayscale = lvImages.Items[n].Checked;
			}
			return result;
		}
		
		private long DisplayBitmap(String fileName){
			long countPixels = 0;
			if( ! fileName.ToLower().EndsWith(".avi")){
				Bitmap bmp = new Bitmap(fileName);
				countPixels = bmp.Width * bmp.Height;
				picSelectedImage.Image = new Bitmap(bmp);
				bmp.Dispose();
			}
			return countPixels;
		}

		private void btnAdd_Click(object sender, System.EventArgs e) {
			if(txtImageFile.Text.Length > 0){
				if( System.IO.File.Exists(txtImageFile.Text) ){

					//Check the files are already listed
					bool isOkay = true;
					CarrierImage ci;
					foreach(ListViewItem existingItem in lvImages.Items){
						ci = (CarrierImage)existingItem.Tag;
						if((ci.sourceFileName==txtImageFile.Text) || (ci.resultFileName ==txtImageFile.Text)){
							MessageBox.Show("The source file is already in use.");
							isOkay = false;
							existingItem.Selected = true;
							break;
						}else if((txtDstFile.Text.Length > 0)&&(ci.resultFileName==txtDstFile.Text)){
							MessageBox.Show("The destination file is already in use.");
							isOkay = false;
							existingItem.Selected = true;
							break;
						}
					}

					long countPixels = 0;
					int aviCountFrames = 0;

					if(isOkay){
						if(txtImageFile.Text.ToLower().EndsWith(".avi")){
							//Video - get stream info
							AviReader ar = new AviReader();
							ar.Open(txtImageFile.Text);
							Size size = ar.BitmapSize;
							aviCountFrames = ar.CountFrames;
							countPixels = size.Width * size.Height * aviCountFrames;
							ar.Close();
						}else{
							//Bitmap - display preview
							countPixels = DisplayBitmap(txtImageFile.Text);
						}
						ListViewItem item;
						if(showSaveAsFields){
							//Manage empty carrier images, configure result file names
							item = new ListViewItem(
								new String[4]{ String.Empty, txtImageFile.Text, txtDstFile.Text, countPixels.ToString() }
								);
						}else{
							//Manage carrier images for extraction, there are no results to save, count of pixels is not interesting
							item = new ListViewItem(
								new String[1]{ txtImageFile.Text }
								);
						}
						item.Tag = new CarrierImage(txtImageFile.Text, txtDstFile.Text, countPixels, aviCountFrames, true);
						item.Checked = true;
						lvImages.Items.Add(item);
					}

				}else{
					MessageBox.Show("File "+txtImageFile.Text+" not found");
				}
			}	
		}

		private void lvImages_SelectedIndexChanged(object sender, System.EventArgs e) {
			if(lvImages.SelectedItems.Count > 0){
				int itemIndex = (showSaveAsFields) ? 1 : 0;
				DisplayBitmap(lvImages.SelectedItems[0].SubItems[itemIndex].Text);
			}
		}

		private void btnOk_Click(object sender, System.EventArgs e) {
			this.DialogResult = DialogResult.OK;
			this.Close();
		}

		private void btnCancel_Click(object sender, System.EventArgs e) {
			this.DialogResult = DialogResult.Cancel;
			this.Close();
		}

		private void btnImageFile_Click(object sender, System.EventArgs e) {
			OpenFileDialog dlg = new OpenFileDialog();
			dlg.Filter = "Bitmaps (*.bmp)|*.bmp|Tagged Image File Format (*.tif)|*.tif|PNG-24 (*.png)|*.png|Video (*.avi)|*.avi";
			dlg.Multiselect = false;
			if( dlg.ShowDialog(this) != DialogResult.Cancel){
				txtImageFile.Text = dlg.FileName;
				if(dlg.FileName.ToLower().EndsWith(".avi")){
					if(txtDstFile.Text.Length > 0){ //destination already selected
						if( ! txtDstFile.Text.ToLower().EndsWith(".avi") ){ //destination must be avi
							MessageBox.Show("AVI videos can only be saved to AVI videos.");
							int index = txtDstFile.Text.IndexOf(".");
							txtDstFile.Text = txtDstFile.Text.Substring(0, index) + ".avi";
						}
					}
				}
			}
		}

		private void btnDstFile_Click(object sender, System.EventArgs e) {
			SaveFileDialog dlg = new SaveFileDialog();
			if(txtImageFile.Text.ToLower().EndsWith(".avi")){
				dlg.Filter = "Video (*.avi)|*.avi";
			}else{
				dlg.Filter = "Bitmaps (*.bmp)|*.bmp|Tagged Image File Format (*.tif)|*.tif|PNG-24 (*.png)|*.png";
			}
			if( dlg.ShowDialog() == DialogResult.OK ){
				txtDstFile.Text = dlg.FileName;
			}
		}

		private void lvImages_KeyDown(object sender, System.Windows.Forms.KeyEventArgs e) {
			if((e.KeyCode == Keys.Delete)&&(lvImages.SelectedItems.Count==1)){
				lvImages.Items.Remove(lvImages.SelectedItems[0]);
			}
		}
	}
}
