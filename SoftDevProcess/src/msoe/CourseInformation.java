package msoe;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class CourseInformation {

    private static LinkedHashMap<String, Course> CSTrack; //gets the CS Track to add courses to
    private static LinkedHashMap<String, Course> SETrack; //gets the SE Track to add courses to
    private static CourseTracksDataStructure ctds;

    public CourseInformation(CourseTracksDataStructure ctds) {

        this.ctds = new CourseTracksDataStructure();
        this.ctds = ctds;
        CSTrack = ctds.getCSTrackCSV();
        SETrack = ctds.getSETrackCSV();
    }

    public static void parseCurriculum(String filepath) throws CsvValidationException, IOException {
        int csIndex = 1; //needed for adding duplicate classes, can we fix this?
        int seIndex = 1; //needed for adding duplicate classes, can we fix this?
        String yearCS = "Freshman";
        String yearSE = "Freshman";


        CSVReader reader = new CSVReader(new FileReader(filepath)); //creates CSV Reader for parsing file
        String[] line; //string array for each line of the file
        while ((line = reader.readNext()) != null) {
            //If statement ignores the header line
            if (!line[0].equalsIgnoreCase("CS") && !line[0].equals("")) {
                //If the class already exists, adds an index number because hashmap doesnt allow duplicate keys
                if (CSTrack.containsKey(line[0])) {
                    if (line[0].equalsIgnoreCase("CS2911")) {
                        yearCS = "Sophomore";
                    } else if (line[0].equalsIgnoreCase("CS3040")) {
                        yearCS = "Junior";
                    } else if (line[0].equalsIgnoreCase("CS4000")) {
                        yearCS = "Senior";
                    }
                    String duplicate = line[0] + " - " + csIndex;
                    CSTrack.put(duplicate, new Course("", line[0], "", yearCS, 0, ""));
                    csIndex++;
                    //Otherwise just adds the class without the index
                } else {

                    if (line[0].equalsIgnoreCase("CS2911")) {
                        yearCS = "Sophomore";
                    } else if (line[0].equalsIgnoreCase("CS3040")) {
                        yearCS = "Junior";
                    } else if (line[0].equalsIgnoreCase("CS4000")) {
                        yearCS = "Senior";
                    }
                    CSTrack.put(line[0], new Course("", line[0], "", yearCS, 0, ""));
                }
                //If the class already exists, adds an index number because hashmap doesnt allow duplicate keys
                if (SETrack.containsKey(line[1])) {
                    if (line[1].equalsIgnoreCase("SE2030")) {
                        yearSE = "Sophomore";
                    } else if (line[1].equalsIgnoreCase("SE3010")) {
                        yearSE = "Junior";
                    } else if (line[1].equalsIgnoreCase("SE4000")) {
                        yearSE = "Senior";
                    }
                    String duplicate = line[1] + " - " + seIndex;
                    SETrack.put(duplicate, new Course("", line[1], "", yearSE, 0, ""));
                    seIndex++;
                    //Otherwise just adds the class without the index
                } else {
                    if (line[1].equalsIgnoreCase("SE2030")) {
                        yearSE = "Sophomore";
                    } else if (line[1].equalsIgnoreCase("SE3010")) {
                        yearSE = "Junior";
                    } else if (line[1].equalsIgnoreCase("SE4000")) {
                        yearSE = "Senior";
                    }
                    SETrack.put(line[1], new Course("", line[1], "", yearSE, 0, ""));
                }
            }
        }
        parseDate();
    }

    public static void parsePreRequisites(String filepath) throws CsvValidationException, IOException {
        LinkedHashMap<String, Course> prerequisites = ctds.getPrerequisites();

        CSVReader reader = new CSVReader(new FileReader(filepath));
        String[] line;
        while ((line = reader.readNext()) != null) {
            //Ignores header line
            if (!line[0].equalsIgnoreCase("course")) {
                //Creates the course and adds it to the prerequisites LinkedHashMap
                prerequisites.put(line[0], new Course(line[3], line[0], "", "", Integer.parseInt(line[1]), ""));
                //If the prerequisites section is not empty, gets the course and adds its prerequisites
                if (!line[2].equalsIgnoreCase("")) {
                    //If more than one prerequisite exists, splits it by the pipe symbol and adds each individual course
                    //as a prerequisite
                    String[] orClasses = line[2].split("\\|");
                    for (String preReqOR : orClasses) {
                        String[] andClasses = preReqOR.split("\\s+");
                        for (String preReqAnd : andClasses) {
                            if (preReqAnd.equals("MA231")) preReqAnd = "MA2314";
                            if (preReqAnd.equals("SE1011")) continue;
                            if (preReqAnd.equals("SE1021")) continue;
                            prerequisites.get(line[0]).addPrereq(new Course("", preReqAnd, "", "", 0, ""));
                        }
                    }
                }
            }
        }
        syncPreRequisites();
    }

    public static void parseOfferings(String filepath) throws IOException, CsvValidationException {
        LinkedHashMap<String, ArrayList<String>> offerings = ctds.getOfferings();

        CSVReader reader = new CSVReader(new FileReader(filepath));

        String[] line;

        while ((line = reader.readNext()) != null) {
            String csTerm = "";
            String seTerm = "";

            if (!line[0].equalsIgnoreCase("course")) {

                offerings.put(line[0], new ArrayList<>());

                if (line[6].equalsIgnoreCase("1")) {
                    csTerm = "Fall";
                } else if (line[6].equalsIgnoreCase("2")) {
                    csTerm = "Winter";
                } else if (line[6].equalsIgnoreCase("3")) {
                    csTerm = "Spring";
                }

                if (line[7].equalsIgnoreCase("1")) {
                    seTerm = "Fall";
                } else if (line[7].equalsIgnoreCase("2")) {
                    seTerm = "Winter";
                } else if (line[7].equalsIgnoreCase("3")) {
                    seTerm = "Spring";
                }
                offerings.get(line[0]).addAll(Arrays.asList(csTerm, seTerm));
            }
        }
        syncOfferings();
    }

    private static void syncPreRequisites() {
        LinkedHashMap<String, Course> preRequisites = ctds.getPrerequisites();

        for (Map.Entry<String, Course> csTrackEntry : CSTrack.entrySet()) {
            //gets key for comparison to list of prerequisites
            String courseCodeCS = csTrackEntry.getKey();

            Course courseToAddPreReqsTo = csTrackEntry.getValue();
            Course prereqsCourse = preRequisites.getOrDefault(courseCodeCS, null);
            if (prereqsCourse == null) continue;

            courseToAddPreReqsTo.setCourseName(prereqsCourse.getCourseName());
            ArrayList<Course> preRequisitesToAdd = prereqsCourse.getPrerequisites();

            if (preRequisitesToAdd.size() != 0) {
                for (Course course : preRequisitesToAdd) {
                    courseToAddPreReqsTo.addPrereq(course);
                }
            }
        }

        for (Map.Entry<String, Course> seTrackEntry : SETrack.entrySet()) {
            //gets key for comparison to list of prerequisites
            String courseCodeSE = seTrackEntry.getKey();

            Course courseToAddPreReqsTo = seTrackEntry.getValue();
            Course prereqsCourse = preRequisites.getOrDefault(courseCodeSE, null);
            if (prereqsCourse == null) continue;

            courseToAddPreReqsTo.setCourseName(prereqsCourse.getCourseName());
            ArrayList<Course> preRequisitesToAdd = prereqsCourse.getPrerequisites();

            if (preRequisitesToAdd.size() != 0) {
                for (Course course : preRequisitesToAdd) {
                    courseToAddPreReqsTo.addPrereq(course);
                }
            }
        }
        parseDate();
    }

    private static void syncOfferings() {
        LinkedHashMap<String, ArrayList<String>> offerings = ctds.getOfferings();

        for (Map.Entry<String, Course> csTrackEntry : CSTrack.entrySet()) {
            //gets key for comparison to list of offerings
            String courseCodeCS = csTrackEntry.getKey();
            if (courseCodeCS.contains("FREE") || courseCodeCS.contains("HUSS") || courseCodeCS.contains("TECHEL")
                    || courseCodeCS.contains("MASCIEL") || courseCodeCS.contains("SCIEL") || courseCodeCS.contains("BUSEL")) {
                continue;
            }
            if (courseCodeCS.equals("HU4321")) {
                courseCodeCS = "HU432";
            }
            Course course = csTrackEntry.getValue();
            String term = offerings.get(courseCodeCS).get(0);

            if (!term.equals("")) {
                course.setTerm(term);
            }
        }

        for (Map.Entry<String, Course> seTrackEntry : SETrack.entrySet()) {
            //gets key for comparison to list of offerings
            String courseCodeSE = seTrackEntry.getKey();
            if (courseCodeSE.contains("FREE") || courseCodeSE.contains("HUSS") || courseCodeSE.contains("TECHEL")
                    || courseCodeSE.contains("MASCIEL") || courseCodeSE.contains("SCIEL") || courseCodeSE.contains("BUSEL")) {
                continue;
            }
            Course course = seTrackEntry.getValue();
            String term = offerings.get(courseCodeSE).get(1);

            if (!term.equals("")) {
                course.setTerm(term);
            }
        }
        parseDate();
    }

    public static void parseDate() {
        dated(CSTrack);
        dated(SETrack);
    }

    public static LinkedHashMap<String, Course> dated(LinkedHashMap<String, Course> track) {
        for (Map.Entry<String, Course> csTrackEntry : track.entrySet()) {

            String term = track.get(csTrackEntry.getKey()).getTerm();
            String year = track.get(csTrackEntry.getKey()).getYear();
            int yearNum = yearToNum(year);
            if (term.equalsIgnoreCase("Fall")) {
                track.get(csTrackEntry.getKey()).setDate("September " + yearNum);
            } else if (term.equalsIgnoreCase("Winter")) {
                track.get(csTrackEntry.getKey()).setDate("November " + yearNum);
            } else if (term.equalsIgnoreCase("Spring")) {
                track.get(csTrackEntry.getKey()).setDate("March " + yearNum);
            }

        }
        return track;
    }

    private static int yearToNum(String year) {
        int numYear = Year.now().getValue();

        if (year.equalsIgnoreCase("sophomore")) {
            numYear += 1;
        } else if (year.equalsIgnoreCase("junior")) {
            numYear +=2;

        } else if (year.equalsIgnoreCase("senior")) {
            numYear += 3;
        }

        return numYear;
    }

    /**
     * This method will call upon the other methods to parse everything.
     * @param curr
     * @param pre
     * @param off
     * @throws IOException
     * @throws CsvValidationException
     */
    public void completedParse(File curr, File pre, File off) throws IOException, CsvValidationException {
        parseCurriculum(curr.getPath());
        parsePreRequisites(pre.getPath());
        parseOfferings(off.getPath());
    }
}
