# Will parse through .csv files in same directory
import csv
import re

# Word counter dictionary & current participant file
reactionDict = dict()
partNum = 0

# Participant files counted/discarded from analysis
counted = []
discard = [9, 18, 28, 31, 33, 41, 57, 75]

# Full list of reaction card words
reactionList = ["Advanced", "Annoying", "Approachable", "Attractive", "Busy", "Clean", "Collaborative", "Comfortable",
                "Complex", "Comprehensive", "Confusing", "Consistent", "Convenient", "Creative", "Cutting edge",
                "Dated", "Desirable", "Difficult", "Dull", "Easy to use", "Effective", "Efficient", "Engaging",
                "Entertaining", "Essential", "Exceptional", "Exciting", "Familiar", "Fast", "Flexible", "Friendly",
                "Helpful", "High quality", "Impersonal", "Inconsistent", "Ineffective", "Innovative", "Inspiring",
                "Intimidating", "Inviting", "Irrelevant", "Organized", "Overwhelming", "Patronizing", "Personal",
                "Poor quality", "Powerful", "Predictable", "Professional", "Relevant", "Reliable", "Rigid",
                "Satisfying", "Simplistic", "Straight Forward", "Stressful", "Time-consuming", "Trustworthy",
                "Unattractive", "Unconventional", "Undesirable", "Unpredictable", "Unrefined", "Useful"]

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

    # Iterate through each participant file until completion
    done = False
    while not done:

        # Open current participant file & add reaction cards to word counter dictionary
        inputFile = "p" + str(partNum) + ".reaction.csv"

        with open(inputFile, mode='r') as partFile:
            fileReader = csv.reader(partFile, delimiter=',')

            for row in fileReader:

                for word in row:

                    # Exclude "words" denoting current participant number
                    if re.search("[0-9]", word) is None:

                        if word in reactionDict:
                            reactionDict[word] += 1

                        else:
                            reactionDict[word] = 1

        # Mark current participant file as counted
        counted.append(partNum)

        # For overall scope, move on to next immediate non-discarded file
        if studyType == 1:
            partNum += 1

            while partNum in discard:
                partNum += 1

        # For LIL/matrix scope, skip over by one & parse through every other non-discarded file
        else:

            # The final files in LIL/matrix scope break the +2 pattern
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

    # Include the reaction card words not chosen by participants
    for word in reactionList:
        if word not in reactionDict:
            reactionDict[word] = 0

    # Output word counter dictionary as .csv file
    outputFile = "reaction.count." + study + ".csv"

    with open(outputFile, mode='w', newline='') as resultsFile:
        fileWriter = csv.writer(resultsFile, delimiter=',')

        for key in reactionDict:
            fileWriter.writerow([key, reactionDict[key]])

        fileWriter.writerow([])

        # List the included/discarded participant files for manual double-checking
        fileWriter.writerow(["Participants counted:"])
        fileWriter.writerow(counted)

        fileWriter.writerow(["Participants discarded:"])
        fileWriter.writerow(discard)
