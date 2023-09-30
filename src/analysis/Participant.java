package analysis;

import java.io.File;
import java.io.FileReader;

public class Participant {
    private int id;

    private File fixationFile;
    private File answersFile;

    public Participant(int id) {
        this.id = id;
    }
}
