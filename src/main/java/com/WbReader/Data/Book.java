package com.WbReader.Data;

import com.WbReader.Services.BookParser;
import org.apache.xmlbeans.XmlException;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.IOException;
import java.util.*;

@Entity
@Table(name="book")
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String url;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Transient
    private TreeMap<Integer, String> content;

    @Transient
    private List<String> pageList;

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public Map<Integer, String> getContent() throws IOException, XmlException {
        if (content == null) {
            new BookParser(this, url).loadBookContentAndPageListFromXml();
        }
        return content;
    }

    public void setContent(TreeMap<Integer, String> content) {
        this.content = content;
    }

    public List<String> getPageList() throws IOException, XmlException {
        if (pageList == null) {
            new BookParser(this, url).loadBookContentAndPageListFromXml();
        }
        return pageList;
    }

    public void setPageList(List<String> pageList) {
        this.pageList = pageList;
    }

    public String getPage (int pageNumber) throws IOException, XmlException {
        if (getPageList() != null) {
            return pageList.get(pageNumber);
        } else {
            return null;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //    public boolean loadBookMetaFromXml (String path)  {
//        boolean result = false;
//        try {
//            FictionBookDocument fb2 = FictionBookDocument.Factory.parse(new File(path));
//
//            if (fb2 != null &&
//                    fb2.getFictionBook() != null &&
//                    fb2.getFictionBook().getDescription() != null &&
//                    fb2.getFictionBook().getDescription().getTitleInfo() != null
//            ) {
//                TitleInfoType titleInfo = fb2.getFictionBook().getDescription().getTitleInfo();
//                title = getText(titleInfo.getBookTitle());
//                TitleInfoType.Author[] authors = titleInfo.getAuthorArray();
//                if (authors != null) {
//                    StringBuilder xmlAuthors = new StringBuilder();
//                    for (TitleInfoType.Author author : authors) {
//                        xmlAuthors. append(getText(author.getFirstName()))
//                                    .append(" ")
//                                    .append(getText(author.getMiddleName()))
//                                    .append(" ")
//                                    .append(getText(author.getLastName()))
//                                    .append( "\n");
//                    }
//                    author = xmlAuthors.toString();
//                }
//            }
//            result = true;
//        } catch (IOException | XmlException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }



//    public boolean loadBookContentAndPageListFromXml (String path) {
//        boolean result = false;
//
//        if (content == null || pageList == null) {
//            TreeMap<Integer, String> xmlContentMap = new TreeMap<>();
//            ArrayList<String> xmlPageList = new ArrayList<>();
//            try {
//                FictionBookDocument fb2 = FictionBookDocument.Factory.parse(new File(path));
//                if (fb2 != null && fb2.getFictionBook() != null && fb2.getFictionBook().getBody() != null) {
//                    BodyType body = fb2.getFictionBook().getBody();
//
//                    StringBuilder sb = null;
//                    SectionType [] sectionTypes = body.getSectionArray();
//                    if (sectionTypes != null) {
//                        for (int i = 0; i < sectionTypes.length; i++) {
//                            sb = new StringBuilder();
//                            SectionType sec = body.getSectionArray(i);
//                            String sectionTitle = getSectionTitle(sec);
//                            xmlContentMap.put(xmlPageList.size(), sectionTitle);
//                            sb.append(sectionTitle);
//                            PType [] pTypes = sec.getPArray();
//                            if (pTypes != null) {
//                                for (PType p : pTypes) {
//                                    if (p != null) {
//                                        String pText = replaceXml(p.xmlText());
//                                        sb.append(pText);
//                                        if (!pText.endsWith("\n")) {
//                                            sb.append("\n");
//                                        }
//                                        if (sb.length()>2000)  {
//                                            xmlPageList.add(sb.toString());
//                                            sb = new StringBuilder();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        xmlPageList.add(sb != null ? sb.toString() : "");
//                    }
//                }
//                result = true;
//            } catch (IOException | XmlException e) {
//                e.printStackTrace();
//            }
//            if (result) {
//                content = xmlContentMap;
//                pageList = xmlPageList;
//            }
//        }
//        return result;
//    }
//
//    public String getSectionTitle (SectionType sec) {
//        if (sec != null) {
//            StringBuilder titleSb = new StringBuilder();
//            TitleType titleType = sec.getTitle();
//            if (titleType != null) {
//                for (PType p : titleType.getPArray()) {
//                    if (p != null) {
//                        titleSb.append(replaceXml(p.xmlText())).append("\n");
//                    }
//                }
//            }
//            return titleSb.toString();
//        } else  {
//            return "";
//        }
//    }
//
//    static String getText(TextFieldType object) {
//        if (object != null) {
//            return object.xmlText().replaceAll("<.+?>","");
//        } else {
//            return "";
//        }
//
//    }
//
//    static String replaceXml (String xml) {
//        if (xml != null) {
//            return xml.replaceAll("<.+?>","");
//        } else {
//            return "";
//        }
//
//    }
//    -------------------> from K2 Parser
//
//    public boolean loadBookMetaFromXml (String path)  {
//        boolean result = false;
//        try {
//            FictionBook fb2 = new FictionBook(new File(path));
//            if (fb2.getDescription() != null) {
//
//                Description desc = fb2.getDescription();
//                if (desc.getTitleInfo() != null) {
//                    title = fb2.getDescription().getTitleInfo().getBookTitle();
//                    if (desc.getTitleInfo().getAuthors() != null) {
//                        String xmlAuthors ="";
//                        for (Person person : desc.getTitleInfo().getAuthors()) {
//                            xmlAuthors += person.getFirstName() == null ? "" : person.getFirstName() + " ";
//                            xmlAuthors += person.getMiddleName() == null ? "" : person.getMiddleName() + " ";
//                            xmlAuthors += person.getLastName() == null ? "" : person.getLastName() + "\n";
//                        }
//                        author = xmlAuthors;
//                    }
//                }
//            }
//            result = true;
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public boolean loadBookContentAndPageListFromXml (String path) {
//        boolean result = false;
//
//        if (content == null || pageList == null) {
//            TreeMap<Integer, String> xmlContentMap = new TreeMap<>();
//            ArrayList<String> xmlPageList = new ArrayList<>();
//            try {
//                FictionBook fb2 = new FictionBook(new File(path));
//                if (fb2.getBody() != null) {
//                    Body body = fb2.getBody();
//
//                    StringBuilder sb = null;
//                    for (int i = 0; i < body.getSections().size(); i++) {
//                        sb = new StringBuilder();
//                        Section sec = body.getSections().get(i);
//                        String sectionTitle = getSectionTitle(sec);
//                        xmlContentMap.put(xmlPageList.size(), sectionTitle);
//                        sb.append(sectionTitle);
//                        for (Element e : sec.getElements()) {
//                            if (e != null && e.getText() != null) {
//                                sb.append(e.getText());
//                                if (!e.getText().endsWith("\n")) {
//                                    sb.append("\n");
//                                }
//                                if (sb.length()>2000)  {
//                                    xmlPageList.add(sb.toString());
//                                    sb = new StringBuilder();
//                                }
//                            }
//                        }
//                    }
//                    xmlPageList.add(sb != null ? sb.toString() : "");
//                }
//                result = true;
//            } catch (ParserConfigurationException | IOException | SAXException e) {
//                e.printStackTrace();
//            }
//            if (result) {
//                content = xmlContentMap;
//                pageList = xmlPageList;
//            }
//        }
//        return result;
//    }

}
