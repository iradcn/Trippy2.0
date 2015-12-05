package server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import model.Property;
import model.Vote;

import model.VoteAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import protocol_model.QuestionAndResults;
import protocol_model.SearchByLocation;
import services.QuestionsGeneratorService;

@RestController
public class VoteController {

    @Autowired
    QuestionsGeneratorService questionsGeneratorService;

	@RequestMapping(value="app/vote/request", method=RequestMethod.GET)
	public @ResponseBody Vote getSomeStuff() throws Exception {
        Vote vote = new Vote();
        vote.setPlaceId("929200_1571840069710538_1151072500_n");
        vote.setPlaceName("Room Service");

        // Todo - call questionsGeneratorService
        //vote.setProperty(questionsGeneratorService.generate(placeId));
        String[] properties = new String[3];
        properties[0] = "Full with beautiful girls";
        properties[1] = "Good for friday afternoon drink";
        properties[2] = " Serve good cocktails";

        vote.setProperty(properties);

        // Todo - Fetch image for the place (DB or http query)
        //File initialFile = new File("src/main/resources/929200_1571840069710538_1151072500_n.jpg");
        //InputStream targetStream = Files.asByteSource(initialFile).openStream();
        //BufferedImage img = ImageIO.read(target Stream);
        //vote.setPlaceImage(getDataFromBufferedImage(img));
        //QuestionsGeneratorService questionsGeneratorService = new QuestionsGeneratorService();
       // long propId = questionsGeneratorService.generate(10096); 
        return vote;

	}

    @RequestMapping(value="app/vote/answer", method=RequestMethod.GET)
    public QuestionAndResults answerVoteQuestion(@RequestParam("voteAnswer") VoteAnswer answer,
                                                 @RequestParam("searchQuery") SearchByLocation searchQueryJson) throws SQLException {

        // Save the

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //Property.AddPropToPlace(placeId, propId, voteValue, username, placenId);

        // todo - delete open qustion
        return null;

    }

    private byte[] getDataFromBufferedImage(BufferedImage thumbnail) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(thumbnail, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } finally {
            baos.close();
        }
    }
}
