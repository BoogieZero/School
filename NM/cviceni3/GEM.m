function x = GEM(A,b)
 
n = size(A,1);

%pøímý chod GEM
for k = 1:n-1
   for i = k+1:n % elementární úprava i-tého øádku
      multiplikator = - A(i,k)/A(k,k); %výpoèet multiplikátoru
      for j = k+1:n % staèí od k+1 protože víme, že poddiagonální prvky nulujeme
         A(i,j) = A(i,j) + multiplikator*A(k,j);
      end
      b(i) = b(i) + multiplikator*b(k); 
   end
   A(k+1:n,k) = 0; %neni nutné nulovat - zde jen kvùli výpisu
   fprintf('Matice soustavy po %d. kroku GEM\n', k)
   disp(A)
end

%zpìtný chod GEM
x = zeros(n,1);
for l = n:-1:1
   pom = 0;
   for j = l+1:n
      pom = pom + A(l,j)*x(j);
   end
   x(l) = 1/A(l,l) * (b(l) - pom);
end 



