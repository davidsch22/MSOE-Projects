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
import java.util.HashMap;

/**
 * A data structure containing multiple HashMaps and hardcoded test courses.
 */
public class CourseLists {
    public static HashMap<String, BasicCourseInfo> courses = new HashMap<>();
    public static HashMap<String, TrackCourseInfo> CSTrack = new HashMap<>();
    public static HashMap<String, TrackCourseInfo> SETrack = new HashMap<>();
    public static HashMap<String, ArrayList<String>> offerings = new HashMap<>();
}
