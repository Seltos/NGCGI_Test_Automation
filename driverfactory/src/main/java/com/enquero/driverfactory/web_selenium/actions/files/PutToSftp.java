package com.enquero.driverfactory.web_selenium.actions.files;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.enquero.driverfactory.web_selenium.base.TestAction;

import java.io.File;
import java.io.FileInputStream;

public class PutToSftp extends TestAction {

    @Override
    public void run() {
        super.run();

        String sftpHost = this.readStringArgument("sftpHost");
        Integer sftpPort = this.readIntArgument("sftpPort", 22);
        String userName = this.readStringArgument("userName");
        String password = this.readStringArgument("password");
        String sourceDir = this.readStringArgument("sourceDir");
        String sourceFileName = this.readStringArgument("sourceFile");
        String destinationDir = this.readStringArgument("destinationDir");
        String destinationFileName = this.readStringArgument("destinationFile", sourceFileName);

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(userName, sftpHost, sftpPort);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            this.log.trace("Connected to SFTP host");
            
            channel = session.openChannel("sftp");
            channel.connect();
            this.log.trace("The SFTP channel was opened and connected");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(destinationDir);
            
            File sourceFile = new File(sourceDir, sourceFileName);
            FileInputStream inputStream = new FileInputStream(sourceFile);
            channelSftp.put(inputStream, destinationFileName);
            inputStream.close();
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