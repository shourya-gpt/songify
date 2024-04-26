runApp: App.class
	java App

App.class: App.java RedBlackTree.class IterableRedBlackTree.class
	javac App.java

RedBlackTree.class: RedBlackTree.java
	javac -cp .:../junit5.jar RedBlackTree.java

IterableRedBlackTree.class: IterableRedBlackTree.java
	javac -cp .:../junit5.jar IterableRedBlackTree.java

runBDTests: BackendDeveloperTests.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

runFDTests: FrontendDeveloperTests.java
	javac -cp .:../junit5.jar FrontendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c FrontendDeveloperTests

clean:
	rm -f *.class
