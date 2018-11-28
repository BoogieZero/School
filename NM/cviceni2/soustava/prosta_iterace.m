function [x_new,y_new] = prosta_iterace(fce_1,phi_1,fce_2,phi_2,x0,y0,N,tol)

x_new = x0;
y_new = y0;

figure(1)
hold on

for k = 1:N
    x_old = x_new;
    y_old = y_new;
    
    x_new = phi_1(x_old,y_old);
    y_new = phi_2(x_old,y_old);
    fprintf('[x%d, y%d] = [%f, %f]\n', k, k, x_new, y_new)
    
    
    error1 = norm([x_old,y_old] - [x_new, y_new]);
    fprintf('norm1: %f\n', error1)
    %plot(k,error1,'.')
    error2 = norm([fce_1(x_old,y_old),fce_2(x_old,y_old)] - [fce_1(x_new,y_new),fce_2(x_new,y_new)]);
    fprintf('norm2: %f\n', error2)
    if error1 < tol || error2 < tol
        break; 
    end
end

xlim = get(gca,'xlim');  %Get x range 
plot([xlim(1) xlim(2)],[0 0], 'Color',[.7 .7 .7])
ylim = get(gca,'ylim');  %Get y range 
plot([0 0], [ylim(1) ylim(2)], 'Color',[.7 .7 .7])



