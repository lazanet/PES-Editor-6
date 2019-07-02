all:	
	find ./src -name '*.java'|xargs javac

VER=6.1.1
jar: all
	cd ./src && jar cvfm ../PES.Editor.$(VER).jar ../manifest editor/* splash.png
	find ./src -name '*.class'|xargs rm -f

clean:
	find ./src -name '*.class'|xargs rm -f
	rm -f *.jar

