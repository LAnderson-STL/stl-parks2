package org.launchcode.stlparks2.controllers;

import org.launchcode.stlparks2.models.Park;
import org.launchcode.stlparks2.models.User;
import org.launchcode.stlparks2.models.data.ParkDao;
import org.launchcode.stlparks2.models.data.UserDao;
import org.launchcode.stlparks2.models.forms.AddParkToProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")

public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ParkDao parkDao;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String displayAddUser(Model model) {

        model.addAttribute("title", "New User Registration");
        model.addAttribute(new User());

        return "user/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String processAddUser(Model model, @RequestParam String userName, String password, String verifyPassword) {
        //TODO: error handling

            User newUser = new User(userName, password);

            if (newUser.getUserName().length() < 5 || newUser.getUserName().length() > 15){
                model.addAttribute("title", "register");
                model.addAttribute("userNameError", "Username must be 5 - 15 characters");
                model.addAttribute("password", password);
                return "user/register";
            }


            for (User user : userDao.findAll()){
                if (user.getUserName().equals(newUser.getUserName())){
                    model.addAttribute("title", "register");
                    model.addAttribute("userNameError", "Username already exists");
                    model.addAttribute("userName", userName);
                    return "user/register";
                }
            }

            if (password.length() < 5 || password.length() > 15){
                model.addAttribute("title", "register");
                model.addAttribute("passwordError", "Password must be 5 - 15 characters");
                model.addAttribute("userName", userName);
                return "user/register";
            }

            if (!password.equals(verifyPassword)){
                model.addAttribute("title", "register");
                model.addAttribute("verifyPasswordError", "Passwords do not match");
                model.addAttribute("userName", userName);
                return "user/register";
            }

            userDao.save(newUser);

            return "redirect:" + newUser.getId();

    }




    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String displayProfilePage(Model model, @PathVariable int userId){

        User user = userDao.findById(userId).orElse(null);
        AddParkToProfileForm form = new AddParkToProfileForm(user, parkDao.findAllByOrderByNameAsc());
        model.addAttribute("title", "My Park Page");
        model.addAttribute("user", user);
        model.addAttribute("form", form);
        model.addAttribute("parks", user.getParks());
        //model.addAttribute("profileParks", user.getParks());

        return "user/profile-page";
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String processAddPark(@ModelAttribute User user, AddParkToProfileForm form, Model model) {


        //User user = userDao.findById(form.getUserId()).orElse(null);
        Park park = parkDao.findById(form.getParkId()).orElse(null);
        user.addPark(park);
        userDao.save(user);


        return "redirect:" + user.getId();


    }





}