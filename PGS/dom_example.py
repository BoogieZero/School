from enum import Enum
from xml.dom import minidom as dom


class IngredientsDescriptor(Enum):
    tag_root = "slozky"
    tag_nodes = "slozka"
    node_id = "id"
    node_name = "nazev"
    node_unit = "jednotka"
    node_price = "cena"


class CocktailsDescriptor(Enum):
    tag_root = "koktejly"
    tag_nodes = "koktejl"
    tag_ingredients = "slozky"
    tag_ingredient = "slozka"

    node_id = "id"
    node_name = "nazev"
    node_price = "cena"


class Ingredients:
    def __init__(self, name, unit, price):
        self.__name = name
        self.__unit = unit
        self.__price = price

    @property
    def name(self):
        return self.__name

    @property
    def unit(self):
        return self.__unit

    @property
    def price(self):
        return self.__price


class CocktailIngredients:
    def __init__(self, id, amount):
        self.__id = id
        self.__amount = amount

    @property
    def id(self):
        return self.__id

    @property
    def amount(self):
        return self.__amount


class Cocktail:
    def __init__(self, id, name, price, ingredients):
        self.__id = int(id)
        self.__name = name
        self.__price = int(price)
        self.__ingredients = ingredients

    @property
    def id(self):
        return self.__id

    @property
    def name(self):
        return self.__name

    @property
    def price(self):
        return self.__price

    @property
    def ingredients(self):
        return self.__ingredients


def prepare_ingredients(dom_handler, ingredients_descriptor):
    dic = {}

    class_root = dom_handler.getElementsByTagName(ingredients_descriptor.tag_root.value)

    if len(class_root) <= 0:
        raise Exception("No class root element!")

    for n in class_root[0].getElementsByTagName(ingredients_descriptor.tag_nodes.value):
        # print("Id: ", n.getAttribute(ingredients_descriptor.node_id.value))
        #
        # print("Name: ", n.getElementsByTagName(ingredients_descriptor.node_name.value)[0].firstChild.data)
        # print("Unit: ", n.getElementsByTagName(ingredients_descriptor.node_unit.value)[0].firstChild.data)
        # print("Price: ", n.getElementsByTagName(ingredients_descriptor.node_price.value)[0].firstChild.data)

        dic[n.getAttribute(ingredients_descriptor.node_id)] = Ingredients(
                n.getElementsByTagName(ingredients_descriptor.node_name.value)[0].firstChild.data,
                n.getElementsByTagName(ingredients_descriptor.node_unit.value)[0].firstChild.data,
                n.getElementsByTagName(ingredients_descriptor.node_price.value)[0].firstChild.data)

    return dic


def prepare_cocktails(dom_handler, cocktail_descriptor):
    dic = []

    class_root = dom_handler.getElementsByTagName(cocktail_descriptor.tag_root.value)

    if len(class_root) <= 0:
        raise Exception("No class root element!")

    for n in class_root[0].getElementsByTagName(cocktail_descriptor.tag_nodes.value):
        # print("ID: ", n.getAttribute(cocktail_descriptor.node_id.value))
        #
        # print("Name: ", n.getElementsByTagName(cocktail_descriptor.node_name.value)[0].firstChild.data)
        # print("Price: ", n.getElementsByTagName(cocktail_descriptor.node_price.value)[0].firstChild.data)

        ingredients = n.getElementsByTagName(cocktail_descriptor.tag_ingredients.value)

        list = []

        for i in ingredients[0].getElementsByTagName(cocktail_descriptor.tag_ingredient.value):
            list.append(CocktailIngredients(i.getAttribute(cocktail_descriptor.node_id), i.firstChild.data))

        dic.append(Cocktail(n.getAttribute(cocktail_descriptor.node_id.value),
                            n.getElementsByTagName(cocktail_descriptor.node_name.value)[0].firstChild.data,
                            n.getElementsByTagName(cocktail_descriptor.node_price.value)[0].firstChild.data,
                            list))

    return dic


if __name__ == '__main__':

    doc = dom.parse("bar.xml")

    ingredients = prepare_ingredients(doc, IngredientsDescriptor)
    cocktails = prepare_cocktails(doc, CocktailsDescriptor)

    print ("{0:2s} {1:25s} {2:5s}\t{3:5s}\t{4:5s}".format("id", "nazev", "cena", "vydaj", "zisk"))

    for cocktail in cocktails:
        costs = 0
        for i in cocktail.ingredients:
            costs += float(ingredients[i.id].price) * float(i.amount)

        print("{0:2d} {1:25s} {2:5.2f}\t{3:5.2f}\t{4:5.2f}".format(cocktail.id, cocktail.name, cocktail.price, costs, cocktail.price - costs))
