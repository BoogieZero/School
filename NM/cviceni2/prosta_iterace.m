function [x_new] = prosta_iterace(fce,phi,a,b,x0,N,tol)

x_new = x0;
figure(1)
plot(x_new,0,'r.')
hold on
x = a:0.01:b;
plot(x,fce(x))
    
for k = 1:N
    x_old = x_new;
    
    x_new = phi(x_old);
    fprintf('x%d = %f\n', k, x_new)
    fprintf('old = %f  new = %f\n', x_old, x_new)
    
    plot(x_new,0,'r.')
   
    if abs(fce(x_new)) < tol || abs(x_new - x_old) < tol
        break; 
    end
end

xlim = get(gca,'xlim');  %Get x range 
plot([xlim(1) xlim(2)],[0 0], 'Color',[.7 .7 .7])
ylim = get(gca,'ylim');  %Get y range 
plot([0 0], [ylim(1) ylim(2)], 'Color',[.7 .7 .7])



