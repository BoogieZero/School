function [] = Gauss_Seidel()
clear all
clc
%matice soustavy
A = [9 3 1;
     4 2 1;
     1 3 6];
%pravá strana soustavy 
b = [3;
     2;
     7];
% %============ Priklad 0 =============
% %matice soustavy
% A = [3 1 0;
%      1 4 0;
%      0 1 5];
% %pravá strana soustavy  
% b = [1;
%      2;
%      3];
% %===================================


% %============ Priklad 1 =============
% %matice soustavy
% A = [6 3 -1;
%      1 5 -2;
%      1 -3 4];
% %pravá strana soustavy  
% b = [5;
%      3;
%      1];
% %===================================

% %============ Priklad 2 =============
%  %matice soustavy
% A = [5 3 4;
%      2 5 4;
%      1 4 5];
% %pravá strana soustavy 
% b = [12;
%      11;
%      10];
% %====================================

% %============ Priklad 3 =============
%  %matice soustavy
% A = [1 2 -2;
%      1 1 1;
%      2 2 1];
% %pravá strana soustavy 
% b = [1;
%      3;
%      5];
% %====================================
 
%poèáteèní aproximace pro Jacobiho metodu 
x0 = [0;
      0;
      0];
%tolerance pro zastavení iterace  
tol = 0.001;
%macimální poèet iterací
max_it = 50;

x_new = x0;
iterace = 0;
error = inf;

%rozklad matice soustavy A = L + D + U
D = diag(diag(A)); % diagonála matice A tvoøí diagonální matici D
L = tril(A,-1);    % dolní trojúhelníková matice s nulovou diagonálou - ekvivalentní zápis: tril(A)-D
U = triu(A,1);     % horní trojúhelníková matice s nulovou diagonálou - ekvivalentní zápis: triu(A)-D

H = -(L+D)\U;      % iteraèní matice pro Jacobiho metodu
g = (L+D)\b;       % vektor g pro Jacobiho metodu

fprintf('Diagonala = \n')
disp(D);



fprintf('Iteraèní matice H = \n')
disp(H)
fprintf('Vlastní èísla iteraèní matice: \n')
disp(eig(H))

nvm = norm(H,inf)
disp(nvm)
   
while ((error > tol) && (iterace < max_it))
    iterace = iterace + 1;
    fprintf('%d. iterace:\n', iterace)
    x_old = x_new;
    
    x_new = H*x_old + g;         % výpoèet nové aproximace
    error = norm(x_new - x_old); % Eukleidovská norma rozdílu staré a nové aproximace øešení
    
    fprintf('Aproximace øešení SLAR x%d = \n', iterace)
    disp(x_new)
    fprintf('Chyba ||x%d - x%d|| = %.4f\n', iterace, iterace-1, error)
    pause
    fprintf('------------------\n')
end

fprintf('Numerické øešení SLAR je x = \n')
disp(x_new)




