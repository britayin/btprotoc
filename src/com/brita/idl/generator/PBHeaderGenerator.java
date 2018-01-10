package com.brita.idl.generator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.brita.Main;
import com.brita.idl.define.Define;
import com.brita.idl.generator.format.Formater;
import com.brita.idl.node.*;
import com.brita.idl.token.Annotation;
import com.brita.idl.utils.FileUtil;
import com.brita.idl.utils.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * Created by britayin on 2017/9/3.
 */
public class PBHeaderGenerator extends CodeGenerator{

    private static final String BASE_PATH = PATH_TEMPLATE + File.separator + "pbheader" + File.separator;

    private Map<EnumNode, String> enumCodes = new HashMap<>();
    private Map<MessageNode, String> messageCodes = new HashMap<>();

    public PBHeaderGenerator(File inputFile, RootNode rootNode) {
        super(inputFile, rootNode);
    }

    private Config config;

    @Override
    protected boolean generate(File outputDir) {

        //System.out.println("PBHeaderGenerator outputDir: "+ outputDir.getAbsolutePath());

        String configStr = FileUtil.readFile(new File(BASE_PATH, "config.json"));
        config = JsonUtil.gson.fromJson(configStr, Config.class);

        outputDir = new File(outputDir.getAbsolutePath());

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        Set<Node> nodes = getNodes();
        if (nodes == null) {
            return true;
        }

        for (Node node : nodes) {
            if (node instanceof MessageNode) {
                MessageNode modelNode = (MessageNode)node;
                generateModel(modelNode, outputDir);
            }else if (node instanceof EnumNode) {
                EnumNode modelNode = (EnumNode)node;
                generateEnum(modelNode, outputDir);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (EnumNode node : enumCodes.keySet()) {
            String code = enumCodes.get(node);
            if (code!=null){
                stringBuilder.append(code);
                stringBuilder.append("\r\n");
            }
        }

        for (MessageNode node : messageCodes.keySet()) {
            String code = messageCodes.get(node);
            if (code!=null){
                stringBuilder.append(code);
                stringBuilder.append("\r\n");
            }
        }

        String tmplate = FileUtil.readFile(getTemplateFile("Header.t"));

        Formater formater = new Formater(tmplate);
        addCommonProperty(formater);
        formater.addProperty("$Imports$", generateImports(rootNode));
        formater.addProperty("$FileContent$", stringBuilder.toString());

        String code = formater.format();

        File outputFile = new File(outputDir.getAbsolutePath(), getOutputFileName()+config.outputFileSuffix);
        if (outputFile.exists() && outputFile.isFile()) {
            outputFile.delete();
        }
        FileUtil.writeFile(code, outputFile);
        Main.printlnGreen("Output File: "+ outputFile.getAbsolutePath());


        return true;

    }

    private void addCommonProperty(Formater formater) {
        formater.addProperty("$Date$", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        formater.addProperty("$SourceFileName$", inputFile.getName());
        formater.addProperty("$FileName$", getOutputFileName());
        formater.addProperty("$Version$", Main.VERSION);
    }

    private String getOutputFileName(){
        String inputFileName = inputFile.getName();
        int pos = inputFileName.lastIndexOf('.');
        if (pos > 0) {
            String outputName = inputFileName.substring(0, pos);
            return outputName;
        }
        return null;
    }

    private String generateModel(MessageNode messageNode, File outputDir) {
        String modelName = messageNode.getName();
        for (String basicType : Define.BASE_TYPE) {
            if (modelName.equals(basicType)) {
                return null;
            }
        }

        if (messageNode.getMembers()==null){
            return null;
        }

        if (messageCodes.containsKey(messageNode)) {
            return null;
        }

        String tmplate = FileUtil.readFile(getTemplateFile("Message.t"));

        Formater formater = new Formater(tmplate);
        addCommonProperty(formater);
        formater.addProperty("$PackageName$", generatePackageName(messageNode));
        formater.addProperty("$MessageName$", messageNode.getName());
        formater.addProperty("$MessageItems$", generateMessageItems(messageNode));

        String code = formater.format();

        //FileUtil.writeFile(code, new File(outputDir, modelNode.getName()+".java"));
        messageCodes.put(messageNode, code);

        if (messageNode.models!=null) {
            for (MessageNode node : messageNode.models) {
                generateModel(node, outputDir);
            }
        }

        if (messageNode.enums!=null) {
            for (EnumNode node : messageNode.enums) {
                generateEnum(node, outputDir);
            }
        }

        return code;
    }

    private String generateEnum(EnumNode enumNode, File outputDir) {

        if (enumNode.getItems()==null){
            return null;
        }

        if (enumCodes.containsKey(enumNode)) {
            return null;
        }

        String tmplate = FileUtil.readFile(getTemplateFile("Enum.t"));

        Formater formater = new Formater(tmplate);
        addCommonProperty(formater);
        formater.addProperty("$PackageName$", generateRootPackagePath());
        formater.addProperty("$EnumName$", enumNode.getName());
        formater.addProperty("$EnumItems$", generateEnumItems(enumNode));

        String code = formater.format();

        //FileUtil.writeFile(code, new File(outputDir, modelNode.getName()+".java"));
        enumCodes.put(enumNode, code);

        return code;
    }

    private String generateEnumItems(EnumNode messageNode) {
        if(messageNode.getItems()==null) {
            return null;
        }

        StringBuilder code = new StringBuilder();

        for (EnumItemNode memberNode : messageNode.getItems()) {
            code.append("    "+generateEnumMember(messageNode ,memberNode)+"\r\n");
        }

        return code.toString();
    }

    private String generateEnumMember(EnumNode messageNode ,EnumItemNode memberNode) {

        String tmplate = FileUtil.readFile(getTemplateFile("EnumItem.t"));
        tmplate = tmplate.trim();

        Formater formater = new Formater(tmplate);
        addCommonProperty(formater);
        formater.addProperty("$MessageType$", messageNode.getName());
        formater.addProperty("$EnumItemName$", memberNode.getName());
        formater.addProperty("$EnumItemValue$", ""+memberNode.value);

        String annotation = getAnnotationInLine(memberNode.line);
        formater.addProperty("$Annotation$", annotation==null?"":(annotation));

        return formater.format();
    }

    private String generateMessageItems(MessageNode messageNode) {
        if(messageNode.getMembers()==null) {
            return null;
        }

        StringBuilder code = new StringBuilder();

        for (MemberNode memberNode : messageNode.getMembers()) {
            code.append("    "+generateMember(messageNode ,memberNode)+"\r\n");
        }

        return code.toString();
    }

    private String generateMember(MessageNode messageNode ,MemberNode memberNode) {

        String tmplate = FileUtil.readFile(getTemplateFile("MessageItem.t"));
        tmplate = tmplate.trim();

        Formater formater = new Formater(tmplate);
        addCommonProperty(formater);
        formater.addProperty("$PackageName$", generatePackageName(messageNode));
        formater.addProperty("$MessageType$", messageNode.getName());
        formater.addProperty("$MemberType$", memberNode.getModelType().getName());
        formater.addProperty("$MessageItemName$", memberNode.getName());
        formater.addProperty("$MessageItemIndex$", ""+memberNode.index);
        formater.addProperty("$MessageItemModifier$", memberNode.getModifier());

        String annotation = getAnnotationInLine(memberNode.line);
        formater.addProperty("$Annotation$", annotation==null?"":(annotation));

        return formater.format();
    }

    private String generatePackageName(MessageNode messageNode) {

        StringBuilder code = new StringBuilder();

        StringBuilder parentPath = new StringBuilder();

        MessageNode currentNode = messageNode;
        while (currentNode.parentNode!=null) {
            currentNode = currentNode.parentNode;
            parentPath.append(currentNode.getName());
        }

        if (currentNode.packagePaths!=null) {
            if (currentNode.packagePaths!=null) {
                for (String path : currentNode.packagePaths) {
                    code.append(generatePackagePath(path));
                }
            }

        }

        if (!parentPath.toString().isEmpty()) {
            code.append(parentPath.toString());
            code.append("_");
        }

        return code.toString();
    }

    private String generateRootPackagePath() {

        StringBuilder code = new StringBuilder();

        for (String path : rootNode.packageNode.getPaths()) {
            code.append(path);
            code.append("_");
        }

        return code.toString();
    }

    private String generatePackagePath(String path) {

        String tmplate = FileUtil.readFile(getTemplateFile("PackagePath.t"));
        tmplate = tmplate.trim();

        Formater formater = new Formater(tmplate);
        addCommonProperty(formater);
        formater.addProperty("$PackagePath$", path);

        return formater.format().trim();
    }

    private String getAnnotationInLine(int line) {
        for (Annotation annotation : rootNode.annotations ) {
            if (annotation.getLine() == line+1) {
                return annotation.getText();
            }
        }
        return null;
    }

    private String generateImports(RootNode rootNode) {

        if (rootNode==null || rootNode.importNodes == null){
            return null;
        }

        StringBuilder code = new StringBuilder();

        for (ImportNode importNode : rootNode.importNodes) {
            code.append(generateImportItem(importNode));
        }

        return code.toString();
    }

    private String generateImportItem(ImportNode importNode) {
        if(importNode==null) {
            return null;
        }

        String tmplate = FileUtil.readFile(getTemplateFile("ImportItem.t"));

        Formater formater = new Formater(tmplate);




        String importPath = importNode.getName();
        int pos = importPath.lastIndexOf('.');
        if (pos > 0) {
            importPath = importPath.substring(0, pos);
        }

        addCommonProperty(formater);
        formater.addProperty("$ImportPath$",importPath+config.outputFileSuffix);

        String code = formater.format();

        return code.toString();
    }

    private File getTemplateFile(String templateFileName) {

        File tFile = new File(BASE_PATH, templateFileName);

        //查找是否有匹配的自定义模板,以最后一个为准
        if (config != null && config.customTemplates != null) {
            for (CustomTemplate templateConfig : config.customTemplates) {
                if (templateConfig == null || templateConfig.o_template==null || templateConfig.c_template==null || templateConfig.files==null) {
                    continue;
                }
                if (!templateFileName.equals(templateConfig.o_template)) {
                    continue;
                }

                for (String fileName : templateConfig.files) {
                    if (fileName == null) {
                        continue;
                    }

                    if (!inputFile.getName().endsWith(fileName)) {
                        continue;
                    }

                    File cFile = new File(BASE_PATH, templateConfig.c_template);
                    if (cFile.isDirectory() || !cFile.exists()) {
                        continue;
                    }

                    tFile = cFile;
                }
            }
        }

        //没有自定义模板就使用通用模板
        return tFile;
    }

}
