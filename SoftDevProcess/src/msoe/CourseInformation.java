package msoe;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class CourseInformation {
    /**
     * This method will call upon the other methods to parse everything.
     * @param curr
     * @param pre
     * @param off
     * @throws IOException
     * @throws CsvValidationException
     */
    public static void completedParse(File curr, File pre, File off) throws IOException, CsvValidationException {
        parsePreRequisites(pre.getPath());
        parseCurriculum(curr.getPath());
        parseOfferings(off.getPath());
    }

    private static void parseCurriculum(String filepath) throws CsvValidationException, IOException {
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
                if (CourseLists.CSTrack.containsKey(line[0])) {
                    String duplicate = line[0] + " - " + csIndex;
                    CourseLists.courses.put(duplicate, new BasicCourseInfo("", line[0], 0));
                    CourseLists.CSTrack.put(duplicate, new TrackCourseInfo("", yearCS, ""));
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

                    BasicCourseInfo newCourse = CourseLists.courses.getOrDefault(line[0], null);
                    if (newCourse == null) {
                        CourseLists.courses.put(line[0], new BasicCourseInfo("", line[0], 0));
                    }
                    CourseLists.CSTrack.put(line[0], new TrackCourseInfo("", yearCS, ""));
                }

                if (CourseLists.SETrack.containsKey(line[1])) {
                    String duplicate = line[1] + " - " + seIndex;
                    CourseLists.courses.put(duplicate, new BasicCourseInfo("", line[1], 0));
                    CourseLists.SETrack.put(duplicate, new TrackCourseInfo("", yearSE, ""));
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

                    BasicCourseInfo newCourse = CourseLists.courses.getOrDefault(line[1], null);
                    if (newCourse == null) {
                        CourseLists.courses.put(line[1], new BasicCourseInfo("", line[1], 0));
                    }
                    CourseLists.SETrack.put(line[1], new TrackCourseInfo("", yearSE, ""));
                }
            }
        }
        parseDate();
    }

    private static void parsePreRequisites(String filepath) throws CsvValidationException, IOException {
        CSVReader reader = new CSVReader(new FileReader(filepath));
        String[] line;
        while ((line = reader.readNext()) != null) {
            //Ignores header line
            if (!line[0].equalsIgnoreCase("course")) {
                //Creates the course and adds it to the prerequisites LinkedHashMap
                CourseLists.courses.put(line[0], new BasicCourseInfo(line[3], line[0], Integer.parseInt(line[1])));
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

                            BasicCourseInfo prereq = CourseLists.courses.getOrDefault(preReqAnd, null);
                            BasicCourseInfo newPrereq;
                            if (prereq == null) {
                                newPrereq = new BasicCourseInfo("", preReqAnd, 0);
                                CourseLists.courses.put(preReqAnd, newPrereq);
                            } else {
                                newPrereq = prereq;
                            }

                            CourseLists.courses.get(line[0]).addPrereq(newPrereq);
                        }
                    }
                }
            }
        }
    }

    private static void parseOfferings(String filepath) throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader(filepath));

        String[] line;

        while ((line = reader.readNext()) != null) {
            String csTerm = "";
            String seTerm = "";

            if (!line[0].equalsIgnoreCase("course")) {

                CourseLists.offerings.put(line[0], new ArrayList<>());

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
                CourseLists.offerings.get(line[0]).addAll(Arrays.asList(csTerm, seTerm));
            }
        }
        syncOfferings();
    }

    private static void syncOfferings() {
        for (Entry<String, TrackCourseInfo> csTrackEntry : CourseLists.CSTrack.entrySet()) {
            //gets key for comparison to list of offerings
            String courseCodeCS = csTrackEntry.getKey();
            if (courseCodeCS.contains("FREE") || courseCodeCS.contains("HUSS") || courseCodeCS.contains("TECHEL")
                    || courseCodeCS.contains("MASCIEL") || courseCodeCS.contains("SCIEL") || courseCodeCS.contains("BUSEL")) {
                continue;
            }
            if (courseCodeCS.equals("HU4321")) {
                courseCodeCS = "HU432";
            }
            String term = CourseLists.offerings.get(courseCodeCS).get(0);
            TrackCourseInfo course = csTrackEntry.getValue();
            if (!term.equals("")) {
                course.setTerm(term);
            }
        }

        for (Entry<String, TrackCourseInfo> seTrackEntry : CourseLists.SETrack.entrySet()) {
            //gets key for comparison to list of offerings
            String courseCodeSE = seTrackEntry.getKey();
            if (courseCodeSE.contains("FREE") || courseCodeSE.contains("HUSS") || courseCodeSE.contains("TECHEL")
                    || courseCodeSE.contains("MASCIEL") || courseCodeSE.contains("SCIEL") || courseCodeSE.contains("BUSEL")) {
                continue;
            }
            String term = CourseLists.offerings.get(courseCodeSE).get(1);
            TrackCourseInfo course = seTrackEntry.getValue();
            if (!term.equals("")) {
                course.setTerm(term);
            }
        }
        parseDate();
    }

    public static void parseDate() {
        dated(CourseLists.CSTrack);
        dated(CourseLists.SETrack);
    }

    private static void dated(HashMap<String, TrackCourseInfo> track) {
        for (TrackCourseInfo course : track.values()) {
            String term = course.getTerm();
            String year = course.getYear();
            int yearNum = yearToNum(year);
            if (term.equalsIgnoreCase("Fall")) {
                course.setDate("September " + yearNum);
            } else if (term.equalsIgnoreCase("Winter")) {
                course.setDate("November " + yearNum);
            } else if (term.equalsIgnoreCase("Spring")) {
                course.setDate("March " + yearNum);
            }
        }
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
}
