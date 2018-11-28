using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Data;
using System.IO;
using System.Text;

namespace PictureKey
{
	public class frmMain : System.Windows.Forms.Form
	{
		private System.Windows.Forms.GroupBox grpPicture;
		private System.Windows.Forms.GroupBox grpKey;
		private System.Windows.Forms.Button btnHide;
		private System.Windows.Forms.Button btnExtract;
		private System.Windows.Forms.TabPage tabPage1;
		private System.Windows.Forms.TabPage tabPage2;
		private System.Windows.Forms.GroupBox grpMessage;
		private System.Windows.Forms.RadioButton rdoMessageText;
		private System.Windows.Forms.TextBox txtMessageFile;
		private System.Windows.Forms.TextBox txtMessageText;
		private System.Windows.Forms.Button btnMessage;
		private System.Windows.Forms.RadioButton rdoMessageFile;
		private System.Windows.Forms.GroupBox groupBox3;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Button btnKeyFile;
		private System.Windows.Forms.TextBox txtExtractedMsgFile;
		private System.Windows.Forms.Button btnExtractedMsgFile;
		private System.Windows.Forms.TextBox txtExtractedMsgText;
		private System.Windows.Forms.Label lblKeyFiles;
		private System.ComponentModel.Container components = null;
		private System.Windows.Forms.Button btnImageFile;
		private System.Windows.Forms.Label lblImageFiles;
		private System.Windows.Forms.GroupBox grpPictureExtract;
		private System.Windows.Forms.Button btntImageFileExtract;
		private System.Windows.Forms.TabControl tabAction;
		private System.Windows.Forms.Label lblImageFilesExtract;
		private System.Windows.Forms.GroupBox grpSplitBytes;
		private System.Windows.Forms.CheckBox chkSplitBytes;

		private FilePasswordPair[] keys = new FilePasswordPair[0]; //{new FilePasswordPair(@"C:\Projekte\C\PictureKey_3\testdaten\text1.txt", "a")};
		private CarrierImage[] imagesHide = new CarrierImage[0]; //{new CarrierImage(@"C:\projekte\c\picturekey_3\testdaten\white.bmp", @"C:\projekte\c\picturekey_3\testdaten\result.bmp", 90000, true)};
		private CarrierImage[] imagesExtract = 	new CarrierImage[0]; //{new CarrierImage(@"C:\projekte\c\picturekey_3\testdaten\result.bmp", "", 90000, true)};
		
		public frmMain(){
			InitializeComponent();
		}

		/// <summary>Cleanup</summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if (components != null) 
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
			this.grpPicture = new System.Windows.Forms.GroupBox();
			this.lblImageFiles = new System.Windows.Forms.Label();
			this.btnImageFile = new System.Windows.Forms.Button();
			this.grpKey = new System.Windows.Forms.GroupBox();
			this.lblKeyFiles = new System.Windows.Forms.Label();
			this.btnKeyFile = new System.Windows.Forms.Button();
			this.btnHide = new System.Windows.Forms.Button();
			this.btnExtract = new System.Windows.Forms.Button();
			this.tabAction = new System.Windows.Forms.TabControl();
			this.tabPage1 = new System.Windows.Forms.TabPage();
			this.grpMessage = new System.Windows.Forms.GroupBox();
			this.rdoMessageText = new System.Windows.Forms.RadioButton();
			this.txtMessageFile = new System.Windows.Forms.TextBox();
			this.txtMessageText = new System.Windows.Forms.TextBox();
			this.btnMessage = new System.Windows.Forms.Button();
			this.rdoMessageFile = new System.Windows.Forms.RadioButton();
			this.tabPage2 = new System.Windows.Forms.TabPage();
			this.grpPictureExtract = new System.Windows.Forms.GroupBox();
			this.lblImageFilesExtract = new System.Windows.Forms.Label();
			this.btntImageFileExtract = new System.Windows.Forms.Button();
			this.groupBox3 = new System.Windows.Forms.GroupBox();
			this.label1 = new System.Windows.Forms.Label();
			this.txtExtractedMsgFile = new System.Windows.Forms.TextBox();
			this.txtExtractedMsgText = new System.Windows.Forms.TextBox();
			this.btnExtractedMsgFile = new System.Windows.Forms.Button();
			this.label3 = new System.Windows.Forms.Label();
			this.grpSplitBytes = new System.Windows.Forms.GroupBox();
			this.chkSplitBytes = new System.Windows.Forms.CheckBox();
			this.grpPicture.SuspendLayout();
			this.grpKey.SuspendLayout();
			this.tabAction.SuspendLayout();
			this.tabPage1.SuspendLayout();
			this.grpMessage.SuspendLayout();
			this.tabPage2.SuspendLayout();
			this.grpPictureExtract.SuspendLayout();
			this.groupBox3.SuspendLayout();
			this.grpSplitBytes.SuspendLayout();
			this.SuspendLayout();
			// 
			// grpPicture
			// 
			this.grpPicture.Controls.AddRange(new System.Windows.Forms.Control[] {
																					 this.lblImageFiles,
																					 this.btnImageFile});
			this.grpPicture.Location = new System.Drawing.Point(456, 16);
			this.grpPicture.Name = "grpPicture";
			this.grpPicture.Size = new System.Drawing.Size(312, 184);
			this.grpPicture.TabIndex = 0;
			this.grpPicture.TabStop = false;
			this.grpPicture.Text = "Carrier Bitmaps";
			// 
			// lblImageFiles
			// 
			this.lblImageFiles.Location = new System.Drawing.Point(16, 32);
			this.lblImageFiles.Name = "lblImageFiles";
			this.lblImageFiles.Size = new System.Drawing.Size(168, 23);
			this.lblImageFiles.TabIndex = 3;
			this.lblImageFiles.Text = "No image files specified";
			// 
			// btnImageFile
			// 
			this.btnImageFile.Location = new System.Drawing.Point(184, 32);
			this.btnImageFile.Name = "btnImageFile";
			this.btnImageFile.Size = new System.Drawing.Size(112, 23);
			this.btnImageFile.TabIndex = 2;
			this.btnImageFile.Text = "Add/Remove...";
			this.btnImageFile.Click += new System.EventHandler(this.btnImageFile_Click);
			// 
			// grpKey
			// 
			this.grpKey.Controls.AddRange(new System.Windows.Forms.Control[] {
																				 this.lblKeyFiles,
																				 this.btnKeyFile});
			this.grpKey.Location = new System.Drawing.Point(8, 16);
			this.grpKey.Name = "grpKey";
			this.grpKey.Size = new System.Drawing.Size(792, 72);
			this.grpKey.TabIndex = 1;
			this.grpKey.TabStop = false;
			this.grpKey.Text = "Keys";
			// 
			// lblKeyFiles
			// 
			this.lblKeyFiles.Location = new System.Drawing.Point(16, 32);
			this.lblKeyFiles.Name = "lblKeyFiles";
			this.lblKeyFiles.Size = new System.Drawing.Size(152, 23);
			this.lblKeyFiles.TabIndex = 3;
			this.lblKeyFiles.Text = "No key files specified";
			// 
			// btnKeyFile
			// 
			this.btnKeyFile.Location = new System.Drawing.Point(168, 32);
			this.btnKeyFile.Name = "btnKeyFile";
			this.btnKeyFile.Size = new System.Drawing.Size(112, 23);
			this.btnKeyFile.TabIndex = 2;
			this.btnKeyFile.Text = "Add/Remove...";
			this.btnKeyFile.Click += new System.EventHandler(this.btnKeyFile_Click);
			// 
			// btnHide
			// 
			this.btnHide.Enabled = false;
			this.btnHide.Location = new System.Drawing.Point(608, 216);
			this.btnHide.Name = "btnHide";
			this.btnHide.Size = new System.Drawing.Size(160, 23);
			this.btnHide.TabIndex = 2;
			this.btnHide.Text = "Hide Message";
			this.btnHide.Click += new System.EventHandler(this.btnHide_Click);
			// 
			// btnExtract
			// 
			this.btnExtract.Enabled = false;
			this.btnExtract.Location = new System.Drawing.Point(608, 216);
			this.btnExtract.Name = "btnExtract";
			this.btnExtract.Size = new System.Drawing.Size(160, 23);
			this.btnExtract.TabIndex = 2;
			this.btnExtract.Text = "Extract Hidden Text";
			this.btnExtract.Click += new System.EventHandler(this.btnExtract_Click);
			// 
			// tabAction
			// 
			this.tabAction.Controls.AddRange(new System.Windows.Forms.Control[] {
																					this.tabPage1,
																					this.tabPage2});
			this.tabAction.Location = new System.Drawing.Point(8, 184);
			this.tabAction.Name = "tabAction";
			this.tabAction.SelectedIndex = 0;
			this.tabAction.Size = new System.Drawing.Size(792, 280);
			this.tabAction.TabIndex = 2;
			// 
			// tabPage1
			// 
			this.tabPage1.Controls.AddRange(new System.Windows.Forms.Control[] {
																				   this.grpMessage,
																				   this.btnHide,
																				   this.grpPicture});
			this.tabPage1.Location = new System.Drawing.Point(4, 25);
			this.tabPage1.Name = "tabPage1";
			this.tabPage1.Size = new System.Drawing.Size(784, 251);
			this.tabPage1.TabIndex = 0;
			this.tabPage1.Text = "Hide";
			// 
			// grpMessage
			// 
			this.grpMessage.Controls.AddRange(new System.Windows.Forms.Control[] {
																					 this.rdoMessageText,
																					 this.txtMessageFile,
																					 this.txtMessageText,
																					 this.btnMessage,
																					 this.rdoMessageFile});
			this.grpMessage.Location = new System.Drawing.Point(16, 16);
			this.grpMessage.Name = "grpMessage";
			this.grpMessage.Size = new System.Drawing.Size(424, 184);
			this.grpMessage.TabIndex = 0;
			this.grpMessage.TabStop = false;
			this.grpMessage.Text = "Message";
			// 
			// rdoMessageText
			// 
			this.rdoMessageText.Checked = true;
			this.rdoMessageText.Location = new System.Drawing.Point(16, 48);
			this.rdoMessageText.Name = "rdoMessageText";
			this.rdoMessageText.Size = new System.Drawing.Size(72, 24);
			this.rdoMessageText.TabIndex = 3;
			this.rdoMessageText.TabStop = true;
			this.rdoMessageText.Text = "Text";
			this.rdoMessageText.Click += new System.EventHandler(this.rdoMessage_Click);
			// 
			// txtMessageFile
			// 
			this.txtMessageFile.Location = new System.Drawing.Point(104, 24);
			this.txtMessageFile.Name = "txtMessageFile";
			this.txtMessageFile.Size = new System.Drawing.Size(232, 22);
			this.txtMessageFile.TabIndex = 1;
			this.txtMessageFile.Text = "";
			this.txtMessageFile.Enter += new System.EventHandler(this.txtMessageFile_Enter);
			// 
			// txtMessageText
			// 
			this.txtMessageText.Location = new System.Drawing.Point(16, 72);
			this.txtMessageText.Multiline = true;
			this.txtMessageText.Name = "txtMessageText";
			this.txtMessageText.Size = new System.Drawing.Size(400, 96);
			this.txtMessageText.TabIndex = 4;
			this.txtMessageText.Text = "";
			this.txtMessageText.Enter += new System.EventHandler(this.txtMessageText_Enter);
			// 
			// btnMessage
			// 
			this.btnMessage.Location = new System.Drawing.Point(336, 24);
			this.btnMessage.Name = "btnMessage";
			this.btnMessage.Size = new System.Drawing.Size(80, 23);
			this.btnMessage.TabIndex = 2;
			this.btnMessage.Text = "Browse...";
			this.btnMessage.Click += new System.EventHandler(this.btnMessage_Click);
			// 
			// rdoMessageFile
			// 
			this.rdoMessageFile.Location = new System.Drawing.Point(16, 24);
			this.rdoMessageFile.Name = "rdoMessageFile";
			this.rdoMessageFile.Size = new System.Drawing.Size(88, 24);
			this.rdoMessageFile.TabIndex = 0;
			this.rdoMessageFile.Text = "Filename";
			this.rdoMessageFile.Click += new System.EventHandler(this.rdoMessage_Click);
			// 
			// tabPage2
			// 
			this.tabPage2.Controls.AddRange(new System.Windows.Forms.Control[] {
																				   this.grpPictureExtract,
																				   this.groupBox3,
																				   this.btnExtract});
			this.tabPage2.Location = new System.Drawing.Point(4, 25);
			this.tabPage2.Name = "tabPage2";
			this.tabPage2.Size = new System.Drawing.Size(784, 251);
			this.tabPage2.TabIndex = 1;
			this.tabPage2.Text = "Extract";
			// 
			// grpPictureExtract
			// 
			this.grpPictureExtract.Controls.AddRange(new System.Windows.Forms.Control[] {
																							this.lblImageFilesExtract,
																							this.btntImageFileExtract});
			this.grpPictureExtract.Location = new System.Drawing.Point(456, 16);
			this.grpPictureExtract.Name = "grpPictureExtract";
			this.grpPictureExtract.Size = new System.Drawing.Size(312, 184);
			this.grpPictureExtract.TabIndex = 3;
			this.grpPictureExtract.TabStop = false;
			this.grpPictureExtract.Text = "Carrier Bitmaps";
			// 
			// lblImageFilesExtract
			// 
			this.lblImageFilesExtract.Location = new System.Drawing.Point(16, 32);
			this.lblImageFilesExtract.Name = "lblImageFilesExtract";
			this.lblImageFilesExtract.Size = new System.Drawing.Size(168, 23);
			this.lblImageFilesExtract.TabIndex = 3;
			this.lblImageFilesExtract.Text = "No image files specified";
			// 
			// btntImageFileExtract
			// 
			this.btntImageFileExtract.Location = new System.Drawing.Point(184, 32);
			this.btntImageFileExtract.Name = "btntImageFileExtract";
			this.btntImageFileExtract.Size = new System.Drawing.Size(112, 23);
			this.btntImageFileExtract.TabIndex = 2;
			this.btntImageFileExtract.Text = "Add/Remove...";
			this.btntImageFileExtract.Click += new System.EventHandler(this.btnImageFile_Click);
			// 
			// groupBox3
			// 
			this.groupBox3.Controls.AddRange(new System.Windows.Forms.Control[] {
																					this.label1,
																					this.txtExtractedMsgFile,
																					this.txtExtractedMsgText,
																					this.btnExtractedMsgFile,
																					this.label3});
			this.groupBox3.Location = new System.Drawing.Point(16, 16);
			this.groupBox3.Name = "groupBox3";
			this.groupBox3.Size = new System.Drawing.Size(424, 184);
			this.groupBox3.TabIndex = 0;
			this.groupBox3.TabStop = false;
			this.groupBox3.Text = "Message";
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(16, 32);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(376, 16);
			this.label1.TabIndex = 10;
			this.label1.Text = "Save Extracted Message to File";
			// 
			// txtExtractedMsgFile
			// 
			this.txtExtractedMsgFile.Location = new System.Drawing.Point(16, 48);
			this.txtExtractedMsgFile.Name = "txtExtractedMsgFile";
			this.txtExtractedMsgFile.Size = new System.Drawing.Size(312, 22);
			this.txtExtractedMsgFile.TabIndex = 0;
			this.txtExtractedMsgFile.Text = "";
			// 
			// txtExtractedMsgText
			// 
			this.txtExtractedMsgText.Location = new System.Drawing.Point(16, 96);
			this.txtExtractedMsgText.Multiline = true;
			this.txtExtractedMsgText.Name = "txtExtractedMsgText";
			this.txtExtractedMsgText.ReadOnly = true;
			this.txtExtractedMsgText.Size = new System.Drawing.Size(392, 72);
			this.txtExtractedMsgText.TabIndex = 5;
			this.txtExtractedMsgText.Text = "";
			// 
			// btnExtractedMsgFile
			// 
			this.btnExtractedMsgFile.Location = new System.Drawing.Point(328, 48);
			this.btnExtractedMsgFile.Name = "btnExtractedMsgFile";
			this.btnExtractedMsgFile.Size = new System.Drawing.Size(80, 23);
			this.btnExtractedMsgFile.TabIndex = 1;
			this.btnExtractedMsgFile.Text = "Browse...";
			this.btnExtractedMsgFile.Click += new System.EventHandler(this.btnExtractedMsgFile_Click);
			// 
			// label3
			// 
			this.label3.Location = new System.Drawing.Point(16, 80);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(376, 16);
			this.label3.TabIndex = 10;
			this.label3.Text = "Extracted UnicodeText";
			// 
			// grpSplitBytes
			// 
			this.grpSplitBytes.Controls.AddRange(new System.Windows.Forms.Control[] {
																						this.chkSplitBytes});
			this.grpSplitBytes.Location = new System.Drawing.Point(8, 96);
			this.grpSplitBytes.Name = "grpSplitBytes";
			this.grpSplitBytes.Size = new System.Drawing.Size(792, 72);
			this.grpSplitBytes.TabIndex = 1;
			this.grpSplitBytes.TabStop = false;
			this.grpSplitBytes.Text = "Split Bytes";
			// 
			// chkSplitBytes
			// 
			this.chkSplitBytes.Location = new System.Drawing.Point(16, 32);
			this.chkSplitBytes.Name = "chkSplitBytes";
			this.chkSplitBytes.Size = new System.Drawing.Size(736, 24);
			this.chkSplitBytes.TabIndex = 4;
			this.chkSplitBytes.Text = "Use 8 pixels per byte - this requires larger images, but makes the message nearly" +
				" 100% invisible";
			// 
			// frmMain
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(6, 15);
			this.ClientSize = new System.Drawing.Size(810, 473);
			this.Controls.AddRange(new System.Windows.Forms.Control[] {
																		  this.tabAction,
																		  this.grpKey,
																		  this.grpSplitBytes});
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
			this.Name = "frmMain";
			this.Text = "Use this on your own risk - for help and bug reports contact picturekey@binary-un" +
				"iverse.net";
			this.grpPicture.ResumeLayout(false);
			this.grpKey.ResumeLayout(false);
			this.tabAction.ResumeLayout(false);
			this.tabPage1.ResumeLayout(false);
			this.grpMessage.ResumeLayout(false);
			this.tabPage2.ResumeLayout(false);
			this.grpPictureExtract.ResumeLayout(false);
			this.groupBox3.ResumeLayout(false);
			this.grpSplitBytes.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		/// <summary>
		/// Der Haupteinstiegspunkt für die Anwendung.
		/// </summary>
		[STAThread]
		static void Main() 
		{
			Application.Run(new frmMain());
		}

		private void btnHide_Click(object sender, System.EventArgs e) {
			//get a stream for the message to hide
			Stream messageStream = GetMessageStream();
			if(messageStream.Length == 0){
				MessageBox.Show("Please enter a message or select a file.");
				txtMessageText.Focus();
			}else{
				this.Cursor = Cursors.WaitCursor;
				
				//try{
					//hide the message
					CryptUtility.HideMessageInBitmap(messageStream, imagesHide, keys, chkSplitBytes.Checked);
				//}catch(Exception ex){
				//	MessageBox.Show(ex.Message + "\nStackTrace: " + ex.StackTrace, "Exception");
				//}

				this.Cursor = Cursors.Default;
			}
			messageStream.Close();
		}

		private void btnExtract_Click(object sender, System.EventArgs e) {
			//empty stream for the extracted message
			Stream messageStream = new MemoryStream();
			
			this.Cursor = Cursors.WaitCursor;
			
			try{
				//extract the hidden message from the bitmap
				CryptUtility.ExtractMessageFromBitmap(imagesExtract, keys, ref messageStream, chkSplitBytes.Checked);
				
				//save the message, if a filename is available
				if(txtExtractedMsgFile.Text.Length > 0){
					messageStream.Seek(0, SeekOrigin.Begin);
					FileStream fs = new FileStream(txtExtractedMsgFile.Text, FileMode.Create);
					byte[] streamContent = new Byte[messageStream.Length];
					messageStream.Read(streamContent, 0, streamContent.Length);
					fs.Write(streamContent, 0, streamContent.Length);
				}

				//display the message - displays chaos, if it's no unicode text
				messageStream.Seek(0, SeekOrigin.Begin);
				StreamReader reader = new StreamReader(messageStream, UnicodeEncoding.Unicode);
				String readerContent = reader.ReadToEnd();
				if(readerContent.Length > txtExtractedMsgText.MaxLength){
					readerContent = readerContent.Substring(0, txtExtractedMsgText.MaxLength);
				}
				txtExtractedMsgText.Text = readerContent;
			}catch(Exception ex){
				MessageBox.Show(ex.Message + "\nStackTrace: " + ex.StackTrace, "Exception");
			}

			this.Cursor = Cursors.Default;
			
			//close the stream
			messageStream.Close();
		}

		/// <summary>Creates a stream to read the message from a string or a file</summary>
		/// <returns>FileStream for a message file, or MemoryStream</returns>
		private Stream GetMessageStream(){
			Stream messageStream;
			if(rdoMessageText.Checked){
				byte[] messageBytes = UnicodeEncoding.Unicode.GetBytes(txtMessageText.Text);
				messageStream = new MemoryStream(messageBytes);
			}else{
				messageStream = new FileStream(txtMessageFile.Text, FileMode.Open, FileAccess.Read);
			}
			return messageStream;
		}
				
		/// <summary>Displays the OpenFile dialog</summary>
		/// <returns>The selected file name, or null</returns>
		private String GetFileName(String filter){
			OpenFileDialog dlg = new OpenFileDialog();
			dlg.Multiselect = false;
			if(filter.Length > 0){ dlg.Filter = filter; }

			if( dlg.ShowDialog(this) != DialogResult.Cancel){
				return dlg.FileName;
			}else{
				return null;
			}
		}

		private void rdoMessage_Click(object sender, System.EventArgs e) {
			txtMessageFile.Enabled = rdoMessageFile.Checked;
			txtMessageText.Enabled = rdoMessageText.Checked;
		}

		private void txtMessageFile_Enter(object sender, System.EventArgs e) {
			rdoMessageFile.Checked = true;
		}

		private void txtMessageText_Enter(object sender, System.EventArgs e) {
			rdoMessageText.Checked = true;
		}

		private void btnMessage_Click(object sender, System.EventArgs e) {
			String fileName = GetFileName(String.Empty);
			if(fileName != null){
				txtMessageFile.Text = fileName;
				rdoMessageFile.Checked = true;
			}
		}

		private void btnExtractedMsgFile_Click(object sender, System.EventArgs e) {
			SaveFileDialog dlg = new SaveFileDialog();
			if( dlg.ShowDialog() == DialogResult.OK ){
				txtExtractedMsgFile.Text = dlg.FileName;
			}			
		}

		private void btnKeyFile_Click(object sender, System.EventArgs e) {
			KeyFilesDialog dlg = new KeyFilesDialog(keys);
			if( dlg.ShowDialog(this) == DialogResult.OK ){
				keys = dlg.GetKeys();
				switch(keys.Length){
					case 0:{
						lblKeyFiles.Text = "No key files specified";
						btnHide.Enabled = btnExtract.Enabled = false;
						break; }
					case 1:{
						lblKeyFiles.Text = "1 key file specified";
						btnHide.Enabled = (imagesHide.Length > 0);
						btnExtract.Enabled = (imagesExtract.Length > 0);
						break; }
					default:{
						lblKeyFiles.Text = keys.Length.ToString() + " key file specified";
						btnHide.Enabled = (imagesHide.Length > 0);
						btnExtract.Enabled = (imagesExtract.Length > 0);
						break; }
				}
			}
		}

		private void btnImageFile_Click(object sender, System.EventArgs e) {
			Label lblFeedback;
			Button btnAction;
			CarrierImage[] images;
			ImageFilesDialog dlg;
			if(sender == btnImageFile){
				lblFeedback = lblImageFiles;
				btnAction = btnHide;
				dlg = new ImageFilesDialog(imagesHide, true);
			}else{
				lblFeedback = lblImageFilesExtract;
				btnAction = btnExtract;
				dlg = new ImageFilesDialog(imagesExtract, false);
			}
			
			if( dlg.ShowDialog(this) == DialogResult.OK ){
				
				if(sender == btnImageFile){
					imagesHide = dlg.GetImages();
					images = imagesHide;
				}else{
					imagesExtract = dlg.GetImages();
					images = imagesExtract;
				}

				switch(images.Length){
					case 0:{
						lblFeedback.Text = "No carrier files specified";
						btnAction.Enabled = false;
						break; }
					case 1:{
						lblFeedback.Text = "1 carrier file specified";
						btnAction.Enabled = (keys.Length > 0);
						break; }
					default:{
						lblFeedback.Text = images.Length.ToString() + " carrier file specified";
						btnAction.Enabled = (keys.Length > 0);
						break; }
				}
			}
		}

	}
}
