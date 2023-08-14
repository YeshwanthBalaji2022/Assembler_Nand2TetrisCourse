@i                //initialisng i=1 
M=1
@20               //taking n=20
D=A
@n 
M=D
@sum             //initialising sum=0
M=0
@avg             //initialising avg=0
M=0
@j               //initialising a random variable j =0
M=0

(LOOP)           //For loop 
@sum
M=M+D            //sum=sum+i
@i
D=M
@n
D=M-D           //condition for the loop to run, if the condition doest satisfy it goes to AVERAGE loop
@AVERAGE
D;JLE           //if its less than or equal to 0, it jumps to AVERAGE loop

@i
M=M+1           //increments i value for the next iteration
@LOOP
0;JMP

(AVERAGE)
@sum
D=M
@j              // j and AVG stores the same value
M=D
@avg
M=D

@SUBLOOP
0;JMP

(SUBLOOP)      //Another for loop that performs repeated subtraction
@j             //average value is 0
D=M
@n             //number of elements = 20
D=M
@j 
M=M-D          //Sum-number of elements (done in a loop)
D=M
@END 
D;JLT         //when the value becomes less than 0,jumps to end 
@AVG          //Average value is updated 
M=D
@SUBLOOP      //returns back to the label and continues next iteration   
0;JMP

(END)
@END
0;JMP