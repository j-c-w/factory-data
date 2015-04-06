This is the folder where data should be stored.

You can write a custom adapter for this data in the
DataLoader class (in fact, that it a necessity).

There is no data here now because I do not have permission
to release a full data set yet. It is required that data be
in here (or anywhere on the development/deployment machine)
so it can be accessed from the DataLoader.

The other thing that goes in this folder is the default queries.
Although they could very well be requested by a POST request
to the public/images/gen folder (as long as they are prefixed
with KEEP so as not to be deleted), being in this folder means
that they cannot be accessed externally (except through the
example query system)