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
        input_Line=input_Line.trim();
        System.out.println("class maker input line"+input_Line);

        return "public "+input_Line+" {"; //the last parantheses  wait to be put in the for-loop. 
    }
}
class Variable_Maker{
    String variable_In_Lines(String input_Line){
        int plus_Position=input_Line.indexOf("+");
        int minus_Position=input_Line.indexOf("-");
        if (input_Line.contains("+")){
            return "    public "+input_Line.substring(plus_Position+1,input_Line.length()).trim()+";";// +2==(-or+)
        }
        else{
            return "    private "+input_Line.substring(minus_Position+1,input_Line.length()).trim()+";";// +2==(-or+)
        }
    }
}
class Method_Maker{
    String method_In_Lines(String input_Line){
        int right_parantheses_positon=input_Line.indexOf(')');
        int plus_Position=input_Line.indexOf("+");
        int minus_Position=input_Line.indexOf("-");
        if (input_Line.contains("+")){ //change to contains??
            return "    public "+input_Line.substring(right_parantheses_positon+1,input_Line.length()).replace(" ","")+" "+input_Line.substring(plus_Position+1,right_parantheses_positon+1)+" {"; //fix later
        }
        else{
            return "    private "+input_Line.substring(right_parantheses_positon+1,input_Line.length()).replace(" ","")+" "+input_Line.substring(minus_Position+1,right_parantheses_positon+1)+" {";
        }
        
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
            ArrayList<String> write_section=new ArrayList<String>();
            // for(int gg=2;gg<lines.length;gg++){
            //     String[] read_Line;
            //     read_Line=lines[gg].split("\n");
            //     for(int ff=0;ff<read_Line.length;ff++){
            //         string_List.add(read_Line[ff]);
            //     }
            // }

           String read_Line;
            while((read_Line=reader.readLine())!=null){
                // bw.write(read_Line);
                // bw.write("\n");
                string_List.add(read_Line);
                System.out.println(read_Line);
                
            }
            
            //create the file
            

            //String content = mermaidCode;
            
            

//===
            string_List.remove("classDiagram");

            int class_num=0;
            int class_count=0;
            ArrayList<Integer> class_row=new ArrayList<Integer>();
            for(int s=0;s<string_List.size();s++){
                if (string_List.get(s).contains("class")){ //the number of class
                    class_num++;
                    class_row.add(s);
                }
            }
            for(int init=0;init<class_num;init++){
                for(int i=class_row.get(init);i<string_List.size();i++){
                    if(string_List.get(i).contains("class") && class_count>0){
                        class_count=0;
                        break;
                    }
                    if (string_List.get(i).contains("class")){
                        class_count++;
                        String class_line=class_maker.class_In_Lines(string_List.get(i));
                        write_section.add(class_line);
                        write_section.add("\n");
                        System.out.println("there is a class");
                    }
                    else if(string_List.get(i).contains("(")){ //find method first and then variables,buz they are almost same
                        String method_line=method_maker.method_In_Lines(string_List.get(i));
                        write_section.add(method_line);
                        //write_section.add("\n");
                        if (string_List.get(i).contains("get")){
                            int position_Of_Get=string_List.get(i).indexOf("get");
                            String get_value=string_List.get(i).substring(position_Of_Get+3,string_List.get(i).indexOf("("));
                            write_section.add("\n");
                            write_section.add("        return "+get_value.toLowerCase()+";");
                            write_section.add("\n");
                            write_section.add("    }");//finish the left part of method(the return value )
                            write_section.add("\n"); 
                        }
                        else if(string_List.get(i).contains("set")){
                            int position_Of_set=string_List.get(i).indexOf("set");
                            String set_value=string_List.get(i).substring(position_Of_set+3,string_List.get(i).indexOf("("));
                            write_section.add("\n");
                            write_section.add("        this."+set_value.toLowerCase()+" = "+set_value.toLowerCase()+";");
                            write_section.add("\n");
                            write_section.add("    }");//finish the left part of method(the set value )
                            write_section.add("\n"); 
    
                        }
                        else if(string_List.get(i).contains("boolean")){
                            write_section.add("return false;}");
                            write_section.add("\n");
                            //write_section.set(write_section.size()-1,write_section.get(write_section.size()-1)+("return flase;}"));
                        }
                        else if(string_List.get(i).contains("void")){
                            write_section.add(";}");
                            write_section.add("\n");
                        }
                        
                        else if(string_List.get(i).contains("int")){
                            write_section.add("return 0;}");
                            write_section.add("\n");

                            //write_section.set(write_section.size()-1,write_section.get(write_section.size()-1)+("return 0;}"));
                        }
                        
                        System.out.println("there is a method");
                    }
                    else if(string_List.get(i).replace(" ","").equals("")){
                        continue;
                    }
                    else{//except for class and methods,(e.g.variable,array),deal with the same way.
                        String variable_line=variable_maker.variable_In_Lines(string_List.get(i));
                        write_section.add(variable_line);
                        write_section.add("\n");
                        System.out.println("there is a variable");
                    }
                }
                write_section.add("}");
                
                //create file
                String output = string_List.get(class_row.get(init)).substring(string_List.get(class_row.get(init)).indexOf("class")+5+1,string_List.get(class_row.get(init)).length())+".java";//find the position of class and then add the length of "class" and one <space> 
                File file = new File(output);
                if (!file.exists()) {
                    file.createNewFile();                    
                    }
                //write in
                ////writing in the content
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));   //newing object cannot be in the loop!!
                for(int w=0;w<write_section.size();w++){
                    bw.write(write_section.get(w));
                    System.out.println(w+write_section.get(w));
                }
                bw.flush();
                bw.close();
                write_section.clear();
            }
            
            reader.close();
            //bw.close();
//===
            //System.out.println("Java class has been generated: " + output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}