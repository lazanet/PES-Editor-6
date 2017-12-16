rm build/*.class
rm build/editor/*.class

javac src/*.java -d build
javac src/editor/*.java -d build
