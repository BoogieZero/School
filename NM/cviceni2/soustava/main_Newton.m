function [] = main_Newton()
clc

X0 = [2;2];
N = 100;
tol = 0.001;

fce_1 = @(x,y) x.^2 + 4*y.^2 - 8*y;
fce_2 = @(x,y) x.^3 - y + 1;


fprintf('Newtonova metoda:\n\n')
X_aprox = Newton(fce_1,fce_2,X0,N,tol);

fprintf('Numericke reseni [x*, y*] = [%f, %f]\n', X_aprox(1),X_aprox(2))
fprintf('Funkèní hodnota [f(x*), f(y*)] = [%f, %f]\n', fce_1(X_aprox(1),X_aprox(2)), fce_2(X_aprox(1),X_aprox(2)))


