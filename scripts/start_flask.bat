ECHO ON
ECHO "Starting python server via batch script"
python -m flask --app ../python/FlaskServer.py run
PAUSE