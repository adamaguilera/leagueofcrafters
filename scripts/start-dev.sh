#!/bin/bash
cd ../../server/ || exit
java -Xmx2048M -Xms2048M -DIReallyKnowWhatIAmDoingISwear=true -jar latest.jar

