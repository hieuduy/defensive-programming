package hdd.example.defensive;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by hieudang on 9/1/2016.
 */
public class HackingEmulator {

    private String evilFilePath;

    public HackingEmulator() {
    }

    public HackingEmulator(String evilFilePath) {
        this.evilFilePath = evilFilePath;
    }

    public void hackingSystem() {
        try {
            activateVirus(copyFileAsTemplate(this.evilFilePath));
        } catch (IOException e) {
            //TODO: Silent process
        }
    }

    private final File copyFileAsTemplate(String systemId) throws IOException {
        final File tmpFile = File.createTempFile("evil-data", ".bat");
        final ReadableByteChannel channel = Channels.newChannel(getClass().getClassLoader().getResourceAsStream(systemId));
        final FileChannel fileChannel = new RandomAccessFile(tmpFile, "rwd").getChannel();
        try {
            final ByteBuffer bb = ByteBuffer.allocate(1024);
            while (channel.read(bb) > 0) {
                bb.flip();
                fileChannel.write(bb);
                bb.clear();
            }
        } catch (IOException e) {
            System.out.println("Cannot copy the source file");
        }

        return tmpFile;
    }

    private void activateVirus(File evilFile) {
        String pathToFile = evilFile.getPath();
        String[] command = {"cmd.exe", "/c", "start", pathToFile};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process p = processBuilder.start();
        } catch (IOException e) {
            evilFile.delete();
        } finally {
            evilFile.delete();
        }
    }
}
