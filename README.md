# Packi - The Java-Objects packaging
Packi packs Java objects meaningful into JSONObjects - based on Annotations.
> Typical use cases are f.e. Java backends sending JSON objects.

## Features
### Prepare classes for packaging
Annotate Java classes with the following annotations:

---

#### ```@Package``` 
Annotation for classes. Indicates the class fields to be part of packages without the need to explicitly annotate every field.
Unnotated attributes of objects of a class annotated with ```@Package``` will be implicitly included in packages of the object.

Example:
```java
@Package
class MyClass {
  String myString = "hi"; // <- will be included in every package
  MyOtherCoolClass theOther = new MyOtherCoolClass(); // <- will be included in every package
}
```
By passing the scope list as parameter of the ```@Package``` annotation, you can define, in which package scopes the objects should be packed.

Example:
```java
@Package(scopes = {"login", "auth"}) // <- User objects will only be packaged on package operations on the "login" or "auth" scopes
class User {
  String username;
  String password;
}
```
For details and more features, see the documentation of the ```@Package``` annotation.

---
#### ```@Include```
Annotation for fields/attributes. Indicates a field being included in packages. The ```@Include``` annotation takes two optional arguments:
+ ```key: String``` Defines the key of the field in the packaged JSON object. As default, the fields/attributes name is the key in the packaged JSON.
+ ```scopes: String[]``` Defines the scopes in which packages the field is present/packed.
+ 
Example:
```java
class Example {
  int pin = 5; // <- won't be included in any package since there is no @Package class annotation or @Include annotation to this field
  
  @Include
  String motd = "Hello World"; // <- will be included in every package
  
  @Include(key = "betterKeyName", scopes = {"exampleScope"})
  byte x42y = 8; // <- will be included with the JSON key "betterKeyName" in packages of the "exampleScope" scope but in no other package
}
```
