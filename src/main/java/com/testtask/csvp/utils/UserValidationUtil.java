package com.testtask.csvp.utils;

import com.testtask.csvp.domains.User;
import com.testtask.csvp.enums.GenderType;
import com.testtask.csvp.enums.SymType;
import com.testtask.csvp.to.UserWTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UserValidationUtil {

    private static final Logger log = LoggerFactory.getLogger(UserValidationUtil.class);

    private static final String PREFIX_ERROR = "###ERROR###:'";
    private static final String EMPTY_ERROR = PREFIX_ERROR + "' Field shouldn't be NULL, empty or blank.";
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\p{No}\\s]+$");
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
    private static final Pattern GENDER_PATTERN = Pattern.compile("^[FM]$");
    private static final Pattern ID_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{2,}$");
    private static final Pattern MSISDN_PATTERN = Pattern.compile("^(\\+)([0-9]{1,5})(\\d{9})$");

    public static Map<String, User> validateAndGetUsers(List<UserWTO> tos) {

        Map<String, User> validUsers = new LinkedHashMap<>();

        for (UserWTO to : tos) {

            String msisdn = to.getMsisdn();

            log.debug(String.format("%sNumber %s. Status - VALIDATION.%s", "\u001b[0;34m", msisdn, "\u001b[m"));

            validateMsisdn(to, validUsers, msisdn);
            validateSymType(to, msisdn);
            validateName(to, msisdn);
            validatedDateOfBirth(to, msisdn);
            validateGender(to, msisdn);
            validateAddress(to, msisdn);
            validateId(to, msisdn);

            if (to.isValid()) {
                User validUser = createUser(to);
                validUsers.put(validUser.getMsisdn(), validUser);
            }
        }

        return validUsers;
    }

    private static User createUser(UserWTO to) {
        User user = new User();
        user.setMsisdn(to.getMsisdn());
        user.setSymType(to.getSymType().toUpperCase());
        user.setName(to.getName());
        user.setDateOfBirth(LocalDate.parse(to.getDateOfBirth()));
        user.setGender(to.getGender().toUpperCase());
        user.setAddress(to.getAddress());
        user.setIdName(to.getId());

        return user;
    }

    private static void validateMsisdn(UserWTO to, Map<String, User> validUsers, String msisdn) {
        if (StringUtils.isBlank(msisdn)) {
            to.setMsisdn(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getMsisdn(), "\u001b[m"));
            return;
        }
        if (!MSISDN_PATTERN.matcher(msisdn).matches()) {
            to.setMsisdn(PREFIX_ERROR + msisdn + "' MSISDN should comply to country's standard (e.g. +66).");
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getMsisdn(), "\u001b[m"));
            return;
        }
        if (validUsers.containsKey(msisdn)) {
            to.setMsisdn(PREFIX_ERROR + msisdn + "' MSISDN can't be duplicated.");
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getMsisdn(), "\u001b[m"));
        }
    }

    private static void validateSymType(UserWTO to, String msisdn) {
        String symType = to.getSymType().toUpperCase();
        if (StringUtils.isBlank(symType)) {
            to.setSymType(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getSymType(), "\u001b[m"));
            return;
        }
        if (!symType.equals("PREPAID") && !symType.equals("POSTPAID")) {
            to.setSymType(PREFIX_ERROR + symType + "' SIM_TYPE can only be PREPAID or POSTPAID.");
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getSymType(), "\u001b[m"));
        }
    }

    private static void validateName(UserWTO to, String msisdn) {
        String name = to.getName();
        if (StringUtils.isBlank(name)) {
            to.setName(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getName(), "\u001b[m"));
            return;
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            to.setName(PREFIX_ERROR + name + "' NAME shouldn't have any special character.");
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getName(), "\u001b[m"));
        }
    }

    private static void validatedDateOfBirth(UserWTO to, String msisdn) {
        String stringDate = to.getDateOfBirth();
        if (StringUtils.isBlank(stringDate)) {
            to.setDateOfBirth(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getDateOfBirth(), "\u001b[m"));
            return;
        }
        if (!DATE_PATTERN.matcher(stringDate).matches()) {
            to.setDateOfBirth(PREFIX_ERROR + stringDate + "' DATE_OF_BIRTH must be written in the format such as 2020-01-27.");
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getDateOfBirth(), "\u001b[m"));
            return;
        }

        LocalDate date = LocalDate.parse(to.getDateOfBirth());
        LocalDate now = LocalDate.now();

        if (!date.isBefore(now)) {
            to.setDateOfBirth(PREFIX_ERROR + stringDate + "' DATE_OF_BIRTH shouldn't be TODAY or FUTURE.");
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getDateOfBirth(), "\u001b[m"));
            to.setValid(false);
        }
    }

    private static void validateGender(UserWTO to, String msisdn) {
        String gender = to.getGender().toUpperCase();
        if (StringUtils.isBlank(gender)) {
            to.setGender(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getGender(), "\u001b[m"));
            return;
        }
        if (!GENDER_PATTERN.matcher(gender).matches()) {
            to.setGender(PREFIX_ERROR + gender + "' GENDER can only be 'F' or 'M'.");
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getGender(), "\u001b[m"));
            to.setValid(false);
        }
    }

    private static void validateAddress(UserWTO to, String msisdn) {
        String address = to.getAddress();
        if (StringUtils.isBlank(address)) {
            to.setAddress(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getAddress(), "\u001b[m"));
            return;
        }
        if (address.length() < 20) {
            to.setAddress(PREFIX_ERROR + address + "' ADDRESS must at least be 20 characters long.");
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getAddress(), "\u001b[m"));
            to.setValid(false);
        }
    }

    private static void validateId(UserWTO to, String msisdn) {
        String id = to.getId();
        if (StringUtils.isBlank(id)) {
            to.setId(EMPTY_ERROR);
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getId(), "\u001b[m"));
            return;
        }
        if (!ID_PATTERN.matcher(id).matches()) {
            to.setId(PREFIX_ERROR + id + "' ID_NUMBER should be a mix of characters & numbers.");
            to.setValid(false);
            log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getId(), "\u001b[m"));
        }
    }

    public static void validateUniqueUsers(List<UserWTO> tos, Map<String, User> validUsers, List<String> existingMSISDS) {
        for (UserWTO to : tos) {
            String msisdn = to.getMsisdn();

            if (existingMSISDS.contains(msisdn)) {
                validUsers.remove(msisdn);

                to.setMsisdn(PREFIX_ERROR + msisdn + "' MSISDN can't be duplicated.");
                to.setValid(false);
                log.debug(String.format("%sNumber %s. Status - VALIDATION_ERROR. %s%s", "\u001b[0;31m", msisdn, to.getMsisdn(), "\u001b[m"));
            }

            if (to.isValid()) {
                log.debug(String.format("%sNumber %s. Status - VALIDATION_SUCCESSFUL.%s", "\u001b[0;32m", msisdn, "\u001b[m"));
            }
        }
    }

}
