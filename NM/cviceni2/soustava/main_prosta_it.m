function [] = main_prosta_it()
clc

x0 = 2;
y0 = 2;
N = 100;
tol = 0.001;

fce_1 = @(x,y) x.^2 + 4*y.^2 - 8*y;
fce_2 = @(x,y) x.^3 - y + 1;
phi_1 = @(x,y) nthroot(y - 1,3);
phi_2 = @(x,y) (1/2)*nthroot(8*y - x.^2,2);


fprintf('Metoda proste iterace:\n\n')
[x_aprox,y_aprox] = prosta_iterace(fce_1,phi_1,fce_2,phi_2,x0,y0,N,tol);

fprintf('Numericke reseni [x*, y*] = [%f, %f]\n', x_aprox, y_aprox)
fprintf('Funkèní hodnota [f(x*), f(y*)] = [%f, %f]\n', fce_1(x_aprox,y_aprox), fce_2(x_aprox,y_aprox))


