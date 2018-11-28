using System;
using System.Drawing;

namespace PictureKey
{
	public struct FilePasswordPair{
		public String fileName;
		public String password;

		public FilePasswordPair(String fileName, String password){
			this.fileName = fileName;
			this.password = password;
		}
	}

	public struct CarrierImage{
		//file name of the clean image
		public String sourceFileName;
		//file name to save the new image
		public String resultFileName;
		//width * height
		public long countPixels;
		//count of frames in the video stream, or 0
		public int aviCountFrames;
		//produce colorful (false) or grayscale noise (true) for this picture
		public bool useGrayscale;
		//how many bytes will be hidden in this image - this field is set by CryptUtility.HideOrExtract()
		public long messageBytesToHide;
		public long[] aviMessageBytesToHide;

		public void SetCountBytesToHide(long messageBytesToHide){
			this.messageBytesToHide = messageBytesToHide;
			
			if(sourceFileName.ToLower().EndsWith(".avi")){
				aviMessageBytesToHide = new long[aviCountFrames];
				
				//calculate count of message-bytes to hide in (or extract from) each image
				long sumBytes = 0;
				for(int n=0; n<aviCountFrames; n++){
					aviMessageBytesToHide[n] = (long)Math.Ceiling( (float)messageBytesToHide / (float)aviCountFrames );
					sumBytes += aviMessageBytesToHide[n];
				}
				if(sumBytes > messageBytesToHide){ //correct Math.Ceiling effects
					aviMessageBytesToHide[aviCountFrames-1] -= (sumBytes - messageBytesToHide);
				}
			}
		}

		public CarrierImage(String sourceFileName, String resultFileName, long countPixels, int aviCountFrames, bool useGrayscale){
			this.sourceFileName = sourceFileName;
			this.resultFileName = resultFileName;
			this.countPixels = countPixels;
			this.aviCountFrames = aviCountFrames;
			this.useGrayscale = useGrayscale;
			this.messageBytesToHide = 0;
			this.aviMessageBytesToHide = null;
		}
	}

	public struct BitmapInfo{
		//uncompressed image
		public Bitmap bitmap;
		//position of the frame in the AVI stream, or -1
		public int aviPosition;
		//count of frames in the AVI stream, or 0
		public int aviCountFrames;
		//path and name of the bitmap file
		public String sourceFileName;
		//how many bytes will be hidden in this image
		public long messageBytesToHide;

		public void LoadBitmap(String fileName){
			bitmap = new Bitmap(fileName);
			sourceFileName = fileName;
		}
	}
}
