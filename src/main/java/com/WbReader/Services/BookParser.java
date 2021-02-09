package com.WbReader.Services;

import com.WbReader.Data.Book;
import org.apache.xmlbeans.XmlException;
import ru.gribuser.xml.fictionbook.x20.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BookParser {

    private Book book;
    private FictionBookDocument fb2;
    private TreeMap<Integer, String> xmlContentMap = new TreeMap<>();
    private ArrayList<String> xmlPageList = new ArrayList<>();


    public BookParser(Book book, String url) throws IOException, XmlException {
        this.book = book;
        this.fb2 = FictionBookDocument.Factory.parse(new File(url));
    }

    public void loadBookMetaFromXml() {

        if (fb2 != null &&
            fb2.getFictionBook() != null &&
            fb2.getFictionBook().getDescription() != null &&
            fb2.getFictionBook().getDescription().getTitleInfo() != null
        ) {
            TitleInfoType titleInfo = fb2.getFictionBook().getDescription().getTitleInfo();
            book.setTitle(getText(titleInfo.getBookTitle()));
            TitleInfoType.Author[] authors = titleInfo.getAuthorArray();
            if (authors != null) {
                StringBuilder xmlAuthors = new StringBuilder();
                for (TitleInfoType.Author author : authors) {
                    xmlAuthors.append(getText(author.getFirstName()))
                            .append(" ")
                            .append(getText(author.getMiddleName()))
                            .append(" ")
                            .append(getText(author.getLastName()))
                            .append("\n");
                }
                book.setAuthor(xmlAuthors.toString());
            }
        }
    }

    public void loadBookContentAndPageListFromXml() {

        if (fb2 != null && fb2.getFictionBook() != null && fb2.getFictionBook().getBody() != null) {
            BodyType body = fb2.getFictionBook().getBody();

            StringBuilder sb = new StringBuilder();
            SectionType [] sectionTypes = body.getSectionArray();
            if (sectionTypes != null) {
                readChildSections(sectionTypes, sb);
            }
        }
        book.setContent(xmlContentMap);
        book.setPageList(xmlPageList);
    }

    void readChildSections(SectionType[] secArr, StringBuilder sb) {
        for (SectionType sec : secArr) {
            if (sec != null) {
                String sectionTitle = getSectionTitle(sec);
                sb.append(sectionTitle);
                Integer lastPage = xmlPageList.size();
                String lastTitle = xmlContentMap.get(lastPage);
                if (lastTitle != null) {
                    sectionTitle = lastTitle + "\n" + sectionTitle;
                }
                xmlContentMap.put(lastPage, sectionTitle);


                SectionType[] subSecArr = sec.getSectionArray();
                if (subSecArr != null && subSecArr.length > 0) {
                    readChildSections(subSecArr, sb);
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

    void cutPage (StringBuilder sb, List<String> pageList, boolean lastPage) {
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


    public String getSectionTitle (SectionType sec) {
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
            return "...";
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
}
