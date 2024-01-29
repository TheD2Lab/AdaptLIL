Sorry for the mess that is the training of this model.
Anyways, here is the newsetup for the final design change.
Architecture:
We use conv1d w/ 3 filters and 2 stride to map time as past window, current window, future window
stride converts it all into spatiotemporal space. Feed into LSTM

OntoMapAnalysis
P = participants
p - participant in P
1) Gen train data on participant selected for training
   - Leave out three questions [validation] train/px/validation/gaze_qid.arff
   - remainign questions are [training] train/px/train/gaze_qid.arff
2) Gen test data on participant for testing
   - Preselect (|P| * 0.2) participants for training
   - Leave 3 questions for [retraining] test/px/retrain/retrain/gaze_qid.arff
   - Leave 3 questions for [testing] real world test/test/gaze_qid.arff
Python
Model Training
for each p in P:
   1) Get dir of px
   2) load validation
   3) load train
   4) Train model
5) Save weights
6) Plot over each participant? define evaluation x horizon, what do we want to see change 
as the model trains?

Python
Evaluation
2) Load model with pretrained weights
3) for each p in testP:
   * (Reset hidden state), set back to preloaded state 
   * Train using [retraining] (3 questions)
   * Predict the remaining questions
   * Output accuracy
   * Store in testPids

plot histogram using testPids