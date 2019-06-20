package org.launchcode.stlparks2.controllers;

import org.launchcode.stlparks2.models.Amenity;
import org.launchcode.stlparks2.models.Park;
import org.launchcode.stlparks2.models.data.AdminDao;
import org.launchcode.stlparks2.models.data.AmenityDao;
import org.launchcode.stlparks2.models.data.ParkDao;
import org.launchcode.stlparks2.models.forms.AddParkAmenitiesForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;




@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private ParkDao parkDao;

    @Autowired
    private AmenityDao amenityDao;

    @Autowired
    private AdminDao adminDao;




    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("title", "Admin Home");
        model.addAttribute("parks", parkDao.findAll());

        return "admin/index";
    }

    @RequestMapping(value = "add-park", method = RequestMethod.GET)
    public String displayAddPark(Model model) {

        model.addAttribute("title", "Add New Park");
        model.addAttribute(new Park());

        return "admin/add-park";
    }

    @RequestMapping(value = "add-park", method = RequestMethod.POST)
    public String processAddPark(@ModelAttribute @Valid Park newPark, Errors errors, Model model) {


        if (errors.hasErrors()) {
            model.addAttribute("title", "Add New Park");
            return "admin/add-park";
        }



        parkDao.save(newPark);
        return "redirect:view/" + newPark.getId();

    }


    @RequestMapping(value = "view/{parkId}", method = RequestMethod.GET)
    public String viewPark(Model model, @PathVariable int parkId) {
        Park newPark = parkDao.findById(parkId).orElse(null);
        model.addAttribute("park", newPark);
        model.addAttribute("title", "Add Successful!");


        return "admin/view-park";
    }

    @RequestMapping(value = "add-amenity", method = RequestMethod.GET)
    public String displayAddAmenity(Model model) {
        model.addAttribute("title", "Add New Amenity");
        model.addAttribute(new Amenity());

        return "admin/add-amenity";
    }

    @RequestMapping(value = "add-amenity", method = RequestMethod.POST)
    public String processAddAmenity(@ModelAttribute @Valid Amenity newAmenity, Errors errors, Model model) {


        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Amenity");
            return "admin/add-amenity";
        }

        amenityDao.save(newAmenity);
        return "redirect:add-amenity";
    }


    @RequestMapping(value = "/add-park-amenities/{parkId}", method = RequestMethod.GET)
    public String displayAddParkAmenities(Model model, @PathVariable int parkId) {

        Park park = parkDao.findById(parkId).orElse(null);
        AddParkAmenitiesForm form = new AddParkAmenitiesForm(park, amenityDao.findAll());
        model.addAttribute("title", "Add Amenities for Park " + park.getName());
        model.addAttribute("form", form);

        return "admin/add-park-amenities";
    }

    @RequestMapping(value = "add-park-amenities", method = RequestMethod.POST)
    public String processAddItem(@ModelAttribute AddParkAmenitiesForm form, Model model) {


        Park park = parkDao.findById(form.getParkId()).orElse(null);
        Amenity amenity = amenityDao.findById(form.getAmenityId()).orElse(null);
        park.addAmenity(amenity);
        parkDao.save(park);

        return "redirect:view/" + park.getId();

    }

    @RequestMapping(value = "delete-park", method = RequestMethod.GET)
    public String displayDeletePark(Model model) {
        model.addAttribute("parks", parkDao.findAllByOrderByNameAsc());
        model.addAttribute("title", "Delete Park");

        return "admin/delete-park";
    }

    @RequestMapping(value = "delete-park", method = RequestMethod.POST)
    public String processDeletePark(@RequestParam int[] parkIds) {

        for (int parkId : parkIds) {
            Park park = parkDao.findById(parkId).orElse(null);


            parkDao.delete(park);
        }

        return "redirect:/admin/delete-park";
    }


    @RequestMapping(value = "delete-amenity", method = RequestMethod.GET)
    public String displayDeleteAmenity(Model model) {
        model.addAttribute("amenities", amenityDao.findAllByOrderByNameAsc());
        model.addAttribute("title", "Delete Amenities");


        return "admin/delete-amenity";
    }

    @RequestMapping(value = "delete-amenity", method = RequestMethod.POST)
    public String processDeleteAmenity(@RequestParam int[] amenityIds) {

        for (int amenityId : amenityIds) {
            Amenity amenity = amenityDao.findById(amenityId).orElse(null);


            amenityDao.delete(amenity);
        }

        return "redirect:/admin/delete-amenity";


    }
}
