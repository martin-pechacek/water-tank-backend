package watertank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class AbstractController {

    public static String jsonAsString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
