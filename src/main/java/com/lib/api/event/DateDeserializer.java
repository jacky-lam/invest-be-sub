package com.lib.api.event;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateDeserializer extends StdDeserializer<Date> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // specify your specific timezone

    public DateDeserializer() {
        this(null);
    }

    public DateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
