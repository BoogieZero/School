"""
SAX zpracovani souboru bar.xml
"""

import io

# potrebne pro SAX
from xml.sax import handler, make_parser

# potrebne na razeni ...
from operator import attrgetter

# domenova trida - slozka
class Slozka:
    def __init__(self):
        self.nazev = ''
        self.cena = ''

    def setId(self, id):
        self.id = id
        
    def getId(self):
        return self.id
    
    def setNazev(self, nazev):
        self.nazev = nazev
        
    def getNazev(self):
        return self.nazev
    
    def setCena(self, cena):
        self.cena = cena


    def getCena(self):
        return self.cena
    
# domenova trida - koktejl
class Koktejl:
    def __init__(self):
        self.nazev = ''
        self.cena = 0
        self.vydaje = 0
        self.zisk = 0
    
    def setId(self, id):
        self.id = id
        
    def getId(self):
        return self.id
    
    def setNazev(self, nazev):
        self.nazev = nazev
        
    def getNazev(self):
        return self.nazev;
    
    def setCena(self, cena):
        self.cena = cena
        
    def getCena(self):
        return self.cena
    
    def setZisk(self):
        self.zisk = self.cena - self.vydaje
        
    def getZisk(self):
        return self.zisk
    
    def addVydaj(self, slozky, id, mnozstvi):
        for slozka in slozky:
            if (slozka.getId() == id):
                self.vydaje += mnozstvi * slozka.cena
                break
    
    def getVydaje(self):
        return self.vydaje
        
            
# Definice nove tridy oddedene od ContentHandler
class BarHandler(handler.ContentHandler):
    
    # konstruktor tridy    
    def __init__(self):
        
        # inicializace dat tridy
        self.inSlozky = False   # indikator stavu 
        self.inKoktejly = False # indikatot stavu
        self.inNazev = False # indikatot stavu
        self.inCena = False # indikatot stavu
        self.inSlozka = False # indikatot stavu
        self.koktejlyList = []  # seznam koktejlu
        self.slozkyList = []    # seznam slozek

    # getter pro seznam koktejlu
    def getKoktejlyList(self):
        return self.koktejlyList
    
    # prekryti metodu startElement(self)
    def startElement(self, name, attrs):                        
        # ulozeni stavu, kde se prave ve XML nachazime
        if (name == "slozky") and (not self.inKoktejly):
            self.inSlozky = True
        elif (name == "koktejly"):
            self.inKoktejly = True
        elif (name == "nazev"):
            self.inNazev = True
        elif (name == "cena"):
            self.inCena = True
        elif (name == "slozka"):
            self.inSlozka = True
            if self.inSlozky:
                #vytvoreni instance slozky (jsme uvnitr slozky, ne koktejlu)
                self.slozka = Slozka()
                self.slozka.setId(int(attrs.get("id")))
            else:
                self.id = int(attrs.get("id"))                
        elif (name == "koktejl"):
            # vytvoreni instance koktejlu
            self.koktejl = Koktejl()
            self.koktejl.setId(int(attrs.get("id")))

    # prekryti metody endElement(self, name)
    def endElement(self, name):
        # ulozeni stavu, kde se prave ve XML nachazime
        if (name == "slozky") and (not self.inKoktejly):
            self.inSlozky = False
        elif (name == "koktejly"):
            self.inKoktejly = False
        elif (name == "nazev"):
            self.inNazev = False
        elif (name == "cena"):
            self.inCena = False
        elif (name == "slozka"):
            self.inSlozka = False
            if self.inSlozky:
                self.slozkyList.append(self.slozka)                
            else:
                # pridani vydaje k aktualnimu koktejlu
                self.koktejl.addVydaj(self.slozkyList, self.id, self.mnozstvi)                
        elif (name == "koktejl"):
            self.koktejl.setZisk()                   # vypocet zisku z koktejlu            
            self.koktejlyList.append(self.koktejl)   # pridani koktejlu do seznamu


    # prekryti metody characters(self, chrs)    
    def characters(self, chrs):                                 
        if self.inCena:
            if self.inSlozky:
                self.slozka.setCena(float(chrs))
            else:
                self.koktejl.setCena(float(chrs))
                
        elif self.inNazev:
            if self.inSlozky:
                self.slozka.setNazev(chrs)
            else:
                self.koktejl.setNazev(chrs)
                
        elif self.inSlozka and self.inKoktejly:
            self.mnozstvi = float(chrs)

        
# Funkce pro tisk seznamu
def printList(koktejlyList):
    print ("{0:2s} {1:25s} {2:5s}\t{3:5s}\t{4:5s}".format("id", "nazev", "cena", "vydaj", "zisk"))
    for koktejl in koktejlyList:
        print ("{0:2d} {1:25s} {2:5.2f}\t{3:5.2f}\t{4:5.2f}".format(koktejl.getId(), koktejl.getNazev(), koktejl.getCena(), koktejl.getVydaje(), koktejl.getZisk()))


# Hlavni funkce 
def main():

    # vytvoreni instance handleru
    handler = BarHandler()

    # vytvoreni instance parseru
    parser = make_parser()

    # nastaveni handleru
    parser.setContentHandler(handler)

    # otevreni souboru pro cteni
    inFile = io.open('bar.xml', 'r', encoding='UTF-8')

    # parsovani souboru
    parser.parse(inFile)

    # ziskani seznamu koktejlu
    koktejly = handler.getKoktejlyList()

    # sestupne razeni seznamu koktejlu podle hodnoty argumentu zisk v koktejlu
    koktejly.sort(key=attrgetter('zisk'), reverse=True)

    # tisk koktejlu do konzole
    printList(koktejly)

    #zavreni souboru
    inFile.close()
    
main()
