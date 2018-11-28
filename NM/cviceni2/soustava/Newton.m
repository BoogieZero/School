function [X_new] = Newton(fce_1,fce_2,X0,N,tol)

X_new = X0;

figure(1)
hold on

for k = 1:N
    X_old = X_new;

    Fder = [2*X_old(1), 8*X_old(2) - 8;3*X_old(1)^2, -1];
    h = Fder\(-[fce_1(X_old(1),X_old(2));fce_2(X_old(1),X_old(2))]);
    X_new = X_old + h;
    
    fprintf('[x%d, y%d] = [%f, %f]\n', k, k, X_new(1), X_new(2))
    
    error = norm(X_old - X_new);
    plot(k,error,'.')
   
    if error < tol
        break; 
    end
end

xlim = get(gca,'xlim');  %Get x range 
plot([xlim(1) xlim(2)],[0 0], 'Color',[.7 .7 .7])
ylim = get(gca,'ylim');  %Get y range 
plot([0 0], [ylim(1) ylim(2)], 'Color',[.7 .7 .7])



