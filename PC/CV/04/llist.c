#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct Person {
               char name[32];
               int age;
               struct Person *next;
        } person;

void printlist(person *head) {
     while (head) {
           printf("%s - %d\n", head->name, head->age);
           head = head->next;
     }
}

void addtolist(person **head, char *name, int age) {
     person *temp, *p;
     
     temp = malloc(sizeof(person));
     strcpy(temp->name, name);
     temp->age = age;
     
     if (*head) {
          p = *head;
          while (p->next) p = p->next;
          p->next = temp;
     }
     else {
          *head = temp;
     }
}

int main() {
    person *head = NULL;
    int i;
    
    for (i = 0; i < 10; i++)
        addtolist(&head, "Franta", i);
    printlist(head);
    
    getch();
    return 0;
}
