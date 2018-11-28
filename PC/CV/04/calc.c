#include <stdio.h>
#include <float.h>

float add(float a, float b) {
      return a + b;
}

float sub(float a, float b) {
      return a - b;
}

float mul(float a, float b) {
      return a * b;
}

float div(float a, float b) {
      if (b) return a / b;
      else return a > 0.0 ? FLT_MAX : -FLT_MAX;
}

int main() {
    float (*comp_fn) (float a, float b) = NULL;
    float x, y;
    char op;
    
    while (1) {
          op = '+';
          scanf("%f", &x);
          scanf("%s", &op);
          scanf("%f", &y);
          
          if (x == 0.0 && y == 0.0) return 0;
          
          switch (op) {
                 case '+': comp_fn = &add; break;
                 case '-': comp_fn = &sub; break;
                 case '*': comp_fn = &mul; break;
                 case '/': comp_fn = &div; break;       
                 default: printf("Bad op!\n"); continue;
          }
          
          printf(">%f\n", comp_fn(x, y));
    }
    
    return 0;
}
