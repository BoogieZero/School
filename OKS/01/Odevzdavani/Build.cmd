javac -d . -encoding UTF-8 src\logoscislo\*.java
echo Main-Class: logoscislo.Hlavni>man.txt
jar cmf man.txt oks-01.jar logoscislo src