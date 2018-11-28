java kontrola.Preprocesor export_vse export_chyby oks09All oks09Err

javac -encoding UTF-8 -cp kontrola\selenium-server-standalone-2.45.0.jar;oks09All oks09All\*.java
javac -encoding UTF-8 -cp kontrola\selenium-server-standalone-2.45.0.jar;oks09Err oks09Err\*.java

java -cp kontrola\selenium-server-standalone-2.45.0.jar;. kontrola.KontrolaOKS09

rmdir /S /Q oks09All
rmdir /S /Q oks09Err