import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class Class_Maker{
    String class_In_Lines(String input_Line){
        return "public "+input_Line+" {"; //the last parantheses  wait to be put in the for-loop. 
    }
}
class Variable_Maker{
    String variable_In_Lines(String input_Line){
        int semi_Positon=input_Line.indexOf(":");
        if (input_Line.charAt(semi_Positon+2)=='+'){
            return "    public "+input_Line.substring(semi_Positon+2+1,input_Line.length())+";";// +2==(-or+)
        }
        else{
            return "    private "+input_Line.substring(semi_Positon+2+1,input_Line.length())+";";// +2==(-or+)
        }
    }
}
class Method_Maker{
    String method_In_Lines(String input_Line){
        return "here is a method"; //fix later
    }
}

public class CodeGenerator{
    public static void main(String[] args) {
		    //// read files
        if (args.length == 0) {
            System.err.println("you need to input file name");
            return;
        }
        String fileName = args[0];
        System.out.println("File name: " + fileName);
        String mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
        }
        catch (IOException e) {
            System.err.println("cannot read files " + fileName);
            e.printStackTrace();
            return;
        }
        
        
        try {
            ////files operation
            Class_Maker class_maker=new Class_Maker();
            Variable_Maker variable_maker=new Variable_Maker();
            Method_Maker method_maker= new Method_Maker();
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            ArrayList<String> string_List=new ArrayList<String>(); 

            String read_Line;
            while((read_Line=reader.readLine())!=null){
                // bw.write(read_Line);
                // bw.write("\n");
                string_List.add(read_Line);
                System.out.println(read_Line);
                
            }
            
            ////naming the file
            String output = string_List.get(1).substring(6,string_List.get(1).length())+".java";
            File file = new File(output);
            if (!file.exists()) {
                file.createNewFile();
            }
            //String content = mermaidCode;
            
            ////writing in the content
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));   //newing object cannot be in the loop!!

//===
            string_List.remove("classDiagram");

            for(int i=0;i<string_List.size();i++){
                if (string_List.get(i).substring(0, 5).equals("class")){
                    String class_line=class_maker.class_In_Lines(string_List.get(i));
                    bw.write(class_line);
                    bw.write("\n");
                    System.out.println("there is a class");
                }
                else if(string_List.get(i).contains("(")){ //find method first and then variables,buz they are almost same
                    String method_line=method_maker.method_In_Lines(string_List.get(i));
                    bw.write(method_line);
                    bw.write("\n");
                    System.out.println("there is a method");
                }
                else{//except for class and methods,(e.g.variable,array),deal with the same way.
                    String variable_line=variable_maker.variable_In_Lines(string_List.get(i));
                    bw.write(variable_line);
                    bw.write("\n");
                    System.out.println("there is a variable");
                }
            }
            bw.write("}");

            reader.close();
            bw.close();
//===
            System.out.println("Java class has been generated: " + output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}