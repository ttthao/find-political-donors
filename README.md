# Finding Political Donors
by Tommy Truong

## Approach
Initially, I read the README several times to write down the constraints.
I chose Python and took advantage of the Pandas library, a data analysis library,
to quickly clean up the input data in chunks.

However, I did not find Python's priority queue/heap data structure was sufficient
enough to calculate the running median. Thus, I pivoted to Java, and it was difficult
at first to get back up to speed on basic I/O and data cleaning.

Soon enough, I was able to create 2 classes that helped encapsulate the data for
medianvals_by_date and medianvals_by_zip. The classes are almost identical, where
they both take advantage of a min and max heap to balance the stream of input. I
referenced the "Continuous Median" problem from Cracking the Coding Interview for the
choice of data structures. However, I chose to create doubly nested hash maps to
key on unique contributor IDs and a value of either a hashmap that keyed unique zip codes to my transactions- per- zip class, or that keyed unique transaction date to my 
transaction- per- date class.

I wanted to read in chunks of the input stream, which I was planning to do in Python.
Given more time, I would improve my space time complexity by trying to find a way to
integrate my 2 classes, since the heaps store almost identical data. Also, I would
stream chunks of input data to take avantage of having the data in memory.

## Dependencies
I only used native Java packages, as listed below:
- java.io.*;
- java.util.*;
- java.text.*;
- java.math.*;
- java.util.regex.*;
- java.nio.*;

My java version was "1.8.0_91"

## Run:
To run my solution, please navigate to the parent directory (find-political-donors/) and run:

```
sh run.sh
```

Please ignore the $1.class files that get created.

## Special Thanks
Thank you for this opportunity! I found this challenging and enjoyed the subject matter of this problem.