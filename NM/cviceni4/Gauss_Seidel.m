function [] = Gauss_Seidel()
clear all
clc
%matice soustavy
A = [9 3 1;
     4 2 1;
     1 3 6];
%prav� strana soustavy 
b = [3;
     2;
     7];
% %============ Priklad 0 =============
% %matice soustavy
% A = [3 1 0;
%      1 4 0;
%      0 1 5];
% %prav� strana soustavy  
% b = [1;
%      2;
%      3];
% %===================================


% %============ Priklad 1 =============
% %matice soustavy
% A = [6 3 -1;
%      1 5 -2;
%      1 -3 4];
% %prav� strana soustavy  
% b = [5;
%      3;
%      1];
% %===================================

% %============ Priklad 2 =============
%  %matice soustavy
% A = [5 3 4;
%      2 5 4;
%      1 4 5];
% %prav� strana soustavy 
% b = [12;
%      11;
%      10];
% %====================================

% %============ Priklad 3 =============
%  %matice soustavy
% A = [1 2 -2;
%      1 1 1;
%      2 2 1];
% %prav� strana soustavy 
% b = [1;
%      3;
%      5];
% %====================================
 
%po��te�n� aproximace pro Jacobiho metodu 
x0 = [0;
      0;
      0];
%tolerance pro zastaven� iterace  
tol = 0.001;
%macim�ln� po�et iterac�
max_it = 50;

x_new = x0;
iterace = 0;
error = inf;

%rozklad matice soustavy A = L + D + U
D = diag(diag(A)); % diagon�la matice A tvo�� diagon�ln� matici D
L = tril(A,-1);    % doln� troj�heln�kov� matice s nulovou diagon�lou - ekvivalentn� z�pis: tril(A)-D
U = triu(A,1);     % horn� troj�heln�kov� matice s nulovou diagon�lou - ekvivalentn� z�pis: triu(A)-D

H = -(L+D)\U;      % itera�n� matice pro Jacobiho metodu
g = (L+D)\b;       % vektor g pro Jacobiho metodu

fprintf('Diagonala = \n')
disp(D);



fprintf('Itera�n� matice H = \n')
disp(H)
fprintf('Vlastn� ��sla itera�n� matice: \n')
disp(eig(H))

nvm = norm(H,inf)
disp(nvm)
   
while ((error > tol) && (iterace < max_it))
    iterace = iterace + 1;
    fprintf('%d. iterace:\n', iterace)
    x_old = x_new;
    
    x_new = H*x_old + g;         % v�po�et nov� aproximace
    error = norm(x_new - x_old); % Eukleidovsk� norma rozd�lu star� a nov� aproximace �e�en�
    
    fprintf('Aproximace �e�en� SLAR x%d = \n', iterace)
    disp(x_new)
    fprintf('Chyba ||x%d - x%d|| = %.4f\n', iterace, iterace-1, error)
    pause
    fprintf('------------------\n')
end

fprintf('Numerick� �e�en� SLAR je x = \n')
disp(x_new)




