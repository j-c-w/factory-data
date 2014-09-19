This is a set of features I would very much like to implement should I have the time. It would really take this project to the next
level. However, right now, I feel committed to focus on the project itself and not any future extensibility.


This is a temporary file for me to have a try at making this whole program generic (with the exception of the loading classes).

The idea is to pass a list of column headers, which will be used for the toHtml etc. and then a List of rows.

Ideally, each row should be a list of types that inherit from a single DataCell class. This class will take a string
as input and convert to Double/Date/String as nessecary. Additionally, this DataCell trait should contain the standard
mergeSum, mergeAverage methods etc.

The List of rows should have a few util methods (toHtml etc.)

Finally, the List of DataCells (i.e. each row) should be its own class to allow for a row-row merge. Equally, each row needs various util methods
(toHtml etc)

This would enable me to use this in the LineListObject thereby saving me much time in doing all the work again. 

I am going to sit on this idea for a few days an try to decide whether it is a bad idea.