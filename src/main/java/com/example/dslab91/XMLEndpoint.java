package com.example.dslab91;

import com.example.dslab91.Core.Models.Artist;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/xml")
public class XMLEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Artist> getXmlArtists() {
        return Application.getInstance().getRepository().getArtists();
    }
}