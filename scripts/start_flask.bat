ECHO ON
ECHO "Starting python server via batch script"
CALL cd python
ECHO "Updating poetry dependencies"
CALL python -m poetry update
ECHO "Starting flask server"
CALL python -m poetry run python FlaskServer.py
PAUSE
ECHO "Flask server initialized."