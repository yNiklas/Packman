import de.yniklas.packagerize.examples.ExampleClass;
import de.yniklas.packagerize.Packi;
import de.yniklas.packagerize.examples.ExamplePackage;
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
}
