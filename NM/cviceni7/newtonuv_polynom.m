function newtonuv_polynom()
%
% Sestrojeni Newtonova interpolacniho polynomu pro funkci zadanou tabulkou [x;f(x)]

%============== ZADANI ====================================================
x = [-1, 2, 3];
f = [2, 3, 0];
%==========================================================================

clc;
presah=10;
pocet_deleni=1000;
clear texty;
barva=['r','m','g','c','k'];
styl=['- ';'--';'-.';': '];

figure(1);
clf;
hold on;
zoom on;
set(figure(1),'Units','centimeters');
set(figure(1),'Position',[10 3 18 15]);


if ~exist('zobraz_vse')
  zobraz_vse=0;
end;
if ~exist('x') | ~exist('f')
  [x,f]=zadej_body;
  clc;
else
  xmin=min(x);
  xmax=max(x);
  fmin=min(f);
  fmax=max(f);
  axis([xmin-presah/100*(xmax-xmin) xmax+presah/100*(xmax-xmin) ...
	fmin-presah/100*(fmax-fmin) fmax+presah/100*(fmax-fmin)]); 
  for i=1:max(size(x))
    plot(x(i),f(i),'ro');
  end;  
end;  

[sortx,indexy]=sort(x);
%f=f(indexy);
n=max(size(x));
deleni=(sortx(n)-sortx(1))/pocet_deleni;
zoom on;

disp('----------------------------------------------------------------');
disp(sprintf('  Newtonuv interpolacni polynom pro funkci danou tabulkou'));
disp(' ');
disp(sprintf(' |    x(k)    |    f(k)    |'));
disp(sprintf(' ==========================='));
for i = 1:n
  disp(sprintf(' | %d | %9.4f  | %9.4f  |', i-1, x(i),f(i)));
end;  

A=zeros(n);
A(:,1)=f';
for k=2:n,
  for i=k:n,
    A(i,k)=(A(i,k-1)-A(i-1,k-1))/(x(i)-x(i-k+1));
  end;
end;

[xk,fk]=polynom(x,f,deleni,presah/2);

xmin=min(xk);
xmax=max(xk);
fmin=min(fk);
fmax=max(fk);

axis([xmin-presah/100*(xmax-xmin) xmax+presah/100*(xmax-xmin) ...
      fmin-presah/100*(fmax-fmin) fmax+presah/100*(fmax-fmin)]); 
% disp(' ');
% disp('Stiskni klavesu');
% disp(' ');
% pause;

  [xk,fk,koeficienty]=polynom(x,f,deleni,presah/2);  
  plot(xk,fk,'b-');
  plot(x,f,'ro'); 

n=max(size(koeficienty));

text='N(x) = ';
if koeficienty(1)<0 
  text=[text,'- '];
end;  
for i=n-1:-1:0;
  c1=num2str(abs(koeficienty(n-i)));
  c2=num2str(i);
  if i>1
    if koeficienty(n-i+1)>=0
      text=[text, c1,' x^', c2, ' + '];
    else       
      text=[text, c1,' x^', c2, ' - '];
    end;   
  else
    if i==1
      if koeficienty(n)>=0
        text=[text, c1,' x',' + '];
      else       
        text=[text, c1,' x',' - '];
      end;   
    else  
      text=[text, c1];    
    end;
  end; 
end;
disp(' ');
disp(text);
disp(' ');

% koeficienty = polyfit(x,f,max(size(x))-1)







