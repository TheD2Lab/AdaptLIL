ECHO ON
ECHO "Starting python server via batch script"
CALL conda activate gaze_metrics
ECHO "calling python"
CALL python python/FlaskServer.py
PAUSE
ECHO "exiting script?"