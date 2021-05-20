# Packman:baggage_claim:- The Java-Object packaging:closed_book:
Packman packs Java objects meaningful into JSONObjects - based on Annotations.
> Typical use cases are f.e. Java backends sending JSON objects.
A first, little example:
```java
@Package
class Example {
  @Include
  String myStr = "hey!";
  
  @Include(key = "cool")
  private AnotherCoolClass acc;
  
  @Exclude
  int secretPin = 0;
}

class AnotherCoolClass {
  @Include
  byte theCoolByte = 14;
}
```
will result in the JSON Object:
```
{
  myStr: "hey!",
  cool: {
    theCoolByte: 14
  }
}
```
Let's get into this.
## Maven repository/Installation
```xml
<dependency>
  <groupId>io.github.yniklas</groupId>
  <artifactId>Packman</artifactId>
  <version>1.0.4</version>
</dependency>
```
+ v1.0.3 for Java 16 implementation
+ v1.0.4 for Java 11 implementation (recommended for existing projects)

## Usage
1. Prepare classes for packaging
2. Pack using `Packman.pack(...)`;

### Principle
Use scopes to separate objects and adjust the fields/objects implemented in the JSON packages.
For example, use a new scope for every request type like: "login", "getDashboardInfo". With scopes, you let Packman create your Packages individually based on the scopes passed as 
parameter of a annotation. For further explanations and examples, see the example classes in this GitHub repository.
Since v1.0.2, multiple scopes are supported, coming with AND and OR policies.

---

### Prepare classes for packaging
Annotate Java classes with the following annotations:

### ```@Package``` 
Annotation for classes. Indicates the class fields to be part of packages without the need to explicitly annotate every field.
Unnotated attributes of objects of a class annotated with ```@Package``` will be implicitly included in packages of the object.

Example:
```java
@Package
class MyClass {
  String myString = "hi"; // <- will be included in every package
  private MyOtherCoolClass theOther = new MyOtherCoolClass(); // <- will be included in every package
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
### ```@Include```
Annotation for fields/attributes. Indicates a field being included in packages. The ```@Include``` annotation takes two optional arguments:
+ ```key: String``` Defines the key of the field in the packaged JSON object. As default, the fields/attributes name is the key in the packaged JSON.
+ ```scopes: String[]``` Defines the scopes in which packages the field is present/packed.

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

---
### ```@Exclude```
Annotation for fields/attributes. Indicates a field being exluded from packages. Per default - without passing the scopes list as parameter - the field will be excluded from all packages.
+ ```scopes: String[]``` Defines the scopes where the field is excluded. Default is {} which means the field is exluded from all packages of every scope

Example:
```java
@Package
class User {
  String displayName = "SuperCool"; // <- included due to the @Package annotation of the class
  
  @Exclude(scopes = {"displayOnlyNames"})
  String email = "my@mail.com"; // <- included in every package but not in packages of the scope "displayOnlyNames"
  
  @Exlude
  String password = "1234"; // <- excluded from all packages
}
```

---
### ```@IncludeOnly```
Annotation for fields/attributes. Indicates a field being included only in the - as parameter - given scopes. ```@Package``` annotations of the coressponding class won't include the attribute into packages from other scopes than passed for the ```@IncludeOnly``` annotation.
+ ```key: String``` Defines the key of the field in the packaged JSON object. As default, the fields/attributes name is the key in the packaged JSON.
+ ```scopes: String[]``` (Required) Defines all scopes where the field/attribute should be included. {} value will have an Excluding effect.

Example:
```java
@Package
class User {
  String username = "defaultUser"; // <- included due to the @Package annotation of the class
  
  @IncludeOnly(scopes = {"secureAuth"})
  String password = "superSecure"; // <- only included in the package of the "secureAuth" scope, in no other package
}
```

---
### Pack with Packman
After annotating the classes and fields, objects can be packed by typing the following command (**single scope**):
```java
Packman.pack("myScope", theObjectIWantToPack);
```
Pack parameters:
+ ```scope: String``` The scope you want to package. "" is default value and explicitly allowed since classes and fields can be annotated so.
+ ```packObject: Object``` A object to pack.
+ ```moreObjects...``` More objects to be packed all in the same package (the packObject will be also part of the result JSON object). The additional objects are separated by ",".

Pack with multiple scopes:
```java
Packman.pack(new String[]{"productInfo", "userData"}, ScopePolicy.OR, theObjectIWantToPack);
```
Pack parameters:
+ ```scopes: String[]``` All scopes you want Packman to consider while packaging.
+ ```policy: ScopePolicy``` The Policy you want to apply to the given scopes. If you choose `ScopePolicy.OR`, a object/field must only be included in one of the given scopes. If you choose `ScopePolicy.AND`, a object/field must be in all of the given scopes to be present in the result JSON Object. Fields/Object being included for every scope (e.g. throw a simple @Include) won't be affected and furthermore present.
+ ```packObject: Object``` A object to pack.
+ ```moreObjects...``` More objects to be packed all in the same package (the packObject will be also part of the result JSON object). The additional objects are separated by ",".

---
## Important additional information:
+ There is currently no protection for StackOverflowErrors in case of infinite recursions caused by object references to the same object to which being packed.
+ To multiple equal JSON keys will be digits added to identify and include every value

The GitHub repository contains example classes and a Test class to understand the Packaging System. They won't be included in the .jar library.

## Coming soon
+ Implement options to use different keys for different scopes
