package com.example.dslab91;

import com.example.dslab91.Core.Repository.DBRepository;
import com.example.dslab91.Core.Repository.Repository;
import jakarta.ws.rs.ApplicationPath;
import lombok.Getter;

@ApplicationPath("/")
public class Application extends jakarta.ws.rs.core.Application {
    @Getter
    private static Application instance;

    @Getter
    private final Repository repository;

    public Application() {
        repository = new DBRepository("jdbc:postgresql://localhost:5432/dev", "postgres", "postgres");
        instance = this;
    }

}