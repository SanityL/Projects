/*
 * PROJ1-1: YOUR TASK B CODE HERE
 *
 * Feel free to define additional helper functions.
 */

#include <stdlib.h>
#include <stdio.h>
#include "quadtree.h"
#include "make_qtree.h"
#include "utils.h"

#define ABS(x) (((x) < 0) ? (-(x)) : (x))

int homogenous(unsigned char *depth_map, int map_width, int x, int y, int section_width) {
    
    /* YOUR CODE HERE */
    int color = depth_map[x + (map_width * y)];
    for (int i = 0; i < section_width; i++) {
        for (int j = 0; j < section_width; j++) {
            if (color != depth_map[x + (map_width * y)]) {
                return 256;
            }
            x++;
        }
        x-=section_width;
        y++;
    }
    return color;
}

// Helper function that recursively calls homogenous while it equals 256,
// else it would return the node that is homogenous and has no more child nodes
qNode *more_children(unsigned char *depth_map, int map_width, int x, int y, int section_width) {
    
    qNode *child_node = (qNode *)malloc(sizeof(qNode));
    child_node->leaf = 0;
    child_node->size = section_width;
    child_node->x = x;
    child_node->y = y;
    int color = homogenous(depth_map, map_width, x, y, section_width);
    child_node->gray_value = color;
    if (child_node) {
        if (color == 256) {
            section_width = section_width / 2;
            child_node->child_NW = more_children(depth_map, map_width, x, y, section_width);
            child_node->child_NE = more_children(depth_map, map_width, x + section_width, y, section_width);
            child_node->child_SE = more_children(depth_map, map_width, x + section_width, y + section_width, section_width);
            child_node->child_SW = more_children(depth_map, map_width, x, y + section_width, section_width);
        } else {
            child_node->leaf = 1;
            return child_node;
        }
    } else {
        allocation_failed();
    }
    return child_node;
}   

qNode *depth_to_quad(unsigned char *depth_map, int map_width) {

    /* YOUR CODE HERE */
    qNode *root = (qNode *)malloc(sizeof(qNode));
    root->leaf = 0;
    root->size = map_width;
    root->x = 0;
    root->y = 0;
    int color = homogenous(depth_map, map_width, root->x, root->y, map_width);
    root->gray_value = color;
    if (root) {
        if (color == 256) {
        root = more_children(depth_map, map_width, root->x, root->y, map_width);   
        } else {
            root->leaf = 1;
            return root;
        }
    } else {
        allocation_failed();
    }
    return root;
}

void free_qtree(qNode *qtree_node) {
    if(qtree_node) {
        if(!qtree_node->leaf){
            free_qtree(qtree_node->child_NW);
            free_qtree(qtree_node->child_NE);
            free_qtree(qtree_node->child_SE);
            free_qtree(qtree_node->child_SW);
        }
        free(qtree_node);
    }
}
