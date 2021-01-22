package com.testtask.csvp.controllers;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.testtask.csvp.domains.User;
import com.testtask.csvp.services.UserService;
import com.testtask.csvp.to.UserWTO;
import com.testtask.csvp.utils.FSUtil;
import com.testtask.csvp.utils.SMSUtil;
import com.testtask.csvp.utils.UserValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `UserWTO` (Web Transport Object)
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<UserWTO> csvToBean = new CsvToBeanBuilder<UserWTO>(reader)
                        .withType(UserWTO.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<UserWTO> tos = csvToBean.parse();

                // get valid users
                Map<String, User> validUsers = UserValidationUtil.validateAndGetUsers(tos);

                // load existing users
                List<User> existingUsers = userService.getUsersByMsisds(validUsers.keySet());
                List<String> existingMSISDS = existingUsers
                        .stream()
                        .map(User::getMsisdn)
                        .collect(Collectors.toList());

                // remove existing users from request
                UserValidationUtil.validateUniqueUsers(tos, validUsers, existingMSISDS);

                // save users to DB
                userService.saveUsers(validUsers.values());

                //TFor each validated record, save a new file with name as '<MSISDN>.txt' and put all the info there
                FSUtil.write(validUsers);

                //Send welcome SMS to new MSISDN numbers
                SMSUtil.sendSms(validUsers);


                // save users list on model
                model.addAttribute("users", tos);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }
}
