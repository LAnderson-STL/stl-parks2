package org.launchcode.stlparks2.controllers;

import org.launchcode.stlparks2.models.Park;
import org.launchcode.stlparks2.models.User;
import org.launchcode.stlparks2.models.data.ParkDao;
import org.launchcode.stlparks2.models.data.UserDao;
import org.launchcode.stlparks2.models.forms.AddParkToProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("user")

public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ParkDao parkDao;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    private Cookie cookie;




    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String displayProfilePage(Model model, @PathVariable int userId) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "redirect:/";
        }

        String currentCookieName = WebUtils.getCookie(request, "name").getValue();
        User currentUser = userDao.findByUserName(currentCookieName);

        User userFromPathVar = userDao.findById(userId).orElse(null);
        if (!userFromPathVar.getUserName().equals(currentCookieName)) {
            return "redirect:/";
        }

        for (User userx : userDao.findAll()) {
            if (userx.getUserName().toLowerCase().equals(currentUser.getUserName().toLowerCase())) {
                User user = userDao.findById(userId).orElse(null);

                List<Park> availableParks = new ArrayList<>();
                for (Park park : parkDao.findAllByOrderByNameAsc()) {
                    if (!user.getParks().contains(park)) {
                        availableParks.add(park);
                    }
                }

                AddParkToProfileForm form = new AddParkToProfileForm(user, availableParks);
                model.addAttribute("title", "MyParks");
                model.addAttribute("user", user);
                model.addAttribute("form", form);
                model.addAttribute("parks", user.getParks());
                //model.addAttribute("profileParks", user.getParks());

                return "user/profile-page";
            }
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public String processAddPark(AddParkToProfileForm form, @RequestParam int parkId, int userId, Model model) {

        model.addAttribute("title", "Remove Park");
        User user = userDao.findById(form.getUserId()).orElse(null);
        Park park = parkDao.findById(parkId).orElse(null);
        user.addPark(park);
        userDao.save(user);

        return "redirect:" + user.getId();
    }


    @RequestMapping(value = "delete-park/{userId}", method = RequestMethod.GET)
    public String displayDeletePark(Model model, @PathVariable int userId) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "redirect:/";
        }

        String currentCookieName = WebUtils.getCookie(request, "name").getValue();
        User currentUser = userDao.findByUserName(currentCookieName);

        User userFromPathVar = userDao.findById(userId).orElse(null);
        if (!userFromPathVar.getUserName().equals(currentCookieName)) {
            return "redirect:/";
        }

        for (User userx : userDao.findAll()) {
            if (userx.getUserName().toLowerCase().equals(currentUser.getUserName().toLowerCase())) {
                User user = userDao.findById(userId).orElse(null);
                AddParkToProfileForm form = new AddParkToProfileForm(user, parkDao.findAllByOrderByNameAsc());
                model.addAttribute("user", user);
                model.addAttribute("title", "Remove Park");

                return "user/delete-park";
            }
        }
        return "redirect:/";
    }


    @RequestMapping(value = "delete-park/{userId}", method = RequestMethod.POST)
    public String processDeletePark(Model model, @PathVariable int userId, @RequestParam int[] parkIds) {
        User user = userDao.findById(userId).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("title", "Remove Park");
        for (int parkId : parkIds) {
            user.removePark(parkDao.findById(parkId).orElse(null));
        }
        userDao.save(user);
        return "user/delete-park";
    }


    @RequestMapping(value = "profile-link", method = RequestMethod.GET)
    public String followProfileLink() {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return "redirect:/";
        }

        String currentCookieName = WebUtils.getCookie(request, "name").getValue();

        User currentUser = userDao.findByUserName(currentCookieName);
        for (User user : userDao.findAll()) {
            if (user.getUserName().toLowerCase().equals(currentUser.getUserName().toLowerCase())) {

                return "redirect:/user/" + currentUser.getId();
            }

        }
        return "redirect:/";
    }

    @RequestMapping(value = "remove-link", method = RequestMethod.GET)
    public String followRemoveLink() {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return "redirect:/";
        }

        String currentCookieName = WebUtils.getCookie(request, "name").getValue();

        User currentUser = userDao.findByUserName(currentCookieName);
        for (User user : userDao.findAll()) {
            if (user.getUserName().toLowerCase().equals(currentUser.getUserName().toLowerCase())) {

                return "redirect:/user/delete-park/" + currentUser.getId();
            }
        }
        return "redirect:/";
    }


    @RequestMapping(value = "logout")
    public String logOut() {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "redirect:/";
        }

        cookie = new Cookie("name", "");
        cookie.setMaxAge(0);
        cookie.setPath("/user");
        response.addCookie(cookie);
        return "redirect:/";
    }
}