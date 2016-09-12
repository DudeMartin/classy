# classy
A small, Java bytecode engineering library.

## Features
* Small. Around 30 classes, most of which are less than 50 lines.
* Simple. Excessive abstraction is avoided to keep the implementation as straightforward as possible.
* Easy to use. All the details are handled in the background.
* Object-based representation. The individual components of class files are represented by objects. 

## Usage
To read a `.class` file, you first need to read its contents into a `byte` array. Then, simply construct a `ClassFile` object using that array.

```
byte[] classBytes = ...
ClassFile classFile = new ClassFile(classBytes);
```

Once the file is read, its contents can be accessed using the `public` fields defined in the `ClassFile` class. In fact, all classes that represent some aspect of a class file contain these `public` fields.

### Example

##### *Hello.java*
```
public class Hello {

    private static int invokeMe(int number) {
        return number * 3 / 2;
    }

    public static void main(String[] args) {
        System.out.print("Hello, world!");
        if (args.length > 0) {
            System.out.print(" Your number is: " + invokeMe(Integer.parseInt(args[0])));
        }
        System.out.println();
    }
}
```

##### *Test.java (imports omitted)*
```
public class Test {

    public static void main(String[] args) throws Exception {
        File helloFile = new File("Hello.class");
        FileInputStream in = new FileInputStream(helloFile);
        int length = (int) helloFile.length();
        byte[] bytes = new byte[length];
        for (int offset = 0; offset < length; offset += in.read(bytes, offset, length - offset));
        ClassFile classFile = new ClassFile(bytes);
        System.out.println(classFile.name);
        System.out.println(classFile.superclassName);
        for (MethodMember method : classFile.methods) {
            if (method.name.equals("main")) {
                System.out.println(method.descriptor);
            }
        }
        in.close();
    }
}
```

Yields the output:
```
Hello
java/lang/Object
([Ljava/lang/String;)V
```

## Limitations
* Currently, **classy** only supports reading class files, not writing them. This means that while you can read any (valid) class file up to, and including Java 8, generating your own is (currently) unsupported. (The support for this is *coming eventually...*) From a design aspect, **classy** is designed with class modification (and generation) in mind. Where applicable, the `public` fields are mutable, which means that changing a specific detail about a class file is as simple as assigning a different value to a field.
* The source code is currently undocumented. This is a high-priority task right now.