# Packi - The Java-Objects packaging
Packi packs Java objects meaningful into JSONObjects - based on Annotations.
> Typical use cases are f.e. Java backends sending JSON objects.

## Usage
1. Prepare classes for packaging
2. Pack using Packi.pack(...);
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
#### ```@Exclude```
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
