import de.yniklas.packman.examples.ExampleClass;
import de.yniklas.packman.Packman;
import de.yniklas.packman.examples.ExampleListAttributes;
import de.yniklas.packman.examples.ExampleMultipleKeys;
import de.yniklas.packman.examples.ExamplePackage;
import de.yniklas.packman.examples.ExampleScopePackage;
import de.yniklas.packman.examples.ExampleScopePackage2;
import de.yniklas.packman.examples.ExampleUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PackageTest {
    @Test
    public void testPackageAnnotation() {
        JSONObject packaged = Packman.pack("", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");
        expected.put("shameJon", "Jon is a N00b");

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testPackageAnnotationWithScope() {
        JSONObject packaged = Packman.pack("John", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testDirectoryPackaging() {
        JSONObject packaged = Packman.pack("", new ExampleClass());

        JSONObject expected = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("username", "Jack");
        user.put("password", "topSecret1234");
        expected.put("user", user);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testSubObjects() {
        JSONObject packaged = Packman.pack("auth", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");
        expected.put("shameJon", "Jon is a N00b");

        JSONObject userData = new JSONObject();
        userData.put("username", "Jog");
        userData.put("password", "topSecret");
        expected.put("userData", userData);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testSubObjectsWithExclude() {
        JSONObject packaged = Packman.pack("display", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");
        expected.put("shameJon", "Jon is a N00b");

        JSONObject userData = new JSONObject();
        userData.put("username", "Jog");
        // No password in the display scope package
        expected.put("userData", userData);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testLists() {
        JSONObject packaged = Packman.pack("", new ExampleListAttributes());

        JSONObject expected = new JSONObject();

        JSONArray myInts = new JSONArray();
        myInts.put(1).put(5);

        JSONArray myBytes = new JSONArray();
        myBytes.put(1).put(2);

        expected.put("myInts", myInts);
        expected.put("myBytes", myBytes);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testAdvancedCollectionTypes() {
        JSONObject packaged = Packman.pack("advancedCollections", new ExampleListAttributes());

        JSONObject expected = new JSONObject();

        JSONArray twoDimensionsOverpower = new JSONArray();

        JSONArray subArr1 = new JSONArray();
        subArr1.put("have");

        JSONArray subArr2 = new JSONArray();
        subArr2.put("a").put("nice").put("day");

        twoDimensionsOverpower.put(subArr1).put(subArr2);

        JSONArray registeredUser = new JSONArray();

        JSONObject u1 = new JSONObject();
        u1.put("username", "Jog");
        u1.put("password", "topSecret");
        registeredUser.put(u1);

        JSONObject u2 = new JSONObject();
        u2.put("username", "Jog");
        u2.put("password", "topSecret");
        registeredUser.put(u2);

        JSONArray floatLists = new JSONArray();
        JSONArray innerFloats = new JSONArray();
        innerFloats.put(6.8f);
        floatLists.put(new ArrayList<>()).put(innerFloats);

        expected.put("floats", floatLists);
        expected.put("registeredUsers", registeredUser);
        expected.put("twoDimensionsOverpower", twoDimensionsOverpower);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testMultiPackaging() {
        JSONObject packaged = Packman.pack("", new ExampleClass(), new ExampleUser());

        JSONObject expected = new JSONObject();

        JSONObject userData = new JSONObject();
        userData.put("username", "Jog");
        userData.put("password", "topSecret");

        JSONObject exampleClass = new JSONObject();
        JSONObject innerUser = new JSONObject();
        innerUser.put("username", "Jack").put("password", "topSecret1234");
        exampleClass.put("user", innerUser);

        expected.put("userData", userData);
        expected.put("de.yniklas.packman.examples.ExampleClass", exampleClass);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testScopes() {
        JSONObject packaged = Packman.pack("example", new ExampleScopePackage());
        JSONObject expected = new JSONObject().put("myCoolString", "cool!");
        assertTrue(expected.similar(packaged));

        JSONObject packagedWithoutScope = Packman.pack("", new ExampleScopePackage());
        assertTrue(new JSONObject().similar(packagedWithoutScope));

        JSONObject packagedWithDirScope = Packman.pack("example", new ExampleScopePackage(), new ExampleUser());
        JSONObject expected2 = new JSONObject();
        expected2.put("userData", new JSONObject().put("username", "Jog").put("password", "topSecret"));
        expected2.put("asCoolObj", new JSONObject().put("de.yniklas.packman.examples.ExampleScopePackage",
                new JSONObject().put("myCoolString", "cool!")));

        assertTrue(expected2.similar(packagedWithDirScope));

        JSONObject packagedWithSameDir = Packman.pack("example", new ExampleScopePackage(), new ExampleScopePackage2());
        JSONObject expected3 = new JSONObject().put("asCoolObj", new JSONObject().put("de.yniklas.packman.examples.ExampleScopePackage",
                new JSONObject().put("myCoolString", "cool!")).put("de.yniklas.packman.examples.ExampleScopePackage2",
                new JSONObject().put("seven", 7)));
        assertTrue(expected3.similar(packagedWithSameDir));
    }

    /**
     * @since 1.0.1
     */
    @Test
    public void testMultipleKeys() {
        JSONObject packaged = Packman.pack("", new ExampleMultipleKeys(), new ExampleMultipleKeys());

        JSONObject expected = new JSONObject()
                .put("de.yniklas.packman.examples.ExampleMultipleKeys",
                        new JSONObject().put("cool", 10).put("cool1", "huhu"))
                .put("de.yniklas.packman.examples.ExampleMultipleKeys1",
                        new JSONObject().put("cool", 10).put("cool1", "huhu"));

        assertTrue(expected.similar(packaged));
    }
}
