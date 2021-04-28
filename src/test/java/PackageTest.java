import de.yniklas.packi.examples.ExampleClass;
import de.yniklas.packi.Packi;
import de.yniklas.packi.examples.ExampleListAttributes;
import de.yniklas.packi.examples.ExamplePackage;
import de.yniklas.packi.examples.ExampleUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PackageTest {
    @Test
    public void testPackageAnnotation() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");
        expected.put("shameJon", "Jon is a N00b");

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testPackageAnnotationWithScope() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("John", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testDirectoryPackaging() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("", new ExampleClass());

        JSONObject expected = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("username", "Jack");
        user.put("password", "topSecret1234");
        expected.put("user", user);

        assertTrue(expected.similar(packaged));
    }

    @Test
    public void testSubObjects() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("auth", new ExamplePackage());

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
    public void testSubObjectsWithExclude() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("display", new ExamplePackage());

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
    public void testLists() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("", new ExampleListAttributes());

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
    public void testAdvancedCollectionTypes() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("advancedCollections", new ExampleListAttributes());

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
    public void testMultiPackaging() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("", new ExampleClass(), new ExampleUser());

        JSONObject expected = new JSONObject();

        JSONObject userData = new JSONObject();
        userData.put("username", "Jog");
        userData.put("password", "topSecret");

        JSONObject exampleClass = new JSONObject();
        JSONObject innerUser = new JSONObject();
        innerUser.put("username", "Jack").put("password", "topSecret1234");
        exampleClass.put("user", innerUser);

        expected.put("userData", userData);
        expected.put("de.yniklas.packi.examples.ExampleClass", exampleClass);

        assertTrue(expected.similar(packaged));
    }
}
