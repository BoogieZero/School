#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <windows.h>
#include <strsafe.h>

const int32_t FAT_UNUSED = UINT8_MAX - 0;
const int32_t FAT_FILE_END = UINT8_MAX - 1;
const int32_t FAT_BAD_CLUSTER = UINT8_MAX - 2;
const int32_t FAT_DIRECTORY = UINT8_MAX - 3;

#define CLUSTER_SIZE 256
#define USABLE_CLUSTERS 251
#define DIRECTORIES_PER_CLUSTER 10
#define CLEAR_SIGN '\0'

#define NUM_THREADS 1
#define BUF_SIZE 255

static int data_t = 5;

static struct cluster *clusters;		//array of clusters
static uint8_t *fat;					//fat table
static uint8_t *fat_sec;				//secondary fat table
static uint8_t min_free_cluster = 1;	//index of first possible unused cluster for writing

struct boot_record {
	char volume_descriptor[250];    //popis vygenerovaného FS
	int8_t fat_type;                //typ FAT (FAT12, FAT16...) 2 na fat_type - 1 clusterù
	int8_t fat_copies;              //poèet kopií FAT tabulek
	int16_t cluster_size;           //velikost clusteru
	int32_t usable_cluster_count;   //max poèet clusterù, který lze použít pro data (-konstanty)
	char signature[9];              //login autora FS
};// 272B

//pokud bude ve FAT FAT_DIRECTORY, budou na disku v daném clusteru uloženy struktury o velikosti sizeof(directory) = 24B
struct node {
	char name[13];                  //jméno souboru, nebo adresáøe ve tvaru 8.3'/0' 12 + 1
	bool isFile;                    //identifikace zda je soubor (TRUE), nebo adresáø (FALSE)
	int32_t size;                   //velikost položky, u adresáøe 0
	int32_t start_cluster;          //poèáteèní cluster položky
};// 24B

struct cluster {
	char value[CLUSTER_SIZE];
};

/*
checks if name have correct length
	name:	name for check
	return:	true	for correct lentgh
			false	for incorrect length
*/
bool name_check(char *name) {
	int len = strlen(name);
	if (len <= 12 && len > 3 && name[len - 4] == '.') {
		return true;
	}
	return false;
}

/*
checks if in directory specified by cluster index position
	name:		name for checking
	postition:	directory cluster index used as check location
	exception:	offset for node that should not be included in check
	return:		true	for no duplicity detected
				false	if duplicity is found
*/
bool name_duplicity_check(char *name, int position, int exception) {
	struct node *dir_p = &clusters[position];
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (i == exception) continue;	//skip exception node
		if (strcmp(dir_p[i].name, name) == 0) return false;
	}
	return true;
}

/*
creates name in pattern
without duplicity in the same directory 
	file ->		"__fnd%d.fnd"
	folder->	"__fnd%d"
with duplicity
	file ->		"__fnd%d.(%d)"
	folder ->	"__fnd%d(%d)"

%d		is given cluster
(%d)	is number diferentiating between duplicate files
created name is coppied to given addres
	cluster:	cluster index which appears in the name
	*name:		destination adress for created name
*/
create_name(int cluster, char *name, bool isFile) {
	int index = 0;
	if (isFile) {
		snprintf(name, 13, "_fnd%d.fnd", cluster);
		while (name_duplicity_check(name, cluster, -1) != true) {
			snprintf(name, 13, "_fnd%d.(%d)", cluster, index);
			index++;
		}
	}
	else {
		snprintf(name, 13, "_fnd%d", cluster);
		while (name_duplicity_check(name, cluster, -1) != true) {
			snprintf(name, 13, "_fnd%d(%d)", cluster, index);
			index++;
		}
	}
	
}

/*
checks if given index is valid index of cluster by dimension of USABLE_CLUSTERS 
cluster have not to be directory as it should be continuous file
if not tryes to use cluster from secondary fat table if both are unusable returns -1
as there is no valid cluster available
return:	true	for valid index
false	for invalid index
*/
int get_valid_reference(int position) {
	if (position > UINT8_MAX || position < 0) {
		return -1;
	}
	int cl = fat[position];

	if (cl == FAT_DIRECTORY || cl == FAT_UNUSED) {
		//not valid primary
		cl = fat_sec[position];
		if (cl == FAT_DIRECTORY) {
			//not valid secondary
			return -1;
		}
		//ok secondary
		fat[position] = fat_sec[position];
		return cl;
	}
	//ok primary
	return cl;
}

/*
writes content of cluster to console with size restriction defined by bytes
	position:	cluster index
	bytes:		number of bytes from given cluster should be witten to console
*/
write_cluster_to_cli(int position, int bytes) {
	if (bytes > CLUSTER_SIZE) bytes = CLUSTER_SIZE;
	char *ch = &clusters[position];
	for (int i = 0; i < bytes; i++) {
		printf("%c", ch[i]);
	}
}

/*
frees clusters in order of references in fat table given index
	index:	index of first cluster to be freed
*/
clear_rest_clusters_file(int index) {
	int cl = index;
	int fat_cl = get_valid_reference(cl);
	if (fat_cl == -1) {
		fat[cl] = FAT_FILE_END;
		fat_sec[cl] = FAT_FILE_END;
	}
	while (fat_cl != FAT_FILE_END) {
		set_unused(cl);
		cl = fat_cl;
		fat_cl = get_valid_reference(cl);
		if (fat_cl == -1) {
			fat[cl] = FAT_FILE_END;
			fat_sec[cl] = FAT_FILE_END;
		}
	}
	set_unused(cl);
}

/*
sets cluster at given index to FAT_UNUSED for it to be used as free space
sets min_free_cluster appropriately
*/
set_unused(int fat_index) {
	fat[fat_index] = FAT_UNUSED;
	fat_sec[fat_index] = FAT_UNUSED;
	if (fat_index < min_free_cluster) {
		min_free_cluster = fat_index;
	}
}

/*
returns index of first unused cluster
sets the cluster as FAT_FILE_END -> not unused
	return:	index of unused cluster
			-1 if there are not any unused clusters
*/
int get_free_cluster() {
	for (int i = min_free_cluster; i < USABLE_CLUSTERS; i++) {
		if (fat[i] == FAT_UNUSED) {
			min_free_cluster = i + 1;
			fat[i] = FAT_FILE_END;	//mark cluster as used (lock)
			fat_sec[i] = FAT_FILE_END;
			return i;	//index of free cluster
		}
	}
	return -1;	//no free cluster available
}

/*
checks if given node is empty (empty nodes name starts with CLEAR_SIGN)
	*node_p:	pointer to the node
	return:	true	empty node
			false	not empty node
*/
bool is_free_node(struct node *node_p) {
	if (node_p->name[0] == CLEAR_SIGN) {
		return true;
	}
	else {
		return false;
	}
}

/*
gets offset in cluster given by index to first available space for node
	position:	index of directory cluster
	return:	index of available space for new node structure
			-1 if there is no available space -> directory is full
*/
int get_free_dir_offset(int position) {
	struct node *dir_p = &clusters[position];
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (is_free_node(&dir_p[i])) return i;
	}
	return -1;
}

/*
gets offset in cluster given by index to node with given name
	*name:		name of the node
	position:	index of cluster with the node
	return:		offset index to the node
				-1 if given cluster does not contains node with given name
*/
int get_node_offset(char *name, int position) {
	struct node *dir_p = &clusters[position];
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (strcmp(name, dir_p[i].name) == 0) return i;
	}
	return -1;
}

/*
clears cluster at given index for it to be used as empty directory
fills spaces where atribute name from node should be with CLEAR_SIGN characters
	position:	index of the cluster
*/
clear_directory_cluster(int position) {
	struct node *dir_p = &clusters[position];
	int t = 0;
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		clear_node(&dir_p[i]);
		t++;
	}
}

/*
checks if directory given by pointer is empty (does not contain any named nodes)
	*directory:	pointer to examined directory
	return:	true	for empty directory
			false	for not empty directory
*/
bool is_directory_empty(struct cluster *directory) {
	struct node *dir_p = directory;
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (dir_p[i].name[0] != CLEAR_SIGN) {
			//record found
			return false;
		}
	}
	return true;
}

/*
clears node given by pointer (removes name of the node)
	*clear:	pointer to node for clearing
*/
clear_node(struct node *clear) {
	memset(clear->name, CLEAR_SIGN, sizeof(clear->name));
}

/*
properly removes node with it's data
for file all used clusters are freed
for empty directory it's start cluster is freed
record of this node in it's respective directory is removed
	*remove:	pointer to node for removing
	return:		0	if removing was succesful
				-1	for unsuccessful (directory was not empty)
*/
int remove_node(struct node *remove) {
	int cl = remove->start_cluster;	//data position
	int cl_old;
	
	if (remove->isFile) {
		//FILE
		//free data clusters
		clear_rest_clusters_file(cl);
	}
	else {
		//DIRECTORY
		struct cluster *cl_p = &clusters[remove->start_cluster];
		if (is_directory_empty(cl_p)) {
			set_unused(remove->start_cluster);	//free directory cluster
		}
		else {
			//not empty
			return -1;
		}
	}

	//remove record of node
	clear_node(remove);
	return 0;
}

/*
gets start cluster index of node given by name and cluster index
	*name:		name of the node
	position	index of cluster with the node
	return:		start cluster index of the node
*/
int get_node_start_cluster(char *name, int position) {
	struct node *dir_p = &clusters[position];

	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (strcmp(name, dir_p[i].name) == 0) {
			return dir_p[i].start_cluster;
		}
	}
	return -1;
}

/*
loads data from file given by *file_p and fills clusters from start position
	start_position:		index of first cluster assigned for this file
	*file_p:			pointer to source file
	return:		0	for successful load of file
				1	for unsuccessful load (not enough free clusters)
*/
int load_file(int start_postition, FILE *file_p) {
	int wr_cl = start_postition;	//cluster for writing
	int wr_cl_old;					//last used cluster
	char *wr = &clusters[wr_cl];	//pointer for writing single bytes
	char rd;						//one byte from file for writing
	int count = 0;					//counter for bytes writen to actual cluster

	while ((rd = fgetc(file_p)) != EOF) {
		*wr = rd;
		wr++;
		count++;

		if (count >= CLUSTER_SIZE) {
			//cluster is full
			count = 0;
			
			wr_cl_old = wr_cl;	//save old cluster index
			wr_cl = get_free_cluster();

			if (wr_cl == -1) {
				return -1;	//no available free cluster for writing
			}

			//record new cluster for file to fat
			fat[wr_cl_old] = wr_cl;
			fat_sec[wr_cl_old] = wr_cl;
			
			//pointer to next cluster position
			wr = &clusters[wr_cl];
		}
	}

	return 0;
}

/*
creates new file record (node) with given parameters
	*node_p:	position for new record
	name:		name for the new file
	size:		size of the file
	return:	index of first cluster for file
			-1	if there is no available cluster for file
*/
int create_file_rec(struct node *node_p, char name[], int32_t size) {
	int start_pos = get_free_cluster();
	if (start_pos == -1) {
		printf("No available cluster!\n");
		return -1;	//no available cluster
	}

	//filling the record
	strcpy(node_p->name, name);
	node_p->isFile = true;
	node_p->size = size;
	node_p->start_cluster = start_pos;
	return start_pos;
}

/*
creates new file record (node) with given parameters
	*node_p:	position for new record
	name[]:		name for new file
	size:		size of the file
	cluster:	starting position for file
*/
create_file_rec_set(struct node *node_p, char name[], int size, int cluster) {
	//filling the record
	strcpy(node_p->name, name);
	node_p->isFile = true;
	node_p->size = size;
	node_p->start_cluster = cluster;
}

/*
creates new directory record with given parameters and clears new assigned directory cluster
	*node_p:	position for new record
	name:		name for the new directory
	return:	index of assigned directory cluster
			-1	if there is no available cluster for directory
*/
int create_directory_rec(struct node *node_p, char name[]) {
	int start_pos = get_free_cluster();
	if (start_pos == -1) {
		printf("No available cluster!\n");
		return -1;	//no available cluster
	}

	//filling the record
	strcpy(node_p->name, name);
	node_p->isFile = false;
	node_p->size = 0;
	node_p->start_cluster = start_pos;
	fat[start_pos] = FAT_DIRECTORY;	//change to directory (from FILE_END <- get_free_cluster())
	fat_sec[start_pos] = FAT_DIRECTORY;
	clear_directory_cluster(start_pos);	//clearing for use

	return start_pos;
}

/*
creates new directory record with given parameters (does not clear directory cluster)
*node_p:	position for new record
name:		name for the new directory
return:	index of assigned directory cluster
		-1	if there is no available cluster for directory
*/
int create_directory_rec_set(struct node *node_p, char name[], int cluster) {
	strcpy(node_p->name, name);
	node_p->isFile = false;
	node_p->size = 0;
	node_p->start_cluster = cluster;
	fat[cluster] = FAT_DIRECTORY;	//change to directory (from FILE_END <- get_free_cluster())
	fat_sec[cluster] = FAT_DIRECTORY;
	return cluster;
}

/*
searches for adress given by target from root directory
to get start cluster of that directory
	target:	adress to directory
	return:	start cluster index of the directory
			-1 if directory was not found
*/
int get_directory_cluster(char target[]) {

	char adrbuff[13];			//buffer for one folder name
	char *adrbuff_p = adrbuff;	//pointer to buffer

	if (target[0] == '.') target++;	//skip first dot

	char *target_p = target;
	
	//process given adress
	int32_t result = 0;	//cluster with correct directory 0 for start in root
	
	while (target_p[0] != '\0') {
		if (target_p[0] == '/') {
			if (*adrbuff == *adrbuff_p) {
				//empty buffer
				target_p++;
				continue;
			}

			adrbuff_p[0] = '\0';	//close string

			//find directory
			result = get_node_start_cluster(adrbuff, result);
			if (result == -1) {
				//path not found
				return -1;
			}

			adrbuff_p = adrbuff;	//clear buffer
		}
		else {
			adrbuff_p[0] = target_p[0];
			adrbuff_p++;
		}
		target_p++;
	}

	//not empty buffer
	if (*adrbuff != *adrbuff_p) {
		//in case that target did not have '/' on the end terminate string
		adrbuff_p[0] = '\0';
		result = get_node_start_cluster(adrbuff, result);
		if (result == -1) {
			//path not found
			return -1;
		}
	}

	return result;
}

/*
separates name of folder or file from given adress
	*adr:	adress for separation
	return:	file/folder name from the adress
*/
char *get_address_name(char *adr) {
	char *name;
	name = strrchr(adr, '/');
	if (name == NULL) {
		//no slash in address -> adress is root directory
		name = adr;
	}
	else {
		name++;
	}

	return name;
}

/*
separates name of folder or file from given adress
requires editable char array *adr
	*adr:	adress for separation
	return:	adress without final file/folder name
*/
char *get_address_address(char *adr) {
	char *target;
	char *name;

	name = strrchr(adr, '/');
	if (name == NULL) {
		//no slash in address -> adress is root directory
		return "";
	}
	else {
		name[0] = '\0';
		target = adr;
	}

	return target;
}

/*
gets pointer to node (record) with given address
	address:	address of node (file/directory)
	return:		pointer to the node
*/
struct node *get_record(char *address) {
	char *name;
	char *adr;		//pointer to editable version of adress
	char *target;	//address without name

	//get target adress and filename separated
	int size = strlen(address);
	adr = malloc(size + 1);
	strcpy(adr, address);

	name = get_address_name(adr);
	target = get_address_address(adr);

	int cl = get_directory_cluster(target);	//cluster index of coresponding directory
	if (cl == -1) {
		printf("PATH NOT FOUND");
		free(adr);
		exit(0);
	}

	//file record
	int offset = get_node_offset(name, cl);
	if (offset == -1) {
		printf("PATH NOT FOUND");
		exit(0);
	}
	struct node *record = &clusters[cl];
	record = &record[offset];

	free(adr);
	return record;
}

/*
imports file from source to data structures with relative adress set by target
	source_file:	adress with source file
	target:			relative adress in fat structure
*/
import_file(char source_file[], char target[]) {
	//try to open file
	FILE *file_p = fopen(source_file, "rb");
	if (file_p == NULL){
		printf("File not found : %s\n", source_file);
		exit(0);
	}

	//file size
	fseek(file_p, 0L, SEEK_END);
	int32_t size = ftell(file_p);	//size of the file
	fseek(file_p, 0L, SEEK_SET);	//rewind

	//position of target directory
	int dir_position = get_directory_cluster(target);
	if (dir_position == -1) {
		//Directory not found!
		printf("PATH NOT FOUND : %s\n", target);
		fclose(file_p);
		exit(0);
	}

	//point to space for new record
	struct node *dir_p = &clusters[dir_position];	//pointer to directory cluster
	int offset = get_free_dir_offset(dir_position);
	if (offset == -1) {
		printf("Directory is full!\n");
		fclose(file_p);
		exit(0);	//no available directory
	}
	struct node *node_p = &dir_p[offset];	//pointer to actual empty node in directory

	//filename
	char *filename = get_address_name(source_file);
	if (!name_check(filename)) {
		printf("incorrect file name (maximum is 12 characters)\n");
		exit(0);
	}

	//duplicity check
	if (!name_duplicity_check(filename, dir_position, -1)) {
		printf("directory already contains record with name %s\n", filename);
		exit(0);
	}

	//create file record
	int start_pos = create_file_rec(node_p, filename, size);
	if (start_pos == -1) {
		printf("Creating file record failed!\n");
		fclose(file_p);
		exit(0);
	}

	//load file
	if (load_file(start_pos, file_p) == -1) {
		//no available cluster for writing
		printf("Writing file failed!\n");
		fclose(file_p);
		remove_node(node_p);	//cleaning
		exit(0);
	}
	printf("OK\n");
}

/*
creates new directory with specified name at given adress
	name:	directory name
	target:	adress of the new directory
*/
create_directory(char name[], char target[]) {
	//position of target directory
	int dir_position = get_directory_cluster(target);
	if (dir_position == -1) {
		//Directory not found!
		printf("PATH NOT FOUND : %s\n", target);
		exit(0);
	}

	//point to space for new record
	struct node *dir_p = &clusters[dir_position];	//pointer to directory cluster
	int offset = get_free_dir_offset(dir_position);	//offset in cluster for available node space
	if (offset == -1) {
		printf("Directory is full!\n");
		exit(0);	//no available directory
	}
	struct node *node_p = &dir_p[offset];	//pointer to actual empty node in directory

	//duplicity check
	if (!name_duplicity_check(name, dir_position, -1)) {
		printf("directory already contains record with name %s\n", name);
		exit(0);
	}

	//create directory record
	int start_pos = create_directory_rec(node_p, name);
	if (start_pos == -1) {
		printf("Creating directory record failed!\n");
		exit(0);
	}
	printf("OK\n");
}

/*
removes file with given address
	adress:	adress to file for removing
*/
remove_file(char address[]) {
	struct node *file_rec = get_record(address);
	//remove file
	remove_node(file_rec);
	printf("OK\n");
}

/*
removes directory with given address
*/
remove_directory(char address[]) {
	struct node *dir_rec = get_record(address);

	//remove directory
	if (remove_node(dir_rec) == -1) {
		printf("PATH NOT EMPTY");
		exit(0);
	}
	printf("OK\n");
}

/*
writes to console name of file given by address and clusters assigned to this file
	address:	address to file
*/
list_file_clusters(char address[]) {
	struct node *file = get_record(address);

	//list clusters
	printf("%s ", address);
	int cl = file->start_cluster;
	int fat_cl = get_valid_reference(cl);
	while (fat_cl != FAT_FILE_END) {
		printf("%d, ", cl);
		cl = fat_cl;
		fat_cl = get_valid_reference(cl);
	}
	printf("%d", cl);
}

/*
writes to console content of file given by address
	address:	address to file
*/
list_file(char address[]) {
	struct node *file = get_record(address);

	//list file to console
	int cl = file->start_cluster;
	int size = file->size;

	printf("%s:", address);
	int fat_cl = get_valid_reference(cl);
	while (fat_cl != FAT_FILE_END) {
		size -= CLUSTER_SIZE;
		write_cluster_to_cli(cl, CLUSTER_SIZE);
		cl = fat_cl;
		fat_cl = get_valid_reference(cl);
	}
	//last cluster -> possibly with size restriction
	write_cluster_to_cli(cl, size);
	printf("\n");
}

/*
writes name of given node with start cluster index and number of assigned clusters to console with nesting depth
represented by number of '\t'
	*node_p:	pointer to file node
	depth:		nesting depth of the node
*/
list_file_node(struct node *node_p, int depth) {
	int count = 1;
	int cl = node_p->start_cluster;
	int fat_cl = get_valid_reference(cl);
	if (fat_cl == -1) {	//file with invalid adress will not be listed
		return;
	}

	while (fat_cl != FAT_FILE_END) {
		count++;
		cl = fat_cl;
		fat_cl = get_valid_reference(cl);
		if (fat_cl == -1) {
			fat[cl] == FAT_FILE_END;
		}
	}
	for (int i = 0; i < depth; i++) printf("\t");	//nesting depth
	printf("-%s %d %d\n", node_p->name, node_p->start_cluster, count);
}

/*
recursive function writes name of given node to console with nesting depth
represented by number of '\t'
*node_p:	pointer to directory node
depth:		nesting depth of the node
*/
list_dir_node(struct node *node_p, int depth) {

	for (int i = 0; i < depth; i++) printf("\t");	//nesting depth
	printf("+%s\n", node_p->name);

	node_p = &clusters[node_p->start_cluster];

	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (is_free_node(node_p)) {
			//skip empty node record
			node_p++;
			continue;
		}

		if (node_p->isFile) {
			//file node
			list_file_node(node_p, depth + 1);
		}
		else {
			//directory node
			list_dir_node(node_p, depth + 1);
		}

		node_p++;
	}
	for (int i = 0; i < depth; i++) printf("\t");
	printf("--\n");
}

/*
writes filesystem structure to console with indentation coresponding to nesting depth
*/
list_structure() {
	struct cluster *root_dir_p = &clusters[0];
	if (is_directory_empty(root_dir_p)) {
		//empty root directory
		printf("EMPTY");
		return;
	}
	
	printf("+ROOT\n");

	int depth = 0;	//nesting depth
	struct node *node_p = root_dir_p;
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (is_free_node(node_p)) {
			//skip empty node record
			node_p++;
			continue;
		}
		if (node_p->isFile) {
			//file node
			list_file_node(node_p, depth + 1);
		}
		else {
			//directory node
			list_dir_node(node_p, depth + 1);
		}

		node_p++;
	}

	printf("--\n");
}

/*
gets number of clusters which follow up start_cluster using fat table
	start_cluster:	first cluster
	return:	total number of conected clusters from the first one
*/
int count_clusters(int start_cluster) {
	int cnt = 1;
	int cl = start_cluster;
	int fat_cl = get_valid_reference(cl);
	while (fat_cl != FAT_FILE_END) {
		cl = fat_cl;
		fat_cl = get_valid_reference(cl);
		cnt++;
	}
	return cnt;
}

/*
creates new directory record which coresponds to found directory cluster and fires new hit test with result in *fat_cnt
	*fat_cnt:	hit test record for correcting it's values
	index:		index of the found directory cluster
*/
recover_directory(int *fat_cnt, int index) {
	struct node *dir_p = &clusters[0];	//pointer to directory cluster
	int offset = get_free_dir_offset(0);	//offset in cluster for available node space
	if (offset == -1) {
		printf(" root directory is full\n");
		printf("  found directory could not be moved to root\n");
		return 1;
	}
	struct node *node_p = &dir_p[offset];	//pointer to actual empty node in directory

	//create new directory record for found one
	char name[13];
	create_name(index, name, false);
	int start_pos = create_directory_rec_set(node_p, name, index);	//create directory record
	if (start_pos == -1) {
		printf("  Creating directory record failed! not enough space\n");
		return 1;
	}
	fat_cnt[start_pos]++;
	printf("  directory moved to root as %s\n", name);

	//create new hit test
	hit_test_arr(fat_cnt);
	return 0;
}

/*
corrects name of given node to standard size (12 characters + '\0')
	*node_p:	node for name correction
*/
name_correction(struct node *node_p) {
	if (strlen(node_p->name)>12) {
		printf(" file name (%.20s) was too long and was shortened to standard (12 characters)\n", node_p->name);
		node_p->name[12] = '\0';
		node_p->name[8] = '.';
	}

	//duplicity check
	int cls_adr = &clusters[0];
	int node_adr = node_p;
	int cl_index = (cls_adr - node_adr) / CLUSTER_SIZE;
	int node_offset = (node_adr - cls_adr) / (sizeof(*node_p));	//ndoe offset
	if (name_duplicity_check(node_p->name, cl_index, node_offset) != true) {
		//duplicity found
		int index = 0;

		if (strlen(node_p->name) <= 7) {
			//short name
			do {
				memset(node_p->name, '\0', sizeof(node_p->name));
				snprintf(node_p->name, 13, "(%d).fnd", index);
				index++;
			} while (name_duplicity_check(node_p->name, cl_index, node_offset) != true);
			printf(" new file name: %s\n", node_p->name);
		}
		else {
			//normal name (*X.XXX)
			int len = strlen(node_p->name);
			char *str = &node_p->name[len - 4];
			*str = '.';
			str[-3] = '(';
			str[-1] = ')';
			do {
				str[-2] = ('0' + index);
				index++;
			} while (name_duplicity_check(node_p->name, cl_index, node_offset) != true);
			printf(" new file name: %s\n", node_p->name);
		}
	}
}

/*
logs given directory to hit test record *fat_cnt and makes apropriates repairs
multiple references are reduced to one
name is corrected to standard
in case of directory reference to unused cluster cluster is changed to directory
function recursively proceeds to it's directories
	*dir:		directory for hit test
	*fat_cnt:	hit test results
*/
mark_directory(struct node *dir, int *fat_cnt) {
	fat_cnt[dir->start_cluster]++;
	if (fat_cnt[dir->start_cluster] > 1) {
		//multiple hits to same directory
		clear_node(dir);	//remove unecessary
		printf(" multiple ways to directory at cluster %d (%d hits)\n", dir->start_cluster, fat_cnt[dir->start_cluster]);
		printf("  unnecessary reference was removed\n");
		//stop recursive branching
		return;
	}

	//correct name size
	name_correction(dir);

	//reference to unused cluster
	if (fat[dir->start_cluster] == FAT_UNUSED) {
		printf(" cluster %d was recognized as directory %s and was marked as directory\n",dir->start_cluster, dir->name);
		fat[dir->start_cluster] = FAT_DIRECTORY;
		fat_sec[dir->start_cluster] = FAT_DIRECTORY;
	}

	//recursive go trough it's directories
	struct node *node_p = &clusters[dir->start_cluster];
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (is_free_node(node_p)) {
			//skip empty node record
			node_p++;
			continue;
		}
		if (node_p->isFile) {
			//file node
			mark_file(node_p, fat_cnt);
		}
		else {
			//directory node
			mark_directory(node_p, fat_cnt);
		}

		node_p++;
	}
}


/*
logs given file to hit test record *fat_cnt and makes apropriates repairs
name is corrected to standard
size of the file is checked 
	in case of bigger file than is expected by size file is shortened and rest of references is cleared
in case of file reference to unused cluster cluster is changed to end of file and file is shortened by that action
same action occurs when invalid cluster index is referenced
	*file:		file for hit test
	*fat_cnt:	hit test results
*/
mark_file(struct node *file, int *fat_cnt) {
	int cl = file->start_cluster;
	int fat_cl = get_valid_reference(cl);
	
	if (fat_cl == -1) {
		printf(" corrupted file %.12s invalid first cluster address %d\n", file->name, cl);
		set_unused(cl);
		clear_node(file);
		printf("  file record removed\n");
		return;
	}
	fat_cnt[cl]++;	//start file
	
	//correct name size
	name_correction(file);

	//size check
	int exp_cnt = (file->size / CLUSTER_SIZE) + 1;
	int file_cnt = 1;

	int cl_old;
	while (fat[cl] != FAT_FILE_END) {
		cl_old = cl;
		cl = get_valid_reference(cl);
		if (cl == -1) {
			printf(" corrupted file %s invalid cluster address\n", file->name);
			printf("  expected number of used clusters was %d (%dB)\n", file->name, exp_cnt, file->size);
			fat[cl_old] = FAT_FILE_END;	//end file early
			fat_sec[cl_old] = FAT_FILE_END;
			return;
		}
		if (file_cnt > exp_cnt) {
			//file is bigger than expected
			printf(" corrupted file %s expected number of used clusters was %d (%dB)\n", file->name, exp_cnt, file->size);
			fat[cl_old] = FAT_FILE_END;	//end file early
			fat_sec[cl_old] = FAT_FILE_END;
			clear_rest_clusters_file(cl);
			return;
		}
		fat_cnt[cl]++;

		if (fat[cl] == FAT_UNUSED) {
			//unused space that should belong to file
			printf(" cluster %d was recognized as part of file %s and was marked as end of the file\n", cl, file->name);
			fat[cl] == FAT_FILE_END;
			fat_sec[cl] == FAT_FILE_END;
		}
		file_cnt++;
	}
}

/*
starts hit test procedure which records how many times was specific cluster referenced and takes apropriate actions aftewards
fat_cnt is pointer to integer array of size given by USABLE_CLUSTERS
	*fat_cnt:	hit test results
*/
hit_test_arr(int *fat_cnt) {
	fat_cnt[0] = 1;
	for (int i = 1; i < USABLE_CLUSTERS; i++) {
		fat_cnt[i] = 0;
	}

	//fill hit counter array
	struct cluster *root_dir_p = &clusters[0];
	struct node *node_p = root_dir_p;
	for (int i = 0; i < DIRECTORIES_PER_CLUSTER; i++) {
		if (is_free_node(node_p)) {
			//skip empty node record
			node_p++;
			continue;
		}
		if (node_p->isFile) {
			//file node
			mark_file(node_p, fat_cnt);
		}
		else {
			//directory node
			mark_directory(node_p, fat_cnt);
		}

		node_p++;
	}
}

/*
adds to list given by *list new record of found data cluster
only the furthest cluster references from end of files are stored and hit test record is changed appropriately
if given cluster val cannot be referenced to existing references new reference is created in the list
if the val is not valid reference in primary or secondary fat table cluster with val index is set as FAT_FILE_END
	val:		new data cluster
	*list:		list of stored references
	*list_cnt:	number of references stored in the list
	*fat_cnt:	hit test record
*/
add_to_list(int val, int *list, int *list_cnt, int *fat_cnt) {
	int cl = val;
	fat_cnt[cl]++;	//hit

	int fat_cl = get_valid_reference(cl);	//fat[cl]
	if (fat_cl == -1) {						//not valid reference -> end file
		fat[cl] = FAT_FILE_END;
		fat_sec[cl] = FAT_FILE_END;
	}
	
	//add conected clusters to lists
	while (fat_cl != FAT_FILE_END) {
		for (int i = 0; i < *list_cnt; i++) {
			if (fat_cl == list[i]) {
				list[i] = val;
				return;	//value replaced
			}
		}
		cl = fat_cl;
		fat_cl = get_valid_reference(cl);
		if (fat_cl == -1) {						//not valid reference -> end file
			fat[cl] = FAT_FILE_END;
			fat_sec[cl] = FAT_FILE_END;
		}
		fat_cnt[cl]++;	//hit on all consecutive clusters for this file
	}
	//value not found -> new reference to list
	realloc(list, (*list_cnt + 1) * sizeof(int));
	list[*list_cnt] = val;
	*list_cnt = *list_cnt + 1;
}

/*
by hit test result finds non referenced data clusters and connects them apropriately using fat table
from theese connected clusters creates new files in the root directory if possible and alters hit test record appropriately
*/
file_recovery(int *fat_cnt) {
	printf("data recovery...\n");
	//list of start clusters for found data
	int list_cnt = 0;
	int *list = malloc(sizeof(int));

	//find not referenced data clusters
	for (int i = 0; i < USABLE_CLUSTERS; i++) {
		if (fat[i] != FAT_DIRECTORY && fat[i] != FAT_UNUSED && fat_cnt[i] == 0) {
			//possible data without reference
			add_to_list(i, list, &list_cnt, fat_cnt);
		}
	}

	//create new files
	int offset;
	int cls;
	int size;
	for (int i = 0; i < list_cnt; i++) {
		offset = get_free_dir_offset(0);
		if (offset == -1) {
			printf(" not enough space in root directory for recovered files (%d files not created)\n", i);
			return;
		}
		//position
		struct node *file = &clusters[0];	//root
		file = &file[offset];				//file

		//name
		char name[13];
		create_name(list[i], name, true);

		//size
		cls = count_clusters(list[i]);
		size = cls * CLUSTER_SIZE;

		//create record
		create_file_rec_set(file, name, size, list[i]);
		printf(" recovered file %s created in root\n", name);
	}

	printf("recovery complete\n");
}

/*
tests filesystem structuce using hit tests and tryes to repair possible damage and recover lost data and directories
*/
check_structure() {
	//empty directory
	struct cluster *root_dir_p = &clusters[0];
	if (is_directory_empty(root_dir_p)) {
		//empty root directory
		printf("EMPTY");
		return;
	}

	bool test_ok = true;
	bool recovery = false;

	printf("testing... \n");

	//hit test
	int fat_cnt[USABLE_CLUSTERS];
	hit_test_arr(fat_cnt);

	//go through hit test record table
	for (int i = 0; i < USABLE_CLUSTERS; i++) {
		if (fat[i] == FAT_UNUSED) {
			//free space
			if (fat_cnt[i] != 0) {
				//hit to unused cluster
				test_ok = false;
				//cluster is already set appropriately in hit test in case of directory or file reference
				printf(" unused cluster %d (%d hits)\n", i, fat_cnt[i]);
			}
		}else if (fat[i] == FAT_DIRECTORY) {
			//directory
			if (fat_cnt[i] > 1) {
				//multiple hits to one directory
				test_ok = false;
				printf(" multiple ways to directory at cluster %d (%d hits)\n", i, fat_cnt[i]);
			}
			if (fat_cnt[i] == 0) {
				//directory without access
				test_ok = false;
				printf(" unreachable directory at cluster %d\n", i);

				recover_directory(fat_cnt, i);
				//if (recover_directory(fat_cnt, i) == 1) continue;
				//i = 0;
			}
		}else {
			//data
			if (fat_cnt[i] > 1) {
				//multiple references to cluster
				printf(" multiple hits on cluster %d (%d hits)\n", i, fat_cnt[i]);
				test_ok = false;
			}
			if (fat_cnt[i] == 0) {
				//no references to data
				recovery = true;
				printf(" no reference to data at cluster %d\n", i);
			}
		}
	}

	if (test_ok) {
		printf("test complete: OK\n");
	}
	else {
		printf("test complete: ERROR\n");
	}

	//recover files if necessary
	if (recovery) {
		file_recovery(fat_cnt);
	}

	min_free_cluster = 1;	//reset start of free spaces because of possible changes by repairs
}

void display_message(HANDLE hScreen,	char *text){
	TCHAR msgBuf[BUF_SIZE];
	size_t cchStringSize;
	DWORD dwChars;

	// Print message using thread-safe functions.
	StringCchPrintf(msgBuf, BUF_SIZE, 
		TEXT("threads: %s \n"), text);
	StringCchLength(msgBuf, BUF_SIZE, &cchStringSize);
	WriteConsole(hScreen, msgBuf, cchStringSize, &dwChars, NULL);
}

DWORD WINAPI thread_load(LPVOID lpParam) {
	int *data;
	HANDLE  hStdout = NULL;
	data = ((int*)lpParam);

	//Handle to console
	if ((hStdout = GetStdHandle(STD_OUTPUT_HANDLE)) == INVALID_HANDLE_VALUE)
		return 1;
	
	display_message(hStdout, "Working");

}

/*
fills filesystem structures from file
boot record
primary fat table
secondary fat table
clusters with data
	*address:	source file
	*br:		boot record
*/
load_fat(char *address, struct boot_record *br) {
	FILE *file = fopen(address, "rb");
	if (file == NULL) {
		printf("Error opening file!\n");
		exit(0);
	}
	
	fread(br, sizeof(*br), sizeof(char), file);							//boot record
	fread(fat, sizeof(uint8_t), USABLE_CLUSTERS, file);					//primary fat
	fread(fat_sec, sizeof(uint8_t), USABLE_CLUSTERS, file);				//secondary fat
	///
	HANDLE hThread;
	HANDLE thread_handles[NUM_THREADS];

	thread_handles[0] = CreateThread(NULL, 0,	thread_load, &data_t, 0, NULL);

	WaitForMultipleObjects(NUM_THREADS, thread_handles, TRUE, INFINITE);

	for (int i = 0; i < NUM_THREADS; i++) {
		CloseHandle(thread_handles[i]);
	}
	///
	fread(clusters, sizeof(struct cluster), USABLE_CLUSTERS, file);		//data

	fclose(file);
}

/*
saves filesystem structures to file
	*name:	name of the file
	*br:	boot record
*/
save_fat(char *name, struct boot_record *br) {
	FILE *file = fopen(name, "wb");
	if (file == NULL){
		printf("Error opening file!\n");
		exit(0);
	}

	fwrite(br, sizeof(*br), 1, file);
	fwrite(fat, sizeof(uint8_t), USABLE_CLUSTERS, file);
	fwrite(fat, sizeof(uint8_t), USABLE_CLUSTERS, file);			//copy primary fat
	fwrite(clusters, sizeof(clusters[0]), USABLE_CLUSTERS, file);

	fclose(file);
}

/*
saves filesystem structures to file without rewriting secondary fat table
for testing purposes
*/
dump_structure(char *name, struct boot_record *br) {
	FILE *file = fopen(name, "wb");
	if (file == NULL) {
		printf("Error opening file!\n");
		exit(0);
	}

	fwrite(br, sizeof(*br), 1, file);
	fwrite(fat, sizeof(int32_t), USABLE_CLUSTERS, file);
	fwrite(fat_sec, sizeof(int32_t), USABLE_CLUSTERS, file);
	fwrite(clusters, sizeof(clusters[0]), USABLE_CLUSTERS, file);

	fclose(file);
}

test(char *name, struct boot_record *br_p) {
	return;
	import_file("./tfolder/testn.txt", "");
	//create_directory("dokumenty", "./");
	//import_file("tfolder/test.txt", "dokumenty");
	//remove_file("testn.txt");
	//create_directory("pohadky", "./dokumenty");
	//import_file("./tfolder/testh.txt", "");
	//import_file("./tfolder/testh.txt", "dokumenty/pohadky");
	//import_file("./tfolder/testn.txt", "dokumenty/pohadky");
	list_structure();
	save_fat("default_FAT.dat", br_p);
	
	//struct node *p = &clusters[0];
	//p[1].start_cluster = 100;
	//clear_directory_cluster(200);
	/*
	char *p_c = &clusters[0];
	strcpy(p_c, "ABCDE67890123456789");

	struct node *testh_pohadky_p = &clusters[1];
	memset(&testh_pohadky_p[1], '\0', 13);
	list_structure();
	*/
	//remove_file("/testn.txt");
	//remove_directory("./tatau/pohadky");

	//list_file_clusters("./testn.txt");
	//list_structure();
	check_structure();
	list_structure();
	check_structure();
	list_structure();
	list_file("/ABCDE678.012");
	
}

/*
prepares data structures and calls functions based on given agruments when program was started
*/
main(int argc, char *argv[]) {
	
	if (argc < 3) {
		printf("at least 2 arguments expected\n");
		exit(0);
	}

	//initialize fat
	struct boot_record br;
	struct boot_record *br_p = &br;

	//fat
	uint8_t fat_f[USABLE_CLUSTERS];
	uint8_t fat_f_sec[USABLE_CLUSTERS];
	fat = fat_f;
	fat_sec = fat_f_sec;

	//clusters
	struct cluster clusters_f[USABLE_CLUSTERS];
	clusters = clusters_f;

	char *swt = argv[2];
	if (swt[0] == '-') swt++;	//skip dash

	if (swt[0] == 'e' && argc == 3) {
		//boot_rec
		memset(br.signature, '\0', sizeof(br.signature));
		memset(br.volume_descriptor, '\0', sizeof(br.volume_descriptor));
		strcpy(br.volume_descriptor, "Testovaci - FAT8 - tedy 256 clusteru, cluster 256B - 4 rezervovane - pouzitelne 0-251 (zde neni nutne rezervovat)");
		br.fat_type = 8;
		br.fat_copies = 2;
		br.cluster_size = CLUSTER_SIZE;
		br.usable_cluster_count = USABLE_CLUSTERS;
		strcpy(br.signature, "hamet");

		//root directory
		fat[0] = FAT_DIRECTORY;
		fat_sec[0] = FAT_DIRECTORY;
		clear_directory_cluster(0);	//clear root directory

		//unused space
		for (int i = 1; i < USABLE_CLUSTERS; i++) {
			fat[i] = FAT_UNUSED;
			fat_sec[i] = FAT_UNUSED;
		}
		save_fat(argv[1], br_p);
		return;
	}
	else {
		load_fat(argv[1], br_p);
	}

	if (swt[0] == 'a' && argc == 5) {
		//import file
		import_file(argv[3], argv[4]);
	}
	else if (swt[0] == 'f' && argc == 4) {
		//delete file
		remove_file(argv[3]);
	}
	else if (swt[0] == 'c' && argc == 4) {
		//list clusters for file
		list_file_clusters(argv[3]);
	}
	else if (swt[0] == 'm' && argc == 5) {
		//new directory
		if (!name_check(argv[3])) {
			printf("incorrect directory name (maximum is 12 characters)\n");
			exit(0);
		}
		create_directory(argv[3], argv[4]);	//name check
	}
	else if (swt[0] == 'r' && argc == 4) {
		//delete directory
		remove_directory(argv[3]);
	}
	else if (swt[0] == 'l' && argc == 4) {
		//list file
		list_file(argv[3]);
	}
	else if (swt[0] == 'p' && argc == 3) {
		//list structure
		list_structure();
	}
	else if (swt[0] == 's' && argc == 3) {
		//check structure
		check_structure();
	}
	else if (swt[0] == 't' && argc == 3) {
		//tests
		import_file("./tfolder/testn.txt", "");
		create_directory("dokumenty", "./");
		import_file("tfolder/test.txt", "dokumenty");
		remove_file("testn.txt");
		create_directory("pohadky", "./dokumenty");
		import_file("./tfolder/testh.txt", "");
		import_file("./tfolder/testh.txt", "dokumenty/pohadky");
		import_file("./tfolder/testn.txt", "dokumenty/pohadky");
		list_structure();
		save_fat("default_FAT.dat", br_p);
		
		struct node *p = &clusters[0];
		p[1].start_cluster = 100;
		//clear_directory_cluster(100);
		
		char *p_c = &clusters[0];
		strcpy(p_c, "ABCDE67890123456789");
		
		struct node *testh_pohadky_p = &clusters[1];
		memset(&testh_pohadky_p[1], '\0', 13);
	//	list_structure();

		//fat[7] = -1;

	//	dump_structure("test_FAT.dat", br_p);

		//remove_file("/testn.txt");
		//remove_directory("./tatau/pohadky");

		//list_file_clusters("./testn.txt");
		//list_structure();
		check_structure();
		list_structure();
		check_structure();
		list_structure();
		list_file("/ABCDE678.012");
		//test(argv[1], br_p);
		return;
	}
	else {
		printf("wrong number of arguments\n");
		exit(0);
	}

	save_fat(argv[1], br_p);
}