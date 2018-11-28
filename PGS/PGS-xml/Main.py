import io

# potrebne pro SAX
from xml.sax import handler, make_parser

# potrebne na razeni ...
from operator import attrgetter, methodcaller

class Slozka:
    def __init__(self):
        self.id = 0
        self.nazev = ''
        self.cena = 0

    def setId(self, id):
        self.id = id

    def setNazev(self, nazev):
        self.nazev = nazev

    def setCena(self, cena):
        self.cena = cena

    def getId(self):
        return self.id

    def getNazev(self):
        return self.nazev

    def getCena(self):
        return self.cena

    def print(self):
        print(  "id:    "+str(self.id)+"\n" +
                "   Nazev: "+str(self.nazev)+"\n" +
                "   Cena:  "+str(self.cena)+"\n")

class Koktejly:
    def __init__(self):
        self.id = 0
        self.nazev = ''
        self.cena = 0
        self.vydaje = 0;

    def setId(self, id):
        self.id = id

    def setNazev(self, nazev):
        self.nazev = nazev

    def setCena(self, cena):
        self.cena = cena

    def addVydaj(self, slozka, mnozstvi):
        if not isinstance(slozka, Slozka):
            raise TypeError("slozka must be typed to Slozka")

        self.vydaje += slozka.getCena() * mnozstvi

    def setVydaje(self, vydaje):
        self.vydaje = vydaje

    def getId(self):
        return self.id

    def getNazev(self):
        return self.nazev

    def getCena(self):
        return self.cena

    def getVydaje(self):
        return self.vydaje

    def getZisk(self):
        return self.cena - self.vydaje

    def print(self):
        print("Nazev:   "+self.nazev+"\n" +
              " Cena:    "+str(self.cena)+"\n" +
              " Vydaje:  "+str(self.vydaje)+"\n" +
              " Zisk:    "+str(self.getZisk())+"\n")

class Boris(handler.ContentHandler):
    def __init__(self):
        self.inSlozky = False
        self.inSlozka = False
        self.inNazev = False
        self.inCena = False
        self.inKoktejly = False
        self.inKoktejl = False

        self.slozkyMap = {}
        self.koktejlyList = []
        self.slozka = Slozka()
        self.koktejl = Koktejly()
        self.idSlozky = 0       #pro slozku koktejlu
        self.mnozstviSlozky = 0 #pro slozku koktejlu

    def getSlozkyMap(self):
        return self.slozkyMap

    def getKoktejlyList(self):
        return self.koktejlyList

    def startElement(self, name, attrs):
        if(name == "slozky" ):
            self.inSlozky = True
        elif(name == "slozka"):
            self.inSlozka = True
            if(self.inKoktejly == False):
                self.slozka = Slozka()
                self.slozka.setId( int(attrs.get("id")) )
            else:
                self.idSlozky = int(attrs.get("id"))
        elif(name == "nazev"):
            self.inNazev = True
        elif(name == "cena"):
            self.inCena = True
        elif(name == "koktejly"):
            self.inKoktejly = True
        elif(name == "koktejl"):
            self.inKoktejl = True
            self.koktejl = Koktejly()
            self.koktejl.setId( int(attrs.get("id")) )

    def endElement(self, name):
        if(name == "nazev"):
            self.inNazev = False
        elif(name == "cena"):
            self.inCena = False
        elif(name == "slozka"):
            self.inSlozka = False
            if(self.inKoktejly == False):       #uzavreni vyplnene slozky self.slozka
                self.slozkyMap[self.slozka.getId()] = self.slozka
            else:
                slozka = self.slozkyMap[self.idSlozky]
                self.koktejl.addVydaj(slozka, self.mnozstviSlozky)
        elif(name == "slozky"):
            self.inSlozky == False
        elif(name == "koktejl"):
            self.inKoktejl = False
            self.koktejlyList.append(self.koktejl)
        elif(name == "koktejly"):
            self.inKoktejly = False

    def characters(self, content):
        if(self.inSlozka == True) and (self.inKoktejly == False):   #vrchni slozky
            if(self.inNazev == True):
                self.slozka.setNazev(content)
            if(self.inCena == True):
                self.slozka.setCena(float(content))
        elif(self.inKoktejl == True):
            if(self.inNazev == True):
                self.koktejl.setNazev(content)
            if(self.inCena == True):
                self.koktejl.setCena(float(content))
            if(self.inSlozky == True) and (self.inSlozka == True):
                self.mnozstviSlozky = float(content)


def main():
    inputFile = io.open('bar.xml', 'r', encoding='UTF-8')
    handler = Boris()
    parser = make_parser()
    parser.setContentHandler(handler)
    parser.parse(inputFile)

    inputFile.close()


    koktejly = handler.getKoktejlyList()

    #sorted(koktejly, key=methodcaller)
    koktejly.sort(key=methodcaller('getZisk'), reverse=True)

    for koktejl in koktejly:
        koktejl.print()


    """
    slozky = handler.getSlozkyMap()
    for keys, values in slozky.items():
        values.print()
    """

if __name__ == '__main__':
    main()