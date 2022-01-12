package com.letscode.moviesbattle.web.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.letscode.moviesbattle.web.response.serializables.RoundResponseDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = RoundResponseDeserializer.class)
public class RoundResponse {
    TitlesResponse title01;
    TitlesResponse title02;
}
