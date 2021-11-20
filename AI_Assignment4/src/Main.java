import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Goal Stack Planner");
        ScrollPane scrollpane = new ScrollPane();
        Scene scene = new Scene(scrollpane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        VBox instructions = new VBox();

        Text firstline = new Text("Please use the following commands to input initial state and goal state:");
        firstline.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        Text label1 = new Text("ON - for stacking two blocks x and y");
        Text label2 = new Text("CL - for clear");
        Text label3 = new Text("ONT - for on table");
        Text label4 = new Text("HOLD - for hold");
        Text label5 = new Text("AE - for arm empty");

        List<Object> Startstate = new ArrayList<>();
        List<Object> Goalstate = new ArrayList<>();

        instructions.getChildren().addAll(firstline,label1,label2,label3,label4,label5);
        instructions.setSpacing(10);
        instructions.setPadding(new Insets(10));
        scrollpane.setContent(instructions);
        scrollpane.setPannable(true);

        VBox Sbox = new VBox();

        Button start = new Button("Start");
        Sbox.getChildren().addAll(start);
        instructions.getChildren().addAll(Sbox);

        start.setOnMouseClicked(e-> {

            VBox initialinputpane = new VBox();
            Text initialinput = new Text("Please enter initial state:");
            Label initialinputinstruction = new Label("Please input all the commands separated by a comma and arguments in brackets:");
            TextField initialinputtextfield = new TextField();
            Button initialinputbutton = new Button("Submit");
            initialinputpane.getChildren().addAll(initialinput,initialinputinstruction,initialinputtextfield,initialinputbutton);
            instructions.getChildren().addAll(initialinputpane);

            initialinputbutton.setOnMouseClicked(initialinputevent ->{

                String str = initialinputtextfield.getText();
                List<String> predicatesarray = Arrays.asList(str.split("\\),"));
                for (String s : predicatesarray) {
                    String[] splitString = (s.split("\\("));
                    String predicate = splitString[0];
                    switch (predicate) {
                        case "ON": {
                            //ON
                            String[] splitStringON = (s.split("\\("));
                            String[] splitStringonab = splitStringON[1].split(",");

                            char arg1 = splitStringonab[0].charAt(0);
                            char arg2 = splitStringonab[1].charAt(0);

                            OnPredicate ON = new OnPredicate("ON", arg1, arg2);
                            Startstate.add(ON);

                            break;
                        }
                        case "CL": {
                            //CL
                            String[] splitStringCL = (s.split("\\("));
                            char arg1 = splitStringCL[1].charAt(0);
                            Predicates CL = new Predicates("CL", arg1);
                            Startstate.add(CL);
                            break;
                        }
                        case "ONT": {
                            //ONT
                            String[] splitStringONT = (s.split("\\("));
                            char arg1 = splitStringONT[1].charAt(0);
                            Predicates ONT = new Predicates("ONT", arg1);
                            Startstate.add(ONT);
                            break;
                        }
                        case "HOLD": {
                            //HOLD
                            String[] splitStringHOLD = (s.split("\\("));
                            char arg1 = splitStringHOLD[1].charAt(0);
                            Predicates HOLD = new Predicates("HOLD", arg1);
                            Startstate.add(HOLD);
                            break;
                        }
                        case "AE":
                            //AE
                            Predicates AE = new Predicates("AE", 'T');
                            Startstate.add(AE);
                            break;
                        default:
                            break;
                    }


                }

                VBox goalstatevbox = new VBox();
                Text goalinput = new Text("Please enter goal state:");
                Label goalinputinstruction = new Label("Please input all the commands separated by a comma and arguments in brackets:");                TextField goalinputtextfield = new TextField();
                Button goalinputbutton = new Button("Submit");
                goalstatevbox.getChildren().addAll(goalinput,goalinputinstruction,goalinputtextfield,goalinputbutton);
                instructions.getChildren().addAll(goalstatevbox);

                goalinputbutton.setOnMouseClicked(goalinputevent ->{
                    String goalstr = goalinputtextfield.getText();
                    List<String> goalpredicatesarray = Arrays.asList(goalstr.split("\\),"));
                    for (String s : goalpredicatesarray) {
                        String[] splitString = (s.split("\\("));
                        //ON(B,A),ONT(A),ONT(C),ONT(D),CL(B),CL(C),CL(D),AE
                        //ON(C,A),ON(B,D),ONT(A),ONT(D),CL(B),CL(C),AE
                        String predicate = splitString[0];
                        switch (predicate) {
                            case "ON": {
                                //ON
                                String[] splitStringgON = (s.split("\\("));
                                String[] splitStringgonab = splitStringgON[1].split(",");

                                char arg1 = splitStringgonab[0].charAt(0);
                                char arg2 = splitStringgonab[1].charAt(0);

                                OnPredicate ON = new OnPredicate("ON", arg1, arg2);
                                Goalstate.add(ON);

                                break;
                            }
                            case "CL": {
                                //CL
                                String[] splitStringgCL = (s.split("\\("));
                                char arg1 = splitStringgCL[1].charAt(0);
                                Predicates CL = new Predicates("CL", arg1);
                                Goalstate.add(CL);
                                break;
                            }
                            case "ONT": {
                                //ONT
                                String[] splitStringgONT = (s.split("\\("));
                                char arg1 = splitStringgONT[1].charAt(0);
                                Predicates ONT = new Predicates("ONT", arg1);
                                Goalstate.add(ONT);
                                break;
                            }
                            case "HOLD": {
                                //HOLD
                                String[] splitStringgHOLD = (s.split("\\("));
                                char arg1 = splitStringgHOLD[1].charAt(0);
                                Predicates HOLD = new Predicates("HOLD", arg1);
                                Goalstate.add(HOLD);
                                break;
                            }
                            case "AE":
                                //AE
                                Predicates AE = new Predicates("AE", 'T');
                                Goalstate.add(AE);
                                break;
                            default:
                                break;
                        }
                    }
                    Processing Process = new Processing(Startstate, Goalstate);
                    Process.function_process();
                    List<Object> plan = Process.plan;


                    VBox output = new VBox();

                    for (Object pred : plan) {
                        if (pred.getClass().equals(Operators.class)) {
                            Operators o1 = (Operators) pred;
                            Text x = new Text(o1.operator + "(" + o1.block + ")");
                            output.getChildren().add(x);

                        } else if (pred.getClass().equals(OnOperator.class)) {
                            OnOperator o2 = (OnOperator) pred;
                            Text y = new Text(o2.Onoperator + "(" + o2.block1 + "," + o2.block2 + ")");
                            output.getChildren().add(y);

                        }
                    }

                    instructions.getChildren().add(output);
                });
            });
        });
    }
}


