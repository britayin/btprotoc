package com.brita;

import com.brita.idl.Parser;
import com.brita.idl.generator.PBHeaderGenerator;
import com.brita.idl.node.RootNode;
import com.brita.idl.utils.IOUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static final String VERSION = "0.9";

    public static void main(String[] args) {

        System.out.println("btprotoc v_"+VERSION);
        System.out.println("================================");

        if (args.length == 0) {
            System.out.println("btprotoc inputFile [outputDir]");
            return;
        }

        String inputPath = "";
        if (args.length >= 1) {
            inputPath = args[0];
        }


        String outDir = "";
        if (args.length >= 2) {
            outDir = args[1];
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            printlnError(inputFile.getAbsoluteFile()+" File does not exist!!!");
            return;
        }

        File output = new File(outDir);



        File[] files;
        if (inputFile.isDirectory()) {
            files = inputFile.listFiles();
        }else{
            files = new File[]{inputFile};
        }

        int total = 0;
        int success = 0;

        if (files!=null) {

            for (File file : files) {

                if (!file.isFile()) {
                    continue;
                }

                if (!file.getName().endsWith(".proto")) {
                    continue;
                }

                if (!file.exists()) {
                    continue;
                }
                total++;

                if(total>1) System.out.println();
                if(inputFile.isDirectory()) System.out.println("<"+total+">");
                System.out.println("Compile File:"+file.getAbsoluteFile());


                FileReader reader = null;
                try {
                    reader = new FileReader(file);

                    Parser parser = new Parser();
                    RootNode rootNode = parser.parse(reader);
                    //System.out.println(rootNode);

                    if(new PBHeaderGenerator(file, rootNode).generateCode(output) == true){
                        success++;
                    }



                } catch (Exception e) {
                    printlnError("Compile Failure:"+inputFile.getAbsoluteFile());
                    printlnRed(e.getMessage());
                    e.printStackTrace();
                } finally {

                    IOUtil.close(reader);

                }
            }
        }

        System.out.println("================================");
        if (total > success) {
            printlnRed("total:"+total+"\tfailure:"+(total-success));
        }else{
            printlnGreen("total:"+total+"\tfailure:"+(total-success));
        }


    }

    public static void printlnError(String text) {
        printlnRed("[ERROR]"+text);
    }

    public static void printlnRed(String text) {
        System.out.println("\033[31m"+text+"\033[0m");
    }

    public static void printlnGreen(String text) {
        System.out.println("\033[32m"+text+"\033[0m");
    }

    public static void printlnYellow(String text) {
        System.out.println("\033[33m"+text+"\033[0m");
    }

    public static void printlnBlue(String text) {
        System.out.println("\033[34m"+text+"\033[0m");
    }

    public static void printlnMagenta(String text) {
        System.out.println("\033[35m"+text+"\033[0m");
    }

    public static void printlnCyan(String text) {
        System.out.println("\033[36m"+text+"\033[0m");
    }
}
