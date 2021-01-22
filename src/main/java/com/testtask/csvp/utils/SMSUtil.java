package com.testtask.csvp.utils;

import com.testtask.csvp.domains.User;
import com.testtask.csvp.enums.GenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SMSUtil {

    private static final Logger log = LoggerFactory.getLogger(SMSUtil.class);

    public static void sendSms(Map<String, User> validUsers) {
        validUsers.forEach((msisdn, user) -> sendSms(msisdn, GenderType.valueOf(user.getGender()), user.getName()));
    }

    public static void sendSms(String msisdn, GenderType gender, String name) {
        log.debug(String.format("%sNumber %s. Status - SEND.%s", "\u001b[0;34m", msisdn, "\u001b[m"));
        log.debug(String.format("%sHello %s %s, Number %s was registered!%s", "\u001b[0;33m", gender == GenderType.M ? "Mr" : "Mrs", name, msisdn, "\u001b[m"));
        log.debug(String.format("%sNumber - %s. Status - SEND_SUCCESSFUL!%s", "\u001b[0;32m", msisdn, "\u001b[m"));
    }
}
