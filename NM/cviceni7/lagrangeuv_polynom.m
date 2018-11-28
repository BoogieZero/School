function lagrangeuv_polynom()
clc

%=============== ZADANI ==============================
xi = 0:0.5:1;                       % body interpolace
fi = xi.^4 - xi.^3 + 2;             % funk�n� hodnoty
fce = @(xx) xx.^4 - xx.^3 + 2;
%====================================================
% %=============== ZADANI ==============================
% xi = 0:pi:10*pi;                       % body interpolace
% fi = sin(xi);                          % funk�n� hodnoty
% fce = @(xx) sin(xx);
% %====================================================


% v�pis zad�n� do tabulky
fprintf('  Lagrangeuv interpolacni polynom pro funkci danou tabulkou\n');
fprintf(' |    x(k)    |    f(k)    |\n');
fprintf(' ===========================\n');
for i = 1:length(xi)
  fprintf(' | %d | %9.4f  | %9.4f  |\n', i-1, xi(i),fi(i));
end;  

% d�len� intervalu
dx = 0.01;
x = xi(1):dx:xi(end);

Ln = 0.*x;

% cyklus pro sestrojen� Lagrangeova polynomu
for i = 1:length(xi)
    ln = 0.*x + 1;
    for k = 1:length(xi)
        if i ~= k
            ln = ln.*((x - xi(k))./(xi(i) - xi(k)));  
        end
    end
%     %---------------
%     % vykresleni bazovych funkci
%     figure(1)
%     plot(x, ln)
%     hold on
%     %---------------
    
    Ln = Ln + fi(i).*ln;              % Lagrange�v polynom
end

% vykreslen� polynomu a zadan�ch bod�
figure(2)
plot(x,Ln)
hold on
plot(xi,fi,'ro')
plot(x,fce(x),'r')










