/*
 * PROJ1-1: YOUR TASK A CODE HERE
 *
 * Feel free to define additional helper functions.
 */

#include "calc_depth.h"
#include "utils.h"
#include <math.h>
#include <limits.h>
#include <stdio.h>

/* Implements the normalized displacement function */
unsigned char normalized_displacement(int dx, int dy,
        int maximum_displacement) {

    double squared_displacement = dx * dx + dy * dy;
    double normalized_displacement = round(255 * sqrt(squared_displacement) / sqrt(2 * maximum_displacement * maximum_displacement));
    return (unsigned char) normalized_displacement;
}

int if_out_of_bound(int position, int image_width, int image_height, int feature_width, int feature_height) {
	
	if ((position/image_width)<feature_height) //checking the upper edge
	{
		return 1;
	}
	if ((position%image_width)<feature_width) //check the left edge
	{
		return 1;
	}
	if ((image_width-(position%image_width)-1)<feature_width) //checking the right edge
	{
		return 1;
	}
	if ((image_height-(position/image_width)-1)<feature_height) //checking the bottom edge
	{
		return 1;
	}
	return 0;
}

int feature_displacement(unsigned char *depth_map, unsigned char *left,
        unsigned char *right, int image_width, int image_height,
        int feature_width, int feature_height, int right_image_p, int left_image_p) {
	//right_image_p changes, whereas left_image_p is unchanged all the time
	
	int box_w = 2 * feature_width + 1;
	int box_h = 2 * feature_height + 1;
	int pos_left = left_image_p - feature_width - feature_height * image_width; //position of the very upper left corner of box
	int pos_right = right_image_p - feature_width - feature_height * image_width; //position of the very upper left corner of box
	int distance = 0;
	for (int i = 0; i < box_h; i++)
	{
		for (int j = 0; j < box_w; j++) //for each row
		{
			distance = distance + (right[pos_right] - left[pos_left]) * (right[pos_right] - left[pos_left]);
			pos_left++;
			pos_right++;
		}
		pos_left = pos_left - box_w + image_width;
		pos_right = pos_right - box_w + image_width;
	}
	return distance;
}

unsigned char calc_normalized_displacement(unsigned char *depth_map, int min_p, int image_w, int image_h, int p, int maximum_displacement) {
	
	int x = min_p % image_w; //x-coordinate of min_pos
	int y = min_p / image_w; //y-coordinate of min_pos
	int dx = x - (p % image_w); //dx of left and right
	int dy = y - (p / image_w); //dy of left and right
	return normalized_displacement(dx, dy, maximum_displacement);
}

void search_space(unsigned char *depth_map, unsigned char *left,
        unsigned char *right, int image_width, int image_height,
        int feature_width, int feature_height, int maximum_displacement, int position) { //position = p

	int min = 255 * 255 * (2 * feature_width + 1) * ( 2 * feature_height + 1); // the first feature displacement
	int min_pos; //position of the minimum distance pixel; initialized to the same position
	unsigned char min_nor = 255;
	int center_x = position % image_width; //x-coordinate of position 
	int center_y = position / image_width; //y-coordinate of position
	int upper_left_x = center_x - maximum_displacement;
	int upper_left_y = center_y - maximum_displacement;
	int search_dim = 2 * maximum_displacement + 1; //side length of the search square
	for (int i = 0; i < search_dim; i++)
	{
		for (int j = 0; j < search_dim; j++)
		{
			int upper_left = upper_left_x + upper_left_y * image_width;
			if ((upper_left_x >= 0) && (upper_left_y >= 0) && (upper_left_x < image_width) && (upper_left_y < image_height)
				&& !if_out_of_bound(upper_left, image_width, image_height, feature_width, feature_height))
			{
				//int upper_left = upper_left_x + upper_left_y * image_width;
				int curr = feature_displacement(depth_map, left, right, image_width, image_height, feature_width, feature_height, upper_left, position);
				if (curr < min) //update min = curr if curr is smaller
				{
					min = curr;
					min_pos = upper_left;
					min_nor = calc_normalized_displacement(depth_map, min_pos, image_width, image_height, position, maximum_displacement);
					*depth_map = min_nor;
				} else if (curr == min) //choose the one whose normalized displacement is smaller
				{
					unsigned char curr_nor = calc_normalized_displacement(depth_map, upper_left, image_width, image_height, position, maximum_displacement);
					if (curr_nor < min_nor)
					{
						min = curr;
						min_pos = upper_left;
						min_nor = curr_nor;
						*depth_map = curr_nor;
					}
				}
			}
			upper_left_x++;
		}
		upper_left_x-=search_dim;
		upper_left_y++;
	}
}

void calc_depth(unsigned char *depth_map, unsigned char *left,
        unsigned char *right, int image_width, int image_height,
        int feature_width, int feature_height, int maximum_displacement) {

    /* YOUR CODE HERE */
	int len_of_array = image_width * image_height;
	for (int p = 0; p < len_of_array; p++)
	{
		if (maximum_displacement == 0 || if_out_of_bound(p, image_width, image_height, feature_width, feature_height)) //whole image would have a normalized displacement of 0
		{	
			*depth_map = 0;
		} else
		{
			search_space(depth_map, left, right, image_width, image_height, feature_width, feature_height, maximum_displacement, p);
			//update the depth_map
		}
		depth_map++;
	}
}
