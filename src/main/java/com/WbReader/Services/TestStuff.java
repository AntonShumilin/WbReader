package com.WbReader.Services;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.jwt.Jwt;
import ru.gribuser.xml.fictionbook.x20.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class TestStuff {




    public static void main(String[] args) throws IOException, XmlException {

//        Jwt.withTokenValue().


//
//        TreeMap<Integer, String> xmlContentMap = new TreeMap<>();
//        ArrayList<String> xmlPageList = new ArrayList<>();
//
////        FictionBookDocument fb2 = FictionBookDocument.Factory.parse(new File("/home/thunder/1/Voyna_neudachnikov.fb2"));
//        FictionBookDocument fb2 = FictionBookDocument.Factory.parse(new File("/home/thunder/1/Ring.fb2"));
//
//        if (fb2 != null && fb2.getFictionBook() != null && fb2.getFictionBook().getBody() != null) {
//            BodyType body = fb2.getFictionBook().getBody();
//
//            StringBuilder sb = new StringBuilder();
//            SectionType [] sectionTypes = body.getSectionArray();
//            if (sectionTypes != null) {
//                readChildSections(sectionTypes, sb, xmlPageList, xmlContentMap);
//            }
//        }
//
//        String resultFile = "/home/thunder/1/result.txt";
//        Path resultPath = Paths.get(resultFile);
//        try  {
//            if (Files.exists(resultPath)) {
//                Files.delete(resultPath);
//            }
//
//            FileWriter fw = new FileWriter(resultFile);
//
//            for(Map.Entry<Integer,String> entry : xmlContentMap.entrySet()) {
//                fw.write("page " + entry.getKey() + " ");
//                fw.write(entry.getValue());
//            }
//
//            fw.flush();
//            for (int i = 0; i <xmlPageList.size(); i++) {
//                fw.write(" \n");
//                fw.write(" \n");
//                fw.write("СТРАНИЦА " + i);
//                fw.write(" \n");
//                fw.write(xmlPageList.get(i));
//                fw.flush();
//            }
//
//            fw.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    static void readChildSections(SectionType[] secArr, StringBuilder sb, ArrayList<String> xmlPageList, TreeMap<Integer, String> xmlContentMap) {
        for (SectionType sec : secArr) {
            if (sec != null) {
                String sectionTitle = getSectionTitle(sec);
                if (sectionTitle != null) {
                    sb.append(sectionTitle);
                    Integer lastPage = xmlPageList.size();
                    String lastTitle = xmlContentMap.get(lastPage);
                    if (lastTitle != null) {
                        sectionTitle = lastTitle + sectionTitle;
                    }
                    xmlContentMap.put(lastPage, sectionTitle);
                }
                SectionType[] subSecArr = sec.getSectionArray();
                if (subSecArr != null && subSecArr.length > 0) {
                    readChildSections(subSecArr, sb, xmlPageList, xmlContentMap);
                } else {
                    PType[] pTypes = sec.getPArray();
                    if (pTypes != null) {
                        for (PType p : pTypes) {
                            if (p != null) {
                                String pText = replaceXml(p.xmlText());
                                sb.append(pText);
                                if (!pText.endsWith("\n")) {
                                    sb.append("\n");
                                }
                                cutPage(sb, xmlPageList, false);
                            }
                        }
                    }
                }
            }
            cutPage(sb, xmlPageList, true);
        }
        cutPage(sb, xmlPageList, true);
    }

    static void cutPage (StringBuilder sb, List<String> pageList, boolean lastPage) {
        if (sb.length() > 0) {
            if (sb.length() >= 2000) {
                int cutIndex = sb.indexOf("\n", 2000);
                if (cutIndex > 0) {
                    String tail = sb.substring(cutIndex, sb.length());
                    pageList.add(sb.substring(0, cutIndex));
                    sb.delete(0, cutIndex);
                } else {
                    pageList.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else if (lastPage) {
                pageList.add(sb.toString());
                sb.delete(0, sb.length());
            }
        }
    }



    static String getText(TextFieldType object) {
        if (object != null) {
            return object.xmlText().replaceAll("<.+?>","");
        } else {
            return "";
        }

    }

    static String replaceXml (String xml) {
        if (xml != null) {
            return xml.replaceAll("<.+?>","");
        } else {
            return "";
        }

    }

    public static String getSectionTitle (SectionType sec) {
        StringBuilder titleSb = new StringBuilder();
        TitleType titleType = sec.getTitle();
        if (titleType != null) {
            for (PType p : titleType.getPArray()) {
                if (p != null) {
                    titleSb.append(replaceXml(p.xmlText())).append("\n");
                }
            }
            return titleSb.toString();
        } else {
            return null;
        }
    }

}
