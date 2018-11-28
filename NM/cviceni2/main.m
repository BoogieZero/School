function [] = main()
clc

a = 0;
b = 10;
x0 = (a+b)/2;
N = 100;
tol = 0.008;

fce = @(x) x.^2 - 3*x - 5;
fder = @(x) 2*x - 3;
phi = @(x) sqrt(3*x + 5);


fprintf('Metoda proste iterace:\n\n')
x_aprox = prosta_iterace(fce,phi,a,b,x0,N,tol);

%fprintf('Numericke reseni x* = %f\n', x_aprox)
%fprintf('Funkèní hodnota f(x*) = %f\n', fce(x_aprox))


fprintf('\n\nNewtonova metoda:\n\n')
%x_aprox = Newton(fce,fder,a,b,x0,N,tol);

fprintf('Numericke reseni x* = %f\n', x_aprox)
fprintf('Funkèní hodnota f(x*) = %f\n', fce(x_aprox))