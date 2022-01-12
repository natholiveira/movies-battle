package com.letscode.moviesbattle.domain.service.impl;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.GatewayException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.resources.gateway.ImdbGatewayService;
import com.letscode.moviesbattle.domain.model.Round;
import com.letscode.moviesbattle.domain.model.Title;
import com.letscode.moviesbattle.domain.model.TitlesGenre;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.domain.model.type.Genres;
import com.letscode.moviesbattle.domain.repository.QuizzRepository;
import com.letscode.moviesbattle.domain.repository.RoundRepository;
import com.letscode.moviesbattle.domain.repository.TitleRepository;
import com.letscode.moviesbattle.domain.repository.UserRepository;
import com.letscode.moviesbattle.web.request.RoundAnswerRequest;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.RoundResultResponse;
import com.letscode.moviesbattle.web.response.TitlesResponse;
import com.letscode.moviesbattle.domain.service.QuizzService;
import com.letscode.moviesbattle.domain.service.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RoundServiceImpl implements RoundService {
    @Autowired
    ImdbGatewayService imdbGatewayService;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    QuizzRepository quizzRepository;

    @Value("${api.score.value}")
    private Long value = 3L;

    @Value("${api.score.maximunAttempts}")
    private Long maximunAttempts = 3L;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuizzService quizzService;

    @Autowired
    TitleRepository titleRepository;

    private static final String MAXIMUN_ATTEMPTS_MESSAGE = "Wrong answer! Max attempts exceeded, finished quiz.";
    private static final String CONTINUE_QUIZZ_MESSAGE = "Right answer!";

    @Override
    public RoundResponse getPair(String username) throws InterruptedException, IOException, GatewayException, NotFoundException, NotFinishedException, FinishedException {

        var quizz = quizzRepository.findByUser_usernameAndFinished(username, false).orElseThrow(
                () -> new NotFoundException("Not found opened quizz for User "+username+"!")
        );

        var openRound = roundRepository.existsByQuizz_UserAndFinished(quizz.getUser(), false);

        if (quizz.getFinished()) throw new FinishedException("This quizz is finished");

        if(openRound) {
            var round = roundRepository.findByQuizzAndFinished(quizz,false);
            return new RoundResponse(new TitlesResponse(round.getFirstTitle()), new TitlesResponse(round.getSecondTitle()));
        }

        var roundResponse = getRound();

        var firstTitleSaved = titleRepository.save(new Title(roundResponse.getTitle01()));
        var secondTitleSaved = titleRepository.save(new Title(roundResponse.getTitle02()));

        roundRepository.save(new Round(quizz, firstTitleSaved, secondTitleSaved));

        return roundResponse;
    }

    @Override
    public RoundResultResponse saveAnswer(RoundAnswerRequest roundAnswerRequest, String username) throws NotFoundException, FinishedException {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User with username "+username+" not found.")
        );

        var round = roundRepository.findByQuizz_UserAndFinished(user, false).orElseThrow(
                () -> new FinishedException("No open round found for the logged in user.")
        );

        var quizz = round.getQuizz();
        var rounds = roundRepository.findAllByQuizz(quizz);
        var rightAnswer =  round.getFirstTitle().getId();

        if (round.getSecondTitle().getRating() > round.getFirstTitle().getRating()) {
            rightAnswer = round.getSecondTitle().getId();
        }

        if (rightAnswer.equalsIgnoreCase(roundAnswerRequest.getTitleId())) {
            round.setAnsweredCorrectly(true);
            round.setFinished(true);
            roundRepository.save(round);

            quizz.setPoints(quizz.getPoints()+1);

            user.setScore(calculateScore(rounds, user));

            quizzRepository.save(quizz);
        } else {
            round.setAnsweredCorrectly(false);
            round.setFinished(true);
            roundRepository.save(round);

            user.setScore(calculateScore(rounds, user));

            quizz.setErrors(quizz.getErrors()+1);
            quizzRepository.save(quizz);

            if (quizz.getErrors() == maximunAttempts) quizzService.finish(username);

            return new RoundResultResponse(
                    rightAnswer,
                    MAXIMUN_ATTEMPTS_MESSAGE,
                    round,
                    maximunAttempts
            );
        }

        return new RoundResultResponse(
                rightAnswer,
                CONTINUE_QUIZZ_MESSAGE,
                round,
                maximunAttempts
        );
    }

    private RoundResponse getRound() throws InterruptedException, IOException, GatewayException {
        var genre = Genres.getRandom();
        var titlesGenre = new TitlesGenre(genre, imdbGatewayService.getTitlesByGenre(genre).getTitles());

        var firstTitle = titlesGenre.getRandom().replace("title", "").replace("/","");
        var secondTitle = titlesGenre.getRandom().replace("title", "").replace("/","");

        while (!isValidTitlesPair(firstTitle, secondTitle)) {
            secondTitle = titlesGenre.getRandom().replace("title", "").replace("/","");
        }

        var roundResponse = imdbGatewayService.getDetailsOfTitles(firstTitle, secondTitle);

        while (roundResponse.getTitle01().getRating().compareTo(roundResponse.getTitle02().getRating()) == 0) {
            secondTitle = titlesGenre.getRandom().replace("title", "").replace("/","");
            roundResponse = imdbGatewayService.getDetailsOfTitles(firstTitle, secondTitle);
        }

        return roundResponse;
    }

    private Boolean isValidTitlesPair(String firstTitleId, String secondTitleId) {
         if (firstTitleId.equals(secondTitleId)) return false;

         var existPairAB = roundRepository.existsByFirstTitle_idAndSecondTitle_id(firstTitleId, secondTitleId);
         var existPairBA = roundRepository.existsByFirstTitle_idAndSecondTitle_id(secondTitleId, firstTitleId);

         if (existPairAB && existPairBA) return false;

         return true;
    }

    private Long calculateScore(List<Round> rounds, User user) {
        var quizzAmount = quizzRepository.countByUser(user);

        var roundsAnsweredCorrectly = rounds.stream().filter(round -> round.getAnsweredCorrectly());
        var percentageRightAnswer = ((roundsAnsweredCorrectly.count()*100)/rounds.size());

        return (quizzAmount*percentageRightAnswer)+user.getScore();
    }
}
