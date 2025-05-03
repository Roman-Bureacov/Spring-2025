package PSPC;

import java.io.File;
import java.io.FileNotFoundException;

public final class Utility {
    /** The file path to the samples folder */
    public static final String SAMPLES_PATH = "edu\\PSPC\\samples";

    /**
     * Retrieves the file name from the samples path
     */
    public static File getFile(final String pFileName) throws FileNotFoundException {
        return new File(SAMPLES_PATH + "\\" + pFileName);
    }
}
