function [xk,fk,koeficienty]=polynom(x,f,deleni,presah)
%
% Vrati interpolovane hodnoty xk, fk pro deleni
% (presah v % uvede rozsireni intervalu <x1,xn>)
% koeficienty = vektor koeficientu polynomu

[x,indexy]=sort(x);
f=f(indexy);
n=max(size(x));
koef=zeros(n);

xk=x(1)-presah/100*(x(n)-x(1)):deleni:x(n)+presah/100*(x(n)-x(1));
koeficienty = polyfit(x,f,max(size(x))-1);
for i=1:length(koeficienty)
  if abs(koeficienty(i))<1e-12
     koeficienty(i)=0;
  end;
end;  
for i=1:max(size(xk))
  fk(i)=0;
  for j=1:n
    fk(i)=fk(i)+koeficienty(j)*xk(i)^(n-j);
  end;
end
















