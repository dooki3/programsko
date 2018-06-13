package WEKALogic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Karlo
 */
public class FileHandlerTest {
    FileHandler fl = new FileHandler();
    String filepath = "C:\\Users\\Karlo\\Downloads\\JDT_R2_0.csv";



    public FileHandlerTest() throws Exception {
        fl.loadFile(filepath);
    }

    @Test
    public void testLoadFile() throws Exception {
        FileHandler f2 = new FileHandler();
        System.out.println("loadFile");
        String path;
        path = "C:\\Users\\Karlo\\Downloads\\JDT_R2_0.csv";
        f2.loadFile(path);

        // TODO review the generated test code and remove the default call to fail.
        assertNotNull(f2.getData());

    }
    @Test
    public void testRemoveFileInstance() throws Exception {
        System.out.println("removeFileInstance");
        fl.loadFile(filepath);
        fl.removeFileInstance();
        // TODO review the generated test code and remove the default call to fail.
        assertNull(fl.getData());
    }

    @Test
    public void testConvertValues() {
        fl.convertValues(fl.getData(), FileHandler.convertMethods.NUMERIC_TO_BINARY, "50");
        fl.getData().attribute(49).isNominal();
        assertTrue(fl.getData().attribute(49).isNominal());
    }
}