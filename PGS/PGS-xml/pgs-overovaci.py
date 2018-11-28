import io

# potrebne pro SAX
from xml.sax import handler, make_parser

# potrebne na razeni ...
from operator import attrgetter, methodcaller

class Jmeno:
    def __init__(self):
        self.letter = ''
        self.value = 1
        self.lengths = {}

    def getLetter(self):
        return self.jmeno

    def getValue(self):
        return self.value

    def getLenghts(self):
        return self.lengths

    def setLetter(self, letter):
        self.letter = letter

    def addValue(self):
        self.value += 1

    def addLength(self, value):
        if(value in self.lengths):
            num = self.lengths[value]
            self.lengths[value] = num + 1;
        else:
            self.lengths[value] = 1

    def print(self):
        print(self.letter + " : " + str(self.value))
        for key, val in self.lengths.items():
            numStar =  int( (val/self.value)*10 )+1
            star = ''
            for i in range(0, numStar):
                star += '*'

            #print("\t"+str(val)+" delky "+str(key)+" : "+str(val/self.value))
            print("\t"+str(val)+" delky "+str(key)+" : "+str(star))

class Boris(handler.ContentHandler):
    def __init__(self):
        self.inJmeno = False
        self.jmenaMap = {}


    def getJmenaMap(self):
        return self.jmenaMap

    def startElement(self, name, attrs):

        if(name == "jmeno"):
            self.inJmeno = True


    def endElement(self, name):

        if(name == "jmeno"):
            self.inJmeno = False

    def characters(self, content):

        if(self.inJmeno == True):
            #print(content[0]+" "+content+" len: "+str(len(content)))

            if(content[0] in self.jmenaMap):
                self.jmenaMap[content[0]].addValue()
                self.jmenaMap[content[0]].addLength(len(content))
            else:
                jmeno = Jmeno()
                jmeno.setLetter(content[0])
                jmeno.addLength(len(content))
                self.jmenaMap[content[0]] = jmeno



def main():
    inputFile = io.open('jmena.xml', 'r', encoding='UTF-8')
    handler = Boris()
    parser = make_parser()
    parser.setContentHandler(handler)
    parser.parse(inputFile)

    inputFile.close()


    jmenaMap = handler.getJmenaMap()
    """
    for keys, values in jmenaMap.items():
        print(str(keys)+" : "+str(values.getValue()))
    """


    for keys, values in jmenaMap.items():
        values.print()


    #jmenaMap['X'].print()

    """
    for i in range(0, 3):
        print("toa")
    """

if __name__ == '__main__':
    main()