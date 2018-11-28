def main():
    ntice = ('hrebik', 5, 'pila', 400, 'kladivo', 250, 'lopata', 300)
    dic = {}

    k = 0
    for i in range(4):
        dic[ntice[k]] = ntice[k+1]
        k += 2

    for zbozi in dic:
        if(dic[zbozi] > 100):
            print('{0} {1}'.format(zbozi, dic[zbozi]))

main()