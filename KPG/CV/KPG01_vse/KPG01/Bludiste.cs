using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KPG01
{
    public class Bludiste
    {
        public int width;
        public int height;
        public Bunka[,] bunky;
        public Random rng;

        public Bludiste(int width, int height)
        {
            if (width < 1) width = 1;
            if (height < 1) height = 1;
            this.width = width;
            this.height = height;
            bunky = new Bunka[width + 1, height + 1];
            rng = new Random(Environment.TickCount);        
        }

        /// <summary>
        /// Vygeneruj nahodne bludiste.
        /// </summary>
        public void build()
        {
            //zavolej konstruktor uzavrene bunky pro vsechny bunky
            for (int i = 0; i < height; ++i)
            {
                for (int j = 0; j < width; ++j)
                {
                    bunky[i, j] = new Bunka(true);
                }
            }

            //priprav prazdnou frontu a vloz 1. prvek
            LinkedList<Position> lifo = new LinkedList<Position>();
            lifo.AddFirst(new Position(0, 0));

            Position cp;

            Position[] r4;
            int section = 1;
            while (lifo.Count > 0)
            {
                //vyber 1.
                cp = lifo.First.Value;
                lifo.RemoveFirst();

                //bunka je jiz spojena
                if (bunky[cp.y, cp.x].visited) continue;

                bunky[cp.y, cp.x].visited = true;
                bunky[cp.y, cp.x].section = section;
                r4 = randomNotVisitedNeighbours(cp);

                //konec casti
                if (r4 == null)
                {
                    //propojeni sekce se "svetem"
                    bool joined = false;
                    foreach (Position p1 in lifo)
                    {
                        if (bunky[p1.y, p1.x].visited)
                        {
                            continue;
                        }
                        if (p1.x > 0 && bunky[p1.y, p1.x - 1].section == section)
                        {
                            spojBunky(p1, new Position(p1.x - 1, p1.y));
                            joined = true;
                            break;
                        }
                        else if (p1.x < width - 1 && bunky[p1.y, p1.x + 1].section == section)
                        {
                            spojBunky(p1, new Position(p1.x + 1, p1.y));
                            joined = true;
                            break;
                        }
                        else if (p1.y > 0 && bunky[p1.y - 1, p1.x].section == section)
                        {
                            spojBunky(p1, new Position(p1.x, p1.y - 1));
                            joined = true;
                            break;
                        }
                        else if (p1.y < height - 1 && bunky[p1.y + 1, p1.x].section == section)
                        {
                            spojBunky(p1, new Position(p1.x, p1.y + 1));
                            joined = true;
                            break;
                        }
                    }

                    if (!joined)
                    {
                        for (int i = 0; i < height; ++i)
                        {
                            for (int j = 0; j < width; ++j)
                            {
                                if (bunky[i, j].section == section)
                                {
                                    if (j > 0 && bunky[i, j - 1].section != section)
                                    {
                                        spojBunky(new Position(j, i), new Position(j - 1, i));
                                        break;
                                    }
                                    else if (j < width - 1 && bunky[i, j + 1].section != section)
                                    {
                                        spojBunky(new Position(j, i), new Position(j + 1, i));
                                        break;
                                    }
                                    else if (i > 0 && bunky[i - 1, j].section != section)
                                    {
                                        spojBunky(new Position(j, i), new Position(j, i - 1));
                                        break;
                                    }
                                    else if (i < height - 1 && bunky[i + 1, j].section != section)
                                    {
                                        spojBunky(new Position(j, i), new Position(j, i + 1));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    ++section;
                    continue;
                }

                //prvni spojim
                spojBunky(cp, r4[0]);

                for (int i = 1; i < r4.Length; ++i)
                {
                    lifo.AddFirst(r4[i]);
                }
                //chci pokracovat tam, kam jsem prokopal zed
                lifo.AddFirst(r4[0]);
            }
        }

        /// <summary>
        /// Spoji dve bunky c1 a c2, pouze pokud spolu sousedi.
        /// </summary>
        /// <param name="c1">Bunka c1</param>
        /// <param name="c2">Bunka c2</param>
        private void spojBunky(Position c1, Position c2)
        {
            if (c1.y == c2.y)
            { //stejny radek
                if (c1.x == c2.x - 1)
                {
                    //c1 vlevo od c2
                    bunky[c1.y, c1.x].right = false;
                    bunky[c2.y, c2.x].left = false;
                }
                else if (c2.x == c1.x - 1)
                {
                    //c2 vlevo od c1
                    bunky[c2.y, c2.x].right = false;
                    bunky[c1.y, c1.x].left = false;
                }
            }
            else if (c1.x == c2.x)
            { //stejny sloupec
                if (c1.y == c2.y - 1)
                {
                    //c1 pod od c2
                    bunky[c1.y, c1.x].top = false;
                    bunky[c2.y, c2.x].bottom = false;
                }
                else if (c2.y == c1.y - 1)
                {
                    //c2 pod od c1
                    bunky[c2.y, c2.x].top = false;
                    bunky[c1.y, c1.x].bottom = false;
                }
            }
        }

        /// <summary>
        /// Vrati nahodne zamichane pole nenavstivenych sousedu bunky.
        /// </summary>
        /// <param name="pos">Bunka, pro kterou hledam sousedy.</param>
        /// <returns></returns>
        private Position[] randomNotVisitedNeighbours(Position pos)
        {
            List<Position> nbl = new List<Position>(4);
            if (pos.x > 0)
            {
                if (!bunky[pos.y, pos.x - 1].visited)
                {
                    nbl.Add(new Position(pos.x - 1, pos.y));
                }
            }
            if (pos.y > 0)
            {
                if (!bunky[pos.y - 1, pos.x].visited)
                {
                    nbl.Add(new Position(pos.x, pos.y - 1));
                }
            }
            if (pos.x < width - 1)
            {
                if (!bunky[pos.y, pos.x + 1].visited)
                {
                    nbl.Add(new Position(pos.x + 1, pos.y));
                }
            }
            if (pos.y < height - 1)
            {
                if (!bunky[pos.y + 1, pos.x].visited)
                {
                    nbl.Add(new Position(pos.x, pos.y + 1));
                }
            }

            //zadny nenavstiveny soused
            if (nbl.Count <= 0) return null;

            Position[] retval = nbl.ToArray();

            Position x;
            int rni;
            //zamichani
            for (int i = 0; i < retval.Length; ++i)
            {
                rni = rng.Next(0, retval.Length);
                x = retval[i];
                retval[i] = retval[rni];
                retval[rni] = x;
            }
            return retval;
        }

        public void drawOnto(System.Drawing.Graphics gr, System.Drawing.Rectangle rect, Position playerPosition,Position cil)
        {
            gr.Clear(Color.White);
            int t, b, l, r;
            double cellWidth = (double)rect.Width / width;
            double cellHeight = (double)rect.Height / height;

            for (int i = 0; i < height; ++i)
            {
                for (int j = 0; j < width; ++j)
                {
                    l = (int)(cellWidth * j);
                    r = (int)(cellWidth * (j + 1));
                    b = rect.Height - (int)(cellHeight * i);
                    t = rect.Height - (int)(cellHeight * (i + 1));

                    //vykresli bunku
                    if (bunky[i, j].top)
                    {
                        gr.DrawLine(Pens.Black, new Point(l, t), new Point(r, t));
                    }
                    if (bunky[i, j].left)
                    {
                        gr.DrawLine(Pens.Black, new Point(l, t), new Point(l, b));
                    }
                    if (bunky[i, j].bottom)
                    {
                        gr.DrawLine(Pens.Black, new Point(l, b), new Point(r, b));
                    }
                    if (bunky[i, j].right)
                    {
                        gr.DrawLine(Pens.Black, new Point(r, t), new Point(r, b));
                    }
                }
            }

            l = (int)(cellWidth * playerPosition.x);
            t = rect.Height - (int)(cellHeight * (playerPosition.y + 1));
            gr.DrawEllipse(Pens.Red, l, t, (int)cellWidth, (int)cellHeight);
            int Cx = (int)(cellWidth * cil.x);
            int Cy = rect.Height - (int)(cellHeight * (cil.y + 1));
            gr.FillEllipse(Brushes.Green, Cx, Cy, (int)cellWidth, (int)cellHeight);


        }


    }
}
