# Connect4AI
Connect 4 with an AI. 
Input moves in indexes 0-6.
AI automatically plays.

AI is using a min-max algorithm.
Connect4.java runs the AI with alpha-beta pruning.
Connect4NoPruning.java runs the AI without alpha-beta pruning.

Game runs on .java file. 

Pruning vs NoPruning Folder:
Contains Game that pits both algorithms against each other. 

Code for testing time:

long start1 = System.nanoTime();  
long end1 = System.nanoTime();      
System.out.println("Elapsed Time in nano seconds: "+ (end1-start1));  