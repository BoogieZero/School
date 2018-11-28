function text=predpis_newton(x,f,koef,stupen)
%
% Vypise tvar Newtonova polynomu

c1=num2str(koef(1));
text=['N_',sprintf('%d',stupen-1),'(x) = ', c1 ];

for i=2:stupen
 c1=num2str(abs(koef(i)));
 c2=num2str(x(i-1));
 if koef(i) >= 0 
   text=[text,' + ',c1]; 
 else
   text=[text,' - ',c1]; 
 end;
 for k=2:i
   c3=num2str(abs(x(k-1)));
   if x(k-1) >= 0 
     text=[text,'(x-',c3,')'];
   else
     text=[text,'(x+',c3,')'];
   end;  
 end;  
end;


