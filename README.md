# btprotoc

**btprotoc** is a compiler for proto files. The proto file is compiled into a `.h` file consisting of simple enumerated types.
It was used in the process of code simplification (PBCodec) and de-c (OCS). It has been used in mobile QQ.

If you just need to compile the proto file into the `.h` file, you can use the compiled `btprotoc.jar` directly.

Compile a file or all the `.proto` files under a folder.
```
java -jar btprotoc.jar    file(folder)    [output dir]
```

Tips：
1. You need a Java environment. Installation : http://www.oracle.com/technetwork/java/javase/downloads/index.html
2. Ensure that the btprotoc.jar file exists.
3. Ensure that the `config.json` and template files in the `template/pbheader` are present.

##### V_0.1
1. Fixing the problems that not having corresponds comments;
2. Support for the `default` property;

##### V_0.2
1. Fix the problem of submessage compilation failed;
2. Support RPC compilations;
3. Supports negative Numbers and 16 decimal number compilations;
4. Supports compiling folders;

##### V_0.3
1. Modify the suffix for `pb.h`;

##### V_0.4
1. Add version attributes;

##### V_0.6 
1. Increase the `template configuration file/pbheader/config.json`;
2. Configuration files support the use of custom templates for some files;
3. Some naming conflicts can be solved by custom templates (see config.json); 
4. Support import file;
5. The console output statistics and coloration;
6. Add a modifier description to the comment;
