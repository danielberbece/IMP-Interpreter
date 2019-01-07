import java.util.*;
import java.util.Stack;

%%
 
%class HomeworkLexer
%line
%int
%{

  	Stack<Program> stack = new Stack<>();
    int variablesRead = 0;
    public static ArrayList<String> variables = new ArrayList<>();
    public static ArrayList<Integer> varValues = new ArrayList<>();
    public static int divisionByZero = 0;
    public static int varNotFound = 0;
    public int currentLine = 1;
    
    void incrementLine() {
        currentLine += 1;
    }

  Program getNthElemFromStack(int numberElems) {
        Stack<Program> tempStack = new Stack<>();

        if (numberElems > stack.size()) {
            return null;
        }

        for (int j = 0; j < numberElems; j++) {
            tempStack.push(stack.pop());
        }

        Program res = tempStack.peek();

        for (int j = 0; j < numberElems; j++) {
            stack.push(tempStack.pop());
        }
        return res;
    }

     Program reduceUntil(String end) {
        Stack<Program> tmpStack1 = new Stack<>();
        Stack<Program> tmpStack2 = new Stack<>();

        while(!(stack.peek() instanceof Symbol) || (stack.peek() instanceof Symbol && !((Symbol)stack.peek()).symbol().equals(end))) {
            tmpStack1.push(stack.pop());
        }
        stack.pop();

        Program p1 = tmpStack1.pop();
        Program op;
        while(tmpStack1.size() != 0) {
            op = tmpStack1.pop();
            Program p2 = tmpStack1.pop();
            if(((Symbol)op).symbol().equals("/")) {
                p1 = new DivNode(p1, p2);
                p1.setLine(currentLine);
            } else {
                tmpStack2.push(p1);
                tmpStack2.push(op);
                p1 = p2;
            }
        }

        tmpStack2.push(p1);
        while(tmpStack2.size() != 0) {
            op = tmpStack2.pop();
            tmpStack1.push(op);
        }

        p1 = tmpStack1.pop();
        while(tmpStack1.size() != 0) {
            op = tmpStack1.pop();
            Program p2 = tmpStack1.pop();
            if (((Symbol) op).symbol().equals("+")) {
                p1 = new PlusNode(p1, p2);
                p1.setLine(currentLine);
            } else {
                tmpStack2.push(p1);
                tmpStack2.push(op);
                p1 = p2;
            }
        }

        while(tmpStack2.size() != 0) {
            op = tmpStack2.pop();
            Program p2 = tmpStack2.pop();
            if(((Symbol)op).symbol().equals(">")) {
                p1 = new GreaterNode(p2, p1);
                p1.setLine(currentLine);
            } else {
                tmpStack1.push(p1);
                tmpStack1.push(op);
                p1 = p2;
            }
        }

        while(tmpStack1.size() != 0) {
            op = tmpStack1.pop();
            Program p2 = tmpStack1.pop();
            if (((Symbol) op).symbol().equals("&&")) {
                p1 = new AndNode(p1, p2);
                p1.setLine(currentLine);
            }
        }

        if(end.equals("=")) {
            Program var = stack.pop();
            p1 = new AssignmentNode(var, p1);
            p1.setLine(currentLine);
        }

        return p1;
    }

    void semiColon(){
      if(variablesRead == 0) {
        variablesRead = 1;
        while(stack.size() != 1) {
          Program p = stack.pop();
          if(p instanceof VarNode) {
                    variables.add(((VarNode) p).getValue());
                    varValues.add(null);
                }
        }
            Collections.reverse(variables);
            stack.pop();
        Program mainNode = new MainNode();
        stack.push(mainNode);
      } else {
            Program v = reduceUntil("=");
        // Should check for sequence?
            Program seqNode = new SequenceNode(v);
            Program anteriorProgram = stack.peek();
            if(anteriorProgram instanceof MainNode) {
                ((MainNode) anteriorProgram).setChild1(seqNode);
            } else if(anteriorProgram instanceof SequenceNode) {
                stack.pop();
                ((SequenceNode) anteriorProgram).setChild2(seqNode);
            } else if(anteriorProgram instanceof BlockNode) {
                ((BlockNode) anteriorProgram).setChild1(seqNode);
            }
      
      stack.push(seqNode);
      }
    }

    void closePar(){
        Program brNode = reduceUntil("(");
        brNode = new BracketNode(brNode);
        Program an = stack.peek();
        if(an instanceof Symbol && ((Symbol) an).symbol.equals("!")) {
            stack.pop();
            brNode = new NotNode(brNode);
        }
        
        stack.push(brNode);
    }

    void appendNodeToFather(Program node) {
        if(stack.peek() instanceof SequenceNode) {
            SequenceNode beforeSeq = (SequenceNode) stack.pop();
            beforeSeq.setChild2(node);
        } else if(stack.peek() instanceof BlockNode) {
            BlockNode blockNode = (BlockNode) stack.peek();
            blockNode.setChild1(node);
        }
    }

    void closeBlock() {

      Program beforeP = stack.pop();
        if(beforeP instanceof BlockNode) {
            stack.push(beforeP);
        }

        // Check for if:
        Program p = getNthElemFromStack(2);
        if(p instanceof Symbol && ((Symbol)p).symbol.equals("else")) {
            Program elseBlock = stack.pop();
            stack.pop();
            Program ifBlock = stack.pop();
            Program condition = stack.pop();
            stack.pop();
            Program ifNode = new IfNode(condition, ifBlock, elseBlock);
            ifNode = new SequenceNode(ifNode);
            appendNodeToFather(ifNode);
            stack.push(ifNode);
        } else {    // Check for while:
            p = getNthElemFromStack(3);
            if (p instanceof Symbol && ((Symbol)p).symbol.equals("while")){
                Program block = stack.pop();
                Program condition = stack.pop();
                stack.pop();    // Pop the "while" symbol
                Program whileNode = new WhileNode(condition, block);
                whileNode = new SequenceNode(whileNode);
                appendNodeToFather(whileNode);
                stack.push(whileNode);
            }
        }
    }

    void checkNotNode() {
      Program p = getNthElemFromStack(2);
      if(p instanceof Symbol && ((Symbol)p).symbol.equals("!")) {
        p = stack.pop();
        stack.pop();
        p = new NotNode(p);
        stack.push(p);
      }
    }

%}

digit = [1-9]
number = {digit}({digit}|0)* | 0
string = [a-z]+
var = {string}
a_val = {number}
b_val = "false" | "true"

openblock = "{"
closeblock = "}"

openpar = "("
closepar = ")"

assign = "="
semicolon = ";"

plus = "+"
div = "/"

and = "&&"
greater = ">"
not = "!"

if_ = "if"
else_ = "else"
while_ = "while"

init = "int"
newline = "\n"|"\r\n"

%%

{init}			{ stack.push(new Symbol("int")); }
{openblock}		{ stack.push(new BlockNode()); }
{openpar}		{ stack.push(new Symbol("(")); }
{if_}			{ stack.push(new Symbol("if")); }
{else_}			{ stack.push(new Symbol("else")); }
{while_}		{ stack.push(new Symbol("while")); }
{plus}			{ stack.push(new Symbol("+")); }
{div}			{ stack.push(new Symbol("/")); }
{and}			{ stack.push(new Symbol("&&")); }
{greater}		{ stack.push(new Symbol(">")); }
{not}			{ stack.push(new Symbol("!")); }
{b_val}			{ stack.push(new BoolNode(yytext())); checkNotNode();}
{var}			{ Program p = new VarNode(yytext()); p.setLine(currentLine); stack.push(p); }
{assign}		{ stack.push(new Symbol("=")); }
{semicolon} 	{ semiColon(); }
{a_val}			{ stack.push(new IntNode(yytext())); }
{closeblock}	{ closeBlock();}
{closepar}		{ closePar();}
{newline}		{ incrementLine(); }
. {}


