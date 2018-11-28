using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenTK.Graphics.OpenGL;
using System.Drawing.Imaging;

namespace Lesson_13 {
    class TextureLoader {
        private const int NUM_FILTERS = 3;
        private static int NUM_TEXTURES;                //number of textures total ( + diferent filters)
        private static int[] texID;                     //array of textures in OpenGL

        /// <summary>
        /// Loads textures from paths given by textures[] array and returns array of IDs for 
        /// those textures and their versions by specific filters.
        /// </summary>
        /// <param name="textures">array of paths to texture files</param>
        /// <returns>true for succes</returns>
        public static System.Boolean load(String[] textures, out int[] texID) {
            NUM_TEXTURES = NUM_FILTERS * textures.Length;
            texID = new int[NUM_TEXTURES];

            if(!loadTextures(textures)) {
                Console.WriteLine("Loading textures: Error");
                return false;
            }

            texID = TextureLoader.texID;
            Console.WriteLine("Loading textures: OK");
            return true;
        }

        /// <summary>
        /// Creates textures from paths in textures[]
        /// </summary>
        /// <returns>true if loading textures was succesfull</returns>
        private static bool loadTextures(String[] textures) {
            texID = new int[NUM_TEXTURES];
            int index = 0;
            Bitmap bmp;

            for(int i = 0; i < textures.Length; i++) {
                if(!createBitmap(textures[i], out bmp)) {
                    return false;
                }
                index = texForFilters(bmp, index);
                Console.WriteLine("Textures created: " + textures[i]);
            }

            return true;
        }

        /// <summary>
        /// Creates texture for each filter.
        /// (Nearest, Linear, MipMap)
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="index">index for first texture</param>
        /// <returns></returns>
        private static int texForFilters(Bitmap bmp, int index) {

            createTexture(bmp, index, (int)TextureMinFilter.Nearest, (int)TextureMagFilter.Nearest);
            createTexture(bmp, index + 1, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear);
            createTexture(bmp, index + 2, (int)TextureMinFilter.LinearMipmapNearest, (int)TextureMagFilter.Linear);

            return index + 3;
        }



        /// <summary>
        /// Creates texture by given parameters.
        /// </summary>
        /// <param name="bmp">source Bitmap</param>
        /// <param name="indexID">ID of new texture</param>
        /// <param name="texFilterMin">texture filter min</param>
        /// <param name="texFilterMag">texture filter mag</param>
        /// <param name="flower">true for flower texture</param>
        private static void createTexture(Bitmap bmp, int indexID, int texFilterMin, int texFilterMag) {
            GL.GenTextures(indexID, out texID[indexID]);
            GL.BindTexture(TextureTarget.Texture2D, texID[indexID]);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, texFilterMin);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, texFilterMag);

            BitmapData data;

            data = bmp.LockBits(
            new Rectangle(0, 0, bmp.Width, bmp.Height),
            ImageLockMode.ReadOnly,
            System.Drawing.Imaging.PixelFormat.Format32bppArgb
            );

            GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba,
                data.Width, data.Height, 0, OpenTK.Graphics.OpenGL.PixelFormat.Bgra,
                PixelType.UnsignedByte, data.Scan0);


            if(texFilterMin == (int)TextureMinFilter.LinearMipmapNearest) {
                GL.GenerateMipmap(GenerateMipmapTarget.Texture2D);
            }

            bmp.UnlockBits(data);
        }

        /// <summary>
        /// Creates Bitmap from file on given path
        /// </summary>
        /// <param name="path">path to file</param>
        /// <param name="bmp">created bitmap</param>
        /// <returns></returns>
        private static bool createBitmap(string path, out Bitmap bmp) {
            if(!File.Exists(path)) {
                Console.WriteLine("Texture not found: " + path);
                bmp = null;
                return false;
            }
            bmp = new Bitmap(path);
            return true;
        }
    }
}
