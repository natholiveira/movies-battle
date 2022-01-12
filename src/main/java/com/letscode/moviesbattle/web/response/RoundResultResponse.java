package com.letscode.moviesbattle.web.response;

import com.letscode.moviesbattle.domain.model.Round;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoundResultResponse {
    String rightAnswer;
    String message;
    Round round;
    Long maximunAttempts;
}
