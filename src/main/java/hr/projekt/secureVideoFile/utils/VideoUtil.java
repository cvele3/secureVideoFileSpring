package hr.projekt.secureVideoFile.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoUtil {

    public static void imagesToVideo(List<BufferedImage> frames, String outputPath) throws Exception {
        // Create an AWTSequenceEncoder to encode the video frames
        AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(new File(outputPath), 25);

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
