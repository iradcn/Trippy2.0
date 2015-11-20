package server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.Files;
import model.Vote;

import org.springframework.web.bind.annotation.*;

import services.QuestionsGeneratorService;

import javax.imageio.ImageIO;

@RestController
public class VoteController {

    //@Autowired
    QuestionsGeneratorService questionsGeneratorService;

	@RequestMapping(value="app/vote/request", method=RequestMethod.GET)
	public @ResponseBody Vote getSomeStuff() throws Exception {
        Vote vote = new Vote();
        vote.setPlaceId("place1");
        vote.setPlaceName("Room Service");

        // Todo - call questionsGeneratorService
        //vote.setProperty(questionsGeneratorService.generate(placeId));
        String[] properties = new String[3];
        properties[0] = "Full with beautiful girls";
        properties[1] = "Good for friday afternoon drink";
        properties[2] = " Serve good cocktails";

        vote.setProperty(properties);

        // Todo - Fetch image for the place (DB or http query)
        File initialFile = new File("src/main/resources/929200_1571840069710538_1151072500_n.jpg");
        InputStream targetStream = Files.asByteSource(initialFile).openStream();
        BufferedImage img = ImageIO.read(targetStream);
        vote.setPlaceImage(getDataFromBufferedImage(img));
        return vote;

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
