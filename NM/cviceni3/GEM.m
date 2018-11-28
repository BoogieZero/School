function x = GEM(A,b)
 
n = size(A,1);

%p��m� chod GEM
for k = 1:n-1
   for i = k+1:n % element�rn� �prava i-t�ho ��dku
      multiplikator = - A(i,k)/A(k,k); %v�po�et multiplik�toru
      for j = k+1:n % sta�� od k+1 proto�e v�me, �e poddiagon�ln� prvky nulujeme
         A(i,j) = A(i,j) + multiplikator*A(k,j);
      end
      b(i) = b(i) + multiplikator*b(k); 
   end
   A(k+1:n,k) = 0; %neni nutn� nulovat - zde jen kv�li v�pisu
   fprintf('Matice soustavy po %d. kroku GEM\n', k)
   disp(A)
end

%zp�tn� chod GEM
x = zeros(n,1);
for l = n:-1:1
   pom = 0;
   for j = l+1:n
      pom = pom + A(l,j)*x(j);
   end
   x(l) = 1/A(l,l) * (b(l) - pom);
end 



