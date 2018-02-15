# Population-calculation-
Calculated the population of a state or area in a rectangular grid using JAVA and swing.
The file CenPop2010.txt contains real data published by the U.S. Census Bureau.
The data divides the U.S. into 220,333 geographic areas called "census-block-groups" and reports for each such group the population in 2010 and the latitude/longitude of the group. It actually reports the average latitude/longitude of the people in the group.
To implement the program, we will need to determine within which grid rectangle each census-block-group lies. This requires computing the minimum and maximum latitude and longitude over all the census-block-groups.

Version 1: Simple and Sequential

Before processing any queries, process the data to find the four corners of the U.S. rectangle using a sequential O(n) algorithm where n is the number of census-block-groups. Then for each query do another sequential O(n) traversal to answer the query (determining for each census-block-group whether or not it is in the query rectangle). The simplest and most reusable approach for each census-block-group is probably to first compute what grid position it is in and then see if this grid position is in the query rectangle.

Version 2: Simple and Parallel

This version is the same as version 1 except both the initial corner-finding and the traversal for each query should use the Fork-Join Framework effectively. The work will remain O(n), but the span should lower to O(log n). Finding the corners should require only one data traversal, and each query should require only one additional data traversal.

Version 3: Smarter and Sequential

This version will, like version 1, not use any parallelism, but it will perform additional preprocessing so that each query can be answered in O(1) time. This involves two additional steps:

    First create a grid of size x*y (use an array of arrays) where each element is an int that will hold the total population for that grid position. Recall x and y are the command-line arguments for the grid size. Compute the grid using a single traversal over the input data.
    Now modify the grid so that instead of each grid element holding the total for that position, it instead holds the total for all positions that are neither farther East nor farther South. In other words, grid element g stores the total population in the rectangle whose upper-left is the North-West corner of the country and the lower-right corner is g. This can be done in time O(x*y) but you need to be careful about the order you process the elements. Keep reading...

For example, suppose after step 1 we have this grid:

0   11   1  9
1   7    4  3
2   2    0  0
9   1    1  1

Then step 2 would update the grid to be:

0   11  12  21
1   19  24  36
3   23  28  40
12  33  39  52

There is an arithmetic trick to completing the second step in a single pass over the grid. Suppose our grid positions are labeled starting from (1,1) in the bottom-left corner. (You can implement it differently, but this is how queries are given.) So our grid is:

(1,4)  (2,4)  (3,4)  (4,4)
(1,3)  (2,3)  (3,3)  (4,3)
(1,2)  (2,2)  (3,2)  (4,2)
(1,1)  (2,1)  (3,1)  (4,1)

Now, using standard Java array notation, notice that after step 2, for any element not on the left or top edge: grid[i][j]=orig+grid[i-1][j]+grid[i][j+1]-grid[i-1][j+1] where orig is grid[i][j] after step 1. So you can do all of step 2 in O(x*y) by simply proceeding one row at a time top to bottom -- or one column at a time from left to right, or any number of other ways. The key is that you update (i-1 , j), (i , j+1) and (i-1 , j+1) before (i , j).

Given this unusual grid, we can use a similar trick to answer queries in O(1) time. Remember a query gives us the corners of the query rectangle. In our example above, suppose the query rectangle has corners (3,3), (4,3), (3,2), and (4,2). The initial grid would give us the answer 7, but we would have to do work proportional to the size of the query rectangle (small in this case, potentially large in general). After the second step, we can instead get 7 as 40 - 21 - 23 + 11. In general, the trick is to:

    Take the value in the bottom-right corner of the query rectangle.
    Subtract the value just above the top-right corner of the query rectangle (or 0 if that is outside the grid).
    Subtract the value just left of the bottom-left corner of the query rectangle (or 0 if that is outside the grid).
    Add the value just above and to the left of the upper-left corner of the query rectangle (or 0 if that is outside the grid).

Notice this is O(1) work. 

Version 4: Smarter and Parallel

As in version 2, the initial corner finding should be done in parallel. As in version 3, you should create the grid that allows O(1) queries. The first step of building the grid should be done in parallel using the ForkJoin Framework. The second step should remain sequential; just use the code you wrote in version 3. Parallelizing it is part of the Above & Beyond.

To parallelize the first grid-building step, you will need each parallel subproblem to return a grid. To combine the results from two subproblems, you will need to add the contents of one grid to the other. Although for small grids doing this sequentially may be okay, you will need to parallelize this as well using another ForkJoin computation. (To test that this works correctly, you may need to set a sequential-cutoff lower than your final setting.)



Version 5: Smarter and Lock-Based

Version 4 may suffer from doing a lot of grid-copying in the first grid-building step. An alternative is to have just one shared grid that different threads add to as they process different census-block-groups. But to avoid losing any of the data, that means grid elements need to be protected by locks. To allow simultaneous updates to distinct grid elements, each element should have a different lock.




