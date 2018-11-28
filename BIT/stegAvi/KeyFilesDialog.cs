using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

namespace PictureKey {

	public class KeyFilesDialog : System.Windows.Forms.Form {

		private System.Windows.Forms.Panel panel1;
		private System.Windows.Forms.Splitter splitter1;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.TextBox txtKeyFile;
		private System.Windows.Forms.Button btnKeyFile;
		private System.Windows.Forms.TextBox txtPassword;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Button btnAdd;
		private System.Windows.Forms.ListView lvKeys;
		private System.Windows.Forms.ColumnHeader clmFileName;
		private System.Windows.Forms.ColumnHeader clmPasswordChars;
		private System.Windows.Forms.GroupBox grpAddKey;
		private System.Windows.Forms.Button btnOk;
		private System.Windows.Forms.Button btCancel;
		private System.ComponentModel.Container components = null;

		public KeyFilesDialog(FilePasswordPair[] initialKeys)
		{
			//
			// Erforderlich für die Windows Form-Designerunterstützung
			//
			InitializeComponent();

			ListViewItem item;
			foreach(FilePasswordPair key in initialKeys){
				item = new ListViewItem(
					new String[2]{ key.fileName, key.password.Length.ToString() }
				);
				item.Tag = key;
				lvKeys.Items.Add(item);
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
			this.lvKeys = new System.Windows.Forms.ListView();
			this.clmFileName = new System.Windows.Forms.ColumnHeader();
			this.clmPasswordChars = new System.Windows.Forms.ColumnHeader();
			this.panel1 = new System.Windows.Forms.Panel();
			this.btnOk = new System.Windows.Forms.Button();
			this.grpAddKey = new System.Windows.Forms.GroupBox();
			this.txtPassword = new System.Windows.Forms.TextBox();
			this.txtKeyFile = new System.Windows.Forms.TextBox();
			this.btnKeyFile = new System.Windows.Forms.Button();
			this.label1 = new System.Windows.Forms.Label();
			this.btnAdd = new System.Windows.Forms.Button();
			this.label2 = new System.Windows.Forms.Label();
			this.btCancel = new System.Windows.Forms.Button();
			this.splitter1 = new System.Windows.Forms.Splitter();
			this.panel1.SuspendLayout();
			this.grpAddKey.SuspendLayout();
			this.SuspendLayout();
			// 
			// lvKeys
			// 
			this.lvKeys.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
																					 this.clmFileName,
																					 this.clmPasswordChars});
			this.lvKeys.Dock = System.Windows.Forms.DockStyle.Fill;
			this.lvKeys.FullRowSelect = true;
			this.lvKeys.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
			this.lvKeys.MultiSelect = false;
			this.lvKeys.Name = "lvKeys";
			this.lvKeys.Size = new System.Drawing.Size(560, 343);
			this.lvKeys.TabIndex = 0;
			this.lvKeys.View = System.Windows.Forms.View.Details;
			this.lvKeys.KeyDown += new System.Windows.Forms.KeyEventHandler(this.lvKeys_KeyDown);
			// 
			// clmFileName
			// 
			this.clmFileName.Text = "Key file";
			this.clmFileName.Width = 300;
			// 
			// clmPasswordChars
			// 
			this.clmPasswordChars.Text = "Password length";
			this.clmPasswordChars.Width = 120;
			// 
			// panel1
			// 
			this.panel1.Controls.AddRange(new System.Windows.Forms.Control[] {
																				 this.btnOk,
																				 this.grpAddKey,
																				 this.btCancel});
			this.panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.panel1.Location = new System.Drawing.Point(0, 167);
			this.panel1.Name = "panel1";
			this.panel1.Size = new System.Drawing.Size(560, 176);
			this.panel1.TabIndex = 1;
			// 
			// btnOk
			// 
			this.btnOk.Location = new System.Drawing.Point(384, 144);
			this.btnOk.Name = "btnOk";
			this.btnOk.Size = new System.Drawing.Size(80, 23);
			this.btnOk.TabIndex = 6;
			this.btnOk.Text = "OK";
			this.btnOk.Click += new System.EventHandler(this.btnOk_Click);
			// 
			// grpAddKey
			// 
			this.grpAddKey.Controls.AddRange(new System.Windows.Forms.Control[] {
																					this.txtPassword,
																					this.txtKeyFile,
																					this.btnKeyFile,
																					this.label1,
																					this.btnAdd,
																					this.label2});
			this.grpAddKey.Location = new System.Drawing.Point(8, 8);
			this.grpAddKey.Name = "grpAddKey";
			this.grpAddKey.Size = new System.Drawing.Size(544, 120);
			this.grpAddKey.TabIndex = 5;
			this.grpAddKey.TabStop = false;
			this.grpAddKey.Text = "Add Key";
			// 
			// txtPassword
			// 
			this.txtPassword.Location = new System.Drawing.Point(96, 56);
			this.txtPassword.Name = "txtPassword";
			this.txtPassword.PasswordChar = '*';
			this.txtPassword.Size = new System.Drawing.Size(360, 22);
			this.txtPassword.TabIndex = 2;
			this.txtPassword.Text = "";
			// 
			// txtKeyFile
			// 
			this.txtKeyFile.Location = new System.Drawing.Point(96, 32);
			this.txtKeyFile.Name = "txtKeyFile";
			this.txtKeyFile.Size = new System.Drawing.Size(360, 22);
			this.txtKeyFile.TabIndex = 0;
			this.txtKeyFile.Text = "";
			// 
			// btnKeyFile
			// 
			this.btnKeyFile.Location = new System.Drawing.Point(456, 32);
			this.btnKeyFile.Name = "btnKeyFile";
			this.btnKeyFile.TabIndex = 1;
			this.btnKeyFile.Text = "Browse...";
			this.btnKeyFile.Click += new System.EventHandler(this.btnKeyFile_Click);
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(16, 32);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(80, 23);
			this.label1.TabIndex = 1;
			this.label1.Text = "Filename";
			// 
			// btnAdd
			// 
			this.btnAdd.Location = new System.Drawing.Point(264, 80);
			this.btnAdd.Name = "btnAdd";
			this.btnAdd.Size = new System.Drawing.Size(192, 23);
			this.btnAdd.TabIndex = 3;
			this.btnAdd.Text = "Add to key files";
			this.btnAdd.Click += new System.EventHandler(this.btnAdd_Click);
			// 
			// label2
			// 
			this.label2.Location = new System.Drawing.Point(16, 56);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(80, 23);
			this.label2.TabIndex = 3;
			this.label2.Text = "Password";
			// 
			// btCancel
			// 
			this.btCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.btCancel.Location = new System.Drawing.Point(472, 144);
			this.btCancel.Name = "btCancel";
			this.btCancel.Size = new System.Drawing.Size(80, 23);
			this.btCancel.TabIndex = 7;
			this.btCancel.Text = "Cancel";
			this.btCancel.Click += new System.EventHandler(this.btCancel_Click);
			// 
			// splitter1
			// 
			this.splitter1.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.splitter1.Location = new System.Drawing.Point(0, 164);
			this.splitter1.Name = "splitter1";
			this.splitter1.Size = new System.Drawing.Size(560, 3);
			this.splitter1.TabIndex = 2;
			this.splitter1.TabStop = false;
			// 
			// KeyFilesDialog
			// 
			this.AcceptButton = this.btnAdd;
			this.AutoScaleBaseSize = new System.Drawing.Size(6, 15);
			this.CancelButton = this.btCancel;
			this.ClientSize = new System.Drawing.Size(560, 343);
			this.ControlBox = false;
			this.Controls.AddRange(new System.Windows.Forms.Control[] {
																		  this.splitter1,
																		  this.panel1,
																		  this.lvKeys});
			this.Name = "KeyFilesDialog";
			this.Text = "Manage Key Files";
			this.panel1.ResumeLayout(false);
			this.grpAddKey.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		
		public FilePasswordPair[] GetKeys(){
			FilePasswordPair[] result = new FilePasswordPair[lvKeys.Items.Count];
			ListViewItem item;
			for(int n=0; n<lvKeys.Items.Count; n++){
				item = lvKeys.Items[n];
				result[n] = (FilePasswordPair)item.Tag;
			}
			return result;
		}

		private void btnKeyFile_Click(object sender, System.EventArgs e) {
			OpenFileDialog dlg = new OpenFileDialog();
			dlg.Multiselect = false;
			if( dlg.ShowDialog(this) != DialogResult.Cancel){
				txtKeyFile.Text = dlg.FileName;
			}
		}

		private void btnAdd_Click(object sender, System.EventArgs e) {
			if(txtKeyFile.Text.Length > 1){
				if(txtPassword.Text.Length > 0){
					//add the key file to the list
					ListViewItem item= new ListViewItem(
						new String[2]{ txtKeyFile.Text, txtPassword.Text.Length.ToString() }
					);
					item.Tag = new FilePasswordPair(txtKeyFile.Text, txtPassword.Text);

					lvKeys.Items.Add(item);
				}else{
					MessageBox.Show("Please enter a password to protect the key.");
				}
			}else{
				MessageBox.Show("Please enter path and name of the key file.");
			}
		}

		private void btnOk_Click(object sender, System.EventArgs e) {
			this.DialogResult = DialogResult.OK;
			this.Close();
		}

		private void btCancel_Click(object sender, System.EventArgs e) {
			this.DialogResult = DialogResult.Cancel;
			this.Close();
		}

		private void lvKeys_KeyDown(object sender, System.Windows.Forms.KeyEventArgs e) {
			if((e.KeyCode == Keys.Delete) && (lvKeys.SelectedItems.Count == 1)){
				lvKeys.SelectedItems[0].Remove();
			}
		}
	}
}
