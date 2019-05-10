Running the application:
The application can be compiled/executed with the "start" bash script in the project's root
folder. The "start" script takes the payments file name or location as an argument. 
Example: ./start test_input.txt
Results are printed to the change.txt file.

The project can be run with the above command as is, and all of the current defaults
will be used. The configuration/currency can also be changed, if desired.

configuration:
The config file contains the currency file name ("United States") and the
random trigger ("3"). The random trigger can be changed to any integer. The 
currency can be changed to any text file that exists in the project's 
root directory, as long as that file has the correct format. 

currency:
"United States.txt" is the current currency file. To create a new
currency, provide the singular noun for the denomination, then the value
for a single instance of that denomination. Then on the next line, 
give the plural noun for that same denomination. See "United States.txt" for
an example. The app will use whatever currency is listed in the config file.

Thought process for this application:
* What might happen if the client needs to change the random divisor? 
  This can be done by changing the random property in the config file
* What might happen if the client needs to add another special case (like the random twist)? 
  A new implementation of the ChangeProcessor interface can be created or a new child of
  the BasicChangeProcessor can be created so that only the code that is relevant to the 
  special case needs to be written. 
* What might happen if sales closes a new client in France? 
  A new currency file can easily be created by the client and the config can be edited by 
  the client as well, so that the new currency can be used.

