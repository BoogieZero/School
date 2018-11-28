function[] = main()
clc

a = 0;
b = 5;

N = 100;
tol = 0.01;

fprintf('BISEKCE:\n')
%bisekce(@zadani_fce1,a,b,N,tol);
fprintf('========================================\n')
fprintf('REGULA FALSI:\n')
regula_falsi(@zadani_fce1,a,b,N,tol);