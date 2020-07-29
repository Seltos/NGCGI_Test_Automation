package com.enquero.driverfactory.web_selenium.actions.files;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.enquero.driverfactory.web_selenium.base.TestAction;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class GetFromSftp extends TestAction {

    @Override
    public void run() {
        super.run();

        String sftpHost = this.readStringArgument("sftpHost");
        Integer sftpPort = this.readIntArgument("sftpPort", 22);
        String userName = this.readStringArgument("userName");
        String password = this.readStringArgument("password");
        String sourceDir = this.readStringArgument("sourceDir");
        String sourceFile = this.readStringArgument("sourceFile");
        String destinationDir = this.readStringArgument("destinationDir");
        String destinationFileName = this.readStringArgument("destinationFile", sourceFile);

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(userName, sftpHost, sftpPort);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            this.log.trace("Connected to SFTP host");
            
            channel = session.openChannel("sftp");
            channel.connect();
            this.log.trace("The SFTP channel was opened and connected");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(sourceDir);
            
            File destinationFile = new File(destinationDir, destinationFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
            channelSftp.get(sourceFile, fileOutputStream);
            
            fileOutputStream.close();
        } catch (Exception ex) {
            throw new RuntimeException("SFTP transfer failed", ex);
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}