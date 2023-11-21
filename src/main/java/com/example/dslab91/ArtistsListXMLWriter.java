package com.example.dslab91;

import com.example.dslab91.Core.Models.Album;
import com.example.dslab91.Core.Models.Artist;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

@Provider
@Produces(MediaType.APPLICATION_XML)
public class ArtistsListXMLWriter implements MessageBodyWriter<List<Artist>> {
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return List.class.isAssignableFrom(type) && genericType.getTypeName().contains("Artist");
    }

    @Override
    public void writeTo(List<Artist> artists, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException, WebApplicationException {
        PrintWriter writer = new PrintWriter(out);

        try {
            Document doc = createDocument(artists);

            Source source = new DOMSource(doc);
            Result result = new StreamResult(writer);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
        } catch (TransformerException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        writer.flush();
        writer.close();
    }

    Document createDocument(List<Artist> artists) throws ParserConfigurationException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element root = document.createElement(Artist.ARTISTS);
        for (Artist item : artists) {
            Element artist = document.createElement(Artist.ARTIST);

            artist.setAttribute(Artist.ID, String.valueOf(item.getId()));
            artist.setAttribute(Artist.NAME, item.getName());
            artist.setAttribute(Artist.AGE, String.valueOf(item.getAge()));

            Element albums = document.createElement(Artist.ALBUMS);
            for (Album subitem : item.getAlbums()) {
                Element album = document.createElement(Album.ALBUM);
                album.setAttribute(Album.ID, String.valueOf(subitem.getId()));
                album.setAttribute(Album.NAME, subitem.getName());
                albums.appendChild(album);
            }
            artist.appendChild(albums);

            root.appendChild(artist);
        }

        document.appendChild(root);

        return document;
    }
}
