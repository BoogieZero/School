function [] = mocninna_metoda()
clear all
clc

%Input
A = [4 2 3;
     3 0 4;
     1 2 5];
 
y0 = [1;
      1;
      1];

%Settings
tol = 0.001;
max_it = 500;

%Initialization
y_old = y0;
lam_old = realmax;
it = 0;

%Function
while (it < max_it)
    y_new = A*y_old;
    [y_max, y_max_index] = max(y_new);
    
    lam = y_max / (y_old(y_max_index));
    
    err = abs(lam - lam_old);
    if(err <= tol)
        break
    end
    
    lam_old = lam;
    y_old = y_new;
    it = it + 1;
end

fprintf('Nalezene dominantni vlastni cislo: \t\t%f\n', lam)
fprintf('Dominantni vlastni cislo z "eig(A)": \t%f\n', max(eig(A)))