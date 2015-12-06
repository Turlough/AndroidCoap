#! /bin/bash

mvn install:install-file \
-Dfile=$ANDROID_HOME/platforms/android-21/android.jar \
-DgroupId=com.google.android \
-DartifactId=android \
-Dversion=5.0.1_r2 \
-Dpackaging=jar \
-DgeneratePom=true

