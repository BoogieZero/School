function [] = mocninna_metoda()
clear all
clc

A = [2 0 0;
     2 2 1;
     1 1 2];
 
y0 = [1;
      1;
      1];
  
tol = 0.001;
max_it = 500;

y_old = y0
lam_old = realmax

it = 0
while (it < max_it)
    y_new = A*y_old
    [y_max, y_max_index] = max(y_new)
    
    lam = y_max / (y_old(y_max_index))
    err = abs(lam - lam_old)
    
    if(err <= tol)
        break
    end
    
    lam_old = lam
    y_old = y_new
    it = it + 1
end

fprintf('Newtonova metoda:\n\n')
disp(lam)




