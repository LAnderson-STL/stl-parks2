package org.launchcode.stlparks2.controllers;

import org.launchcode.stlparks2.models.Admin;
import org.launchcode.stlparks2.models.Amenity;
import org.launchcode.stlparks2.models.Park;
import org.launchcode.stlparks2.models.User;
import org.launchcode.stlparks2.models.data.AdminDao;
import org.launchcode.stlparks2.models.data.AmenityDao;
import org.launchcode.stlparks2.models.data.ParkDao;
import org.launchcode.stlparks2.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private ParkDao parkDao;

    @Autowired
    private AmenityDao amenityDao;

    @Autowired
    UserDao userDao;

    @Autowired
    AdminDao adminDao;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    private Cookie cookie;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("amenities", amenityDao.findAll());

        return "park/index";
    }

    @RequestMapping(value = "show-parks", method = RequestMethod.GET)
    public String showParks(Model model) {

        model.addAttribute("parks", parkDao.findAllByOrderByNameAsc());

        return "park/show-parks";
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String showSearch(Model model) {


        model.addAttribute("title", "Search by Amenity");
        model.addAttribute("amenities", amenityDao.findAllByOrderByNameAsc());

        return "park/search";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String processSearch(Model model, @RequestParam ArrayList<Integer> amenityIds) {


        ArrayList<Park> foundParks = new ArrayList<>();

        for (Park park : parkDao.findAll()) {
            ArrayList<Integer> parkAmenityIds = new ArrayList<>();
            for (Amenity amenity : park.getAmenities()) {
                parkAmenityIds.add(amenity.getId());
            }

            if (parkAmenityIds.containsAll(amenityIds)) {
                foundParks.add(park);
            }

        }

        model.addAttribute("parks", foundParks);

        return "park/show-parks";

    }

    @RequestMapping(value = "view/{parkId}", method = RequestMethod.GET)
    public String viewPark(Model model, @PathVariable int parkId) {
        Park park = parkDao.findById(parkId).orElse(null);
        model.addAttribute("park", park);
        model.addAttribute("title", "Park Info");


        return "park/view-park";
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public String userLogin(Model model, @RequestParam String userName, String password) {

        User user = userDao.findByUserName(userName);
        if (user == null) {
            model.addAttribute("loginError", "Username does not exist");
            return "park/index";
        }

        if (user.getSHA256(password).equals(user.getHashedPassword())) {
            cookie = new Cookie("name", user.getUserName());
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/user");
            response.addCookie(cookie);
            return "redirect:/user/" + user.getId();
        }

        model.addAttribute("loginError", "Username and password do not match");
        return "redirect:";

    }

    @RequestMapping(value = "", params = "admin-login", method = RequestMethod.POST)
    public String adminLogin(Model model, @RequestParam String adminUserName, String adminPassword){
        Admin admin = adminDao.findByUserName(adminUserName);

        if (admin == null ) {
           model.addAttribute("adminUserNameError", "Username does not exist");
            return "redirect:";
        }

        String hashedPassword = admin.getSHA256(adminPassword);
        if (hashedPassword.equals(admin.getPassword())) {
            cookie = new Cookie("name", admin.getUserName());
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/admin");
            response.addCookie(cookie);
            HttpSession session = request.getSession();
            session.setAttribute("name", admin.getUserName());



            return "redirect:/admin";
        }
        //TODO: fix error messages
        model.addAttribute("adminPasswordError", "Username and password do not match");
        return "park/index";

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String displayAddUser(Model model) {

        model.addAttribute("title", "New User Registration");
        model.addAttribute(new User());

        return "park/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processAddUser(Model model, @RequestParam String userName, String password, String verifyPassword) {
        //TODO: error handling

        User newUser = new User(userName, password);

        if (newUser.getUserName().length() < 5 || newUser.getUserName().length() > 15){
            model.addAttribute("title", "register");
            model.addAttribute("userNameError", "Username must be 5 - 15 characters");
            model.addAttribute("password", password);
            return "park/register";
        }


        for (User user : userDao.findAll()){
            if (user.getUserName().equals(newUser.getUserName())){
                model.addAttribute("title", "register");
                model.addAttribute("userNameError", "Username already exists");
                model.addAttribute("userName", userName);
                return "park/register";
            }
        }

        if (password.length() < 5 || password.length() > 15){
            model.addAttribute("title", "register");
            model.addAttribute("passwordError", "Password must be 5 - 15 characters");
            model.addAttribute("userName", userName);
            return "park/register";
        }

        if (!password.equals(verifyPassword)){
            model.addAttribute("title", "register");
            model.addAttribute("verifyPasswordError", "Passwords do not match");
            model.addAttribute("userName", userName);
            return "park/register";
        }

        cookie = new Cookie("name", newUser.getUserName());
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/user");
        response.addCookie(cookie);

        userDao.save(newUser);

        return "redirect:/user/" + newUser.getId();

    }


    @RequestMapping(value = "logout")
    public String logout() {
        cookie = new Cookie("userName", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }




}