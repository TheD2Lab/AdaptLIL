
# About
This repository contains both the backend and frontend of an intelligent adaptive ontological visualization. Integrated with Gazepoint Api, the visualization applies deep learning techniques to intelligently adapt a visualization to a user's gaze profile. The current adaptations are aimed to reduce clutter, improve readability, and improve understanding of the ontology.

# Differences in original gazepoint-data-analysis repo.
This repository implements the Gazepoint API for real-time gaze data analysis. It also builds the foundation for using gaze data to construct adaptive ontology visualizations (see OntoMapVisAdpative: https://github.com/TheD2Lab/OntoMapVisAdaptive). Lastly, there are data classes for gaze data metrics to enhance reusability of D2Lab experiments

# Requirements
Eye Tracker compatabile with gazepoint
Java 11 or greater
Python 3.X
    - Flask
    - WSGDI
    - TensorFlow

# Setup
1. Ensure you have maven installed (https://maven.apache.org/download.cgi)
2. Create a directory and clone this repository into it
3. Run the following inside the base directory of the repository:
```
mvn compile
mvn package
```
4. The backend is now compiled and can be ran with:
```
java -jar target/iav-ontology-*.0-bin.jar
```
5. Launch and calibrate gazepoint
6. Open the frontend visualization located in web/index.html
7. Continue to the link indented list (to be altered when study commences)
8. At this point the websocket socket should now be connected

# Websocket
The goal of implementing websocket is to invoke adaptive visualization changes into the OntoMap Visualization.
 
# Limitations

1. Access to eye tracking technology
