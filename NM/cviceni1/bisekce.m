function[] = bisekce(fce,a,b,N,tol)

fa = fce(a);
fb = fce(b);
fprintf('F(a) = %f  F(b) = %f \n', fa, fb)

for i = 1:N
    
    s = (a+b)/2;
    fprintf('Aproximace reseni x%d = %f \n', i, s)
    
    fs = fce(s);
    fprintf('a = %f  b = %f s = %f\n', a, b, s)
    fprintf('F(a) = %f  F(b) = %f F(s) = %f\n', fa, fb, fs)
    fprintf('---------\n')
    
    if abs(fs) < tol && abs(b - a) < tol
        break;
    else
        if fa*fs < 0
            b = s;
            fb = fs;
        else
            a = s;
            fa = fs;
        end
    end
end
fprintf('--------------------- \n')
fprintf('Koren x* = %f \n', s)
fprintf('funkcni hodnota f(x*) = %f \n', fce(s))

