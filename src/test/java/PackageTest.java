import de.yniklas.packagerize.examples.ExampleClass;
import de.yniklas.packagerize.Packi;
import de.yniklas.packagerize.examples.ExampleListAttributes;
import de.yniklas.packagerize.examples.ExamplePackage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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

        assertEquals(expected.toString(), packaged.toString());
    }

    @Test
    public void testPackageAnnotationWithScope() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("John", new ExamplePackage());

        JSONObject expected = new JSONObject();
        expected.put("foo", "bar");
        expected.put("b", 19);
        expected.put("synonym", "Poop");
        expected.put("name", "Jack");

        assertEquals(expected.toString(), packaged.toString());
    }

    @Test
    public void testDirectoryPackaging() throws IllegalAccessException {
        JSONObject packaged = Packi.pack("", new ExampleClass());

        JSONObject expected = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("username", "Jack");
        user.put("password", "topSecret1234");
        expected.put("user", user);

        assertEquals(expected.toString(), packaged.toString());
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

        assertEquals(expected.toString(), packaged.toString());
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

        assertEquals(expected.toString(), packaged.toString());
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

        assertEquals(expected.toString(), packaged.toString());
    }
}
