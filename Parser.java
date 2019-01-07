// Author: Vlad Nedelcu

import java.io.*;
import java.util.*;

public class Parser {

    public static void writeAST(String AST) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("arbore"));
            writer.write(AST);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeResult() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output"));
            String output = "";
            if (HomeworkLexer.varNotFound != 0) {
                output = "UnassignedVar " + String.valueOf(HomeworkLexer.varNotFound) + '\n';
            } else if (HomeworkLexer.divisionByZero != 0) {
                output = "DivideByZero " + String.valueOf(HomeworkLexer.divisionByZero) + '\n';
            } else {
                for(int i = 0; i < HomeworkLexer.variables.size(); i++) {
                    Integer value = HomeworkLexer.varValues.get(i);
                    output = output + HomeworkLexer.variables.get(i) + "=" + value + '\n';
                }
            }
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main (String[] args) throws IOException {
		HomeworkLexer l = new HomeworkLexer(new FileReader("input"));

		l.yylex();
        l.stack.pop();
        Program mainNode = l.stack.peek();
		writeAST(mainNode.show(0));
		mainNode.verify();
        if (HomeworkLexer.varNotFound == 0) {
            mainNode.interpret();
        }
        writeResult();
	}
}