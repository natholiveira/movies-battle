package com.letscode.moviesbattle.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzResponse {
    String id;
    Long currentScore;
    Long maximunAttempts;
    Long errors;
    Boolean finished;
    Long currentRankingPosition;
}
