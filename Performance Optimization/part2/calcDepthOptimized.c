// CS 61C Fall 2014 Project 3

// include SSE intrinsics
#if defined(_MSC_VER)
#include <intrin.h>
#elif defined(__GNUC__) && (defined(__x86_64__) || defined(__i386__))
#include <x86intrin.h>
#endif
#include <math.h>
#include "calcDepthOptimized.h"
#include "calcDepthNaive.h"
#include <stdbool.h>
#include "utils.h"
#include <memory.h>
// include OpenMP
#if !defined(_MSC_VER)
#include <pthread.h>
#endif
#include <omp.h>

#define ABS(x) (((x) < 0) ? (-(x)) : (x))
#define MAX(a, b) ( (a > b) ? a : b )
#define MIN(a, b) ( (a < b) ? a : b )

// Implements the displacement function
float displacementOptimized(int dx, int dy)
{
	float squaredDisplacement = dx * dx + dy * dy;
	float displacement = sqrt(squaredDisplacement);
	return displacement;
	// return dx * dx + dy * dy; //squareDisplacement
}

void calcDepthOptimized(float *depth, float *left, float *right, int imageWidth, int imageHeight, int featureWidth, int featureHeight, int maximumDisplacement)
{
	// calcDepthNaive(depth, left, right, imageWidth, imageHeight, featureWidth, featureHeight, maximumDisplacement, NULL);
	// memset(depth, 0, imageHeight*imageWidth*sizeof(float));
	// int range = (2*featureWidth+1)/4*4;
	// float arr[4];
	// float difference;
	// float remaining;
	// __m128 difference_4 = _mm_setzero_ps(); // sets up empty difference vector, 4 32-bit operands
	// __m128 squaredDifference_4 = _mm_setzero_ps(); // sets up empty squaredDifference vector, 4 32-bit operands

	
	memset(depth, 0, imageHeight*imageWidth*sizeof(float));
	int range = (2*featureWidth+1)/4*4;
	// float arr[4];
	// float difference;
	// float remaining;
	// __m128 difference_4 = _mm_setzero_ps(); // sets up empty difference vector, 4 32-bit operands
	// __m128 squaredDifference_4 = _mm_setzero_ps(); // sets up empty squaredDifference vector, 4 32-bit operands
	// #pragma omp parallel
	// {
	#pragma omp parallel for
	for (int y = featureHeight; y < (imageHeight-featureHeight); y++)
	{
		int y_times_imageWidth = y * imageWidth; //y * imageWidth
		int start_dy = MAX(-maximumDisplacement, (featureHeight - y));
		int end_dy = MIN(maximumDisplacement, (imageHeight - featureHeight - y - 1));
		// __m128 displacementNaive1_4 = _mm_setzero_ps(); // sets up empty displacementNaive1 vector
		for (int x = featureWidth; x < (imageWidth-featureWidth); x++)
		{
			float minimumSquaredDifference = -1;
			int minimumDy = 0;
			int minimumDx = 0;
			
			int start_dx = MAX(-maximumDisplacement, (featureWidth - x));
			int end_dx = MIN(maximumDisplacement, (imageWidth - featureWidth - x - 1));
			for (int dy = start_dy; dy <= end_dy; dy++)
			{
				int y_plus_dy = y + dy; //y + dy
				for (int dx = start_dx; dx <= end_dx; dx++)
				{
					int x_plus_dx = x + dx; //x + dx
					float squaredDifference = 0;
					__m128 squaredDifference_4 = _mm_setzero_ps(); // sets up empty squaredDifference vector, 4 32-bit operands
					for (int boxX = -featureWidth; boxX < (range - featureWidth); boxX+=4)
					{
						for (int boxY = -featureHeight; boxY <= featureHeight; boxY++)
						{
							__m128 difference_4 = _mm_sub_ps(_mm_loadu_ps(left + (y + boxY) * imageWidth + (x + boxX)), _mm_loadu_ps(right + (y_plus_dy + boxY) * imageWidth + (x_plus_dx + boxX)));
							squaredDifference_4 = _mm_add_ps(squaredDifference_4, _mm_mul_ps(difference_4, difference_4));
						}
					}
					float arr[4];
					_mm_storeu_ps(arr, squaredDifference_4);
					squaredDifference = arr[0] + arr[1] + arr[2] + arr[3];

					if ((squaredDifference <= minimumSquaredDifference) || (minimumSquaredDifference == -1))
					{
						if (featureWidth % 2 == 0) //only have one leftover, which is the last one
						{
							float remaining = 0;
							int leftX = x + featureWidth;
							int rightX = x_plus_dx + featureWidth;
							for (int boxY = -featureHeight; boxY <= featureHeight; boxY++)
							{								
								float difference = left[(y + boxY) * imageWidth + leftX] - right[(y_plus_dy + boxY) * imageWidth + rightX];
								remaining += difference * difference;
							}
							squaredDifference += remaining;
						} else //have 3 leftovers, so use a vector
						{
							squaredDifference_4 = _mm_setzero_ps(); // sets up empty squaredDifference vector, 4 32-bit operands			
							int leftX = x + (range - featureWidth) - 1; //minus 1 and take the rest
							int rightX = x_plus_dx + (range - featureWidth) - 1;
							for (int boxY = -featureHeight; boxY <= featureHeight; boxY++)
							{
								__m128 difference_4 = _mm_sub_ps(_mm_loadu_ps(left + (y + boxY) * imageWidth + leftX), _mm_loadu_ps(right + (y_plus_dy + boxY) * imageWidth + rightX));
								squaredDifference_4 = _mm_add_ps(squaredDifference_4, _mm_mul_ps(difference_4, difference_4));
							}
							_mm_storeu_ps(arr, squaredDifference_4);
							squaredDifference += arr[1] + arr[2] + arr[3];
						}
					}

					if ((minimumSquaredDifference == -1) || ((minimumSquaredDifference == squaredDifference) && ((dx * dx + dy * dy) < (minimumDx * minimumDx + minimumDy * minimumDy))) || (minimumSquaredDifference > squaredDifference))
					{
						minimumSquaredDifference = squaredDifference;
						minimumDx = dx;
						minimumDy = dy;
					}
				}
			}

			if (minimumSquaredDifference != -1)
			{
				if (maximumDisplacement == 0)
				{
					depth[y_times_imageWidth + x] = 0;
				}
				else
				{
					depth[y_times_imageWidth + x] = displacementOptimized(minimumDx, minimumDy);
				}
			}
			else
			{
				depth[y_times_imageWidth + x] = 0;
			}
		}
		// depth[y_times_imageWidth + x] = sqrt(displacementNaive1(minimumDx, minimumDy));
		// for (int x = 0; x < imageWidth/4*4; x+=4) //sqrt each value in depth using a vector
		// {

		// 	displacementNaive1_4 = _mm_sqrt_ps(_mm_loadu_ps(depth + y_times_imageWidth + x));
		// 	_mm_storeu_ps((depth + y_times_imageWidth + x), displacementNaive1_4);
		// }

		// for (int x = imageWidth/4*4; x < imageWidth; x++) //tail case
		// {
		// 	depth[y_times_imageWidth + x] = sqrt(depth[y_times_imageWidth + x]);
		// }	
	}
		
	// }
}