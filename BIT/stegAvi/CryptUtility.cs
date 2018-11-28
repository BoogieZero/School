/*
 * This class has been written by
 * Corinna John (Hannover, Germany)
 * picturekey@binary-universe.net
 * 
 * You can use the code in any context you like,
 * as long as you do not delete this comment.
 * 
 * Please send me a little feedback about what you're
 * using this code for and what changes you'd like to
 * see in later versions. (And please excuse the bad english)
 * */

using System;
using System.Drawing;
using System.Windows.Forms;
using System.Text;
using System.IO;

namespace PictureKey {

	public class CryptUtility {

		private const String TempFileName = "picturekey_temp.bmp";

		/// <summary>Hides a message in a bitmap</summary>
		/// <param name="messageStream">The message to hide</param>
		/// <param name="bitmap">The carrier bitmap</param>
		/// <param name="keyStream">The key to use</param>
		public static void HideMessageInBitmap(Stream messageStream, CarrierImage[] imageFiles, FilePasswordPair[] keys, bool splitBytes){
			HideOrExtract(ref messageStream, imageFiles, keys, splitBytes, false);
			messageStream = null;
		}

		/// <summary>Extracts an hidden message from a bitmap</summary>
		/// <param name="bitmap">The carrier bitmap</param>
		/// <param name="keyStream">The key used for hiding the message</param>
		/// <param name="messageStream">Empty stream to receive the message</param>
		public static void ExtractMessageFromBitmap(CarrierImage[] imageFiles, FilePasswordPair[] keys, ref Stream messageStream, bool splitBytes){
			HideOrExtract(ref messageStream, imageFiles, keys, splitBytes, true);
		}

		/// <summary>Steps through the pixels of bitmaps using a key pattern and hides or extracts a message</summary>
		/// <param name="messageStream">If exctract is false, the message to hide - otherwise an empty stream to receive the extracted message</param>
		/// <param name="bitmap">The carrier bitmap</param>
		/// <param name="keyStream">The key specifying the unchanged pixels between two hidden bytes</param>
		/// <param name="extract">Extract a hidden message (true), or hide a message in a clean carrier bitmap (false)</param>
		private static void HideOrExtract(ref Stream messageStream, CarrierImage[] imageFiles, FilePasswordPair[] keys, bool splitBytes, bool extract){
			AviWriter aviWriter = new AviWriter();
			AviReader aviReader = new AviReader();
			
			//index for imageFiles
			int indexBitmaps = 0;
			
			//count available pixels
			long countPixels = 0;
			for(indexBitmaps=0; indexBitmaps<imageFiles.Length; indexBitmaps++){
				countPixels += imageFiles[indexBitmaps].countPixels;
			}

			//load the first bitmap
			BitmapInfo bitmapInfo = LoadBitmap(imageFiles[0], aviReader, aviWriter);
			
			//Stores the color of a pixel
			Color pixelColor;
			
			//Length of the message
			Int32 messageLength;

			//combine all keys
			Stream keyStream = GetKeyStream(keys);

			if(extract){
				//Read the length of the hidden message from the first pixel
				pixelColor = bitmapInfo.bitmap.GetPixel(0,0);
				messageLength = (pixelColor.R << 16) + (pixelColor.G << 8) + pixelColor.B;
				messageStream = new MemoryStream(messageLength);
			}else{
				
				messageLength = (Int32)messageStream.Length;

				if(messageStream.Length >= 16777215){ //The message is too long
					String exceptionMessage = "The message is too long, only 16777215 bytes are allowed.";
					throw new Exception(exceptionMessage);
				}
			}

			//calculate count of message-bytes to hide in (or extract from) each image
			long sumBytes = 0;
			for(int n=0; n<imageFiles.Length; n++){
				float pixels = (float)imageFiles[n].countPixels / (float)countPixels;
				imageFiles[n].SetCountBytesToHide(
					(long)Math.Ceiling( (float)messageLength * pixels ) );
				sumBytes += imageFiles[n].messageBytesToHide;
			}
			if(sumBytes > messageLength){
				//correct Math.Ceiling effects
				imageFiles[imageFiles.Length-1].messageBytesToHide -= (sumBytes - messageLength);
			}

			//set count of bytes for the first image
			if(bitmapInfo.aviPosition >= 0){ //video
				bitmapInfo.messageBytesToHide = imageFiles[0].aviMessageBytesToHide[0];
			}else{ //bitmap
				bitmapInfo.messageBytesToHide = imageFiles[0].messageBytesToHide;
			}
			

			if( ! extract){
				
				//Check size of the carrier image

				long countRequiredPixels;
				int readByte;
				String errorMessage = String.Empty;
				for(int n=0; n<imageFiles.Length; n++){
					//One pixel of the first image is used for the message's length
					countRequiredPixels = (n==0)?1:0;

					//Count pixels
					long countRequiredPixelsImage;
					if(splitBytes){ //use 8 pixels for a message byte
						countRequiredPixelsImage = imageFiles[n].messageBytesToHide * 8;
					}else{ //use one pixel for a message byte
						countRequiredPixelsImage = imageFiles[n].messageBytesToHide;
					}
					for(int countBytes=0; countBytes<countRequiredPixelsImage; countBytes++){
						readByte = keyStream.ReadByte();
						if(readByte < 0){
							keyStream.Seek(0, SeekOrigin.Begin);
							readByte = keyStream.ReadByte();
						}
						countRequiredPixels += readByte;
					}

					if(countRequiredPixels > imageFiles[n].countPixels){
						errorMessage += "The images "+imageFiles[n].sourceFileName+" is too small for this message and key. "+countRequiredPixels+" pixels are required.\n";
					}
				}

				if(errorMessage.Length > 0){
					//One or more images are too small
					throw new Exception(errorMessage);
				}
				
				//Write length of the bitmap into the first pixel
				int colorValue = messageLength;
				int red = colorValue >> 16;
				colorValue -= red << 16;
				int green = colorValue >> 8;
				int blue = colorValue - (green << 8);
				pixelColor = Color.FromArgb(red, green, blue);
				bitmapInfo.bitmap.SetPixel(0,0, pixelColor);
			}

			//Reset the streams
			keyStream.Seek(0, SeekOrigin.Begin);
			messageStream.Seek(0, SeekOrigin.Begin);
			
			//Loop over the message and hide each byte
			if(splitBytes){
				HideBits(keyStream, messageStream, messageLength, aviReader, aviWriter, imageFiles, bitmapInfo, extract);
			}else{
				HideBytes(keyStream, messageStream, messageLength, aviReader, aviWriter, imageFiles, bitmapInfo, extract);
			}

			//Close AVI files
			aviWriter.Close();
			aviReader.Close();

			//Delete temporary file
			String fileName = Application.ExecutablePath;
			int index = fileName.LastIndexOf("\\")+1;
			fileName = fileName.Substring(0, index) + TempFileName;
			if(File.Exists(fileName)){ File.Delete(fileName); }

			keyStream.Close();
		}

		private static void MovePixelPosition(bool extract, AviReader aviReader, AviWriter aviWriter, CarrierImage[] imageFiles, Stream keyStream, ref int countBytesInCurrentImage, ref int indexBitmaps, ref Point pixelPosition, ref int bitmapWidth, ref BitmapInfo bitmapInfo){
			//Repeat the key, if it is shorter than the message
			if(keyStream.Position == keyStream.Length){
				keyStream.Seek(0, SeekOrigin.Begin);
			}
			//Get the next pixel-count from the key, use "1" if it's 0
			byte currentKeyByte = (byte)keyStream.ReadByte();
			int currentStepWidth = (currentKeyByte==0) ? (byte)1 : currentKeyByte;
				
			//Perform line breaks, if current step is wider than the image
			while(currentStepWidth > bitmapWidth){
				currentStepWidth -= bitmapWidth;
				pixelPosition.Y++;
			}
				
			//Move X-position
			if((bitmapWidth - pixelPosition.X) < currentStepWidth){
				pixelPosition.X = currentStepWidth - (bitmapWidth - pixelPosition.X);	
				pixelPosition.Y++;
			}else{
				pixelPosition.X += currentStepWidth;
			}
				
			//Proceed to next bitmap
			if(countBytesInCurrentImage == bitmapInfo.messageBytesToHide){
				//Reset indices
				pixelPosition.Y = 0;
				countBytesInCurrentImage = 0;
				
				if( ! extract ){ 
					if(bitmapInfo.aviPosition < 0){
						//Save bitmap
						SaveBitmap( bitmapInfo.bitmap, imageFiles[indexBitmaps].resultFileName );
					}else{
						//Write frame
						aviWriter.AddFrame(bitmapInfo.bitmap);
					}
				}

				//Load next bitmap
				
				bitmapInfo.bitmap.Dispose();
				
				bool nextFile = true;
				if(bitmapInfo.aviPosition >= 0){
					if(bitmapInfo.aviPosition == (bitmapInfo.aviCountFrames-1)){
						//Last frame - close AVI file
						aviWriter.Close();
						//Delete temporary file
						bitmapInfo.bitmap.Dispose();
						File.Delete(bitmapInfo.sourceFileName);
					}else{
						//Overwrite temporary file with the next bitmap
						bitmapInfo.aviPosition++;
						aviReader.ExportBitmap(bitmapInfo.aviPosition, bitmapInfo.sourceFileName);
						bitmapInfo.bitmap = new Bitmap(bitmapInfo.sourceFileName);
						bitmapInfo.messageBytesToHide = imageFiles[indexBitmaps].aviMessageBytesToHide[bitmapInfo.aviPosition];
						nextFile = false;
					}
				}
				if(nextFile){
					indexBitmaps++;
					bitmapInfo = LoadBitmap(imageFiles[indexBitmaps], aviReader, aviWriter);
					bitmapWidth = bitmapInfo.bitmap.Width - 1;
				}

				if(pixelPosition.X > bitmapWidth){ pixelPosition.X = 0; }
			}
		}

		private static byte GetReverseKeyByte(Stream keyStream){
			//jump to reverse-read position and read from the end of the stream
			long keyPosition = keyStream.Position;
			keyStream.Seek(-keyPosition, SeekOrigin.End);
			byte reverseKeyByte = (byte)keyStream.ReadByte();
			//jump back to normal read position
			keyStream.Seek(keyPosition, SeekOrigin.Begin);
			return reverseKeyByte;
		}

		/// <summary>Loop over the message and hide each byte in one pixel</summary>
		/// <param name="keyStream">The key</param>
		/// <param name="messageStream">A stream containing the message (extract==false) or an empty stream (extract==true)</param>
		/// <param name="messageLength">Expected length of the message</param>
		/// <param name="imageFiles">CarrierImages describing the bitmaps</param>
		/// <param name="bitmaps">The bitmaps</param>
		/// <param name="extract">Hide the message (false) or extract it (true)</param>
		private static void HideBytes(Stream keyStream, Stream messageStream, long messageLength, AviReader aviReader, AviWriter aviWriter, CarrierImage[] imageFiles, BitmapInfo bitmapInfo, bool extract){
			//Color component to hide the next byte in (0-R, 1-G, 2-B)
			//Rotates with every hidden byte
			int currentColorComponent = 0;

			//Index of the current bitmap
			int indexBitmaps = 0;
			//Maximum X and Y position in the current bitmap
			int bitmapWidth = bitmapInfo.bitmap.Width-1;
			//int bitmapHeight = bitmaps[0].Height-1;
			
			//Current position in the carrier bitmap
			//Start with 1, because (0,0) contains the message length
			Point pixelPosition = new Point(1,0);

			//Count of bytes already hidden in the current image
			int countBytesInCurrentImage = 0;

			//Stores the color of a pixel
			Color pixelColor;

			//A value read from the key stream in reverse direction
			byte currentReverseKeyByte = 0;

			for(int messageIndex=0; messageIndex<messageLength; messageIndex++){
				MovePixelPosition(extract, aviReader, aviWriter, imageFiles, keyStream,  ref countBytesInCurrentImage, ref indexBitmaps, ref pixelPosition, ref bitmapWidth, ref bitmapInfo);
				currentReverseKeyByte = GetReverseKeyByte(keyStream);
				countBytesInCurrentImage++;
			
				//Get color of the "clean" pixel
				pixelColor = bitmapInfo.bitmap.GetPixel(pixelPosition.X, pixelPosition.Y);

				if(extract){
					//Extract the hidden message-byte from the color
					byte foundByte = (byte)(currentReverseKeyByte ^ GetColorComponent(pixelColor, currentColorComponent));
					messageStream.WriteByte(foundByte);
					//Rotate color components
					currentColorComponent = (currentColorComponent==2) ? 0 : (currentColorComponent+1);

				}else{
					//To add a bit of confusion, xor the byte with a byte read from the keyStream
					int currentByte = messageStream.ReadByte() ^ currentReverseKeyByte;
					
					if(imageFiles[indexBitmaps].useGrayscale){
						pixelColor = Color.FromArgb(currentByte, currentByte, currentByte);
					}else{
						//Change one component of the color to the message-byte
						SetColorComponent(ref pixelColor, currentColorComponent, currentByte);
						//Rotate color components
						currentColorComponent = (currentColorComponent==2) ? 0 : (currentColorComponent+1);
					}
					bitmapInfo.bitmap.SetPixel(pixelPosition.X, pixelPosition.Y, pixelColor);
				}
			}
			
			//Save last image
			if( ! extract ){ 
				if(bitmapInfo.aviPosition < 0){ //Save bitmap
					SaveBitmap( bitmapInfo.bitmap, imageFiles[indexBitmaps].resultFileName );
				}else{ //Write frame
					aviWriter.AddFrame(bitmapInfo.bitmap);
				}
			}
			bitmapInfo.bitmap.Dispose();
		}

		private static void HideBits(Stream keyStream, Stream messageStream, long messageLength, AviReader aviReader, AviWriter aviWriter, CarrierImage[] imageFiles, BitmapInfo bitmapInfo, bool extract){
			//Color component to hide the next byte in (0-R, 1-G, 2-B)
			//Rotates with every hidden byte
			int currentColorComponent = 0;

			//Index of the current bitmap
			int indexBitmaps = 0;
			//Maximum X and Y position in the current bitmap
			int bitmapWidth = bitmapInfo.bitmap.Width-1;
			
			//Current position in the carrier bitmap
			//Start with 1, because (0,0) contains the message length
			Point pixelPosition = new Point(1,0);

			//Count of bytes already hidden in the current image
			int countBytesInCurrentImage = 0;

			//Stores the color of a pixel
			Color pixelColor;

			//A value read from the key stream in reverse direction
			byte currentReverseKeyByte = 0;
			//The current byte of the message stream
			byte currentByte;
			
			for(int messageIndex=0; messageIndex<messageLength; messageIndex++){
				
				currentReverseKeyByte = GetReverseKeyByte(keyStream);
				
				if(extract){
					currentByte = 0;
				}else{
					currentByte = (byte)messageStream.ReadByte();
					//To add a bit of confusion, xor the byte with a byte read from the keyStream
					currentByte = (byte)(currentByte ^ currentReverseKeyByte);
				}
				
				for(byte bitPosition=0; bitPosition<8; bitPosition++){
				
					MovePixelPosition(extract, aviReader, aviWriter, imageFiles, keyStream, ref countBytesInCurrentImage, ref indexBitmaps, ref pixelPosition, ref bitmapWidth, ref bitmapInfo);
					
					//Get color of the "clean" pixel
					pixelColor = bitmapInfo.bitmap.GetPixel(pixelPosition.X, pixelPosition.Y);

					if(extract){
						//Extract the hidden message-byte from the color
						byte foundByte = GetColorComponent(pixelColor, currentColorComponent);
						bool foundBit = GetBit(foundByte, 0);
						currentByte = SetBit(currentByte, bitPosition, foundBit);					
						//Rotate color components
						currentColorComponent = (currentColorComponent==2) ? 0 : (currentColorComponent+1);

					}else{
						bool currentBit = GetBit(currentByte, bitPosition);

						if(imageFiles[indexBitmaps].useGrayscale){
							byte r = SetBit(pixelColor.R, 0, currentBit);
							byte g = SetBit(pixelColor.G, 0, currentBit);
							byte b = SetBit(pixelColor.B, 0, currentBit);
							pixelColor = Color.FromArgb(r, g, b);
						}else{
							//Change one component of the color to the message-byte
							byte colorComponentValue = GetColorComponent(pixelColor, currentColorComponent);
							colorComponentValue = SetBit(colorComponentValue, 0, currentBit);
							SetColorComponent(ref pixelColor, currentColorComponent, colorComponentValue);
							//Rotate color components
							currentColorComponent = (currentColorComponent==2) ? 0 : (currentColorComponent+1);
						}
						bitmapInfo.bitmap.SetPixel(pixelPosition.X, pixelPosition.Y, pixelColor);
					}
				}

				if(extract){
					currentByte = (byte)(currentByte ^ currentReverseKeyByte);
					messageStream.WriteByte(currentByte);
				}

				countBytesInCurrentImage++;
			
			}

			//Save last image
			if( ! extract ){ 
				if(bitmapInfo.aviPosition < 0){ //Save bitmap
					SaveBitmap( bitmapInfo.bitmap, imageFiles[indexBitmaps].resultFileName );
				}else{ //Write frame
					aviWriter.AddFrame(bitmapInfo.bitmap);
				}
			}
			bitmapInfo.bitmap.Dispose();
			/*if(bitmapInfo.aviPosition >= 0){
				//the bitmap is a temporary extrated AVI frame
				File.Delete(bitmapInfo.sourceFileName);
			}*/
		}

		/// <summary>Get the value of a bit</summary>
		/// <param name="b">The byte value</param>
		/// <param name="position">The position of the bit</param>
		/// <returns>The value of the bit</returns>
		private static bool GetBit(byte b, byte position){
			return ((b & (byte)(1 << position)) != 0);
		}

		/// <summary>Set a bit to [newBitValue]</summary>
		/// <param name="b">The byte value</param>
		/// <param name="position">The position (1-8) of the bit</param>
		/// <param name="newBitValue">The new value of the bit in position [position]</param>
		/// <returns>The new byte value</returns>
		private static byte SetBit(byte b, byte position, bool newBitValue){
			byte mask = (byte)(1 << position);
			if(newBitValue){
				return (byte)(b | mask);
			}else{
				return (byte)(b & ~mask);
			}
		}

		/// <summary>Return one component of a color</summary>
		/// <param name="pixelColor">The Color</param>
		/// <param name="colorComponent">The component to return (0-R, 1-G, 2-B)</param>
		/// <returns>The requested component</returns>
		private static byte GetColorComponent(Color pixelColor, int colorComponent){
			byte returnValue = 0;
			switch(colorComponent){
				case 0:
					returnValue = pixelColor.R;
					break;
				case 1:
					returnValue = pixelColor.G;
					break;
				case 2:
					returnValue = pixelColor.B;
					break;
			}
			return returnValue;
		}

		/// <summary>Changees one component of a color</summary>
		/// <param name="pixelColor">The Color</param>
		/// <param name="colorComponent">The component to change (0-R, 1-G, 2-B)</param>
		/// <param name="newValue">New value of the component</param>
		private static void SetColorComponent(ref Color pixelColor, int colorComponent, int newValue){
			switch(colorComponent){
				case 0:
					pixelColor = Color.FromArgb(newValue, pixelColor.G, pixelColor.B);
					break;
				case 1:
					pixelColor = Color.FromArgb(pixelColor.R, newValue, pixelColor.B);
					break;
				case 2:
					pixelColor = Color.FromArgb(pixelColor.R, pixelColor.G, newValue);
					break;
			}
		}

		private static String UnTrimColorString(String color, int desiredLength){
			int difference = desiredLength - color.Length;
			if(difference > 0){
				color = new String('0', difference) + color;
			}
			return color;
		}

		private static BitmapInfo LoadBitmap(CarrierImage imageFile, AviReader aviReader, AviWriter aviWriter){
			BitmapInfo bitmapInfo = new BitmapInfo();

			if(imageFile.sourceFileName.ToLower().EndsWith(".avi")){
				try{
					//first carrier image is a video - extract the first frame
					aviReader.Open(imageFile.sourceFileName);
					if(imageFile.resultFileName.Length > 0){
						aviWriter.Open(imageFile.resultFileName, aviReader.FrameRate);
					}

					String fileName = Application.ExecutablePath;
					int index = fileName.LastIndexOf("\\")+1;
					fileName = fileName.Substring(0, index) + TempFileName;
					
					aviReader.ExportBitmap(0, fileName);
					bitmapInfo.LoadBitmap(fileName);
					bitmapInfo.aviPosition = 0;
					bitmapInfo.aviCountFrames = aviReader.CountFrames;
					if(imageFile.aviMessageBytesToHide != null){
						bitmapInfo.messageBytesToHide = imageFile.aviMessageBytesToHide[0];
					}

				}catch(Exception ex){
					aviReader.Close();
					aviWriter.Close();
					throw ex;
				}
			
			}else{
				//first carrier file is a bitmap
				bitmapInfo.LoadBitmap(imageFile.sourceFileName);
				bitmapInfo.messageBytesToHide = imageFile.messageBytesToHide;
				bitmapInfo.aviPosition = -1;
				bitmapInfo.aviCountFrames = 0;
			}
			return bitmapInfo;
		}

		private static void SaveBitmap(Bitmap bitmap, String fileName){
			String fileNameLower = fileName.ToLower();
			
			System.Drawing.Imaging.ImageFormat format = System.Drawing.Imaging.ImageFormat.Bmp;			
			if((fileNameLower.EndsWith("tif"))||(fileNameLower.EndsWith("tiff"))){
				format = System.Drawing.Imaging.ImageFormat.Tiff;
			}else if(fileNameLower.EndsWith("png")){
				format = System.Drawing.Imaging.ImageFormat.Png;
			}
				
			//copy the bitmap
			Image img = new Bitmap(bitmap);
				
			//close bitmap file
			bitmap.Dispose();
			//save new bitmap
			img.Save(fileName, format);
			img.Dispose();
		}

		//--------------------------------------------- combining the keys

		/// <summary>Combines key file and password using XOR</summary>
		/// <param name="key">The key/password pair to combine</param>
		/// <returns>The stream created from key and password</returns>
		public static MemoryStream CreateKeyStream(FilePasswordPair key){
			FileStream fileStream = new FileStream(key.fileName, FileMode.Open);
			MemoryStream resultStream = new MemoryStream();
			int passwordIndex = 0;
			int currentByte = 0;

			while( (currentByte = fileStream.ReadByte()) >= 0 ){
				//combine the key-byte with the corresponding password-byte
				currentByte = currentByte ^ key.password[passwordIndex];

				//add the result to the key stream
				resultStream.WriteByte((byte)currentByte);

				//proceed to the next letter or repeat the password
				passwordIndex++;
				if(passwordIndex == key.password.Length){
					passwordIndex = 0;
				}
			}

			fileStream.Close();

			resultStream.Seek(0, SeekOrigin.Begin);
			return resultStream;
		}

		/// <summary>Combines all key files and passwords into one key stream</summary>
		/// <param name="keys">The keys to combine</param>
		/// <returns>The resulting key stream</returns>
		private static MemoryStream GetKeyStream(FilePasswordPair[] keys){
			//Xor the keys an their passwords
			MemoryStream[] keyStreams = new MemoryStream[keys.Length];
			for(int n=0; n<keys.Length; n++){
				keyStreams[n] = CreateKeyStream(keys[n]);				
			}
			
			//Buffer for the resulting stream
			MemoryStream resultKeyStream = new MemoryStream();

			//Find length of longest stream
			long maxLength = 0;
			foreach(MemoryStream stream in keyStreams){
				if( stream.Length > maxLength ){
					maxLength = stream.Length;
				}
			}
			
			int readByte = 0;
			for(long n=0; n<=maxLength; n++){
				for(int streamIndex=0; streamIndex<keyStreams.Length; streamIndex++){
					if(keyStreams[streamIndex] != null){
						readByte = keyStreams[streamIndex].ReadByte();
						if(readByte < 0){
							//end of stream - close the file
							//the last loop (n==maxLength) will close the last stream
							keyStreams[streamIndex].Close();
							keyStreams[streamIndex] = null;
						}else{
							//copy a byte into the result key
							resultKeyStream.WriteByte( (byte)readByte );
						}
					}
				}
			}
			
			return resultKeyStream;
		}

	}
}
