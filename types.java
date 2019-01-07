import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

abstract class Program {
    int line;
    public void setLine(int i) {
        line = i;
    }
	public abstract String show(int n);
	public abstract Object interpret();
	public abstract void verify();
};

class Symbol extends Program {
	String symbol;

	public Symbol(String symbol) {
		super();
		this.symbol = symbol;
	}
	
	String symbol() {
		return symbol;
	}

	@Override
	public String show(int n) {
		return symbol;
	}

	@Override
	public Object interpret() {
		return null;
	}

	@Override
	public void verify() {

	}
}

class MainNode extends Program {
	Program child1;
	public MainNode(Program s1){
		child1 = s1;
	}

	public MainNode() { this(null); }

    public void setChild1(Program child1) {
        this.child1 = child1;
    }

    @Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<MainNode>" + "\n" + child1.show(n + 1);
	}

	@Override
	public Object interpret() {
		return child1.interpret();
	}

	@Override
	public void verify() {
		child1.verify();
	}
};

class BoolNode extends Program {
	String value;

	public BoolNode(String value){
		this.value = value;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<BoolNode> " + value + "\n";
	}

	@Override
	public Object interpret() {
		if(value.equals("true")) {
		    return true;
        } else {
		    return false;
        }
	}

	@Override
	public void verify() {

	}
};

class IntNode extends Program {
	String value;

	public IntNode(String value){
		this.value = value;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<IntNode> " + value + "\n";
	}

	@Override
	public Object interpret() {
		return Integer.valueOf(value);
	}

	@Override
	public void verify() {

	}
}

class VarNode extends Program {
	String value;

	public VarNode(String value){
		this.value = value;
	}

	String getValue() {return value;}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<VariableNode> " + value + "\n";
	}

	@Override
	public Object interpret() {
        int index = HomeworkLexer.variables.indexOf(value);

        if(HomeworkLexer.varValues.get(index) == null) {
            if(HomeworkLexer.varNotFound == 0) {
                HomeworkLexer.varNotFound = line;
            }
            return -1;
        }
	    return HomeworkLexer.varValues.get(index);
	}

    @Override
    public void verify() {
        int index = HomeworkLexer.variables.indexOf(value);
        if(index == -1) {
            if(HomeworkLexer.varNotFound == 0) {
                HomeworkLexer.varNotFound = line;
            }
        }
    }
}

class PlusNode extends Program {
	Program child1, child2;
	public PlusNode(Program s1, Program s2){
		child1 = s1;
		child2 = s2;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<PlusNode> +" + "\n" + child1.show(n + 1) + child2.show(n + 1);
	}

	@Override
	public Object interpret() {
		return (int) child1.interpret() + (int) child2.interpret();
	}

    @Override
    public void verify() {
        child1.verify();
        child2.verify();
    }
}


class DivNode extends Program {
	Program child1, child2;
	public DivNode(Program s1, Program s2){
		child1 = s1;
		child2 = s2;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<DivNode> /" + "\n" + child1.show(n + 1) + child2.show(n + 1);
	}

	@Override
	public Object interpret() {
	    int d = (int) child2.interpret();
	    if(d == 0) {
	        if(HomeworkLexer.divisionByZero == 0) {
                HomeworkLexer.divisionByZero = line;
            }
	        return 0;
        }
		return (int) child1.interpret() / (int) child2.interpret();
	}

    @Override
    public void verify() {
        child1.verify();
        child2.verify();
    }
}


class AndNode extends Program {
	Program child1, child2;
	public AndNode(Program s1, Program s2){
		child1 = s1;
		child2 = s2;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<AndNode> &&" + "\n" + child1.show(n + 1) + child2.show(n + 1);
	}

	@Override
	public Object interpret() {
		return (boolean) child1.interpret() && (boolean) child2.interpret();
	}

    @Override
    public void verify() {
        child1.verify();
        child2.verify();
    }
}

class BracketNode extends Program {
	Program child1;
	public BracketNode(Program s1){
		child1 = s1;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<BracketNode> ()" + "\n" + child1.show(n + 1);
	}

	@Override
	public Object interpret() {
        return child1.interpret();
	}

    @Override
    public void verify() {
        child1.verify();
    }
}

class GreaterNode extends Program {
	Program child1, child2;
	public GreaterNode(Program s1, Program s2){
		child1 = s1;
		child2 = s2;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<GreaterNode> >" + "\n" + child1.show(n + 1) + child2.show(n + 1);
	}

	@Override
	public Object interpret() {
		return (int) child1.interpret() > (int) child2.interpret();
	}

    @Override
    public void verify() {
        child1.verify();
        child2.verify();
    }
};


class NotNode extends Program {
	Program child1;
	public NotNode(Program s1){
		child1 = s1;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<NotNode> !" + "\n" + child1.show(n + 1);
	}

	@Override
	public Object interpret() {
		return !((boolean) child1.interpret());
	}

    @Override
    public void verify() {
        child1.verify();
    }
};


class AssignmentNode extends Program {
	Program child1, child2;
	public AssignmentNode(Program s1, Program s2){
		child1 = s1;
		child2 = s2;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<AssignmentNode> =" + "\n" + child1.show(n + 1) + child2.show(n + 1);
	}

	@Override
	public Program interpret() {
	    int index = HomeworkLexer.variables.indexOf(((VarNode) child1).value);
		HomeworkLexer.varValues.set(index, (Integer) child2.interpret());
		return this;

	}

    @Override
    public void verify() {
        int index = HomeworkLexer.variables.indexOf(((VarNode) child1).value);
        if (index == -1 && HomeworkLexer.varNotFound == 0) {
            HomeworkLexer.varNotFound = line;
        }
        child2.verify();
    }
}

class BlockNode extends Program {
    Program child1;

    public BlockNode(Program s1) {
        child1 = s1;
    }

    public BlockNode() {
        child1 = null;
    }

    public void setChild1(Program child1) {
        this.child1 = child1;
    }

    @Override
    public String show(int n) {
        String tabs = "";
        for (int i = 0; i < n; i++) {
            tabs = tabs + "\t";
        }
        if (child1 == null) {
            return tabs + "<BlockNode> {}" + "\n";
        } else {
            return tabs + "<BlockNode> {}" + "\n" + child1.show(n + 1);
        }
    }

    @Override
    public Object interpret() {
        if (child1 != null) {
            child1.interpret();
        }
        return this;
    }

    @Override
    public void verify() {
        if (child1 != null) {
            child1.verify();
        }
    }
}

class IfNode extends Program {
	Program child1, child2, child3;
	public IfNode(Program s1, Program s2, Program s3){
		child1 = s1;
		child2 = s2;
		child3 = s3;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<IfNode> if" + "\n" + child1.show(n + 1) + child2.show(n + 1) + child3.show(n + 1);
	}

	@Override
	public Object interpret() {
        if((boolean) child1.interpret()) {
            child2.interpret();
            return this;
        } else {
            child3.interpret();
            return this;
        }
	}

    @Override
    public void verify() {
        child1.verify();
        if (child2 != null) {
            child2.verify();
        }

        if (child3 != null) {
            child3.verify();
        }
    }
}

class WhileNode extends Program {
	Program child1, child2;
	public WhileNode(Program s1, Program s2){
		child1 = s1;
		child2 = s2;
	}

	@Override
	public String show(int n) {
		String tabs = "";
		for(int i = 0; i < n; i++) {
			tabs = tabs + "\t";
		}
		return tabs + "<WhileNode> while" + "\n" + child1.show(n + 1) + child2.show(n + 1);
	}

	@Override
	public Object interpret() {
	    while((boolean) child1.interpret() && HomeworkLexer.divisionByZero == 0 &&
                HomeworkLexer.varNotFound == 0) {
	        child2.interpret();
        }
		return this;
	}

    @Override
    public void verify() {
        child1.verify();
        child2.verify();
    }

};

class SequenceNode extends Program {
	Program child1, child2;

    public SequenceNode(Program s1, Program s2){
        child1 = s1;
        child2 = s2;
    }

    public SequenceNode() { this(null, null);}

    public SequenceNode(Program s1) {
        this(s1, null);
    }

    void setChild2(Program s2) {
        child2 = s2;
    }

	@Override
	public String show(int n) {
		if(child2 == null) {
            return child1.show(n);
        } else {
            String tabs = "";
            for(int i = 0; i < n; i++) {
                tabs = tabs + "\t";
            }
            return tabs + "<SequenceNode>" + "\n" + child1.show(n + 1) + child2.show(n + 1);
        }
	}

	@Override
	public Object interpret() {
		child1.interpret();
		if(child2 != null) {
		    child2.interpret();
        }
        return this;
	}

    @Override
    public void verify() {
        child1.verify();
        if (child2 != null) {
            child2.verify();
        }
    }

};
