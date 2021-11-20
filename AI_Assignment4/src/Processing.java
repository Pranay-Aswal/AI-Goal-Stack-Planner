import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Processing {

    List<Object> StateStart = new ArrayList<>();

    List<Object> StateGoal = new ArrayList<>();

    List<Object> StateCurr = new ArrayList<>();

    Stack<Object> goalStack;

    List<Object> plan = new ArrayList<>();

    public boolean Containing(List<Object> l, Object o){

        boolean b=false;

        for (Object ob : l) {
            if (ob.getClass().equals(Predicates.class)) {
                if (o.getClass().equals(Predicates.class)) {
                    Predicates pdash = (Predicates) o;
                    Predicates phash = (Predicates) ob;
                    if (pdash.pred.equals(phash.pred) && pdash.block == phash.block) {
                        b = true;
                    }

                }
            } else if (ob.getClass().equals(OnPredicate.class)) {
                if (o.getClass().equals(OnPredicate.class)) {
                    OnPredicate pdash = (OnPredicate) o;
                    OnPredicate phash = (OnPredicate) ob;
                    if (pdash.Onpred.equals(phash.Onpred) && pdash.block1 == phash.block1 && pdash.block2 == phash.block2) {
                        b = true;
                    }

                }
            }


        }
        return(b);

    }

    public Processing(List<Object> S, List<Object> G){
        goalStack = new Stack<>();
        StateStart = S;
        StateGoal = G;
        StateCurr = StateStart;
        make_goalStack();
        //create goal stack
    }


    private void make_goalStack(){
        // System.out.println("yeah");
        List<Object> tsubg = new ArrayList<>();
        List<Object> g = new ArrayList<>();
        for (Object o : StateGoal) {
            if (Containing(StateStart, o)) {
                tsubg.add(o);
            } else {
                g.add(o);
            }
        }
        goalStack.push(StateGoal);
        for (Object o : g) {
            goalStack.push(o);
            System.out.println(o + " is pushed in goal");

        }
    }


    public void function_process(){

        while(!goalStack.isEmpty()){
            //pop - check object type - get class
            Object obj = goalStack.pop();
            System.out.println("Popped object is: " + obj);
            //if object is pred

            if(obj.getClass().equals(Predicates.class) || obj.getClass().equals(OnPredicate.class) ){
                //check if pred holds or not - that is if curr state contains the curr pred
                //if true - pop it
                if(Containing(StateCurr,obj)){
                    // System.out.println("fo");
                    //already popped
                    System.out.println("already popped "+ obj);

                    continue;
                }
                else{
                    //if not - check from which operator it might have resulted --- make a seperate for this as well
                    Object op = operator_for_pred(obj);
                    //add that operator to the goal stack along with its precons
                    System.out.println("Adding " + obj + " to goal stack");
                    push_op(op);
                }
            }


            //if object is operator 
            else if(obj.getClass().equals(Operators.class) || obj.getClass().equals(OnOperator.class) ){
                //add to plan and change curr state

                //add list to curr state
                op_add_list(obj);
                System.out.println("adding " + obj + " to current state");
                //del list from curr state
                op_del_list(obj);
                System.out.println("deleting "+ obj +" from current list");

                plan.add(obj);
            }

            //write the case for  obj being the list of all goals

            else if(obj.getClass().equals(ArrayList.class)){
                List<Object> list = (ArrayList<Object>) obj;
                for (Object o : list) {
                    if (!Containing(StateCurr, o)) {
                        goalStack.push(o);
                    }
                }

                System.out.println("making "+obj+" listed in all goals");


            }
        }
    }

    //push an operator in the goal stack - after pushing op, push its precons as well
    private void push_op(Object obj){
        //push the operator
        goalStack.push(obj);
        //push its pre-conditions
        if(obj.getClass().equals(OnOperator.class)){
            OnOperator op2 = (OnOperator)obj;
            System.out.println("Operator pushed is" + op2.Onoperator);

            // if op = US
            if(op2.Onoperator.equals("US")){
                List<Object> precon_list = new ArrayList<>();
                precon_list.add(new OnPredicate("ON",op2.block1,op2.block2));
                precon_list.add(new Predicates("CL",op2.block1));
                precon_list.add(new Predicates("AE",'T'));
                goalStack.push(precon_list);
                goalStack.push(new OnPredicate("ON",op2.block1,op2.block2));
                goalStack.push(new Predicates("CL",op2.block1));
                goalStack.push(new Predicates("AE",'T'));
            }
            // if op = S
            else if(op2.Onoperator.equals("S")){
                List<Object> precon_list = new ArrayList<>();
                precon_list.add(new Predicates("CL",op2.block2));
                precon_list.add(new Predicates("HOLD",op2.block1));
                goalStack.push(precon_list);
                goalStack.push(new Predicates("CL",op2.block2));
                goalStack.push(new Predicates("HOLD",op2.block1));
            }
        }

        else if(obj.getClass().equals(Operators.class)){
            Operators op1 = (Operators)obj;
            System.out.println("Operator pushed is"+ op1.operator);

            // if op = PU
            if(op1.operator.equals("PU")){
                List<Object> precon_list = new ArrayList<>();
                precon_list.add(new Predicates("ONT",op1.block));
                precon_list.add(new Predicates("AE",'T'));
                precon_list.add(new Predicates("CL",op1.block));
                goalStack.push(precon_list);
                goalStack.push(new Predicates("ONT",op1.block));
                goalStack.push(new Predicates("AE",'T'));
                goalStack.push(new Predicates("CL",op1.block));
            }
            // if op = PD
            else if(op1.operator.equals("PD")){
                goalStack.push(new Predicates("HOLD",op1.block));
            }
        }
    }


    //operator add list to curr state
    private void op_add_list(Object obj){


        if(obj.getClass().equals(OnOperator.class)){
            OnOperator operator = (OnOperator)obj;
            //System.out.println(operator.name);
            System.out.println(operator.Onoperator + " is added to current state");

            if(operator.Onoperator.equals("US")){
                StateCurr.add(new Predicates("HOLD",operator.block1));
                StateCurr.add(new Predicates("CL",operator.block2));
            }
            else if(operator.Onoperator.equals("S")){
                StateCurr.add(new Predicates("AE",'T'));
                StateCurr.add(new OnPredicate("ON",operator.block1,operator.block2));
            }

        }
        else if(obj.getClass().equals(Operators.class)){
            Operators operator = (Operators)obj;
            System.out.println(operator.operator + " is added to current state");

            if(operator.operator.equals("PU")){
                StateCurr.add(new Predicates("HOLD",operator.block));
            }
            else if(operator.operator.equals("PD")){
                StateCurr.add(new Predicates("AE",'T'));
                StateCurr.add(new Predicates("ONT",operator.block));
            }
        }

    }


    //operator del list from curr state
    private void op_del_list(Object obj){

        if(obj.getClass().equals(OnOperator.class)){

            OnOperator operator = (OnOperator)obj;
            System.out.println(operator.Onoperator + " is added to current state");
            if(operator.Onoperator.equals("US")){
                Iterator<Object> it = StateCurr.iterator();

                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Predicates.class)){
                        Predicates p = (Predicates)o;
                        if(p.pred.equals("AE")){
                            it.remove();
                        }
                    }
                    else if(o.getClass().equals(OnPredicate.class)){
                        OnPredicate p = (OnPredicate)o;
                        if(p.Onpred.equals("ON") && p.block1 == operator.block1 && p.block2 ==operator.block2){
                            // Curr.remove(o);
                            it.remove();
                        }
                    }
                }
            }

            else if(operator.Onoperator.equals("S")){
                Iterator<Object> it = StateCurr.iterator();

                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Predicates.class)){
                        Predicates p = (Predicates)o;
                        if(p.pred.equals("CL") && p.block ==operator.block2){
                            //Curr.remove(o);
                            it.remove();
                        }
                        if(p.pred.equals("HOLD") && p.block ==operator.block1){
                            //Curr.remove(o);
                            it.remove();
                        }
                    }
                }
            }

        }


        else if(obj.getClass().equals(Operators.class)){

            Operators operator = (Operators)obj;
            //   System.out.println(operator.name);

            if(operator.operator.equals("PU")){
                Iterator<Object> it = StateCurr.iterator();

                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Predicates.class)){
                        Predicates p = (Predicates)o;
                        if(p.pred.equals("ONT") && p.block ==operator.block){
                            //Curr.remove(o);
                            it.remove();
                        }
                        if(p.pred.equals("AE")){
                            //Curr.remove(o);
                            it.remove();
                        }
                    }
                }
            }

            else if(operator.operator.equals("PD")){
                Iterator<Object> it = StateCurr.iterator();

                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Predicates.class)){
                        Predicates p = (Predicates)o;
                        if(p.pred.equals("HOLD") && p.block ==operator.block){
                            //Curr.remove(o);
                            it.remove();
                        }
                    }
                }
            }

        }

    }


    //operator whose application resulted in the given predicate
    private Object operator_for_pred(Object pred){
        Object opfinal = new Object();

        if(pred.getClass().equals(Predicates.class)){
            Predicates p = (Predicates)pred;

            //ae
            //hold
            //cl
            //ont
            switch (p.pred) {
                case "AE" -> {
                    char x = 0;
                    for (Object o : StateCurr) {
                        if (o.getClass().equals(Predicates.class)) {
                            Predicates p1 = (Predicates) o;
                            if (p1.pred.equals("HOLD")) {
                                x = p1.block;
                            }
                        }
                    }
                    opfinal = new Operators("PD", x);
                }
                case "HOLD" -> {
                    char x = 0;
                    Iterator<Object> it = StateCurr.iterator();
                    Iterator<Object> it2 = StateCurr.iterator();

                    //if ont and cl then pu
                    while (it.hasNext()) {
                        Object o = it.next();
                        if (o.getClass().equals(Predicates.class)) {
                            Predicates o2 = (Predicates) o;
                            if (o2.pred.equals("ONT") && o2.block == p.block) {
                                //check if CL is also true
                                while (it2.hasNext()) {
                                    Object o_ = it2.next();
                                    if (o_.getClass().equals(Predicates.class)) {
                                        Predicates o2_ = (Predicates) o_;
                                        if (o2_.pred.equals("CL") && o2_.block == p.block) {
                                            opfinal = new Operators("PU", p.block);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //if on something then US
                    for (Object o : StateCurr) {
                        if (o.getClass().equals(OnPredicate.class)) {
                            OnPredicate o2 = (OnPredicate) o;
                            if (o2.Onpred.equals("ON") && o2.block1 == p.block) {
                                x = o2.block2;
                                opfinal = new OnOperator("US", p.block, x);
                            }
                        }
                    }
                }
                case "CL" -> {
                    char x = 0;
                    //find x
                    for (Object o : StateCurr) {
                        if (o.getClass().equals(OnPredicate.class)) {
                            OnPredicate o2 = (OnPredicate) o;
                            if (o2.Onpred.equals("ON") && o2.block2 == p.block) {
                                x = o2.block1;
                            }
                        }
                    }
                    opfinal = new OnOperator("US", x, p.block);
                }
                case "ONT" -> {
                    Operators op = new Operators("PD", p.block);
                    opfinal = op;
                }
            }
        }

        else if (pred.getClass().equals(OnPredicate.class)){
            //on
            OnPredicate p = (OnPredicate)pred;
            opfinal = new OnOperator("S",p.block1,p.block2);
        }

        return opfinal;
    }


}