#rem  First use Spring to package.it into a jar、and then use:jpackage to package it into an-exe.
#mac 输出为target下的bill.app
set -m
/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/jpackage --name bill --input target/dist --main-jar billSB-0.0.1-SNAPSHOT.jar --type app-image --dest target

#win
#set JDKHOME=E:\JDK\jdk-17
#%JDKHOME%\bin\jpackage --name bill --input target/dist --main-jar billSB-0.0.1-SNAPSHOT.jar --type app-image --dest target
