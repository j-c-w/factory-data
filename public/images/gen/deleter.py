# This is a script that should be put on a crontab on a server to 
# run at least once a day.
# It cleans up the files in this gen directory by deleting the
# ones older than MAX_AGE. 
from datetime import datetime
from os import remove, listdir
from os.path import isfile, join, dirname, realpath
from xml.dom import minidom

MAX_AGE = 60 # In Days
IGNORED_FILES = ['.gitignore', 'README.md', 'deleter.py']
IGNORED_PREFIXES = ['KEEP.']

def can_delete(file):
	for ignored_file in IGNORED_FILES:
		if ignored_file == file:
			return False
	for ignored_prefix in IGNORED_PREFIXES:
		if file.startswith(ignored_prefix):
			return False
	return True

def days_between(d1, d2):
	return abs((d1 - d2).days)

current_dir = dirname(realpath(__file__))
all_files = [ f for f in listdir(current_dir)
              if isfile(join(current_dir, f)) ]
print all_files
all_files = filter(can_delete, all_files)
print all_files

current_date = datetime.today()
files_deleted = 0

for file in all_files:
	should_delete = False
	try:
		doc = minidom.parse(file)
		date_string = doc.getElementsByTagName('date')[0].childNodes[0].data
		date_object = datetime.strptime(date_string, "%d/%m/%Y")
		if (days_between(current_date, date_object) > MAX_AGE):
			should_delete = True
			files_deleted += 1
	except Exception as e:
		print e
		# Probably the result of badly formatted XML (corrupted in all likelyhood)
		should_delete = True
		files_deleted += 1
	if should_delete:
		remove(file)

print "Cleaning successful!"
print "Working with maximum age of ", MAX_AGE, "days"
print files_deleted, "files deleted"
