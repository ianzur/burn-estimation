# Burn Estimation app

The goal of this application is to estimate percentage total burn skin area (%TBSA) from an image. 

Patient data is stored locally, on device, using Room (an abstraction layer over SQLite Database).
Patient images are stored in app specific files and cannot be accessed outside of the app, (they won't show up in the gallery or file explorer).

The `python` directory contains several scripts to we used to test different methods.

Known bugs:
- image shown in patient detail fragment has incorrect orientation
- currently segmentation model executes in the UI thread, this blocks the UI and sometimes results in a crash.
- current segmentation model (deeplab)[https://arxiv.org/pdf/1606.00915.pdf] does not properly segment several tested burn patients.
- there is no burn segmentation (%TBSA shown is a random number)

Contact:
- If you are interested in continuing this project please reach out. Raise an issue here and @ianzur in the post.