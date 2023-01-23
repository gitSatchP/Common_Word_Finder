# Common_Word_Finder
## Introduction:
Common_Word_Finder is a command line Java application to find the 𝑛 most common
words in a document. Written from scratch by me as a final project for my data
structures class, it uses three of the data structures we developed for
previous programming assignments to create a program that finds the 𝑛 most
common words in a document. BSTMap, AVLTreeMap, and MyHashMap implement the
MyMap interface (respectively, these class names refer to Binary Search Tree,
AVL Tree, and Hash Map data structures). Using polymorphism, I create any one
of these data structures and refer to it from a MyMap reference. Though all
three classes implement the same interface, their methods are implemented very
differently, leading to different execution times on the computer.

## Project upload details:
I only included the Common_Word_Finder class in this repository because of the
many classes used by this program, this is the only one that was written
entirely from scratch by me. In past programming assignments for the class
to create implementations of the BST, AVL Tree, and Hash Map data structures,
as well as other classes used by this program such as Entry (a class for
encapsulating a key-value entry in a map), and Node (a class for a Node
containing a key-value mapping), I either filled in empty methods in an
otherwise functional program, or was provided a template, and didn't want
to put these on my public GitHub without the permission of the professor.
While the program is not functional without these other classes, I
included it on my GitHub as an example of the type of code that can be
expected of me to write.

##Project spec details:
After validating all command line arguments, my program instantiates either a
BSTMap, AVLTreeMap, or MyHashMap. The key-value pairs are String-to-Integer,
where String is the word and Integer is the number of times the word is
found in the document. Lowercase letters (a-z) and single quotes (') are legal
characters for words. Hyphens (-) are legal too, as long as they are not the
first character in a word. Uppercase letters (A-Z) are converted to lowercase
letters before putting them into a word. Words are separated by end-of-line
characters and spaces. Every time the program parses a word for the first
time, it is inserted into the map with a count of 1. If it has been seen
before, the count associated with it is incremented by 1. The first line of
output displays the following: "Total unique words: <some positive integer>"
Then up to <limit> words and their counts are displayed, right-aligned to the
width of the largest number. The word in all lowercase letters appears after
the period, left-aligned with one space between the period and the word. If two
lowercase words have the same count, the words are alphabetized in the output.
Finally, the count of the words appears at the end of each line, with one space
between the longest word and the count.

## Time analysis:
The results of taking the average of 10 running time commands on a .txt file of
the Bible for each terminal command below are as follows:

time java CommonWordFinder Bible.txt bst 20000
real	0m0.393s
user	0m0.372s
sys	    0m0.053s

time java CommonWordFinder Bible.txt avl 20000
real	0m0.346s
user	0m0.421s
sys	    0m0.042s

time java CommonWordFinder Bible.txt hash 20000
real	0m0.231s
user	0m0.354s
sys	    0m0.042s

From these results, the Hash Map performed the fastest, followed by AVL, and
then BST. Although, considering individual time tests the AVL Tree is sometimes
faster than BST and BST is sometimes faster than AVL Tree. Hash Map was
consistently faster than the other two data structures. This matched my
expectations as I expected the AVL Tree data structure to outperform BST but
didn't expect there to be a huge time difference between BST and AVL Tree as in
our implementation of these two classes, entries are sorted by key. In this
case, this means the BST is in increasing alphabetical ordering for right
subtrees and decreasing alphabetical ordering for left subtrees so the BST
should never really become widely imbalanced as long as there is a similar
count of words starting with the beginning of the alphabet and words starting
with the end of the alphabet. I also expected Hash Map to consistently
perform the best, as search and insert operations are being called 13680 times
each for Bible.txt, so its average Theta(1) time search and insert are much
faster than the average Theta(logn) for AVL Tree and BST.
