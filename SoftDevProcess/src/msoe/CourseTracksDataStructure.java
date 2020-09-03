/*
 * Name: Anthony Lohmiller, Kam Mitchell, David Schulz, Jesse Sierra
 * Date: 14 April 2020
 * Quarter: Spring 2019-2020
 * Class: SE Process
 * Lab: 4
 * Created: 30 March 2020
 */

package msoe;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A data structure containing multiple HashMaps and hardcoded test courses.
 */
public class CourseTracksDataStructure {

    private LinkedHashMap<String, Course> CSTrackCSV;
    private LinkedHashMap<String, Course> SETrackCSV;
    private LinkedHashMap<String, Course> prerequisites;
    private LinkedHashMap<String, ArrayList<String>> offerings;

    public CourseTracksDataStructure(){
        CSTrackCSV = new LinkedHashMap<>();
        SETrackCSV = new LinkedHashMap<>();
        prerequisites = new LinkedHashMap<>();
        offerings = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, Course> getCSTrackCSV() {
        return CSTrackCSV;
    }

    public LinkedHashMap<String, Course> getSETrackCSV() {
        return SETrackCSV;
    }

    public LinkedHashMap<String, Course> getPrerequisites() {
        return prerequisites;
    }

    public LinkedHashMap<String, ArrayList<String>> getOfferings() {
        return offerings;
    }
}
