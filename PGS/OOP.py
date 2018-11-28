class Rasa:
    def stekat(self):
        raise NotImplementedError("Kazdy pes by mel stekat!")


class Ovcak(Rasa):
    def __init__(self):
        pass

    def stekat(self):
        print("Ja, ja, ja und Haf!\n")


class MojeVyjimka(Exception):
    def __init__(self, value):
        self.value = value

    def __str__(self):
        return repr(self.value)


class Abstract():
    def neco(self):
        raise NotImplementedError("Implementuj neco!")

class Abstract1(Abstract):
    def __init__(self):
        pass

    def neco(self):
        print("Abstract 1 implementoval!")

class Abstrac2():
    def neco(self):
        print("Abstract 2 implementoval!")

class Mix(Abstract1):
    pass

if __name__ == '__main__':
    m = Mix()
    m.neco()

    # o = Ovcak()
    # o.stekat()
    #
    # try:
    #     assert isinstance(o, MojeVyjimka)
    # except (AssertionError, NameError, IOError):
    #     print("Toto neni instance psa!\n")
    #
    # try:
    #     raise MojeVyjimka("Hello name error!")
    # except MojeVyjimka as m:
    #     print("> ", m.value)
