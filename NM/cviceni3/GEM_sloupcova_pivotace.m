function x = GEM_sloupcova_pivotace(A,b)

n = size(A,1);
index = zeros(n-1,1);

for k = 1:n-1
% sloupcova pivotace -> prehazeni radku
  [y,index(k,1)] = max(abs(A(k:n,k))); % najdeme prvek s nejvetsi absolutni hodnotou v danem sloupci
  index(k,1) = index(k,1) + k - 1;
  tmp1 = A(index(k,1),:);
  tmp2 = b(index(k,1));
  A(index(k,1),:) = A(k,:);
  b(index(k,1)) = b(k);
  A(k,:) = tmp1;
  b(k) = tmp2;
  
  if index(k,1) ~= k % pokud index(k,1) = k, potom již je na diagonálním prvku nejvetsi pivot a k prohozeni radku nedojde
    fprintf('V %d. kroku GEM menim %d. øádek s %d. radkem. \n A = \n', k, index(k,1), k);
    disp(A)
  end;
  
  for i = k+1:n
     multiplikator = - A(i,k)/A(k,k);
     for j = k+1:n %od 1 neni nutne       
        A(i,j) = A(i,j) + multiplikator*A(k,j);
     end
     b(i) = b(i) + multiplikator*b(k);
  end
   A(k+1:n,k) = 0; %neni nutné nulovat - zde jen kvùli výpisu
   fprintf('Matice soustavy po %d. kroku GEM\n', k)
   disp(A)
end

x = zeros(n,1);
for l = n:-1:1
   pom = 0;
   for j = l+1:n
      pom = pom + A(l,j) * x(j);
   end
   x(l) = 1/A(l,l) * (b(l) - pom);
end 











