function [] = main()
% Gaussova eliminaèní metoda pro reseni soustavy Ax = b
clc

A = [2,-5,1;2,-4,0;-6,19,-8];
b = [-3;-7;-15];

fprintf('Resime soustavu uzitim GEM, kde matice soustavy je\n A = \n')
disp(A)
fprintf('s pravou stranou \n b = \n')
disp(b)

x = GEM(A,b);
% x = Gauss_Seidel();

fprintf('Reseni soustavy x = \n')
disp(x)
