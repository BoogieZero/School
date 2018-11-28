#include <cstdlib>
#include <cstdint>
#include <iostream>
#include <string.h>
#include <cstdint>
#include <stdio.h>
#include <stdint.h>
#include <vector>

//pocitame s FAT32 MAX - tedy horni 4 hodnoty
const int32_t FAT_UNUSED = INT32_MAX - 1;
const int32_t FAT_FILE_END = INT32_MAX - 2;
const int32_t FAT_BAD_CLUSTER = INT32_MAX - 3;
const int32_t FAT_DIRECTORY = INT32_MAX - 4;

const int16_t CLUSTER_SIZE = 256;
const int32_t USABLE_CLUSTER_COUNT = 251;


struct Boot_record {
	char volume_descriptor[250];    //popis vygenerovaného FS
	int8_t fat_type;                //typ FAT (FAT12, FAT16...) 2 na fat_type - 1 clusterù
	int8_t fat_copies;              //poèet kopií FAT tabulek
	int16_t cluster_size;			//velikost clusteru
	int32_t usable_cluster_count;   //max poèet clusterù, který lze použít pro data (-konstanty)
	char signature[9];              //login autora FS
};// 272B

  //pokud bude ve FAT FAT_DIRECTORY, budou na disku v daném clusteru uloženy struktury o velikosti sizeof(directory) = 24B
struct Directory {
	char name[13];                  //jméno souboru, nebo adresáøe ve tvaru 8.3'/0' 12 + 1
	bool isFile;                    //identifikace zda je soubor (TRUE), nebo adresáø (FALSE)
	int32_t size;                   //velikost položky, u adresáøe 0
	int32_t start_cluster;          //poèáteèní cluster položky
};// 24B

struct Cluster {
	char value[CLUSTER_SIZE];
};

int main(int argc, char** argv) {

	FILE *fp;
	struct Boot_record br;
	struct Directory root_a, root_b, root_c, root_d;

	//Clear with '/0'
	memset(br.signature, '\0', sizeof(br.signature));
	memset(br.volume_descriptor, '\0', sizeof(br.volume_descriptor));

	
	//Volume settings
	strcpy_s(br.volume_descriptor, "Testiovaci - FAT8 - tedy 256 clusteru, cluster 256B - 4 rezervovane - pouzitelne 0-251 (zde neni nutne rezervovat)");
	br.fat_type = 8;				//FAT 8
	br.fat_copies = 2;				//Two copies of FAT
	br.cluster_size = CLUSTER_SIZE;	//Number of clusters
	br.usable_cluster_count = 251;	//Non-reserved clusters
	strcpy_s(br.signature, "Hamet");
	
	//Test files

	//A
	memset(root_a.name, '\0', sizeof(root_a.name));
	root_a.isFile = true;
	strcpy_s(root_a.name, "cisla.txt");
	root_a.size = 135;
	root_a.start_cluster = 1;

	//B
	memset(root_b.name, '\0', sizeof(root_b.name));
	root_b.isFile = true;
	strcpy_s(root_b.name, "pohadka.txt");
	root_b.size = 5975;
	root_b.start_cluster = 2;

	//C
	memset(root_c.name, '\0', sizeof(root_c.name));
	root_c.isFile = 1;
	strcpy_s(root_c.name, "msg.txt");
	root_c.size = 396;
	root_c.start_cluster = 30;

	//D
	memset(root_d.name, '\0', sizeof(root_d.name));
	root_d.isFile = 0;
	strcpy_s(root_d.name, "direct-1");
	root_d.size = 0;
	root_d.start_cluster = 29;
	
	//Clusters
	std::vector<Cluster> clusters(br.usable_cluster_count);

	//FAT
	std::vector<int32_t> fat(br.usable_cluster_count);
	fat[0] = FAT_DIRECTORY;	//root


	struct Directory dir;
	printf("Hello world\n");
	printf("size: %d\n", sizeof(bool));
	system("pause");
}