void swap(int *a, int *b) {
	int temp;

	temp = *a;
	*a = *b;
	*b = temp;
}

int main() {
    int x = 5, y = 7;
    
    printf("Pre : %d, %d\n", x, y);
    swap(&x, &y);
    printf("Post: %d, %d\n", x, y);
    
    getch();
	
    return 0;
}
