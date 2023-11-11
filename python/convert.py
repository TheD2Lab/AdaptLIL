import os
import tensorflow as tf
from tensorflow.keras.preprocessing import image

pb_model_dir = "C:\\Users\\LeaseCalcs\Desktop\\d2 lab\\gp3 tracking\\models\\stacked_lstm-Adam0,0014_10-30 20_31_55"
h5_model = "C:\\Users\\LeaseCalcs\Desktop\\d2 lab\\gp3 tracking\\models\\stacked_lstm-Adam0,0014_10-30 20_31_55.h5"

# Loading the Tensorflow Saved Model (PB)
model = tf.keras.models.load_model(pb_model_dir)
print(model.summary())

# Saving the Model in H5 Format
tf.keras.models.save_model(model, h5_model)

# Loading the H5 Saved Model
loaded_model_from_h5 = tf.keras.models.load_model(h5_model)
print(loaded_model_from_h5.summary())