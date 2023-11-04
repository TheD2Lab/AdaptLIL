# Will parse through .csv files in same directory
import csv

# Current participant file
partNum = 0

# Participants counted/discarded from analysis
counted = []
discard = [9, 18, 28, 31, 33, 41, 57, 75]

# Choose scope of analysis/output
print("1. Overall Study\n"
      "2. LIL Visualization\n"
      "3. Matrix Visualization\n")

studyType = int(input("1-3: "))
print()

error = False
study = ""

# Overall scope begins on P1 & includes all non-discarded files
if studyType == 1:
    study = "overall"
    partNum = 1

# LIL scope begins on P1 & includes all odd-numbered files, plus P80
elif studyType == 2:
    study = "lil"
    partNum = 1

# Matrix scope begins on P2 & includes all even-numbered files, plus P81
elif studyType == 3:
    study = "matrix"
    partNum = 2

else:
    print("Error.")
    error = True

if not error:

    # Initialize & open output file
    outputFile = "reaction.compile." + study + ".csv"

    with open(outputFile, mode='w', newline='') as resultsFile:
        fileWriter = csv.writer(resultsFile, delimiter=',')

        # Iterate through each participant file until completion
        done = False
        while not done:

            inputFile = "p" + str(partNum) + ".reaction.csv"

            # Open current participant file & copy reaction cards into output file
            with open(inputFile, mode='r') as partFile:
                fileReader = csv.reader(partFile, delimiter=',')

                # Prepend participant number to correspond with chosen reaction cards
                for row in fileReader:
                    numRow = ["p" + str(partNum)] + row
                    fileWriter.writerow(numRow)

            # Mark current participant file as counted
            counted.append(partNum)

            # For overall scope, move on to next immediate non-discarded file
            if studyType == 1:
                partNum += 1

                while partNum in discard:
                    partNum += 1

            # For LIL/matrix scope, skip over by one & parse through every other non-discarded file
            else:

                # The final files in LIL/matrix scope breaks the +2 pattern
                if partNum == 78:
                    partNum += 3

                elif partNum == 79:
                    partNum += 1

                else:
                    partNum += 2

                    while partNum in discard:
                        partNum += 2

            # P81 is the last file
            if partNum > 81:
                done = True

        fileWriter.writerow([])

        # List the included/discarded participant files for manual double-checking
        fileWriter.writerow(["Participants counted:"])
        fileWriter.writerow(counted)

        fileWriter.writerow(["Participants discarded:"])
        fileWriter.writerow(discard)

