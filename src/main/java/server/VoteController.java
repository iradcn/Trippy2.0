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
    vote.setPlace("place1");
    vote.setProperty("prop1");
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
