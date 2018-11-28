from enum import Enum
import xml.etree.ElementTree as ET

# ~==============================================
# Basic objects
# ~==============================================


class Ingredients:
    """
        Přepravka pro informace o ingrediencích
    """

    def __init__(self):
        self.__name = ""
        self.__unit = ""
        self.__price = ""

    @property
    def name(self):
        return self.__name

    @property
    def unit(self):
        return self.__unit

    @property
    def price(self):
        return self.__price

    @name.setter
    def name(self, value):
        self.__name = value

    @unit.setter
    def unit(self, value):
        self.__unit = value

    @price.setter
    def price(self, value):
        self.__price = value


class Cocktail:
    """
        Přepravka pro informace o jednotlivých koktailech
    """
    def __init__(self):
        self.__id = -1
        self.__name = ""
        self.__price = -1
        self.__ingredients = None

    @property
    def id(self):
        return self.__id

    @id.setter
    def id(self, value):
        self.__id = value

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value

    @property
    def price(self):
        return self.__price

    @price.setter
    def price(self, value):
        self.__price = value

    @property
    def ingredients(self):
        return self.__ingredients

    @ingredients.setter
    def ingredients(self, value):
        self.__ingredients = value


class CocktailIngredients:
    def __init__(self):
        self.__id = -1
        self.__amount = -1

    @property
    def id(self):
        return self.__id

    @id.setter
    def id(self, value):
        self.__id = value

    @property
    def amount(self):
        return self.__amount

    @amount.setter
    def amount(self, value):
        self.__amount = value

# ~==============================================
# Xml Support
# ~==============================================


class XmlOp:

    def __init__(self, text, operation, setter, descriptor=None):
        self.__text = text
        self.__operation = operation
        self.__setter = setter
        self.__descriptor = descriptor

    @property
    def descriptor(self):
        return self.__descriptor

    @property
    def text(self):
        return self.__text

    @property
    def operation(self):
        return self.__operation

    @property
    def setter(self):
        return self.__setter


class XmlOperation(Enum):
    find_all = 1,
    attribute = 2,
    find_text = 3,
    text = 4,
    create = 5

# ~==============================================
# Xml descriptors
# ~==============================================


class CocktailIngredientsDescriptor:

    __xml_iterate = None
    __xml_id = "id"
    __xml_root = "slozka"
    __xml_descriptor = [
        XmlOp("id", XmlOperation.attribute, "id"),
        XmlOp("", XmlOperation.text, "amount")
    ]

    def create_instance(self):
        return globals()["CocktailIngredients"]()

    @property
    def xml_id(self):
        return self.__xml_id

    @property
    def xml_root(self):
        return self.__xml_root

    @property
    def xml_descriptor(self):
        return self.__xml_descriptor

    @property
    def xml_iterate(self):
        return self.__xml_iterate


class IngredientsDescriptor:

    __xml_id = "id"
    __xml_iterate = "slozky"
    __xml_root = "slozka"
    __xml_descriptor = [
        XmlOp("id", XmlOperation.attribute, "id"),
        XmlOp("nazev", XmlOperation.find_text, "name"),
        XmlOp("cena", XmlOperation.find_text, "price"),
        XmlOp("jednotka", XmlOperation.find_text, "unit"),
    ]

    def create_instance(self):
        return globals()["Ingredients"]()

    @property
    def xml_id(self):
        return self.__xml_id

    @property
    def xml_root(self):
        return self.__xml_root

    @property
    def xml_descriptor(self):
        return self.__xml_descriptor

    @property
    def xml_iterate(self):
        return self.__xml_iterate


class CocktailDescriptor:

    __xml_id = "id"
    __xml_iterate = "koktejly"
    __xml_root = "koktejl"
    __xml_descriptor = [
        XmlOp("id", XmlOperation.attribute, "id"),
        XmlOp("nazev", XmlOperation.find_text, "name"),
        XmlOp("cena", XmlOperation.find_text, "price"),
        XmlOp("slozky", XmlOperation.find_all, "ingredients", CocktailIngredientsDescriptor()),
    ]

    def create_instance(self):
        return globals()["Cocktail"]()

    @property
    def xml_id(self):
        return self.__xml_id

    @property
    def xml_root(self):
        return self.__xml_root

    @property
    def xml_descriptor(self):
        return self.__xml_descriptor

    @property
    def xml_iterate(self):
        return self.__xml_iterate


def xml_element_factory(xml_root, class_descriptor):
    dic = {}

    if xml_root is None:
        return None

    for find in xml_root.findall(class_descriptor.xml_root):

        new_element = class_descriptor.create_instance()

        for xml_operation in class_descriptor.xml_descriptor:

            if xml_operation.operation == XmlOperation.find_all:
                child_list = xml_element_factory(find.find(xml_operation.text), xml_operation.descriptor)

                setattr(new_element, xml_operation.setter, child_list)

            elif xml_operation.operation == XmlOperation.attribute:

                setattr(new_element, xml_operation.setter, find.get(xml_operation.text))

                # xml_operation.setter(new_element, find.get(xml_operation.text))
                # new_element.setter = find.get(xml_operation.text)

            elif xml_operation.operation == XmlOperation.find_text:

                setattr(new_element, xml_operation.setter, find.find(xml_operation.text).text)

            elif xml_operation.operation == XmlOperation.text:
                setattr(new_element, xml_operation.setter, find.text)

            else:
                print("Something goes wrong!")

        dic[getattr(new_element, class_descriptor.xml_id)] = new_element

    return dic

if __name__ == '__main__':

    tree = ET.parse("bar.xml")

    root = tree.getroot()

    coctail_descriptor = CocktailDescriptor()
    ingredients_descriptor = IngredientsDescriptor()

    ingredients = xml_element_factory(root.iter(ingredients_descriptor.xml_iterate).__next__(), ingredients_descriptor)
    coctails = xml_element_factory(root.iter(coctail_descriptor.xml_iterate).__next__(), coctail_descriptor)

    print ("{0:2s} {1:25s} {2:5s}\t{3:5s}\t{4:5s}".format("id", "nazev", "cena", "vydaj", "zisk"))

    for id, coctail in coctails.items():
        costs = 0
        for i, coctail_ingredients in coctail.ingredients.items():
            costs += float(ingredients[i].price) * float(coctail_ingredients.amount)

        print("{0:2d} {1:25s} {2:5.2f}\t{3:5.2f}\t{4:5.2f}".format(int(coctail.id), coctail.name, float(coctail.price), costs, float(coctail.price) - costs))
