build:	Parser.java HomeworkLexer.java types.java
	javac *.java
run:	Parser.class
	java Parser
clean:
	rm -rf *.class arbore output
