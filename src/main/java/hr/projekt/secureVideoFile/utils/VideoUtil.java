package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.constants.VideoConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoUtil {

    public static void imagesToVideo(List<BufferedImage> frames, String videoName) throws Exception {

        //check if folder for temporary video storage exists
        File pathToTmpStorage = new File(VideoConstants.TMP_STORAGE);

        if (!pathToTmpStorage.exists()) {
            pathToTmpStorage.mkdirs();
        }

        // Create an AWTSequenceEncoder to encode the video frames
        AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(new File(VideoConstants.TMP_STORAGE + videoName), 25);

        // Encode each frame and add it to the video
        for (BufferedImage frame : frames) {
            encoder.encodeImage(frame);
        }

        // Finalize the video encoding and close the encoder
        encoder.finish();
    }

    public static List<BufferedImage> videoToImages(String videoPath) throws Exception {
        List<BufferedImage> frames = new ArrayList<>();

        // Open the video file using JCodec
        SeekableByteChannel channel = NIOUtils.readableFileChannel(videoPath);
        FrameGrab grab = FrameGrab.createFrameGrab(channel);

        // Decode each frame and add it to the list
        Picture picture;
        while ((picture = grab.getNativeFrame()) != null) {
            BufferedImage frame = AWTUtil.toBufferedImage(picture);
            frames.add(frame);
        }

        // Close the video file and return the list of frames
        NIOUtils.closeQuietly(channel);
        return frames;
    }

}
