import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class translator {
	
	PrintWriter printWriter;
    int counter = 0;
    
    public translator(File fileOut) {
        try {
            printWriter = new PrintWriter(fileOut);
            counter = 0;

        } catch (FileNotFoundException exception) {

            exception.printStackTrace();

        }

    }

    public void setArithms(String command){

        if (command.equals("add")){

            printWriter.print(arithmsType1() + "M=M+D\n");

        }else if (command.equals("sub")){

            printWriter.print(arithmsType1() + "M=M-D\n");

        }else if (command.equals("and")){

            printWriter.print(arithmsType1() + "M=M&D\n");

        }else if (command.equals("or")){

            printWriter.print(arithmsType1() + "M=M|D\n");

        }else if (command.equals("gt")){

            printWriter.print(arithmsType2("JLE"));//not <=
            counter++;

        }else if (command.equals("lt")){

            printWriter.print(arithmsType2("JGE"));//not >=
            counter++;

        }else if (command.equals("eq")){

            printWriter.print(arithmsType2("JNE"));//not <>
            counter++;

        }else if (command.equals("not")){

            printWriter.print("@SP\nA=M-1\nM=!M\n");

        }else if (command.equals("neg")){

            printWriter.print("D=0\n@SP\nA=M-1\nM=D-M\n");

        }else {

            throw new IllegalArgumentException("non-arithmetic command");

        }

    }

    public void setRest(int command, String segment, int index){

        if (command == compiler.push1){

            if (segment.equals("constant")){

                printWriter.print("@" + index + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");

            }else if (segment.equals("local")){

                printWriter.print(arithmsRest1("LCL",index,false));

            }else if (segment.equals("argument")){

                printWriter.print(arithmsRest1("ARG",index,false));

            }else if (segment.equals("this")){

                printWriter.print(arithmsRest1("THIS",index,false));

            }else if (segment.equals("that")){

                printWriter.print(arithmsRest1("THAT",index,false));

            }else if (segment.equals("temp")){

                printWriter.print(arithmsRest1("R5", index + 5,false));

            }else if (segment.equals("pointer") && index == 0){

                printWriter.print(arithmsRest1("THIS",index,true));

            }else if (segment.equals("pointer") && index == 1){

                printWriter.print(arithmsRest1("THAT",index,true));

            }else if (segment.equals("static")){

                printWriter.print(arithmsRest1(String.valueOf(16 + index),index,true));

            }

        }else if(command == compiler.pop1){

            if (segment.equals("local")){

                printWriter.print(arithmsRest2("LCL",index,false));

            }else if (segment.equals("argument")){

                printWriter.print(arithmsRest2("ARG",index,false));

            }else if (segment.equals("this")){

                printWriter.print(arithmsRest2("THIS",index,false));

            }else if (segment.equals("that")){

                printWriter.print(arithmsRest2("THAT",index,false));

            }else if (segment.equals("temp")){

                printWriter.print(arithmsRest2("R5", index + 5,false));

            }else if (segment.equals("pointer") && index == 0){

                printWriter.print(arithmsRest2("THIS",index,true));

            }else if (segment.equals("pointer") && index == 1){

                printWriter.print(arithmsRest2("THAT",index,true));

            }else if (segment.equals("static")){

                printWriter.print(arithmsRest2(String.valueOf(16 + index),index,true));

            }

        }else {

            throw new IllegalArgumentException("different type of command");

        }

    }

    public void close(){

        printWriter.close();

    }

    private String arithmsType1(){

        return "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n";

    }

    private String arithmsType2(String type){

        return "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "D=M-D\n" +
                "@FALSE" + counter + "\n" +
                "D;" + type + "\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "@CONTINUE" + counter + "\n" +
                "0;JMP\n" +
                "(FALSE" + counter + ")\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "(CONTINUE" + counter + ")\n";

    }


    private String arithmsRest1(String segment, int index, boolean isDirect){

        String noPointerCode = (isDirect)? "" : "@" + index + "\n" + "A=D+A\nD=M\n";

        return "@" + segment + "\n" +
                "D=M\n"+
                noPointerCode +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n";

    }

    private String arithmsRest2(String segment, int index, boolean isDirect){

        String noPointerCode = (isDirect)? "D=A\n" : "D=M\n@" + index + "\nD=D+A\n";

        return "@" + segment + "\n" +
                noPointerCode +
                "@R13\n" +
                "M=D\n" +
                "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "@R13\n" +
                "A=M\n" +
                "M=D\n";

    }

}
