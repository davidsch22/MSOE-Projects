import msoe.CourseTracksDataStructure;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class courseTracksDataStructureTesting {

    @Test
    public void testConstructor(){
        CourseTracksDataStructure sut = new CourseTracksDataStructure();
        assertTrue(sut.getSETrackCSV().isEmpty());
        assertTrue(sut.getCSTrackCSV().isEmpty());
        assertTrue(sut.getOfferings().isEmpty());
        assertTrue(sut.getPrerequisites().isEmpty());
    }
}
