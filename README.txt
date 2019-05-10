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
currency can be changed to any file name/extension that exists in the projects 
root directory, as long as that file has the correct format. 

currency:
"United States.txt" is the current currency file. To create a new
currency, provide the singular noun for the denomination, then the value
for a single instance of that denomination. Then on the next line, 
give the plural noun for that same denomination. See "United States.txt" for
an example. The app will use whatever currency is listed in the config file.

