package com.letscode.moviesbattle.web.response.serializables;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.TitlesResponse;

import java.io.IOException;
import java.util.Iterator;

public class RoundResponseDeserializer extends StdDeserializer<RoundResponse> {

    public static final String ID_FIELD = "id";
    public static final String TITLE_FIELD = "title";
    public static final String RATINGS_FIELD = "ratings";
    public static final String RATING_FIELD = "rating";
    public static final String YEAR_FIELD = "year";

    public RoundResponseDeserializer(){
        super(RoundResponse.class);
    }

    @Override
    public RoundResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        Iterator<String> fieldNames = jsonNode.fieldNames();

        var firstTitleNode = jsonNode.get(fieldNames.next());
        var secondTitleNode = jsonNode.get(fieldNames.next());

        var firstTitle = new TitlesResponse(
                getId(firstTitleNode),
                getTitle(firstTitleNode),
                getRating(firstTitleNode),
                getYear(firstTitleNode)
        );

        var secondTitle = new TitlesResponse(
                getId(secondTitleNode),
                getTitle(secondTitleNode),
                getRating(secondTitleNode),
                getYear(secondTitleNode)
        );

        return new RoundResponse(firstTitle, secondTitle);
    }

    private String getId(JsonNode jsonNode) {
        return jsonNode.get(TITLE_FIELD)
                .get(ID_FIELD)
                .asText()
                .replace("title","")
                .replace("/", "");
    }
    private String getTitle(JsonNode jsonNode) {
        return jsonNode.get(TITLE_FIELD).get(TITLE_FIELD).asText();
    }

    private int getYear(JsonNode jsonNode) {
        return jsonNode.get(TITLE_FIELD).get(YEAR_FIELD).asInt();
    }

    private Double getRating(JsonNode jsonNode) {
        var rating = jsonNode.get(RATINGS_FIELD).get(RATING_FIELD);
        return rating != null ? rating.asDouble() : 0.0;
    }
}


