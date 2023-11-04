import csv
import json
import sys

# Gets argument
args = sys.argv
if len(args) != 2 or not args[1].endswith('.csv'):
    print('Use: csv2json.py [csv_file_path.csv]')
    sys.exit()

csvfpath = args[1]
jsonfpath = csvfpath.replace(".csv", ".json")
print("Convert", csvfpath, "to", jsonfpath)

# Read csv file
csvfile = open(csvfpath, 'r')
fieldnames = csvfile.readline().rstrip().split(",")
reader = csv.DictReader(csvfile, fieldnames)

# Write json file
jsonfile = open(jsonfpath, 'w')
data = []
for row in reader:
    json.dump(row, jsonfile, indent=2)
    jsonfile.write('\n')

# # Using pandas
# import pandas as pd
# pd.read_csv(csvfpath).to_json(jsonfpath, orient="records")