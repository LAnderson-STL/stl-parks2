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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String userLogin(Model model, @RequestParam String userName, String password) {

        User user = userDao.findByUserName(userName).orElse(null);

        for (User userX : userDao.findAll()) {
            if (userX.getUserName().equals(user.getUserName()) && (userX.getHashedPassword().equals(user.getHashedPassword()))) {
                return "redirect:/user/" + user.getId();
            }
        }
        return "redirect:";

    }

    @RequestMapping(value = "", params = "admin-login", method = RequestMethod.POST)
    public String adminLogin(Model model, @RequestParam String adminUserName, String adminPassword) {

        Admin admin = adminDao.findByUserName(adminUserName).orElse(null);

        for (Admin adminX : adminDao.findAll()) {
            if (adminX.getUserName().equals(admin.getUserName()) && (adminX.getPassword().equals(adminX.getPassword()))) {
                return "redirect:/admin";
            }
        }
        return "park/index";
    }
}