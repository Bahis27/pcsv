package com.testtask.csvp.utils;

import com.testtask.csvp.domains.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FSUtil {

    private static final Logger log = LoggerFactory.getLogger(FSUtil.class);

    public static void write(Map<String, User> validUsers) {

        for (User user : validUsers.values()) {

            String msisdn = user.getMsisdn();

            log.debug(String.format("%sNumber %s. Status - WRITE.%s", "\u001b[0;34m", msisdn, "\u001b[m"));

            try(FileWriter writer = new FileWriter(msisdn, false))
            {
                writer.write(user.toString());
                writer.flush();
            }
            catch(IOException ex){
                log.error(String.format("%sNumber - %s. Status - WRITE_FAILURE!%s", "\u001b[0;31m", msisdn, "\u001b[m"));
            }

            log.debug(String.format("%sNumber - %s. Status - WRITE_SUCCESSFUL!%s", "\u001b[0;32m", msisdn, "\u001b[m"));
        }

    }
}
