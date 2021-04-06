import java.io.File;
import java.util.ArrayList;

public class Main 
{
    public static ArrayList<File> getVM(File file){
        File[] files = file.listFiles();
        ArrayList<File> result = new ArrayList<File>();
        for (File f:files){
            if (f.getName().endsWith(".vm")){
                result.add(f);
            }
        }
        return result;
    }

    public static void main(String[] args) 
    {
        if (args.length != 1)
        {
            System.out.println("Out Of Bound Exception");

        }
        else 
        {
            File inputFile = new File(args[0]);
            String outputPath = "";
            File outputFile;
            ArrayList<File> VM = new ArrayList<File>();
            translator translator;

            if (inputFile.isFile()) 
            {
                VM.add(inputFile);
                outputPath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(".")) + ".asm";

                VM = getVM(inputFile);

                outputPath = inputFile.getAbsolutePath() + "/" +  inputFile.getName() + ".asm";
            }

            outputFile = new File(outputPath);
            translator = new translator(outputFile);

            for (File f : VM) 
            {
                compiler compiler = new compiler(f);
                int value = -1;

                while (compiler.Commands()) {

                    compiler.extra();
                    value = compiler.commandvalue();

                    if (value == compiler.arithm) {

                        translator.setArithms(compiler.arg1());

                    } else if (value == compiler.pop1 || value == compiler.push1) {

                        translator.setRest(value, compiler.arg1(), compiler.arg2());
                    }
                }
            }
            translator.close();

        }
    }

}
